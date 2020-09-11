package com.app.armygyan.quizz.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.quizz.model.QuizDetail
import kotlinx.android.synthetic.main.list_item_quiz_category.view.*

class QuizCategoryAdapter : PagedListAdapter<QuizDetail,QuizCategoryAdapter.MiiiViewHolder>(ITEM_COMPARATOR) {
    private var mItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiiiViewHolder {
        return MiiiViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_quiz_category,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MiiiViewHolder, pos: Int) {
        var item=getItem(pos)
        val movieViewHolder = holder
        item?.let { movieViewHolder.bindView(it) }
        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(item)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(quiz: QuizDetail?)
    }

    public fun setOnItemClickListener(mItemClickListener: OnItemClickListener){
        this.mItemClickListener=mItemClickListener;

    }
    class MiiiViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: QuizDetail) {
            val titleTopics = itemView.context.resources.getString(R.string.title_topics)
            val titleViews = itemView.context.resources.getString(R.string.title_views)
            val titleFree = itemView.context.resources.getString(R.string.title_free)
            val titleQues = itemView.context.getString(R.string.question)
            val totMins = itemView.context.getString(R.string.minutes)
            itemView.tv_quiz_name?.text = model.name
            itemView.tv_topics?.text =titleTopics.plus(" ").plus(":").plus(model.topics)
            itemView.tv_view_count?.text =model.viewCount.plus(" ").plus(titleViews)
          //  val qCount = String.format("%02", model.quesCount)
            itemView.tv_ques_count?.text =model.quesCount.plus(" ").plus(titleQues)
            itemView.tv_quiz_type?.setBackgroundResource(R.color.colorGreen)
            itemView.tv_quiz_type?.text=titleFree
//            if(pos%4==0){
//                itemView.tv_quiz_type?.setBackgroundResource(R.color.colorYellow)
//                itemView.tv_quiz_type?.text="UNLOCK"
//            }else {
//                itemView.tv_quiz_type?.setBackgroundResource(R.color.colorGreen)
//                itemView.tv_quiz_type?.text="FREE"
//            }

            itemView.tv_quiz_duration.text=model.duration.plus(" ").plus(totMins)
        }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<QuizDetail>() {
            override fun areItemsTheSame(
                oldItem: QuizDetail,
                newItem: QuizDetail
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: QuizDetail,
                newItem: QuizDetail
            ): Boolean =
                newItem == oldItem
        }
    }
}

