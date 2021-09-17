package cn.woodwhales.common.business.collection;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author woodwhales on 2020-12-13 16:32
 * @description
 */
public class CollectionContainer<K, T> implements CollectionFieldComparable<K> {

    private T data;

    private K dataKey;

    public CollectionContainer(T data, K dataKey) {
        this.data = data;
        this.dataKey = dataKey;
    }

    public T getData() {
        return data;
    }

    @Override
    public K getDataKey() {
        return dataKey;
    }

    public static <K, T> CollectionContainer<K, T> build(T data, Function<T, K> keyFunction) {
        return new CollectionContainer(data, keyFunction.apply(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectionContainer)) {
            return false;
        }
        CollectionContainer<?, ?> that = (CollectionContainer<?, ?>) o;
        return getDataKey().equals(that.getDataKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDataKey());
    }

    @Override
    public String toString() {
        return "CollectionContainer{" +
                "data=" + data +
                ", dataKey=" + dataKey +
                '}';
    }
}
