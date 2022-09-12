package com.demo.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.app.data.BrandNameListItem
import com.demo.app.OnSelectAllCheckedListener
import com.demo.app.databinding.ListItemBrandNLocationsBinding

class BrandsListAdapter (private val brands:MutableList<BrandNameListItem>):
    RecyclerView.Adapter<BrandsListAdapter.ViewHolder>(){
    lateinit var listener: OnSelectAllCheckedListener

    class ViewHolder constructor(private val binding:ListItemBrandNLocationsBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(list:MutableList<BrandNameListItem>, item: BrandNameListItem,
                         adapter:BrandsListAdapter, listener:OnSelectAllCheckedListener){
                    item.id = adapterPosition
                    binding.cbName.text = item.brandName
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
        holder.bind(brands,brands[position],this,listener)
    }

    override fun getItemCount(): Int {
        return brands.size
    }
}