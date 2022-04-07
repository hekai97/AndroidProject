package com.hekai.androidproject.localdatas

import androidx.annotation.WorkerThread

class LocalUserRepository(private val localUserDao: LocalUserDao) {
    val currentUser=localUserDao.getLoggedUser()

    @WorkerThread
    suspend fun insert(lUser: LUser) {
        localUserDao.insert(lUser)
    }
}