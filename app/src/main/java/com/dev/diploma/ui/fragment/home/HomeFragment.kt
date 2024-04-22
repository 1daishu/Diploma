package com.dev.diploma.ui.fragment.home

import OrderAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.diploma.data.network.dateFromJson.DateFromJson
import com.dev.diploma.databinding.FragmentHomeBinding
import com.dev.diploma.ui.activity.MainActivity
import com.dev.diploma.ui.activity.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: OrderAdapter
    private var isButtonClicked = false
    private var startIndex = 0
    private var endIndex = 3

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extracted()
        observeButtonVisible()
        updateDateBtOrders()
        prepareRecyclerView()
        binding.btnDishesFour.setOnClickListener {
            isButtonClicked = !isButtonClicked
        }
        binding.btnDishesThree.setOnClickListener {
            isButtonClicked = !isButtonClicked
        }
    }

    private fun updateDateBtOrders() {
        binding.dateButton.setOnClickListener {
            if (!isButtonClicked) {
                startIndex = 0
                endIndex = 3
                updateAdapterData()
            } else {
                startIndex = 0
                endIndex = 4
                updateAdapterData()
            }
        }
        binding.dateButtonNext.setOnClickListener {
            if (!isButtonClicked) {
                startIndex = 4
                endIndex = 7
                updateAdapterData()
            } else {
                startIndex = 5
                endIndex = 9
                updateAdapterData()
            }
        }
        binding.dateButtonNextNext.setOnClickListener {
            if (!isButtonClicked) {
                startIndex = 8
                endIndex = 11
                updateAdapterData()
            } else {
                startIndex = 10
                endIndex = 14
                updateAdapterData()
            }
        }
    }

    private fun updateAdapterData() {
        val dateFromJson = DateFromJson()
        val mealItems = dateFromJson.loadMealItemsFromJson(requireContext())
        endIndex = minOf(endIndex, mealItems.size)
        val sublist = mealItems.subList(startIndex, endIndex)
        homeAdapter.submitMealItems(sublist)
    }

    private fun prepareRecyclerView() {
        homeAdapter = OrderAdapter()
        binding.rvOrderHome.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = homeAdapter
        }
        updateAdapterData()
    }

    private fun observeButtonVisible() {
        sharedViewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                (activity as? MainActivity)?.showButtonLoginToHome()
                sharedViewModel.onHomeNavigated()
            }
        }
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