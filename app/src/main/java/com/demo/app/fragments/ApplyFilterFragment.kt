package com.demo.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.demo.app.data.DataViewModel
import com.demo.app.R
import com.demo.app.databinding.BottomSheetApplyFilterBinding

class ApplyFilterFragment:RoundedBottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding:BottomSheetApplyFilterBinding
    private var navController:NavController? = null
    private val viewModel by activityViewModels<DataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetApplyFilterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        addListeners()
        observeData()
    }

    private fun initUI(){
        navController = this.findNavController()
    }

    private fun observeData(){
        viewModel.selectedAccountList.observe(viewLifecycleOwner){
            binding.tvSelectedAccCount.text = (if (it.isNotEmpty()) it.size.toString() else "")
        }
        viewModel.selectedBrandsList.observe(viewLifecycleOwner){
            binding.tvSelectedBrandCount.text = (if (it.isNotEmpty()) it.size.toString() else "")
        }
        viewModel.selectedLocationList.observe(viewLifecycleOwner){
            binding.tvSelectedLocCount.text = (if (it.isNotEmpty()) it.size.toString() else "")
        }
    }

    private fun addListeners(){
        binding.apply {
            tvSelectAccNo.setOnClickListener(this@ApplyFilterFragment)
            tvSelectedAccCount.setOnClickListener(this@ApplyFilterFragment)
            ivSelectAccArrow.setOnClickListener(this@ApplyFilterFragment)
            tvSelectBrand.setOnClickListener(this@ApplyFilterFragment)
            tvSelectedBrandCount.setOnClickListener(this@ApplyFilterFragment)
            ivSelectBrandArrow.setOnClickListener(this@ApplyFilterFragment)
            tvSelectLocation.setOnClickListener(this@ApplyFilterFragment)
            tvSelectedLocCount.setOnClickListener(this@ApplyFilterFragment)
            ivSelectLocArrow.setOnClickListener(this@ApplyFilterFragment)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.tvSelectAccNo,binding.ivSelectAccArrow,binding.tvSelectedAccCount ->{
                navController?.navigate(R.id.openAccountFilterFragment)
            }
            binding.tvSelectBrand,binding.ivSelectBrandArrow,binding.tvSelectedLocCount ->{
                navController?.navigate(R.id.openBrandFilterFragment)
            }
            binding.tvSelectLocation,binding.ivSelectLocArrow,binding.tvSelectedLocCount ->{
                navController?.navigate(R.id.openLocationsFilterFragment)
            }
        }
    }
}