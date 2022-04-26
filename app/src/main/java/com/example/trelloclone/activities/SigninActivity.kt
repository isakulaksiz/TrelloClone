package com.example.trelloclone.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.trelloclone.R
import com.example.trelloclone.models.User
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : BaseActivity() {
    var _tbSignIn: Toolbar? = null
    private lateinit var _btn_signin: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _tbSignIn = findViewById<Toolbar>(R.id.toolbar_sign_in_activity)
        _btn_signin = findViewById<Button>(R.id.btn_sign_in)

        _btn_signin.setOnClickListener {
            signInRegUser()
        }

        auth = FirebaseAuth.getInstance()
        setupActionBar()
    }

    private fun signInRegUser(){
        val email: String = findViewById<EditText>(R.id.et_email_signin).text.toString().trim{it <= ' '}
        val password: String = findViewById<EditText>(R.id.et_password_signin).text.toString().trim{it <= ' '}

        if(validateForm(email, password)){
            showProgressDialog("Please wait")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task -> hideProhgressDialog()
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        val w = Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun signInSuccess(user: User){
        hideProhgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun validateForm(email: String, password: String): Boolean {
        return when{
            TextUtils.isEmpty(email) ->{
                showError("Please enter a name")
                false
            }
            TextUtils.isEmpty(password) ->{
                showError("Please enter a password")
                false
            }else -> {
                true
            }
        }
    }
    private fun setupActionBar(){
        setSupportActionBar(_tbSignIn)

        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        _tbSignIn?.setNavigationOnClickListener{onBackPressed()}
    }
}