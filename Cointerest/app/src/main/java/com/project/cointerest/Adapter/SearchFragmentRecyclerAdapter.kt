package com.project.cointerest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.CoinData
import com.project.cointerest.Fragment.searchFragment
//import com.project.cointerest.Fragment.CoinList
import com.project.cointerest.R
import kotlinx.android.synthetic.main.fragment_search.view.*



class SearchFragmentRecyclerAdapter(val context: Context, var coin_list:ArrayList<CoinData>) :
        RecyclerView.Adapter<SearchFragmentRecyclerAdapter.Holder>(), Filterable {

    val unFilteredList = coin_list // 필터 전
    var filteredList = coin_list // 필터 후


    override fun getItemCount(): Int = filteredList.size
    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charString = constraint.toString()
                println(charString)
                filteredList = if (charString.isEmpty()) {
                    println("비엇음")
                    unFilteredList

                } else {
                    var filteringList = ArrayList<CoinData>()
                    for (item in unFilteredList) {
                        if (item.kor_name == charString) {
                            filteringList.add(item)
                            println("찾았다!")
                            println(item.kor_name)
                        }
                    }
                    filteringList

                }
                val filterResults = FilterResults()
                filterResults.values = filteredList

                notifyDataSetChanged()
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults??) {
                filteredList = results?.values as ArrayList<CoinData>
                println("result")
                println(filteredList.size)
                notifyDataSetChanged()

            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_1, parent, false)
        return Holder(view)
    }



    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val C_image = itemView?.findViewById<ImageView>(R.id.imageView2)
        val C_kor = itemView?.findViewById<TextView>(R.id.Coin_kor)
        val C_symbol = itemView?.findViewById<TextView>(R.id.Symbol)
        val C_market = itemView?.findViewById<TextView>(R.id.Market)

        fun bind(coin: CoinData, context: Context) {

            if (coin.coin_image != "") {
                val resourceId = context.resources.getIdentifier(coin.coin_image, "drawable", context.packageName)
                C_image?.setImageResource(resourceId)
            } else {
                C_image?.setImageResource(R.mipmap.ic_launcher)
            }
            C_kor?.text = coin.kor_name
            C_symbol?.text = coin.symbol
            C_market?.text = coin.market

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        println("check@@@!!")
        println(filteredList.size)
        holder?.bind(filteredList[position], context)

    }


}