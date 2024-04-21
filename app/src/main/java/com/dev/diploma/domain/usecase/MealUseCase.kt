package com.dev.diploma.domain.usecase

import com.dev.diploma.domain.repository.MealRepository
import javax.inject.Inject

class MealUseCase @Inject constructor(
    private val repository: MealRepository
) {
    operator fun invoke() = repository.getProduct()
}