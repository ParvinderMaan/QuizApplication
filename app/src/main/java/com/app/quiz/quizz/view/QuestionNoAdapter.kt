package com.app.quiz.quizz.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.quizz.model.QuestionSet
import com.app.quiz.R
import kotlinx.android.synthetic.main.list_item_question_no.view.*

class QuestionNoAdapter : RecyclerView.Adapter<QuestionNoAdapter.QuestionNoViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<QuestionSet> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionNoViewHolder {
        return QuestionNoViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_question_no, parent, false))
    }


    override fun onBindViewHolder(holder: QuestionNoViewHolder, pos: Int) {
        var item = items.get(pos)
        val viewHolder = holder
        viewHolder.bindView(item, pos)
        // listener
        viewHolder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(viewHolder.adapterPosition)
            items.forEach { it.isQuesAttempted=false  }
            item.isQuesAttempted=true // Note : this is flag used for icon visibility
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int);
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    fun add(item: QuestionSet) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(items: List<QuestionSet>) {
        for (item in items) {
            add(item)
        }
    }

    fun setItemIndex(item:QuestionSet,visiblePos: Int) {
        items.forEach { it.isQuesAttempted=false }
        item.isQuesAttempted=true // Note : this is flag used for icon visibility
        items.set(visiblePos,item)
        notifyDataSetChanged()
    }

    class QuestionNoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: QuestionSet, pos: Int) {
            // views
            itemView.tv_ques_no?.text = ("").plus(adapterPosition+1)
            if(model.isQuesAttempted){
                itemView.iv_ques_no_pointer?.setBackgroundResource(R.color.colorGreen)
            }else{
                itemView.iv_ques_no_pointer?.setBackgroundResource(0)
            }
        }
    }
    // region Helper Methods
    fun getItem(position: Int): QuestionSet {
        return items[position]
    }
}
