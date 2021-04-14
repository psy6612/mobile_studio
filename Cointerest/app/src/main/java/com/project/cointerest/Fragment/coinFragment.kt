package com.project.cointerest.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.Adapter.CoinContentAdapter
import com.project.cointerest.Adapter.SearchFragmentRecyclerAdapter
import com.project.cointerest.CoinData
import com.project.cointerest.R


class coinFragment() : Fragment() {

    lateinit var My_recyclerView: RecyclerView
    var selectedList = ArrayList<CoinData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("코인프래그먼트 체크")
        // Inflate the layout for this fragment

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

        //selectedList.add(com.project.cointerest.CoinData("비트코인","BTC","BTC","KRW"))

      //  var rootView = inflater.inflate(R.layout.fragment_coin, container, false)
     //   My_recyclerView = rootView.findViewById(R.id.coin_content_view!!) as RecyclerView

       // My_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        My_recyclerView.adapter = CoinContentAdapter(requireContext(), selectedList )
       // My_recyclerView.adapter = SearchFragmentRecyclerAdapter(requireContext(), coin_list_KRW ,selectedList)

    }


}