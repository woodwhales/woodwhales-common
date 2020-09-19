package org.woodwhales.common.model.enums;

import lombok.Getter;

/**
 * @author woodwhales on 2020-08-25
 * @description
 */
@Getter
public enum RespCodeEnum {

    SUCCESS(0, "success"),
    ERROR(-1, "fail"),
    ;

    private final Integer code;
    private final String message;

    RespCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
