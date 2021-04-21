package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import okhttp3.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.cointerest.Fragment.*
import kotlinx.android.synthetic.main.fragment_search.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    //val coinFrag : Fragment = coinFragment()

    var coinFrag : Fragment = coinFragment()
    var searchFrag : Fragment = searchFragment()
    var marketFrag : Fragment = marketFragment()
    var chatFrag : Fragment = chatFragment()
    var settingFrag : Fragment = settingFragment()

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
  /*              if(coinFrag == null){
                    supportFragmentManager.beginTransaction().add(R.id.vp_ac_main_frag_pager, coinFrag).commitAllowingStateLoss()
                }

                if(coinFrag != null){
                    supportFragmentManager.beginTransaction().show(coinFrag).commitAllowingStateLoss()
                    supportFragmentManager.beginTransaction().hide(searchFrag).commitAllowingStateLoss()
                    supportFragmentManager.beginTransaction().hide(marketFrag).commitAllowingStateLoss()
                    supportFragmentManager.beginTransaction().hide(chatFrag).commitAllowingStateLoss()
                    supportFragmentManager.beginTransaction().hide(settingFrag).commitAllowingStateLoss()
                }*/
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

}