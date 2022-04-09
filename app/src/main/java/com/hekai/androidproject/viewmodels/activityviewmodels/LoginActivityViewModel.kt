package com.hekai.androidproject.viewmodels.activityviewmodels

import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.network.NWPost
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class LoginActivityViewModel:ViewModel() {
    private val _user=MutableLiveData<Users>()
    var user:LiveData<Users> = _user
    private val _isTimeLoading=MutableLiveData<Boolean>(false)
//    var isTimeLoading=_isTimeLoading
    fun vaildUser(username:String,password:String){
//        viewModelScope.launch {
//            try {
//                val nwPost=NWPost.nwPosts.vaLidUser(username,password)
//                _user.value=nwPost
//            }catch (e:Exception){
//                Log.d("Hekai", "验证用户网络错误")
//                e.printStackTrace()
//            }
//        }
        runBlocking {
            val res=viewModelScope.async(Dispatchers.IO) {
                validUserByUserNameAndPassword(username, password)
            }
            _user.value=res.await()
        }
    }

    private suspend fun validUserByUserNameAndPassword(username: String, password: String): Users? {
        var users:Users?=null
        try {
            Log.d("Hekai", "try")
            val nwPost=NWPost.nwPosts.vaLidUser(username,password)
            users=nwPost
            Log.d("Hekai", "user=nwPost")
        }catch (e:Exception){
            Log.d("Hekai", "验证用户网络错误")
            e.printStackTrace()
        }
        Log.d("Hekai", "即将return")
        return users
    }
//    fun waitTime(){
//        runBlocking {
//            delay(1000)
//            isTimeLoading.value=true
//        }
//    }
}