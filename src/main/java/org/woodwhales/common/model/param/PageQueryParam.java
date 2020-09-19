package org.woodwhales.common.model.param;

import lombok.Data;
import org.woodwhales.common.model.field.PageQueryInterface;

import javax.validation.constraints.Min;

/**
 * @author woodwhales on 2020-08-25
 * @description
 */
@Data
public class PageQueryParam implements PageQueryInterface {

    @Min(value = 1, message = "当前页码不允许小于第一页")
    private Integer page;

    @Min(value = 0, message = "当前页总记录数不允许为负数")
    private Integer limit;
}