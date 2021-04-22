package com.project.cointerest.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.*
import com.project.cointerest.Adapter.CoinContentAdapter
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.DateFormatSymbols


class coinFragment() : Fragment() {

    lateinit var My_recyclerView: RecyclerView
    lateinit var emptyView: ConstraintLayout
    var selectedList = ArrayList<CoinInfo>()

    override fun onCreateView(


            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

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

        selectedList.clear()

        DataAdd()
        println("코인프래그먼트 체크2")
        println(selectedList.size)

        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)

    }

    override fun onPause() {
        super.onPause()
    }


    fun DataAdd() {
        println("데어터를 가져 오는 중...")
        val url = "https://api.upbit.com/v1/market/all"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val js = response?.body()?.string()
                //println(js)
                try {
                    val coinInfo = JSONArray(js)
                    var i = 0
                    var ListCount = 0
                    while (i < coinInfo.length()) {
                        val jsonObject = coinInfo.getJSONObject(i)
                        val market_name = jsonObject.getString("market")
                        val name_market = market_name.split("-")

                        var str = App.prefs.getString("${name_market[1]}-${name_market[0]}", "nothing")
                        //println(str)
                        if (str != "nothing") {
                            val arr = str.split("-")
                            selectedList.add(CoinInfo(arr[0], arr[1], arr[2], arr[3], ""))
                            PriceSet(name_market[0], name_market[1],ListCount)
                            ListCount++
                        }
                        i++
                    }
                    activity?.runOnUiThread(Runnable{
                        My_recyclerView.adapter?.notifyDataSetChanged()
                        if (selectedList.isEmpty()) {
                            My_recyclerView.visibility = View.GONE
                            emptyView.visibility = View.VISIBLE
                        } else {
                            My_recyclerView.visibility = View.VISIBLE
                            emptyView.visibility = View.GONE
                        }
                    })

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


    fun PriceSet(market:String, symbol : String, ListCount : Int){


        val priceUrl = "https://api.upbit.com/v1/ticker?markets=${market}-${symbol}"
        val request = Request.Builder().url(priceUrl).build()
        val client = OkHttpClient()
        var priceStr : String =""

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val js = response?.body()?.string()
                println(js)
                try {
                    val CInfo = JSONArray(js)
                    val jsonObject = CInfo.getJSONObject(0)
                    var price = jsonObject.getString("trade_price")

                    priceStr += "${price} ${market}"
                    println("가격정보")
                    println(priceStr)
                    selectedList[ListCount].price = priceStr

                } catch (e: JSONException) {
                    println("error")
                    println(e.printStackTrace())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Request Fail")
            }
        })
        println("가격정보2")
        println(priceStr)
        println("@@기격정보@@")

    }





}
