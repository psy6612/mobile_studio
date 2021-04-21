package com.project.cointerest.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.Adapter.CoinContentAdapter
import com.project.cointerest.App
import com.project.cointerest.CoinData
import com.project.cointerest.MainActivity
import com.project.cointerest.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException


class coinFragment() : Fragment() {

    lateinit var My_recyclerView: RecyclerView
    lateinit var emptyView: ConstraintLayout
    var selectedList = ArrayList<CoinData>()

    override fun onCreateView(


            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

        //selectedList.clear()
        //DataAdd()

        //selectedList.add(CoinData("비트코인","BTC","BTC","KRW"))
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
        var str = App.prefs.getString("BTC-KRW", "nothing")

        DataAdd()
        println("코인프래그먼트 체크2")
        println(selectedList.size)

        //arguments?.let{selectedList}
        //selectedList.add(CoinData("비트코인","BTC","BTC","KRW"))

        //  var rootView = inflater.inflate(R.layout.fragment_coin, container, false)
        //   My_recyclerView = rootView.findViewById(R.id.coin_content_view!!) as RecyclerView

        // My_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList)
        println("@@@@")
        // My_recyclerView.adapter = SearchFragmentRecyclerAdapter(requireContext(), coin_list_KRW ,selectedList)

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

                    while (i < coinInfo.length()) {
                        val jsonObject = coinInfo.getJSONObject(i)
                        val market_name = jsonObject.getString("market")
                        val korean_name = jsonObject.getString("korean_name")
                        //val symbol = jsonObject.getString("symbol")
                        //val english_name = jsonObject.getString("english_name")
                        val name_market = market_name.split("-")

                        var str = App.prefs.getString("${name_market[1]}-${name_market[0]}", "nothing")
                        //println(str)
                        if (str != "nothing") {
                            val arr = str.split("-")

                            //println(str)
                            //println(arr[3])
                            selectedList.add(CoinData(arr[0], arr[1], arr[2], arr[3]))

                            //println(selectedList)
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

}


