package org.woodwhales.business;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;

/**
 * 业务处理工具类
 * @author woodwhales
 * @create 2020-11-17 13:44
 */
public class DataTool {


    /**
     * list 转 map 集合
     * @param source
     * @param keyMapper
     * @param valueMapper
     * @param <K>
     * @param <S>
     * @param <T>
     * @return
     */
    public static <K, S, T> Map<K,T> toMap(List<S> source,
                                           Function<? super S, ? extends K> keyMapper,
                                           Function<? super S, ? extends T> valueMapper) {
        if(null == source || source.size() == 0) {
            return Collections.emptyMap();
        }

        return source.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * list 转 map 集合
     * @param source
     * @param keyMapper
     * @param valueMapper
     * @param mergeFunction
     * @param <K>
     * @param <S>
     * @param <T>
     * @return
     */
    public static <K, S, T> Map<K,T> toMap(List<S> source,
                                           Function<? super S, ? extends K> keyMapper,
                                           Function<? super S, ? extends T> valueMapper,
                                           BinaryOperator<T> mergeFunction) {
        if(null == source || source.size() == 0) {
            return Collections.emptyMap();
        }

        return source.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    /**
     * list 转 map 集合
     * map 的 value 为集合元素本身
     * @param source
     * @param keyMapper
     * @param <K>
     * @param <S>
     * @return
     */
    public static <K, S> Map<K, S> toMap(List<S> source,
                                         Function<? super S, ? extends K> keyMapper) {
        return toMap(source, keyMapper, identity());
    }

    /**
     * list 转 map 集合
     * map 的 value 为集合元素本身
     * @param source
     * @param keyMapper
     * @param mergeFunction
     * @param <K>
     * @param <S>
     * @return
     */
    public static <K, S> Map<K, S> toMap(List<S> source,
                                         Function<? super S, ? extends K> keyMapper,
                                         BinaryOperator<S> mergeFunction) {
        return toMap(source, keyMapper, identity(), mergeFunction);
    }

    /**
     * list 转 map 集合
     * map 的 value 为集合元素本身
     * 如果出现 key 重复，则取前一个元素
     * @param source
     * @param keyMapper
     * @param <K>
     * @param <S>
     * @return
     */
    public static <K, S> Map<K, S> toMapForSaveOld(List<S> source,
                                                   Function<? super S, ? extends K> keyMapper) {
        return toMap(source, keyMapper, identity(), (o1, o2) -> o1);
    }

    /**
     * list 转 map 集合
     * 如果出现 key 重复，则取前一个元素
     * @param source
     * @param keyMapper
     * @param valueMapper
     * @param <K>
     * @param <S>
     * @param <T>
     * @return
     */
    public static <K, S, T> Map<K, T> toMapForSaveOld(List<S> source,
                                                      Function<? super S, ? extends K> keyMapper,
                                                      Function<? super S, ? extends T> valueMapper) {
        return toMap(source, keyMapper, valueMapper, (o1, o2) -> o1);
    }

    /**
     * list 转 map 集合
     * map 的 value 为集合元素本身
     * 如果出现 key 重复，则取前一个元素
     * @param source
     * @param keyMapper
     * @param <K>
     * @param <S>
     * @return
     */
    public static <K, S> Map<K, S> toMapForSaveNew(List<S> source,
                                                   Function<? super S, ? extends K> keyMapper) {
        return toMap(source, keyMapper, identity(), (o1, o2) -> o2);
    }

    /**
     * list 转 map 集合
     * 如果出现 key 重复，则取前一个元素
     * @param source
     * @param keyMapper
     * @param valueMapper
     * @param <K>
     * @param <S>
     * @param <T>
     * @return
     */
    public static <K, S, T> Map<K, T> toMapForSaveNew(List<S> source,
                                                      Function<? super S, ? extends K> keyMapper,
                                                      Function<? super S, ? extends T> valueMapper) {
        return toMap(source, keyMapper, valueMapper, (o1, o2) -> o2);
    }

    /**
     * 将 list 集合分组
     * @param source
     * @param classifier
     * @param <K>
     * @param <S>
     * @return
     */
    public static <K, S> Map<K, List<S>> groupingBy(List<S> source,
                                                    Function<? super S, ? extends K> classifier) {
        if(null == source || source.size() == 0) {
            return Collections.emptyMap();
        }

        return source.stream().collect(Collectors.groupingBy(classifier));
    }

    /**
     * 对集合数据进行去重器
     * @param source
     * @param deduplicatedInterface
     * @param <K>
     * @param <T>
     * @return
     */
    public static <K, T> DeduplicateResult<T> deduplicate(List<T> source,
                                                          DeduplicatedInterface<K, T> deduplicatedInterface) {
        if(null == source || source.size() == 0) {
            return new DeduplicateResult<T>(source, emptyList(), emptyList(), emptyList());
        }

        Map<K, T> container =  new LinkedHashMap();
        // 无效的数据集合
        List<T> invalidList = new LinkedList<>();
        // 重复的数据集合
        List<T> repetitiveList = new LinkedList<>();

        for (T data : source) {
            if (!deduplicatedInterface.isValid(data)) {
                invalidList.add(data);
            } else {
                K deduplicatedKey = deduplicatedInterface.getDeduplicatedKey(data);
                T putData = container.put(deduplicatedKey, data);
                if(Objects.nonNull(putData)) {
                    repetitiveList.add(putData);
                }
            }
        }

        // 已去重的数据集合
        List<T> deduplicatedList = new ArrayList(container.values());

        return new DeduplicateResult<T>(source, invalidList, deduplicatedList, repetitiveList);
    }

}
