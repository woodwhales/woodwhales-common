package cn.woodwhales.common.model.param;

import lombok.Data;

/**
 * 条件分页查询对象
 * @author woodwhales on 2022-09-21 15:49
 */
@Data
public class SimplePageQueryParam<Param> {

    /**
     * 条件对象
     */
    private Param param;

    /**
     * 分页对象
     */
    private PageQueryParam pageQueryParam;

}
