package cn.woodwhales.common.model.vo;

import cn.woodwhales.common.model.enums.RespCodeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * layui 分页响应对象
 * @author woodwhales on 2022-06-10 14:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LayuiPageVO<T> {

    /**
     * 响应成功状态码
     */
    private Integer code;

    /**
     * 响应描述
     */
    private String msg;

    /**
     * 总记录数
     */
    private Long count;

    /**
     * 当前分页记录集合
     */
    private List<T> data;

    public static <S, T> LayuiPageVO<T> build(IPage<S> page,
                                           Function<? super S, ? extends T> mapper,
                                           Comparator<T> comparator) {
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return build(RespCodeEnum.SUCCESS, page.getTotal(), emptyList());
        }

        return build(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords()
                .stream()
                .map(mapper)
                .sorted(comparator)
                .collect(toList()));
    }

    public static <T> LayuiPageVO<T> build(RespCodeEnum respCodeEnum, long total, List<T> data) {
        return new LayuiPageVO<>(respCodeEnum.getCode(), respCodeEnum.getMessage(), total, data);
    }

}
