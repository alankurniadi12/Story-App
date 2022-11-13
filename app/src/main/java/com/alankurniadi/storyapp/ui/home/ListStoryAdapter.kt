package com.alankurniadi.storyapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.databinding.StoryItemBinding
import com.alankurniadi.storyapp.helper.StoryDiffCallback
import com.alankurniadi.storyapp.model.ListStoryItem
import com.bumptech.glide.Glide

class ListStoryAdapter(private val itemClicked: (viewItem: StoryItemBinding, data: ListStoryItem) -> Unit) :
    RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private var listStory = ArrayList<ListStoryItem>()

    fun setData(data: List<ListStoryItem>) {
        val diffCallback = StoryDiffCallback(this.listStory, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listStory.clear()
        listStory.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = StoryItemBinding.bind(itemView)
        fun bind(data: ListStoryItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .circleCrop()
                    .into(ivItemPhoto)
                tvItemName.text = data.name
                tvItemDesc.text = data.description

                root.setOnClickListener {
                    itemClicked(binding, data)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listStory[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listStory.size
}
