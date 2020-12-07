package org.woodwhales.business.chinese;

import com.google.common.collect.ComparisonChain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author woodwhales
 * @create 2020-12-07 22:27
 */
public class ChineseDataContainer<T> {

    private T data;
    private String field;

    public ChineseDataContainer(T data, ChineseInterface<T> chineseInterface) {
        this.data = data;
        this.field = chineseInterface.getSortedField(data);
    }

    public static <T> List<ChineseDataContainer<T>> build(List<T> dataList, ChineseInterface<T> chineseInterface) {
        if(isEmpty(dataList)) {
            return Collections.emptyList();
        }

        List<ChineseDataContainer<T>> list = new ArrayList<>(dataList.size());
        for (T data : dataList) {
            list.add(new ChineseDataContainer(data, chineseInterface));
        }
        return list;
    }

    /**
     * 对中文字段进行比较
     * @param chineseDataContainer
     * @return
     */
    public int compare(ChineseDataContainer chineseDataContainer) {
        return ComparisonChain.start()
                .compare(this.field, chineseDataContainer.getField(), ChineseDataTool.collator)
                .result();
    }

    public T getData() {
        return data;
    }

    public String getField() {
        return field;
    }
}
