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
import java.lang.Exception
import android.content.Intent
import android.net.Uri


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var coinFrag : Fragment = coinFragment()
    var backBtn : Long = 0

    val upbitPackage = "com.dunamu.exchange" //업비트 앱 패키지 주소
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("GO")
        tl_ac_main_bottom_menu.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, coinFrag).commit()

    }

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
                try {
                    val intentupbit = packageManager.getLaunchIntentForPackage(upbitPackage)
                    intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intentupbit)
                } catch (e: Exception){
                    val url = "market://details?id=$upbitPackage"
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(i)
                }
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