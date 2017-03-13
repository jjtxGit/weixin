package com.jjtx.wlw.servlet;

import com.jjtx.wlw.entity.MsgType;
import com.jjtx.wlw.services.MainService;
import com.jjtx.wlw.utils.WeiXinUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * MainServlet用于对接微信的接入服务
 * get方法进行接入验证
 * pos方法用来获取消息
 */
public class MainServlet extends HttpServlet {

    private MainService mainService;

    public MainServlet() {
        super();
        mainService = MainService.getInstance();
    }

    /**
     * 微信信息分发处理控制
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, String> map = WeiXinUtils.getMapFromStream(request.getInputStream());

        switch (map.get("MsgType")) {

            case MsgType.TEXT://收到来自微信的普通文本信息
                mainService.dealText(request, response, map);
                break;
            case MsgType.LOCATION://收到来自微信的位置信息
                mainService.dealLocation(request, response, map);
                break;
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mainService.weiXinJoinUp(request, response);//微信接入验证
    }
}
