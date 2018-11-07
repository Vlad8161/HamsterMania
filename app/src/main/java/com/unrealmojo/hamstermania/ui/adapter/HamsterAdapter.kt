package com.unrealmojo.hamstermania.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.unrealmojo.hamstermania.R
import com.unrealmojo.hamstermania.data.ui.Hamster

class HamsterAdapter(
        private val mContext: Context,
        private val mOnItemClick: (item: Hamster, holder: HamsterViewHolder) -> Unit,
        private val mOnItemShareClick: (item: Hamster, holder: HamsterViewHolder) -> Unit
) : RecyclerView.Adapter<HamsterAdapter.HamsterViewHolder>() {
    var data: List<Hamster> = ArrayList()
        set(value) {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                        field[oldItemPosition] == value[newItemPosition]

                override fun getOldListSize(): Int =
                        field.size

                override fun getNewListSize(): Int =
                        value.size

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                        true

            })
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onBindViewHolder(holder: HamsterViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.title
        holder.description.text = item.description
        Picasso.get().cancelRequest(holder.image)
        if (item.image != null) {
            Picasso.get().load(item.image)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_logo)
        }
        holder.itemView.setOnClickListener { mOnItemClick(item, holder) }
        holder.btnShare.setOnClickListener { mOnItemShareClick(item, holder) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HamsterViewHolder =
            HamsterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_hamster, parent, false))
                    .also { holder ->
                        val wrappedBtnShareIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_btn_share)
                                .let { DrawableCompat.wrap(it) }
                                .also { DrawableCompat.setTint(it, ContextCompat.getColor(mContext, R.color.white)) }
                        holder.btnShare.setImageDrawable(wrappedBtnShareIcon)
                    }

    override fun getItemCount(): Int = data.size

    class HamsterViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.title)
        val description: TextView = v.findViewById(R.id.description)
        val btnShare: ImageView = v.findViewById(R.id.btnShare)
        val image: ImageView = v.findViewById(R.id.image)
    }
}