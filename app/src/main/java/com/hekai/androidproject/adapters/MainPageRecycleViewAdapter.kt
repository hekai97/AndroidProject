package com.hekai.androidproject.adapters

import android.content.Intent
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.hekai.androidproject.MainActivity
import com.hekai.androidproject.databinding.RecyclerViewItemBinding
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.otheractivity.ContentActivity
import com.hekai.androidproject.util.myBaseURL

class MainPageRecycleViewAdapter(data: LiveData<List<Posts>>, activity:MainActivity):
    RecyclerView.Adapter<MainPageRecycleViewAdapter.MainPageViewHolder>() {
//    private val myInflater:LayoutInflater=inflater
    private val datas:LiveData<List<Posts>> = data
    private val myActivity=activity
    class MainPageViewHolder(recyclerViewItemBinding: RecyclerViewItemBinding,activity: MainActivity):RecyclerView.ViewHolder(recyclerViewItemBinding.root){
        private val binding=recyclerViewItemBinding
        private val myActivityInViewHolder=activity
        fun bindDataToViewHolder(posts: Posts){
            if(!startWithURL(posts.PublishUserAvatar)){
                posts.PublishUserAvatar= myBaseURL()+posts.PublishUserAvatar
            }
            binding.post=posts
            binding.postItem.setOnClickListener {
                val intent=Intent(myActivityInViewHolder.applicationContext,ContentActivity::class.java)
                intent.putExtra("UID",posts.pid)
                intent.putExtra("obj",posts)
                myActivityInViewHolder.startActivity(intent)
            }
        }
        private fun startWithURL(avatar:String):Boolean{
            return avatar.substring(0,4) == "http"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPageViewHolder {
        val itemBinding:RecyclerViewItemBinding= RecyclerViewItemBinding.inflate(myActivity.layoutInflater,parent,false)
        return MainPageViewHolder(itemBinding,myActivity)
    }

    override fun onBindViewHolder(holder: MainPageViewHolder, position: Int) {
        datas.value?.let { holder.bindDataToViewHolder(it.get(position)) }
    }

    override fun getItemCount(): Int {
        return datas.value?.size?:0
    }
}