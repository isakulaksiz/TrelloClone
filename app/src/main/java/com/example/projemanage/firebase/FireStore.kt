package com.example.projemanage.firebase

import android.app.Activity
import android.util.Log
import androidx.viewbinding.ViewBindings
import com.bumptech.glide.Glide
import com.example.projemanage.R
import com.example.projemanage.activities.MainActivity
import com.example.projemanage.activities.ProfileActivity
import com.example.projemanage.activities.SigninActivity
import com.example.projemanage.activities.SignupActivity
import com.example.projemanage.models.User
import com.example.projemanage.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import de.hdodenhof.circleimageview.CircleImageView
import com.example.projemanage.databinding.ActivityProfileBinding as ActivityProfileBinding

class FireStore {

    private val _fireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignupActivity, userInfo: User){
        _fireStore.collection(Constants.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge())
            .addOnSuccessListener { activity.userRegisteredSuccess() }
            .addOnFailureListener { e -> Log.e(activity.javaClass.simpleName, "Error writing document") }
    }

    fun loadUserData(activity: Activity){
        _fireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {  document ->
                val loggedInUser = document.toObject(User::class.java)!!
                when(activity){
                    is SigninActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser)
                    }
                    is ProfileActivity -> {
                        activity.setUserDataUI(loggedInUser)
                    }
                }

            }
            .addOnFailureListener { e -> Log.e("Firestore - signInUser","Error should be fireStore class") }
    }

    fun getCurrentUserId(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}