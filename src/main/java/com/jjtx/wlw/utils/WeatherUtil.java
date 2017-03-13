package com.jjtx.wlw.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjtx on 2017/2/16.
 */
public class WeatherUtil {

    /**
     * 通过经纬度获取天气情况编码
     *
     * @param locationX
     * @param locationY
     * @return
     */
    public static int getWeatherByLocation(String locationX, String locationY) {
        String url = "https://api.thinkpage.cn/v3/weather/now.json";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("key", "u3rykyqbrrhs6lcr");
        paramMap.put("language", "en");
        paramMap.put("location", locationX + ":" + locationY);
        paramMap.put("unit", "c");

        String result = WeiXinSender.sendGet(url, paramMap);

        return getWeatherFromResult(result);
    }


    /**
     * 解析心知天气的服务数据，并获取其中的天气标识码 code
     *
     * @param jsonResult
     * @return
     */
    public static int getWeatherFromResult(String jsonResult) {
        JSONObject jsonParser = new JSONObject(jsonResult);
        JSONObject object = (JSONObject) jsonParser.getJSONArray("results").get(0);
        JSONObject nowObject = object.getJSONObject("now");//现在的天气
        String code = nowObject.getString("code");

        return Integer.parseInt(code);


    }
}
