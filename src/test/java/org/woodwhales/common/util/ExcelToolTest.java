package org.woodwhales.common.util;

import org.junit.jupiter.api.Test;
import org.woodwhales.common.util.excel.ExcelTool;

import java.io.InputStream;

class ExcelToolTest {

    @Test
    public void testParseData() {
        InputStream resourceAsStream = this.getClass()
                                           .getClassLoader()
                                           .getResourceAsStream("demo.xlsx");
        ExcelTool.parseData(resourceAsStream, (index, row) -> {
            String name = ExcelTool.getStringValue(row, 0);
            int age = ExcelTool.getIntegerValue(row, 1);
            System.out.println("name = " + name + " , " + "age = " + age);
            return name;
        });
    }

}