package cn.woodwhales.common.business.chinese;

import com.ibm.icu.text.Collator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author woodwhales
 * 2020-12-07 22:19
 */
public class ChineseDataTool {

    public static final Collator collator = com.ibm.icu.text.Collator.getInstance(com.ibm.icu.util.ULocale.SIMPLIFIED_CHINESE);

    /**
     * 根据 dataList 进行中文字段排序
     *
     * @param dataList       原始数据
     * @param stringFunction 中文排序接口
     * @param <T>            数据集合泛型
     * @return 已排序的结果集
     */
    public static <T> List<T> sortedList(List<T> dataList, Function<T, String> stringFunction) {
        if (isEmpty(dataList)) {
            return emptyList();
        }

        List<ChineseDataContainer<T>> chineseDataContainerList = ChineseDataContainer.build(dataList, stringFunction);

        return chineseDataContainerList.stream()
                .sorted(ChineseDataContainer::compare)
                .map(ChineseDataContainer::getData)
                .collect(Collectors.toList());
    }
}
