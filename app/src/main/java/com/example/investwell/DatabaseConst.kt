package com.example.investwell

object DatabaseConst
{
    val database:UserDatabase by lazy {
        UserDatabase.getDatabase(MyApp.getInstance())
    }
}