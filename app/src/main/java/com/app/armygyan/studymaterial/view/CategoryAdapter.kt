package com.app.armygyan.studymaterial.view

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.studymaterial.model.StudyMaterialCategory
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoryAdapter  : RecyclerView.Adapter<CategoryAdapter.MiiViewHolder>(){
    private var mItemClickListener: OnItemClickListener? = null
    private val items: ArrayList<StudyMaterialCategory> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiiViewHolder {
        return MiiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false))
    }

    override fun onBindViewHolder(holder: MiiViewHolder, pos: Int) {
        var item = items.get(pos) //getItem(pos)
        val viewHolder = holder
        item.let {
            viewHolder.bindView(it)
        }
        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(item)
        }
        holder.itemView.iv_favourite.setOnClickListener {
          if(item.isFavourite){
              item.isFavourite=false

              mItemClickListener?.onItemFavouriteClick(item,viewHolder.adapterPosition )
          }else{
              item.isFavourite=true

              mItemClickListener?.onItemFavouriteClick(item,viewHolder.adapterPosition )
          }


        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: StudyMaterialCategory?)
        fun onItemFavouriteClick(
            item: StudyMaterialCategory?,
            adapterPosition: Int
        )
    }

    public fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    class MiiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: StudyMaterialCategory?) {
            val totalChapters = itemView.context.getString(R.string.title_total_chapters)
            itemView.tv_category_name?.text = model?.catName
            itemView.tv_no_of_chapters?.text = totalChapters.plus(" ").plus(model?.chapterCount)
            itemView.tv_desc?.text = model?.catDesc

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.tv_desc?.text = Html.fromHtml(model?.catDesc, Html.FROM_HTML_MODE_LEGACY);
            } else {
                @Suppress("DEPRECATION")
                itemView.tv_desc?.text = Html.fromHtml(model?.catDesc)
            }

            model?.isFavourite?.let {
                if(it)
                    itemView.iv_favourite?.setImageResource(R.drawable.ic_heart_fill)
                else
                    itemView.iv_favourite?.setImageResource(R.drawable.ic_heart_unfill)
            }

        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun add(item: StudyMaterialCategory) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(items: List<StudyMaterialCategory>) {
        for (item in items) {
            add(item)
        }
    }
    fun refreshUi(item:StudyMaterialCategory,visiblePos: Int) {
        items.set(visiblePos,item)
        notifyDataSetChanged()
    }

    fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }
}