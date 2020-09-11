package com.app.armygyan.studymaterial.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.helper.TimeUtil
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import kotlinx.android.synthetic.main.list_item_chapter.view.*
import kotlinx.android.synthetic.main.list_item_chapter.view.tv_created_on
import kotlinx.android.synthetic.main.list_item_chapter.view.tv_topics
import kotlinx.android.synthetic.main.list_item_quiz_category.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ChapterAdapter(var currentTime: Date) : PagedListAdapter<StudyMaterialChapter, ChapterAdapter.MiViewHolder>(ITEM_COMPARATOR) {
    private var mItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: StudyMaterialChapter?)
    }

    public fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener;

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        return MiViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_chapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MiViewHolder, pos: Int) {
        var item = getItem(pos)
        val viewHolder = holder
        item?.let { viewHolder.bindView(it)
            val createdOn = TimeUtil.utcToLocal(item.createdOn)
            val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createdOn)
            val diffInMillies = Math.abs(currentTime.time - date1.time)/1000
            val days = TimeUnit.SECONDS.toDays(diffInMillies).toInt()
          //  Log.e("days:  ",days.toString())
            if(days<=7){
                viewHolder.itemView.tv_chapter_status?.setBackgroundResource(R.color.colorYellow)
                viewHolder.itemView.tv_chapter_status?.text = viewHolder.itemView.context.getString(R.string.title_new)
                }else{
                viewHolder.itemView.tv_chapter_status?.visibility = View.INVISIBLE
            }
        }
        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(item)
        }

    }

    class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: StudyMaterialChapter) {
            val chapterName = model.chapterName[0].toUpperCase().plus(model.chapterName.substring(1))
            itemView.tv_chapter_name?.text = chapterName
            itemView.tv_topics?.text = model.topics
            itemView.tv_created_on?.text = model.createdOn.split(" ")[0]
           // tv_chapter_status
            itemView.tv_chapter_view_count?.text=model.viewCount.plus(" ").plus(this.itemView.context.getString(R.string.title_views))




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

