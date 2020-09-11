package com.app.armygyan.quizz.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.R
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
        viewHolder.bindView(item)
        // listener
        viewHolder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(viewHolder.adapterPosition)
            items.forEach { it.isQuesActive=false  }
            item.isQuesActive=true // Note : this is flag used for icon visibility
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int);
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }




    class QuestionNoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: QuestionSet) {
            // views
            itemView.tv_ques_no?.text = ("").plus(adapterPosition+1)
            if(model.isQuesActive){
                itemView.iv_ques_no_pointer?.setBackgroundResource(R.color.colorWhite)
            }else{
                itemView.iv_ques_no_pointer?.setBackgroundResource(0)
            }

            itemView.tv_ques_no?.isSelected = model.isQuesAttempted


        }
    }
    // region Helper Methods
    fun getItem(position: Int): QuestionSet {
        return items[position]
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

    fun refreshItemIndex(item:QuestionSet, visiblePos: Int) {
        items.forEach { it.isQuesActive=false }
        item.isQuesActive=true // Note : this is flag used for icon visibility
        items.set(visiblePos,item)
        notifyDataSetChanged()
    }
    fun refreshItemStatus(item:QuestionSet, visiblePos: Int) {
        item.isQuesAttempted=true // Note : this is flag used for icon visibility
        items.set(visiblePos,item)
        notifyDataSetChanged()
    }

}
