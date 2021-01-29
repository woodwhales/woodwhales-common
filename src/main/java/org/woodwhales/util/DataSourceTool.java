package org.woodwhales.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author woodwhales on 2021-01-28 21:22
 * @description 数据库查询工具
 */
public class DataSourceTool {

    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DataSourceTool(String driverClass, String url, String username, String password) {
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driverClass);
            Connection connection = DriverManager.getConnection(url, username, password);
            this.connection = connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> queryList(String sql, Function<ResultSet, T> function) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<T> dataList = new ArrayList<>();
        while (rs.next()) {
            T data = function.apply(rs);
            dataList.add(data);
        }
        statement.close();
        return dataList;
    }

    public <T> T queryOne(String sql, Function<ResultSet, T> function) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        try {
            rs.next();
            return function.apply(rs);
        } finally {
            statement.close();
            connection.close();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private DataSourceTool() {
    }
}
