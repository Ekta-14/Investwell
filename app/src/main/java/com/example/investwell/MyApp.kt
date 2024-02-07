package com.example.investwell

import android.app.Application

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: MyApp

        // Access the application instance from anywhere in your code
        fun getInstance(): MyApp {
            return instance
        }
    }
}