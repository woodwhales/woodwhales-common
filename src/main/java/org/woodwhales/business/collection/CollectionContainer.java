package org.woodwhales.business.collection;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author woodwhales on 2020-12-13 16:32
 * @description
 */
public class CollectionContainer<K> {

    private Object data;

    private K dataKey;

    public CollectionContainer(Object data, K dataKey) {
        this.data = data;
        this.dataKey = dataKey;
    }

    public Object getData() {
        return data;
    }

    public K getDataKey() {
        return dataKey;
    }

    public static <K, T> CollectionContainer<K> build(T data, Function<T, K> keyFunction) {
        CollectionContainer<K> collectionContainer = new CollectionContainer<K>(data, keyFunction.apply(data));
        return collectionContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CollectionContainer)) {
            return false;
        }
        CollectionContainer<?> that = (CollectionContainer<?>) o;
        return getDataKey().equals(that.getDataKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDataKey());
    }

}
