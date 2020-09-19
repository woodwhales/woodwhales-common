package org.woodwhales.common.model.field;

/**
 * @projectName: woodwhales-common
 * @author: woodwhales
 * @date: 20.9.18 22:15
 * @description:
 */
public interface PageQueryInterface {

    /**
     * 设置当前页码
     * @param page
     */
    void setPage(Integer page);

    /**
     * 设置当前页总记录数
     * @param limit
     */
    void setLimit(Integer limit);

    /**
     * 获取当前页码
     * @return
     */
    Integer getPage();

    /**
     * 获取当前页总记录数
     * @return
     */
    Integer getLimit();
}
