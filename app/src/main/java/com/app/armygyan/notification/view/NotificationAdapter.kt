package com.app.armygyan.notification.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.helper.TimeUtil
import com.app.armygyan.notification.model.NotificationResponse
import kotlinx.android.synthetic.main.list_item_notification.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class NotificationAdapter(var currentTime: Date) : RecyclerView.Adapter<NotificationAdapter.MiViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<NotificationResponse.Notification>

    init {
        this.items = ArrayList()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        return MiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val item=items.get(position)
        holder.tvNotification?.text=item.content
        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(item)
        }
        item.let {
            val createdOn = TimeUtil.utcToLocal(item.createdOn)
            val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createdOn)
            val diffInMillies = Math.abs(currentTime.time - date1.time)/1000
                val days = TimeUnit.SECONDS.toDays(diffInMillies).toInt()
                val hours: Long = TimeUnit.SECONDS.toHours(diffInMillies) - TimeUnit.SECONDS.toDays(diffInMillies) * 24
                val minutes: Long = TimeUnit.SECONDS.toMinutes(diffInMillies) - TimeUnit.SECONDS.toHours(diffInMillies) * 60
                val seconds: Long = TimeUnit.SECONDS.toSeconds(diffInMillies) - TimeUnit.SECONDS.toMinutes(diffInMillies) * 60
                if(days!=0){
                    holder.tvTime.text=days.toString().plus(" ").plus("days ago")
                    return
                }
                if(hours!=0L){
                    holder.tvTime.text=hours.toString().plus(" ").plus("hours ago")
                    return
                }
                if(minutes!=0L){
                    holder.tvTime.text=minutes.toString().plus(" ").plus("minutes ago")
                    return
                }
                if(seconds!=0L){
                    holder.tvTime.text=seconds.toString().plus(" ").plus("seconds ago")
                    return
                }
        }

    }

    interface OnItemClickListener{
        fun onItemClick(item: NotificationResponse.Notification);
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

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    class MiViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val tvNotification = view.tv_notification
        val tvTime= view.tv_time
    }

}

