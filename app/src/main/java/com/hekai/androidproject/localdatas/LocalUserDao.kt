package com.hekai.androidproject.localdatas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalUserDao {
    @Query("select * from login_status where status = 1")
    fun getLoggedUser(): Flow<LUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lUser: LUser)

    @Query("delete from login_status")
    suspend fun deleteAll()

    @Query("update login_status set status=0 where status=1")
    suspend fun logOut()
}