package com.project.cointerest

//import android.support.v7.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager

import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import okhttp3.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.cointerest.Fragment.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_setting.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("GO")
        tl_ac_main_bottom_menu.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().add(R.id.vp_ac_main_frag_pager, coinFragment())
            .commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var coinFrag :Fragment
        coinFrag = coinFragment()
        //fragmentManager.beginTransaction().replace(R.id.coinItem,coinFrag).commit();

        when(item.itemId){
            R.id.coinItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager , coinFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.searchItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, searchFragment()).commitAllowingStateLoss()
                if(coinFrag== null){
                    fragmentManager.beginTransaction().add(R.id.coinItem, coinFrag).commit();
                }
                return true
            }
            R.id.marketItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, marketFragment()).commitAllowingStateLoss()
                if(coinFrag== null){
                    fragmentManager.beginTransaction().add(R.id.coinItem,
                            coinFrag).commit();
                }
                return true
            }
            R.id.chatItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, chatFragment()).commitAllowingStateLoss()
                if(coinFrag== null){
                    fragmentManager.beginTransaction().add(R.id.coinItem, coinFrag).commit();
                }
                return true
            }
            R.id.settingItem -> {
                supportFragmentManager.beginTransaction().replace(R.id.vp_ac_main_frag_pager, settingFragment()).commitAllowingStateLoss()
                if(coinFrag== null){
                    fragmentManager.beginTransaction().add(R.id.coinItem, coinFrag).commit();
                }
                return true
            }
        }
        return false
    }



}