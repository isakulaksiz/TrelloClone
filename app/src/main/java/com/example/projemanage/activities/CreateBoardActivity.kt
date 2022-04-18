package com.example.projemanage.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projemanage.R
import com.example.projemanage.firebase.FireStore
import com.example.projemanage.models.Board
import com.example.projemanage.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class CreateBoardActivity : BaseActivity() {
    private lateinit var toolbar_create_board_activity: Toolbar
    private var _selectedFileUri: Uri? = null
    private lateinit var iv_board_image: CircleImageView
    private lateinit var et_board_name: AppCompatEditText
    private lateinit var btn_create: Button

    private lateinit var _userName: String
    private var _boardImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        toolbar_create_board_activity = findViewById(R.id.toolbar_create_board_activity)
        iv_board_image = findViewById(R.id.ic_board_image)
        et_board_name = findViewById(R.id.et_board_name)
        btn_create = findViewById(R.id.btn_create)

        iv_board_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }
        setUpActionBar()
        if(intent.hasExtra(Constants.NAME)){
            _userName = intent.getStringExtra(Constants.NAME).toString()
        }
        btn_create.setOnClickListener {
            if(_selectedFileUri!= null){
                uploadBoardImage()
            }else{
                showProgressDialog("Please wait")
                createBoard()
            }
        }
    }

    private fun createBoard(){
        val assignedUsersArrList: ArrayList<String> = ArrayList()
        assignedUsersArrList.add(getCurrentUserId())

        var board = Board(
            et_board_name.text.toString(),
            _boardImageURL,
            _userName,
            assignedUsersArrList
        )

        FireStore().createBoard(this, board)
    }

    private fun uploadBoardImage(){
        showProgressDialog("Please wait")
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "BOARD_IMAGE" + System.currentTimeMillis() +
                    "."+ Constants.getImageFromUri(this, _selectedFileUri))
        storageRef.putFile(_selectedFileUri!!).addOnSuccessListener {
                e ->
            run {
                Log.i("Board Image URL", e.metadata!!.reference!!.downloadUrl.toString())
                e.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("Downloadable Image Uri", uri.toString())
                    _boardImageURL = uri.toString()

                    createBoard()
                }
            }
        }.addOnFailureListener{
                e ->
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            hideProhgressDialog()
        }
    }
    fun boardCreatedSuccessfully(){
        hideProhgressDialog()
        finish()
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_create_board_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            actionBar.title = resources.getString(R.string.create_board_title)
        }

        toolbar_create_board_activity.setNavigationOnClickListener { onBackPressed() }
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
                .with(this)
                .load(_selectedFileUri)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(iv_board_image)
        }
    }
}