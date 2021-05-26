package com.project.cointerest

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.util.*


class DataSend(uuid: String, price_range: String, mycoin:String,current_price:String, listener: Response.Listener<String?>?) :
    StringRequest(Method.POST, URL, listener, null) {
    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return map
    }

    companion object {
        //서버 URL 설정(php 파일 연동)
        private const val URL = "http://54.180.134.53/data_send.php"
    }

    init {
        map = HashMap()
        map["uuid"] = uuid
        map["price_range"] = price_range
        map["my_coin"] = mycoin
        map["current_price"] = current_price
    }
}