package cn.woodwhales.common.model.field;

import java.util.Date;

/**
 * @projectName: common
 * @author: woodwhales
 * @date: 20.9.18 21:30
 * @description:
 */
public interface CommonFieldTimeInterface {

    /**
     * 设置创建时间
     * @param createTime
     */
    void setCreateTime(Date createTime);

    /**
     * 获取创建时间
     * @return
     */
    Date getCreateTime();

    /**
     * 设置更新时间
     * @param updateTime
     */
    void setUpdateTime(Date updateTime);

    /**
     * 获取更新时间
     * @return
     */
    Date getUpdateTime();

}
