package com.app.quiz.notification.view

import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.R
import com.app.quiz.notification.model.NotificationResponse
import com.app.quiz.quizz.model.QuestionSet
import kotlinx.android.synthetic.main.list_item_notification.view.*


class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.MiViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<NotificationResponse.Notification>

    init {
        this.items = ArrayList()
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        return MiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        var item=items.get(position)
        item.let {
            holder.tvNotification?.text =item.content
            holder.tvTime?.text =item.createdOn.split(" ")[0]
        }


//        holder.tvMsg?.append(" ")
//        holder.tvMsg?.append(span2)
//
//        holder.itemView.setOnClickListener {
//
//            mItemClickListener?.onItemClick()
//        }

    }

    interface OnItemClickListener{
        fun onItemClick();
    }

    public fun setOnItemClickListener(mItemClickListener: OnItemClickListener){
        this.mItemClickListener=mItemClickListener;

    }

    fun add(item: NotificationResponse.Notification) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(items: List<NotificationResponse.Notification>) {
        for (item in items) {
            add(item)
        }
    }

    class MiViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val tvNotification = view.tv_notification
        val tvTime= view.tv_time
    }
}

