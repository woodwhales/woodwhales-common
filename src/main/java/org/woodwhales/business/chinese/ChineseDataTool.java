package org.woodwhales.business.chinese;

import com.ibm.icu.text.Collator;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author woodwhales
 * @create 2020-12-07 22:19
 */
public class ChineseDataTool {

    public static final Collator collator = com.ibm.icu.text.Collator.getInstance(com.ibm.icu.util.ULocale.SIMPLIFIED_CHINESE);

    /**
     * 根据 dataList 进行中文字段排序
     * @param dataList
     * @param chineseInterface
     * @param <T>
     * @return
     */
    public static <T> List<T> sortedList(List<T> dataList, ChineseInterface<T> chineseInterface) {
        if(isEmpty(dataList)) {
            return emptyList();
        }

        return ChineseDataContainer.build(dataList, chineseInterface)
                                .stream()
                                .sorted(ChineseDataContainer::compare)
                                .map(ChineseDataContainer::getData)
                                .collect(Collectors.toList());
    }
}
