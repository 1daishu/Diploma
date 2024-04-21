package com.dev.diploma.data.network.dateFromJson

import android.content.Context
import com.dev.diploma.R
import com.dev.diploma.domain.model.MealItem
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class DateFromJson {
    fun loadMealItemsFromJson(context: Context): List<MealItem> {
        val inputStream = context.resources.openRawResource(R.raw.product)
        val jsonText = inputStream.bufferedReader().use { it.readText() }

        val gson = Gson()
        val listType = object : TypeToken<List<MealItem>>() {}.type
        return gson.fromJson(jsonText, listType)
    }

}