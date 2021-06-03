package com.project.cointerest

//import androidx.test.core.app.ApplicationProvider.getApplicationContext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.chart_view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.math.BigDecimal
import java.net.URL


class ChartView : AppCompatActivity() {

    var currentPrice : String = ""
    var sign : String = "+"
    var uuid : String = ""
    var str : String? = ""
    var selectedPrice : String = ""

    var tokenStr = App.prefs.getString("token", "nothing")

    lateinit var arr : List<String>
    lateinit var spinnerAdapter : ArrayAdapter<String>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.project.cointerest.R.layout.chart_view)

        var coinPrice : String =""
        uuid =Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        chart_view.apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
        }
        str = intent.getStringExtra("coin")
        println(str)
        chart_name.text = str

        arr = str!!.split("-")
        callPrice(arr[1], arr[0])
        //arr[1]

        chart_view.loadUrl("http://54.180.134.53/chart.php?coin=${arr[0]}${arr[1]}")




        val spinner: Spinner = findViewById(R.id.target_spinner)

        get_target_data()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            updatePriceRangeText(null)
        }


        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedPrice = parent?.getItemAtPosition(position) as String
                delete_text.isVisible = selectedPrice != "현재가"
                println(selectedPrice)
                val delim = arr[1] + " "

                if (selectedPrice.split(delim).size >= 2){
                    println(selectedPrice.split(delim))
                    if (selectedPrice.split(delim)[1].trim().startsWith('±')){
                        plus_minus_btn.performClick()
                    }
                    else if (selectedPrice.split(delim)[1].trim().startsWith('+')){
                        plus_btn.performClick()
                    }
                    else{
                        minus_btn.performClick()
                    }
                    currentPrice = selectedPrice.split(delim)[0].trim()
                    runOnUiThread(Runnable {
                        set_price_btn.text = "설정된 기준가 : ${currentPrice} ${arr[1]}"
                        Log.d("현재기준가", currentPrice)
                    })
                }


            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        input_goal.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatePriceRangeText(s)
            }
        })

        delete_text.setOnClickListener{
            delete_target_data()
            Toast.makeText(this, "목표가가 삭제되었습니다", Toast.LENGTH_SHORT).show()
        }

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
            plus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_900))
            minus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_500))
            plus_minus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_500))
            Toast.makeText(this, "${sign}", Toast.LENGTH_SHORT).show()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                updatePriceRangeText(input_goal.text.toString())
            }
        }

        minus_btn.setOnClickListener {
            sign = "-"
            plus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_500))
            minus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_900))
            plus_minus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_500))
            Toast.makeText(this, "${sign}", Toast.LENGTH_SHORT).show()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                updatePriceRangeText(input_goal.text.toString())
            }
        }
        plus_minus_btn.setOnClickListener {
            sign = "*"
            plus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_500))
            minus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_500))
            plus_minus_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_900))
            Toast.makeText(this, "±", Toast.LENGTH_SHORT).show()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                updatePriceRangeText(input_goal.text)
            }
        }

        goal_btn.setOnClickListener {
            //Todo DB로 uuid랑 목표 가격, 기준가격, 심볼-마켓 보내기
            if(input_goal.text.toString().isEmpty() ||input_goal.text.toString() == "." ){
                Toast.makeText(this, "퍼센테이지를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                send()
                Toast.makeText(this, "목표가 저장완료", Toast.LENGTH_SHORT).show()
            }
        }

        set_price_btn.setOnClickListener {
            callPrice(arr[1], arr[0])
            Toast.makeText(this, "기준가가 갱신되었습니다.", Toast.LENGTH_SHORT).show()

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
    @RequiresApi(Build.VERSION_CODES.N)
    fun updatePriceRangeText(s:CharSequence?){
        if (!s.isNullOrEmpty()) {
            val percentage: Double? = s.toString().toDoubleOrNull()
            val priceDouble: Double? = currentPrice.toDoubleOrNull()
            val nf: NumberFormat? = NumberFormat.getNumberInstance()
            if (nf != null) {
                nf.maximumFractionDigits = 3
            }

            when (sign) {
                "+" -> {
                    val priceRange: Double =
                        priceDouble?.times(((100.0 + percentage!!) / 100.0)) ?: 0.0
                    var printText =
                        "+" + s.toString() + "% " + (nf?.format(priceRange) ?: priceRange.toString()) + " " + arr[1]
                    runOnUiThread(Runnable {
                        target_price_text.text = printText
                    })
                }
                "-" -> {
                    val priceRange: Double =
                        priceDouble?.times(((100.0 - percentage!!) / 100.0)) ?: 0.0
                    var printText =
                        "-" + s.toString() + "% " + (nf?.format(priceRange) ?: priceRange.toString()) + " " + arr[1]
                    runOnUiThread(Runnable {
                        target_price_text.text = printText
                    })
                }
                "*" -> {
                    val plusPriceRange: Double =
                        priceDouble?.times(((100.0 + percentage!!) / 100.0)) ?: 0.0
                    val minusPriceRange: Double =
                        priceDouble?.times(((100.0 - percentage!!) / 100.0)) ?: 0.0
                    var printText =
                        "+" + s.toString() + "% " + (nf?.format(plusPriceRange) ?: plusPriceRange.toString()) + " " + arr[1]
                    printText += "\n-" + s.toString() + "% " + (nf?.format(minusPriceRange) ?: minusPriceRange.toString()) + " " + arr[1]
                    runOnUiThread(Runnable {
                        target_price_text.text = printText
                    })
                }
            }
        }
        else{
            runOnUiThread(Runnable {
                target_price_text.text = ""
            })
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
                        Log.d("현재기준가", currentPrice)
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

    fun send(){

        // URL을 만들어 주고
        val url = URL("http://54.180.134.53/data_send.php")
        //데이터를 담아 보낼 바디를 만든다
        val requestBody : RequestBody = FormBody.Builder()
                .add("uuid", uuid)
                .add("price_range", "${sign}${input_goal.text}")
                .add("market", arr[1])
                .add("symbol", arr[0])
                .add("coin", "${arr[1]}${arr[0]}")
                .add("current_price", currentPrice)
                .add("token", tokenStr)
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
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "요청 완료")
                // 스피너 업데이트
                get_target_data()
                input_goal.text = null
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", "요청 실패 ")
            }

        })
    }

    fun get_target_data(){
        val spinner: Spinner = findViewById(R.id.target_spinner)
        spinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            android.R.id.text1
        )
        // URL을 만들어 주고
        val url = URL("http://54.180.134.53/get_target_data.php")
        //데이터를 담아 보낼 바디를 만든다
        val requestBody : RequestBody = FormBody.Builder()
                .add("uuid", uuid)
                .add("coin", "${arr[1]}${arr[0]}")
                .build()
        // OkHttp Request 를 만들어준다.
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        // 클라이언트 생성
        val client = OkHttpClient()
        Log.d("전송 주소 ", "http://54.180.134.53/get_target_data.php")
        // 요청 전송
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                runOnUiThread(Runnable {
                    spinner.adapter = spinnerAdapter
                    spinnerAdapter.add("현재가")
                })
                if (response.isSuccessful) {
                    try {
                        val js = response?.body()?.string()
                        println(js)
                        val targetInfo = JSONArray(js)
                        var i = 0
                        while (i < targetInfo.length()) {
                            val jsonObject = targetInfo.getJSONObject(i)
                            val current_price = jsonObject.getString("current_price")
                            val price_range = jsonObject.getString("price_range").replace('*', '±')
                            val spinnerItem = current_price + " " + arr[1] + " " + price_range + "%"
                            runOnUiThread(Runnable {
                                spinnerAdapter.add(spinnerItem)
                            })

                            println(spinnerItem)

                            i++
                        }
                        runOnUiThread(Runnable {
                            spinnerAdapter.notifyDataSetChanged()
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
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", "요청 실패 ")
            }
        })
    }

    fun delete_target_data(){
        // URL을 만들어 주고
        val url = URL("http://54.180.134.53/delete_target_data.php")
        //데이터를 담아 보낼 바디를 만든다
        val requestBody : RequestBody = FormBody.Builder()
                .add("uuid", uuid)
                .add("price_range", selectedPrice.replace('±', '*').split(" ")[2].dropLast(1))
                .add("market", arr[1])
                .add("coin", "${arr[1]}${arr[0]}")
                .add("current_price", selectedPrice.split(" ")[0])
                .build()

        // OkHttp Request 를 만들어준다.
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

        // 클라이언트 생성
        val client = OkHttpClient()

        Log.d("전송 주소 ", "http://54.180.134.53/delete_target_data.php")

        // 요청 전송
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val js = response?.body()?.string()
                    println(js)

                    Log.d("요청", "요청 완료")
                    input_goal.text = null
                    get_target_data()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", "요청 실패 ")
            }

        })
    }
}




