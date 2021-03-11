package com.bruce.countdowntimer.status

import com.bruce.countdowntimer.viewmodel.TimerViewModel

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2021/03/09
 *     desc  :
 * </pre>
 */
class PausedStatus(private val timerViewModel: TimerViewModel) : IStatus {
    override fun progressSweepAngle(): Float {
        return timerViewModel.leftTime * 1.0f / timerViewModel.totalTime * 360
    }

    override fun startBtnText(): String {
        return "Resume"
    }

    override fun startBtnClick() {
        timerViewModel.animatorController.resume()
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