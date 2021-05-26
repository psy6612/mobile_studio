package com.project.cointerest.Fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
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
//import sun.rmi.runtime.Log
import java.io.File
import java.io.IOException
import java.lang.Runnable
import java.math.BigDecimal
import java.net.URL
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


//Todo isRunning말고 LifecycleOwner로

//Todo 그리고 newPriceSet, DataAdd이거는 모델 따로 만들기

//Todo 시세 알람용 수신 코드는 서버 따로 만들어서 거기서 구현(웹소켓)

class coinFragment() : Fragment() {
    var itemPositionList = mutableSetOf<Int>() // 바뀐 가격 저장 SET
    lateinit var my_recyclerView: RecyclerView
    //lateinit var Price_recyclerView : RecyclerView
    lateinit var emptyView: ConstraintLayout
    var selectedList = ArrayList<CoinInfo>()

    var runCheck =0 //같은 코인의 타이머 중복동작 방지
    var timerCheck = 0 // 타이머를 정지하기위한 변수

    private var isRunning=true

    val thread by lazy {ThreadClass()}

    private lateinit var coinDataModel : CoinDataModel


    override fun onDestroyView() {
        super.onDestroyView()
        isRunning=false
    }

    override fun onCreateView(

            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        coinDataModel =CoinDataModel()

/*        val myUuid :String = ""

        myUUID = GetDevicesUUID(getBaseContext())

        fun GetUUID(context : Context){
            var tManager : TelephonyManager

        }

        String useruuid = null;
        useruuid = GetDevicesUUID(getBaseContext());

        private String GetDevicesUUID(Context mContext) {
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tManager.getDeviceId();
            return deviceId;
        }*/



        DataAdd()
        coinDataModel.newPriceSet(selectedList, itemPositionList)
        Log.d("싸이즈","${selectedList.size}")

        println("코인프래그먼트 체크")
        var rootView = inflater.inflate(R.layout.fragment_coin, container, false)
        my_recyclerView = rootView.findViewById(R.id.coin_content_view!!) as RecyclerView
        emptyView = rootView.findViewById(R.id.coin_content_empty_view!!) as ConstraintLayout;
        my_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        my_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)
        thread.start()
        return rootView
    }


    inner class ThreadClass:Thread(){
        override fun run(){
            while(isRunning){
                try {

                    coinDataModel.newPriceSet(selectedList, itemPositionList)
                    if (itemPositionList.isNotEmpty()) {
                        Log.d("itemPositionList2", "${itemPositionList.size}")
                        runOnUiThread {
                            for (changePosition in itemPositionList) {
                                Log.d("가격변동체크", "${changePosition}번째 변동됨")

                                my_recyclerView.adapter?.notifyItemChanged(changePosition, "Price")
                                //My_recyclerView.adapter?.notifyItemChanged(0,"@@@") // TODO Payload 추가하면 텍스트부분만 새로고침 할 수 있음, 어댑터에서도 Payload 받아야함
                            }
                            itemPositionList = mutableSetOf<Int>()
//                        my_recyclerView.adapter?.notifyDataSetChanged()
                        }

                    } else {
                        Log.d("emptyItem", "비었음")
                    }
                    SystemClock.sleep(2000)
                }
                catch(e : Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning=false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isRunning = true
        //println("코인프래그먼트 체크2")
        //My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        Log.d("Resume체크","${isRunning}")
        //timerCheck = 0
       // selectedList.clear()
    }
    override fun onPause() {
        super.onPause()
        timerCheck = 1
        runCheck = 0

        //isRunning = false
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

                            my_recyclerView.adapter?.notifyDataSetChanged() //Todo

                            if (selectedList.isEmpty()) {
                                my_recyclerView.visibility = View.GONE
                                emptyView.visibility = View.VISIBLE
                            } else {
                                my_recyclerView.visibility = View.VISIBLE
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
}