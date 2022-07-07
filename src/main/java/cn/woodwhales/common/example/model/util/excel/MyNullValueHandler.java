package cn.woodwhales.common.example.model.util.excel;

import cn.woodwhales.common.util.excel.NullValueHandler;

/**
 * @author woodwhales on 2022-07-07 23:13
 */
public class MyNullValueHandler implements NullValueHandler {
    @Override
    public Object defaultValueIfExcelDataIdNull() {
        return -1;
    }
}
