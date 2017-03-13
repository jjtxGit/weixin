package com.jjtx.wlw.entity

/**
 * 微信普通文本类
 */
data class WXText constructor(
        var ToUserName: String = "",
        var FromUserName: String = "",
        var CreateTime: String = "",
        var MsgType: String = "",
        var Content: String = "",
        var MsgId: String = ""
)