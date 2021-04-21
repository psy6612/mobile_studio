package com.project.cointerest.Fragment/*
package com.project.cointerest.Fragment

import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

interface CoinList {

    fun JsonMake(check:Int){

        println("데어터를 가져 오는 중...")
        //val url = "http://3.35.174.63/market.php"
        var url = "https://api.upbit.com/v1/market/all"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val js = response?.body()?.string()
                println(js)
                //baseTextView.text = body
                try {
                    println("체크11111!")
                    val coinInfo = JSONArray(js)
                    println("체크22222!")
                    //val jsonArray = coinInfo.optJSONArray("trade_price")
                    println("체크메이드1")
                    print(coinInfo[0])
                    println("체크메이드2")
                    ///담아야할거///
                    //한글이름, 영문이름, 심볼, 마켓


                    var i = 0
                    while(i< coinInfo.length()){
                        val jsonObject = coinInfo.getJSONObject(i)
                        val market_name = jsonObject.getString("market")
                        val korean_name = jsonObject.getString("korean_name")
                        //val english_name = jsonObject.getString("english_name")
                        val arr = market_name.split("-")

                        //check == 0(KWR마켓 선택)
                        if(arr[0] == "BTC"){
                            println(market_name)
                        }
                        //check == 1(BTC마켓 선택)
                        else if(arr[0] == "KRW"){

                        }
                        //전체 둘다 선택
                        else{

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
        return
    }
}
*/
