package org.woodwhales.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.woodwhales.common.model.enums.RespCodeEnum;
import org.woodwhales.common.model.result.BaseRespResult;
import org.woodwhales.common.model.result.OpResult;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author woodwhales on 2020-08-25
 * @description 通用响应视图
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespVO<T> {

    private Integer code;
    private String msg;
    private T data;

    public static <T> RespVO<T> success() {
        return build(RespCodeEnum.SUCCESS, null);
    }

    public static <T> RespVO<T> success(T data) {
        return build(RespCodeEnum.SUCCESS, data);
    }

    public static <T> RespVO<T> error() {
        return build(RespCodeEnum.ERROR, null);
    }

    public static <T> RespVO<T> error(BaseRespResult baseRespResult) {
        return buildWithBaseRespResult(baseRespResult, null);
    }

    public static <T> RespVO<T> error(BaseRespResult baseRespResult, T data) {
        return buildWithBaseRespResult(baseRespResult, data);
    }

    public static <T> RespVO<T> buildWithBaseRespResult(BaseRespResult baseRespResult) {
        return buildWithBaseRespResult(baseRespResult, null);
    }

    public static <T> RespVO<T> buildWithBaseRespResult(BaseRespResult baseRespResult, T data) {
        Objects.requireNonNull(baseRespResult);
        return build(baseRespResult.getCode(), baseRespResult.getMessage(), data);
    }

    public static <S, T> RespVO<T> resp(OpResult<S> opResult, @NotNull Function<S, T> function) {
        Objects.requireNonNull(function, "对象转换接口不允许为空");
        if(opResult.isSuccessful()) {
            return success(function.apply(opResult.getData()));
        }

        BaseRespResult baseRespResult = opResult.getBaseRespResult();
        return error(baseRespResult, function.apply(opResult.getData()));
    }

    public static <T> RespVO<T> resp(OpResult<T> opResult) {
        if(opResult.isSuccessful()) {
            return success(opResult.getData());
        }

        BaseRespResult baseRespResult = opResult.getBaseRespResult();
        return error(baseRespResult, opResult.getData());
    }

    private static <T> RespVO<T> build(Integer code, String message, T data) {
        RespVO<T> respVO = new RespVO<>();
        respVO.setCode(code);
        respVO.setMsg(message);
        respVO.setData(data);
        return respVO;
    }

    private static <T> RespVO<T> build(RespCodeEnum respCodeEnum, T data) {
        return buildWithBaseRespResult(respCodeEnum, data);
    }

}
