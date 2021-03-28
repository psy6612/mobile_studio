package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        my_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//        my_recycler_view.setHasFixedSize(true)

        println("체크00000")
        JsonMake()
    }

    fun JsonMake(){
        println("데어터를 가져 오는 중...")
        val url = "http://3.35.174.63/market.php"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val js = response?.body()?.string()
                println(js)
                //baseTextView.text = body
                try {
                    println("체크11111")
                    val coinInfo = JSONArray(js)
                    println("체크22222")
                    //val jsonArray = coinInfo.optJSONArray("trade_price")

                    var i = 0;
                    var tempStr = ""
                    while(i< coinInfo.length()){
                        val jsonObject = coinInfo.getJSONObject(i)
                        val market_name = jsonObject.getString("market")
                        val arr = market_name.split("-")
                        if(arr[0] == "BTC"){
                            println(market_name)
                        }
                        i++
                    }


                } catch (e: JSONException) {
                    println("error")
                    println(e.printStackTrace())
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Request Fail")
            }
        })

    }
}