package org.woodwhales.util;

import lombok.Data;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

class DataSourceToolTest {

    @Test
    public void test() throws Exception {
        DataSourceTool dataSourceTool = DataSourceTool.newMysql("jdbc:mysql://127.0.0.1:3306/woodwhales_rbac?useUnicode=true&characterEncoding=utf8&autoReconnect=true&autoReconnectForPools=true&zeroDateTimeBehavior=convertToNull&useSSL=false",
                "root", "root");

        List<RbacRole> rbacRoles = dataSourceTool.queryList("select * from rbac_role", RbacRole.class, MyAnnotation.class, MyAnnotation::dbColumn);
        rbacRoles.stream().forEach(System.out::println);

        RbacRole rbacRole = dataSourceTool.queryOne("select * from rbac_role where id = 3", RbacRole.class, MyAnnotation.class, MyAnnotation::dbColumn);
        System.out.println("rbacRole = " + rbacRole);
    }

    @Data
    public static class RbacRole {
        @MyAnnotation(dbColumn = "id")
        private Long id;

        @Column(name = "name")
        private String name;

        @Column(name = "create_time")
        private Date createTime;

        @Column(name = "update_time")
        private LocalDateTime updateTime;

        @Column(name = "status")
        private Boolean status;

        @Column(name = "number")
        private BigDecimal number;
    }

    @Target({METHOD, FIELD})
    @Retention(RUNTIME)
    public @interface MyAnnotation {
        String dbColumn();
    }
}