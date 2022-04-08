package com.hekai.androidproject.otheractivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hekai.androidproject.R
import com.hekai.androidproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}