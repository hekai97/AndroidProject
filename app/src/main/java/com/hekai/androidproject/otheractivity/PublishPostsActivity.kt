package com.hekai.androidproject.otheractivity

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.setMargins
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hekai.androidproject.R
import com.hekai.androidproject.databinding.ActivityPublishPostsBinding
import com.hekai.androidproject.entites.Contents
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.viewmodels.activityviewmodels.PublishPostsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.sql.Timestamp

class PublishPostsActivity : AppCompatActivity() {
    private val TAG="Hekai"
    private lateinit var binding:ActivityPublishPostsBinding
    private lateinit var viewModel:PublishPostsViewModel
    private var editTextArray=ArrayList<EditText>()
    private var imageViewArray=ArrayList<ImageView>()
    private var imageUriArray=ArrayList<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPublishPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this)[PublishPostsViewModel::class.java]
        //获取当前的用户id
        if(intent.extras?.get("currentUser")!=null) {
            viewModel.setCurrentUser(intent.extras?.get("currentUser") as LUser)
        }
        viewModel.currentUser.observe(this){
            Log.d(TAG, "onCreate: ${viewModel.currentUser.value}")
        }
        setSupportActionBar(binding.toolbarInPublish)
        binding.toolbarInPublish.title="写文章"
        binding.toolbarInPublish.setNavigationOnClickListener {
            this.finish()
        }
        binding.photoButtonInPublish.apply {
            this.setOnClickListener {
                checkExternalStoragePermissionAndOpenGallery()
            }
        }
        //TODO 发布的时候要能生成图片和文本，现在使用edittext只能上传文本
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.publish_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.publish_sketch -> {
                Log.d(TAG, "onOptionsItemSelected: 点击了草稿")
                true
            }
            R.id.publish_post -> {
                Log.d(TAG, "onOptionsItemSelected: 点击了发布${haveMainContent()}")
                publish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    fun requestPermission(){
//
//    }

    private fun checkExternalStoragePermissionAndOpenGallery(){
        when {
            ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getResult.launch("image/*")
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Snackbar.make(binding.root,"同意文件读取权限",Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(binding.root,"请打开文件读取权限",Snackbar.LENGTH_LONG).show()
            }
        }
    val getResult=registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it!=null){
            createImageViewByUri(it)
            createEditText()
            //这块放在最后按钮提交的部分
//            val contentResolver=binding.root.context.contentResolver
//            val typeOfUri:String=contentResolver.getType(it)!!
//            Log.d(TAG, "${getFileFromUri(it)?.name}")
//            val file=getFileFromUri(it)
//            file?.let {
//                val requestBody:RequestBody= RequestBody.create(MediaType.parse(typeOfUri),it)
//                val imageToUpload:MultipartBody.Part=MultipartBody.Part.createFormData("image",file.name,requestBody)
//                viewModel.uploadImage(imageToUpload)
//            }

            //TODO 获取uri之后将其转换成文件，上传到springboot中
//            createImageViewByUri(it)
//            createEditText()
        }
    }

//    private fun createFileFromUri(uri: Uri):File{
//        val file=File("test1.jpg")
//        val inputstream: InputStream? = contentResolver.openInputStream(uri)
//        val out: OutputStream = FileOutputStream(file)
//        val buf = ByteArray(1024)
//        var len: Int
//        if (inputstream != null) {
//            while (inputstream.read(buf).also { len = it } > 0) {
//                out.write(buf, 0, len)
//            }
//        }
//        out.close()
//        inputstream?.close()
//        return file
//    }
    //从uri中创建图片
    private fun getFileFromUri(uri: Uri): File? {
        if (uri.path == null) {
            return null
        }
        var realPath = String()
        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (uri.path!!.contains("/document/image:")) {
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else {
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val column = "_data"
            val projection = arrayOf(column)
            val cursor = binding.root.context.contentResolver.query(
                databaseUri,
                projection,
                selection,
                selectionArgs,
                null
            )
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    realPath = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.i("GetFileUri Exception:", e.message ?: "")
        }
        val path = if (realPath.isNotEmpty()) realPath else {
            when {
                uri.path!!.contains("/document/raw:") -> uri.path!!.replace(
                    "/document/raw:",
                    ""
                )
                uri.path!!.contains("/document/primary:") -> uri.path!!.replace(
                    "/document/primary:",
                    "/storage/emulated/0/"
                )
                else -> return null
            }
        }
        return File(path)
    }

    private fun createEditText(){
        val margin= resources.getDimension(R.dimen.text_to_border).toInt()
        val editText=EditText(binding.root.context)
        val editTextParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        editTextParams.setMargins(margin,margin,margin,0)
        editText.layoutParams=editTextParams
        editText.background=null
        editTextArray.add(editText)
        binding.contentInPublish.addView(editText)
    }
    private fun createImageViewByUri(uri: Uri){
        val margin= resources.getDimension(R.dimen.text_to_border).toInt()
        if(binding.mainTextEdittextInPublish.text.toString()==""){
            binding.mainTextEdittextInPublish.hint= " "
        }
        val imageView=ImageView(binding.root.context)
        val deleteImageButton=ImageButton(binding.root.context)

        //设置imageview
        val imageViewParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        imageViewParams.setMargins(margin,margin,margin,0)
        imageView.layoutParams=imageViewParams
        imageView.setImageURI(uri)
        imageView.scaleType=ImageView.ScaleType.CENTER_INSIDE
        imageView.adjustViewBounds=true

        //设置deleteButton
        deleteImageButton.apply {
            setImageResource(R.drawable.ic_baseline_clear_24)
            background=null
            val deleteButtonLayoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            deleteButtonLayoutParams.setMargins(margin,margin,0,0)
            layoutParams=deleteButtonLayoutParams
            setOnClickListener {
                imageView.isGone=true
                //该处保证删除的话也会将下面的edittext删除，而且将它里面的文本挪到上一行去,加上换行符
                val index=imageUriArray.indexOf(uri)
                imageUriArray.remove(uri)
                if(index==0){
                    Log.d(TAG, "createImageViewByUri: index=0")
                    binding.mainTextEdittextInPublish.text.append("\n${editTextArray[index].text}")
                }else{
                    Log.d(TAG, "createImageViewByUri: index=${index}")
                    editTextArray[index-1].text.append("\n"+editTextArray[index].text)
                }
                editTextArray[index].isGone=true
                editTextArray.removeAt(index)
                this.isGone=true
            }
        }
        //uri添加到数组中
        imageUriArray.add(uri)
        //添加到linearLayout
        binding.contentInPublish.addView(imageView)
        binding.contentInPublish.addView(deleteImageButton)
    }
    private fun putAllImageToRemote(){
        for(uri in imageUriArray){
            val contentResolver=binding.root.context.contentResolver
            val typeOfUri:String=contentResolver.getType(uri)!!
            Log.d(TAG, "正在插入${getFileFromUri(uri)?.name}")
            val file=getFileFromUri(uri)
            file?.let {
                val requestBody:RequestBody= RequestBody.create(MediaType.parse(typeOfUri),it)
                //重置文件名为 用户id+第几个帖子+图片名
                val fileName=viewModel.currentUser.value?.uid.toString()+"_"+(viewModel.currentUser.value?.PublishNumber!!+1).toString()+"_"+file.name
                Log.d(TAG, "putAllImageToRemote: ${fileName}")
                val imageToUpload:MultipartBody.Part=MultipartBody.Part.createFormData("image",fileName,requestBody)
                viewModel.uploadImage(imageToUpload)
            }
        }
    }
    private fun putPostToRemote(){
        var title=binding.titleEdittextInPublish.text.toString()
        var postContent=binding.mainTextEdittextInPublish.text.toString()
        var index=0
        //这里的处理逻辑是
        //每次增加一个imageView必定增加一个editText，每次删除imageview也是必定删除它下面的
        //因此，对于每一个textView来说，它的上面下面如果没有图片的话，就代表这个edittext已经是最底部的一个了
        //
        for(t in editTextArray){
            if(t.text!=null){
                postContent+=t.text.toString()
            }
            if(imageUriArray.isNotEmpty()){
                postContent+="[图片]images/picture/"+viewModel.currentUser.value?.uid.toString()+"_"+(viewModel.currentUser.value?.PublishNumber!!+1).toString()+"_"+getFileFromUri(imageUriArray[index])!!.name+" "
            }
            index++
        }
        val des:String
        if(postContent.length>100){
            des=postContent.substring(0,100)
        }
        else{
            des=postContent
        }
        val time = System.currentTimeMillis()
        val tsTemp = Timestamp(time)
        val ts: String = tsTemp.toString()
        val post=Posts(
            null,
            viewModel.currentUser.value?.uid,
            null,
            viewModel.currentUser.value?.NickName!!,
            viewModel.currentUser.value?.UserAvatar!!,
            title,
            des,
            ts,
            0,
            ts,
            0,
            0
        )
        val contents=Contents(null,postContent)
        runBlocking {
            contents.cid=viewModel.insertPosts(post)
            Log.d(TAG, "putPostToRemote: ${contents}")
            viewModel.insertToRemoteContent(contents)
        }

    }
    private fun haveMainContent():Boolean{
        if(binding.mainTextEdittextInPublish.text.toString()!=""){
            return true
        }
        for(t in editTextArray){
            if(t.text.toString()!=""){
                return true
            }
        }
        return false
    }
    fun publish(){
        if(binding.titleEdittextInPublish.text.toString()==""){
            Snackbar.make(binding.root,"请输入标题",Snackbar.LENGTH_LONG).show()
            return
        }
        if(!haveMainContent()){
            Snackbar.make(binding.root,"请输入正文",Snackbar.LENGTH_LONG).show()
            return
        }
        putAllImageToRemote()
        putPostToRemote()
        finish()
    }
}