package com.yandex.academy.mobdev.push

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yandex.metrica.push.YandexMetricaPush
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        next.setOnClickListener {
            startActivity(Intent(this, PollActivity::class.java))
            finish()
        }
        next.animate().alpha(1f).start()

        intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD)?.let { url ->
            val options = DrawableTransitionOptions().crossFade()
            Glide.with(this).load(url).transition(options).into(image)
        }
    }
}