package com.fancylight.helpdesk.SharedPref

import android.app.Application

class MyApplication : Application() {
    companion object
    {
        lateinit var prefs : PreferneceUtil
    }

    override fun onCreate() {
        prefs = PreferneceUtil(applicationContext)
        super.onCreate()
    }
}