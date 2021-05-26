package com.project.cointerest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.project.cointerest.*
import kotlinx.android.synthetic.main.activity_login.*
import android.view.*
import kotlinx.android.synthetic.main.*
import android.annotation.SuppressLint
import android.view.MenuItem
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
import android.widget.EditText
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.chart_view.*
import kotlinx.android.synthetic.main.fragment_signup.*

class CreateAccountActivity : AppCompatActivity() {

    //private var mAuth : FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_signup)

        auth = FirebaseAuth.getInstance()

        //사용자가 입력한 값들
//        var email: String = et_email.text.toString()
//        var password: String = et_password.text.toString()
        val email = findViewById<EditText>(R.id.signup_email)
        val password = findViewById<EditText>(R.id.signup_password)


        //새로운 계정을 생성한다.
        signupbtn.setOnClickListener {
            //Log.d(TAG, "Data: " + email.text + password.text)

            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "email과 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(password.text.toString().length < 8){
                Toast.makeText(this, "password를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                finish()
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                        baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                ).show()
                                //입력필드 초기화
                                email?.setText("")
                                password?.setText("")
                                email.requestFocus()
                            }
                        }
            }
        }


        signupcanclebtn.setOnClickListener {
            finish()
        }
    }
}