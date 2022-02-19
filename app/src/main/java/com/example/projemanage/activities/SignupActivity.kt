package com.example.projemanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import com.example.projemanage.R

class SignupActivity : AppCompatActivity() {

    var _tbSignUp: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _tbSignUp = findViewById<Toolbar>(R.id.toolbar_sign_up_activity)

        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(_tbSignUp)

        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        _tbSignUp?.setNavigationOnClickListener{onBackPressed()} // geri gitme
    }
}