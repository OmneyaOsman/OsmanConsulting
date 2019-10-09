package com.omni.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


val authenticationDataRepository: AuthenticationDataRepository by lazy { AuthenticationDataRepository() }

class AuthenticationDataRepository(private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()) {


    suspend fun login(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser ?: throw FirebaseAuthException("Auth", "Failed")
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser ?: throw FirebaseAuthException("Auth", "Failed")
    }

    private fun FirebaseUser.sendVerificationEMail() {
        this.sendEmailVerification()
    }

    fun logout() = firebaseAuth.signOut()

}