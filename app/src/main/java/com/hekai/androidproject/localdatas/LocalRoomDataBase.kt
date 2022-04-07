package com.hekai.androidproject.localdatas

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LUser::class], version = 1, exportSchema = false)
abstract class LocalRoomDataBase: RoomDatabase() {
    abstract fun getLocalUserDao(): LocalUserDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: LocalRoomDataBase? = null

        fun getDatabase(context: Context): LocalRoomDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalRoomDataBase::class.java,
                    "local_room_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}