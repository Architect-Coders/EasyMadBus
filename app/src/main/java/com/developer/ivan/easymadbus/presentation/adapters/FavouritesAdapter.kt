package com.developer.ivan.easymadbus.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.presentation.map.LineCustomView
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import kotlinx.android.synthetic.main.item_favourite.view.*

class FavouritesAdapter :
    RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>() {

    var items: List<Pair<UIBusStop, UIStopFavourite>> = emptyList()
        private set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder =
        FavouriteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_favourite,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.onBind(items[position])
    }
    fun updateItem(item: Pair<UIBusStop,UIStopFavourite>)
    {
        items = items.toList().map { if(it.first.node==item.first.node) it.copy(item.first,item.second) else it }
    }
    fun updateItems(items: List<Pair<UIBusStop,UIStopFavourite>>){
        this.items = items
    }

    fun addItem(item: Pair<UIBusStop,UIStopFavourite>){
        this.items += item
    }

    class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(item: Pair<UIBusStop, UIStopFavourite>) {

            with(itemView)
            {
                txtName.text = item.second.name

                lyLineContainer.removeAllViews()

                item.first.lines.forEach {

                    lyLineContainer.addView(LineCustomView(context).apply { setLine(it) })
                }

                lyLineContainer
//                rcvLineContainer.adapter = adapter
//                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun getItemCount(): Int = items.size

}
