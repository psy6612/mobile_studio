package com.project.cointerest

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.chart_view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.math.BigDecimal


class ChartView : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.project.cointerest.R.layout.chart_view)

        var coinPrice : String =""
        chart_webview.apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
        }
        var str = intent.getStringExtra("coin")
        println(str)
        chart_name.text = str

        val arr = str!!.split("-")
        callPrice(arr[1],arr[0])
        //arr[1]

        chart_webview.loadUrl("http://54.180.134.53/chart.php?coin=${arr[0]}${arr[1]}")

        back_btn.setOnClickListener {
            finishAndRemoveTask()
        }
        trade_btn.setOnClickListener {
            var isMarket :Boolean?

            isMarket = isInstalledApp("com.dunamu.exchange")
            println(isMarket)
            //"com.dunamu.exchange" 업비트 코리아
            //"com.dunamu.exchange.global" 업비트 그로벌

            if(isMarket == true){
                //openApp("com.dunamu.exchange.order?code=CRIX.UPBIT.KRW-BTC&exchangeCode=kr")
                openApp("com.dunamu.exchange")
            }
            else{
                market("com.dunamu.exchange")
            }
        }
        goal_btn.setOnClickListener {
            //Todo DB로 uuid랑 목표 가격, 기준가격, 심볼-마켓 보내기
            Toast.makeText(this, "Test",Toast.LENGTH_SHORT).show()
        }

        set_price_btn.setOnClickListener {
            callPrice(arr[1],arr[0])
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    // 앱이 설치 설치되었는지 판단하는 함수
    fun Context.isInstalledApp(packageName: String): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        return intent != null
    }
    // 특정 앱을 실행하는 함수
    fun Context.openApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }
    // 마켓으로 이동하는 함수
    fun Context.market(packageName: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=$packageName")
            startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            false
        }
    }


    fun callPrice(market:String, symbol:String) {


        println("데어터를 가져 오는 중...")
        //val url = "http://3.35.174.63/market.php"
        val url = "https://api.upbit.com/v1/ticker?markets=${market}-${symbol}"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        // var price :String="@"
        //coin_list_NULL.add(CoinData("", "", "", ""))

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val js = response?.body()?.string()
                println(js)
                //baseTextView.text = body
                try {
                    //println("체크11111")
                    val coinInfo = JSONArray(js)
                    val jsonObject = coinInfo?.getJSONObject(0)
                    var pcr = jsonObject.getString("trade_price")
                    pcr = BigDecimal(pcr).toPlainString()

                    runOnUiThread(Runnable {
                        set_price_btn.text = "현재 기준가 : ${pcr} ${market}"
                    })

                } catch (e: JSONException) {
                    println("error")
                    println(e.printStackTrace())
                } finally {
                    // 연결 해제
                    response.body()?.close()
                    client.connectionPool().evictAll()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Request Fail")
            }
        })

        return
    }
}