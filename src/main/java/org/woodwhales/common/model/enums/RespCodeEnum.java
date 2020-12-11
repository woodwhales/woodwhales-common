package org.woodwhales.common.model.enums;

import org.woodwhales.common.model.result.BaseRespResult;

/**
 * @author woodwhales on 2020-08-25
 * @description 通用响应枚举
 */
public enum RespCodeEnum implements BaseRespResult {

    /**
     * 响应成功
     */
    SUCCESS(0, "操作成功"),

    /**
     * 响应失败
     */
    ERROR(-1, "操作失败"),
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

    @Override
    public boolean isSucceed() {
        return this.code == 0;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }


}
