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
class CompleteStatus(private val timerViewModel: TimerViewModel) : IStatus {
    override fun progressSweepAngle(): Float {
        return 0f
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