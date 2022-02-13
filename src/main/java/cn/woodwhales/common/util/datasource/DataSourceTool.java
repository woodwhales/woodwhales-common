package cn.woodwhales.common.util.datasource;

import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.example.model.util.datasource.DataSourceIgnore;
import com.google.common.base.CaseFormat;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author woodwhales on 2021-01-28 21:22
 * 数据库查询工具
 */
@Slf4j
public class DataSourceTool {

    /**
     * 数据库驱动类名
     */
    private String driverClass;

    /**
     * 数据库链接
     */
    private String url;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 链接对象
     */
    private Connection connection;

    /**
     * key 数据对象的类对象
     * value TargetInfo 对象（数据对象的类对象、数据对象的所有属性映射关系）
     */
    private Map<Class, LinkedHashMap<Field, ColumnDict>> dbColumnMapping = new HashMap<>(16);


    public static DataSourceTool newMysql(String url, String username, String password) {
        return new DataSourceTool("com.mysql.jdbc.Driver", url, username, password);
    }

    public static DataSourceTool newMysql8(String url, String username, String password) {
        return new DataSourceTool("com.mysql.cj.jdbc.Driver", url, username, password);
    }

    public static DataSourceTool newOracle(String url, String username, String password) {
        return new DataSourceTool("oracle.jdbc.OracleDriver", url, username, password);
    }

