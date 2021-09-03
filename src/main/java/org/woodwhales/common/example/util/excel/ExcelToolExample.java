package org.woodwhales.common.example.util.excel;

import org.woodwhales.common.example.model.util.excel.ExcelTempData;
import org.woodwhales.common.util.excel.ExcelTool;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author woodwhales on 2021-07-25 14:39
 * @Description
 */
public class ExcelToolExample {

    public static void main(String[] args) {
//        testParseData1();
        testParseData2();
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

            System.out.println("name = " + name);
            System.out.println("age = " + age);
            System.out.println("gender = " + gender);
            System.out.println("mobile = " + mobile);
            System.out.println("birthday = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(birthday));
            System.out.println("createTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
            System.out.println("createTime = " + memo);
            return name;
        });
    }

    public static void testParseData2() {
        InputStream resourceAsStream = ExcelToolExample.class.getClassLoader()
                                                               .getResourceAsStream("demo.xlsx");
        List<ExcelTempData> excelTempData = ExcelTool.parseData(resourceAsStream, ExcelTempData.class);
        excelTempData.stream().forEach(System.out::println);
    }
}
