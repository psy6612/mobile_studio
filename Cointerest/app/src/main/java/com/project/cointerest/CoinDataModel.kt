package com.project.cointerest

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.math.BigDecimal

class CoinDataModel : ViewModel(){

    fun newPriceSet(selectedList:ArrayList<CoinInfo>, itemPositionList : MutableSet<Int>){
        println("newPriceSet")
        for((listCount, item) in selectedList.withIndex()) {
            Log.d("카운터","${listCount} - ${selectedList[listCount].symbol}")

            val priceUrl = "https://api.upbit.com/v1/ticker?markets=${item.market}-${item.symbol}"
            val request = Request.Builder().url(priceUrl).build()
            val client = OkHttpClient()
            var priceStr: String = ""


            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val js = response?.body()?.string()
                        //println(js)
                        try {
                            val CInfo = JSONArray(js)
                            val jsonObject = CInfo.getJSONObject(0)
                            var price = jsonObject.getString("trade_price")
                            var newPrice = BigDecimal(price).toPlainString()



                            priceStr += "${newPrice} ${item.market}"
                            Log.d("수신가격", "${item.symbol} ${priceStr}")

                            if(priceStr != selectedList[listCount].price){

                                itemPositionList.add(listCount)

                                Log.d("가격변동체크","${item.symbol} ${priceStr}")
                                Log.d("itemPositionList","${itemPositionList.size}")

                            }

                            selectedList[listCount].price = priceStr

                        } catch (e: JSONException) {
                            println("error")
                            println(e.printStackTrace())
                        } finally {
                            // 연결 해제
                            response.body()?.close()
                            client.connectionPool().evictAll()
                        }
                    } else {
                        println("Not Successful")
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    println("Request Fail")
                }
            })
        }
    }



}