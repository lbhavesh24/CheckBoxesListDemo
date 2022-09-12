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
import com.demo.app.data.BrandNameListItem
import com.demo.app.data.DataViewModel
import com.demo.app.data.HierarchyItem
import com.demo.app.OnSelectAllCheckedListener
import com.demo.app.adapters.BrandsListAdapter
import com.demo.app.databinding.BottomSheetBrandLocationsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BrandFilterFragment:RoundedBottomSheetDialogFragment(), OnSelectAllCheckedListener,
    View.OnClickListener {
    private lateinit var binding: BottomSheetBrandLocationsBinding
    private lateinit var brandsAdapter: BrandsListAdapter
    private val viewModel by activityViewModels<DataViewModel>()
    private var brands:MutableList<BrandNameListItem> = mutableListOf()
    private var selectedAccountsList:MutableList<HierarchyItem> = mutableListOf()

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
        brandsAdapter = BrandsListAdapter(brands)
        binding.apply {
            rvBrandNLocations.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
                adapter = brandsAdapter
                brandsAdapter.listener = this@BrandFilterFragment
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
            ivCloseBtn.setOnClickListener(this@BrandFilterFragment)
            tvAddBtn.setOnClickListener(this@BrandFilterFragment)
        }
    }

    private fun observeData(){
        viewModel.selectedAccountList.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                selectedAccountsList.clear()
                selectedAccountsList.addAll(it)
            }
        }

        viewModel.accountsList.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                brands.clear()
                it.forEach { item ->
                    if (selectedAccountsList.isNotEmpty()) {
                        selectedAccountsList.forEach { sl ->
                            if (sl.accountNumber == item.accountNumber){
                                brands.addAll(item.brandNameList)
                            }
                        }
                    } else brands.addAll(item.brandNameList)
                }
                binding.cbSelectAll.isChecked = brands.filter { listItem ->
                    listItem.isSelected!!
                }.size == brands.size
                brandsAdapter.notifyItemRangeInserted(0,brands.size -1)
            }
        }
    }

    override fun onAllSelected(checked: Boolean) {
        binding.cbSelectAll.isChecked = checked
    }

    private fun updateList(isSelected:Boolean){
        val indexList = mutableListOf<Int>()
        brands.forEach {
            it.isSelected = isSelected
            indexList.add(brands.indexOf(it))
        }
        indexList.forEach {
            brandsAdapter.notifyItemChanged(it)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivCloseBtn -> dismiss()
            binding.tvAddBtn -> {
                lifecycleScope.launch(Dispatchers.IO){
                    viewModel.selectedBrandsList.postValue(
                        brands.filter { it.isSelected == true }.toMutableList())
                    withContext(Dispatchers.Main){dismiss()}
                }
            }
        }
    }
}