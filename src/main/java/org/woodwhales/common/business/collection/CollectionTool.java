package org.woodwhales.common.business.collection;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

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
     * 从原始 set 集合生成新的 set
     * @param set 源数据集合
     * @param function 生成新的 set 集合接口
     * @param <T> 源数据类型
     * @param <R>
     * @return
     */
    public static <T, R> Set<R> toSet(Set<T> set, Function<T, R> function) {
        if(isEmpty(set)) {
            return emptySet();
        }

        return set.stream().map(function).collect(Collectors.toSet());
    }
}
