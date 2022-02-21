package com.example.projemanage.firebase

import android.util.Log
import com.example.projemanage.activities.SigninActivity
import com.example.projemanage.activities.SignupActivity
import com.example.projemanage.models.User
import com.example.projemanage.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStore {

    private val _fireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignupActivity, userInfo: User){
        _fireStore.collection(Constants.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge())
            .addOnSuccessListener { activity.userRegisteredSuccess() }
            .addOnFailureListener { e -> Log.e(activity.javaClass.simpleName, "Error writing document") }
    }

    fun signInUser(activity: SigninActivity){
        _fireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {  document ->
                val loggedInUser = document.toObject(User::class.java)!!
                if(loggedInUser != null)
                    activity.signInSuccess(loggedInUser)
            }
            .addOnFailureListener { e -> Log.e("Firestore - signInUser","Error should be fireStore class") }
    }

    fun getCurrentUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}