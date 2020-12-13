package org.woodwhales.business.collection;

/**
 * @author woodwhales
 * @create 2020-12-13 21:40
 */
public interface CollectionFieldComparable<K> {

    /**
     * 获取可比较的 key
     * @return
     */
    K getDataKey();
}
