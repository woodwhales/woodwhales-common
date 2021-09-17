package cn.woodwhales.common.business.collection;

import java.util.List;
import java.util.Set;

/**
 * @author woodwhales
 * 2020-12-13 22:00
 */
public interface CollectionMath<K, M, N> {

    /**
     * 交集
     * @return 交集
     */
    Set<CollectionFieldComparable<K>> getIntersectionSet();

    /**
     * 交集
     * @return 交集
     */
    Set<K> getIntersectionKeySet();

    /**
     * 并集
     * @return 并集
     */
    Set<K> getUnionKeySet();

    /**
     * 并集
     * @return 并集
     */
    Set<CollectionFieldComparable<K>> getUnionSet();

    /**
     * 正差集
     * @return 正差集
     */
    Set<K> getPositiveDifferenceKeySet();

    /**
     * 正差集
     * @return 正差集
     */
    List<M> getPositiveDifferenceList();

    /**
     * 正差集
     * @return 正差集
     */
    Set<CollectionFieldComparable<K>> getPositiveDifferenceSet();

    /**
     * 负差集
     * @return 负差集
     */
    Set<K> getNegativeDifferenceKeySet();

    /**
     * 负差集
     * @return 负差集
     */
    List<N> getNegativeDifferenceList();

    /**
     * 负差集
     * @return 负差集
     */
    Set<CollectionFieldComparable<K>> getNegativeDifferenceSet();
}
