package cn.woodwhales.common.model.field;

/**
 * @author woodwhales on 22.8.12 11:10
 */
public interface DefaultPageQueryInterface {

    /**
     * 获取当前页码
     * @return
     */
    Integer getCurrentPage();

    /**
     * 设置当前页码
     * @param currentPage 当前页码
     */
    void setCurrentPage(Integer currentPage);

    /**
     * 获取每页记录数
     * @return
     */
    Integer getPageSize();

    /**
     * 设置每页记录数
     * @param pageSize 每页记录数
     */
    void setPageSize(Integer pageSize);

}
