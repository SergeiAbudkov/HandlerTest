package com.example.handlertest

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.handlertest.databinding.ActivityHandlerBinding
import kotlin.random.Random

class HandlerLevel2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityHandlerBinding

    private val handler = Handler(Looper.getMainLooper()) {
        when (it.what) {
            MSG_TOGGLE_BUTTON -> toggleTestButtonState()
            MSG_NEXT_RANDOM_COLOR -> randomColor()
            MSG_SHOW_TOAST -> showToast()
        }
        return@Handler true
    }

    private val token = Any()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHandlerBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.root.forEach {
            if (it is Button) it.setOnClickListener(universalListenerButton)
        }

    }

    private val universalListenerButton = View.OnClickListener {
        Thread {
            when (it.id) {
                R.id.enableDisableButton -> {
                    val message = handler.obtainMessage(MSG_TOGGLE_BUTTON)
                    handler.sendMessage(message)
                }

                R.id.randomColorButton -> {
                    val message = Message()
                    message.what = MSG_NEXT_RANDOM_COLOR
                    handler.sendMessage(message)
                }

                R.id.enableDisableDelayedButton -> {
                    val message = Message.obtain(handler, MSG_TOGGLE_BUTTON)
                    handler.sendMessageDelayed(message, DELAY)
                }

                R.id.randomColorDelayedButton -> {
                    val message = Message.obtain(handler) {
                        randomColor()
                    }
                    handler.sendMessageDelayed(message, DELAY)
                }

                R.id.randomColorTokenDelayedButton -> {
                    val message = handler.obtainMessage(MSG_NEXT_RANDOM_COLOR)
                    message.obj = token
                    handler.sendMessageDelayed(message, DELAY)
                }
                R.id.cancelButton -> handler.removeCallbacksAndMessages(token)
            }
        }.start()
    }

    private fun showToast() {
        Toast.makeText(this, R.string.hello, Toast.LENGTH_SHORT).show()
    }

    private fun toggleTestButtonState() {
        binding.testButton.isEnabled = !binding.testButton.isEnabled
    }

    private fun randomColor() {
        val color = Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
        binding.colorView.setBackgroundColor(color)
    }

    companion object {
        @JvmStatic
        private val DELAY = 2000L

        @JvmStatic
        private val TAG = HandlerLevel2Activity::class.java.simpleName

        private const val MSG_TOGGLE_BUTTON = 1
        private const val MSG_NEXT_RANDOM_COLOR = 2
        private const val MSG_SHOW_TOAST = 3

    }


}