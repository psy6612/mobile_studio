package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import okhttp3.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.cointerest.Fragment.*
import kotlinx.android.synthetic.main.fragment_search.*
import android.widget.RelativeLayout
import android.view.View

class MainActivity : AppCompatActivity() {
    var coinFrag : Fragment = coinFragment()
    var backBtn : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("GO")
        //tl_ac_main_bottom_menu.setOnNavigationItemSelectedListener(this)
        configureBottomNavigation()
        //supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, coinFrag).commit()
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


/*    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.coinItem -> {
                tl_ac_main_bottom_menu.findViewById(R.id.coinItem) as RelativeLayout
                //supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager , coinFrag).commitAllowingStateLoss()
                return true
            }
            R.id.searchItem -> {
                tl_ac_main_bottom_menu.findViewById(R.id.searchItem) as RelativeLayout
                //supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, searchFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.marketItem -> {
                tl_ac_main_bottom_menu.findViewById(R.id.marketItem) as RelativeLayout
                //supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, marketFragment()).commitAllowingStateLoss()

                return true
            }
            R.id.chatItem -> {
                tl_ac_main_bottom_menu.findViewById(R.id.chatItem) as RelativeLayout
                //supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, chatFragment()).commitAllowingStateLoss()

                return true
            }
            R.id.settingItem -> {
                tl_ac_main_bottom_menu.findViewById(R.id.settingItem) as RelativeLayout
                //supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, settingFragment()).commitAllowingStateLoss()

                return true
            }
        }
        return false
    }*/


    override fun onBackPressed() {
        var curTime : Long = System.currentTimeMillis()
        var gapTime : Long = curTime -backBtn
        if(gapTime >= 0 && gapTime <= 2000){
            super.onBackPressed()
        }
        else{
            backBtn = curTime
            Toast.makeText(this, "뒤로가기를 한 번 더 누르시면 앱이 종료됩니다.",Toast.LENGTH_SHORT).show()
        }


    }
}