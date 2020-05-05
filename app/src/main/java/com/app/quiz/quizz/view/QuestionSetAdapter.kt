package com.app.quiz.quizz.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.R
import com.app.quiz.quizz.model.QuestionSet
import kotlinx.android.synthetic.main.list_item_question_set.view.*

class QuestionSetAdapter : RecyclerView.Adapter<QuestionSetViewHolder> {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<QuestionSet>

    constructor() : super() {
        this.items = ArrayList();
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionSetViewHolder {
        return QuestionSetViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_question_set,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: QuestionSetViewHolder, pos: Int, payloads: MutableList<Any>) {
       // Log.e("onBindViewHolder", "............extended.............."+pos)
        var item = items.get(pos)
        val viewHolder = holder as QuestionSetViewHolder
        if(!payloads.isEmpty() ){
            viewHolder.bindTwoView(item,pos)
        }else{
            super.onBindViewHolder(holder, pos, payloads)
        }
    }

    // Update ALL VIEW holder
    override fun onBindViewHolder(holder: QuestionSetViewHolder, pos: Int) {
      //  Log.e("onBindViewHolder", ".............normal............."+pos)
        var item = items.get(pos)
        val viewHolder = holder as QuestionSetViewHolder
        viewHolder.bindOneView(item, pos)
        viewHolder.bindTwoView(item, pos)
        // listener
        viewHolder.itemView.tv_ans_one?.setOnClickListener {
            if (items.get(pos).ansOptSelected != 1) {
                items.get(pos).ansOptSelected = 1
//                viewHolder.itemView.fbtn_ans_one.show()
             notifyItemChanged(viewHolder.adapterPosition,1)
            }
        }
        viewHolder.itemView.tv_ans_two?.setOnClickListener {
            if (items.get(pos).ansOptSelected != 2) {
                items.get(pos).ansOptSelected = 2
//                viewHolder.itemView.fbtn_ans_two.show()
                notifyItemChanged(viewHolder.adapterPosition,1)
            }
        }
        viewHolder.itemView.tv_ans_three?.setOnClickListener {
            if (items.get(pos).ansOptSelected != 3) {
                items.get(pos).ansOptSelected = 3
//                viewHolder.itemView.fbtn_ans_three.show()
                notifyItemChanged(viewHolder.adapterPosition,1)
            }
        }
        viewHolder.itemView.tv_ans_four?.setOnClickListener {
            if (items.get(pos).ansOptSelected != 4) {
                items.get(pos).ansOptSelected = 4
//                viewHolder.itemView.fbtn_ans_four.show()
                notifyItemChanged(viewHolder.adapterPosition,1)
            }
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

    fun addAll(items: MutableList<QuestionSet>) {
        for (item in items) {
            add(item)
        }
    }

}

class QuestionSetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindOneView(model: QuestionSet, pos: Int) {
        // views
        itemView.tv_ques_no?.text = ("Question").plus(" ").plus(model.quesId)
        itemView.tv_ques_name?.text = model.quesName
        itemView.tv_ans_one?.text = model.optOne
        itemView.tv_ans_two?.text = model.optTwo
        itemView.tv_ans_three?.text = model.optThree
        itemView.tv_ans_four?.text = model.optFour



    }
    fun bindTwoView(model: QuestionSet, pos:Int){
        when (model.ansOptSelected) {
            1 -> {
                itemView.fbtn_ans_one?.show()
                    if (itemView.fbtn_ans_two.visibility == View.VISIBLE)
                itemView.fbtn_ans_two.hide()
                    if (itemView.fbtn_ans_three.visibility == View.VISIBLE)
                itemView.fbtn_ans_three.hide()
                    if (itemView.fbtn_ans_four.visibility == View.VISIBLE)
                itemView.fbtn_ans_four.hide()
            }
            2 -> {
                    if (itemView.fbtn_ans_one.visibility == View.VISIBLE)
                itemView.fbtn_ans_one.hide()
                itemView.fbtn_ans_two.show()
                    if (itemView.fbtn_ans_three.visibility == View.VISIBLE)
                itemView.fbtn_ans_three.hide()
                    if (itemView.fbtn_ans_four.visibility == View.VISIBLE)
                itemView.fbtn_ans_four.hide()

            }
            3 -> {
                    if (itemView.fbtn_ans_one.visibility == View.VISIBLE)
                itemView.fbtn_ans_one.hide()
                    if (itemView.fbtn_ans_two.visibility == View.VISIBLE)
                itemView.fbtn_ans_two.hide()
                itemView.fbtn_ans_three.show()
                    if (itemView.fbtn_ans_four.visibility == View.VISIBLE)
                itemView.fbtn_ans_four.hide()
            }
            4 -> {
                    if (itemView.fbtn_ans_one.visibility == View.VISIBLE)
                itemView.fbtn_ans_one.hide()
                    if (itemView.fbtn_ans_two.visibility == View.VISIBLE)
                itemView.fbtn_ans_two.hide()
                    if (itemView.fbtn_ans_three.visibility == View.VISIBLE)
                itemView.fbtn_ans_three.hide()
                itemView.fbtn_ans_four.show()
            }
            else -> {
                if (itemView.fbtn_ans_one.visibility == View.VISIBLE)
                itemView.fbtn_ans_one.hide()
                if (itemView.fbtn_ans_two.visibility == View.VISIBLE)
                itemView.fbtn_ans_two.hide()
                if (itemView.fbtn_ans_three.visibility == View.VISIBLE)
                itemView.fbtn_ans_three.hide()
                if (itemView.fbtn_ans_four.visibility == View.VISIBLE)
                itemView.fbtn_ans_four.hide()
            }
        }

    }
}