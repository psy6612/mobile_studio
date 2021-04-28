package com.project.cointerest

import android.content.Context
import android.content.SharedPreferences


class SharedPreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }
    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    //초기화
    fun deleteAll(){
        prefs.edit().clear().apply()
        println("SharedPreference 삭제완료")
        //prefs.edit().commit()
    }

    //선택 삭제
    fun selectedDelete(symbol:String, market:String){
        prefs.edit().remove("${symbol}-${market}").apply()
        println("${symbol}-${market} 삭제완료")
    }
}

