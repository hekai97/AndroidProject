package com.hekai.androidproject.otheractivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.hekai.androidproject.R
import com.hekai.androidproject.databinding.ActivityContentBinding
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.viewmodels.ContentActivityViewModel

class ContentActivity : AppCompatActivity() {
    private val TAG:String="Hekai"
    private lateinit var binding:ActivityContentBinding
    private val viewModel:ContentActivityViewModel by lazy {
        ViewModelProvider(this)[ContentActivityViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityContentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        setContentView(binding.root)
        //设置左上角的返回箭头按键
        binding.toolbar.setNavigationOnClickListener{
            this.finish()
        }
        Log.d(TAG, "onCreate: ${intent.extras?.get("obj")}")

        viewModel.setPost(intent.extras?.get("obj") as Posts)
        var cid:Int= intent.extras?.get("UID") as Int
        viewModel.getContentById(cid)
        binding.contentviewModel=viewModel
    }
}