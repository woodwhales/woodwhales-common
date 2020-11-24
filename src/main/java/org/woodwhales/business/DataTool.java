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
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param valueMapper map 集合中的 value 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @param <T> map 集合中的 value 类型
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
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param valueMapper map 集合中的 value 获取规则
     * @param mergeFunction 存在相同 key 时取 value 的规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @param <T> map 集合中的 value 类型
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
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @return
     */
    public static <K, S> Map<K, S> toMap(List<S> source,
                                         Function<? super S, ? extends K> keyMapper) {
        return toMap(source, keyMapper, identity());
    }

    /**
     * list 转 map 集合
     * map 的 value 为集合元素本身
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param mergeFunction 存在相同 key 时取 value 的规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @return
     */
    public static <K, S> Map<K, S> toMap(List<S> source,
                                         Function<? super S, ? extends K> keyMapper,
                                         BinaryOperator<S> mergeFunction) {
        return toMap(source, keyMapper, identity(), mergeFunction);
    }

    /**
     * 将原始的 list 按照 mapper 规则转成新的 list
     * 再按照新的 list 按照 keyMapper 为生成 key 规则转成 map 集合
     * @param source
     * @param mapper
     * @param keyMapper
     * @param <S>
     * @param <T>
     * @param <K>
     * @return
     */
    public static <S, T, K> Map<K, T> toMapFromList(List<S> source,
                                            Function<? super S, ? extends T> mapper,
                                            Function<? super T, ? extends K> keyMapper) {
        if (null == source || source.size() == 0) {
            return Collections.emptyMap();
        }

        return toMap(source.stream().map(mapper).collect(Collectors.toList()), keyMapper);
    }

    /**
     * list 转 map 集合
     * map 的 value 为集合元素本身
     * 如果出现 key 重复，则取前一个元素
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @return
     */
    public static <K, S> Map<K, S> toMapForSaveOld(List<S> source,
                                                   Function<? super S, ? extends K> keyMapper) {
        return toMap(source, keyMapper, identity(), (o1, o2) -> o1);
    }

    /**
     * list 转 map 集合
     * 如果出现 key 重复，则取前一个元素
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param valueMapper map 集合中的 value 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @param <T> map 集合中的 value 类型
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
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @return
     */
    public static <K, S> Map<K, S> toMapForSaveNew(List<S> source,
                                                   Function<? super S, ? extends K> keyMapper) {
        return toMap(source, keyMapper, identity(), (o1, o2) -> o2);
    }

    /**
     * list 转 map 集合
     * 如果出现 key 重复，则取前一个元素
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param valueMapper map 集合中的 value 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @param <T> map 集合中的 value 类型
     * @return
     */
    public static <K, S, T> Map<K, T> toMapForSaveNew(List<S> source,
                                                      Function<? super S, ? extends K> keyMapper,
                                                      Function<? super S, ? extends T> valueMapper) {
        return toMap(source, keyMapper, valueMapper, (o1, o2) -> o2);
    }

    /**
     * 将原始的 list 按照 mapper 规则转成新的 list
     * @param source
     * @param mapper
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> toList(List<S> source, Function<? super S, ? extends T> mapper) {
        if (null == source || source.size() == 0) {
            return Collections.emptyList();
        }

        return source.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 将 list 集合分组
     * @param source 数据源集合
     * @param classifier 分组规则
     * @param <K> map 集合中的 key 类型
     * @param <S> map 集合中的 value 类型
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
     * 非线程安全
     * @param source 数据源集合
     * @param deduplicateInterface 去重器接口
     * @param <K> 去重属性的类型
     * @param <T> 数据源集合中元素的类型
     * @return
     */
    public static <K, T> DeduplicateResult<T> deduplicate(List<T> source,
                                                          DeduplicateInterface<K, T> deduplicateInterface) {
        if(null == source || source.size() == 0) {
            return new DeduplicateResult<T>(source, emptyList(), emptyList(), emptyList());
        }

        Map<K, T> container =  new LinkedHashMap<>();
        // 无效的数据集合
        List<T> invalidList = new LinkedList<>();
        // 重复的数据集合
        List<T> repetitiveList = new LinkedList<>();

        for (T data : source) {
            if (!deduplicateInterface.isValid(data)) {
                invalidList.add(data);
            } else {
                K deduplicatedKey = deduplicateInterface.getDeduplicatedKey(data);
                T putData = container.put(deduplicatedKey, data);
                if(Objects.nonNull(putData)) {
                    repetitiveList.add(putData);
                }
            }
        }

        // 已去重的数据集合
        List<T> deduplicatedList = new ArrayList<>(container.values());

        return new DeduplicateResult<T>(source, invalidList, deduplicatedList, repetitiveList);
    }

    /**
     * 枚举转 map 集合
     * @param sourceEnumClass 数据源枚举 Class类
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <T> map 集合中的 value 类型
     * @return
     */
    public static <K, T extends Enum<T>> Map<K, T> enumMap(Class<T> sourceEnumClass,
                                                         Function<? super T, ? extends K> keyMapper) {
        EnumSet<T> enumSet = EnumSet.allOf(sourceEnumClass);
        return enumSet.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    /**
     * 枚举转 map 集合
     * map 集合中的 key 为 Enum 的 name() 方法返回值
     * @param sourceEnumClass 数据源枚举 Class类
     * @param <T> map 集合中的 value 类型
     * @return
     */
    public static <T extends Enum<T>> Map<String, T> enumMap(Class<T> sourceEnumClass) {
        EnumSet<T> enumSet = EnumSet.allOf(sourceEnumClass);
        return enumSet.stream().collect(Collectors.toMap(Enum::name, Function.identity()));
    }

    /**
     * 判断 key 是否存在于枚举转 map 的集合中
     * @param key 要判断的 key
     * @param sourceEnumClass 数据源枚举 Class类
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <T> map 集合中的 value 类型
     * @return
     */
    public static <K, T extends Enum<T>> boolean enumContainsKey(K key, Class<T> sourceEnumClass,
                                                                 Function<? super T, ? extends K> keyMapper) {
        if(Objects.isNull(key)) {
            return false;
        }

        Map<? extends K, T> map = enumMap(sourceEnumClass, keyMapper);
        return map.containsKey(key);
    }

    /**
     * 根据 key 获取对应的枚举实例
     * @param key 要获取枚举实例的 key
     * @param sourceEnumClass 数据源枚举 Class类
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <T> map 集合中的 value 类型
     * @return
     */
    public static <K, T extends Enum<T>> T enumGetValue(K key, Class<T> sourceEnumClass,
                                                                 Function<? super T, ? extends K> keyMapper) {
        if(Objects.isNull(key)) {
            return null;
        }

        Map<? extends K, T> map = enumMap(sourceEnumClass, keyMapper);
        return map.get(key);
    }

}
