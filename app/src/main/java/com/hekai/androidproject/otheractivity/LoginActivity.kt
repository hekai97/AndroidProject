package com.hekai.androidproject.otheractivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hekai.androidproject.R
import com.hekai.androidproject.databinding.ActivityLoginBinding
import com.hekai.androidproject.util.myhash
import com.hekai.androidproject.viewmodels.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var viewModel:LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this)[LoginActivityViewModel::class.java]
        binding.loginButtonInLogin.setOnClickListener{
            val username=binding.userNameInLogin.text.toString()
            val password= myhash(binding.passwordInLogin.text.toString())
            viewModel.vaildUser(username,password)
            Log.d("Hekai", "onCreate: ${viewModel.user.value}")
            if(viewModel.user.value?.UserName.equals(username)&&viewModel.user.value?.Password.equals(password)){
                intent.putExtra("user",viewModel.user.value)
                binding.loginButtonInLogin.setImageResource(R.drawable.ic_baseline_check_24)
                viewModel.waitTime()
                viewModel.isTimeLoading.observe(this){
                    this.finish()
                }
            }else if(viewModel.user.value?.Password==null){
                Snackbar.make(binding.root,"密码错误",Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(binding.root,"无此用户",Snackbar.LENGTH_LONG).show()
            }
        }

    }
}