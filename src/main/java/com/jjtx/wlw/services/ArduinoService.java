package com.jjtx.wlw.services;

import com.jjtx.wlw.entity.Arduino;
import com.jjtx.wlw.utils.WeatherUtil;
import com.jjtx.wlw.utils.WeiXinUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jjtx on 2016/10/7.
 */
public class ArduinoService {

    private ArduinoService() {
    }

    private static ArduinoService arduinoService = new ArduinoService();

    public static ArduinoService getInstance() {
        return arduinoService;
    }

    /**
     * 处理dataType=0时的指令
     */
    public String deal0Order(Arduino arduino) {

        //如果设备开启了自动收衣服务
        if (arduino.getAutoReceive()) {

            if (arduino.getTimer() == 0) {//如果设备的计数器为0，则表明已经过了一定的时间，开始检查天气情况
                arduino.resetTimer();
                int weatherCode = WeatherUtil.getWeatherByLocation(arduino.getLocationX(), arduino.getLocationY());

                if (weatherCode >= 10 && weatherCode <= 29) {//恶劣天气
                    return "21";//收衣
                }

            } else {//如果计数器不为0，则改变计数器的值
                arduino.decTimer();
            }
        }

        String result = arduino.getResponseCode();//获取 指令
        arduino.setResponseCode("0");//清理指令
        return result;
    }

    public String deal2Order(Arduino arduino, String dataValue) {
        arduino.setTemperature(dataValue);
        return "0";//返回0 以告诉单片机 连接正常
    }





//    public String deal1Order(Arduino arduino, String dataValue) {
//        if ("10".equals(dataValue)) {
//
//        }
//
//    }

}
