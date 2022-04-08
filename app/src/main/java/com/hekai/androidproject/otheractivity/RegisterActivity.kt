package com.hekai.androidproject.otheractivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hekai.androidproject.R
import com.hekai.androidproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}