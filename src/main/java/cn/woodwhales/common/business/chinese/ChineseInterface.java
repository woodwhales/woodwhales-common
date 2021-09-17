package cn.woodwhales.common.business.chinese;

/**
 * @author woodwhales
 * 2020-12-07 22:38
 */
@FunctionalInterface
public interface ChineseInterface<T> {

    /**
     * 获取要排序的字段
     * @param data 原始数据
     * @return 要排序的字段
     */
    String getSortedField(T data);
}
