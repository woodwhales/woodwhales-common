package org.woodwhales.common.model.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.woodwhales.common.model.field.PageQueryInterface;

import java.util.Objects;

/**
 * @author woodwhales on 2020-08-25
 * @description
 */
public class PageUtil {

    public static <T> IPage<T> buildPage(PageQueryInterface PageQueryInterface) {
        Objects.requireNonNull(PageQueryInterface);
        IPage<T> page = new Page<>(PageQueryInterface.getPage(), PageQueryInterface.getLimit());
        return page;
    }
}
