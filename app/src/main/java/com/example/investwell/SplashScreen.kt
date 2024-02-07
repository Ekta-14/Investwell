package com.example.investwell

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPrefrences.init(this)

        val success=AppPrefrences.getBooleanLogin(PrefConstants.isUserLoggedIn)
        if(success) {
            startActivity(Intent(this, home::class.java))
            finish()
        }
        else{
            startActivity(Intent(this, login::class.java))
            finish()
        }

    }
}