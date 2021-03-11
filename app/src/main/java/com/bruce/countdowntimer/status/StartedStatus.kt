package com.bruce.countdowntimer.status

import com.bruce.countdowntimer.viewmodel.TimerViewModel

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2021/03/09
 *     desc  : 已开始状态
 * </pre>
 */
class StartedStatus(private val timerViewModel: TimerViewModel) : IStatus {
    override fun progressSweepAngle(): Float {
        return timerViewModel.leftTime * 1.0f / timerViewModel.totalTime * 360
    }

    override fun startBtnText(): String {
        return "Pause"
    }

    override fun startBtnClick() {
        timerViewModel.animatorController.pause()
    }

    override fun stopBtnEnable(): Boolean {
        return true
    }

    override fun stopBtnClick() {
        timerViewModel.animatorController.stop()
    }

    override fun showTimeEdit(): Boolean {
        return false
    }
}