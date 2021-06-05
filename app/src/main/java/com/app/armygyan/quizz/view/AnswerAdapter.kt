package com.app.armygyan.quizz.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.databinding.ListItemAnswerSetBinding
import com.app.armygyan.quizz.model.QuestionSet

class AnswerAdapter() : RecyclerView.Adapter<AnswerViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<QuestionSet> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val binding = ListItemAnswerSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerViewHolder(binding)

    }


    override fun onBindViewHolder(holder: AnswerViewHolder, pos: Int) {
        val item = items[pos]
        holder.bindView(item,mItemClickListener,itemCount)
    }

    interface OnItemClickListener {
        fun onItemClick(model: QuestionSet,pos: Int);
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

class AnswerViewHolder(val binder: ListItemAnswerSetBinding) : RecyclerView.ViewHolder(binder.root) {

    fun bindView(model: QuestionSet, itemClickListener: AnswerAdapter.OnItemClickListener?, totCount: Int) {
        val quesTitle = itemView.context.resources.getString(R.string.title_question)
        binder.tvQuesNo.text = (quesTitle).plus(" ").plus(adapterPosition + 1)
        binder.tvCategoryName.text =itemView.context.getString(R.string.slash).plus(totCount.toString())


        binder.tvExplain.setOnClickListener {
            itemClickListener?.onItemClick(model,adapterPosition)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binder.tvQuesName.text = Html.fromHtml(model.quesName, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsOne.text = Html.fromHtml(model.optOne, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsTwo.text = Html.fromHtml(model.optTwo, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsThree.text = Html.fromHtml(model.optThree, Html.FROM_HTML_MODE_LEGACY)
            binder.tvAnsFour.text = Html.fromHtml(model.optFour, Html.FROM_HTML_MODE_LEGACY)
        } else {
            binder.tvQuesName.text = Html.fromHtml(model.quesName)
            binder.tvAnsOne.text = Html.fromHtml(model.optOne)
            binder.tvAnsTwo.text = Html.fromHtml(model.optTwo)
            binder.tvAnsThree.text = Html.fromHtml(model.optThree)
            binder.tvAnsFour.text = Html.fromHtml(model.optFour)
        }
        when (model.ansOption) {
            "a" -> {
                binder.fbtnAnsOne.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#22da93"))
                binder.fbtnAnsOne.setImageResource(R.drawable.ic_check)
                binder.fbtnAnsOne.show()
                binder.fbtnAnsTwo.hide()
                binder.fbtnAnsThree.hide()
                binder.fbtnAnsFour.hide()


            }
            "b" -> {
                binder.fbtnAnsTwo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#22da93"))
                binder.fbtnAnsTwo.setImageResource(R.drawable.ic_check)

                binder.fbtnAnsOne.hide()
                binder.fbtnAnsTwo.show()
                binder.fbtnAnsThree.hide()
                binder.fbtnAnsFour.hide()

            }
            "c" -> {
                binder.fbtnAnsThree.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#22da93"))
                binder.fbtnAnsThree.setImageResource(R.drawable.ic_check)

                binder.fbtnAnsOne.hide()
                binder.fbtnAnsTwo.hide()
                binder.fbtnAnsThree.show()
                binder.fbtnAnsFour.hide()
            }
            "d" -> {
                binder.fbtnAnsFour.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#22da93"))
                binder.fbtnAnsFour.setImageResource(R.drawable.ic_check)
                binder.fbtnAnsOne.hide()
                binder.fbtnAnsTwo.hide()
                binder.fbtnAnsThree.hide()
                binder.fbtnAnsFour.show()
            }

        }
        when (model.ansOptSelected) {
            1 -> {

                if (model.ansOption != "a"){
                    binder.fbtnAnsOne.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    binder.fbtnAnsOne.setImageResource(R.drawable.ic_un_check)
                    binder.fbtnAnsOne.show()
                }

            }
            2 -> {
                if (model.ansOption != "b"){
                    binder.fbtnAnsTwo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    binder.fbtnAnsTwo.setImageResource(R.drawable.ic_un_check)
                    binder.fbtnAnsTwo.show()
                }

            }
            3 -> {
                if (model.ansOption != "c"){
                    binder.fbtnAnsThree.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    binder.fbtnAnsThree.setImageResource(R.drawable.ic_un_check)
                    binder.fbtnAnsThree.show()
                }
            }
            4 -> {
                if (model.ansOption != "d"){
                    binder.fbtnAnsFour.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    binder.fbtnAnsFour.setImageResource(R.drawable.ic_un_check)
                    binder.fbtnAnsFour.show()
                }


            }

        }
    }


}