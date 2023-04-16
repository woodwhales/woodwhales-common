package cn.woodwhales.common.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 随机抽取器
 * @author woodwhales on 2023-04-16 11:06
 */
public class RandomExtractor {

    /**
     * 按照指定样本率，从原始数据集合中随机抽取指定样本率的不重复元素
     * @param dataList 原始数据集合
     * @param randomDrawRate 随机抽取样本率
     * @return 随机抽取元素集合
     * @param <T> 原始数据集合类型
     */
    public static <T> List<T> randomDrawWithRate(List<T> dataList, double randomDrawRate) {
        if (randomDrawRate < 0 || randomDrawRate > 1) {
            throw new IllegalArgumentException("概率值不在 0 至 1 之间");
        }
        int randomDrawCount = (int) (CollectionUtils.size(dataList) * randomDrawRate);
        return randomDrawWithCount(dataList, randomDrawCount);
    }

    private static <T> List<T> randomDrawProcess(List<T> dataList, int randomDrawCount) {
        if (randomDrawCount < 0) {
            throw new IllegalArgumentException("随机总量不能小于 0");
        }
        List<T> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(dataList) || Objects.equals(randomDrawCount, 0)) {
            return result;
        }
        List<Integer> randomIndexList = randomIndexList(dataList.size());
        for (int i = 0; i < randomDrawCount; i++) {
            int index = randomIndexList.get(i);
            result.add(dataList.get(index));
        }
        return result;
    }

    /**
     * 按照指定样本量，从原始数据集合中随机抽取指定样本率的不重复元素
     * @param dataList 原始数据集合
     * @param randomDrawCount 随机抽取样本量
     * @return 随机抽取元素集合
     * @param <T> 原始数据集合类型
     */
    public static <T> List<T> randomDrawWithCount(List<T> dataList, Integer randomDrawCount) {
        return randomDrawProcess(dataList, randomDrawCount);
    }

    private static List<Integer> randomIndexList(Integer initSize) {
        List<Integer> indexList = new ArrayList<>(initSize);
        for (int i = 0; i < initSize; i++) {
            indexList.add(i);
        }
        Collections.shuffle(indexList);
        return indexList;
    }

}
