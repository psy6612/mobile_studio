package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.annotation.SuppressLint
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
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.chart_view.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    var coinFrag : Fragment = coinFragment()
    var backBtn : Long = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*
        val webView = findViewById<WebView>(R.id.chart_view)

        // 와이파이 & 데이터 연결되어 있으면 웹뷰 생성


            // 인터넷 연결 되어 있을 때 (셀룰러/와이파이)
            webView.settings.javaScriptEnabled = true // 자바 스크립트 허용

            // 웹뷰안에 새 창이 뜨지 않도록 방지
            webView.webViewClient = WebViewClient()
            webView.webChromeClient = WebChromeClient()

            // 원하는 주소를 WebView에 연결
            webView.loadUrl("http://3.35.174.63/chart.php")
*/

        println("GO")
        tl_ac_main_bottom_menu.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, coinFrag).commit()
    }

/*    private fun configureBottomNavigation() {
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
    }*/


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.coinItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager , coinFrag).commitAllowingStateLoss()
                return true
            }
            R.id.searchItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, searchFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.marketItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, marketFragment()).commitAllowingStateLoss()

                return true
            }
            R.id.chatItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, chatFragment()).commitAllowingStateLoss()

                return true
            }
            R.id.settingItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, settingFragment()).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }


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