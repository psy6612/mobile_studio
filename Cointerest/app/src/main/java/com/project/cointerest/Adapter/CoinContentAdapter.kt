package com.project.cointerest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.service.voice.AlwaysOnHotwordDetector
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.TextKeyListener.clear
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.ChartView
import com.project.cointerest.CoinInfo
import com.project.cointerest.DiffCallback
import com.project.cointerest.R
import kotlinx.android.synthetic.main.coin_row_item.view.*
import kotlinx.android.synthetic.main.fragment_coin.*
import kotlinx.android.synthetic.main.fragment_coin.view.*
import okhttp3.*
import java.net.URL
import java.util.Collections.addAll
import kotlin.concurrent.timer
import kotlin.math.pow


class CoinContentAdapter(val context: Context, var selected: ArrayList<CoinInfo>):
        RecyclerView.Adapter<CoinContentAdapter.Holder>() {


    private val diffUtil = AsyncListDiffer(this, DiffCallback())

    fun replaceTo(newItems: List<CoinInfo>) = diffUtil.submitList(newItems)

    fun getItem(position: Int) = diffUtil.currentList[position]


    override fun getItemCount(): Int = selected.size


//    override fun getItemCount(): Int = selected.size
            //selected.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.coin_row_item, parent, false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val c_image = itemView?.findViewById<ImageView>(R.id.coin_row_item_image)
        val c_kor = itemView?.findViewById<TextView>(R.id.coin_row_item_name)
        val c_symbol = itemView?.findViewById<TextView>(R.id.coin_row_item_eng)
//        val C_market = itemView?.findViewById<TextView>(R.id.Market)
        var c_price = itemView?.findViewById<TextView>(R.id.coin_row_item_price)
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
                c_image?.setImageBitmap(bitmap)

            } else {
                c_image?.setImageResource(R.mipmap.ic_launcher)
            }

            c_kor?.text = coin.kor_name
            c_symbol?.text = coin.symbol
            c_price?.text = coin.price
            //C_market?.text = coin.market

            itemView.coin_row_item_chart.setOnClickListener {

                Toast.makeText(context, "차트 클릭 체크", Toast.LENGTH_SHORT).show()

                Intent(context, ChartView::class.java).apply {
                    putExtra("coin", "${coin.symbol}${coin.market}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.itemView.coin_row_item_layout.setOnClickListener {
            Toast.makeText(context, "레이아웃 클릭 체크", Toast.LENGTH_SHORT).show();
            //val drawer :SlidingDrawer = (SlidingDrawer).findViewById(R.id.slide)
            //drawer.animateClose()
            var chart_mini = holder.itemView.mini_chart
            chart_mini.visibility=View.VISIBLE
        }

        holder.itemView.coin_row_item_price.addTextChangedListener(object : TextWatcher {
            var priceView = holder.itemView.coin_row_item_price
            var upColor = Color.parseColor("#B22222")
            var downColor = Color.parseColor("#4169E1")
            var normalColor = Color.GRAY
            var priceBeforeChange : Float? = null
            var priceAfterChange : Float? = null
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString().contains("E") && s.toString().contains("KRW")){
                    var digits = s.toString().split("E")[1].split("KRW")[0].toFloatOrNull()
                    val ten : Float = 10.0F
                    if (digits != null) {
                        digits = ten.pow(digits)
                    }
                    priceBeforeChange = s.toString().split("E")[0].toFloatOrNull()?.times(digits!!)
                }
                else if (priceView.text.toString().contains("KRW")){
                    priceBeforeChange = s.toString().split("KRW")[0].toFloatOrNull()
                }
                else {
                    priceBeforeChange = s.toString().split("BTC")[0].toFloatOrNull()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (priceView.text.toString().contains("E") && priceView.text.toString().contains("KRW")){
                    var digits = priceView.text.toString().toString().split("E")[1].split("KRW")[0].toFloatOrNull()
                    val ten : Float = 10.0F
                    if (digits != null) {
                        digits = ten.pow(digits!!)
                    }
                    priceAfterChange = priceView.text.toString().split("E")[0].toFloatOrNull()?.times(digits!!)
                }
                else if (priceView.text.toString().contains("KRW")){
                    priceAfterChange =priceView.text.toString().split("KRW")[0].toFloatOrNull()
                }
                else {
                    priceAfterChange =priceView.text.toString().split("BTC")[0].toFloatOrNull()
                }
                if (priceBeforeChange != null && priceAfterChange != null) {
                    if (priceAfterChange!! > priceBeforeChange!!) {
                        //priceView.paintFlags = priceView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                        priceView.setTextColor(upColor)
                    }
                    else if (priceAfterChange!! < priceBeforeChange!!) {
                        //priceView.paintFlags = priceView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                        priceView.setTextColor(downColor)
                    }
                    else{
                        Thread.sleep(100)
                        priceView.setTextColor(normalColor)
                        //priceView.paintFlags = priceView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
                    }
                }
            }
        })

//        holder.bind(getItem(position),context)
        holder?.bind(selected[position], context)

/*        holder.itemView.setOnClickListener {

        }*/
    }

}




