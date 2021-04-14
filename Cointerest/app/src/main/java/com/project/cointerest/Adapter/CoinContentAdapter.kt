package com.project.cointerest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.fragment_coin.*
import kotlinx.android.synthetic.main.fragment_coin.view.*
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.CoinData
import com.project.cointerest.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.URL
import kotlin.concurrent.timer

class CoinContentAdapter(val context: Context, var selected:ArrayList<CoinData>):
        RecyclerView.Adapter<CoinContentAdapter.Holder>() {

    //선택한 아이템리스트
    //var selectedList = ArrayList<CoinData>()

    // var btn : Button = findViewById(R.id.search_searchView)

/*    //클릭 인터페이스  정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener*/

    override fun getItemCount(): Int = selected.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.coin_row_item, parent, false)
        return Holder(view)
    }




    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val C_image = itemView?.findViewById<ImageView>(R.id.coin_row_item_image)
        val C_kor = itemView?.findViewById<TextView>(R.id.coin_row_item_name)
        val C_symbol = itemView?.findViewById<TextView>(R.id.coin_row_item_eng)
        //val C_market = itemView?.findViewById<TextView>(R.id.Market)

        //var btn = itemView?.findViewById<Button>(R.id.search_add_button)



        fun bind(coin: CoinData, context: Context) {
            println("버튼값!!")
            println(selected.size)
/*            println(btn)
            btn?.setOnClickListener{
                println("어댑터 버튼")
            }*/

            if (coin.coin_image != "") {

                //println("이미지 체크메이드")

                //이미지 다이나믹하게 호출
                var image_task: URLtoBitmapTask = URLtoBitmapTask()
                image_task = URLtoBitmapTask().apply {
                    url = URL("https://static.upbit.com/logos/${coin.coin_image}.png")
                }
                var bitmap: Bitmap = image_task.execute().get()
                C_image?.setImageBitmap(bitmap)

            } else {
                C_image?.setImageResource(R.mipmap.ic_launcher)
            }
            C_kor?.text = coin.kor_name
            C_symbol?.text = coin.symbol
            //C_market?.text = coin.market
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        println("테스트!!")
        holder?.bind(selected[position], context)

/*        holder.itemView.setOnClickListener {

        }*/
    }

}




