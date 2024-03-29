package cn.woodwhales.common.business.collection;

/**
 * @author woodwhales
 * 2020-12-13 21:40
 */
public interface CollectionFieldComparable<K> {

    /**
     * 获取可比较的 key
     *
     * @return 获取可比较的 key
     */
    K getDataKey();
}
