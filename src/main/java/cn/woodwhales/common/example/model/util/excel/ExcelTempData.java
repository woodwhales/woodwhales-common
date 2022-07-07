package cn.woodwhales.common.example.model.util.excel;

import cn.woodwhales.common.util.excel.ExcelDateField;
import cn.woodwhales.common.util.excel.ExcelField;
import lombok.Data;

/**
 * @author woodwhales on 2021-07-29 9:57
 */
@Data
public class ExcelTempData extends BaseExcelTempData {

    @ExcelField("姓名")
    private String name;

    @ExcelField(value = "年龄", type = Integer.class)
    private Integer age;

    @ExcelField
    private String gender;

    @ExcelDateField(value = "出生日期", pattern = "yyyy-MM-dd")
    private String birthday;

    private String memo;

    @ExcelField(jsonFlag = true, nullValueHandler = MyNullValueHandler2.class)
    private Desc desc;

    @ExcelField(nullValueHandler = MyNullValueHandler.class , type = Integer.class)
    private Integer test;

    @Data
    public static class Desc {
        private String address;
        private String tag;
    }
}
