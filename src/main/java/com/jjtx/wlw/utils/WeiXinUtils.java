package com.jjtx.wlw.utils;

import com.jjtx.wlw.dao.WeiXinDao;
import com.jjtx.wlw.entity.Arduino;
import com.jjtx.wlw.entity.WXText;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by jjtx on 2016/9/20.
 */
public class WeiXinUtils {

    private static final String TOKEN = "jjtxwlw";

    /**
     * 微信接入验证工具类
     *
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    public static boolean JoinUpCheck(String timestamp, String nonce, String signature) {

        String[] strs = {timestamp, nonce, TOKEN};
        Arrays.sort(strs);
        StringBuilder temp = new StringBuilder();
        for (String str : strs) {
            temp.append(str);
        }
        String resultStr = temp.toString();
        resultStr = SHA(resultStr);
        return signature.equals(resultStr);
    }

    /**
     * sha1加密算法
     *
     * @param decript
     * @return
     */
    private static String SHA(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从输入流中获取xml文档，并把xml文档转为map对象返回
     *
     * @return
     */
    public static Map<String, String> getMapFromStream(InputStream is) {
        Map<String, String> map = new HashMap<String, String>();

        SAXReader reader = new SAXReader();
        List<Element> elements = null;

        try {
            elements = reader.read(is).getRootElement().elements();
        } catch (DocumentException e) {
            e.printStackTrace();
            map.put("MsgType", null);
            return map;
        }

        for (Element element : elements) {
            map.put(element.getName(), element.getText());
        }
        return map;
    }

    /**
     * 根据map获取传来的值信息
     *
     * @param instance
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T getEntityByMap(Class<T> instance, Map<String, String> map) {
        T t = null;
        try {
            t = instance.newInstance();

            Set<String> keys = map.keySet();

            for (String key : keys) {
                String value = map.get(key);
                Method method = instance.getMethod("set" + key, String.class);
                method.invoke(t, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * 根据itoken获取Arduino的虚拟对象
     *
     * @param servletContext
     * @param itoken
     * @return
     */
    public static Arduino getArduino(ServletContext servletContext, String itoken) {
        HttpSession session = (HttpSession) servletContext.getAttribute(itoken);
        if (session == null) {
            return null;
        }
        Arduino arduino = (Arduino) session.getAttribute(itoken);

        return arduino;

    }

    /**
     * 把arduino推入Package中
     *
     * @param arduino
     * @param request
     * @param itoken
     */
    public static void setAruino(Arduino arduino, HttpServletRequest request, String itoken) {
        HttpSession session = request.getSession();
        session.setAttribute(itoken, arduino);
        request.getServletContext().setAttribute(itoken, session);
    }

    /**
     * 判断一些非必要的操作
     *
     * @param response
     * @param servletContext
     * @param fromUserName:微信客户端的id编号
     * @param toUserName:微信服务器的id编号
     * @return
     */
    public static Arduino isPrepareOk(HttpServletResponse response, String fromUserName, String toUserName, ServletContext servletContext) {

        WeiXinDao weiXinDao = WeiXinDao.getWeiXinDao();

        String responseMessage = "";
        Arduino arduino = null;
        if (!weiXinDao.hasPrimaryItokenByUserName(fromUserName)) {
            responseMessage = "您好,你尚未拥有硬件设备，请回复 注册:您的设备编号 以关联设备";
            WeiXinSender.sendWXText(response, toUserName, fromUserName, responseMessage);
        } else {
            String itoken = weiXinDao.getPrimaryItokenByUserName(fromUserName);
            arduino = WeiXinUtils.getArduino(servletContext, itoken);
            if (arduino == null) {
                responseMessage = "您的设备离线了";
                WeiXinSender.sendWXText(response, toUserName, fromUserName, responseMessage);
            }
        }
        return arduino;
    }

}
