package com.hekai.androidproject.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.network.NWPost
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {
    private val _data= MutableLiveData<List<Posts>>()
    val data=_data

    init {
        getPostList()
    }

    fun getPostList() {
        viewModelScope.launch {
            try {
                val nwPosts= NWPost.nwPosts.getPostsList()
                _data.value=nwPosts
            }catch (e: Exception){
                Log.d("Hekai", "getPostsListFail: ")
                e.printStackTrace()
            }
        }
    }
}