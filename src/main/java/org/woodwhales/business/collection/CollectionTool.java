package org.woodwhales.business.collection;

import com.google.common.collect.Sets;
import org.checkerframework.checker.units.qual.K;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.woodwhales.business.DataTool.toSet;
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
     * 集合数学计算（交集、并集、正差集、反差集）
     * @param sourceList1 要交集的集合1
     * @param keyFunction1 生成 key 接口
     * @param sourceList2 要交集的集合2
     * @param keyFunction2 生成 key 接口
     * @param <K> 俩者集合元素共有类型
     * @param <M> 集合1数据类型
     * @param <N> 集合2数据类型
     * @return
     */
    public static <K, M, N> CollectionMathResult<K, M, N> mathCollection(List<M> sourceList1,
                                                                   Function<M, K> keyFunction1,
                                                                   List<N> sourceList2,
                                                                   Function<N, K> keyFunction2) {
        if(isEmpty(sourceList1) && isEmpty(sourceList2)) {
            return CollectionMathResult.empty();
        }

        if(isEmpty(sourceList1) && isNotEmpty(sourceList2)) {
            checkNotNull(keyFunction2, "keyFunction2 不允许为空");
            return buildCollectionMathResult(sourceList2, keyFunction2);
        }

        if(isEmpty(sourceList2) && isNotEmpty(sourceList1)) {
            checkNotNull(keyFunction1, "keyFunction1 不允许为空");
            return buildCollectionMathResult(sourceList1, keyFunction1);
        }

        checkNotNull(keyFunction1, "keyFunction1 不允许为空");
        checkNotNull(keyFunction1, "keyFunction2 不允许为空");

        Set<CollectionContainer<K>> set1 = toSet(sourceList1, source1 -> CollectionContainer.build(source1, keyFunction1));
        Set<CollectionContainer<K>> set2 = toSet(sourceList2, source2 -> CollectionContainer.build(source2, keyFunction2));

        return build(sourceList1, sourceList2)
                    .setPositiveDifferenceSet(difference(set1, set2))
                    .setNegativeDifferenceSet(difference(set2, set1))
                    .setUnionSet(Sets.union(set1, set2))
                    .setIntersectionSet(Sets.intersection(set1, set2));
    }

    private static <K, M, N> CollectionMathResult<K, M, N> buildCollectionMathResult(List<M> sourceList,
                                                                                     Function<M, K> keyFunction,
                                                                                     List<N> emptyList) {
        CollectionMathResult<K, M, N> collectionMathResult = build(sourceList, emptyList);
        Set<CollectionContainer<K>> set = toSet(sourceList, source -> CollectionContainer.build(source, keyFunction));
        collectionMathResult.setUnionSet(set);
        collectionMathResult.setNegativeDifferenceSet(set);
        collectionMathResult.setPositiveDifferenceSet(emptySet());
        collectionMathResult.setIntersectionSet(emptySet());
        return collectionMathResult;
    }

}
