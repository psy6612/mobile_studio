package com.project.cointerest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SlidingDrawer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupType
import com.project.cointerest.CoinInfo
import com.project.cointerest.MainActivity
import com.project.cointerest.R
import kotlinx.android.synthetic.main.coin_row_item.view.*
import kotlinx.android.synthetic.main.fragment_coin.*
import kotlinx.android.synthetic.main.fragment_coin.view.*
import okhttp3.*
import java.net.URL


class CoinContentAdapter(val context: Context, var selected: ArrayList<CoinInfo>):
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
            //selected.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.coin_row_item, parent, false)
        return Holder(view)
    }




    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val C_image = itemView?.findViewById<ImageView>(R.id.coin_row_item_image)
        val C_kor = itemView?.findViewById<TextView>(R.id.coin_row_item_name)
        val C_symbol = itemView?.findViewById<TextView>(R.id.coin_row_item_eng)
        //val C_market = itemView?.findViewById<TextView>(R.id.Market)
        var C_price = itemView?.findViewById<TextView>(R.id.coin_row_item_price)
        var Price_str = ""

        fun bind(coin: CoinInfo, context: Context) {

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
            C_price?.text = coin.price
            //C_market?.text = coin.market
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        //println("테스트!!")

        holder.itemView.coin_row_item_chart.setOnClickListener {

            Toast.makeText(context, "차트 클릭 체크", Toast.LENGTH_SHORT).show();
        }
        
        holder.itemView.coin_row_item_layout.setOnClickListener {
            //Toast.makeText(context, "레이아웃 클릭 체크", Toast.LENGTH_SHORT).show();
            val drawer :SlidingDrawer = (SlidingDrawer)findViewById(R.id.slide)
        }

        holder?.bind(selected[position], context)

/*        holder.itemView.setOnClickListener {

        }*/
    }

}




