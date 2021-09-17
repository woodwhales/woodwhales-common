package cn.woodwhales.common.model.result;

import cn.woodwhales.common.model.enums.RespCodeEnum;

import java.util.Objects;

/**
 * @projectName: woodwhales-common
 * @author: woodwhales
 * @date: 20.9.18 22:05
 * @description: 业务数据响应体
 */
public class OpResult<T> {

    private BaseRespResult baseRespResult;

    /**
     * 业务数据
     */
    private T data;

    public static <T> OpResult<T> success(T data) {
        return new OpResult<T>(RespCodeEnum.SUCCESS, data);
    }

    public static <T> OpResult<T> success() {
        return new OpResult<T>(RespCodeEnum.SUCCESS, null);
    }

    public static <T> OpResult<T> failure() {
        return new OpResult<T>(RespCodeEnum.ERROR, null);
    }

    public static <T> OpResult<T> failure(BaseRespResult baseRespResult) {
        return new OpResult<T>(baseRespResult, null);
    }

    public static <T> OpResult<T> failure(BaseRespResult baseRespResult, T data) {
        return new OpResult<T>(baseRespResult, data);
    }

    /**
     * 获取数据对象
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * 获取响应状态码对象
     * @return
     */
    public BaseRespResult getBaseRespResult() {
        return baseRespResult;
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
        return Objects.equals(baseRespResult.getCode(), RespCodeEnum.SUCCESS.getCode());
    }

    /**
     * 是否响应失败
     * @return
     */
    public boolean isFailure() {
        return !isSuccessful();
    }

    private OpResult() {
    }

    private OpResult(BaseRespResult baseRespResult, T data) {
        this.baseRespResult = baseRespResult;
        this.data = data;
    }
}
