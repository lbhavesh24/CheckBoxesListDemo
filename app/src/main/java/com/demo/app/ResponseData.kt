package com.demo.app

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseData(

	@field:SerializedName("filterData")
	var filterData: List<FilterDataItem> = ArrayList()
) : Parcelable

@Parcelize
data class BrandNameListItem(

	@field:SerializedName("locationNameList")
	var locationNameList: List<LocationNameListItem> = ArrayList(),

	@field:SerializedName("brandName")
	var brandName: String? = null,

	var isSelected:Boolean? = false,
	var id: Int? = null,
	var accountNumber: String? = null
) : Parcelable

@Parcelize
data class LocationNameListItem(

	@field:SerializedName("locationName")
	var locationName: String? = null,

	var isSelected:Boolean? = false,
	var id: Int? = null
) : Parcelable

@Parcelize
data class FilterDataItem(

	@field:SerializedName("companyName")
	var companyName: String? = null,

	@field:SerializedName("hierarchy")
	var hierarchy: List<HierarchyItem> = ArrayList()
) : Parcelable

@Parcelize
data class HierarchyItem(

	@field:SerializedName("brandNameList")
	var brandNameList: List<BrandNameListItem> = ArrayList(),

	@field:SerializedName("accountNumber")
	var accountNumber: String? = null,
	var isSelected:Boolean? = false,
	var id: Int? = null
) : Parcelable{
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is HierarchyItem) return false

		if (brandNameList != other.brandNameList) return false
		if (accountNumber != other.accountNumber) return false
		if (isSelected != other.isSelected) return false
		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		var result = brandNameList.hashCode()
		result = 31 * result + (accountNumber?.hashCode() ?: 0)
		result = 31 * result + (isSelected?.hashCode() ?: 0)
		result = 31 * result + (id ?: 0)
		return result
	}
}
