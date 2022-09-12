package com.demo.app.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repo: DataRepo,
    application: Application
):AndroidViewModel(application){
    var accountsList: MutableLiveData<MutableList<HierarchyItem>> = MutableLiveData()
    var selectedAccountList:MutableLiveData<MutableList<HierarchyItem>> = MutableLiveData()
    var selectedBrandsList:MutableLiveData<MutableList<BrandNameListItem>> = MutableLiveData()
    var selectedLocationList:MutableLiveData<MutableList<LocationNameListItem>> = MutableLiveData()

    fun fetchData(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val responseData = repo.getData(context)
            responseData.filterData[0].let {
                it.hierarchy.let { accList -> accountsList.postValue(accList.toMutableList()) }
            }
        }
    }
}