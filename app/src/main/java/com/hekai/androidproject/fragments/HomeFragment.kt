package com.hekai.androidproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hekai.androidproject.MainActivity
import com.hekai.androidproject.adapters.MainPageRecycleViewAdapter
import com.hekai.androidproject.databinding.BottomHomeFragmentBinding
import com.hekai.androidproject.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: BottomHomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= BottomHomeFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * 参数：viewModel.data    用于在recycleView上呈现的数据条目
         * activity     传入的是MainActivity，用于创建recycleViewItemBinding时候获取inflater
         * 同时，给每个条目添加监听事件，让其点击能够跳转到另一个activity中
         * */
        binding.recyclerView.adapter=MainPageRecycleViewAdapter(viewModel.data,activity as MainActivity)
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getPostList()
            binding.recyclerView.adapter?.notifyDataSetChanged()
            binding.swipeRefreshLayout.isRefreshing=false
        }
    }

}