package cn.woodwhales.common.model.vo;

import cn.woodwhales.common.model.enums.RespCodeEnum;
import cn.woodwhales.common.model.result.BaseRespResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * @author woodwhales on 2020-08-25
 * @description 分页查询响应视图
 */
@Data
public class PageRespVO<T> extends RespVO<List<T>> {

    /**
     * 总记录数
     */
    private Long count;

    public static <T> PageRespVO<T> success(Long count, List<T> data) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, count, data);
    }

    public static <T> PageRespVO<T> success(List<T> data) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, Long.parseLong(data.size() + ""), data);
    }

    public static <T> PageRespVO<T> empty() {
        return buildPageRespVO(RespCodeEnum.SUCCESS, 0L, null);
    }

    public static PageRespVO error(BaseRespResult baseRespResult) {
        return buildPageRespVO(baseRespResult, 0L, null);
    }

    public static <T> PageRespVO<T> buildPageRespVO(IPage<T> page) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords());
    }

    /**
     * IPage<T> 数据按照 mapper 规则转成 PageRespVO<<R>
     * @param page
     * @param mapper
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> PageRespVO<T> buildPageRespVO(IPage<S> page,
                                                       Function<? super S, ? extends T> mapper) {
        if(CollectionUtils.isEmpty(page.getRecords())) {
            return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), emptyList());
        }

        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords()
                                                                          .stream()
                                                                          .map(mapper)
                                                                          .collect(toList()));
    }

    public static <S, T> PageRespVO<T> buildPageRespVO(IPage<S> page,
                                                       Function<? super S, ? extends T> mapper,
                                                       Comparator<T> comparator) {
        if(CollectionUtils.isEmpty(page.getRecords())) {
            return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), emptyList());
        }

        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords()
                                                                          .stream()
                                                                          .map(mapper)
                                                                          .sorted(comparator)
                                                                          .collect(toList()));
    }

    public static <T> PageRespVO<T> buildPageRespVO(@NotNull BaseRespResult baseRespResult,
                                                    Long count,
                                                    List<T> data) {
        Objects.requireNonNull(baseRespResult, "baseRespResult不允许为空");
        return build(baseRespResult.getCode(), baseRespResult.getMessage(), count, data);
    }

    private static <T> PageRespVO<T> build(Integer code,
                                           String message,
                                           Long count,
                                           List<T> data) {
        Objects.requireNonNull(code, "code不允许为空");
        PageRespVO<T> pageRespVO = new PageRespVO<>();
        pageRespVO.setCode(code);
        pageRespVO.setMsg(message);

        if(CollectionUtils.isEmpty(data)) {
            pageRespVO.setCount(0L);
            pageRespVO.setData(emptyList());
        } else {
            pageRespVO.setCount(count);
            pageRespVO.setData(data);
        }

        return pageRespVO;
    }

}

