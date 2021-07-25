package org.woodwhales.common.business.chinese;

/**
 * @author woodwhales
 * @create 2020-12-07 22:38
 */
@FunctionalInterface
public interface ChineseInterface<T> {

    /**
     * 获取要排序的字段
     * @param data
     * @return
     */
    String getSortedField(T data);
}
