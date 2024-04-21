package com.dev.diploma.domain.repository

import androidx.lifecycle.LiveData
import com.dev.diploma.domain.model.MealItem
import retrofit2.Response

interface MealRepository {

    fun getProduct(): LiveData<List<MealItem>>
}