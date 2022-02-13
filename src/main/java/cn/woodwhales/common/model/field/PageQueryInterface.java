package cn.woodwhales.common.model.field;

/**
 * @author: woodwhales on 2020-09-18 22:15
 * <p>
 * 分页查询请求对象接口
 */
public interface PageQueryInterface {

    /**
     * 设置当前页码
     *
     * @param page 当前页码
     */
    void setPage(Integer page);

    /**
     * 设置当前页总记录数
     *
     * @param limit 当前页总记录数
     */
    void setLimit(Integer limit);

    /**
     * 获取当前页码
     *
     * @return 当前页码
     */
    Integer getPage();

    /**
     * 获取当前页总记录数
     *
     * @return 当前页总记录数
     */
    Integer getLimit();
}
