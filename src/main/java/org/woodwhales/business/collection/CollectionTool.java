package org.woodwhales.business.collection;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptySet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.woodwhales.business.collection.CollectionContainer.build;
import static org.woodwhales.business.collection.CollectionMathResult.build;

/**
 * @author woodwhales on 2020-12-07
 * @description
 */
public class CollectionTool {

    private CollectionTool() {

    }

    /**
     * set1 和 set2 集合进行差集运算
     * @param set1
     * @param set2
     * @param <E>
     * @return
     */
    public static <E> Set<E> difference(Set<E> set1, Set<E> set2) {
        if(isEmpty(set1)) {
            return emptySet();
        }

        if(isEmpty(set2)) {
            return set1;
        }

        return Sets.difference(set1, set2);
    }

    /**
     * 集合数学计算（交集、并集、正差集、反差集）默认非懒获取
     * @param sourceList1 要交集的集合1
     * @param keyFunction1 生成 key 接口
     * @param sourceList2 要交集的集合2
     * @param keyFunction2 生成 key 接口
     * @param <K> 俩者集合元素共有类型
     * @param <M> 集合1数据类型
     * @param <N> 集合2数据类型
     * @return
     */
    public static <K, M, N> CollectionMathResult<K, M, N> compute(List<M> sourceList1,
                                                                  Function<M, K> keyFunction1,
                                                                  List<N> sourceList2,
                                                                  Function<N, K> keyFunction2) {
        return compute(sourceList1, keyFunction1, sourceList2, keyFunction2, false);
    }

    /**
     * 集合数学计算（交集、并集、正差集、反差集）
     * @param sourceList1 要交集的集合1
     * @param keyFunction1 生成 key 接口
     * @param sourceList2 要交集的集合2
     * @param keyFunction2 生成 key 接口
     * @param <K> 俩者集合元素共有类型
     * @param <M> 集合1数据类型
     * @param <N> 集合2数据类型
     * @param lazyCompute 是否懒获取
     * @return
     */
    public static <K, M, N> CollectionMathResult<K, M, N> compute(List<M> sourceList1,
                                                                  Function<M, K> keyFunction1,
                                                                  List<N> sourceList2,
                                                                  Function<N, K> keyFunction2,
                                                                  final boolean lazyCompute) {
        if(isEmpty(sourceList1) && isEmpty(sourceList2)) {
            return CollectionMathResult.empty();
        }

        CollectionMathResult<K, M, N> collectionMathResult = build(sourceList1, sourceList2, lazyCompute);

        if(lazyCompute) {
            return collectionMathResult;
        }

        if(isNotEmpty(sourceList1) && isEmpty(sourceList2)) {
            checkNotNull(keyFunction1, "keyFunction1 不允许为空");
            Set<CollectionFieldComparable<K>> set = toSet(sourceList1, source1 -> build(source1, keyFunction1));
            collectionMathResult.setUnionSet(set)
                    .setPositiveDifferenceSet(set)
                    .setIntersectionSet(emptySet())
                    .setNegativeDifferenceSet(emptySet());
            return collectionMathResult;
        }

        if(isEmpty(sourceList1) && isNotEmpty(sourceList2)) {
            checkNotNull(keyFunction1, "keyFunction2 不允许为空");
            Set<CollectionFieldComparable<K>> set = toSet(sourceList2, source2 -> build(source2, keyFunction2));
            collectionMathResult.setUnionSet(set)
                    .setPositiveDifferenceSet(emptySet())
                    .setIntersectionSet(emptySet())
                    .setNegativeDifferenceSet(set);
            return collectionMathResult;
        }

        checkNotNull(keyFunction1, "keyFunction1 不允许为空");
        checkNotNull(keyFunction1, "keyFunction2 不允许为空");

        Set<CollectionFieldComparable<K>> set1 = toSet(sourceList1, source1 -> build(source1, keyFunction1));
        Set<CollectionFieldComparable<K>> set2 = toSet(sourceList2, source2 -> build(source2, keyFunction2));
        collectionMathResult.setPositiveDifferenceSet(difference(set1, set2))
                .setNegativeDifferenceSet(difference(set2, set1))
                .setUnionSet(Sets.union(set1, set2))
                .setIntersectionSet(Sets.intersection(set1, set2));
        return collectionMathResult;
    }

    public static <T, R> Set<R> toSet(Set<T> set, Function<T, R> function) {
        if(isEmpty(set)) {
            return emptySet();
        }

        checkNotNull("function 不允许为空");
        return set.stream().map(function).collect(Collectors.toSet());
    }

    public static <T, R> Set<R> toSet(List<T> list, Function<T, R> function) {
        if(isEmpty(list)) {
            return emptySet();
        }

        checkNotNull("function 不允许为空");
        return list.stream().map(function).collect(Collectors.toSet());
    }
}
