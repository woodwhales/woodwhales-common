package org.woodwhales.common.business.collection;

import com.google.common.collect.Sets;
import org.woodwhales.common.business.DataTool;

import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author woodwhales on 2020-12-13 16:32
 * @description
 */
public class CollectionMathResult<K, M, N> implements CollectionMath<K, M, N> {

    /**
     * 集合 A
     */
    private List<M> sourceList1;

    /**
     * 集合 B
     */
    private List<N> sourceList2;

    /**
     * 交集
     */
    private Set<CollectionFieldComparable<K>> intersectionSet;

    /**
     * 并集
     */
    private Set<CollectionFieldComparable<K>> unionSet;

    /**
     * 正差集 A - B
     */
    private Set<CollectionFieldComparable<K>> positiveDifferenceSet;

    /**
     * 负差集 B - A
     */
    private Set<CollectionFieldComparable<K>> negativeDifferenceSet;

    /**
     * 是否懒计算
     * 是 - 每调用方法时计算
     * 否 - new 实例完成时初始化
     */
    private boolean lazyCompute;

    /**
     * 数据源集合 A 生成 set 集合的接口
     */
    private Function<M, K> keyFunction1;

    /**
     * 数据源集合 B 生成 set 集合的接口
     */
    private Function<N, K> keyFunction2;

    /**
     * 集合操作
     * @param sourceList1 集合 A
     * @param keyFunction1 集合 A 生成 set 的接口
     * @param sourceList2 集合 B
     * @param keyFunction2 集合 B 生成 set 的接口
     * @param <K> 要比较的数据类型
     * @param <M> 集合 A 的数据类型
     * @param <N> 集合 B 的数据类型
     * @return
     */
    public static <K, M, N> CollectionMathResult<K, M, N> compute(List<M> sourceList1,
                                                                  Function<M, K> keyFunction1,
                                                                  List<N> sourceList2,
                                                                  Function<N, K> keyFunction2) {
        return compute(sourceList1, keyFunction1, sourceList2, keyFunction2, false);
    }

    /**
     * 集合操作
     * @param sourceList1 集合 A
     * @param keyFunction1 集合 A 生成 set 的接口
     * @param sourceList2 集合 B
     * @param keyFunction2 集合 B 生成 set 的接口
     * @param lazyCompute 是否懒计算
     * @param <K> 要比较的数据类型
     * @param <M> 集合 A 的数据类型
     * @param <N> 集合 B 的数据类型
     * @return
     */
    public static <K, M, N> CollectionMathResult<K, M, N> compute(List<M> sourceList1,
                                                                  Function<M, K> keyFunction1,
                                                                  List<N> sourceList2,
                                                                  Function<N, K> keyFunction2,
                                                                  final boolean lazyCompute) {
        CollectionMathResult<K, M, N> collectionMathResult = new CollectionMathResult<>(sourceList1, keyFunction1,
                                                                                        sourceList2, keyFunction2,
                                                                                        lazyCompute);
        if(lazyCompute) {
            return collectionMathResult;
        }

        if(isNotEmpty(sourceList1) && isEmpty(sourceList2)) {
            checkNotNull(keyFunction1, "keyFunction1 不允许为空");
            Set<CollectionFieldComparable<K>> set = DataTool.toSet(sourceList1, source1 -> CollectionContainer.build(source1, keyFunction1));
            collectionMathResult.setUnionSet(set)
                    .setPositiveDifferenceSet(set)
                    .setIntersectionSet(emptySet())
                    .setNegativeDifferenceSet(emptySet());
            return collectionMathResult;
        }

        if(isEmpty(sourceList1) && isNotEmpty(sourceList2)) {
            checkNotNull(keyFunction1, "keyFunction2 不允许为空");
            Set<CollectionFieldComparable<K>> set = DataTool.toSet(sourceList2, source2 -> CollectionContainer.build(source2, keyFunction2));
            collectionMathResult.setUnionSet(set)
                    .setPositiveDifferenceSet(emptySet())
                    .setIntersectionSet(emptySet())
                    .setNegativeDifferenceSet(set);
            return collectionMathResult;
        }

        checkNotNull(keyFunction1, "keyFunction1 不允许为空");
        checkNotNull(keyFunction1, "keyFunction2 不允许为空");

        Set<CollectionFieldComparable<K>> set1 = DataTool.toSet(sourceList1, source1 -> CollectionContainer.build(source1, keyFunction1));
        Set<CollectionFieldComparable<K>> set2 = DataTool.toSet(sourceList2, source2 -> CollectionContainer.build(source2, keyFunction2));
        collectionMathResult.setPositiveDifferenceSet(Sets.difference(set1, set2))
                .setNegativeDifferenceSet(Sets.difference(set2, set1))
                .setUnionSet(Sets.union(set1, set2))
                .setIntersectionSet(Sets.intersection(set1, set2));
        return collectionMathResult;
    }

    public List<M> getSourceList1() {
        return sourceList1;
    }

    public List<N> getSourceList2() {
        return sourceList2;
    }

    @Override
    public Set<CollectionFieldComparable<K>> getIntersectionSet() {
        return getSet(intersectionSet, (set1, set2) -> Sets.intersection(set1, set2));
    }

