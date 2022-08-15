package cn.woodwhales.common.model.field;

import java.util.Date;

/**
 * @author woodwhales
 * 2020.9.18 21:33
 */
public interface CommonFieldTimeInterface2 {

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    void setGmtCreate(Date gmtCreate);

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    Date getGmtCreate();

    /**
     * 设置更新时间
     *
     * @param gmtModified 更新时间
     */
    void setGmtModified(Date gmtModified);

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    Date getGmtModified();

}
