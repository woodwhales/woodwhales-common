package cn.woodwhales.common.example.util.datasource;

import cn.woodwhales.common.example.model.util.datasource.DataSourceToolTempMusic;
import cn.woodwhales.common.util.datasource.DataSourceTool;

import java.util.List;

/**
 * @author woodwhales on 2021-07-25 13:28
 * DataSourceTool 使用示例
 */
public class DataSourceToolExample {

    public static void main(String[] args) throws Exception {
        testFormatSql();
    }

    private static void test() throws Exception {
        DataSourceTool dataSourceTool = DataSourceTool.newMysql("jdbc:mysql://127.0.0.1:3306/open_music?useUnicode=true&characterEncoding=utf8&autoReconnect=true&autoReconnectForPools=true&zeroDateTimeBehavior=convertToNull&useSSL=false",
                "root", "root1234");

        List<DataSourceToolTempMusic> musicList = dataSourceTool.queryList("select * from music", DataSourceToolTempMusic.class);
        musicList.stream().forEach(System.out::println);

        System.out.println(" ================ ");

        DataSourceToolTempMusic music = dataSourceTool.queryOne("select * from music where id = 3", DataSourceToolTempMusic.class);
        System.out.println("music = " + music);
    }

    private static void testFormatSql() {
        String sql = "update table set a = {}, b = {}, d = {}, e = {} where f = {}";
        Object a = "aaa";
        Object b = 2.0;
        Object d = "[1,2,3]";
        Object e = null;
        Object f = 23L;
        String finalSql = DataSourceTool.formatSql(sql, a, b, d, e, f);
        System.out.println("finalSql = " + finalSql);
    }

}
