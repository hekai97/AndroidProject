package com.hekai.androidproject.localdatas

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class LocalUserRepository(private val localUserDao: LocalUserDao) {
    val currentUser: Flow<LUser> = localUserDao.getLoggedUser()

    @WorkerThread
    suspend fun insert(lUser: LUser) {
        localUserDao.insert(lUser)
    }

    suspend fun deleteAll(){
        localUserDao.deleteAll()
    }
}