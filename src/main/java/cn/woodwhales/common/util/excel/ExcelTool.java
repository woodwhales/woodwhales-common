package cn.woodwhales.common.util.excel;

import cn.hutool.core.date.DatePattern;
import cn.woodwhales.common.business.DataTool;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static cn.woodwhales.common.business.DataTool.toMapForSaveNew;
import static java.util.Objects.isNull;

/**
 * @author woodwhales on 2021-01-29 15:32
 * excel 数据解析工具
 */
public class ExcelTool {

    /**
     * 解析 excel 中的内容为 list 集合数据
     * 默认解析：
     * 第一个 sheet
     * 跳过第一行数据
     *
     * @param filePath 文件路径
     * @param function 解析接口
     * @param <T>      返回数据泛型
     * @return list
     */
    public static <T> List<T> parseData(String filePath, BiFunction<Integer, Row, T> function) {
        return parseData(filePath, 0, 1, function);
    }

    /**
     * 解析 excel 中的内容为 list 集合数据
     * 默认解析：
     * 第一个 sheet
     * 跳过第一行数据
     *
     * @param inputStream 文件输入流
     * @param function    解析接口
     * @param <T>         返回数据泛型
     * @return list
     */
    public static <T> List<T> parseData(InputStream inputStream, BiFunction<Integer, Row, T> function) {
        return parseData(buildWorkbook(inputStream), 0, 1, function, null);
    }

    /**
     * 解析数据
     *
     * @param inputStream 输入流
     * @param clazz       解析数据对象类型
     * @param <T>         解析数据对象类型泛型
     * @return 解析数据集合
     */
    public static <T> List<T> parseData(InputStream inputStream, Class<T> clazz) {
        final Field[] declaredFields = FieldUtils.getAllFields(clazz);
        Map<String, ExcelFieldConfig> excelFieldConfigMap =
                toMapForSaveNew(DataTool.toList(declaredFields, ExcelFieldConfig::new), ExcelFieldConfig::getExcelFieldName);

        AtomicReference<Map<Integer, ExcelFieldConfig>> excelFieldConfigMap2 = new AtomicReference<>();

        return parseData(buildWorkbook(inputStream), 0, 1, (index, row) -> {
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            T target = null;
            Cell cell = null;
            try {
                target = clazz.newInstance();
                for (int cellIndex = 0; cellIndex < physicalNumberOfCells; cellIndex++) {
                    cell = row.getCell(cellIndex);
                    ExcelFieldConfig excelFieldConfig = excelFieldConfigMap2.get()
                            .get(cellIndex);
                    fillFieldValue(target, row, cell, excelFieldConfig);
                }
            } catch (Exception e) {
                System.err.printf("index = %s, cell value = [%s], parese error, cause by : %s\n", index, cell, e.getMessage());
                e.printStackTrace();
            }
            return target;
        }, (cellIndex, row) -> {
            Cell cell = row.getCell(cellIndex);
            if (Objects.nonNull(cell)) {
                String cellName = cell.getStringCellValue();
                if (excelFieldConfigMap.containsKey(cellName)) {
                    excelFieldConfigMap.get(cellName).cellIndex = cellIndex;
                }

                if (cellIndex.equals(row.getPhysicalNumberOfCells() - 1)) {
                    excelFieldConfigMap2.set(toMapForSaveNew(excelFieldConfigMap.values(), ExcelFieldConfig::getCellIndex));
                }
            }
        });
    }

    private static <T> void fillFieldValue(T target, Row row, Cell cell, ExcelFieldConfig excelFieldConfig) throws IllegalAccessException {
        if (isNull(cell) || isNull(excelFieldConfig)) {
            return;
        }

        excelFieldConfig.fillField(target, row, cell);
    }

