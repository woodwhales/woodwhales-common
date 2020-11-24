package org.woodwhales.common.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;
import org.woodwhales.common.model.enums.RespCodeEnum;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

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

    public static <T> PageRespVO<T> buildPageRespVO(IPage<T> page) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), page.getRecords());
    }

    public <R> PageRespVO<R> mapToPageResult(Function<? super T, ? extends R> mapper) {
        return mapToPageResult(mapper, null);
    }

    public <R> PageRespVO<R> mapToPageResult(Function<? super T, ? extends R> mapper, Comparator<? super R> comparator) {
        Objects.requireNonNull(mapper);
        if(CollectionUtils.isEmpty(this.getData())) {
            return empty();
        }

        if(Objects.isNull(comparator)) {
            return buildPageRespVO(RespCodeEnum.SUCCESS, this.getCount(), this.getData()
                    .stream()
                    .map(mapper)
                    .collect(Collectors.toList()));
        }

        return buildPageRespVO(RespCodeEnum.SUCCESS, this.getCount(), this.getData()
                                                                            .stream()
                                                                            .map(mapper)
                                                                            .sorted(comparator)
                                                                            .collect(Collectors.toList()));
    }

    public static <T> PageRespVO<T> success(Long count, List<T> data) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, count, data);
    }

    public static <T> PageRespVO<T> success(List<T> data) {
        return buildPageRespVO(RespCodeEnum.SUCCESS, Long.parseLong(data.size() + ""), data);
    }

    public static <T> PageRespVO<T> empty() {
        return buildPageRespVO(RespCodeEnum.SUCCESS, 0L, null);
    }

    /**
     * IPage<T> 数据按照 mapper 规则转成 PageRespVO<<R>
     * @param page
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> PageRespVO<R> buildPageRespVO(IPage<T> page, Function<? super T, ? extends R> mapper) {
        if(CollectionUtils.isEmpty(page.getRecords())) {
            return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(), emptyList());
        }

        return buildPageRespVO(RespCodeEnum.SUCCESS, page.getTotal(),
                page.getRecords().stream().map(mapper).collect(Collectors.toList()));
    }

    public static <T> PageRespVO<T> buildPageRespVO(RespCodeEnum respCodeEnum, Long count, List<T> data) {
        Objects.requireNonNull(respCodeEnum);
        PageRespVO<T> pageRespVO = new PageRespVO<>();
        pageRespVO.setCode(respCodeEnum.getCode());
        pageRespVO.setMsg(respCodeEnum.getMessage());

        if(CollectionUtils.isEmpty(data)) {
            pageRespVO.setCount(0L);
            pageRespVO.setData(Collections.emptyList());
        } else {
            pageRespVO.setCount(count);
            pageRespVO.setData(data);
        }

        return pageRespVO;
    }

}
