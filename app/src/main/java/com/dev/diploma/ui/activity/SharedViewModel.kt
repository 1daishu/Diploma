package com.dev.diploma.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _navigateToHome = MutableLiveData<Boolean>()
    var isWeekButtonClicked = MutableLiveData<Boolean>()
    var isMonthButtonClicked = MutableLiveData<Boolean>()
    var isTwoWeekMonthButtonClicked = MutableLiveData<Boolean>()
    var isButtonClicked = MutableLiveData<Boolean>()
    var isButtonClickedTwo = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    fun navigateToHome() {
        _navigateToHome.value = true
    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }

    fun setWeekButtonClicked(isClicked: Boolean) {
        isWeekButtonClicked.value = isClicked
    }

    fun setMonthButtonClicked(isClicked: Boolean) {
        isMonthButtonClicked.value = isClicked
    }

    fun setTwoWeekMonthButtonClicked(isClicked: Boolean) {
        isTwoWeekMonthButtonClicked.value = isClicked
    }

    fun setButtonClicked(isClicked: Boolean) {
        isButtonClicked.value = isClicked
    }

    fun setButtonClickedTwo(isClicked: Boolean) {
        isButtonClickedTwo.value = isClicked
    }
}