    @Override
    public Set<K> getIntersectionKeySet() {
        return getKeySet(intersectionSet, getIntersectionSet());
    }

    public CollectionMathResult<K, M, N> setIntersectionSet(Set<CollectionFieldComparable<K>> intersectionSet) {
        this.intersectionSet = intersectionSet;
        return this;
    }

    @Override
    public Set<K> getUnionKeySet() {
        return getKeySet(unionSet, getUnionSet());
    }

    @Override
    public Set<CollectionFieldComparable<K>> getUnionSet() {
        return getSet(unionSet, Sets::union);
    }

    public CollectionMathResult<K, M, N> setUnionSet(Set<CollectionFieldComparable<K>> unionSet) {
        this.unionSet = unionSet;
        return this;
    }

    /**
     * 正差集 A - B
     * @return
     */
    @Override
    public Set<K> getPositiveDifferenceKeySet() {
        return getKeySet(positiveDifferenceSet, getPositiveDifferenceSet());
    }

    /**
     * 正差集 A - B
     * @return
     */
    @Override
    public List<M> getPositiveDifferenceList() {
        return getList(this.sourceList1, getPositiveDifferenceSet(), keyFunction1);
    }

    /**
     * 正差集 A - B
     * @return
     */
    @Override
    public Set<CollectionFieldComparable<K>> getPositiveDifferenceSet() {
        return getSet(positiveDifferenceSet, (set1, set2) -> CollectionTool.difference(set1, set2));
    }

    /**
     * 负差集 B - A
     * @return
     */
    @Override
    public Set<K> getNegativeDifferenceKeySet() {
        return getKeySet(negativeDifferenceSet, getNegativeDifferenceSet());
    }

    /**
     * 负差集 B - A
     * @return
     */
    @Override
    public List<N> getNegativeDifferenceList() {
        return getList(this.sourceList2, getNegativeDifferenceSet(), keyFunction2);
    }

    /**
     * 负差集 B - A
     * @return
     */
    @Override
    public Set<CollectionFieldComparable<K>> getNegativeDifferenceSet() {
        return getSet(negativeDifferenceSet, (set1, set2) -> CollectionTool.difference(set2, set1));
    }

    private static <K, M, N> CollectionMathResult<K, M, N> empty() {
        return new CollectionMathResult<K, M, N>()
                .setIntersectionSet(emptySet())
                .setUnionSet(emptySet())
                .setPositiveDifferenceSet(emptySet())
                .setNegativeDifferenceSet(emptySet());
    }

    private CollectionMathResult(List<M> sourceList1,
                                 Function<M, K> keyFunction1,
                                 List<N> sourceList2,
                                 Function<N, K> keyFunction2,
                                 final boolean lazyCompute) {
        this.sourceList1 = sourceList1;
        this.keyFunction1 = keyFunction1;
        this.sourceList2 = sourceList2;
        this.keyFunction2 = keyFunction2;
        this.lazyCompute = lazyCompute;
    }

    private CollectionMathResult() {
    }

    private Set<K> getKeySet(Set<CollectionFieldComparable<K>> set, Set<CollectionFieldComparable<K>> keySet) {
        if (this.lazyCompute) {
            return keySet.stream()
                    .map(CollectionFieldComparable::getDataKey)
                    .collect(Collectors.toSet());
        }
        return getKeySet(set);
    }

    private Set<K> getKeySet(Set<CollectionFieldComparable<K>> set) {
        return CollectionTool.toSet(set, CollectionFieldComparable::getDataKey);
    }

    private Set<CollectionFieldComparable<K>> getSet(Set<CollectionFieldComparable<K>> set,
                                                     BinaryOperator<Set<CollectionFieldComparable<K>>> setFunction) {
        if(!lazyCompute) {
            return set;
        }
        Set<CollectionFieldComparable<K>> set1 = DataTool.toSet(sourceList1, source1 -> CollectionContainer.build(source1, keyFunction1));
        Set<CollectionFieldComparable<K>> set2 = DataTool.toSet(sourceList2, source2 -> CollectionContainer.build(source2, keyFunction2));
        return setFunction.apply(set1, set2);
    }

    private <T, K> List<T> getList(List<T> list, Set<CollectionFieldComparable<K>> set, Function<T, K> keyFunction) {
        if(isEmpty(list)) {
            return emptyList();
        }

        Set<K> keySet = set.stream()
                            .map(CollectionFieldComparable::getDataKey)
                            .collect(Collectors.toSet());

        return DataTool.filter(list, source -> keySet.contains(keyFunction.apply(source)));
    }

    public CollectionMathResult<K, M, N> setPositiveDifferenceSet(Set<CollectionFieldComparable<K>> positiveDifferenceSet) {
        this.positiveDifferenceSet = positiveDifferenceSet;
        return this;
    }

    public CollectionMathResult<K, M, N> setNegativeDifferenceSet(Set<CollectionFieldComparable<K>> negativeDifferenceSet) {
        this.negativeDifferenceSet = negativeDifferenceSet;
        return this;
    }
}
