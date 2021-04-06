package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.project.cointerest.Adapter.SearchFragmentRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_search.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        my_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//        my_recycler_view.setHasFixedSize(true)
      //  val mAdapter = SearchFragmentRecyclerAdapter(this, cl)
      //  search_content_view.adapter = mAdapter
        println("GO")
        configureBottomNavigation()



        //btn_click()

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

}