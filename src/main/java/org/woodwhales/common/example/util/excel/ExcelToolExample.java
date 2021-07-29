package org.woodwhales.common.example.util.excel;

import org.woodwhales.common.example.model.util.excel.ExcelTempData;
import org.woodwhales.common.util.excel.ExcelTool;

import java.io.InputStream;
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
            System.out.println("name = " + name + " , " + "age = " + age);
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
