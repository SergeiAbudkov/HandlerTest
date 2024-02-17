package com.example.handlertest

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.handlertest.databinding.ActivityHandlerBinding
import kotlin.random.Random

class HandlerLevel1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityHandlerBinding

    private val handler = Handler(Looper.getMainLooper()){
        return@Handler false
    }

    private val token = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHandlerBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.root.forEach {
            if (it is Button) it.setOnClickListener(uniqueListener)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val uniqueListener = View.OnClickListener {
        Thread {
        when (it.id) {
            R.id.enableDisableButton -> {
                handler.post { switchingButtonState() }
            }

            R.id.randomColorButton -> {
                handler.post { getRandomColor() }
            }

            R.id.enableDisableDelayedButton -> {
                handler.postDelayed({ switchingButtonState() }, DELAY_KEY)
            }

            R.id.randomColorDelayedButton -> {
                handler.postDelayed({ getRandomColor() }, DELAY_KEY)
            }

            R.id.randomColorTokenDelayedButton -> {
                handler.postDelayed({getRandomColor()}, token, DELAY_KEY)
            }

            R.id.showToastButton -> {
                handler.postDelayed({showToast()},token, DELAY_KEY)
            }

            R.id.cancelButton -> {
            handler.removeCallbacksAndMessages(token)
            }

        }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun getRandomColor() {
        val color = Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
        binding.colorView.setBackgroundColor(color)
    }

    private fun switchingButtonState() {
        binding.testButton.isEnabled = !binding.testButton.isEnabled
    }

    private fun showToast() {
        Toast.makeText(this, R.string.hello, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        private val DELAY_KEY = 2000L
    }

}