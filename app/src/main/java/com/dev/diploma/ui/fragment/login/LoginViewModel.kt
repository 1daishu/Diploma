package com.dev.diploma.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _textLiveData = MutableLiveData<String>()
    val textLiViewModel: LiveData<String> = _textLiveData

    init {
        _textLiveData.value = ""
    }

    fun appendText(text: String) {
        val currentText = _textLiveData.value ?: "+7"
        _textLiveData.value = currentText + text
    }

    fun clearText() {
        _textLiveData.value = ""
    }
}