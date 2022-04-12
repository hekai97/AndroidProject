package com.hekai.androidproject.otheractivity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hekai.androidproject.R
import com.hekai.androidproject.adapters.ReplyRecycleViewAdapter
import com.hekai.androidproject.adapters.bindImageFromUrl
import com.hekai.androidproject.databinding.ActivityContentBinding
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.entites.Responses
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.util.myBaseURL
import com.hekai.androidproject.viewmodels.activityviewmodels.ContentActivityViewModel
import okio.IOException
import java.sql.Timestamp

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
            binding.currentUserImage.setImageResource(R.drawable.ic_baseline_person_outline_24)
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

        //设置评论区
        Log.d(TAG, "此时的postid=${viewModel.postData.value?.pid} ")
        viewModel.constructionReponses(viewModel.postData.value?.pid!!)
        viewModel.allResponse.observe(this){
            binding.replyAreaRecycleView.adapter=ReplyRecycleViewAdapter(viewModel.allResponse, this)
            binding.replyAreaRecycleView.layoutManager= LinearLayoutManager(binding.root.context)
            binding.noBodyRelpyContent.isGone=true
        }

        //设置发送按钮的按钮声音
        val mediaPlayer:MediaPlayer = MediaPlayer()
        try {
            val assetFileDescriptor_send_sound=assets.openFd("sound/send_response_sound.wav")
            mediaPlayer.setDataSource(assetFileDescriptor_send_sound.fileDescriptor,assetFileDescriptor_send_sound.startOffset,assetFileDescriptor_send_sound.declaredLength)

        }catch (e:IOException){
            e.printStackTrace()
        }
        mediaPlayer.prepare()
        //设置评论发送按钮
        binding.sendImageButtonInContent.apply {
            this.setOnClickListener {
                if(viewModel.currentUser.value==null){
                    Snackbar.make(binding.root,"请登陆后回复",Snackbar.LENGTH_LONG).show()
                }
                else if(binding.responseMessageEdittext.text.toString()==""){
                    Snackbar.make(binding.root,"请输入回复",Snackbar.LENGTH_LONG).show()
                }else{
                    val time = System.currentTimeMillis()
                    val tsTemp = Timestamp(time)
                    val ts: String = tsTemp.toString().substring(0,16)
                    val response=Responses(null,
                        viewModel.currentUser.value!!.uid,
                        viewModel.postData.value!!.pid!!,
                        binding.responseMessageEdittext.text.toString(),
                        ts)
                    viewModel.insertResponse(response)
                    Snackbar.make(binding.root,"回复成功",Snackbar.LENGTH_LONG).show()
                    binding.responseMessageEdittext.text=null
                    viewModel.constructionReponses(viewModel.postData.value!!.pid!!)
                    mediaPlayer.start()
                }
            }
        }

    }

    private fun constructingContent(){
        val array=viewModel.setContent()
        if(array.size==0){
            val textView: TextView = TextView(applicationContext)
            textView.text=viewModel.data.value?.Content
            textView.textSize=20F
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
                    textView.textSize=20F
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