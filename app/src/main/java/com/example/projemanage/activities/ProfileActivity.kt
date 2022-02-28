package com.example.projemanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.projemanage.R

class ProfileActivity : BaseActivity() {
    private lateinit var toolbar_profile_activity: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        toolbar_profile_activity = findViewById(R.id.toolbar_my_profile_activity)

        setUpActionBar()
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_profile_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            actionBar.title = "My Profile"
        }

        toolbar_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }
}