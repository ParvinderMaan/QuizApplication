package com.app.quiz.quizz.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.R
import com.app.quiz.quizz.model.QuestionSet
import kotlinx.android.synthetic.main.list_item_question_set.view.*

class AnswerAdapter : RecyclerView.Adapter<AnswerViewHolder> {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<QuestionSet>

    constructor() : super() {
        this.items = ArrayList();
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_answer_set,
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: AnswerViewHolder, pos: Int) {
        //  Log.e("onBindViewHolder", ".............normal............."+pos)
        var item = items.get(pos)
        val viewHolder = holder as AnswerViewHolder
        item.let {
            viewHolder.bindView(it)
        }

    }

    interface OnItemClickListener {
        fun onItemClick();
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

class AnswerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(model: QuestionSet) {
        // views

        var quesTitle = itemView.context.resources.getString(R.string.title_question)
        itemView.tv_ques_no?.text = (quesTitle).plus(" ").plus(adapterPosition + 1)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemView.tv_ques_name?.text = Html.fromHtml(model.quesName, Html.FROM_HTML_MODE_LEGACY)
            itemView.tv_ans_one?.text = Html.fromHtml(model.optOne, Html.FROM_HTML_MODE_LEGACY)
            itemView.tv_ans_two?.text = Html.fromHtml(model.optTwo, Html.FROM_HTML_MODE_LEGACY)
            itemView.tv_ans_three?.text = Html.fromHtml(model.optThree, Html.FROM_HTML_MODE_LEGACY)
            itemView.tv_ans_four?.text = Html.fromHtml(model.optFour, Html.FROM_HTML_MODE_LEGACY)
        } else {
            itemView.tv_ques_name?.text = Html.fromHtml(model.quesName)
            itemView.tv_ans_one?.text = Html.fromHtml(model.optOne)
            itemView.tv_ans_two?.text = Html.fromHtml(model.optTwo)
            itemView.tv_ans_three?.text = Html.fromHtml(model.optThree)
            itemView.tv_ans_four?.text = Html.fromHtml(model.optFour)
        }






        when (model.ansOption) {
            "a" -> {
                itemView.fbtn_ans_one?.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#22da93"))
                itemView.fbtn_ans_one?.setImageResource(R.drawable.ic_check)
                itemView.fbtn_ans_one?.show()
                itemView.fbtn_ans_two.hide()
                itemView.fbtn_ans_three.hide()
                itemView.fbtn_ans_four.hide()


            }
            "b" -> {
                itemView.fbtn_ans_two?.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#22da93"))
                itemView.fbtn_ans_two?.setImageResource(R.drawable.ic_check)

                itemView.fbtn_ans_one.hide()
                itemView.fbtn_ans_two.show()
                itemView.fbtn_ans_three.hide()
                itemView.fbtn_ans_four.hide()

            }
            "c" -> {
                itemView.fbtn_ans_three.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#22da93"))
                itemView.fbtn_ans_three?.setImageResource(R.drawable.ic_check)

                itemView.fbtn_ans_one.hide()
                itemView.fbtn_ans_two.hide()
                itemView.fbtn_ans_three.show()
                itemView.fbtn_ans_four.hide()
            }
            "d" -> {
                itemView.fbtn_ans_four.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#22da93"))
                itemView.fbtn_ans_four?.setImageResource(R.drawable.ic_check)
                itemView.fbtn_ans_one.hide()
                itemView.fbtn_ans_two.hide()
                itemView.fbtn_ans_three.hide()
                itemView.fbtn_ans_four.show()
            }

        }
        when (model.ansOptSelected) {
            1 -> {

                if (model.ansOption != "a"){
                    itemView.fbtn_ans_one.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    itemView.fbtn_ans_one.setImageResource(R.drawable.ic_un_check)
                    itemView.fbtn_ans_one?.show()
                }

            }
            2 -> {
                if (model.ansOption != "b"){
                    itemView.fbtn_ans_two.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    itemView.fbtn_ans_two.setImageResource(R.drawable.ic_un_check)
                    itemView.fbtn_ans_two.show()
                }

            }
            3 -> {
                if (model.ansOption != "c"){
                    itemView.fbtn_ans_three.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    itemView.fbtn_ans_three.setImageResource(R.drawable.ic_un_check)
                    itemView.fbtn_ans_three.show()
                }
            }
            4 -> {
                if (model.ansOption != "d"){
                    itemView.fbtn_ans_four.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#de5246"))
                    itemView.fbtn_ans_four.setImageResource(R.drawable.ic_un_check)
                    itemView.fbtn_ans_four.show()
                }


            }

        }
    }


}