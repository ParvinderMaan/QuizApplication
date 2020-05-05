package com.app.quiz.studymaterial.view

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.R
import com.app.quiz.studymaterial.model.StudyMaterialCategory
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoryAdapter  : PagedListAdapter<StudyMaterialCategory,CategoryAdapter.MiiViewHolder>(ITEM_COMPARATOR){
    private var mItemClickListener: OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiiViewHolder {
        return MiiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false))
    }

    override fun onBindViewHolder(holder: MiiViewHolder, pos: Int) {
        var item = getItem(pos)
        val viewHolder = holder as MiiViewHolder
        viewHolder.bindView(item,viewHolder.adapterPosition )
        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick()
        }

    }

    interface OnItemClickListener {
        fun onItemClick()
    }

    public fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    class MiiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: StudyMaterialCategory?, pos: Int) {
            itemView.tv_category_name?.text = model?.catName
            itemView.tv_no_of_chapters?.text = "Total chapters ".plus(model?.chapterCount)
            itemView.tv_desc?.text = model?.catDesc

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.tv_desc?.setText(Html.fromHtml(model?.catDesc, Html.FROM_HTML_MODE_LEGACY));
            } else {
                itemView.tv_desc?.setText(Html.fromHtml(model?.catDesc))
            }

            itemView.iv_favourite.setOnClickListener {
                itemView.iv_favourite.setImageResource(R.drawable.ic_heart_fill);
            }
        }
    }



    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<StudyMaterialCategory>() {
            override fun areItemsTheSame(oldItem: StudyMaterialCategory, newItem: StudyMaterialCategory): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: StudyMaterialCategory, newItem: StudyMaterialCategory): Boolean =
                newItem == oldItem
        }
    }

}