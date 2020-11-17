package org.woodwhales.common.model.result;

import java.util.Objects;

/**
 * @projectName: woodwhales-common
 * @author: woodwhales
 * @date: 20.9.18 22:05
 * @description: 业务数据响应体
 */
public class OpResult<T> {

    /**
     * 响应成功标识
     * true 标识响应成功
     * false 标识响应失败
     */
    private boolean success;

    /**
     * 业务数据
     */
    private T data;

    private OpResult() {
    }

    private OpResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public static <T> OpResult success(T data) {
        return new OpResult(true, data);
    }

    public static <T> OpResult fail() {
        return new OpResult(false, null);
    }

    public static <T> OpResult fail(T data) {
        return new OpResult(false, data);
    }

    /**
     * 获取数据对象
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * 数据是否为空
     * @return
     */
    public boolean dataIsNull() {
        return Objects.isNull(data);
    }

    /**
     * 数据是否不为空
     * @return
     */
    public boolean dataIsNonNull() {
        return Objects.nonNull(data);
    }

    /**
     * 是否响应成功
     * @return
     */
    public boolean isSuccessful() {
        return success;
    }

    /**
     * 是否响应失败
     * @return
     */
    public boolean isFailed() {
        return !success;
    }
}
