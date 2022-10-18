package cn.woodwhales.common.model.param;

import cn.woodwhales.common.model.field.DefaultPageQueryInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author woodwhales on 22.8.12 11:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultQueryParam implements DefaultPageQueryInterface {

    /**
     * 当前页码
     */
    @Min(value = 1, message = "当前页码不允许小于第一页")
    @NotNull(message = "currentPage不允许为空")
    private Integer currentPage;

    /**
     * 每页显示总记录数
     */
    @Min(value = 0, message = "当前页总记录数不允许为负数")
    @NotNull(message = "pageSize不允许为空")
    private Integer pageSize;

}
