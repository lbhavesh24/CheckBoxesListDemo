package com.demo.app

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DataRepo @Inject constructor() {

    fun getData(context: Context): ResponseData {
        val gson = Gson()
        return gson.fromJson(
            context.loadJSONFromAssets("data.json"),
            ResponseData::class.java
        )
    }
}