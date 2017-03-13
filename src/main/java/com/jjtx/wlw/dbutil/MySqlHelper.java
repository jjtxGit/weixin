package com.jjtx.wlw.dbutil;

import java.sql.*;

public class MySqlHelper {

    private Statement statement = null;
    private Connection connection = null;
    private PreparedStatement pStatement = null;

    /**
     * 省略端口的连接方式<br>
     * <b> 默认端口为:3306</b>
     *
     * @param url          :数据库服务器地址
     * @param userName     :用户名
     * @param password     :密码
     * @param dataBaesName :数据库名
     * @throws SQLException
     */
    public MySqlHelper(String url, String userName, String password, String dataBaesName) {
        this(url, 3306, userName, password, dataBaesName);
    }

    /**
     * 连接到指定数据库
     *
     * @param url          :数据库服务器地址
     * @param port         :数据库服务器的端口
     * @param userName     :用户名
     * @param password     :密码
     * @param dataBaesName :数据库名
     */
    public MySqlHelper(String url, int port, String userName, String password, String dataBaesName) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String arg = "jdbc:mysql://" + url + ":" + port + "/" + dataBaesName;

        try {
            this.connection = DriverManager.getConnection(arg, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接到<b>本机</b>数据库服务器<br>
     * <b>默认端口为3306</b>
     *
     * @param userName     :用户名
     * @param password     :密码
     * @param dataBaesName :数据库名
     * @throws SQLException
     */
    public MySqlHelper(String dataBaesName, String userName, String password) {
        this("localhost", userName, password, dataBaesName);
    }

    /**
     * 获取预命令对象
     *
     * @param preparedStatement:预命令
     * @throws SQLException
     */
    public PreparedStatement getPreparedSta(String preparedStatement) {
        try {
            pStatement = connection.prepareStatement(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pStatement;
    }

    /**
     * 获取 操作对象
     */
    public Statement getStatement() {

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    /**
     * 获取连接对象
     *
     * @return
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * 关闭所有数据库连接
     *
     * @throws SQLException
     */

    public void closeAll() {
        try {

            if (pStatement != null && !pStatement.isClosed())
                pStatement.close();

            if (statement != null && !statement.isClosed())
                statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
