package com.example.investwell

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope


@Database(entities = arrayOf(UserModel::class), version = 1, exportSchema = false)
abstract  class UserDatabase:RoomDatabase(){

    abstract fun UserDao():UserDao

    companion object {

        @Volatile
        private var InstanceRoom: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {




            return InstanceRoom ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user-database"
                ).build()
                InstanceRoom = instance

                // return instance
                instance
            }
        }
    }
}
