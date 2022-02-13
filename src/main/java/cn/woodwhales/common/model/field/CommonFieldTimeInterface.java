package cn.woodwhales.common.model.field;

import java.util.Date;

/**
 * common
 *
 * @author: woodwhales
 * 20.9.18 21:30
 */
public interface CommonFieldTimeInterface {

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    void setCreateTime(Date createTime);

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    Date getCreateTime();

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    void setUpdateTime(Date updateTime);

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    Date getUpdateTime();

}
