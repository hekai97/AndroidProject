package com.hekai.androidproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekai.androidproject.entites.Contents
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.network.NWPost
import kotlinx.coroutines.launch

class ContentActivityViewModel: ViewModel() {
    private val _data = MutableLiveData<Contents>()
    val data:LiveData<Contents> get() = _data
    private val _post=MutableLiveData<Posts>()
    val postData:LiveData<Posts>get() = _post
    private val pattern:String="^http(s?):\\/\\/[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\:[0-9]{1,5}\\/image\\/.*\\.[a-z|A-Z]{1,5}\\s"
    fun setPost(posts: Posts){
        _post.value=posts
    }
    fun getContentById(cid:Int){
        viewModelScope.launch {
            try {
                val nwPosts=NWPost.nwPosts.getContentById(cid)
                _data.value=nwPosts
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    //该函数的作用是分析文本内容，如果有图片的话就创建出一个ImageView，其余的都用textview
    fun setContent(){
        val regex:Regex
    }
}