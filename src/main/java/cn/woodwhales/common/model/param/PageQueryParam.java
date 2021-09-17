package cn.woodwhales.common.model.param;

import lombok.Data;
import cn.woodwhales.common.model.field.PageQueryInterface;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author woodwhales on 2020-08-25
 * @description
 */
@Data
public class PageQueryParam implements PageQueryInterface {

    /**
     * 当前页码
     */
    @Min(value = 1, message = "当前页码不允许小于第一页")
    @NotNull(message = "page不允许为空")
    private Integer page;

    /**
     * 每页显示总记录数
     */
    @Min(value = 0, message = "当前页总记录数不允许为负数")
    @NotNull(message = "limit不允许为空")
    private Integer limit;
}