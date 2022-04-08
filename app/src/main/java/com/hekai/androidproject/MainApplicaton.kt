package com.hekai.androidproject

import android.app.Application
import com.hekai.androidproject.localdatas.LocalRoomDataBase
import com.hekai.androidproject.localdatas.LocalUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplicaton:Application() {
    val applicatonScope=CoroutineScope(SupervisorJob())
    val database by lazy { LocalRoomDataBase.getDatabase(this,applicatonScope) }
    val repository by lazy { LocalUserRepository(database.getLocalUserDao()) }
}