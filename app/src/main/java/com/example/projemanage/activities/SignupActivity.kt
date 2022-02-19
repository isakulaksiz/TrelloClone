package com.example.projemanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.projemanage.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignupActivity : BaseActivity() {

    var _tbSignUp: Toolbar? = null
    lateinit var _btn_sign_up: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _tbSignUp = findViewById<Toolbar>(R.id.toolbar_sign_up_activity)
        _btn_sign_up = findViewById<Button>(R.id.btn_sign_up)
        setupActionBar()
        _btn_sign_up.setOnClickListener { registerUser() }
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

    private fun registerUser(){
        val name: String = findViewById<EditText>(R.id.et_name).text.toString().trim{it <= ' '}
        val email: String = findViewById<EditText>(R.id.et_email).text.toString().trim{it <= ' '}
        val password: String = findViewById<EditText>(R.id.et_password).text.toString().trim{it <= ' '}

        if(validateForm(name, email, password)){
            showProgressDialog("Please wait")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task -> hideProhgressDialog()
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result.user!!
                    val reqEmail = firebaseUser.email!!
                    Toast.makeText(
                        this,
                        "You have successfully email address $reqEmail",
                        Toast.LENGTH_SHORT
                    ).show()
                    FirebaseAuth.getInstance().signOut()
                    finish()
                } else {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun validateForm(name: String, email: String, password: String): Boolean{
        return when{
            TextUtils.isEmpty(name) ->{
                showError("Please enter a name")
                false
            }
            TextUtils.isEmpty(email) ->{
                showError("Please enter a email")
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
}