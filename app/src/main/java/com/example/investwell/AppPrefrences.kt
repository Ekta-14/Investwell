package com.example.investwell

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment

object AppPrefrences {

    private const val NAME = "SpinKotlin"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    //getter
    fun putBooleanLogin(key:String, value:Boolean)
    {
        preferences.edit().putBoolean(key,value).apply()
    }

    fun getBooleanLogin(key: String):Boolean
    {
        return preferences.getBoolean(key,false)
    }

    fun saveLastClickedFragment(fragementID: Int) {
        preferences.edit().putInt(PrefConstants.last_clicked_fragment, fragementID).apply()
    }

    fun getLastClickedFragmentId(): Int {
        return preferences.getInt(PrefConstants.last_clicked_fragment, -1)
    }
}