package com.example.projemanage.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.projemanage.R
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {
    private lateinit var _progressDialog: Dialog

    private var doubleBackToExitPressedOnce = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showProgressDialog(text: String){
        _progressDialog = Dialog(this)
        _progressDialog.setContentView(R.layout.dialog_press)
        _progressDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val _tv_progress = _progressDialog.findViewById<TextView>(R.id.tv_progress)
        _tv_progress.text = text
        _progressDialog.dismiss()
    }

    fun hideProhgressDialog(){
        _progressDialog.dismiss()
    }

    fun getCurrentUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({doubleBackToExitPressedOnce = false},2000)
    }

    fun showError(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@BaseActivity,
                R.color.black
            )
        )
        snackBar.show()
    }
}