package com.hekai.androidproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.recyclerView.adapter=MainPageRecycleViewAdapter(viewModel.data,layoutInflater)
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getPostList()
            binding.recyclerView.adapter?.notifyDataSetChanged()
            binding.swipeRefreshLayout.isRefreshing=false
        }
    }

}