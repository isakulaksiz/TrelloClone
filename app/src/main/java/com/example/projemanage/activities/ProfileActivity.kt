package com.example.projemanage.activities

import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.projemanage.R
import com.example.projemanage.firebase.FireStore
import com.example.projemanage.models.User
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : BaseActivity() {
    private lateinit var toolbar_profile_activity: Toolbar
    private lateinit var iv_user_image: CircleImageView
    private lateinit var et_name: AppCompatEditText
    private lateinit var et_email: AppCompatEditText
    private lateinit var et_mobile: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        toolbar_profile_activity = findViewById(R.id.toolbar_my_profile_activity)
        iv_user_image = findViewById(R.id.iv_user_image)
        et_name = findViewById(R.id.et_name)
        et_email = findViewById(R.id.et_email)
        et_mobile = findViewById(R.id.et_mobile)


        setUpActionBar()
        FireStore().loadUserData(this)
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

    fun setUserDataUI(user: User){
        Glide
            .with(this@ProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_user_image)

        et_name.setText(user.name)
        et_email.setText(user.email)
        if(user.mobile != 0L){
            et_mobile.setText(user.mobile.toString())
        }
    }
}