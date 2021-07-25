package org.woodwhales.common.util;

import cn.hutool.core.date.DatePattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import static java.util.Objects.isNull;

/**
 * @author woodwhales on 2021-01-29 15:32
 * @description excel 数据解析工具
 */
public class ExcelTool {

    /**
     * 解析 excel 中的内容为 list 集合数据
     * 默认解析：
     *  第一个 sheet
     *  跳过第一行数据
     * @param filePath
     * @param function
     * @param <T>
     * @return
     */
    public static <T> List<T> parseData(String filePath, BiFunction<Integer, Row, T> function) {
        return parseData(filePath, 0, 1, function);
    }

    /**
     * 解析 excel 中的内容为 list 集合数据
     * 默认解析：
     *  第一个 sheet
     *  跳过第一行数据
     * @param inputStream
     * @param function
     * @param <T>
     * @return
     */
    public static <T> List<T> parseData(InputStream inputStream, BiFunction<Integer, Row, T> function) {
        return parseData(buildWorkbook(inputStream), 0, 1, function);
    }

    /**
     * 解析 excel 中的内容为 list 集合数据
     * @param filePath
     * @param sheetIndex
     * @param skipLineNumbers 跳过第几行（物理行数）
     * @param <T>
     * @return
     */
    public static <T> List<T> parseData(String filePath,
                                        int sheetIndex,
                                        int skipLineNumbers,
                                        BiFunction<Integer, Row, T> function) {
        File file = new File(filePath);
        if(!file.exists()) {
            throw new RuntimeException(filePath + " 文件不存在");
        }

        Workbook workbook = buildWorkbook(file);
        return parseData(workbook, sheetIndex, skipLineNumbers, function);

    }

    private static <T> List<T> parseData(Workbook workbook,
                                        int sheetIndex,
                                        int skipLineNumbers,
                                        BiFunction<Integer, Row, T> function) {
        Objects.requireNonNull(function, "function不允许为空");

        if(isNull(sheetIndex)) {
            sheetIndex = 0;
        }

        if(isNull(skipLineNumbers)) {
            skipLineNumbers = 0;
        }

        Sheet sheet = workbook.getSheetAt(sheetIndex);

        if(isNull(sheet)) {
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

    public static String getStringValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if(isNull(cell)) {
            return StringUtils.EMPTY;
        }

        if(!Objects.equals(CellType.STRING, cell.getCellTypeEnum())) {
            throw new RuntimeException("cellIndex=[" + cellIndex + "]不是字符串单元格");
        }
        return cell.getStringCellValue();
    }

    public static Integer getIntegerValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if(isNull(cell)) {
            return null;
        }

        if(!Objects.equals(CellType.NUMERIC, cell.getCellTypeEnum())) {
            throw new RuntimeException("cellIndex=[" + cellIndex + "]不是数值单元格");
        }

        Double numericCellValue = cell.getNumericCellValue();
        if(isNull(numericCellValue)) {
            return null;
        }
        return numericCellValue.intValue();
    }

    public static Date getDateValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if(isNull(cell)) {
            return null;
        }

        if(DateUtil.isCellDateFormatted(cell)) {
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
        if(isNull(dateValue)) {
            return StringUtils.EMPTY;
        }

        return DateFormatUtils.format(dateValue, pattern);
    }

    private static Workbook buildWorkbook(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream不允许为空");
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private static Workbook buildWorkbook(File file) {
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
}
