package com.bruce.countdowntimer.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bruce.countdowntimer.anim.AnimatorController
import com.bruce.countdowntimer.status.IStatus
import com.bruce.countdowntimer.status.NotStartStatus

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2021/03/09
 *     desc  : TimerViewModel
 * </pre>
 */
class TimerViewModel : ViewModel() {
    //总时间 by 属性代理
    var totalTime: Long by mutableStateOf(0L)

    //剩余时间 by 属性代理
    var leftTime: Long by mutableStateOf(0L)

    //动画对象
    val animatorController = AnimatorController(this)

    //状态  by 属性代理
    var status: IStatus by mutableStateOf(NotStartStatus(this))

    /**
     * 更新时间
     * @param timeStr 时间
     */
    fun updateTime(timeStr: String) {
        //长度不要超过6
        if (timeStr.length > 6) return
        //剔除非数字字符
        var value = timeStr.replace("\\D".toRegex(), "")
        //剔除首位0
        if (value.startsWith("0"))
            value = value.substring(1)
        if (value.isBlank())
            value = "0"
        totalTime = value.toLong()
        leftTime = value.toLong()
    }

    override fun onCleared() {
        super.onCleared()
        animatorController.stop()
    }
}
