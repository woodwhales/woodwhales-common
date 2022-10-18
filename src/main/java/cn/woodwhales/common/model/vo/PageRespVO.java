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
 * 分页查询响应视图
 *
 * @author woodwhales on 2020-08-25
 */
@Data
public class PageRespVO<T> {

    /**
     * 总记录数
     */
    private Long count;

    /**
     * 数据集合
     */
    private List<T> list;

    public static <T> RespVO<PageRespVO<T>> success(Long count, List<T> data) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, count, data);
    }

    public static <T> RespVO<PageRespVO<T>> success(List<T> data) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, Long.parseLong(data.size() + ""), data);
    }

    public static <T> RespVO<PageRespVO<T>> empty() {
        return buildPageRespVO(RespCodeEnum.SUCCESS, 0L, null);
    }

    public static RespVO error(BaseRespResult baseRespResult) {
        return buildPageRespVO(baseRespResult, 0L, null);
    }

    public static <T> RespVO<PageRespVO<T>> buildPageRespVO(IPage<T> page) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords());
    }

    /**
     * IPage 数据按照 mapper 规则转成 PageRespVO
     *
     * @param page   page
     * @param mapper mapper
     * @param <S>    原始数据泛型
     * @param <T>    目标数据泛型
     * @return PageRespVO
     */
    public static <S, T> RespVO<PageRespVO<T>> buildPageRespVO(IPage<S> page,
                                                       Function<? super S, ? extends T> mapper) {
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), emptyList());
        }

        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords()
                .stream()
                .map(mapper)
                .collect(toList()));
    }

    public static <S, T> RespVO<PageRespVO<T>> buildPageRespVO(IPage<S> page,
                                                       Function<? super S, ? extends T> mapper,
                                                       Comparator<T> comparator) {
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), emptyList());
        }

        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords()
                .stream()
                .map(mapper)
                .sorted(comparator)
                .collect(toList()));
    }

    public static <T> RespVO<PageRespVO<T>> buildPageRespVO(@NotNull BaseRespResult baseRespResult,
                                                            Long count,
                                                            List<T> data) {
        Objects.requireNonNull(baseRespResult, "baseRespResult不允许为空");
        return build(baseRespResult.getCode(), baseRespResult.getMessage(), count, data);
    }

    private static <T> RespVO<PageRespVO<T>> build(Integer code,
                                           String message,
                                           Long count,
                                           List<T> data) {
        Objects.requireNonNull(code, "code不允许为空");

        RespVO<PageRespVO<T>> respVO = new RespVO<>();
        respVO.setCode(code);
        respVO.setMsg(message);

        PageRespVO<T> pageRespVO = new PageRespVO<>();
        if (CollectionUtils.isEmpty(data)) {
            pageRespVO.setCount(0L);
            pageRespVO.setList(emptyList());
        } else {
            pageRespVO.setCount(count);
            pageRespVO.setList(data);
        }
        respVO.setData(pageRespVO);
        return respVO;
    }

}

