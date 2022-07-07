package cn.woodwhales.common.util.excel;

/**
 * 当解析到的excel数据为null时设置默认值处理器
 * @author woodwhales on 2022-07-07 22:43
 */
public interface NullValueHandler {

    /**
     * 当解析到的excel数据为null时设置的默认值
     * @return 默认值
     */
    Object defaultValueIfExcelDataIdNull();

}
