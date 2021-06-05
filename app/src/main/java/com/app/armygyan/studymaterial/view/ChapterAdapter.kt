package com.app.armygyan.studymaterial.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.databinding.ListItemChapterBinding
import com.app.armygyan.helper.TimeUtil
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class ChapterAdapter(var currentTime: Date) : PagedListAdapter<StudyMaterialChapter, ChapterAdapter.MiViewHolder>(ITEM_COMPARATOR) {
    private var mItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: StudyMaterialChapter?)
    }

    public fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener;

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val binding = ListItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MiViewHolder, pos: Int) {
        val item = getItem(pos)
        if (item != null)
            holder.bindView(item)
    }

    inner class MiViewHolder(val binder: ListItemChapterBinding) : RecyclerView.ViewHolder(binder.root) {
        fun bindView(model: StudyMaterialChapter) {
            val chapterName = model.chapterName[0].toUpperCase().plus(model.chapterName.substring(1))
            binder.tvChapterName.text = chapterName
            binder.tvTopics.text = model.topics
            binder.tvCreatedOn.text = model.createdOn.split(" ")[0]
            binder.tvChapterViewCount.text =model.viewCount.plus(" ").plus(this.itemView.context.getString(R.string.title_views))

                val createdOn = TimeUtil.utcToLocal(model.createdOn)
                val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createdOn)
                val diffInMilli = abs(currentTime.time - date1.time) /1000
                val days = TimeUnit.SECONDS.toDays(diffInMilli).toInt()
                if(days<=7){
                    binder.tvChapterStatus.setBackgroundResource(R.color.colorYellow)
                    binder.tvChapterStatus.text = binder.root.context.getString(R.string.title_new)
                }else{
                    binder.tvChapterStatus.visibility = View.INVISIBLE
                }

            binder.root.setOnClickListener {
                mItemClickListener?.onItemClick(model)
            }

        }


    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<StudyMaterialChapter>() {
            override fun areItemsTheSame(
                oldItem: StudyMaterialChapter,
                newItem: StudyMaterialChapter
            ): Boolean =
                oldItem.chapterId == newItem.chapterId

            override fun areContentsTheSame(
                oldItem: StudyMaterialChapter,
                newItem: StudyMaterialChapter
            ): Boolean =
                newItem == oldItem
        }
    }


}

