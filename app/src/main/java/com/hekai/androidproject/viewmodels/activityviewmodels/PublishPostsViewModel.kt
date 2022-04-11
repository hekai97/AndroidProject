package com.hekai.androidproject.viewmodels.activityviewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekai.androidproject.entites.Contents
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.network.NWPost
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.lang.Exception

class PublishPostsViewModel:ViewModel() {
    private val _currentUser= MutableLiveData<LUser>()
    var currentUser:LiveData<LUser> = _currentUser
    fun uploadImage(myImage:MultipartBody.Part){
        viewModelScope.launch {
            try {
                NWPost.nwPosts.imageUpload(myImage)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    suspend fun insertPosts(posts: Posts): Int {
        val res=viewModelScope.async(Dispatchers.IO) {
            insertToRemotePosts(posts)
        }
        return res.await()
    }

    suspend fun insertToRemoteContent(contents: Contents){
        try {
            Log.d("Hekai", "insertToRemoteContent: ${contents}")
            NWPost.nwPosts.insertIntoContents(contents)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private suspend fun insertToRemotePosts(posts: Posts):Int{
        var res=0;
        try {
            res=NWPost.nwPosts.insertPost(posts)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return res
    }
    fun setCurrentUser(lUser: LUser){
        _currentUser.value=lUser
    }
}