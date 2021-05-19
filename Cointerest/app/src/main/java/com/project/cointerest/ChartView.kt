package com.project.cointerest

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.chart_view.*


class ChartView : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.project.cointerest.R.layout.chart_view)

        chart_webview.apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
        }
        var str = intent.getStringExtra("coin")
        println(str)
        chart_name.text = str
        chart_webview.loadUrl("http://54.180.134.53/chart.php?coin=${str}")

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
                openApp("com.dunamu.exchange.order?code=CRIX.UPBIT.KRW-BTC&exchangeCode=kr")
            }
            else{
                market("com.dunamu.exchange")
            }
        }

        goal_btn.setOnClickListener {
            //Todo DB에 목표가정보 전송

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
}