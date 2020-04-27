package com.developer.ivan.easymadbus.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.removeHTML
import com.developer.ivan.easymadbus.presentation.models.UIIncident
import kotlinx.android.synthetic.main.item_incident.view.*
import kotlin.properties.Delegates

class IncidentsAdapter(items: List<UIIncident>) :
    RecyclerView.Adapter<IncidentsAdapter.IncidentViewHolder>() {

    var items: List<UIIncident> by Delegates.observable(items, { _, _, _ ->
        notifyDataSetChanged()
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder =
        IncidentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_incident,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        holder.onBind(items[position])
    }


    class IncidentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(item: UIIncident) {

            with(itemView)
            {
                txtTitle.text = item.title
                txtDescription.text = buildSpannedString {

                    appendln(item.description.removeHTML())
                    appendln()
                    append(
                        itemView.context.getString(
                            R.string.valid_from,
                            item.rssAfectaDesde,
                            item.rssAfectaHasta
                        )
                    )
                }.toString()

            }
        }

    }

    override fun getItemCount(): Int = items.size

}
