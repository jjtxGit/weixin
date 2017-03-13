package com.jjtx.wlw.entity

/**
 * Created by jjtx on 2016/9/24.
 */
class Arduino() {

    //计数器的初始值
    private val INIT_TIMER = 500;

    var itoken = ""

    var temperature = ""

    var responseCode = "0"

    //计数器
    var timer = 0;
        private set

    //经度
    var locationX = ""
    //纬度
    var locationY = ""
    //自动收衣开关
    var autoReceive = false;

    /**
     * 初始化计数器
     */
    fun resetTimer() {
        timer = INIT_TIMER;
    }

    /**
     *将计数器递减
     */
    fun decTimer() {
        if (timer > 0) {
            timer--;
        } else {
            resetTimer();
        }
    }

}