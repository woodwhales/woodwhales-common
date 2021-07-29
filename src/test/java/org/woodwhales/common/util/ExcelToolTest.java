package org.woodwhales.common.util;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.woodwhales.common.util.excel.ExcelDateField;
import org.woodwhales.common.util.excel.ExcelField;
import org.woodwhales.common.util.excel.ExcelTool;

import java.io.InputStream;
import java.util.List;

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

    @Test
    public void testParseData2() {
        InputStream resourceAsStream = this.getClass()
                                           .getClassLoader()
                                           .getResourceAsStream("demo.xlsx");
        List<ExcelTempData> excelTempData = ExcelTool.parseData(resourceAsStream, ExcelTempData.class);
        excelTempData.stream().forEach(System.out::println);
    }

    @Data
    public static class ExcelTempData {

        @ExcelField("姓名")
        private String name;

        @ExcelField(value = "年龄", type = Integer.class)
        private Integer age;

        @ExcelField("性别")
        private String gender;

        @ExcelDateField(value = "出生日期", pattern = "yyyy-MM-dd")
        private String birthday;

        @ExcelDateField(value = "创建时间")
        private String createTime;

        private String memo;

    }

}