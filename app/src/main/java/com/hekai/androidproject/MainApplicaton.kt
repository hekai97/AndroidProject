package com.hekai.androidproject

import android.app.Application
import com.hekai.androidproject.localdatas.LocalRoomDataBase
import com.hekai.androidproject.localdatas.LocalUserRepository

class MainApplicaton:Application() {
    val database by lazy { LocalRoomDataBase.getDatabase(this) }
    val repository by lazy { LocalUserRepository(database.getLocalUserDao()) }
}