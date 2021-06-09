package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
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
import android.view.ViewTreeObserver
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.internal.ViewUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.chart_view.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    var coinFrag : Fragment = coinFragment()
    var backBtn : Long = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //var test = FireBaseMessagingService

//        FirebaseMessaging.getInstance().subscribeToTopic("${Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)}")
        FirebaseMessaging.getInstance().subscribeToTopic("gogo")


        setContentView(R.layout.activity_main)
        tl_ac_main_bottom_menu.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, coinFragment()).commit()

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.coinItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager , coinFragment()).commit()
                return true
            }
            R.id.searchItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, searchFragment()).commit()
                return true
            }
            R.id.marketItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, marketFragment()).commit()

                return true
            }
            R.id.chatItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, chatFragment()).commit()

                return true
            }
            R.id.settingItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, settingFragment()).commit()
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
            finish()
        }
        else{
            backBtn = curTime
            Toast.makeText(this, "뒤로가기를 한 번 더 누르시면 앱이 종료됩니다.",Toast.LENGTH_SHORT).show()
        }
    }
}