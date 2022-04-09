package com.hekai.androidproject.viewmodels.activityviewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.network.NWPost
import kotlinx.coroutines.*

class RegisterActivityViewModel:ViewModel() {

    fun isUserExist(username: String): Deferred<Boolean> {
        val res=viewModelScope.async(Dispatchers.IO) {
            findUserByUserName(username)
        }
        return res
    }
    fun insertUser(users: Users):Deferred<Boolean>{
        val res=viewModelScope.async {
            insertToRemoteDB(users)
        }
        return res
    }


    private suspend fun insertToRemoteDB(users: Users):Boolean{
        var res=false
        try {
            val nwPosts=NWPost.nwPosts.insertUser(users)
            res=nwPosts
        }catch (e:Exception){
            Log.d("Hekai", "insertToRemoteDB: 远程插入失败")
            e.printStackTrace()
        }
        return res
    }
    private suspend fun findUserByUserName(username:String):Boolean{
        var res=false;
        Log.d("Hekai", "test: ${res}before")
        try {
            res=NWPost.nwPosts.isUserExist(username)
        }catch (e:Exception){
            e.printStackTrace()
        }
        Log.d("Hekai", "test: ${res} after")
        return res;
    }
}