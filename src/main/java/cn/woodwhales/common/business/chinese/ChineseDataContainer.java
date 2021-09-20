package cn.woodwhales.common.business.chinese;

import cn.woodwhales.common.business.DataTool;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

import java.util.List;
import java.util.function.Function;

/**
 * 数据存储对象
 * @author woodwhales
 * 2020-12-07 22:27
 */
public class ChineseDataContainer<T> {

    /**
     * 原始数据对象
     */
    private T data;

    /**
     * 要排序的对象属性名称
     */
    private String field;

    public ChineseDataContainer(T data, Function<T, String> stringFunction) {
        Preconditions.checkNotNull(data);
        Preconditions.checkNotNull(stringFunction);
        this.data = data;
        this.field = stringFunction.apply(data);
    }

    public static <T> List<ChineseDataContainer<T>> build(List<T> dataList, Function<T, String> stringFunction) {
        return DataTool.toList(dataList, data -> new ChineseDataContainer(data, stringFunction));
    }

    /**
     * 对中文字段进行比较
     * @param chineseDataContainer 中文数据容器
     * @return 比较结果值
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
