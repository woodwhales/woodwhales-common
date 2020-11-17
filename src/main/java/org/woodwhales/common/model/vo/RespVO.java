package org.woodwhales.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.woodwhales.common.model.enums.RespCodeEnum;

import java.util.Objects;

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

    public static RespVO success() {
        return build(RespCodeEnum.SUCCESS, null);
    }

    public static <T> RespVO<T> success(T data) {
        return build(RespCodeEnum.SUCCESS, data);
    }

    public static <T> RespVO<T> success(RespCodeEnum respCodeEnum, T data) {
        return build(respCodeEnum, data);
    }

    public static RespVO error() {
        return build(RespCodeEnum.ERROR, null);
    }

    public static RespVO error(String errorMsg) {
        return build(RespCodeEnum.ERROR.getCode(), errorMsg, null);
    }

    public static RespVO error(RespCodeEnum respCodeEnum) {
        return build(respCodeEnum, null);
    }

    public static RespVO resp(boolean success) {
        if(success) {
            return success();
        }
        return error();
    }

    public static <T> RespVO<T> build(RespCodeEnum respCodeEnum, T data) {
        Objects.requireNonNull(respCodeEnum);
        return build(respCodeEnum.getCode(), respCodeEnum.getMessage(), data);
    }

    public static <T> RespVO<T> build(Integer code, String message, T data) {
        RespVO respVO = new RespVO();
        respVO.setCode(code);
        respVO.setMsg(message);
        respVO.setData(data);
        return respVO;
    }
}
