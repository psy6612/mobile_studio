package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        my_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//        my_recycler_view.setHasFixedSize(true)

        configureBottomNavigation()

        println("체크000000")
        JsonMake()

    }
    private fun configureBottomNavigation() {
        vp_ac_main_frag_pager.adapter = MainFragmentStatePagerAdapter(supportFragmentManager, 5)

        tl_ac_main_bottom_menu.setupWithViewPager(vp_ac_main_frag_pager)

        val bottomNaviLayout: View =
            this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)

        tl_ac_main_bottom_menu.getTabAt(0)!!.customView =
            bottomNaviLayout.findViewById(R.id.tab_interesting_coin) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(1)!!.customView =
            bottomNaviLayout.findViewById(R.id.tab_search) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(2)!!.customView =
            bottomNaviLayout.findViewById(R.id.tab_market) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(3)!!.customView =
            bottomNaviLayout.findViewById(R.id.tab_chat) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(4)!!.customView =
            bottomNaviLayout.findViewById(R.id.tab_setting) as RelativeLayout
    }
    fun JsonMake(){
        println("데어터를 가져 오는 중...")
        val url = "http://3.35.174.63/market.php"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val js = response?.body()?.string()
                println(js)
                //baseTextView.text = body
                try {
                    println("체크11111")
                    val coinInfo = JSONArray(js)
                    println("체크22222")
                    //val jsonArray = coinInfo.optJSONArray("trade_price")

                    var i = 0
                    while(i< coinInfo.length()){
                        val jsonObject = coinInfo.getJSONObject(i)
                        val market_name = jsonObject.getString("market")
                        val arr = market_name.split("-")
                        if(arr[0] == "BTC"){
                            println(market_name)
                        }
                        i++
                    }


                } catch (e: JSONException) {
                    println("error")
                    println(e.printStackTrace())
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Request Fail")
            }
        })

    }
}