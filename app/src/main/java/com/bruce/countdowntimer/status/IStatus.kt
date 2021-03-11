package com.bruce.countdowntimer.status

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2021/03/09
 *     desc  : IStatus状态接口
 * </pre>
 */
interface IStatus {
    /**
     * 圆环扫过的度数值
     */
    fun progressSweepAngle(): Float

    /**
     * 开始按钮的文字：Start,Pause,Resume
     */
    fun startBtnText(): String

    /**
     * 开始按钮点击事件
     */
    fun startBtnClick()

    /**
     * 停止按钮是否可点击
     */
    fun stopBtnEnable(): Boolean

    /**
     * 停止按钮点击事件
     */
    fun stopBtnClick()

    /**
     * 显示时间输入框
     */
    fun showTimeEdit(): Boolean
}