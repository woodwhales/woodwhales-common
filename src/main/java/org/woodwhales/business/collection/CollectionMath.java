package org.woodwhales.business.collection;

import java.util.Set;

/**
 * @author woodwhales
 * @create 2020-12-13 22:00
 */
public interface CollectionMath<K> {


    Set<CollectionFieldComparable<K>> getIntersectionSet();

    Set<K> getIntersectionKeySet();

    Set<K> getUnionKeySet();

    Set<CollectionFieldComparable<K>> getUnionSet();

    Set<K> getPositiveDifferenceKeySet();

    Set<CollectionFieldComparable<K>> getPositiveDifferenceSet();

    Set<K> getNegativeDifferenceKeySet();

    Set<CollectionFieldComparable<K>> getNegativeDifferenceSet();
}
