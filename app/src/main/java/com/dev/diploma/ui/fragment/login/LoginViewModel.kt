package com.dev.diploma.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _textLiveData = MutableLiveData<String>()
    val textLiViewModel: LiveData<String> = _textLiveData
    private val _verifyLiveData = MutableLiveData<String>()
    val verifyCodeLiveData = _verifyLiveData

    init {
        _textLiveData.value = ""
        _verifyLiveData.value = ""
    }

    fun appendText(text: String) {
        val currentText = _textLiveData.value ?: ""
        _textLiveData.value = currentText + text
    }

    fun appendTextPin(text: String) {
        val currentText1 = _verifyLiveData.value ?: ""
        _verifyLiveData.value = currentText1 + text
    }

    fun clearText() {
        _textLiveData.value = ""
    }

    fun clearTextPin() {
        _verifyLiveData.value = ""
    }


}