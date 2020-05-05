package com.app.quiz.quizz.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.quizz.model.QuestionSet
import com.app.quiz.R
import kotlinx.android.synthetic.main.list_item_question_no.view.*

class QuestionNoAdapter: RecyclerView.Adapter<QuestionNoAdapter.QuestionNoViewHolder> {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<QuestionSet>

    constructor() : super() {
        this.items = ArrayList()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionNoViewHolder {
        return QuestionNoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_question_no,
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: QuestionNoViewHolder, pos: Int) {
        var item = items.get(pos)
        val viewHolder = holder as QuestionNoViewHolder
        viewHolder.bindView(item, pos)
        // listener
        viewHolder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(viewHolder.adapterPosition)
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

    fun addAll(items: MutableList<QuestionSet>) {
        for (item in items) {
            add(item)
        }
    }

    class QuestionNoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: QuestionSet, pos: Int) {
            // views
            itemView.tv_ques_no?.text = ("").plus(model.quesId)

            if(pos==0){
                itemView.iv_ques_no_pointer?.setBackgroundResource(R.color.colorGreen)
            }else{
                itemView.iv_ques_no_pointer?.setBackgroundResource(0)
            }

        }
    }

}
