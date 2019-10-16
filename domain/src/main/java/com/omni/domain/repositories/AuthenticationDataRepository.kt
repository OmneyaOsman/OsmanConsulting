package com.omni.domain.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


val repository: AuthenticationDataRepository by lazy { AuthenticationDataRepository() }

class AuthenticationDataRepository(private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()) {


    suspend fun login(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser ?: throw FirebaseAuthException("Auth", "Failed")
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser ?: throw FirebaseAuthException("Auth", "Failed")
    }

    suspend fun sendVerificationEMail(user: FirebaseUser) =
        user.sendEmailVerification().await()

    fun logout() = firebaseAuth.signOut()

}