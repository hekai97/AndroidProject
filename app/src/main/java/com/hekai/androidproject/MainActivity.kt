package com.hekai.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.hekai.androidproject.databinding.ActivityMainBinding
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.otheractivity.LoginActivity
import com.hekai.androidproject.otheractivity.PublishPostsActivity
import com.hekai.androidproject.util.myBaseURL
import com.hekai.androidproject.viewmodels.activityviewmodels.MainActivityViewModel
import com.hekai.androidproject.viewmodels.activityviewmodels.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {
    private val TAG="Hekai"
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainActivityViewModel by viewModels {
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

        viewModel.currentUser.observe(this){
            Log.d(TAG, "liveDataInDatabases: ${it}")
        }
        Log.d(TAG, "liveDataInDatabases: ${viewModel.currentUser}")
        binding.includedAppBarLayout.fab.setOnClickListener {
            if(viewModel.currentUser.value==null){
                Snackbar.make(binding.root,"请先登录",Snackbar.LENGTH_LONG).show()
            }else {
                val currentUserIntent=Intent(binding.root.context,PublishPostsActivity::class.java)
                currentUserIntent.putExtra("currentUser",viewModel.currentUser.value)
                startActivity(currentUserIntent)
            }
        }
    }

    val getResult=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== RESULT_OK){
            Log.d(TAG, "登录成功")
            val remoteUser= it.data?.extras?.get("user") as Users
            val user=LUser(
                remoteUser.uid!!,
                remoteUser.UserName!!,
                remoteUser.NickName!!,
                remoteUser.UserAvatar!!,
                remoteUser.Password!!,
                remoteUser.PublishNumber!!,
                remoteUser.PhoneNumber!!,
                remoteUser.Email!!,
                true
            )
            Log.d(TAG, "get: ${user}")
            viewModel.insert(user)
        }
        else{
            Log.d(TAG, "取消登录")
        }
    }

    fun getActivityBinding():ActivityMainBinding{
        return binding
    }
    fun getActivityViewModel(): MainActivityViewModel {
        return viewModel
    }
    fun openLoginActivity(){
        val intent=Intent(applicationContext,LoginActivity::class.java)
        getResult.launch(intent)
    }
    fun setToolbarTitle(s:String){
        supportActionBar?.title=s
    }
}