    /**
     * 导出文件
     *
     * @param workbook Workbook
     * @param file     目标文件
     */
    public static void exportToOutputStream(Workbook workbook, File file) {
        try {
            exportToOutputStream(workbook, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 导出至输出流
     *
     * @param workbook     Workbook
     * @param outputStream 输出流
     */
    public static void exportToOutputStream(Workbook workbook, OutputStream outputStream) {
        try {
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 解析 excel 中的内容为 list 集合数据
     *
     * @param filePath        文件路径
     * @param sheetIndex      sheet索引
     * @param skipLineNumbers 跳过第几行（物理行数）
     * @param function        解析接口
     * @param <T>             返回数据泛型
     * @return list
     */
    public static <T> List<T> parseData(String filePath,
                                        int sheetIndex,
                                        int skipLineNumbers,
                                        BiFunction<Integer, Row, T> function) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException(filePath + " 文件不存在");
        }

        Workbook workbook = buildWorkbook(file);
        return parseData(workbook, sheetIndex, skipLineNumbers, function, null);
    }

    private static <T> List<T> parseData(Workbook workbook,
                                         int sheetIndex,
                                         int skipLineNumbers,
                                         BiFunction<Integer, Row, T> function,
                                         BiConsumer<Integer, Row> skipConsumer) {
        Objects.requireNonNull(function, "function不允许为空");

        if (isNull(sheetIndex)) {
            sheetIndex = 0;
        }

        if (isNull(skipLineNumbers)) {
            skipLineNumbers = 0;
        }

        Sheet sheet = workbook.getSheetAt(sheetIndex);

        if (isNull(sheet)) {
            throw new RuntimeException("sheetIndex = " + sheetIndex + " 不存在");
        }

        List<T> dataList = new ArrayList<>(sheet.getLastRowNum());
        int rowIndex = 0;
        while (rowIndex <= sheet.getLastRowNum()) {
            Row row = sheet.getRow(rowIndex);
            if (Objects.isNull(row)) {
                rowIndex++;
                continue;
            }

            // 跳过行数
            if (row.getRowNum() < skipLineNumbers) {
                if (Objects.nonNull(skipConsumer)) {
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    for (int cellIndex = 0; cellIndex < physicalNumberOfCells; cellIndex++) {
                        skipConsumer.accept(cellIndex, row);
                    }
                }
                rowIndex++;
                continue;
            }

            Integer tmpRowIndex = rowIndex;
            T data = function.apply(tmpRowIndex, row);
            dataList.add(data);
            rowIndex++;
        }

        return dataList;
    }

    /**
     * 获取 cell 对象
     *
     * @param row   Row 对象
     * @param index cell 所在索引
     * @return index 所在索引的 cell
     */
    public static Cell getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (Objects.isNull(cell)) {
            cell = row.createCell(index);
        }
        return cell;
    }

    public static String getStringValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (isNull(cell)) {
            return StringUtils.EMPTY;
        }

        if (!Objects.equals(CellType.STRING, cell.getCellTypeEnum())) {
            cell.setCellType(CellType.STRING);
        }
        return cell.getStringCellValue();
    }

    public static Byte getByteValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (isNull(cell)) {
            return null;
        }

        if (Objects.equals(CellType.NUMERIC, cell.getCellTypeEnum())) {
            Double numericCellValue = cell.getNumericCellValue();
            if (isNull(numericCellValue)) {
                return null;
            }
            return numericCellValue.byteValue();
        } else if (Objects.equals(CellType.STRING, cell.getCellTypeEnum())) {
            String stringCellValue = cell.getStringCellValue();
            if (StringUtils.isBlank(stringCellValue)) {
                return null;
            }
            return Byte.parseByte(stringCellValue);
        }

        throw new RuntimeException("cellIndex=[" + cellIndex + "]不是数值单元格");
    }

    public static Long getLongValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (isNull(cell)) {
            return null;
        }

