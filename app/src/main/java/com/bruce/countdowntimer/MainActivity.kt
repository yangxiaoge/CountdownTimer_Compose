package com.bruce.countdowntimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruce.countdowntimer.ui.theme.CountdownTimerTheme
import com.bruce.countdowntimer.viewmodel.TimerViewModel
import kotlinx.coroutines.*
import java.lang.Math.cos
import java.lang.Math.sin
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountdownTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val timerViewModel: TimerViewModel = viewModel()
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar({ Text(stringResource(id = R.string.app_name)) })
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LeftTimeText(timerViewModel)
            ProgressCircleView(timerViewModel)
            TimeEditText(timerViewModel)
            Row {
                StartBtn(timerViewModel)
                StopBtn(timerViewModel)
            }
        }
    }

    //从上至下同步执行
    timerViewModel.viewModelScope.launch {
        val measureTimeMillis = measureTimeMillis {
            val userInfo = getUserInfo()
            val friendList = getFriendList()
            val feedList = getFeedList()
            println("111$userInfo, $friendList, $feedList")
        }
        println("cost1:$measureTimeMillis")
    }
    //异步async await执行
    timerViewModel.viewModelScope.launch {
        val measureTimeMillis = measureTimeMillis {
            val userInfo = async { getUserInfo() }
            val friendList = async { getFriendList() }
            val feedList = async { getFeedList() }
            println("222${userInfo.await()}, ${friendList.await()}, ${feedList.await()}")
        }
        println("cost2:$measureTimeMillis")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CountdownTimerTheme {
        MyApp()
    }
}

@Composable
fun LeftTimeText(timerViewModel: TimerViewModel) {
    Text(
        text = timerViewModel.leftTime.formatTime(),
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun TimeEditText(timerViewModel: TimerViewModel) {
    Box(
        modifier = Modifier.size(280.dp, 100.dp),
        contentAlignment = Alignment.Center
    ) {
        //根据状态判断是否显示时间输入框
        if (timerViewModel.status.showTimeEdit()) {
            TextField(
                value = if (timerViewModel.totalTime == 0L) "" else timerViewModel.totalTime.toString(),
                onValueChange = { timerViewModel.updateTime(it) },
                label = { Text("Countdown Seconds") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun StartBtn(timerViewModel: TimerViewModel) {
    Button(
        onClick = timerViewModel.status::startBtnClick,
        enabled = timerViewModel.totalTime > 0,
        modifier = Modifier
            .width(150.dp)
            .padding(16.dp)
    ) {
        Text(timerViewModel.status.startBtnText())
    }
}

@Composable
fun StopBtn(timerViewModel: TimerViewModel) {
    Button(
        onClick = timerViewModel.status::stopBtnClick,
        enabled = timerViewModel.status.stopBtnEnable(),
        modifier = Modifier
            .width(150.dp)
            .padding(16.dp)
    ) {
        Text("Stop")
    }
}

@Composable
fun ProgressCircleView(timerViewModel: TimerViewModel) {
    //画布宽高，也是圆的直径
    val size = 180.dp
    Canvas(modifier = Modifier.size(size)) {
        //划过的角度
        val progressSweepAngle = timerViewModel.status.progressSweepAngle()
        //半径
        val radius = size.toPx() / 2
        //环宽
        val strokeWidth = 12.dp.toPx()

        //绘制环圆刻度
        drawCircle(
            color = Color.LightGray,
            style = Stroke(
                width = strokeWidth,
                //以给定的间隔绘制一系列虚线
                pathEffect = PathEffect.dashPathEffect(
                    //intervals 中的第一个 Float 参数指刻度的宽度
                    //第二个 Float 参数指刻度的间距
                    intervals = floatArrayOf(
                        1.dp.toPx(),
                        3.dp.toPx()
                    )
                )
            )
        )
        //从 -90° 开始绘制圆环
        drawArc(
            brush = Brush.sweepGradient(
                0f to Color.Magenta,
                0.5f to Color.Blue,
                0.75f to Color.Green,
                0.75f to Color.Transparent,
                1f to Color.Magenta
            ),
            startAngle = -90f,
            sweepAngle = progressSweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth),
            alpha = 0.5f
        )

        //指针
        val angle = (360 - progressSweepAngle) / 180 * Math.PI
        //指针尾巴长度
        val pointTailLength = 10.dp.toPx()
        //画指针线
        drawLine(
            color = Color.Red,
            start = Offset(
                radius + pointTailLength * sin(angle).toFloat(),
                radius + pointTailLength * cos(angle).toFloat()
            ),
            end = Offset(
                (radius - radius * sin(angle) - sin(angle) * strokeWidth / 2).toFloat(),
                (radius - radius * cos(angle) - cos(angle) * strokeWidth / 2).toFloat()
            ),
            strokeWidth = 2.dp.toPx()
        )
        //红色圆
        drawCircle(
            color = Color.Red,
            radius = 6.dp.toPx(),
        )
        //白色小圆
        drawCircle(
            color = Color.White,
            radius = 3.dp.toPx()
        )

    }
}

private fun Long.formatTime(): String {
    var value = this
    val seconds = value % 60
    value /= 60
    val minutes = value % 60
    value /= 60
    val hours = value % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

suspend fun getUserInfo(): String {
    withContext(Dispatchers.IO) {
        delay(1000)
    }
    return "user info"
}

suspend fun getFriendList(): String {
    withContext(Dispatchers.IO) {
        delay(1000)
    }
    return "friend list{}"
}

suspend fun getFeedList(): String {
    withContext(Dispatchers.IO) {
        delay(1000)
    }
    return "feed list{}"
}