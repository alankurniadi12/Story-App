package com.alankurniadi.storyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.alankurniadi.storyapp.model.ListStoryItem

class StoryDiffCallback(
    private val mOldStoryList: List<ListStoryItem>,
    private val mNewStoryList: List<ListStoryItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldStoryList.size

    override fun getNewListSize(): Int = mNewStoryList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldStoryList[oldItemPosition]
        val newEmployee = mNewStoryList[newItemPosition]
        return oldEmployee.name == newEmployee.name && oldEmployee.photoUrl == newEmployee.photoUrl
    }
}
