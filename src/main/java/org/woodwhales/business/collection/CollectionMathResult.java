package org.woodwhales.business.collection;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

/**
 * @author woodwhales on 2020-12-13 16:32
 * @description
 */
public class CollectionMathResult<K, M, N> {

    private List<M> sourceList1;

    private List<N> sourceList2;

    /**
     * 交集
     */
    private Set<CollectionContainer<K>> intersectionSet;

    /**
     * 并集
     */
    private Set<CollectionContainer<K>> unionSet;

    /**
     * 正差集 A - B
     */
    private Set<CollectionContainer<K>> positiveDifferenceSet;

    /**
     * 负差集 B - A
     */
    private Set<CollectionContainer<K>> negativeDifferenceSet;

    public static <K, M, N> CollectionMathResult<K, M, N> empty() {
        return new CollectionMathResult<K, M, N>()
                    .setIntersectionSet(emptySet())
                    .setUnionSet(emptySet())
                    .setPositiveDifferenceSet(emptySet())
                    .setNegativeDifferenceSet(emptySet());
    }

    private CollectionMathResult(List<M> sourceList1, List<N> sourceList2) {
        this.sourceList1 = sourceList1;
        this.sourceList2 = sourceList2;
    }

    private CollectionMathResult() {
    }

    public static <K, M, N> CollectionMathResult<K, M, N> build(List<M> sourceList1, List<N> sourceList2) {
        return new CollectionMathResult<>(sourceList1, sourceList2);
    }

    public Set<CollectionContainer<K>> getIntersectionSet() {
        return intersectionSet;
    }

    public Set<K> getIntersectionKeySet() {
        return intersectionSet.stream().map(CollectionContainer::getDataKey).collect(Collectors.toSet());
    }

    public CollectionMathResult<K, M, N> setIntersectionSet(Set<CollectionContainer<K>> intersectionSet) {
        this.intersectionSet = intersectionSet;
        return this;
    }

    public Set<K> getUnionKeySet() {
        return unionSet.stream().map(CollectionContainer::getDataKey).collect(Collectors.toSet());
    }

    public Set<CollectionContainer<K>> getUnionSet() {
        return unionSet;
    }

    public CollectionMathResult<K, M, N> setUnionSet(Set<CollectionContainer<K>> unionSet) {
        this.unionSet = unionSet;
        return this;
    }

    public Set<K> getPositiveDifferenceKeySet() {
        return positiveDifferenceSet.stream().map(CollectionContainer::getDataKey).collect(Collectors.toSet());
    }

    public Set<CollectionContainer<K>> getPositiveDifferenceSet() {
        return positiveDifferenceSet;
    }

    public CollectionMathResult<K, M, N> setPositiveDifferenceSet(Set<CollectionContainer<K>> positiveDifferenceSet) {
        this.positiveDifferenceSet = positiveDifferenceSet;
        return this;
    }

    public Set<K> getNegativeDifferenceKeySet() {
        return negativeDifferenceSet.stream().map(CollectionContainer::getDataKey).collect(Collectors.toSet());
    }

    public Set<CollectionContainer<K>> getNegativeDifferenceSet() {
        return negativeDifferenceSet;
    }

    public CollectionMathResult<K, M, N> setNegativeDifferenceSet(Set<CollectionContainer<K>> negativeDifferenceSet) {
        this.negativeDifferenceSet = negativeDifferenceSet;
        return this;
    }
}
