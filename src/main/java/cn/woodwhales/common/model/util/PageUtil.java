package cn.woodwhales.common.model.util;

import cn.woodwhales.common.model.field.PageQueryInterface;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

/**
 * mybatisplus 查询对象构建工具
 * @author woodwhales on 2020-08-25
 */
public class PageUtil {

    public static <T> IPage<T> buildPage(PageQueryInterface PageQueryInterface) {
        Objects.requireNonNull(PageQueryInterface);
        IPage<T> page = new Page<>(PageQueryInterface.getPage(), PageQueryInterface.getLimit());
        return page;
    }
}
