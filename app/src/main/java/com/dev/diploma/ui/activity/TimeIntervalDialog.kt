package com.dev.diploma.ui.activity

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TimePicker
import com.dev.diploma.R

class TimeIntervalDialog(context: Context) : Dialog(context) {

    private var startTimePicker: TimePicker
    private var endTimePicker: TimePicker
    private var onIntervalSelectedListener: ((startTime: String, endTime: String) -> Unit)? = null

    init {
        setContentView(R.layout.dialog_time_interval)
        setTitle("Выберите временной интервал")
        startTimePicker = findViewById(R.id.startTimePicker)
        startTimePicker.setIs24HourView(true)
        startTimePicker.hour = 10
        startTimePicker.minute = 0
        endTimePicker = findViewById(R.id.endTimePicker)
        endTimePicker.setIs24HourView(true)
        endTimePicker.hour = 12
        endTimePicker.minute = 0
        findViewById<Button>(R.id.btnSelectInterval).setOnClickListener {
            val startTime = "${startTimePicker.hour}:${startTimePicker.minute}"
            val endTime = "${endTimePicker.hour}:${endTimePicker.minute}"
            onIntervalSelectedListener?.invoke(startTime, endTime)
            dismiss()
        }
    }

    fun setOnIntervalSelectedListener(listener: (startTime: String, endTime: String) -> Unit) {
        this.onIntervalSelectedListener = listener
    }
}
