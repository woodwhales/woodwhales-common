package cn.woodwhales.common.example.util.datasource;

import cn.woodwhales.common.example.model.util.datasource.DataSourceToolTempRbacRole;
import cn.woodwhales.common.example.model.util.datasource.MyAnnotation;
import cn.woodwhales.common.util.datasource.DataSourceTool;

import java.util.List;

/**
 * @author woodwhales on 2021-07-25 13:28
 * DataSourceTool 使用示例
 */
public class DataSourceToolExample {

    public static void main(String[] args) throws Exception {
        DataSourceTool dataSourceTool = DataSourceTool.newMysql("jdbc:mysql://127.0.0.1:3306/woodwhales_rbac?useUnicode=true&characterEncoding=utf8&autoReconnect=true&autoReconnectForPools=true&zeroDateTimeBehavior=convertToNull&useSSL=false",
                "root", "root");

        List<DataSourceToolTempRbacRole> rbacRoles = dataSourceTool.queryList("select * from rbac_role", DataSourceToolTempRbacRole.class, MyAnnotation.class, MyAnnotation::dbColumn);
        rbacRoles.stream().forEach(System.out::println);

        DataSourceToolTempRbacRole rbacRole = dataSourceTool.queryOne("select * from rbac_role where id = 3", DataSourceToolTempRbacRole.class, MyAnnotation.class, MyAnnotation::dbColumn);
        System.out.println("rbacRole = " + rbacRole);
    }

}
