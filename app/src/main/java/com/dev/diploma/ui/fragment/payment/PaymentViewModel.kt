package com.dev.diploma.ui.fragment.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentViewModel : ViewModel() {
    private val _isOrderPlaced = MutableLiveData<Boolean>()
    val isOrderPlaced: LiveData<Boolean> = _isOrderPlaced

    fun setOrderPlaced(isOrderPlaced: Boolean) {
        _isOrderPlaced.value = isOrderPlaced
    }
}