package com.project.cointerest.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.project.cointerest.R
import kotlinx.android.synthetic.main.chart_view.*
import kotlinx.android.synthetic.main.fragment_chat.*


class chatFragment : Fragment() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_chat, container, false)

        var mWebView = v.findViewById(R.id.notice_view) as WebView
        mWebView.loadUrl("https://m.coinness.com/ko-kr")
        val webSettings: WebSettings = mWebView.getSettings()
        webSettings.javaScriptEnabled = true
        mWebView.setWebViewClient(WebViewClient())


        mWebView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action != KeyEvent.ACTION_DOWN)
                    return true
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack()
                    } else {
                        requireActivity().onBackPressed()
                    }
                    return true
                }
                return false
            }
        })

        return v
    }

}