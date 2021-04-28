package com.project.cointerest

import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import kotlin.concurrent.timer

class CallPrice {

}
/*
    fun PriceSet(market:String, symbol : String, ListCount : Int) {

        timer(period = 500) { // 0.5초마다 시세 갱신
            val priceUrl = "https://api.upbit.com/v1/ticker?markets=${market}-${symbol}"
            val request = Request.Builder().url(priceUrl).build()
            val client = OkHttpClient()
            var priceStr: String = ""

            if(timerCheck == 1){
                cancel()
            }

            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val js = response?.body()?.string()
                        //println(js)
                        try {
                            val CInfo = JSONArray(js)
                            val jsonObject = CInfo.getJSONObject(0)
                            var price = jsonObject.getString("trade_price")

                            priceStr += "${price} ${market}"
                            println("가격정보")
                            println(priceStr)

                            activity?.runOnUiThread(Runnable {
                                selectedList[ListCount].price = priceStr
                                My_recyclerView.adapter?.notifyDataSetChanged()
                            })

                        } catch (e: JSONException) {
                            println("error")
                            println(e.printStackTrace())
                        }
                    } else {
                        println("Not Successful")
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    println("Request Fail")
                }
            })
            //*println("가격정보2")
        println(priceStr)
        println("@@기격정보@@")
        }
    }*/