package com.hekai.androidproject.adapters

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.hekai.androidproject.databinding.RecyclerViewItemBinding
import com.hekai.androidproject.databinding.ReplyRecycleViewItemBinding
import com.hekai.androidproject.entites.ReconstructionResponse
import com.hekai.androidproject.entites.Responses
import com.hekai.androidproject.otheractivity.ContentActivity

/*TODO 这里的adapter可以传进来一个replyList列表加一个用户列表
 * 或者换种方式，新建一个对象，这个对象中只有所需要的参数，不过也需要上述两个进行合并构造
 * */

class ReplyRecycleViewAdapter(val replyList: LiveData<List<ReconstructionResponse>>,activity: ContentActivity)
    :RecyclerView.Adapter<ReplyRecycleViewAdapter.ReplyRecycleViewHolder>() {
    private val datas:LiveData<List<ReconstructionResponse>> = replyList
    private val contentActivity=activity
    class ReplyRecycleViewHolder(replyRecycleViewItemBinding:ReplyRecycleViewItemBinding):RecyclerView.ViewHolder(replyRecycleViewItemBinding.root){
        val binding = replyRecycleViewItemBinding

        fun bindToView(reconstructionResponse: ReconstructionResponse){
            binding.reconstructionResponse=reconstructionResponse
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyRecycleViewHolder {
        val recycleViewItemBinding=ReplyRecycleViewItemBinding.inflate(contentActivity.layoutInflater,parent,false)
        return ReplyRecycleViewHolder(recycleViewItemBinding)
    }

    override fun onBindViewHolder(holder: ReplyRecycleViewHolder, position: Int) {
        datas.value?.let { holder.bindToView(it[position]) }
    }

    override fun getItemCount(): Int {
        return datas.value?.size ?: 0
    }
}