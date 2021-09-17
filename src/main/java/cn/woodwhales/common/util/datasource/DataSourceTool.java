package cn.woodwhales.common.util.datasource;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author woodwhales on 2021-01-28 21:22
 * @description 数据库查询工具
 */
@Slf4j
public class DataSourceTool {

    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Connection connection;
    private Map<Class<?>, TargetInfo> targetInfoCache = new HashMap<>(16);
    private Map<Class<? extends Annotation>, Function<? extends Annotation, String>> annotationCache = new HashMap<>(16);

    public static DataSourceTool newMysql(String url, String username, String password) {
        return new DataSourceTool("com.mysql.jdbc.Driver", url, username, password);
    }

    public static DataSourceTool newMysql8(String url, String username, String password) {
        return new DataSourceTool("com.mysql.cj.jdbc.Driver", url, username, password);
    }

    public static DataSourceTool newOracle(String url, String username, String password) {
        return new DataSourceTool("oracle.jdbc.OracleDriver", url, username, password);
    }

    public <A extends Annotation> DataSourceTool(String driverClass, String url, String username, String password) {
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
            if(cause instanceof SQLException){
                SQLException sqlException = (SQLException) cause;
                String sqlState = sqlException.getSQLState();
                if("28000".equals(sqlState)) {
                    System.out.println("数据库账号或密码错误!!!");
                }
            } else {
                e.printStackTrace();
            }
            System.exit(0);
        }

    }

    public <T, A extends Annotation> List<T> queryList(String sql,
                                       Class<T> clazz,
                                       Class<A> annotationClass,
                                       Function<A, String> function) throws Exception {

        return queryList(sql, this.getTarget(clazz, annotationClass, function));
    }

    private <T, A extends Annotation> TargetInfo<T, A> cacheTarget(Class<T> clazz,
                                                                   Class<A> annotationClass,
                                                                   Function<A, String> function) {
        if(!this.targetInfoCache.containsKey(clazz)) {
            this.targetInfoCache.put(clazz, new TargetInfo<>(clazz, annotationClass, function));
        }
        return this.targetInfoCache.get(clazz);
    }

    public <T, A extends Annotation> T queryOne(String sql,
                                                       Class<T> clazz,
                                                       Class<A> annotationClass,
                                                       Function<A, String> function) throws Exception {
        return queryOne(sql, this.getTarget(clazz, annotationClass, function));
    }

    private <T, A extends Annotation> Function<ResultSet, T> getTarget(Class<T> clazz,
                                                                       Class<A> annotationClass,
                                                                       Function<A, String> function) {
        this.cacheTarget(clazz, annotationClass, function);
        return resultSet -> this.fillFieldValue(clazz, resultSet);
    }

    public <T> List<T> queryList(String sql, Class<T> clazz) throws Exception {
        return queryList(sql, clazz, null, null);
    }

    public <T> T queryOne(String sql, Class<T> clazz) throws Exception {
        return queryOne(sql, clazz, null, null);
    }

    public <T> T fillFieldValue(Class<T> clazz, ResultSet resultSet) {
        try {
            T target = clazz.newInstance();
            TargetInfo<T, ?> targetInfo = this.targetInfoCache.get(clazz);
            for (TargetInfo.TargetFieldInfo targetFieldInfo : targetInfo.targetFieldInfoList) {
                Object object = getObject(resultSet, targetFieldInfo);
                if(nonNull(object)) {
                    boolean accessible = targetFieldInfo.field.isAccessible();
                    targetFieldInfo.field.setAccessible(true);
                    targetFieldInfo.field.set(target, object);
                    targetFieldInfo.field.setAccessible(accessible);
                }
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getObject(ResultSet resultSet, TargetInfo.TargetFieldInfo targetFieldInfo) throws SQLException {
        String fieldTypeName = targetFieldInfo.fieldType.getName();
        String columnLabel = targetFieldInfo.columnLabel;

        if(isNull(columnLabel)) {
            return null;
        }

        Object object;
        switch (fieldTypeName) {
            case "java.util.Date" :
                object = resultSet.getTimestamp(targetFieldInfo.columnLabel);
                break;
            case "java.lang.Byte" :
                object = resultSet.getByte(targetFieldInfo.columnLabel);
                break;
            case "java.time.LocalDateTime" :
                object = Optional.ofNullable(resultSet.getTimestamp(columnLabel))
                        .map(timestamp -> timestamp.toInstant()
                                .atZone( ZoneId.systemDefault() )
                                .toLocalDateTime())
                        .orElse(null);
                break;
            default:
                object = resultSet.getObject(columnLabel, targetFieldInfo.fieldType);
        }
        return object;
    }

    private static class TargetInfo<T, A extends Annotation> {
        Class<T> clazz;
        List<TargetFieldInfo> targetFieldInfoList;

        public TargetInfo(Class<T> clazz, Class<A> annotationClass, Function<A, String> function) {
            this.clazz = clazz;
            this.targetFieldInfoList = new ArrayList<>();
            Field[] declaredFields = FieldUtils.getAllFields(clazz);
            for (Field declaredField : declaredFields) {
                this.addTargetFieldInfo(declaredField, annotationClass, function);
            }
        }

        public TargetInfo<T, A> addTargetFieldInfo(Field field, Class<A> clazz, Function<A, String> function) {
            TargetFieldInfo<?> fieldInfo;
            if(isNull(clazz) || isNull(function)) {
                fieldInfo = new TargetFieldInfo<>(field, Column.class, Column::name);
            } else {
                fieldInfo = new TargetFieldInfo<>(field, clazz, function);
            }
            fieldInfo.fillColumnLabel(field, DataColumn.class, DataColumn::value);
            fieldInfo.fillColumnLabel(field, DataTable.class, DataTable::value);

            fieldInfo.fillColumnLabel(field, Table.class, Table::name);
            fieldInfo.fillColumnLabel(field, Column.class, Column::name);

            fieldInfo.fillColumnLabel(field, TableId.class, TableId::value);
            fieldInfo.fillColumnLabel(field, TableField.class, TableField::value);

            fieldInfo.fillColumnLabel(field, DataColumn.class, DataColumn::value);
            fieldInfo.fillColumnLabel(field, DataColumn.class, DataColumn::value);

            this.targetFieldInfoList.add(fieldInfo);
            return this;
        }

        private static class TargetFieldInfo<A extends Annotation> {
            private Field field;
            private Class<?> fieldType;
            private String columnLabel;

            public TargetFieldInfo(Field field, Class<A> annotationClass, Function<A, String> function) {
                this.field = field;
                this.fieldType = field.getType();
                this.fillColumnLabel(field, annotationClass, function);
            }

            private <A> void fillColumnLabel(Field field,
                                         Class<A> annotationClass,
                                         Function<A, String> function) {
                if(nonNull(this.columnLabel)
                        || isNull(field.getAnnotations())
                        || field.getAnnotations().length == 0) {
                    return;
                } else {
                    Annotation[] annotations = field.getAnnotations();
                    Map<? extends Class<? extends Annotation>, Annotation> annotationMap = Arrays.stream(annotations)
                            .collect(Collectors.toMap(Annotation::annotationType, Function.identity()));

                    if (annotationMap.containsKey(annotationClass)) {
                        A annotation = (A) annotationMap.get(annotationClass);
                        this.columnLabel = function.apply(annotation);
                    }
                }
            }
        }
    }


    public <T> List<T> queryList(String sql, Function<ResultSet, T> function) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<T> dataList = new ArrayList<>();
        while (rs.next()) {
            T data = function.apply(rs);
            dataList.add(data);
        }
        rs.close();
        statement.close();
        return dataList;
    }



    public <T> T queryOne(String sql, Function<ResultSet, T> function) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        try {
            rs.next();
            return function.apply(rs);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private DataSourceTool() {
    }
}
