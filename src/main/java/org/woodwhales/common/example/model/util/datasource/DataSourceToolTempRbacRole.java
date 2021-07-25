package org.woodwhales.common.example.model.util.datasource;

import lombok.Data;
import org.woodwhales.common.util.datasource.DataColumn;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author woodwhales on 2021-07-25 13:25
 * @Description
 */
@Data
public class DataSourceToolTempRbacRole {
    @MyAnnotation(dbColumn = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @DataColumn("create_time")
    private Date createTime;

    @DataColumn("update_time")
    private LocalDateTime updateTime;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "number")
    private BigDecimal number;
}
