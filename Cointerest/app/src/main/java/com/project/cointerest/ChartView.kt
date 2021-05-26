package com.project.cointerest

//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.chart_view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.net.URL



class ChartView : AppCompatActivity() {

    var currentPrice : String = ""
    var sign : String = "+"
    var uuid : String = ""
    var str : String? = ""
    lateinit var arr : List<String>

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.project.cointerest.R.layout.chart_view)

        var coinPrice : String =""

        chart_webview.apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
        }
        str = intent.getStringExtra("coin")
        println(str)
        chart_name.text = str

        arr = str!!.split("-")
        callPrice(arr[1], arr[0])
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

        plus_btn.setOnClickListener {
            sign = "+"
            Toast.makeText(this, "${sign}", Toast.LENGTH_SHORT).show()
        }

        minus_btn.setOnClickListener {
            sign = "-"
            Toast.makeText(this, "${sign}", Toast.LENGTH_SHORT).show()
        }
        plus_minus_btn.setOnClickListener {
            sign = "±"
            Toast.makeText(this, "${sign}", Toast.LENGTH_SHORT).show()
        }

        goal_btn.setOnClickListener {
            //Todo DB로 uuid랑 목표 가격, 기준가격, 심볼-마켓 보내기
            uuid =Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

            if(input_goal.text.toString().isEmpty()){
                Toast.makeText(this, "퍼센테이지를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                send()
                Toast.makeText(this, "목표가 저장완료", Toast.LENGTH_SHORT).show()

            }
        }

        set_price_btn.setOnClickListener {
            callPrice(arr[1], arr[0])

            Toast.makeText(this, "${input_goal.text}", Toast.LENGTH_SHORT).show()

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


    fun callPrice(market: String, symbol: String) {


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
                    currentPrice = pcr
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

//    fun test() {
//        fun onResponse(response: String?) {
//            try {
//                val jsonObject = JSONObject(response)
//                val success = jsonObject.getBoolean("success")
//                if (success) { //로그인 성공시
//                    val UserEmail = jsonObject.getString("UserEmail")
//                    val UserPwd = jsonObject.getString("UserPwd")
//                    val UserName = jsonObject.getString("UserName")
//
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    intent.putExtra("UserEmail", UserEmail)
//                    intent.putExtra("UserPwd", UserPwd)
//                    intent.putExtra("UserName", UserName)
//                    ContextCompat.startActivity(intent)
//                } else { //로그인 실패시
//                    Toast.makeText(ApplicationProvider.getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
//                    return
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        }
//    }

    fun send(){

        // URL을 만들어 주고
        val url = URL("http://54.180.134.53/data_send.php")

        //데이터를 담아 보낼 바디를 만든다
        val requestBody : RequestBody = FormBody.Builder()
                .add("uuid",uuid)
                .add("price_range", "${sign}${input_goal.text}")
                .add("market",arr[1])
                .add("symbol",arr[0])
                .add("current_price", currentPrice)
                .build()

        // OkHttp Request 를 만들어준다.
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

        // 클라이언트 생성
        val client = OkHttpClient()

        Log.d("전송 주소 ", "http://54.180.134.53/data_send.php")

        // 요청 전송
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청","요청 완료")
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청","요청 실패 ")
            }

        })
    }


}




