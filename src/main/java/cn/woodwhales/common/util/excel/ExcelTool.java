package cn.woodwhales.common.util.excel;

import cn.hutool.core.date.DatePattern;
import cn.woodwhales.common.business.DataTool;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    public static <T> List<T> parseData(String filePath, BiFunction<ParseDataModel, Row, T> function) {
        return parseData(filePath, 0, 1, function);
    }

    /**
     * 解析 excel 中的内容为 list 集合数据
     * 默认解析：
     * 第一个 sheet
     * 跳过第一行数据
     * 示例参见：cn.woodwhales.common.example.util.excel.ExcelToolExample#testParseData1()
     *
     * @param inputStream 文件输入流
     * @param function    解析接口
     * @param <T>         返回数据泛型
     * @return list
     */
    public static <T> List<T> parseData(InputStream inputStream, BiFunction<ParseDataModel, Row, T> function) {
        return parseData(buildWorkbook(inputStream), 0, 1, function, null);
    }

    /**
     * 解析 excel 中的内容为 list 集合数据
     * @param inputStream 文件输入流
     * @param sheetIndex 读取sheet索引位置
     * @param skipLineNumbers 跳过第skipLineNumbers行开始读取数据
     * @param function 解析接口
     * @param <T> 集合数据类型
     * @return 成功解析的集合
     */
    public static <T> List<T> parseData(InputStream inputStream,
                                        int sheetIndex,
                                        int skipLineNumbers,
                                        BiFunction<ParseDataModel, Row, T> function) {
        return parseData(buildWorkbook(inputStream), sheetIndex, skipLineNumbers, function, null);
    }

    /**
     * 解析数据
     * 示例：cn.woodwhales.common.example.util.excel.ExcelToolExample#testParseData2()
     * @param fileName    解析的文件路径
     * @param clazz       解析数据对象类型
     * @param <T>         解析数据对象类型泛型
     * @return 解析数据集合
     */
    public static <T> List<T> parseData(String fileName, Class<T> clazz) {
        return parseData(new File(fileName), clazz);
    }

    /**
     * 解析数据
     * 示例：cn.woodwhales.common.example.util.excel.ExcelToolExample#testParseData2()
     * @param file        要解析的文件对象
     * @param clazz       解析数据对象类型
     * @param <T>         解析数据对象类型泛型
     * @return 解析数据集合
     */
    public static <T> List<T> parseData(File file, Class<T> clazz) {
        try {
            return parseData(new FileInputStream(file), clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析数据
     * 示例：cn.woodwhales.common.example.util.excel.ExcelToolExample#testParseData2()
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

        return parseData(buildWorkbook(inputStream), 0, 1, (parseDataModel, row) -> {
            int maxCellNumber = parseDataModel.maxCellNumber;
            T target = null;
            Cell cell = null;
            try {
                target = clazz.newInstance();
                for (int cellIndex = 0; cellIndex < maxCellNumber; cellIndex++) {
                    cell = row.getCell(cellIndex);
                    ExcelFieldConfig excelFieldConfig = excelFieldConfigMap2.get()
                                                                .get(cellIndex);
                    fillFieldValue(target, row, cell, excelFieldConfig);
                }
            } catch (Exception e) {
                System.err.printf("index = %s, cell value = [%s], parese error, cause by : %s\n", parseDataModel.rowIndex, cell, e.getMessage());
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

    private static <T> void fillFieldValue(T target,
                                           Row row,
                                           Cell cell,
                                           ExcelFieldConfig excelFieldConfig) throws IllegalAccessException {
        if (isNull(excelFieldConfig)) {
            return;
        }

        excelFieldConfig.fillField(target, row, cell, excelFieldConfig);
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
                                        BiFunction<ParseDataModel, Row, T> function) {
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
                                         BiFunction<ParseDataModel, Row, T> function,
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
        Integer maxCellNumber = 0;
        while (rowIndex <= sheet.getLastRowNum()) {
            Row row = sheet.getRow(rowIndex);
            if (Objects.isNull(row)) {
                rowIndex++;
                continue;
            }

            // 跳过行数
            if (row.getRowNum() < skipLineNumbers) {
                if (Objects.nonNull(skipConsumer)) {
                    int lastCellNum = row.getLastCellNum();
                    if(lastCellNum > maxCellNumber) {
                        maxCellNumber = lastCellNum;
                    }
                    for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                        skipConsumer.accept(cellIndex, row);
                    }
                }
                rowIndex++;
                continue;
            }

            Integer tmpRowIndex = rowIndex;
            T data = function.apply(new ParseDataModel(tmpRowIndex, maxCellNumber), row);
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
        } catch (Exception e) {
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

    /**
     * 绘制单元格斜线
     * @param drawSlashContext drawSlashContext
     */
    public static void drawSlash(DrawSlashContext drawSlashContext) {
        Workbook workbook = drawSlashContext.workbook;
        Sheet sheet = drawSlashContext.sheet;
        CreationHelper helper = workbook.getCreationHelper();
        // XLSX
        if(workbook instanceof XSSFWorkbook) {
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            // 设置斜线的开始位置
            anchor.setCol1(drawSlashContext.col1);
            anchor.setRow1(drawSlashContext.row1);
            // 设置斜线的结束位置
            anchor.setCol2(drawSlashContext.col2);
            anchor.setRow2(drawSlashContext.row2);
            XSSFSimpleShape shape = drawing.createSimpleShape((XSSFClientAnchor) anchor);
            // 设置形状类型为线型
            shape.setShapeType(ShapeTypes.LINE);
            // 设置线宽
            shape.setLineWidth(0.5);
            // 设置线的风格
            shape.setLineStyle(0);
            // 设置线的颜色
            shape.setLineStyleColor(0, 0, 0);
        }
        // XLS
        if(workbook instanceof HSSFWorkbook) {
            HSSFPatriarch drawing =  (HSSFPatriarch) sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            // 设置斜线的开始位置
            anchor.setCol1(drawSlashContext.col1);
            anchor.setRow1(drawSlashContext.row1);
            // 设置斜线的结束位置
            anchor.setCol2(drawSlashContext.col2);
            anchor.setRow2(drawSlashContext.row2);
            HSSFSimpleShape shape = drawing.createSimpleShape((HSSFClientAnchor) anchor);
            // 设置形状类型为线型
            shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            // 设置线宽
            shape.setLineWidth(6350);
            // 设置线的风格
            shape.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
            // 设置线的颜色
            shape.setLineStyleColor(0, 0, 0);
        }
    }

    private static class ExcelFieldConfig {
        public ExcelFieldType excelFieldType;
        public Field field;
        public boolean jsonFlag;
        public Integer cellIndex;
        public Class<?> clazz;
        public String excelFieldName;
        public String pattern;
        public NullValueHandler nullValueHandler;

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
                Class<?> nullValueHandlerClass = excelField.nullValueHandler();
                if(Objects.nonNull(nullValueHandlerClass) && NullValueHandler.class.isAssignableFrom(nullValueHandlerClass)) {
                    try {
                        this.nullValueHandler = (NullValueHandler) nullValueHandlerClass.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            ExcelDateField excelDateField = field.getAnnotation(ExcelDateField.class);
            if (Objects.nonNull(excelDateField)) {
                this.excelFieldType = ExcelFieldType.DATE_STR;
                this.clazz = String.class;
                this.excelFieldName = excelDateField.value();
                this.pattern = excelDateField.pattern();
                Class<?> nullValueHandlerClass = excelDateField.nullValueHandler();
                if(Objects.nonNull(nullValueHandlerClass) && nullValueHandlerClass.isAssignableFrom(NullValueHandler.class)) {
                    try {
                        this.nullValueHandler = (NullValueHandler) nullValueHandlerClass.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
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

        public void fillField(Object target, Row row, Cell cell, ExcelFieldConfig excelFieldConfig) throws IllegalAccessException {
            boolean accessible = this.field.isAccessible();
            this.field.setAccessible(true);
            String typeName = this.clazz.getName();
            try {
                if (this.jsonFlag) {
                    final String jsonStr = getStringValue(row, this.cellIndex);
                    if (StringUtils.isNotBlank(jsonStr)) {
                        this.field.set(target, new Gson().fromJson(jsonStr, this.field.getType()));
                    } else {
                        this.defaultValueWhenExcelDataIsNull(target, excelFieldConfig);
                    }
                    return;
                }

                if(Objects.isNull(cell)) {
                    this.defaultValueWhenExcelDataIsNull(target, excelFieldConfig);
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
            } finally {
                this.field.setAccessible(accessible);
            }
        }

        private void defaultValueWhenExcelDataIsNull(Object target, ExcelFieldConfig excelFieldConfig) throws IllegalAccessException {
            if(Objects.nonNull(excelFieldConfig.nullValueHandler)) {
                Object defaultValue = excelFieldConfig.nullValueHandler.defaultValueIfExcelDataIdNull();
                if(Objects.nonNull(defaultValue)) {
                    if(!defaultValue.getClass().isAssignableFrom(excelFieldConfig.field.getType())) {
                        throw new RuntimeException(String.format("字段[%s]设置的默认值类不合法", excelFieldConfig.excelFieldName));
                    }
                    excelFieldConfig.field.set(target, defaultValue);
                }
            }
        }
    }


}
