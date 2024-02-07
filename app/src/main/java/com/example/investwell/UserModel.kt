package com.example.investwell

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "User_table")
data class UserModel(
    @ColumnInfo val name:String,
    //primary key
    @PrimaryKey val email:String,
    @ColumnInfo val occupation:String,
    @ColumnInfo val password: Int
)
