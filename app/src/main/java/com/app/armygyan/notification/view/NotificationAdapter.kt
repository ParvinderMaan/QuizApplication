package com.app.armygyan.notification.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.databinding.ListItemNotificationBinding
import com.app.armygyan.helper.TimeUtil
import com.app.armygyan.notification.model.NotificationResponse
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.abs


class NotificationAdapter(var currentTime: Date) : RecyclerView.Adapter<NotificationAdapter.MiViewHolder>() {
    private var mItemClickListener: OnItemClickListener? = null
    val items: ArrayList<NotificationResponse.Notification> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val binding = ListItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val item= items[position]
        holder.bindView(item)
    }

    interface OnItemClickListener{
        fun onItemClick(item: NotificationResponse.Notification);
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener){
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

    inner class MiViewHolder (val binder: ListItemNotificationBinding) : RecyclerView.ViewHolder(binder.root) {
        fun bindView(item: NotificationResponse.Notification) {
            binder.tvNotification.text=item.content
            item.let {
                val createdOn = TimeUtil.utcToLocal(item.createdOn)
                val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(createdOn)
                val diffInMillies = abs(currentTime.time - date1.time) /1000
                val days = TimeUnit.SECONDS.toDays(diffInMillies).toInt()
                val hours: Long = TimeUnit.SECONDS.toHours(diffInMillies) - TimeUnit.SECONDS.toDays(diffInMillies) * 24
                val minutes: Long = TimeUnit.SECONDS.toMinutes(diffInMillies) - TimeUnit.SECONDS.toHours(diffInMillies) * 60
                val seconds: Long = TimeUnit.SECONDS.toSeconds(diffInMillies) - TimeUnit.SECONDS.toMinutes(diffInMillies) * 60
                if(days!=0){
                    binder.tvTime.text=days.toString().plus(" ").plus("days ago")
                    return
                }
                if(hours!=0L){
                    binder.tvTime.text=hours.toString().plus(" ").plus("hours ago")
                    return
                }
                if(minutes!=0L){
                    binder.tvTime.text=minutes.toString().plus(" ").plus("minutes ago")
                    return
                }
                if(seconds!=0L){
                    binder.tvTime.text=seconds.toString().plus(" ").plus("seconds ago")
                    return
                }
            }
            binder.root.setOnClickListener {
                mItemClickListener?.onItemClick(item)
            }
        }

    }

}

