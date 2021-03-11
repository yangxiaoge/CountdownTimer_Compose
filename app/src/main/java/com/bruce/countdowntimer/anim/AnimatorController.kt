package com.bruce.countdowntimer.anim

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.bruce.countdowntimer.status.CompleteStatus
import com.bruce.countdowntimer.status.NotStartStatus
import com.bruce.countdowntimer.status.PausedStatus
import com.bruce.countdowntimer.status.StartedStatus
import com.bruce.countdowntimer.viewmodel.TimerViewModel

/**
 * <pre>
 *     author: Bruce_Yang
 *     email : yangjianan@seuic.com
 *     time  : 2021/03/09
 *     desc  : 动画控制
 * </pre>
 */
class AnimatorController(private val timerViewModel: TimerViewModel) {

    private val valueAnimator by lazy {
        //ofInt(),参数，从total->0
        ValueAnimator.ofInt(timerViewModel.totalTime.toInt(), 0).apply {
            interpolator = LinearInterpolator()
            //更新timeLeft
            addUpdateListener {
                timerViewModel.leftTime = (it.animatedValue as Int).toLong()
            }
            addListener(onEnd = { complete() })
        }
    }

    fun start() {
        //总时间为0时
        if (timerViewModel.totalTime == 0L) return

        valueAnimator.setIntValues(timerViewModel.totalTime.toInt(), 0)
        // 将 ValueAnimator 设置为在 totalTime 秒内从 totalTime 线性变化到 0 的方式设置出动画的间隔时间为 1s。
        valueAnimator?.duration = timerViewModel.totalTime * 1000L
        valueAnimator?.start()

        timerViewModel.status = StartedStatus(timerViewModel)
    }

    fun pause() {
        valueAnimator.pause()

        timerViewModel.status = PausedStatus(timerViewModel)
    }

    fun resume() {
        valueAnimator.resume()

        timerViewModel.status = StartedStatus(timerViewModel)
    }

    fun stop() {
        valueAnimator.cancel()
        timerViewModel.leftTime = 0

        timerViewModel.status = NotStartStatus(timerViewModel)
    }

    fun complete() {
        timerViewModel.totalTime = 0

        timerViewModel.status = CompleteStatus(timerViewModel)
    }
}