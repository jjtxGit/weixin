package com.jjtx.wlw.servlet;

import com.jjtx.wlw.entity.Arduino;
import com.jjtx.wlw.services.ArduinoService;
import com.jjtx.wlw.utils.WeatherUtil;
import com.jjtx.wlw.utils.WeiXinSender;
import com.jjtx.wlw.utils.WeiXinUtils;
import jdk.internal.util.xml.impl.Input;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjtx on 2016/9/21.
 */
public class ArduinoServlet extends HttpServlet {

    private ArduinoService arduinoService;

    public ArduinoServlet() {
        arduinoService = ArduinoService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
        InputStream is = request.getInputStream();

        int bytes;
        while ((bytes = is.read()) != -1) {
            System.out.print(bytes);
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String itoken = request.getParameter("itoken");
        String dataType = request.getParameter("dataType");
        String dataValue = request.getParameter("dataValue");


        /**
         * msg=0:返回0以告诉单片机 连接正常
         * msg=11:开灯
         * msg=10:关灯
         * msg=20:收衣
         * msg=21:晒衣
         */
        String msg = "";//返回给单片机的信息

        HttpSession session = (HttpSession) request.getServletContext().getAttribute(itoken);

        if (session == null) {
            session = request.getSession();
        }

        Arduino arduino = (Arduino) session.getAttribute(itoken);

        if (arduino == null) {//如果连入的arduino设备为一个新的设备
            arduino = new Arduino();
        }

        switch (dataType) {
            case "0"://单片机询问服务器的指令
                msg = arduinoService.deal0Order(arduino);

                break;
            case "1"://单片机发来与电灯相关的信息
                msg = "0";//
                break;

            case "2"://单片机发来的数据是温度的数据
                msg = arduinoService.deal2Order(arduino, dataValue);
                break;

        }

        WeiXinUtils.setAruino(arduino, request, itoken);

        System.out.println("responseMsg=" + msg);

        sendMessage(response, msg);

    }

    private void sendMessage(HttpServletResponse response, String msg) throws IOException {
        //response.setContentType("text/plain");

        OutputStream os = response.getOutputStream();

        String sendMsg = "<" + msg + ">";

        os.write(sendMsg.getBytes());
        os.flush();
        os.close();
    }


}
