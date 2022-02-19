package com.example.projemanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import com.example.projemanage.R

class SigninActivity : AppCompatActivity() {
    var _tbSignIn: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _tbSignIn = findViewById<Toolbar>(R.id.toolbar_sign_in_activity)
        setupActionBar()
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