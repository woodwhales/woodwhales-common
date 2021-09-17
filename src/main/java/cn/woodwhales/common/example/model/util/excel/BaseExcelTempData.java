package cn.woodwhales.common.example.model.util.excel;

import lombok.Data;
import cn.woodwhales.common.util.excel.ExcelDateField;

/**
 * @author woodwhales on 2021-09-03 14:02
 * @description
 */
@Data
public class BaseExcelTempData {

    @ExcelDateField(value = "创建时间")
    private String createTime;

}
