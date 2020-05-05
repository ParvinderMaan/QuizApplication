package com.app.quiz.studymaterial.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.R
import com.app.quiz.studymaterial.model.StudyMaterialChapter
import kotlinx.android.synthetic.main.list_item_chapter.view.*
import kotlinx.android.synthetic.main.list_item_chapter.view.tv_created_on
import kotlinx.android.synthetic.main.list_item_chapter.view.tv_topics
import kotlinx.android.synthetic.main.list_item_quiz_category.view.*
import kotlin.collections.ArrayList

class ChapterAdapter : PagedListAdapter<StudyMaterialChapter, ChapterAdapter.MiViewHolder>(ITEM_COMPARATOR) {
    private var mItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick()
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
        val viewHolder = holder as MiViewHolder
        item?.let { viewHolder.bindView(it, pos) }
        holder.itemView.setOnClickListener {

            mItemClickListener?.onItemClick()
        }
    }

    class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: StudyMaterialChapter, pos: Int) {
            itemView.tv_chapter_name?.text = model.chapterName
            itemView.tv_topics?.text = model.topics
            itemView.tv_created_on?.text = model.createdOn
           // tv_chapter_status
           //tv_view_count
            if (pos % 4 == 0) {
                itemView.tv_quiz_type?.setBackgroundResource(R.color.colorYellow)
                itemView.tv_quiz_type?.text = "NEW"
            } else {
//                itemView.tv_quiz_type?.setBackgroundResource(R.color.colorGreen)
//                itemView.tv_quiz_type?.text="FREE"
                itemView.tv_quiz_type?.visibility = View.INVISIBLE
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

