package com.hekai.androidproject.otheractivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.hekai.androidproject.R
import com.hekai.androidproject.adapters.bindImageFromUrl
import com.hekai.androidproject.databinding.ActivityContentBinding
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.util.myBaseURL
import com.hekai.androidproject.viewmodels.activityviewmodels.ContentActivityViewModel

class ContentActivity : AppCompatActivity() {
    private val TAG:String="Hekai"
    private lateinit var binding:ActivityContentBinding
    private val viewModel: ContentActivityViewModel by lazy {
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

        if(intent.extras?.get("currentUser")!=null){
            viewModel.setCurrentUser(intent.extras?.get("currentUser") as LUser)
            bindImageFromUrl(binding.currentUserImage, myBaseURL() +viewModel.currentUser.value?.UserAvatar)
            Log.d(TAG, "设置图片: ${viewModel.currentUser.value?.UserAvatar}")
        }else{
            binding.currentUserImage.setImageResource(R.drawable.ic_baseline_check_24)
        }
//        viewModel.currentUser.observe(this){
//            if(it.UserAvatar!=""){
//                bindImageFromUrl(binding.currentUserImage,viewModel.currentUser.value?.UserAvatar)
//            }else{
//                binding.currentUserImage.setImageResource(R.drawable.ic_baseline_person_outline_24)
//            }
//        }
        viewModel.setPost(intent.extras?.get("obj") as Posts)
        var cid:Int= intent.extras?.get("UID") as Int
        viewModel.getContentById(cid)
        binding.contentviewModel=viewModel
        viewModel.data.observe(this){
            constructingContent()
        }
    }

    private fun constructingContent(){
        val array=viewModel.setContent()
        if(array.size==0){
            val textView: TextView = TextView(applicationContext)
            textView.text=viewModel.data.value?.Content
            binding.mainContentLayout.addView(textView)
        }
        var mytext:String=""
        var index=0;
        var i=0
        while(i<viewModel.data.value?.Content?.length ?: 0){
            if(index>=array.size){
                break
            }
            if(i<array[index].startIndex){
                mytext+= viewModel.data.value?.Content?.get(i) ?: ""
            }else{
                if(mytext!="") {
                    val textView: TextView = TextView(applicationContext)
                    textView.text = mytext
                    mytext = ""
                    binding.mainContentLayout.addView(textView)
                }
                val imageView:ImageView=ImageView(applicationContext)
                bindImageFromUrl(imageView,array[index].url)
                binding.mainContentLayout.addView(imageView)
                i=array[index].endIndex+1
                index++;
            }
            i++
        }
    }
}