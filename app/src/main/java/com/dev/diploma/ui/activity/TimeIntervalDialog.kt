package com.dev.diploma.ui.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.dev.diploma.databinding.DialogTimeIntervalBinding

class TimeIntervalDialog(context: Context) : Dialog(context) {

    private lateinit var binding: DialogTimeIntervalBinding
    private var onIntervalSelectedListener: ((startTime: String, endTime: String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogTimeIntervalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Выберите временной интервал")

        setupNumberPickers()
        binding.btnSelectInterval.setOnClickListener {
            val startTime = "${binding.startHourPicker.value}:${binding.startMinutePicker.value}"
            val endTime = "${binding.endHourPicker.value}:${binding.endMinutePicker.value}"
            onIntervalSelectedListener?.invoke(startTime, endTime)
            dismiss()
        }
    }

    private fun setupNumberPickers() {
        binding.startHourPicker.apply {
            minValue = 0
            maxValue = 23
            value = 8
        }
        binding.startMinutePicker.apply {
            minValue = 0
            maxValue = 59
            value = 0
        }
        binding.endHourPicker.apply {
            minValue = 0
            maxValue = 23
            value = 10
        }
        binding.endMinutePicker.apply {
            minValue = 0
            maxValue = 59
            value = 0
        }
    }

    fun setOnIntervalSelectedListener(listener: (startTime: String, endTime: String) -> Unit) {
        this.onIntervalSelectedListener = listener
    }
}
