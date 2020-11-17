package org.woodwhales.common.model.enums;

import lombok.Getter;

/**
 * @author woodwhales on 2020-08-25
 * @description 通用响应枚举
 */
@Getter
public enum RespCodeEnum {

    /**
     * 响应成功
     */
    SUCCESS(0, "success"),

    /**
     * 响应失败
     */
    ERROR(-1, "fail"),
    ;

    /**
     * 响应状态码
     */
    private final Integer code;

    /**
     * 响应描述
     */
    private final String message;

    RespCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
