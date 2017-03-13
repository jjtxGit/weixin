package com.jjtx.wlw.entity

/**
 * Created by jjtx on 2016/9/24.
 */
data class ResponseWxText constructor(
        var ToUserName: String = "",
        var FromUserName: String = "",
        var CreateTime: String = "",
        var MsgType: String = "",
        var Content: String = ""
)