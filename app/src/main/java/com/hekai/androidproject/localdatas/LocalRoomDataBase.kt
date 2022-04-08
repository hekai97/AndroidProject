package com.hekai.androidproject.localdatas

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LUser::class], version = 1, exportSchema = false)
abstract class LocalRoomDataBase: RoomDatabase() {
    abstract fun getLocalUserDao(): LocalUserDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: LocalRoomDataBase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): LocalRoomDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalRoomDataBase::class.java,
                    "local_room_database"
                )
                    /*.addCallback(LocalRoomDataBaseCallBack(scope))*/
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

//    private class LocalRoomDataBaseCallBack(private val scope: CoroutineScope) :RoomDatabase.Callback() {
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//            INSTANCE?.let {
//                scope.launch(Dispatchers.IO) {
//                    populateDatabase(it.getLocalUserDao())
//                }
//            }
//        }
//        suspend fun populateDatabase(localUserDao: LocalUserDao){
//            localUserDao.deleteAll()
//            val user:LUser=LUser(1,"1731673423","K999","image/12.jpg","hk19990707",0,"18309346278","hekaigs@gmail.com",true)
//            localUserDao.insert(user)
//        }
//    }
}