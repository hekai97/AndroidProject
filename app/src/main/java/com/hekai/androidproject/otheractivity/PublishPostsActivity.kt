package com.hekai.androidproject.otheractivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hekai.androidproject.databinding.ActivityPublishPostsBinding

class PublishPostsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPublishPostsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPublishPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO 发布的时候要能生成图片和文本，现在使用edittext只能上传文本
    }
    private fun createEditText(){

    }
    private fun createImageView(){

    }
}