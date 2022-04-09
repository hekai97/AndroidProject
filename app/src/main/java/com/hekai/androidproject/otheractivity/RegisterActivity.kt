package com.hekai.androidproject.otheractivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hekai.androidproject.R
import com.hekai.androidproject.databinding.ActivityRegisterBinding
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.util.myhash
import com.hekai.androidproject.viewmodels.activityviewmodels.RegisterActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class RegisterActivity : AppCompatActivity() {
    private val emailRegex=Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private val phoneNumberRegex=Regex("^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}\$")
    private val TAG="Hekai"
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var viewModel:RegisterActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this)[RegisterActivityViewModel::class.java]
        //给按钮加监听
        binding.registerButtonInRegister.apply {
            this.setOnClickListener {
                registerButtonListener(it)
            }
        }
        //给电子邮件加监听
        binding.userEmailInRegister.apply {
            this.doAfterTextChanged {
                if(!emailRegex.matches(it.toString())){
                    this.error="邮箱格式错误"
                }else{
                    runBlocking {
                        if(viewModel.isUserExist(binding.userEmailInRegister.text.toString()).await()){
                            binding.userEmailInRegister.error="该邮箱号已经存在"
                            Log.d(TAG, "registerButtonListener: 用户存在")
                        }
                    }
                }
            }
        }
        //给昵称加监听
        binding.userNickNameInRegister.apply {
            this.doAfterTextChanged {
                if(it.toString().length<5){
                    this.error="用户名必须大于5个字符"
                }
            }
        }
        //用户手机号加监听
        binding.userPhoneNumberInRegister.apply {
            this.doAfterTextChanged {
                if(!phoneNumberRegex.matches(it.toString())){
                    this.error="请输入正确的手机号"
                }
            }
        }
        //用户密码加监听
        binding.userPasswordInRegister.apply {
            this.doAfterTextChanged {
                if(it.toString().length<8){
                    this.error="密码长度必须>=8位"
                }
            }
        }
        //用户重复密码框加监听
        binding.userPasswordAgainInRegister.apply {
            this.doAfterTextChanged {
                if(!it.toString().equals(binding.userPasswordInRegister.text.toString())){
                    this.error="两次密码不一致"
                }else{
                    Log.d(TAG, "密码相等")
                }
            }
        }
    }

    private fun registerButtonListener(view: View){
        //这块是用来检测这几个输入框是不是没有错误值了，如果没有错误值便可以插入数据库
        if(binding.userEmailInRegister.error==null
            &&binding.userNickNameInRegister.error==null
            &&binding.userPasswordInRegister.error==null
            &&binding.userPasswordAgainInRegister.error==null
            &&binding.userPhoneNumberInRegister.error==null) {
            val users = Users(
                null,
                binding.userEmailInRegister.text.toString(),
                binding.userNickNameInRegister.text.toString(),
                null,
                myhash(binding.userPasswordInRegister.text.toString()),
                0,
                binding.userPhoneNumberInRegister.text.toString(),
                null
            )
            viewModel.insertUser(users)
            Snackbar.make(binding.root,"注册成功！",Snackbar.LENGTH_LONG).show()
            runBlocking { delay(1000) }
            this.finish()
        }else{
            Snackbar.make(binding.root,"请检查输入的信息！",Snackbar.LENGTH_LONG).show()
        }
//        if(viewModel.isUserExist(binding.userEmailInRegister.text.toString())){
//            Log.d(TAG, "registerButtonListener: 用户存在")
//        }else {
//            Log.d(TAG, "registerButtonListener: 用户不存在")
//        }
//        viewModel.userExist.observe(this){
//            if(it){
//                Log.d(TAG, "registerButtonListener: 用户已存在")
//            }else{
//                val users=Users(
//                    null,
//                    binding.userEmailInRegister.text.toString(),
//                    binding.userNickNameInRegister.text.toString(),
//                    null,
//                    myhash(binding.userPasswordInRegister.text.toString()),
//                    null,
//                    binding.userPhoneNumberInRegister.text.toString(),
//                    null
//                )
//                viewModel.insertToRemoteDB(users)
//                viewModel.insertStatus.observe(this){
//                    if(it){
//                        Log.d(TAG, "registerButtonListener: 插入成功")
//                    }
//                    else{
//                        Log.d(TAG, "registerButtonListener: 插入失败")
//                    }
//                }
//            }
//        }
    }
}