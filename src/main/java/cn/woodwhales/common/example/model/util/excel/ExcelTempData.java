package cn.woodwhales.common.example.model.util.excel;

import lombok.Data;
import cn.woodwhales.common.util.excel.ExcelDateField;
import cn.woodwhales.common.util.excel.ExcelField;

/**
 * @author woodwhales on 2021-07-29 9:57
 * @description
 */
@Data
public class ExcelTempData extends BaseExcelTempData {

    @ExcelField("姓名")
    private String name;

    @ExcelField(value = "年龄", type = Integer.class)
    private Integer age;

    @ExcelField("性别")
    private String gender;

    @ExcelDateField(value = "出生日期", pattern = "yyyy-MM-dd")
    private String birthday;

    private String memo;
}
