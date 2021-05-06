package com.project.cointerest.Fragment

import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.*
import com.project.cointerest.Adapter.CoinContentAdapter
import com.scichart.core.utility.Dispatcher.runOnUiThread
import kotlinx.android.synthetic.main.coin_row_item.*
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Runnable
import java.math.BigDecimal
import java.net.URL
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class coinFragment() : Fragment() {

    lateinit var My_recyclerView: RecyclerView
    //lateinit var Price_recyclerView : RecyclerView
    lateinit var emptyView: ConstraintLayout
    var selectedList = ArrayList<CoinInfo>()
    var itemPositionList = mutableListOf<Int>() // 바뀐 가격 저장 리스트

    var runCheck =0 //같은 코인의 타이머 중복동작 방지
    var timerCheck = 0 // 타이머를 정지하기위한 변수

    private var isRunning=true
    val thread=ThreadClass()


    override fun onDestroyView() {
        super.onDestroyView()
        isRunning=false
    }

    override fun onCreateView(

            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        thread.start()
        DataAdd()

        Log.d("싸이즈","${selectedList.size}")

        println("코인프래그먼트 체크")


        var rootView = inflater.inflate(R.layout.fragment_coin, container, false)
        My_recyclerView = rootView.findViewById(R.id.coin_content_view!!) as RecyclerView
        emptyView = rootView.findViewById(R.id.coin_content_empty_view!!) as ConstraintLayout;
        My_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)


        return rootView
    }


    inner class ThreadClass:Thread(){
        override fun run(){
            while(isRunning){
                newPriceSet()
                if (itemPositionList.isNotEmpty()){
                    for (changePosition in itemPositionList) {
                        runOnUiThread(UIClass(changePosition))
                    }
                    itemPositionList = mutableListOf<Int>()
                }
                Log.d("once", "체크체크!") //Todo 2초마다 한바퀴만 휴대폰에서만 돌아야하는데 4-5바퀴 돔, 에뮬레이터에서는 정상
                SystemClock.sleep(2000)
            }
        }
    }

    inner class UIClass(val changePosition:Int) :Runnable {
        override fun run(){
            Log.d("가격변동체크", "${changePosition}번째 변동됨")
            My_recyclerView.adapter?.notifyItemChanged(changePosition, "Price")
            //My_recyclerView.adapter?.notifyItemChanged(0,"@@@") // TODO Payload 추가하면 텍스트부분만 새로고침 할 수 있음, 어댑터에서도 Payload 받아야함
         }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("destroy","체크")
        isRunning=false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //println("코인프래그먼트 체크2")
        //My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)
    }

    override fun onResume() {
        super.onResume()
        //timerCheck = 0
        isRunning = true
       // selectedList.clear()
    }
    override fun onPause() {
        super.onPause()
        timerCheck = 1
        runCheck = 0

        isRunning = false
    }

    //ToDo 메인에서 시세 보여주는거는 타이머로 쓰고 목표가 도달해서 알람하는건 WorkManager 또는 서버에서 처리하도록 구현
    //Todo App쪽에 Firebase랑 호환되기 어렵다.
    //Todo RelationLayout형식으로 바꾸기
    //Todo 업비트 차트 구현
    //Todo 목표가 설정
    
    fun DataAdd() {
                // selectedList.clear()
        println("데어터를 가져 오는 중...")
        val url = "https://api.upbit.com/v1/market/all"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val js = response?.body()?.string()
                    //println(js)
                    try {

                        val coinInfo = JSONArray(js)
                        var i = 0
                        var listCount = 0
                        while (i < coinInfo.length()) {
                            runBlocking {
                                val addPrice = async {
                                    val jsonObject = coinInfo.getJSONObject(i)
                                    val market_name = jsonObject.getString("market")
                                    val name_market = market_name.split("-")

                                    var str = App.prefs.getString("${name_market[1]}-${name_market[0]}", "nothing") // key = (마켓-심볼)
                                    //println(str)
                                    if (str != "nothing") {
                                        val arr = str.split("-")

                                        var idx = 0
                                        var duplicateCheck = false
                                        for(item in selectedList){
                                            if(item.symbol == arr[1] && item.market == arr[0]){
                                                duplicateCheck = true
                                                break
                                            }
                                            idx++
                                        }
                                        if(duplicateCheck == false){
                                            selectedList.add(CoinInfo(arr[0], arr[1], arr[2], arr[3], ""))
                                        }
                                        println("${arr[0]} ${arr[1]}")
                                        listCount++
                                    }
                                    i++
                                }
                                addPrice.await()
                            }
                        }

                        activity?.runOnUiThread(Runnable {

                            My_recyclerView.adapter?.notifyDataSetChanged() //Todo

                            if (selectedList.isEmpty()) {
                                My_recyclerView.visibility = View.GONE
                                emptyView.visibility = View.VISIBLE
                            } else {
                                My_recyclerView.visibility = View.VISIBLE
                                emptyView.visibility = View.GONE
                            }
                        })

                        //}
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
        runCheck = 1
        return
    }


    fun newPriceSet(){
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
                            Log.d("가격정보", priceStr)

                            if(priceStr != selectedList[listCount].price){
                                itemPositionList.add(listCount)
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