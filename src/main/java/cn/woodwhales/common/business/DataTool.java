package cn.woodwhales.common.business;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * 业务处理工具类
 * @author woodwhales
 * 2020-11-17 13:44
 */
public class DataTool {

    /**
     * list 转 set 集合
     * @param source 数据源集合
     * @param mapper 按照源数据的 mapper 规则生成 set 元素
     * @param <S> 数据源集合中元素的类型
     * @param <T> set 集合中元素的类型
     * @return set 集合
     */
    public static <S, T> Set<T> toSet(List<S> source,
                                      Function<? super S, ? extends T> mapper) {
        if(isEmpty(source)) {
            return Collections.emptySet();
        }

        return source.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * list 转 map 集合
     * @param source 数据源集合
     * @param keyMapper map 集合中的 key 获取规则
     * @param valueMapper map 集合中的 value 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <S> 数据源集合中元素的类型
     * @param <T> map 集合中的 value 类型
     * @return map 集合
     */
    public static <K, S, T> Map<K,T> toMap(List<S> source,
                                           Function<? super S, ? extends K> keyMapper,
                                           Function<? super S, ? extends T> valueMapper) {
        if(isEmpty(source)) {
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
     * @return map 集合
     */
    public static <K, S, T> Map<K,T> toMap(Collection<S> source,
                                           Function<? super S, ? extends K> keyMapper,
                                           Function<? super S, ? extends T> valueMapper,
                                           BinaryOperator<T> mergeFunction) {
        if(isEmpty(source)) {
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
     * @return map 集合
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
     * @return map 集合
     */
    public static <K, S> Map<K, S> toMap(List<S> source,
                                         Function<? super S, ? extends K> keyMapper,
                                         BinaryOperator<S> mergeFunction) {
        return toMap(source, keyMapper, identity(), mergeFunction);
    }

    /**
     * 将原始的 list 按照 mapper 规则转成新的 list
     * 再按照新的 list 按照 keyMapper 为生成 key 规则转成 map 集合
     * @param source 数据源集合
     * @param mapper list 转 map 的规则
     * @param keyMapper map 集合中的 key 获取规则
     * @param <S> 数据源集合中元素的类型
     * @param <T> 目标数据的类型
     * @param <K> map 集合中的 key 类型
     * @return list
     */
    public static <S, T, K> Map<K, T> toMapFromList(List<S> source,
                                            Function<? super S, ? extends T> mapper,
                                            Function<? super T, ? extends K> keyMapper) {
        if (isEmpty(source)) {
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
     * @return map 集合
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
     * @return map 集合
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
     * @return map 集合
     */
    public static <K, S> Map<K, S> toMapForSaveNew(Collection<S> source,
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
     * @return map 集合
     */
    public static <K, S, T> Map<K, T> toMapForSaveNew(List<S> source,
                                                      Function<? super S, ? extends K> keyMapper,
                                                      Function<? super S, ? extends T> valueMapper) {
        return toMap(source, keyMapper, valueMapper, (o1, o2) -> o2);
    }

    /**
     * 将原始的 list 按照 mapper 规则转成新的 list
     * @param source 源数据集合
     * @param mapper 生成新的 list 接口规则
     * @param <S> 源数据类型
     * @param <T> 目标数据类型
     * @return list
     */
    public static <S, T> List<T> toList(List<S> source,
                                        Function<? super S, ? extends T> mapper) {
        return toList(source, mapper, false);
    }

    /**
     * 将原始的 array 按照 mapper 规则转成新的 list
     * @param array 源数据数组
     * @param mapper 生成新的 list 接口规则
     * @param <S> 源数据类型
     * @param <T> 目标数据类型
     * @return list
     */
    public static <S, T> List<T> toList(S[] array,
                                        Function<? super S, ? extends T> mapper) {
        return toList(Arrays.asList(array), mapper);
    }

    /**
     * 将原始的 list 按照 mapper 规则转成新的 list
     * @param source 源数据集合
     * @param mapper 生成新的 list 接口规则
     * @param distinct 是否去重
     * @param <S> 源数据类型
     * @param <T> 目标数据类型
     * @return list
     */
    public static <S, T> List<T> toList(List<S> source,
                                        Function<? super S, ? extends T> mapper,
                                        boolean distinct) {
        if (isEmpty(source)) {
            return Collections.emptyList();
        }

        if(distinct) {
            return source.stream().map(mapper).distinct().collect(Collectors.toList());
        }

        return source.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * list 遍历并匹配 map 中的元素，根据 discard 决定元素未匹配到 map 是否丢弃
     * @param source 源数据集合
     * @param map mpa 集合
     * @param keyFunction 生成 key 的接口
     * @param mapContainKeyFunction 生成 list 的接口
     * @param discard 决定元素未匹配到 map 是否丢弃：true 丢弃，false 不丢弃
     * @param <S> 源数据集合元素类型
     * @param <T> 目标数据集合元素类型
     * @param <K> map 集合的 key 类型
     * @param <V> map 集合的 value 类型
     * @return list
     */
    private static <S, T, K, V> List<T> toListWithMap(List<S> source, Map<K, V> map,
                                                     Function<S, K> keyFunction,
                                                     BiFunction<S, V, T> mapContainKeyFunction,
                                                     boolean discard,
                                                     Function<S, T> function) {
        Preconditions.checkNotNull(keyFunction, "keyFunction不允许为空");
        Preconditions.checkNotNull(mapContainKeyFunction, "mapContainKeyFunction不允许为空");
        if(!discard) {
            Preconditions.checkNotNull(function, "list元素不存在map集合中的处理接口function不允许为空");
        }

        if (isEmpty(source)) {
            return Collections.emptyList();
        }

        if(MapUtils.isEmpty(map)) {
            map = emptyMap();
        }

        List<T> result = new ArrayList<>();
        for (S s : source) {
            K key = keyFunction.apply(s);
            if(map.containsKey(key)) {
                V v = map.get(key);
                T t = mapContainKeyFunction.apply(s, v);
                result.add(t);
            } else {
                if(!discard) {
                    T t = function.apply(s);
                    result.add(t);
                }
            }
        }
        return result;

    }

    /**
     * list 遍历并匹配 map 中的元素，没有匹配到则丢弃
     * @param source 源数据集合
     * @param map mpa 集合
     * @param keyFunction 生成 key 的接口
     * @param mapContainKeyFunction 生成 list 的接口
     * @param <S> 源数据集合元素类型
     * @param <T> 目标数据集合元素类型
     * @param <K> map 集合的 key 类型
     * @param <V> map 集合的 value 类型
     * @return list
     */
    public static <S, T, K, V> List<T> toListWithMap(List<S> source, Map<K, V> map,
                                                     Function<S, K> keyFunction,
                                                     BiFunction<S, V, T> mapContainKeyFunction) {
        return toListWithMap(source, map, keyFunction, mapContainKeyFunction, true, null);
    }

    /**
     * list 遍历并匹配 map 中的元素，没有匹配到不丢弃
     * @param source 源数据集合
     * @param map mpa 集合
     * @param keyFunction 生成 key 的接口
     * @param mapContainKeyFunction 生成 list 的接口
     * @param function 生成 list 的接口
     * @param <S> 源数据集合元素类型
     * @param <T> 目标数据集合元素类型
     * @param <K> map 集合的 key 类型
     * @param <V> map 集合的 value 类型
     * @return list
     */
    public static <S, T, K, V> List<T> toListWithMap(List<S> source, Map<K, V> map,
                                                     Function<S, K> keyFunction,
                                                     BiFunction<S, V, T> mapContainKeyFunction,
                                                     Function<S, T> function) {
        Preconditions.checkNotNull(function, "function不允许为空");
        return toListWithMap(source, map, keyFunction, mapContainKeyFunction, false, function);
    }

    /**
     * 将原始的 list 按照 filter 过滤之后，按照 mapper 规则转成新的 list
     * @param source 源数据集合
     * @param filter 源数据集合过滤规则
     * @param mapper 生成新的 list 接口规则
     * @param distinct 是否去重 distinct 为 true，表示过滤之后生成list之前进行去重操作
     * @param <S> 源数据类型
     * @param <T> 目标数据类型
     * @return list
     */
    public static <S, T> List<T> toList(List<S> source,
                                        Predicate<S> filter,
                                        Function<? super S, ? extends T> mapper,
                                        boolean distinct) {
        if (isEmpty(source)) {
            return Collections.emptyList();
        }

        Stream<? extends T> stream = source.stream()
                                           .filter(filter::test)
                                           .map(mapper);

        if(distinct) {
            stream = stream.distinct();
        }

        return stream.collect(Collectors.toList());
    }

    /**
     * 将原始的 list 按照 filter 过滤之后，按照 mapper 规则转成新的 list
     *
     * @param source 源数据数组
     * @param filter 源数据集合过滤规则
     * @param mapper 生成新的 list 接口规则
     * @param distinct 是否去重 distinct 为 true，表示过滤之后生成list之前进行去重操作
     * @param <S> 源数据类型
     * @param <T> 目标数据类型
     * @return list
     */
    public static <S, T> List<T> toList(S[] source,
                                        Predicate<S> filter,
                                        Function<? super S, ? extends T> mapper,
                                        boolean distinct) {
        if (Objects.isNull(source)) {
            return Collections.emptyList();
        }

        Stream<? extends T> stream = Stream.of(source)
                                            .filter(filter::test)
                                            .map(mapper);

        if(distinct) {
            stream = stream.distinct();
        }

        return stream.collect(Collectors.toList());
    }

    /**
     * 将原始的 list 按照 filter 过滤之后，按照 mapper 规则转成新的 list
     * @param source 源数据集合
     * @param filter 源数据集合过滤规则
     * @param mapper 生成新的 list 接口规则
     * @param <S> 源数据类型
     * @param <T> 目标数据类型
     * @return list
     */
    public static <S, T> List<T> toList(List<S> source,
                                        Predicate<S> filter,
                                        Function<? super S, ? extends T> mapper) {
        return toList(source, filter, mapper, false);
    }

    /**
     *
     * 从 map 中遍历生成 list
     * @param map 源数据 map 集合
     * @param function 生成 list 规则
     * @param <K> map 集合的 key 类型
     * @param <T> map 集合的数据类型
     * @param <R> 目标数据类型
     * @return list
     */
    public static <K, T, R> List<R> toListByMap(Map<K, T> map,
                                                BiFunction<K, T, R> function) {
        if(MapUtils.isEmpty(map)) {
            return emptyList();
        }

        return map.entrySet().stream()
                .map(entry -> function.apply(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 从过滤的 map 中遍历生成 list
     * @param map 源数据 map 集合
     * @param filterMapMapper 过滤 map 的接口规则
     * @param function 生成 list 规则
     * @param <K> map 集合的 key 类型
     * @param <T> map 集合的数据类型
     * @param <R> 目标数据类型
     * @return list
     */
    public static <K, T, R> List<R> toListByMap(Map<K, T> map,
                                                BiPredicate<K, T> filterMapMapper,
                                                BiFunction<K, T, R> function) {
        if(MapUtils.isEmpty(map)) {
            return emptyList();
        }

        return map.entrySet().stream()
                .filter(entry -> filterMapMapper.test(entry.getKey(), entry.getValue()))
                .map(entry -> function.apply(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 将 list 集合分组
     * @param source 数据源集合
     * @param classifier 分组规则
     * @param <K> map 集合中的 key 类型
     * @param <S> map 集合中的 value 类型
     * @return list
     */
    public static <K, S> Map<K, List<S>> groupingBy(List<S> source,
                                                    Function<? super S, ? extends K> classifier) {
        if(isEmpty(source)) {
            return Collections.emptyMap();
        }

        return source.stream().collect(Collectors.groupingBy(classifier));
    }

    /**
     * 将过滤的 list 集合分组
     * @param source 数据源集合
     * @param filter 过滤源数据集合的接口规则
     * @param classifier 分组规则
     * @param <K> map 集合中的 key 类型
     * @param <S> map 集合中的 value 类型
     * @return list
     */
    public static <K, S> Map<K, List<S>> groupingBy(List<S> source,
                                                    Predicate<S> filter,
                                                    Function<? super S, ? extends K> classifier) {
        if(isEmpty(source)) {
            return Collections.emptyMap();
        }

        return source.stream()
                    .filter(filter::test)
                    .collect(Collectors.groupingBy(classifier));
    }

    /**
     * 对 list 集合进行排序
     * @param source 数据源集合
     * @param comparator 排序器
     * @param <T> 集合的数据类型
     * @return list
     */
    public static <T> List<T> sort(List<T> source, Comparator<T> comparator) {
        return sort(source, comparator, true);
    }

    /**
     * 对 list 集合进行排序
     * @param source 数据源集合
     * @param comparator 排序器
     * @param reverse 是否逆序
     * @param <T> 集合的数据类型
     * @return list
     */
    public static <T> List<T> sort(List<T> source,
                                   Comparator<T> comparator,
                                   boolean reverse) {
        if(isEmpty(source)) {
            return source;
        }

        Stream<T> stream = source.stream();
        Stream<T> sorted;
        if(reverse) {
            sorted = stream.sorted(comparator.reversed());
        } else {
            sorted = stream.sorted(comparator);
        }
        return sorted.collect(Collectors.toList());
    }

    /**
     * 对集合数据进行去重器, 默认保留最后出现的重复元素
     * 非线程安全
     * @param source 数据源集合
     * @param isValidFunction 数据是否有效
     * @param getDeduplicatedKeyFunction 获取去重的数据值
     * @param <K> 去重属性的类型
     * @param <T> 数据源集合中元素的类型
     * @return DeduplicateResult
     */
    public static <K, T> DeduplicateResult<K, T> deduplicate(List<T> source,
                                                             Function<T, Boolean> isValidFunction,
                                                             Function<T, K> getDeduplicatedKeyFunction) {
        return deduplicate(source, isValidFunction, getDeduplicatedKeyFunction, false);
    }

    /**
     * 对集合数据进行去重器, 默认保留最后出现的重复元素
     * 非线程安全
     * @param source 数据源集合
     * @param getDeduplicatedKeyFunction 获取去重的数据值
     * @param <K> 去重属性的类型
     * @param <T> 数据源集合中元素的类型
     * @return DeduplicateResult
     */
    public static <K, T> DeduplicateResult<K, T> deduplicate(List<T> source,
                                                             Function<T, K> getDeduplicatedKeyFunction) {
        return deduplicate(source, null, getDeduplicatedKeyFunction, false);
    }

    /**
     * 对集合数据进行去重器
     * 非线程安全
     * @param source 数据源集合
     * @param getDeduplicatedKeyFunction 获取去重的数据值
     * @param remainFirst 是否保留第一次出现的重复值
     * @param <K> 去重属性的类型
     * @param <T> 数据源集合中元素的类型
     * @return DeduplicateResult
     */
    public static <K, T> DeduplicateResult<K, T> deduplicate(List<T> source,
                                                             Function<T, K> getDeduplicatedKeyFunction,
                                                             boolean remainFirst) {
        return deduplicate(source, null, getDeduplicatedKeyFunction, remainFirst);
    }

    /**
     * 对集合数据进行去重器
     * 非线程安全
     * @param source 数据源集合
     * @param isValidFunction 数据是否有效
     * @param getDeduplicatedKeyFunction 获取去重的数据值
     * @param remainFirst 是否保留第一次出现的重复值
     * @param <K> 去重属性的类型
     * @param <T> 数据源集合中元素的类型
     * @return DeduplicateResult
     */
    public static <K, T> DeduplicateResult<K, T> deduplicate(List<T> source,
                                                          Function<T, Boolean> isValidFunction,
                                                          Function<T, K> getDeduplicatedKeyFunction,
                                                          boolean remainFirst) {
        if(isEmpty(source)) {
            return new DeduplicateResult<K, T>(source, emptyList(), emptyList(), emptyList(), emptyList());
        }

        Map<K, T> container =  new LinkedHashMap<>();
        // 无效的数据集合
        List<T> invalidList = new LinkedList<>();
        // 重复的数据集合
        List<T> repetitiveList = new LinkedList<>();

        for (T data : source) {
            if (Objects.nonNull(isValidFunction) && !Objects.equals(true, isValidFunction.apply(data))) {
                invalidList.add(data);
            } else {
                K deduplicatedKey = getDeduplicatedKeyFunction.apply(data);
                if (container.containsKey(deduplicatedKey)) {
                    T putData;
                    if(remainFirst) {
                        putData = data;
                    } else {
                        putData = container.put(deduplicatedKey, data);
                    }
                    if(Objects.nonNull(putData)) {
                        repetitiveList.add(putData);
                    }
                } else {
                    container.put(deduplicatedKey, data);
                }

            }
        }

        // 已去重的数据集合
        List<T> deduplicatedList = new ArrayList<>(container.values());
        List<K> deduplicatedKeyList = new ArrayList<>(container.keySet());

        return new DeduplicateResult<K, T>(source, invalidList, deduplicatedList, deduplicatedKeyList, repetitiveList);
    }

    /**
     * 枚举转 map 集合
     * @param sourceEnumClass 数据源枚举 Class类
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <T> map 集合中的 value 类型
     * @return map 集合
     */
    public static <K, T extends Enum<T>> Map<K, T> enumMap(Class<T> sourceEnumClass,
                                                           Function<T, K> keyMapper) {
        EnumSet<T> enumSet = EnumSet.allOf(sourceEnumClass);
        return enumSet.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    /**
     * 枚举转 map 集合
     * map 集合中的 key 为 Enum 的 name() 方法返回值
     * @param sourceEnumClass 数据源枚举 Class类
     * @param <T> map 集合中的 value 类型
     * @return map 集合
     */
    public static <T extends Enum<T>> Map<String, T> enumMap(Class<T> sourceEnumClass) {
        EnumSet<T> enumSet = EnumSet.allOf(sourceEnumClass);
        return enumSet.stream().collect(Collectors.toMap(Enum::name, Function.identity()));
    }

    /**
     * 枚举转 list 集合
     * 按照 function 将枚举转成 list 集合
     * @param sourceEnumClass 数据源枚举 Class类
     * @param function 生成接口
     * @param <T> 枚举类型
     * @param <R> 结果集类型
     * @return list
     */
    public static <T extends Enum<T>, R> List<R> enumList(Class<T> sourceEnumClass,
                                                          Function<T, R> function) {
        EnumSet<T> enumSet = EnumSet.allOf(sourceEnumClass);
        return enumSet.stream().map(function).collect(Collectors.toList());
    }

    /**
     * 判断 key 是否存在于枚举转 map 的集合中
     * @param key 要判断的 key
     * @param sourceEnumClass 数据源枚举 Class类
     * @param keyMapper map 集合中的 key 获取规则
     * @param <K> map 集合中的 key 类型
     * @param <T> map 集合中的 value 类型
     * @return map
     */
    public static <K, T extends Enum<T>> boolean enumContainsKey(K key, Class<T> sourceEnumClass,
                                                                 Function<T, K> keyMapper) {
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
     * @return 枚举实例
     */
    public static <K, T extends Enum<T>> T enumGetValue(K key, Class<T> sourceEnumClass,
                                                        Function<T, K> keyMapper) {
        if(Objects.isNull(key)) {
            return null;
        }

        Map<K, T> map = enumMap(sourceEnumClass, keyMapper);
        return map.get(key);
    }

    /**
     * 将原始的 list 按照 filter 过滤
     * @param source 数据原始集合
     * @param filter 过滤规则
     * @param <T> 数据类型
     * @return list
     */
    public static <T> List<T> filter(List<T> source,
                                     Predicate<? super T> filter) {
        if (isEmpty(source)) {
            return Collections.emptyList();
        }

        return source.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    /**
     * 将原始的 list 按照 filter 过滤
     * @param source 数据原始数组
     * @param filter 过滤规则
     * @param <T> 数据类型
     * @return list
     */
    public static <T> List<T> filter(T[] source,
                                     Predicate<? super T> filter) {
        if (Objects.isNull(source)) {
            return Collections.emptyList();
        }

        return Stream.of(source)
                .filter(filter)
                .collect(Collectors.toList());
    }

    /**
     * 根据 searchData 按照 searchFunction 规则从 sourceList 集合中搜索数据
     * @param searchData 要搜索的源数据
     * @param searchFunction 搜索的key
     * @param sourceList 被搜索的数据源
     * @param keyFunction 被搜索的数据源索引生成规则
     * @param <M> 要搜索的源数据类型
     * @param <K> 搜索的 key 类型
     * @param <T> 被搜索的数据源类型
     * @return 被搜索的数据源
     */
    public static <M, K, T> T getDataFromList(M searchData,
                                              Function<M, K> searchFunction,
                                              List<T> sourceList,
                                              Function<T, K> keyFunction) {
        Preconditions.checkNotNull(searchFunction, "function不允许为空");
        if(isEmpty(sourceList)) {
            return null;
        }

        return toMapForSaveNew(sourceList, keyFunction).get(searchFunction.apply(searchData));
    }

    /**
     * 对原始 map 进行数据操作
     * @param map 源 map 集合
     * @param handlerMapper 集合元素处理接口规则
     * @param <K> map 集合的 key 数据类型
     * @param <T> map 集合的 value 数据类型
     * @return 返回原始 map 集合
     */
    public static <K, T> Map<K, T> handleMap(Map<K, T> map,
                                             BiConsumer<K, T> handlerMapper) {
        if(MapUtils.isEmpty(map)) {
            return emptyMap();
        }

        for (Map.Entry<K, T> entry : map.entrySet()) {
            K key = entry.getKey();
            T value = entry.getValue();
            handlerMapper.accept(key, value);
        }
        
        return map;
    }

    /**
     * 去除 list 集合中的 null 元素进行
     * @param oldList 原始数据集合
     * @param <T> 原始数据类型
     * @return list
     */
    public static <T> List<T> removeNull(List<? extends T> oldList) {
        oldList.removeAll(Collections.singleton(null));
        return (List<T>) oldList;
    }




    /**
     * 从 map 中获取 value 集合并根据 function 转成 R 集合
     * @param map 原始数据源集合
     * @param function 映射接口
     * @param <K> key 类型
     * @param <V> value 类型
     * @param <R> 目标数据类型
     * @return list
     */
    public static <K, V, R> List<R> mapValueToList(Map<K, V> map,
                                                   Function<V, R> function) {
        if (MapUtils.isEmpty(map)) {
            return emptyList();
        }
        return map.values()
                  .stream()
                  .map(function)
                  .collect(Collectors.toList());
    }

    /**
     * 遍历 map 元素并按照 function 转成 R 集合
     * @param map 原始数据源集合
     * @param function 映射接口
     * @param <K> key 类型
     * @param <V> key 类型
     * @param <R> 结果集类型
     * @return list
     */
    public static <K, V, R> List<R> mapToList(Map<K, V> map,
                                              BiFunction<K, V, R> function) {
        if (MapUtils.isEmpty(map)) {
            return emptyList();
        }

        return map.entrySet()
                  .stream()
                  .map(entry -> function.apply(entry.getKey(), entry.getValue()))
                  .collect(Collectors.toList());
    }

    /**
     * 遍历 map 元素，先按照 predicate 过滤再按照 function 转成 R 集合
     * @param map 原始数据源集合
     * @param predicate 过滤接口
     * @param function 映射接口
     * @param <K> key 类型
     * @param <V> key 类型
     * @param <R> 结果集类型
     * @return list
     */
    public static <K, V, R> List<R> mapToList(Map<K, V> map,
                                              BiPredicate<K, V> predicate,
                                              BiFunction<K, V, R> function) {
        if (MapUtils.isEmpty(map)) {
            return emptyList();
        }

        return map.entrySet()
                  .stream()
                  .filter(entry -> predicate.test(entry.getKey(), entry.getValue()))
                  .map(entry -> function.apply(entry.getKey(), entry.getValue()))
                  .collect(Collectors.toList());
    }

    /**
     * 从 sourceList 和 baseList 集合中按照 sourceDataKeyFunction 和 baseDataKeyFunction 匹配对应的数据
     * 按照 function 生成结果集合
     * @param sourceList 原始集合
     * @param sourceDataKeyFunction 生成 key 规则
     * @param baseList 基础集合
     * @param baseDataKeyFunction 生成 key 规则
     * @param function 生成结果规则
     * @param <S1> 原始集合数据类型
     * @param <S2> 基础集合数据类型
     * @param <K> key 的数据类型
     * @param <R> 结果集数据类型
     * @return list
     */
    public static <S1, S2, K, R> List<R> getListFromBaseList(List<S1> sourceList,
                                                             Function<S1, K> sourceDataKeyFunction,
                                                             List<S2> baseList, Function<S2, K> baseDataKeyFunction,
                                                             BiFunction<S1, S2, R> function) {
        List<R> resultList = new ArrayList<>();
        Map<K, S2> baseDataMap = DataTool.toMap(baseList, baseDataKeyFunction);
        for (S1 sourceData : sourceList) {
            K sourceDataKey = sourceDataKeyFunction.apply(sourceData);
            if (baseDataMap.containsKey(sourceDataKey)) {
                S2 baseData = baseDataMap.get(sourceDataKey);
                R result = function.apply(sourceData, baseData);
                resultList.add(result);
            }
        }
        return resultList;
    }

    /**
     * 从 sourceList 按照 resultFunction 生成结果集合
     * 结果集合 List1 和 List2 按照 resultDataKeyFunction 和 baseDataKeyFunction 匹配对应的数据
     * 匹配到的 List 按照 consumer 规则供结果集使用
     *
     * @param sourceList 原始集合
     * @param resultFunction 结果集生成规则
     * @param resultDataKeyFunction 生成 key 规则
     * @param baseList 基础集合数据类型
     * @param baseDataKeyFunction 生成 key 规则
     * @param consumer 匹配到的基础数据集合供结果数据使用
     * @param <S1> 原始集合数据类型
     * @param <S2> 基础集合数据类型
     * @param <K> key 的数据类型
     * @param <R> 结果集数据类型
     * @return list
     */
    public static <S1, S2, K, R> List<R> getListFromBaseList(List<S1> sourceList,
                                                             Function<S1, R> resultFunction,
                                                             Function<R, K> resultDataKeyFunction,
                                                             List<S2> baseList,
                                                             Function<S2, K> baseDataKeyFunction,
                                                             BiConsumer<R, List<S2>> consumer) {
        List<R> resultList = DataTool.toList(sourceList, resultFunction);
        Map<K, List<S2>> listMap = DataTool.groupingBy(baseList, baseDataKeyFunction);
        for (R result : resultList) {
            K resultKey = resultDataKeyFunction.apply(result);
            if (listMap.containsKey(resultKey)) {
                List<S2> baseDataList = listMap.get(resultKey);
                if(CollectionUtils.isNotEmpty(baseDataList)) {
                    consumer.accept(result, baseDataList);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取 list 集合中的第一个元素
     * @param list list 集合
     * @param <T> 集合元素的泛型
     * @return 集合元素
     */
    public static <T> T getFirstItem(List<T> list) {
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

    /**
     * 获取 list 集合中的最后一个元素
     * @param list list 集合
     * @param <T> 集合元素的泛型
     * @return 集合元素
     */
    public static <T> T getLastItem(List<T> list) {
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list.get(list.size() - 1);
    }

}
