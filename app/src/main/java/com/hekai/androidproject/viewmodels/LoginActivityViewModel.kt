package com.hekai.androidproject.viewmodels

import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.network.NWPost
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.concurrent.TimeUnit

class LoginActivityViewModel:ViewModel() {
    private val _user=MutableLiveData<Users>()
    var user:LiveData<Users> = _user
    private val _isTimeLoading=MutableLiveData<Boolean>(false)
    var isTimeLoading=_isTimeLoading
    fun vaildUser(username:String,password:String){
        viewModelScope.launch {
            try {
                val nwPost=NWPost.nwPosts.vaLidUser(username,password)
                _user.value=nwPost
            }catch (e:Exception){
                Log.d("Hekai", "验证用户网络错误")
                e.printStackTrace()
            }
        }
    }
    fun waitTime(){
        runBlocking {
            delay(1000)
            isTimeLoading.value=true
        }
    }
}