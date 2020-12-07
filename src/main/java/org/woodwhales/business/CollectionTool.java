package org.woodwhales.business;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

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
            return Collections.emptySet();
        }

        if(isEmpty(set2)) {
            return set1;
        }

        return Sets.difference(set1, set2);
    }

}
