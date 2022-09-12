package com.demo.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.app.data.LocationNameListItem
import com.demo.app.OnSelectAllCheckedListener
import com.demo.app.databinding.ListItemBrandNLocationsBinding

class LocationListAdapter (private val locations:MutableList<LocationNameListItem>):
    RecyclerView.Adapter<LocationListAdapter.ViewHolder>(){
    lateinit var listener: OnSelectAllCheckedListener

    class ViewHolder constructor(private val binding:ListItemBrandNLocationsBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(list:MutableList<LocationNameListItem>, item: LocationNameListItem,
                 adapter:LocationListAdapter, listener: OnSelectAllCheckedListener
        ){
            item.id = adapterPosition
            binding.cbName.text = item.locationName
            binding.cbName.isChecked = item.isSelected!!
            binding.cbName.setOnCheckedChangeListener { button, b ->
                if (button.isPressed){
                    item.id?.let {
                        item.isSelected = b
                        adapter.notifyItemChanged(adapterPosition)
                        listener.onAllSelected(list.filter {
                            it.isSelected!!
                        }.size == list.size)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBrandNLocationsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locations,locations[position],this,listener)
    }

    override fun getItemCount(): Int {
        return locations.size
    }
}