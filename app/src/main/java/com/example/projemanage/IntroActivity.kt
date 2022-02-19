package com.example.projemanage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button

class IntroActivity : AppCompatActivity() {
    lateinit var _btnSignUpIntro: Button
    lateinit var _btnSignInIntro: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        _btnSignUpIntro = findViewById<Button>(R.id.btn_sign_up_intro)
        _btnSignInIntro = findViewById<Button>(R.id.btn_sign_in_intro)

        _btnSignInIntro.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        _btnSignUpIntro.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
        }
    }
}