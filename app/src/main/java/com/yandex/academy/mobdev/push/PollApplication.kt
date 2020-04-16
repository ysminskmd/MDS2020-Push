package com.yandex.academy.mobdev.push

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import androidx.core.content.ContextCompat
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.yandex.metrica.push.YandexMetricaPush

@Suppress("unused")
class PollApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = YandexMetricaConfig.newConfigBuilder("3ba189f5-93eb-49ef-9648-49fdd4365df5")
        YandexMetrica.activate(applicationContext, config.withMaxReportsInDatabaseCount(1).build())
        YandexMetrica.enableActivityAutoTracking(this)
        if (isInMainProcess()) YandexMetricaPush.init(applicationContext)
    }

    private fun isInMainProcess(): Boolean {
        val myPid = Process.myPid()
        val activityManager =
            ContextCompat.getSystemService(applicationContext, ActivityManager::class.java)
        return activityManager != null && activityManager.runningAppProcesses.any { process ->
            process.pid == myPid && process.processName == BuildConfig.APPLICATION_ID
        }
    }
}