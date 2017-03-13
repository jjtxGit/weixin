package com.jjtx.wlw.services;

import com.jjtx.wlw.entity.Arduino;
import com.jjtx.wlw.entity.WXLocation;
import com.jjtx.wlw.entity.WXText;
import com.jjtx.wlw.utils.WeiXinSender;
import com.jjtx.wlw.utils.WeiXinUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjtx on 2016/9/20.
 */
public class MainService {
    private static MainService ourInstance = new MainService();

    public static MainService getInstance() {
        return ourInstance;
    }

    private MainService() {
        operationService = OperationService.getOperationService();
    }

    private OperationService operationService;

    public void weiXinJoinUp(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");


        if (WeiXinUtils.JoinUpCheck(timestamp, nonce, signature)) {
            PrintWriter out = response.getWriter();
            out.print(echostr);
        }

    }

    public void textService(HttpServletRequest request, HttpServletResponse response) {

        String data = request.getParameter("data");

        String result = "we hava get it and data = " + data;


    }

    /**
     * 处理发来的文字信息
     *
     * @param request
     * @param response
     * @param map
     */
    public void dealText(HttpServletRequest request, HttpServletResponse response, Map<String, String> map) {

        WXText wxText = WeiXinUtils.getEntityByMap(WXText.class, map);

        String content = wxText.getContent();


        String[] elements;

        if (content.contains("：")) {
            elements = content.split("：");
        } else {
            elements = content.split(":");
        }

        switch (elements[0]) {


            case "温度":
                operationService.sendTemperature(response, wxText, request.getServletContext());
                break;
            case "注册":
                operationService.regirestArduino(response, elements, wxText);
                break;
            case "开灯":
                operationService.operatorLed(response, wxText, 1, request.getServletContext());
                break;
            case "关灯":
                operationService.operatorLed(response, wxText, 0, request.getServletContext());
                break;
            case "帮助":
                operationService.sendHelper(response, wxText);
                break;
        }

    }

    /**
     * 处理微信发来的地理位置信息
     *
     * @param request
     * @param response
     * @param map
     */
    public void dealLocation(HttpServletRequest request, HttpServletResponse response, Map map) throws IOException {

        WXLocation location = WeiXinUtils.getEntityByMap(WXLocation.class, map);

        Arduino arduino = WeiXinUtils.isPrepareOk(response, location.getFromUserName(), location.getToUserName(), request.getServletContext());

        if (arduino == null) {
            return;//如果没有找到用户的设备，则直接退出
        }

        //开启收衣服务，并设置各种参数
        arduino.setAutoReceive(true);
        arduino.setLocationX(location.getLocation_X());
        arduino.setLocationY(location.getLocation_Y());
        arduino.resetTimer();

        WeiXinSender.sendWXText(response, location.getToUserName(), location.getFromUserName(), "已为您自动开启防雨功能，当雨水降临时，将自动为您收衣");

    }


}
