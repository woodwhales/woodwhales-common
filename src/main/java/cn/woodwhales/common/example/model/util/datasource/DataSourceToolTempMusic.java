package cn.woodwhales.common.example.model.util.datasource;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author woodwhales on 2021-07-25 13:25
 */
@Data
public class DataSourceToolTempMusic {

    private Long id;

    @Column(name = "title")
    private String title;

    @MyAnnotation(dbColumn = "artist")
    private String artist;

    private Integer sort;

    @Column(name = "gmt_created")
    private Date gmtCreated;

    private Date gmtModified;
}
