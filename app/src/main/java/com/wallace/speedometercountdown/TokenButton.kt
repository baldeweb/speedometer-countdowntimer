package com.wallace.speedometercountdown

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
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
        runProgress()

        attributes.recycle()
    }

    private fun runProgress() {
        CoroutineScope(Dispatchers.Main).launch {
            var progress = 0
            var pointerRotation = 0F
            while (progress <= 55) {
                delay(50)
                binding.pbrCountdown.progress = progress
                binding.imvPointer.rotation = pointerRotation

                progress += 1
                pointerRotation += 4.8F
            }
            Toast.makeText(context, "Finalizou", Toast.LENGTH_LONG).show()
        }
    }

    private fun startCountdown(
        durationInMinutes: Long,
        intervalInSeconds: Long,
        onFinish: () -> Unit
    ) {
        mCountDownTimer = object : CountDownTimer(durationInMinutes, intervalInSeconds) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                onFinish.invoke()
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
        startCountdown(timeInMillis, 100) {

        }
    }

    fun startCountdownAccessTimer(timeInMillis: Long) {
        setAccessTimerStyle()
        startCountdown(timeInMillis, 100) {

        }
    }

    fun stopCountdownTimer() {
        mCountDownTimer?.onFinish()
    }
}