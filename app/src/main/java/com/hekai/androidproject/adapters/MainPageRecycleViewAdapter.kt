package com.hekai.androidproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.hekai.androidproject.databinding.RecyclerViewItemBinding
import com.hekai.androidproject.entites.Posts

class MainPageRecycleViewAdapter(data: LiveData<List<Posts>>,inflater: LayoutInflater):
    RecyclerView.Adapter<MainPageRecycleViewAdapter.MainpageViewHolder>() {
    private val myInflater:LayoutInflater=inflater
    private val datas:LiveData<List<Posts>> = data
    class MainpageViewHolder(recyclerViewItemBinding: RecyclerViewItemBinding):RecyclerView.ViewHolder(recyclerViewItemBinding.root){
        private val binding=recyclerViewItemBinding
        fun bindDataToViewHolder(posts: Posts){
            if(!startWithURL(posts.PublishUserAvatar)){
                posts.PublishUserAvatar="http://10.20.92.222:8082/"+posts.PublishUserAvatar
            }
            Log.d("Hekai", "bindDataToViewHolder: ${posts.PublishUserAvatar}")
            binding.post=posts
        }
        fun startWithURL(avatar:String):Boolean{
            if(avatar.substring(0,4).equals("http")){
                return true
            }
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainpageViewHolder {
        val itemBinding:RecyclerViewItemBinding= RecyclerViewItemBinding.inflate(myInflater,parent,false)
        return MainpageViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MainpageViewHolder, position: Int) {
        datas.value?.let { holder.bindDataToViewHolder(it.get(position)) }
    }

    override fun getItemCount(): Int {
        return datas.value?.size?:0
    }
}