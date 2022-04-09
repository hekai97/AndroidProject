package com.hekai.androidproject.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hekai.androidproject.MainActivity
import com.hekai.androidproject.adapters.bindImageFromUrl
import com.hekai.androidproject.databinding.BottomMineFragmentBinding
import com.hekai.androidproject.util.myBaseURL
import com.hekai.androidproject.viewmodels.MineViewModel

class MineFragment : Fragment() {

    companion object {
        fun newInstance() = MineFragment()
    }

    private lateinit var viewModel: MineViewModel
    private lateinit var binding:BottomMineFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= BottomMineFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[MineViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if((activity as MainActivity).getActivityViewModel().currentUser.value==null){
        (activity as MainActivity).getActivityViewModel().currentUser.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.userPersonInformationLayout.isGone = true
                binding.userNameInMain.setOnClickListener {
                    (activity as MainActivity).openLoginActivity()
                }
            } else {
                binding.userPersonInformationLayout.isGone = false
                bindImageFromUrl(
                    binding.userImageInMine,
                    myBaseURL()+(activity as MainActivity).getActivityViewModel().currentUser.value?.UserAvatar
                )
                binding.userNameInMain.text =
                    (activity as MainActivity).getActivityViewModel().currentUser.value?.NickName
            }
        }
    }

}