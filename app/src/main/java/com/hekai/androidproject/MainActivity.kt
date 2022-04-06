package com.hekai.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hekai.androidproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includedAppBarLayout.toolbar)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.bottom_fragment_container_view) as NavHostFragment
        navController=navHostFragment.navController
        binding.includedAppBarLayout.bottomNavigationView.setupWithNavController(navController)
    }
}