package com.example.investwell

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userModel: UserModel)

    @Query("Select* FROM User_table")
    fun getUser():UserModel

    @Query("Select* FROM User_table WHERE User_table.email LIKE :email")
    suspend fun getUserThroughEmail(email:String):UserModel

    @Query("Delete From user_table")
    suspend fun deleteUser()
}