package com.jjtx.wlw.entity

/**
 * 微信位置类
 */
data class WXLocation constructor(

        /**
         * 地理位置纬度
         */
        var Location_X: String = "",

        var CreateTime: String = "",
        /**
         * 地理位置经度
         */
        var Location_Y: String = "",

        var Label:String ="",

        var Scale:String ="",

        var ToUserName: String = "",

        var FromUserName: String = "",

        var MsgType: String = "",

        var MsgId:String =""
)