package com.example.myapplication10

import androidx.multidex.MultiDexApplication
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

// Dex : Dalvic Executale (64k)
class MyApplication : MultiDexApplication() {
    companion object {
        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            if(currentUser != null) {
                email = currentUser.email
                return currentUser.isEmailVerified
            }
            return false
        }

        lateinit var auth: FirebaseAuth
        var email: String? = null

    }
    override fun onCreate() {
        super.onCreate()

        auth = Firebase.auth


    }
}