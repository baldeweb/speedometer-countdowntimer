package com.wallace.speedometercountdown

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.wallace.speedometercountdown.TokenButtonConstants.ACCESS_FINISHED
import com.wallace.speedometercountdown.TokenButtonConstants.ACCESS_RUNNING
import com.wallace.speedometercountdown.TokenButtonConstants.PUSH_BUTTON
import com.wallace.speedometercountdown.TokenButtonConstants.READY_TO_RELEASE
import com.wallace.speedometercountdown.TokenButtonConstants.SPEEDOMETER
import com.wallace.speedometercountdown.TokenButtonConstants.TOKEN_FINISHED
import com.wallace.speedometercountdown.TokenButtonConstants.TOKEN_RUNNING
import com.wallace.speedometercountdown.databinding.ComponentTokenButtonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class TokenButton(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var _status = ""
    var status = ""
        get() = _status
        private set

    private var mCountDownTimer: CountDownTimer? = null

    private var binding = ComponentTokenButtonBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TokenButton)
        setStatus(attributes.getString(R.styleable.TokenButton_status) ?: "")
        setStyle(attributes.getString(R.styleable.TokenButton_tokenStyle) ?: "")
        setButtonName(
            attributes.getString(R.styleable.TokenButton_buttonName) ?: "Liberar Acesso"
        )

        binding.pbrCountdown.max = 75

        attributes.recycle()
    }

    private fun runProgress(
        minutesLimit: Long,
        minutesTick: Long
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val progress = ((minutesLimit - minutesTick).toDouble() / minutesLimit.toDouble())
            if (progress <= 0.56) {
                binding.pbrCountdown.progress = ceil(progress * 100).roundToInt()
                binding.imvPointer.rotation = ceil(progress * 480).toFloat()
            }
        }
    }

    private fun startCountdown(
        durationInMinutes: Long,
        intervalInSeconds: Long
    ) {
        val millisecondsLimit = TimeUnit.MINUTES.toMillis(durationInMinutes)

        mCountDownTimer = object : CountDownTimer(millisecondsLimit, intervalInSeconds) {
            override fun onTick(millisUntilFinished: Long) {
                runProgress(millisecondsLimit, millisUntilFinished)
            }

            override fun onFinish() {
                stopCountdownTimer()
            }
        }.start()
    }

    fun setButtonName(name: String) {
//        binding.btnComponentTokenbutton.text = name
    }

    fun setStyle(style: String) {
        when (style) {
            PUSH_BUTTON -> setPushButtonStyle()
            SPEEDOMETER -> setSpeedometerStyle()
            else -> setPushButtonStyle()
        }
    }

    private fun setPushButtonStyle() {

    }

    private fun setSpeedometerStyle() {

    }

    private fun setStatus(status: String) {
        when (status) {
            READY_TO_RELEASE -> {
                setPushButtonStyle()
            }
            TOKEN_RUNNING -> {
                setPushButtonStyle()
            }
            TOKEN_FINISHED -> {
                setPushButtonStyle()
            }
            ACCESS_RUNNING -> {
                setSpeedometerStyle()
            }
            ACCESS_FINISHED -> {
                setSpeedometerStyle()

            }
            else -> {
                setPushButtonStyle()
            }
        }
    }

    fun setTokenButtonStyle() {

    }

    fun setAccessTimerStyle() {

    }

    fun startCountdownTokenTimer(timeInMillis: Long) {
        setTokenButtonStyle()
        startCountdown(timeInMillis, 100)
    }

    fun startCountdownAccessTimer(timeInMillis: Long) {
        setAccessTimerStyle()
        startCountdown(timeInMillis, 1000)
    }

    fun stopCountdownTimer() {
        mCountDownTimer?.onFinish()
    }
}