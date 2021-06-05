package com.app.armygyan.quizz.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.databinding.ListItemQuizCategoryBinding
import com.app.armygyan.quizz.model.QuizDetail

class QuizCategoryAdapter : PagedListAdapter<QuizDetail,QuizCategoryAdapter.MiiiViewHolder>(ITEM_COMPARATOR) {
    private var mItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiiiViewHolder {
        val binding = ListItemQuizCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MiiiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MiiiViewHolder, pos: Int) {
        val item=getItem(pos)
        item?.let { holder.bindView(it) }
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
    class MiiiViewHolder (val binder: ListItemQuizCategoryBinding) : RecyclerView.ViewHolder(binder.root) {
        fun bindView(model: QuizDetail) {
            val titleTopics = itemView.context.resources.getString(R.string.title_topics)
            val titleViews = itemView.context.resources.getString(R.string.title_views)
            val titleFree = itemView.context.resources.getString(R.string.title_free)
            val titleQues = itemView.context.getString(R.string.question)
            val totMins = itemView.context.getString(R.string.minutes)
            binder.tvQuizName.text = model.name
            binder.tvTopics.text =titleTopics.plus(" ").plus(":").plus(model.topics)
            binder.tvViewCount.text =model.viewCount.plus(" ").plus(titleViews)
            binder.tvQuesCount.text =model.quesCount.plus(" ").plus(titleQues)
            binder.tvQuizType.setBackgroundResource(R.color.colorGreen)
            binder.tvQuizType.text=titleFree
/*            if(pos%4==0){
                binder.tv_quiz_type?.setBackgroundResource(R.color.colorYellow)
                binder.tv_quiz_type?.text="UNLOCK"
            }else {
                binder.tv_quiz_type?.setBackgroundResource(R.color.colorGreen)
                binder.tv_quiz_type?.text="FREE"
            }
*/
            binder.tvQuizDuration.text=model.duration.plus(" ").plus(totMins)
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

