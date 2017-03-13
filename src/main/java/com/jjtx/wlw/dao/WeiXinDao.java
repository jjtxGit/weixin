package com.jjtx.wlw.dao;

import com.jjtx.wlw.dbutil.MySqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by jjtx on 2016/9/24.
 */
public class WeiXinDao {


    private MySqlHelper mySqlHelper = null;

    private static final WeiXinDao WeiXinDao = new WeiXinDao();

    private WeiXinDao() {
        this.mySqlHelper = new MySqlHelper("weixin", "root", "mysql");
    }

    public static final WeiXinDao getWeiXinDao() {
        return WeiXinDao;
    }

    /**
     * 获取默认的首要硬件设备编号
     *
     * @param userName
     * @return
     * @throws SQLException
     */
    public String getPrimaryItokenByUserName(String userName) {

        String sql = "select itoken from name_itoken where userName=?";
        PreparedStatement preparedStatement = mySqlHelper.getPreparedSta(sql);

        try {

            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySqlHelper.closeAll();
        }
        return null;
    }

    /**
     * 判断是否存在首选itoken设备
     *
     * @param userName
     * @return
     */
    public boolean hasPrimaryItokenByUserName(String userName) {
        return getPrimaryItokenByUserName(userName) != null;
    }

    /**
     * 添加一条用户设备关联信息到数据库
     *
     * @param itoken
     * @param userName
     */
    public void addPrimaryItokenWithUserName(String itoken, String userName) {

        String sql = "insert into name_itoken(userName,itoken) value(?,?)";
        PreparedStatement preparedStatement = mySqlHelper.getPreparedSta(sql);
        try {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, itoken);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySqlHelper.closeAll();
        }
    }

    /**
     * 判断itoken设备是否已经被关联
     *
     * @param itoken
     * @return
     */
    public boolean isItokenHasUser(String itoken) {

        String sql = "select count(*) from name_itoken where itoken=?";
        PreparedStatement ps = mySqlHelper.getPreparedSta(sql);
        try {

            ps.setString(1, itoken);
            ResultSet rs = ps.executeQuery();

            rs.next();
            int count = rs.getInt(1);

            return count > 0;


        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            mySqlHelper.closeAll();

        }

        return true;
    }


}
