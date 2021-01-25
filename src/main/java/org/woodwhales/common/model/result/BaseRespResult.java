package org.woodwhales.common.model.result;

/**
 * @author woodwhales on 2020-12-07
 * @description 通用响应报文接口
 */
public interface BaseRespResult {

    /**
     * 获取响应描述
     * @return
     */
    String getMessage();

    /**
     * 获取响应状态码
     * @return
     */
    Integer getCode();

}
