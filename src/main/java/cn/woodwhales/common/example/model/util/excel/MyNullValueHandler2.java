package cn.woodwhales.common.example.model.util.excel;

import cn.woodwhales.common.util.excel.NullValueHandler;

/**
 * @author woodwhales on 2022-07-08 0:15
 */
public class MyNullValueHandler2 implements NullValueHandler {
    @Override
    public Object defaultValueIfExcelDataIdNull() {
        ExcelTempData.Desc desc = new ExcelTempData.Desc();
        desc.setAddress("默认值address");
        desc.setTag("默认值tag");
        return desc;
    }
}
