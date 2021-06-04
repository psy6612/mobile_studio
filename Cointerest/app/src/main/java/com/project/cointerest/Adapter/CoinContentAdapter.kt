package com.project.cointerest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.project.cointerest.*
import com.scichart.core.utility.Dispatcher.runOnUiThread
import kotlinx.android.synthetic.main.chart_view.*
import kotlinx.android.synthetic.main.coin_row_item.view.*
import kotlinx.android.synthetic.main.fragment_coin.*
import kotlinx.android.synthetic.main.fragment_coin.view.*
import kotlinx.coroutines.selects.select
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.util.*
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
                    putExtra("coin", "${coin.symbol}-${coin.market}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: Holder,
        position: Int
        ) {

        var image_task: URLtoBitmapTask = URLtoBitmapTask()
        image_task = URLtoBitmapTask().apply {
            url = URL("https://static.upbit.com/logos/${selected[position].symbol}.png")
        }
        var bitmap: Bitmap = image_task.execute().get()

        holder.itemView.coin_row_item_layout.setOnLongClickListener {

            var builder = AlertDialog.Builder(context)
            builder.setTitle("${selected[position].symbol}-${selected[position].market}")
            builder.setMessage("관심 코인을 삭제하시겠습니까?")

            builder.setIcon(R.drawable.main_icon)

            var listener = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val url = URL("http://54.180.134.53/data_delete.php")
                    var tokenStr = App.prefs.getString("token", "nothing")

                    val requestBody: RequestBody = FormBody.Builder()
                        .add("token", tokenStr)
                        .add("coin", "${selected[position].market}${selected[position].symbol}")
                        .build()
                    val request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()

                    val client = OkHttpClient()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            Log.d("요청", "요청 완료")
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("요청", "요청 실패 ")
                        }
                    })

                    App.prefs.setString(
                        "${selected[position].symbol}-${selected[position].market}",
                        "nothing"
                    )
                    //var check = App.prefs.getString("${selected[position].symbol}-${selected[position].market}","nothing")
                    //Toast.makeText(context, "${check}", Toast.LENGTH_SHORT).show()
                    selected.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, selected.size)
                }
            }

            builder.setPositiveButton("삭제", listener)
            builder.setNegativeButton("취소",null)
            builder.show()
            true
        }

        holder.itemView.coin_row_item_price.addTextChangedListener(object : TextWatcher {
            var priceView = holder.itemView.coin_row_item_price
            var upColor = Color.parseColor("#B22222")
            var downColor = Color.parseColor("#4169E1")
            var normalColor = Color.GRAY
            var priceBeforeChange: Float? = null
            var priceAfterChange: Float? = null
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString().contains("E") && s.toString().contains("KRW")) {
                    var digits = s.toString().split("E")[1].split("KRW")[0].toFloatOrNull()
                    val ten: Float = 10.0F
                    if (digits != null) {
                        digits = ten.pow(digits)
                    }
                    priceBeforeChange = s.toString().split("E")[0].toFloatOrNull()?.times(digits!!)
                } else if (priceView.text.toString().contains("KRW")) {
                    priceBeforeChange = s.toString().split("KRW")[0].toFloatOrNull()
                } else {
                    priceBeforeChange = s.toString().split("BTC")[0].toFloatOrNull()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (priceView.text.toString().contains("E") && priceView.text.toString()
                        .contains("KRW")
                ) {
                    var digits = priceView.text.toString().toString()
                        .split("E")[1].split("KRW")[0].toFloatOrNull()
                    val ten: Float = 10.0F
                    if (digits != null) {
                        digits = ten.pow(digits!!)
                    }
                    priceAfterChange =
                        priceView.text.toString().split("E")[0].toFloatOrNull()?.times(
                            digits!!
                        )
                } else if (priceView.text.toString().contains("KRW")) {
                    priceAfterChange = priceView.text.toString().split("KRW")[0].toFloatOrNull()
                } else {
                    priceAfterChange = priceView.text.toString().split("BTC")[0].toFloatOrNull()
                }
                if (priceBeforeChange != null && priceAfterChange != null) {
                    if (priceAfterChange!! > priceBeforeChange!!) {
                        //priceView.paintFlags = priceView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                        priceView.setTextColor(upColor)
                    } else if (priceAfterChange!! < priceBeforeChange!!) {
                        //priceView.paintFlags = priceView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                        priceView.setTextColor(downColor)
                    } else {
                        Thread.sleep(100)
                        priceView.setTextColor(normalColor)
                        //priceView.paintFlags = priceView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
                    }
                }
            }
        })

        holder?.bind(selected[position], context)

    }

    fun removeData(position: Int) {
        selected.removeAt(position)
        notifyItemRemoved(position)
    }

    // 두 개의 뷰홀더 포지션을 받아 Collections.swap으로 첫번째 위치와 두번째 위치의 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(selected, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    // 선택한 뷰홀더 포지션의 데이터 내용을 바꾸도록 함
    fun setData(position: Int) {
        //selected[position] = listOf("main viewholder touched!", "sub viewholder touched!")
        notifyItemChanged(position)
    }
}




