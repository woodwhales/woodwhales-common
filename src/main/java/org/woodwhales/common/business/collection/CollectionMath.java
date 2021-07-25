package org.woodwhales.common.business.collection;

import java.util.List;
import java.util.Set;

/**
 * @author woodwhales
 * @create 202-12-13 22:00
 */
public interface CollectionMath<K, M, N> {

    /**
     * 交集
     * @return
     */
    Set<CollectionFieldComparable<K>> getIntersectionSet();

    /**
     * 交集
     * @return
     */
    Set<K> getIntersectionKeySet();

    /**
     * 并集
     * @return
     */
    Set<K> getUnionKeySet();

    /**
     * 并集
     * @return
     */
    Set<CollectionFieldComparable<K>> getUnionSet();

    /**
     * 正差集
     * @return
     */
    Set<K> getPositiveDifferenceKeySet();

    /**
     * 正差集
     * @return
     */
    List<M> getPositiveDifferenceList();

    /**
     * 正差集
     * @return
     */
    Set<CollectionFieldComparable<K>> getPositiveDifferenceSet();

    /**
     * 负差集
     * @return
     */
    Set<K> getNegativeDifferenceKeySet();

    /**
     * 负差集
     * @return
     */
    List<N> getNegativeDifferenceList();

    /**
     * 负差集
     * @return
     */
    Set<CollectionFieldComparable<K>> getNegativeDifferenceSet();
}
