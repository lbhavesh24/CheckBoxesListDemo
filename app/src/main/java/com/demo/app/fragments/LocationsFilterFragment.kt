package com.demo.app.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.app.*
import com.demo.app.adapters.LocationListAdapter
import com.demo.app.data.BrandNameListItem
import com.demo.app.data.DataViewModel
import com.demo.app.data.LocationNameListItem
import com.demo.app.databinding.BottomSheetBrandLocationsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LocationsFilterFragment:RoundedBottomSheetDialogFragment(), OnSelectAllCheckedListener,
    View.OnClickListener {
    private lateinit var binding: BottomSheetBrandLocationsBinding
    private lateinit var locationsAdapter: LocationListAdapter
    private val viewModel by activityViewModels<DataViewModel>()
    private var locations:MutableList<LocationNameListItem> = mutableListOf()
    private var selectedBrandsList:MutableList<BrandNameListItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBrandLocationsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        addListeners()
        observeData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface: DialogInterface? -> setupRatio(
            (dialogInterface as BottomSheetDialog?)!!,binding.tvAddBtn,binding.rvBrandNLocations,
            requireActivity()) }

        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0) //Sliding happens from 0 (Collapsed) to 1 (Expanded) - if so, calculate margins
                    buttonLayoutParams!!.topMargin =
                        ((expandedHeight - buttonHeight - collapsedMargin) * slideOffset + collapsedMargin).toInt() else  //If not sliding above expanded, set initial margin
                    buttonLayoutParams!!.topMargin = collapsedMargin
                binding.tvAddBtn.layoutParams = buttonLayoutParams //Set layout params to button (margin from top)
            }
        })
        return dialog
    }

    private fun initUI(){
        locationsAdapter = LocationListAdapter(locations)
        binding.apply {
            rvBrandNLocations.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
                adapter = locationsAdapter
                locationsAdapter.listener = this@LocationsFilterFragment
            }
            cbSelectAll.setOnCheckedChangeListener { button, b ->
                if (button.isPressed){
                    updateList(b)
                }
            }
        }
    }

    private fun addListeners(){
        binding.apply {
            ivCloseBtn.setOnClickListener(this@LocationsFilterFragment)
            tvAddBtn.setOnClickListener(this@LocationsFilterFragment)
        }
    }

    private fun observeData(){
        viewModel.selectedBrandsList.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                selectedBrandsList.clear()
                selectedBrandsList.addAll(it)
            }
        }

        viewModel.accountsList.observe(viewLifecycleOwner){
            lifecycleScope.launch(Dispatchers.IO){
                if (!it.isNullOrEmpty()){
                    locations.clear()
                    it.forEach { list ->
                        list.brandNameList.forEach { brands ->
                            if (selectedBrandsList.isNotEmpty()){
                                selectedBrandsList.forEach { sl ->
                                    if (sl.brandName == brands.brandName){
                                        locations.addAll(brands.locationNameList)
                                    }
                                }
                            }else {
                                locations.addAll(brands.locationNameList)
                            }
                        }
                    }
                    withContext(Dispatchers.Main) {
                        binding.cbSelectAll.isChecked = locations.filter {
                            it.isSelected!!
                        }.size == locations.size
                        locationsAdapter.notifyItemRangeInserted(0, locations.size - 1)
                    }
                }
            }
        }
    }

    override fun onAllSelected(checked: Boolean) {
        binding.cbSelectAll.isChecked = checked
    }

    private fun updateList(isSelected:Boolean){
        val indexList = mutableListOf<Int>()
        locations.forEach {
            it.isSelected = isSelected
            indexList.add(locations.indexOf(it))
        }
        indexList.forEach {
            locationsAdapter.notifyItemChanged(it)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivCloseBtn -> dismiss()
            binding.tvAddBtn -> {
                lifecycleScope.launch(Dispatchers.IO){
                    viewModel.selectedLocationList.postValue(
                        locations.filter { it.isSelected == true }.toMutableList())
                    withContext(Dispatchers.Main){dismiss()}
                }
            }
        }
    }
}