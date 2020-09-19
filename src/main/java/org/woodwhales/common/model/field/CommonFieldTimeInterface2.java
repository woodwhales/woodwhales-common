package org.woodwhales.common.model.field;

import java.util.Date;

/**
 * @projectName: common
 * @author: woodwhales
 * @date: 20.9.18 21:33
 * @description:
 */
public interface CommonFieldTimeInterface2 {

    /**
     * 设置创建时间
     * @param gmtCreate
     */
    void setGmtCreate(Date gmtCreate);

    /**
     * 获取创建时间
     * @return
     */
    Date getGmtCreate();

    /**
     * 设置更新时间
     * @param gmtModified
     */
    void setGmtModified(Date gmtModified);

    /**
     * 获取更新时间
     * @return
     */
    Date getGmtModified();

}
