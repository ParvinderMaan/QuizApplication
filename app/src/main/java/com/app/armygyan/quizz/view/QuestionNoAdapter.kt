package com.app.armygyan.quizz.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.R
import com.app.armygyan.databinding.ListItemQuestionNoBinding

class QuestionNoAdapter : RecyclerView.Adapter<QuestionNoAdapter.QuestionNoViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    private val items: ArrayList<QuestionSet> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionNoViewHolder {
        val binding=ListItemQuestionNoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionNoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: QuestionNoViewHolder, pos: Int) {
        val item = items[pos]
        holder.bindView(item)

    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int);
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }




    inner class QuestionNoViewHolder(val binder: ListItemQuestionNoBinding) : RecyclerView.ViewHolder(binder.root) {
        fun bindView(model: QuestionSet) {
            binder.tvQuesNo.text = ("").plus(adapterPosition+1)
            if(model.isQuesActive){
                binder.ivQuesNoPointer.setBackgroundResource(R.color.colorWhite)
            }else{
                binder.ivQuesNoPointer.setBackgroundResource(0)
            }

            binder.tvQuesNo.isSelected = model.isQuesAttempted

            binder.root.setOnClickListener {
                mItemClickListener?.onItemClick(adapterPosition)
                items.forEach { it.isQuesActive=false  }
                model.isQuesActive=true /** Note : this is flag used for icon visibility **/
                notifyDataSetChanged()
            }
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
        item.isQuesActive=true /** Note : this is flag used for icon visibility **/
        items[visiblePos] = item
        notifyDataSetChanged()
    }
    fun refreshItemStatus(item:QuestionSet, visiblePos: Int) {
        item.isQuesAttempted=true /** Note : this is flag used for icon visibility **/
        items[visiblePos] = item
        notifyDataSetChanged()
    }

}
