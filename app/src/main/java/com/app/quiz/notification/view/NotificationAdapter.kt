package com.app.quiz.notification.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.R


class NotificationAdapter : RecyclerView.Adapter<MiViewHolder> {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<String>

    constructor() : super() {
        this.items = ArrayList();
//        this.items.add(AlertInfo("Shane Webb","is late for 10:00 am reservation"))
//        this.items.add(AlertInfo("Dwight Pena","is late for 11:00 am reservation"))
//        this.items.add(AlertInfo("Irma Murphy","with 13 guests is late for 11:00 am reservation"))
//        this.items.add(AlertInfo("Shane Webb","is late for 10:00 am reservation"))
//        this.items.add(AlertInfo("Dwight Pena","is late for 11:00 am reservation"))

    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return 5
    //items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        return MiViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.list_item_notification,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
//        var item=items.get(position)
//        val span1: Spannable = SpannableString(item.name)
//        span1.setSpan(StyleSpan(Typeface.BOLD), 0, item.name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//
//        val span2: Spannable = SpannableString(item.msg)
//        span2.setSpan(StyleSpan(Typeface.NORMAL), 0, item.msg.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        holder.tvMsg?.text =span1
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
}

class MiViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
   // val tvMsg = view.tvMsg
}