package com.developer.ivan.easymadbus.presentation.adapters

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.developer.ivan.domain.StopFavourite
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.getValueInDP
import com.developer.ivan.easymadbus.presentation.map.LineCustomView
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite
import kotlinx.android.synthetic.main.item_favourite.view.*


class SwipeToDeleteCallback(
    private val icon: Drawable,
    private val background: ColorDrawable,
    val mAdapter: FavouritesAdapter,
    val onSwipe: (Pair<UIBusStop,UIStopFavourite>, Int)->Unit
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onSwipe.invoke(mAdapter.items[position].copy(), position)
        mAdapter.deleteItem(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset =
            20 //so background is behind the rounded corners of itemView


        val iconMargin: Int = (itemView.height - icon.getIntrinsicHeight()) / 2
        val iconTop: Int =
            itemView.top + (itemView.height - icon.getIntrinsicHeight()) / 2
        val iconBottom: Int = iconTop + icon.getIntrinsicHeight()

        if (dX > 0) { // Swiping to the right
            val iconLeft: Int = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.getIntrinsicWidth()
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft: Int = itemView.right - iconMargin - icon.getIntrinsicWidth()
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }

}

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

    fun updateItem(item: Pair<UIBusStop, UIStopFavourite>) {
        items = items.toList()
            .map { if (it.first.node == item.first.node) it.copy(item.first, item.second) else it }
    }

    fun updateItems(items: List<Pair<UIBusStop, UIStopFavourite>>) {
        this.items = items
    }

    fun addItem(item: Pair<UIBusStop, UIStopFavourite>, position: Int) {
        this.items = this.items.toMutableList()
                         .apply { add(position,item) }
    }

    fun deleteItem(position: Int) {
        this.items = this.items.filterIndexed { index, pair -> index != position }
        notifyItemRemoved(position)
    }

    class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(item: Pair<UIBusStop, UIStopFavourite>) {

            with(itemView)
            {
                txtName.text = item.second.name

                lyLineContainer.removeAllViews()

                item.first.lines.forEach {

                    lyLineContainer.addView(LineCustomView(context).apply {
                        setLine(it)
                        setFixedSize(context.getValueInDP(60))
                    })
                }

                lyLineContainer
            }
        }

    }

    override fun getItemCount(): Int = items.size

}
