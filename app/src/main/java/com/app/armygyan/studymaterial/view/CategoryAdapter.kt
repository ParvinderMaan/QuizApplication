package com.app.armygyan.studymaterial.view

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.R
import com.app.armygyan.databinding.ListItemCategoryBinding
import com.app.armygyan.studymaterial.model.StudyMaterialCategory

class CategoryAdapter  : RecyclerView.Adapter<CategoryAdapter.MiiViewHolder>(){
    private var mItemClickListener: OnItemClickListener? = null
    private val items: ArrayList<StudyMaterialCategory> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiiViewHolder {
        val binding = ListItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MiiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MiiViewHolder, pos: Int) {
        val item = items[pos]
        holder.bindView(item)
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

    inner class MiiViewHolder(val binder :ListItemCategoryBinding) : RecyclerView.ViewHolder(binder.root) {
        fun bindView(model: StudyMaterialCategory) {
            val totalChapters = itemView.context.getString(R.string.title_total_chapters)
            binder.tvCategoryName.text = model.catName
            binder.tvNoOfChapters.text = totalChapters.plus(" ").plus(model.chapterCount)
            binder.tvDesc.text = model.catDesc

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binder.tvDesc.text = Html.fromHtml(model.catDesc, Html.FROM_HTML_MODE_LEGACY);
            } else {
                @Suppress("DEPRECATION")
                binder.tvDesc.text = Html.fromHtml(model.catDesc)
            }

            model.isFavourite?.let {
                if(it) binder.ivFavourite.setImageResource(R.drawable.ic_heart_fill)
                else binder.ivFavourite.setImageResource(R.drawable.ic_heart_unfill)
            }

            binder.root.setOnClickListener {
                mItemClickListener?.onItemClick(model)
            }
            binder.ivFavourite.setOnClickListener {
                if(model.isFavourite){
                    model.isFavourite=false
                    mItemClickListener?.onItemFavouriteClick(model,adapterPosition )
                }else{
                    model.isFavourite=true
                    mItemClickListener?.onItemFavouriteClick(model,adapterPosition )
                }
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