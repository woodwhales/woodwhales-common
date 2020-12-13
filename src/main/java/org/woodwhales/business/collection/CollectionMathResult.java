package org.woodwhales.business.collection;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * @author woodwhales on 2020-12-13 16:32
 * @description
 */
public class CollectionMathResult<K, M, N> implements CollectionMath<K> {

    private List<M> sourceList1;

    private List<N> sourceList2;

    /**
     * 交集
     */
    private Set<CollectionFieldComparable<K>> intersectionSet;

    /**
     * 交集的 key 集合
     */
    private Set<K> intersectionKeySet;

    /**
     * 并集
     */
    private Set<CollectionFieldComparable<K>> unionSet;

    /**
     * 并集的 key 集合
     */
    private Set<K> unionKeySet;

    /**
     * 正差集 A - B
     */
    private Set<CollectionFieldComparable<K>> positiveDifferenceSet;

    /**
     * 正差集 A - B 的 key 集合
     */
    private Set<K> positiveDifferenceKeySet;

    /**
     * 负差集 B - A
     */
    private Set<CollectionFieldComparable<K>> negativeDifferenceSet;

    /**
     * 负差集 B - A 的 key 集合
     */
    private Set<K> negativeDifferenceKeySet;

    /**
     * 是否懒计算
     * 是 - 每调用方法时计算
     * 否 - new 实例完成时初始化
     */
    private boolean lazyCompute;

    public static <K, M, N> CollectionMathResult<K, M, N> empty() {
        return new CollectionMathResult<K, M, N>()
                    .setIntersectionSet(emptySet())
                    .setUnionSet(emptySet())
                    .setPositiveDifferenceSet(emptySet())
                    .setNegativeDifferenceSet(emptySet());
    }

    private CollectionMathResult(List<M> sourceList1,
                                 List<N> sourceList2,
                                 final boolean lazyCompute) {
        this.sourceList1 = sourceList1;
        this.sourceList2 = sourceList2;
        this.lazyCompute = lazyCompute;
    }

    private CollectionMathResult() {
    }

    public static <K, M, N> CollectionMathResult<K, M, N> build(List<M> sourceList1,
                                                                List<N> sourceList2,
                                                                final boolean lazyCompute) {
        return new CollectionMathResult<>(sourceList1, sourceList2, lazyCompute);
    }

    public List<M> getSourceList1() {
        return sourceList1;
    }

    public List<N> getSourceList2() {
        return sourceList2;
    }

    @Override
    public Set<CollectionFieldComparable<K>> getIntersectionSet() {
        if(lazyCompute) {
            // TODO 动态获取
            return intersectionSet;
        }

        return intersectionSet;

    }

    @Override
    public Set<K> getIntersectionKeySet() {
        if(lazyCompute) {
            return getKeySet(intersectionSet);
        }
        return intersectionKeySet;
    }

    public CollectionMathResult<K, M, N> setIntersectionSet(Set<CollectionFieldComparable<K>> intersectionSet) {
        this.intersectionSet = intersectionSet;
        if(!lazyCompute) {
            this.intersectionKeySet = getKeySet(intersectionSet);
        }
        return this;
    }

    @Override
    public Set<K> getUnionKeySet() {
        if(lazyCompute) {
            return getKeySet(unionSet);
        }
        return unionKeySet;
    }

    @Override
    public Set<CollectionFieldComparable<K>> getUnionSet() {
        // TODO 动态获取
        if(lazyCompute) {
            return unionSet;
        }
        return unionSet;
    }

    public CollectionMathResult<K, M, N> setUnionSet(Set<CollectionFieldComparable<K>> unionSet) {
        this.unionSet = unionSet;
        if(!lazyCompute) {
            this.unionKeySet = getKeySet(unionSet);
        }
        return this;
    }

    @Override
    public Set<K> getPositiveDifferenceKeySet() {
        if(lazyCompute) {
            return getKeySet(positiveDifferenceSet);
        }
        return positiveDifferenceKeySet;
    }

    @Override
    public Set<CollectionFieldComparable<K>> getPositiveDifferenceSet() {
        // TODO 动态获取
        if(lazyCompute) {
            return positiveDifferenceSet;
        }
        return positiveDifferenceSet;
    }

    public CollectionMathResult<K, M, N> setPositiveDifferenceSet(Set<CollectionFieldComparable<K>> positiveDifferenceSet) {
        this.positiveDifferenceSet = positiveDifferenceSet;
        if(!lazyCompute) {
            this.positiveDifferenceKeySet = getKeySet(positiveDifferenceSet);
        }
        return this;
    }

    @Override
    public Set<K> getNegativeDifferenceKeySet() {
        if(lazyCompute) {
            return getKeySet(negativeDifferenceSet);
        }

        return negativeDifferenceKeySet;
    }

    @Override
    public Set<CollectionFieldComparable<K>> getNegativeDifferenceSet() {
        // TODO 动态获取
        if(lazyCompute) {
            return negativeDifferenceSet;
        }
        return negativeDifferenceSet;
    }

    public CollectionMathResult<K, M, N> setNegativeDifferenceSet(Set<CollectionFieldComparable<K>> negativeDifferenceSet) {
        this.negativeDifferenceSet = negativeDifferenceSet;
        if(!lazyCompute) {
            this.negativeDifferenceKeySet = getKeySet(negativeDifferenceSet);
        }
        return this;
    }

    private Set<K> getKeySet(Set<CollectionFieldComparable<K>> set) {
        return CollectionTool.toSet(set, CollectionFieldComparable::getDataKey);
    }
}
