package com.omni.osmanconsulting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.omni.osmanconsulting.feature.AuthActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        checkAuthenticationState()
    }


    private fun checkAuthenticationState() {
        Log.d("MainActivity", "checkAuthenticationState: checking authentication state.")

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            Log.d(
                "MainActivity",
                "checkAuthenticationState: user is null, navigating back to login screen."
            )

            val intent = Intent(this@MainActivity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Log.d("MainActivity", "checkAuthenticationState: user is authenticated.")
        }
    }

}
