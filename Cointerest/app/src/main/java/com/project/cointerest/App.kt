package com.project.cointerest

import android.app.Application


class App : Application() {

    companion object {
        lateinit var prefs : SharedPreference
    }
    /* prefs라는 이름의 MySharedPreferences 하나만 생성할 수 있도록 설정. */

    override fun onCreate() {
        prefs = SharedPreference(applicationContext)
        super.onCreate()
    }
}
