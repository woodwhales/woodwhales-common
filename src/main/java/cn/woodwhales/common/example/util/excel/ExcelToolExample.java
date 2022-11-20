package cn.woodwhales.common.example.util.excel;

import cn.woodwhales.common.example.model.util.excel.ExcelTempData;
import cn.woodwhales.common.util.excel.DrawSlashContext;
import cn.woodwhales.common.util.excel.ExcelTool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author woodwhales on 2021-07-25 14:39
 */
public class ExcelToolExample {

    public static void main(String[] args) throws Exception {
//        testParseData1();
//        testParseData2();
        testDrawSlash();
    }

    private static void testParseData1() {
        InputStream resourceAsStream = ExcelToolExample.class.getClassLoader()
                .getResourceAsStream("demo.xlsx");
        ExcelTool.parseData(resourceAsStream, (index, row) -> {
            String name = ExcelTool.getStringValue(row, 0);
            int age = ExcelTool.getIntegerValue(row, 1);
            String gender = ExcelTool.getStringValue(row, 2);
            Long mobile = ExcelTool.getLongValue(row, 3);
            Date birthday = ExcelTool.getDateValue(row, 4);
            Date createTime = ExcelTool.getDateValue(row, 5);
            String memo = ExcelTool.getStringValue(row, 6);

            System.out.println("index = " + index);
            System.out.println("name = " + name);
            System.out.println("age = " + age);
            System.out.println("gender = " + gender);
            System.out.println("mobile = " + mobile);
            System.out.println("birthday = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(birthday));
            System.out.println("createTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
            System.out.println("createTime = " + memo);
            System.out.println(" ============== ");
            return name;
        });
    }

    public static void testParseData2() {
        InputStream resourceAsStream = ExcelToolExample.class.getClassLoader()
                .getResourceAsStream("demo.xlsx");
        // 解析excel文件为数据对象，属性需要使用 @ExcelField、@ExcelDateField 注解修饰
        List<ExcelTempData> excelTempData = ExcelTool.parseData(resourceAsStream, ExcelTempData.class);
        excelTempData.stream().forEach(System.out::println);
    }

    public static void testDrawSlash() throws Exception {
        DrawSlashContext drawSlashContext = new DrawSlashContext();
        drawSlashContext.outputStream = new FileOutputStream("D:\\temp\\test.xlsx");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        // 设置高度
        row.setHeightInPoints(30);
        cell.setCellValue("test title");
        sheet.setColumnWidth(0,2 * 9 * 256);

        Row row1 = sheet.createRow(1);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("2022-08-19 00:00");
        drawSlashContext.workbook = workbook;
        drawSlashContext.sheet = sheet;
        drawSlashContext.col1 = 0;
        drawSlashContext.row1 = 0;
        drawSlashContext.col2 = 1;
        drawSlashContext.row2 = 1;
        ExcelTool.drawSlash(drawSlashContext);
        drawSlashContext.export();
    }
}
