package com.hekai.androidproject.viewmodels.activityviewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekai.androidproject.entites.Contents
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.network.NWPost
import com.hekai.androidproject.util.RangeAndValue
import com.hekai.androidproject.util.myBaseURL
import kotlinx.coroutines.launch

class ContentActivityViewModel: ViewModel() {
    private val _currentUser=MutableLiveData<LUser>()
    val currentUser:LiveData<LUser> get() =_currentUser
    private val _data = MutableLiveData<Contents>()
    val data:LiveData<Contents> get() = _data
    private val _post=MutableLiveData<Posts>()
    val postData:LiveData<Posts>get() = _post

//    private val pattern:String="^http(s?):\\/\\/[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\:[0-9]{1,5}\\/image\\/.*\\.[a-z|A-Z]{1,5}\\s"
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

    fun setCurrentUser(lUser: LUser){
        _currentUser.value=lUser
    }
    //该函数的作用是分析文本内容，如果有图片的话就创建出一个ImageView，其余的都用textview
    fun setContent():ArrayList<RangeAndValue>{
        //正则表达式的格式为：[图片]http://ip:port/image/xxx.xxx space
        //正则表达式2格式[图片]image/picture/xxx.xxx space
//        val regex=Regex("\\[\\u56FE\\u7247\\](http):\\/\\/(([0-9]{1,3}\\.)+[0-9]{1,3}\\:[0-9]{2,5}\\/image\\/[a-z|A-Z|0-9]+\\.[a-z|A-Z]+\\b)")
        val regex=Regex("\\[\\u56FE\\u7247\\]images\\/picture\\/[a-z|A-Z|0-9|_]+\\.[a-z|A-Z]+\\b")
//        val string="[图片]http://10.20.92.222:8082/image/12.jpg"
//        Log.d("Hekai", "setContent: ${string.matches(regex)}")
        var result=ArrayList<RangeAndValue>()
        data.value?.Content?.let {
            regex.findAll(it).forEach {
                val rangeAndValue=RangeAndValue(startIndex = it.range.start,
                    endIndex = it.range.last,
                    //从4开始截取是为了不要 [图片] 这几个字符
                    url = myBaseURL()+it.value.substring(4,it.value.length))
                result.add(rangeAndValue)
            }
        }
        Log.d("Hekai", "setContent: ${result}")
        return result

    }
}