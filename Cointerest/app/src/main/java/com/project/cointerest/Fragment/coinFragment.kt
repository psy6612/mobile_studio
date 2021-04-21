package com.project.cointerest.Fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieSyncManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.project.cointerest.Adapter.CoinContentAdapter
import com.project.cointerest.Adapter.SearchFragmentRecyclerAdapter
import com.project.cointerest.CoinData
import com.project.cointerest.R
import okhttp3.*
import java.io.IOException
import java.net.URL


class coinFragment() : Fragment() {

    lateinit var My_recyclerView: RecyclerView
    var selectedList = ArrayList<CoinData>()
    var title = ArrayList<String?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        println("코인프래그먼트 체크")
        // Inflate the layout for this fragment



        //arguments?.let{title = it.getStringArrayList("data")}
        selectedList.add(CoinData("비트코인","BTC","BTC","KRW"))

        var rootView = inflater.inflate(R.layout.fragment_coin, container, false)
        My_recyclerView = rootView.findViewById(R.id.coin_content_view!!) as RecyclerView
        My_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList )

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("코인프래그먼트 체크2")

        arguments?.let{selectedList}
        //selectedList.add(com.project.cointerest.CoinData("비트코인","BTC","BTC","KRW"))

      //  var rootView = inflater.inflate(R.layout.fragment_coin, container, false)
     //   My_recyclerView = rootView.findViewById(R.id.coin_content_view!!) as RecyclerView

       // My_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList )
       // My_recyclerView.adapter = SearchFragmentRecyclerAdapter(requireContext(), coin_list_KRW ,selectedList)

    }


}

/*
fun fetchJson(){ //파라미트는 필요 없음
    //앞에서 만들었던 php 웹페이지 주소
    val url = URL("http://3.35.174.63/insert.php")
    val request = Request.Builder().url(url).build()
    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println("Success to execute request! : $body")

            //Gson으로 파싱
            val gson = GsonBuilder().create()
            val list = gson.fromJson(body, JsonObj::class.java)

            //println(list.result[0].name)
            //여기서 나온 결과를 어답터로 연결
            runOnUiThread {
                recyclerView.adapter = MainAdapter(list)
            }
        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request!")
        }
    })

}*/
