package com.project.cointerest.Fragment

import android.os.AsyncTask
import android.os.Bundle
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
import kotlinx.android.synthetic.main.coin_row_item.*
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class coinFragment() : Fragment() {

    lateinit var My_recyclerView: RecyclerView
    lateinit var emptyView: ConstraintLayout
    var selectedList = ArrayList<CoinInfo>()

    var runCheck =0 //같은 코인의 타이머 중복동작 방지
    var timerCheck = 0 // 타이머를 정지하기위한 변수

    override fun onCreateView(

            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        println("코인프래그먼트 체크")
        println(selectedList.size)

        var rootView = inflater.inflate(R.layout.fragment_coin, container, false)
        My_recyclerView = rootView.findViewById(R.id.coin_content_view!!) as RecyclerView
        emptyView = rootView.findViewById(R.id.coin_content_empty_view!!) as ConstraintLayout;
        My_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //selectedList.clear()
        //DataAdd()

        println("코인프래그먼트 체크2")
        println(selectedList.size)

        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)

    }

    override fun onResume() {
        super.onResume()
        timerCheck = 0
        selectedList.clear()
        if(runCheck == 0) {
            DataAdd()
        }
    }
    override fun onPause() {
        super.onPause()
        timerCheck = 1
        runCheck = 0
    }

    //ToDo 메인에서 시세 보여주는거는 타이머로 쓰고 목표가 도달해서 알람하는건 WorkManager 또는 서버에서 처리하도록 구현
    //Todo App쪽에 Firebase랑 호환되기 어렵다.

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


                                        selectedList.add(CoinInfo(arr[0], arr[1], arr[2], arr[3], ""))
                                        PriceSet(name_market[0], name_market[1], listCount) //같은 코인의 타이머 함수가 계속 누적됨 -> 렉발생(해결)
                                        listCount++
                                    }
                                    i++
                                }
                                addPrice.await()
                            }
                        }

                        activity?.runOnUiThread(Runnable {

                            My_recyclerView.adapter?.notifyDataSetChanged()
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
            /*println("가격정보2")
        println(priceStr)
        println("@@기격정보@@")*/
        }
    }
}