package com.project.cointerest

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.fragment_signup.*


class LoginActivity : AppCompatActivity(){
    //firebase Auth
    private lateinit var auth: FirebaseAuth
    //google client
    private lateinit var googleSignInClient: GoogleSignInClient

    private val TAG : String = "LoginActivity"

    //private const val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        App.prefs.setString("uuid",Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID))

        if(App.prefs.getString("notice", "nothing") =="nothing"){
            App.prefs.setString("notice","TRUE")
        }


        var token : String
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val registToken = task.result
            token = registToken.toString()
            App.prefs.setString("token",token)

            Log.d("???????????????", token.toString())
        })


        //???????????? ??????
        sign_up.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        not_login.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //?????????
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.login_id)
        val password = findViewById<EditText>(R.id.login_pwd)

        //?????????
        loginbtn.setOnClickListener {

            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "email ?????? password??? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                toMainActivity(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                            }

                            // ...
                        }
            }

        }




        //googleSignInBtn.setOnClickListener (this) // ?????? ????????? ??????
        googleSignInBtn.setOnClickListener {signIn()}

        //Google ????????? ?????? ??????. requestIdToken ??? Email ??????
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onResume() {
        super.onResume()
        val currentUser = auth?.currentUser

    }

    // onStart. ????????? ?????? ?????? ?????? ???????????? ????????? ??????
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!==null){ // ?????? ????????? ??????????????? ?????? ?????? ??????????????? ??????
            toMainActivity(auth.currentUser)
        }
    } //onStart End

    // onActivityResult
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    } // onActivityResult End

    // firebaseAuthWithGoogle
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        //Google SignInAccount ???????????? ID ????????? ???????????? Firebase Auth??? ???????????? Firebase??? ??????
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle ??????", task.exception)
                    toMainActivity(auth?.currentUser)
                } else {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle ??????", task.exception)
                    Snackbar.make(login_layout, "???????????? ?????????????????????.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }// firebaseAuthWithGoogle END


    // toMainActivity
    fun toMainActivity(user: FirebaseUser?) {
        if(user !=null) { // MainActivity ??? ??????
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    } // toMainActivity End

    // signIn
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // signIn End

//    override fun onClick(p0: View?) {
//    }

}