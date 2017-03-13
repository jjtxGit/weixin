package com.jjtx.wlw.services;

import com.jjtx.wlw.dao.WeiXinDao;
import com.jjtx.wlw.entity.Arduino;
import com.jjtx.wlw.entity.MsgInfo;
import com.jjtx.wlw.entity.WXText;
import com.jjtx.wlw.utils.WeiXinSender;
import com.jjtx.wlw.utils.WeiXinUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jjtx on 2016/9/24.
 */
public class OperationService {

    private WeiXinDao weiXinDao = null;

    private static final OperationService operationService = new OperationService();

    private OperationService() {
        weiXinDao = WeiXinDao.getWeiXinDao();
    }

    public static final OperationService getOperationService() {
        return operationService;
    }

    /**
     * 获取设备的温度，并将温度发给微信
     *
     * @param response
     */
    public void sendTemperature(HttpServletResponse response, WXText wxText, ServletContext servletContext) {

        String responseMessage = "";

        Arduino arduino = null;

        if ((arduino = WeiXinUtils.isPrepareOk(response, wxText.getFromUserName(), wxText.getToUserName(), servletContext)) != null) {
            arduino.setResponseCode("2");

            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            responseMessage = "现在温度为:" + arduino.getTemperature() + "度";
            arduino.setTemperature("0");
            WeiXinSender.sendWXText(response, wxText.getToUserName(), wxText.getFromUserName(), responseMessage);
        }
    }


    /**
     * 控制arduino的灯的开关状态
     *
     * @param response
     * @param wXText
     * @param onOrOff
     */

    public void operatorLed(HttpServletResponse response, WXText wXText, int onOrOff, ServletContext context) {
        Arduino arduino = null;
        String responseMessage = "";

        if ((arduino = WeiXinUtils.isPrepareOk(response, wXText.getFromUserName(), wXText.getToUserName(), context)) != null) {
            if (onOrOff == 1) {
                arduino.setResponseCode("11");
                responseMessage = "开灯成功";
            } else {
                arduino.setResponseCode("10");
                responseMessage = "关灯成功";
            }
            WeiXinSender.sendWXText(response, wXText.getToUserName(), wXText.getFromUserName(), responseMessage);
        }
    }


    /**
     * 处理 注册逻辑
     *
     * @param response
     * @param elements
     * @param wxText
     */

    public void regirestArduino(HttpServletResponse response, String[] elements, WXText wxText) {

        String itoken;

        if (elements.length == 1) {
            itoken = null;
        } else {
            itoken = elements[1];
        }

        String responseMessage = "";

        if (itoken == null || itoken.isEmpty()) {
            responseMessage = "请使用 :\n\t注册:您的设备编号\n关联设备";

        } else if (weiXinDao.hasPrimaryItokenByUserName(wxText.getFromUserName())) {
            responseMessage = "您的账号已经关联了设备,\n关联的设备编号为:" + weiXinDao.getPrimaryItokenByUserName(wxText.getFromUserName());

        } else if (itoken.length() != 11) {
            responseMessage = "设备编号的长度应为11位.";

        } else if (weiXinDao.isItokenHasUser(itoken)) {
            responseMessage = "该设备已经被关联了";

        } else {
            weiXinDao.addPrimaryItokenWithUserName(itoken, wxText.getFromUserName());
            responseMessage = "注册成功！开始使用吧";
        }

        WeiXinSender.sendWXText(response, wxText.getToUserName(), wxText.getFromUserName(), responseMessage);

    }

    /**
     * 返回給微信客服端 服务信息
     *
     * @param response
     * @param wxText
     */
    public void sendHelper(HttpServletResponse response, WXText wxText) {

        WeiXinSender.sendWXText(response, wxText.getToUserName(), wxText.getFromUserName(), MsgInfo.HELP_MSG);

    }


}

