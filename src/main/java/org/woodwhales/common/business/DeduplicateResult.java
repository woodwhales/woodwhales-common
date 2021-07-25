package org.woodwhales.common.business;

import java.util.List;

/**
 * @author woodwhales
 * @create 2020-11-17 13:45
 */
public class DeduplicateResult<T> {

    /**
     * 原始集合
     */
    private List<T> source;

    /**
     * 无效的集合
     */
    private List<T> invalidList;

    /**
     * 已去重的集合
     */
    private List<T> deduplicatedList;

    /**
     * 重复的集合
     */
    private List<T> repetitiveList;

    public DeduplicateResult(List<T> source,
                             List<T> invalidList,
                             List<T> deduplicatedList,
                             List<T> repetitiveList) {
        this.source = source;
        this.invalidList = invalidList;
        this.deduplicatedList = deduplicatedList;
        this.repetitiveList = repetitiveList;
    }

    public List<T> getSource() {
        return source;
    }

    public List<T> getDeduplicatedList() {
        return deduplicatedList;
    }

    public List<T> getRepetitiveList() {
        return repetitiveList;
    }

    public List<T> getInvalidList() {
        return invalidList;
    }
}
