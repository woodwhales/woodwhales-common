package org.woodwhales.common.example.util;

import org.woodwhales.common.util.excel.ExcelTool;

import java.io.InputStream;

/**
 * @author woodwhales on 2021-07-25 14:39
 * @Description
 */
public class ExcelToolExample {

    public static void main(String[] args) {
        InputStream resourceAsStream = ExcelToolExample.class.getClassLoader()
                                                                .getResourceAsStream("demo.xlsx");
        ExcelTool.parseData(resourceAsStream, (index, row) -> {
            String name = ExcelTool.getStringValue(row, 0);
            int age = ExcelTool.getIntegerValue(row, 1);
            System.out.println("name = " + name + " , " + "age = " + age);
            return name;
        });
    }
}
