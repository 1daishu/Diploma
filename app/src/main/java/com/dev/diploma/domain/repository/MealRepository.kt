package com.dev.diploma.domain.repository

import androidx.paging.Pager
import com.dev.diploma.domain.model.MealX

interface MealRepository {
    fun getMeal() : Pager<Int, MealX>
}