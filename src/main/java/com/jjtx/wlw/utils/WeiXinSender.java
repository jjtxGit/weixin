package com.jjtx.wlw.utils;

import com.jjtx.wlw.entity.MsgType;
import com.jjtx.wlw.entity.ResponseWxText;
import com.thoughtworks.xstream.XStream;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

/**
 * 回复文字信息
 */
public class WeiXinSender {
    /**
     * 向微信发送普通的文本信息
     *
     * @param response
     * @param fromUserName
     * @param toUserName
     * @param responseText
     */
    public static final void sendWXText(HttpServletResponse response, String fromUserName, String toUserName, String responseText) {

        response.setCharacterEncoding("utf-8");

        ResponseWxText responseWxText = new ResponseWxText();

        responseWxText.setCreateTime(System.currentTimeMillis() + "");
        responseWxText.setContent(responseText);
        responseWxText.setFromUserName(fromUserName);
        responseWxText.setToUserName(toUserName);
        responseWxText.setMsgType(MsgType.TEXT);


        PrintWriter out = null;

        XStream xStream = new XStream();
        xStream.alias("xml", responseWxText.getClass());
        String xml = xStream.toXML(responseWxText);

        try {
            out = response.getWriter();
            out.print(xml);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 以get方式向链接发送网络信息
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String sendGet(String url, Map<String, String> paramMap) {
        String result = "";
        BufferedReader in = null;

        StringBuilder param = new StringBuilder();
        Set<String> mapKeySet = paramMap.keySet();
        for (String key : mapKeySet) {
            param.append(key);
            param.append("=");
            param.append(paramMap.get(key));
            param.append("&");
        }

        param.deleteCharAt(param.length() - 1);//去除最后一个‘&’


        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性，模拟谷歌游览器访问
            connection.setRequestProperty("accept", "*/*");

            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }



}
