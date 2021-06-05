package com.app.armygyan.quizz.view

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.databinding.ListItemQuestionSetBinding
import com.app.armygyan.quizz.model.QuestionSet

class QuestionSetAdapter : RecyclerView.Adapter<QuestionSetViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<QuestionSet> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionSetViewHolder {
        val binding = ListItemQuestionSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionSetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionSetViewHolder, pos: Int, payloads: MutableList<Any>) {
       // Log.e("onBindViewHolder", "............extended.............."+pos)
        val item = items[pos]
        if(payloads.isNotEmpty()){
            holder.bindTwoView(item,pos)
        }else{
            super.onBindViewHolder(holder, pos, payloads)
        }
    }

    // Update ALL VIEW holder
    override fun onBindViewHolder(holder: QuestionSetViewHolder, pos: Int) {
      //  Log.e("onBindViewHolder", ".............normal............."+pos)
        val item = items[pos]
        holder.bindOneView(item, itemCount)
        holder.bindTwoView(item, pos)

        holder.binder.tvAnsOne.setOnClickListener {
            if (item.ansOptSelected != 1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 && item.ansOptSelected != 4) {
                item.ansOptSelected = 1
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }

        }
        holder.binder.tvAnsTwo.setOnClickListener {
            if (item.ansOptSelected != 1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 &&
                item.ansOptSelected != 4) {
                item.ansOptSelected = 2
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }
        }
        holder.binder.tvAnsThree.setOnClickListener {
            if (item.ansOptSelected !=1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 &&
                item.ansOptSelected != 4) {
                item.ansOptSelected = 3
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }
        }
        holder.binder.tvAnsFour.setOnClickListener {
            if (item.ansOptSelected != 1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 &&
                item.ansOptSelected != 4) {
                item.ansOptSelected = 4
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }
        }

        holder.binder.ivOne.setOnClickListener {
            if (item.ansOptSelected != 1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 && item.ansOptSelected != 4) {
                item.ansOptSelected = 1
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }

        }
        holder.binder.ivTwo.setOnClickListener {
            if (item.ansOptSelected != 1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 &&
                item.ansOptSelected != 4) {
                item.ansOptSelected = 2
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }
        }
        holder.binder.ivThree.setOnClickListener {
            if (item.ansOptSelected !=1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 &&
                item.ansOptSelected != 4) {
                item.ansOptSelected = 3
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }
        }
        holder.binder.ivFour.setOnClickListener {
            if (item.ansOptSelected != 1 && item.ansOptSelected != 2 && item.ansOptSelected != 3 &&
                item.ansOptSelected != 4) {
                item.ansOptSelected = 4
                notifyItemChanged(holder.adapterPosition,1)
                mItemClickListener?.onItemClick(item, holder.adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: QuestionSet, adapterPosition: Int);
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

}

class QuestionSetViewHolder(val binder: ListItemQuestionSetBinding) : RecyclerView.ViewHolder(binder.root) {

    fun bindOneView(model: QuestionSet, totCount: Int) {
        val quesTitle = itemView.context.resources.getString(R.string.title_question)
        binder.tvQuesNo.text = (quesTitle).plus(" ").plus(adapterPosition+1)
        binder.tvCategoryName.text =itemView.context.getString(R.string.slash).plus(totCount.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binder.tvQuesName.text = Html.fromHtml(model.quesName, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsOne.text =Html.fromHtml(model.optOne, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsTwo.text =Html.fromHtml(model.optTwo, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsThree.text =Html.fromHtml(model.optThree, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsFour.text =Html.fromHtml(model.optFour, Html.FROM_HTML_MODE_LEGACY)
        } else {
            binder.tvQuesName.text = Html.fromHtml(model.quesName)
            binder.tvAnsOne.text =Html.fromHtml(model.optOne)
            binder.tvAnsTwo.text =Html.fromHtml(model.optTwo)
            binder.tvAnsThree.text =Html.fromHtml(model.optThree)
            binder.tvAnsFour.text =Html.fromHtml(model.optFour)
        }


    }
    fun bindTwoView(model: QuestionSet, pos:Int){
        when (model.ansOptSelected) {
            1 -> {
                binder.fbtnAnsOne.show()
                    if (binder.fbtnAnsTwo.visibility == View.VISIBLE)
                        binder.fbtnAnsTwo.hide()
                    if (binder.fbtnAnsThree.visibility == View.VISIBLE)
                        binder.fbtnAnsThree.hide()
                    if (binder.fbtnAnsFour.visibility == View.VISIBLE)
                        binder.fbtnAnsFour.hide()
            }
            2 -> {
                    if (binder.fbtnAnsOne.visibility == View.VISIBLE)
                        binder.fbtnAnsOne.hide()
                        binder.fbtnAnsTwo.show()
                    if (binder.fbtnAnsThree.visibility == View.VISIBLE)
                        binder.fbtnAnsThree.hide()
                    if (binder.fbtnAnsFour.visibility == View.VISIBLE)
                        binder.fbtnAnsFour.hide()

            }
           3 -> {
                    if (binder.fbtnAnsOne.visibility == View.VISIBLE)
                        binder.fbtnAnsOne.hide()
                    if (binder.fbtnAnsTwo.visibility == View.VISIBLE)
                        binder.fbtnAnsTwo.hide()
               binder.fbtnAnsThree.show()
                    if (binder.fbtnAnsFour.visibility == View.VISIBLE)
                        binder.fbtnAnsFour.hide()
            }
           4 -> {
                    if (binder.fbtnAnsOne.visibility == View.VISIBLE)
                        binder.fbtnAnsOne.hide()
                    if (binder.fbtnAnsTwo.visibility == View.VISIBLE)
                        binder.fbtnAnsTwo.hide()
                    if (binder.fbtnAnsThree.visibility == View.VISIBLE)
                        binder.fbtnAnsThree.hide()
               binder.fbtnAnsFour.show()
            }
            else -> {
                if (binder.fbtnAnsOne.visibility == View.VISIBLE)
                    binder.fbtnAnsOne.hide()
                if (binder.fbtnAnsTwo.visibility == View.VISIBLE)
                    binder.fbtnAnsTwo.hide()
                if (binder.fbtnAnsThree.visibility == View.VISIBLE)
                    binder.fbtnAnsThree.hide()
                if (binder.fbtnAnsFour.visibility == View.VISIBLE)
                    binder.fbtnAnsFour.hide()
            }
        }

    }


}