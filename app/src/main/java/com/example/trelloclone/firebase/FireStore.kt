package com.example.trelloclone.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.trelloclone.activities.*
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.Constants
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

    fun createBoard(activity: CreateBoardActivity, board: Board){
        _fireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Board created sucessfully")
                Toast.makeText(activity,"Board created successfully!", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener{
                e ->
                    activity.hideProhgressDialog()
                Log.e(activity.javaClass.simpleName, "ERR", e)
            }
    }

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){
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
                        activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                    }
                    is ProfileActivity -> {
                        activity.setUserDataUI(loggedInUser)
                    }
                }

            }
            .addOnFailureListener { e -> Log.e("Firestore - signInUser","Error should be fireStore class") }
    }

    fun getBoardsList(activity: MainActivity){
        _fireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener {
                document ->
                    Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for(i in document.documents){
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }
                activity.populateBoardsListToUI(boardList)
            }.addOnFailureListener{
                e ->
                    activity.hideProhgressDialog()
                Log.i(activity.javaClass.simpleName, "ERROR while creating !", e)

            }
    }

    fun updateProfileData(activity:ProfileActivity, userHashMap: HashMap<String, Any>){
        _fireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "profile data updated successfully")
                Toast.makeText(activity, "profile data updated successfully", Toast.LENGTH_SHORT).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                Log.i(activity.javaClass.simpleName, "ERR profile data updated")
                Toast.makeText(activity, "ERR profile data updated", Toast.LENGTH_SHORT).show()
            }
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