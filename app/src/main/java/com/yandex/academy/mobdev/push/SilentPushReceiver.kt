package com.yandex.academy.mobdev.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.yandex.metrica.push.YandexMetricaPush

class SilentPushReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD)
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        prefs.edit().putString(keyGreeting, payload).apply()
        println("zayankov: SilentPushReceiver.onReceive: greeting updated")
    }
}