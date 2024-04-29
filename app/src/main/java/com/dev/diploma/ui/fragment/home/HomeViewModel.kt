package com.dev.diploma.ui.fragment.home

import android.text.format.Time
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class HomeViewModel : ViewModel() {
    private val currentDateLiveData = MutableLiveData<String>()
    init {
        updateDate()
        Timer().scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    updateDate()
                }
            },
            getNextMidnight(),
            24 * 60 * 60 * 1000
        )
    }

    private fun updateDate() {
        val currentDate = SimpleDateFormat("dd.MM", Locale.getDefault()).format(Date())
        currentDateLiveData.postValue(currentDate)
    }

    fun getCurrentDatePlusOneDay(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar
    }

    fun getCurrentDatePlusTwoDay(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        return calendar
    }

    fun getCurrentDateLiveData(): MutableLiveData<String> {
        return currentDateLiveData
    }

    private fun getNextMidnight(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

}
