package com.hekai.androidproject

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hekai.androidproject.databinding.ActivityMainBinding
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.viewmodels.MainActivityViewModel
import com.hekai.androidproject.viewmodels.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel:MainActivityViewModel by viewModels {
        MainActivityViewModelFactory((application as MainApplicaton).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includedAppBarLayout.toolbar)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.bottom_fragment_container_view) as NavHostFragment
        navController=navHostFragment.navController
        binding.includedAppBarLayout.bottomNavigationView.setupWithNavController(navController)
        checkLoginUser()
        viewModel.currentUser.observe(this){
            Log.d("Hekai", "liveDataInDatabases: ${it}")
        }
    }
    private fun checkLoginUser(){
        val user:LUser=LUser(1,"1731673423","K999","image/12.jpg","hk19990707",0,"18309346278","hekaigs@gmail.com",true)
        viewModel.insert(user)
    }
}