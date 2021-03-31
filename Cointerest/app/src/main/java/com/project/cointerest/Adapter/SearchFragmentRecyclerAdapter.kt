package com.project.cointerest.Adapter

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


open class SearchFragmentRecyclerAdapter(val context: Context, var coin_list:ArrayList<CoinData>) :
    RecyclerView.Adapter<SearchFragmentRecyclerAdapter.Holder>(), Filterable {



    private var files: ArrayList<CoinData>? = coin_list

    inner class FileViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val str: SearchView? = itemView.search_searchView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_1, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return coin_list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(coin_list[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val C_image = itemView?.findViewById<ImageView>(R.id.imageView2)
        val C_kor = itemView?.findViewById<TextView>(R.id.Coin_kor)
        val C_symbol = itemView?.findViewById<TextView>(R.id.Symbol)
        val C_market = itemView?.findViewById<TextView>(R.id.Market)

        fun bind (coin: CoinData, context: Context) {

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

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

/*
    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
*/


}