package com.example.trelloclone.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.trelloclone.R
import com.example.trelloclone.firebase.FireStore
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


class ProfileActivity : BaseActivity() {
    private lateinit var toolbar_profile_activity: Toolbar
    private lateinit var iv_user_image: CircleImageView
    private lateinit var et_name: AppCompatEditText
    private lateinit var et_email: AppCompatEditText
    private lateinit var et_mobile: AppCompatEditText
    private lateinit var btn_update: Button
    private var _selectedFileUri: Uri? = null
    private var _profileImageUri: String = ""
    private var _userDetails = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        toolbar_profile_activity = findViewById(R.id.toolbar_my_profile_activity)
        iv_user_image = findViewById(R.id.iv_user_image)
        et_name = findViewById(R.id.et_name)
        et_email = findViewById(R.id.et_email)
        et_mobile = findViewById(R.id.et_mobile)
        btn_update = findViewById(R.id.btn_update)

        setUpActionBar()
        FireStore().loadUserData(this)

        iv_user_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }

        btn_update.setOnClickListener {
            if(_selectedFileUri != null)
                uploadUserImage()
            else{
                showProgressDialog(resources.getString(R.string.please_wait))

                updateUserProfileData()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                Toast.makeText(this, "u just need the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null){
            _selectedFileUri = data.data // data return to uri

            Glide
                .with(this@ProfileActivity)
                .load(_selectedFileUri)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(iv_user_image)
        }
    }

    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        if(_selectedFileUri != null){
            val storageRef:StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() +
                "."+ Constants.getImageFromUri(this, _selectedFileUri))
            storageRef.putFile(_selectedFileUri!!).addOnSuccessListener {
                e ->
                run {
                    Log.i("Firebase Image URL", e.metadata!!.reference!!.downloadUrl.toString())
                    e.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        Log.i("Downloadable Image Uri", uri.toString())
                        _profileImageUri = uri.toString()

                        updateUserProfileData()
                    }
                }
            }.addOnFailureListener{
                e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                hideProhgressDialog()
            }
        }

    }

    fun updateUserProfileData(){
        val userHashMap  = HashMap<String, Any>()

        var isChanged:Boolean = false
        if (_profileImageUri.isNotEmpty() && _profileImageUri != _userDetails.image) {
            userHashMap[Constants.IMAGE] = _profileImageUri
            isChanged = true
        }
        else if(et_name.text.toString() != _userDetails.name){
            userHashMap[Constants.NAME] = et_name.text.toString()
            isChanged = true
        }else if(et_mobile.text.toString() != _userDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = et_mobile.text.toString()
            isChanged = true
        }
        if(isChanged)
            FireStore().updateProfileData(this, userHashMap)
    }

    fun profileUpdateSuccess(){
        hideProhgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
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
        //_userDetails = user as User.Companion
        _userDetails = user
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