    /**
     * 包装字段值
     * 例如：
     * 数字 2 = '2'
     * 空值 null = 'NULL'
     * 字符串 "woodwhales" = 'woodwhales'
     *
     * @param object 要包装的数据
     * @return 使用单引号包装后的字符串数据
     */
    public static String wrappedField(Object object) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        if (Objects.isNull(object)) {
            sb.append("NULL");
        } else {
            sb.append(object);
        }
        sb.append("'");
        return sb.toString();
    }

    /**
     * 格式化 sql 语句
     *
     * @param sql  原始 sql
     * @param args 占位符对应的参数
     * @return 已格式化的 sql 语句
     */
    public static String formatSql(String sql, Object... args) {
        if (Objects.isNull(args)) {
            return sql;
        }

        List<String> wrappedArgs = new ArrayList<>(args.length);
        for (Object arg : args) {
            wrappedArgs.add(wrappedField(arg));
        }
        return format(sql, wrappedArgs.toArray());
    }

    private static String format(String messagePattern, Object[] argArray) {
        if (messagePattern == null) {
            return null;
        } else if (argArray == null) {
            return messagePattern;
        } else {
            int i = 0;
            StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

            for (int L = 0; L < argArray.length; ++L) {
                int j = messagePattern.indexOf("{}", i);
                if (j == -1) {
                    if (i == 0) {
                        return messagePattern;
                    }

                    sbuf.append(messagePattern, i, messagePattern.length());
                    return sbuf.toString();
                }

                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        --L;
                        sbuf.append(messagePattern, i, j - 1);
                        sbuf.append('{');
                        i = j + 1;
                    } else {
                        sbuf.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                        i = j + 2;
                    }
                } else {
                    sbuf.append(messagePattern, i, j);
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                    i = j + 2;
                }
            }

            sbuf.append(messagePattern, i, messagePattern.length());
            return sbuf.toString();
        }
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
        } else {
            if (!o.getClass().isArray()) {
                safeObjectAppend(sbuf, o);
            } else if (o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sbuf, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sbuf, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sbuf, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sbuf, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sbuf, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[]) o);
            } else {
                objectArrayAppend(sbuf, (Object[]) o, seenMap);
            }

        }
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, (Object) null);
            int len = a.length;

            for (int i = 0; i < len; ++i) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }

            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }

        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable var3) {
            sbuf.append("Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
        }

    }

    private static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        } else {
            char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
            return potentialEscape == '\\';
        }
    }

    public DataSourceTool(String driverClass, String url, String username, String password) {
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            Class.forName(this.driverClass);
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
            this.connection = connection;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLException) {
                SQLException sqlException = (SQLException) cause;
                String sqlState = sqlException.getSQLState();
                if ("28000".equals(sqlState)) {
                    log.error("数据库账号或密码错误!!!");
                    System.err.println("数据库账号或密码错误!!!");
                }
            } else {
                log.error("链接数据库失败, cause by = {}", e.getMessage(), e);
                System.out.println("cause by = = " + e.getMessage());
            }
            System.exit(0);
        }

    }

    public <T> List<T> queryList(String sql, Class<T> clazz) throws Exception {
        return this.queryList(sql,
                resultSet -> this.cacheDbColumnMapping(clazz, resultSet),
                resultSet -> this.getDataFromResultSet(clazz, resultSet));
    }

    private <T> List<Field> getNeedFillFieldList(Class<T> clazz) {
        List<Field> needFillFieldList = null;
        if (!this.dbColumnMapping.containsKey(clazz)) {
            Field[] declaredFields = FieldUtils.getAllFields(clazz);
            needFillFieldList = new ArrayList<>(declaredFields.length);
            for (Field field : declaredFields) {
                if (Objects.isNull(field.getAnnotation(DataSourceIgnore.class))) {
                    needFillFieldList.add(field);
                }
            }
        }
        return needFillFieldList;
    }

    private void cacheDbColumnMapping(Class<?> clazz, ResultSet resultSet) {
        if (!this.dbColumnMapping.containsKey(clazz)) {
            List<Field> fieldList = this.getNeedFillFieldList(clazz);
            List<ColumnDict> dbColumnDictList = this.getDbColumnDictList(resultSet);
            this.dbColumnMapping.put(clazz, this.dbColumnMap(fieldList, dbColumnDictList));
        }
    }

    private List<ColumnDict> getDbColumnDictList(ResultSet resultSet) {
        List<ColumnDict> dbColumnDictList = new ArrayList<>();
        ResultSetMetaData metaData = this.getMetaData(resultSet);
        int columnCount = this.getColumnCount(metaData);
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            String columnName = this.getColumnName(metaData, columnIndex);
            String columnTypeName = this.getColumnTypeName(metaData, columnIndex);
            dbColumnDictList.add(new ColumnDict(columnIndex, columnName, columnTypeName));
        }
        return dbColumnDictList;
    }

    private LinkedHashMap<Field, ColumnDict> dbColumnMap(List<Field> fieldList, List<ColumnDict> dbColumnDictList) {
        Map<String, ColumnDict> dbColumnDictMap = DataTool.toMap(dbColumnDictList, ColumnDict::getColumnName);
        LinkedHashMap<Field, ColumnDict> result = new LinkedHashMap<>();
        for (Field field : fieldList) {
            String name = field.getName();
            if (dbColumnDictMap.containsKey(name)) {
                result.put(field, dbColumnDictMap.get(name));
            } else {
                // name 中含有大写的字母转小写，并使用下划线拼接
                String convertName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
                if (dbColumnDictMap.containsKey(convertName)) {
                    result.put(field, dbColumnDictMap.get(convertName));
                }
            }
        }

        return result;
    }

    public String getColumnTypeName(ResultSetMetaData metaData, int columnIndex) {
        String columnTypeName = null;
        try {
            columnTypeName = metaData.getColumnTypeName(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnTypeName;
    }

    public String getColumnName(ResultSetMetaData metaData, int columnIndex) {
        String columnName = null;
        try {
            columnName = metaData.getColumnName(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnName;
    }

    public int getColumnCount(ResultSetMetaData metaData) {
        int columnCount = 0;
        try {
            columnCount = metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnCount;
    }

    public ResultSetMetaData getMetaData(ResultSet resultSet) {
        ResultSetMetaData metaData = null;
        try {
            metaData = resultSet.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metaData;
    }

    public <T> T queryOne(String sql, Class<T> clazz) throws Exception {
        return this.queryOne(sql,
                resultSet -> this.cacheDbColumnMapping(clazz, resultSet),
                resultSet -> this.getDataFromResultSet(clazz, resultSet));
    }

    /**
     * 执行 DML SQL 操作
     *
     * @param sql 要执行的 sql
     * @return 影响行数
     */
    public int executeUpdate(String sql) {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public <T> T getDataFromResultSet(Class<T> clazz, ResultSet resultSet) {
        T target = null;


        try {
            if (!resultSet.next()) {
                return target;
            }
            target = clazz.newInstance();
            LinkedHashMap<Field, ColumnDict> fieldHashMap = this.dbColumnMapping.get(clazz);
            if (MapUtils.isNotEmpty(fieldHashMap)) {
                for (Map.Entry<Field, ColumnDict> entry : fieldHashMap.entrySet()) {
                    Field field = entry.getKey();
                    try {
                        Object object = this.getObject(resultSet, field, entry.getValue());
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        field.set(target, object);
                        field.setAccessible(accessible);
                    } catch (SQLException | IllegalAccessException e) {
                        throw new RuntimeException(String.format("cause by = %s, fieldName=%s, fieldType=%s, ColumnDict = %s",
                                e.getMessage(), field.getName(), field.getType().getName(), entry.getValue()));
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return target;
    }

    private Object getObject(ResultSet resultSet, Field field, ColumnDict columnDict) throws SQLException {
        // 属性类型的类名称
        Class<?> type = field.getType();

        Object object = null;

        if (type.isAssignableFrom(String.class)) {
            object = resultSet.getString(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {
            object = resultSet.getInt(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(Boolean.class)) {
            object = resultSet.getBoolean(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(Boolean.class)) {
            object = resultSet.getBoolean(columnDict.columnIndex);
        } else if (type.isAssignableFrom(BigDecimal.class)) {
            object = resultSet.getBigDecimal(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class)) {
            object = resultSet.getLong(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class)) {
            object = resultSet.getByte(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
            object = resultSet.getByte(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class)) {
            object = resultSet.getByte(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class)) {
            object = resultSet.getShort(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Byte[].class) || type.isAssignableFrom(byte[].class)) {
            object = resultSet.getBytes(columnDict.columnIndex);
        } else if (type.isAssignableFrom(Date.class)) {
            Timestamp timestamp = resultSet.getTimestamp(columnDict.columnIndex);
            if (Objects.nonNull(timestamp)) {
                object = new Date(timestamp.getTime());
            }
        } else if (type.isAssignableFrom(LocalDateTime.class)) {
            Timestamp timestamp = resultSet.getTimestamp(columnDict.columnIndex);
            if (Objects.nonNull(timestamp)) {
                object = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        } else if (type.isAssignableFrom(LocalDate.class)) {
            Timestamp timestamp = resultSet.getTimestamp(columnDict.columnIndex);
            if (Objects.nonNull(timestamp)) {
                object = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        } else if (type.isAssignableFrom(LocalTime.class)) {
            Timestamp timestamp = resultSet.getTimestamp(columnDict.columnIndex);
            if (Objects.nonNull(timestamp)) {
                object = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            }
        }
        return object;
    }

    @ToString
    private static class ColumnDict {
        public int columnIndex;
        public String columnName;
        public String columnTypeName;

        public ColumnDict(int columnIndex, String columnName, String columnTypeName) {
            this.columnIndex = columnIndex;
            this.columnName = columnName;
            this.columnTypeName = columnTypeName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

    /**
     * 查询多条数据
     *
     * @param sql      sql 语句
     * @param function 解析实现
     * @param <T>      要返回的数据对象泛型
     * @return 数据对象
     * @throws Exception Exception
     */
    public <T> List<T> queryList(String sql, Function<ResultSet, T> function) throws Exception {
        return this.queryList(sql, null, function);
    }

    /**
     * 查询多条数据
     *
     * @param sql               sql 语句
     * @param resultSetConsumer ResultSet 前置处理回调函数
     * @param function          解析实现
     * @param <T>               要返回的数据对象泛型
     * @return 数据对象
     * @throws Exception Exception
     */
    public <T> List<T> queryList(String sql, Consumer<ResultSet> resultSetConsumer, Function<ResultSet, T> function) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        if (Objects.nonNull(resultSetConsumer)) {
            resultSetConsumer.accept(rs);
        }

        List<T> dataList = new ArrayList<>();
        while (rs.next()) {
            T data = function.apply(rs);
            dataList.add(data);
        }
        rs.close();
        statement.close();
        return dataList;
    }

    /**
     * 查询单条数据
     *
     * @param sql      sql 语句
     * @param function 解析实现
     * @param <T>      要返回的数据对象泛型
     * @return 数据对象
     * @throws Exception Exception
     */
    public <T> T queryOne(String sql, Function<ResultSet, T> function) throws Exception {
        return this.queryOne(sql, null, function);
    }

    /**
     * 查询单条数据
     *
     * @param sql               sql 语句
     * @param resultSetConsumer ResultSet 前置处理回调函数
     * @param function          解析实现
     * @param <T>               要返回的数据对象泛型
     * @return 数据对象
     * @throws Exception Exception
     */
    public <T> T queryOne(String sql, Consumer<ResultSet> resultSetConsumer, Function<ResultSet, T> function) throws Exception {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (Objects.nonNull(resultSetConsumer)) {
                resultSetConsumer.accept(rs);
            }
            return function.apply(rs);
        }
    }

    private DataSourceTool() {
    }
}
