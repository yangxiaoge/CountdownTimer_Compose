package com.bruce.countdowntimer.status

import com.bruce.countdowntimer.viewmodel.TimerViewModel

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2021/03/09
 *     desc  : 未开始状态
 * </pre>
 */
class NotStartStatus(private val timerViewModel: TimerViewModel) : IStatus {
    override fun progressSweepAngle(): Float {
        return if (timerViewModel.totalTime > 0) 360f else 0f
    }

    override fun startBtnText(): String {
        return "Start"
    }

    override fun startBtnClick() {
        timerViewModel.animatorController.start()
    }

    override fun stopBtnEnable(): Boolean {
        return false
    }

    override fun stopBtnClick() {

    }

    override fun showTimeEdit(): Boolean {
        return true
    }
}