package com.dev.diploma.domain.repository

interface VerificationCodeSentListener {
    fun onCodeSent()
    fun onError(message: String)
}