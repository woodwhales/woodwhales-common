package org.woodwhales.common.business;

/**
 * 去重器接口
 * @author woodwhales
 * @create 2020-11-17 13:46
 */
public interface DeduplicateInterface<K, T> {

    /**
     * 数据是否有效
     * @param data
     * @return
     */
    boolean isValid(T data);

    /**
     * 获取去重的数据值
     * @param data
     * @return
     */
    K getDeduplicatedKey(T data);

}
