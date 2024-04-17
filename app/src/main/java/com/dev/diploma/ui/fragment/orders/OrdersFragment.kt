package com.dev.diploma.ui.fragment.orders

import OrdersViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dev.diploma.databinding.FragmentOrdersBinding
import java.text.SimpleDateFormat
import java.util.Locale


class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private lateinit var viewModel: OrdersViewModel

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]
        viewModel.getCurrentDateLiveData().observe(viewLifecycleOwner) {
            binding.dateButton.text = it

        }
        val nextDayCalendar = viewModel.getCurrentDatePlusOneDay()
        val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
        binding.dateButtonNext.text = dateFormat.format(nextDayCalendar.time)
        val nextNextDayCalendar = viewModel.getCurrentDatePlusTwoDay()
        val dateFormatTwo = SimpleDateFormat("dd.MM", Locale.getDefault())
        binding.dateButtonNextNext.text = dateFormatTwo.format(nextNextDayCalendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}