        if (Objects.equals(CellType.NUMERIC, cell.getCellTypeEnum())) {
            Double numericCellValue = cell.getNumericCellValue();
            if (isNull(numericCellValue)) {
                return null;
            }
            return Long.parseLong(formatNumeric(numericCellValue));
        } else if (Objects.equals(CellType.STRING, cell.getCellTypeEnum())) {
            String stringCellValue = cell.getStringCellValue();
            if (StringUtils.isBlank(stringCellValue)) {
                return null;
            }
            return Long.parseLong(stringCellValue);
        }
        throw new RuntimeException("cellIndex=[" + cellIndex + "]不是数值单元格");
    }

    public static Double getDoubleValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (isNull(cell)) {
            return null;
        }

        if (Objects.equals(CellType.NUMERIC, cell.getCellTypeEnum())) {
            Double numericCellValue = cell.getNumericCellValue();
            if (isNull(numericCellValue)) {
                return null;
            }
            return Double.parseDouble(formatNumeric(numericCellValue));
        } else if (Objects.equals(CellType.STRING, cell.getCellTypeEnum())) {
            String stringCellValue = cell.getStringCellValue();
            if (StringUtils.isBlank(stringCellValue)) {
                return null;
            }
            return Double.parseDouble(stringCellValue);
        }

        throw new RuntimeException("cellIndex=[" + cellIndex + "]不是数值单元格");
    }

    public static String formatNumeric(Double numericCellValue) {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        if (Objects.isNull(numericCellValue)) {
            return null;
        }
        return decimalFormat.format(numericCellValue);
    }

    public static Integer getIntegerValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (isNull(cell)) {
            return null;
        }

        if (Objects.equals(CellType.NUMERIC, cell.getCellTypeEnum())) {
            Double numericCellValue = cell.getNumericCellValue();
            if (isNull(numericCellValue)) {
                return null;
            }
            return Integer.parseInt(formatNumeric(numericCellValue));
        } else if (Objects.equals(CellType.STRING, cell.getCellTypeEnum())) {
            String stringCellValue = cell.getStringCellValue();
            if (StringUtils.isBlank(stringCellValue)) {
                return null;
            }
            return Integer.parseInt(stringCellValue);
        }

        throw new RuntimeException("cellIndex=[" + cellIndex + "]不是数值单元格");
    }

    public static Date getDateValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (isNull(cell)) {
            return null;
        }

        if (DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return date;
        }

        return null;
    }

    public static String getFormatDateValue(Row row, int cellIndex) {
        return getFormatDateValue(row, cellIndex, DatePattern.NORM_DATETIME_PATTERN);
    }

    public static String getFormatDateValue(Row row, int cellIndex, String pattern) {
        Date dateValue = getDateValue(row, cellIndex);
        if (isNull(dateValue)) {
            return StringUtils.EMPTY;
        }

        return DateFormatUtils.format(dateValue, pattern);
    }

    /**
     * 创建 Workbook
     *
     * @param inputStream 输入流
     * @return Workbook 对象
     */
    public static Workbook buildWorkbook(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream不允许为空");
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 创建 Workbook
     *
     * @param file 文件
     * @return Workbook 对象
     */
    public static Workbook buildWorkbook(File file) {
        Workbook workbook = null;
        try (InputStream inputStream = new FileInputStream(file)) {
            workbook = buildWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private ExcelTool() {
    }

    private static class ExcelFieldConfig {
        public ExcelFieldType excelFieldType;
        public Field field;
        public boolean jsonFlag;
        public Integer cellIndex;
        public Class<?> clazz;
        public String excelFieldName;
        public String pattern;

        private ExcelFieldConfig() {
        }

        ExcelFieldConfig(Field field) {
            this.field = field;

            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (Objects.nonNull(excelField)) {
                this.excelFieldType = ExcelFieldType.NORMAL;
                this.jsonFlag = excelField.jsonFlag();
                this.clazz = excelField.type();
                this.excelFieldName = StringUtils.defaultIfBlank(excelField.value(), field.getName());
            }

            ExcelDateField excelDateField = field.getAnnotation(ExcelDateField.class);
            if (Objects.nonNull(excelDateField)) {
                this.excelFieldType = ExcelFieldType.DATE_STR;
                this.clazz = String.class;
                this.excelFieldName = excelDateField.value();
                this.pattern = excelDateField.pattern();
            }

            if (isNull(excelField) && isNull(excelDateField)) {
                this.excelFieldType = ExcelFieldType.DEFAULT;
                this.clazz = field.getType();
                this.excelFieldName = field.getName();
            }

        }

        public String getExcelFieldName() {
            return this.excelFieldName;
        }

        public Integer getCellIndex() {
            return this.cellIndex;
        }

        public void fillField(Object target, Row row, Cell cell) throws IllegalAccessException {
            boolean accessible = this.field.isAccessible();
            this.field.setAccessible(true);
            String typeName = this.clazz.getName();

            if (this.jsonFlag) {
                final String jsonStr = getStringValue(row, this.cellIndex);
                if (StringUtils.isNotBlank(jsonStr)) {
                    this.field.set(target, new Gson().fromJson(jsonStr, this.field.getType()));
                }
                return;
            }

            if (String.class.getName().equals(typeName)) {
                if (ExcelFieldType.DATE_STR.equals(this.excelFieldType)) {
                    this.field.set(target, DateFormatUtils.format(getDateValue(row, this.cellIndex), this.pattern));
                } else {
                    this.field.set(target, cell.getStringCellValue());
                }
            } else if (Integer.class.getName().equals(typeName)) {
                this.field.set(target, getIntegerValue(row, this.cellIndex));
            } else if (Double.class.getName().equals(typeName)) {
                this.field.set(target, getDoubleValue(row, this.cellIndex));
            } else if (Date.class.getName().equals(typeName)) {
                this.field.set(target, getDateValue(row, this.cellIndex));
            } else if (Byte.class.getName().equals(typeName)) {
                this.field.set(target, getByteValue(row, this.cellIndex));
            }

            this.field.setAccessible(accessible);
        }
    }
}
