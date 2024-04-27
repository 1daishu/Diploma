package com.dev.diploma.ui.fragment.currentProduct

import OrderAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.diploma.R
import com.dev.diploma.data.network.dateFromJson.DateFromJson
import com.dev.diploma.databinding.FragmentCurrentProductsBinding
import com.dev.diploma.ui.fragment.orders.OrdersViewModel
import com.dev.diploma.ui.fragment.payment.PaymentViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class CurrentProductsFragment : Fragment() {
    private var _binding: FragmentCurrentProductsBinding? = null
    private val orderViewModel: PaymentViewModel by activityViewModels()
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter
    private var currentIndex = 0
    private val itemsPerPage = 3


    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentProductsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extracted()
        prepareRecyclerView()
        updateDateBtOrders()


    }

    private fun updateDateBtOrders() {
        binding.dateButtonNext.setOnClickListener {
            currentIndex += itemsPerPage
            updateAdapterData(currentIndex, itemsPerPage)
        }

        binding.dateButtonNextNext.setOnClickListener {
            currentIndex += itemsPerPage
            updateAdapterData(currentIndex, itemsPerPage)
        }
    }

    private fun updateAdapterData(startIndex: Int, count: Int) {
        val dateFromJson = DateFromJson()
        val mealItems = dateFromJson.loadMealItemsFromJson(requireContext())
        val sublist = mealItems.subList(startIndex, startIndex + count)
        orderAdapter.submitMealItems(sublist)
    }

    private fun prepareRecyclerView() {
        orderAdapter = OrderAdapter()
        binding.rvOrder.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }
        updateAdapterData(currentIndex, itemsPerPage)
    }

    private fun extracted() {
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