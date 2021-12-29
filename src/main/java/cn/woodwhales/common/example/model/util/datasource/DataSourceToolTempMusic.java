package cn.woodwhales.common.example.model.util.datasource;

import lombok.Data;

import java.util.Date;

/**
 * @author woodwhales on 2021-07-25 13:25
 */
@Data
public class DataSourceToolTempMusic {

    private Long id;

    private String title;

    private String artist;

    @DataSourceIgnore
    private Integer sort;

    private Date gmtCreated;

    private Date gmtModified;
}
