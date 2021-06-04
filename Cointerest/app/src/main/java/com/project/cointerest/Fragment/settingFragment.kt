package com.project.cointerest.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.PopupMenu
import com.project.cointerest.App
import com.project.cointerest.R
import kotlinx.android.synthetic.main.chart_view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_setting.*
import okhttp3.*
import java.io.IOException
import java.net.URL


class settingFragment : Fragment() {
    private val token = App.prefs.getString("token", "nothing")
    var noticeState = App.prefs.getString("notice", "nothing")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_setting, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(noticeState != "TRUE"){
            target_alarm_switch.toggle()
        }

        target_alarm_switch.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    App.prefs.setString("notice","TRUE")
                    noticeSet("TRUE")
                }
                else{
                    App.prefs.setString("notice","FALSE")
                    noticeSet("FALSE")
                }
            }
        })
    }

    fun noticeSet(notice : String){
        val url = URL("http://54.180.134.53/notice.php")

        val requestBody : RequestBody = FormBody.Builder()
            .add("notice", notice)
            .add("token", token)
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
    }
}