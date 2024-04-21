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
    private var currentIndex = 0
    private val itemsPerPage = 3

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
    }

    private fun updateDateBtOrders() {
        binding.dateButtonNext.setOnClickListener {
            currentIndex += itemsPerPage
            updateAdapterData(currentIndex, itemsPerPage)
        }
    }

    private fun updateAdapterData(currentIndex: Int, itemsPerPage: Int) {
        val dateFromJson = DateFromJson()
        val mealItems = dateFromJson.loadMealItemsFromJson(requireContext())
        val sublist = mealItems.subList(currentIndex, currentIndex + itemsPerPage)
        homeAdapter.submitMealItems(sublist)
    }

    private fun prepareRecyclerView() {
        homeAdapter = OrderAdapter()
        binding.rvOrderHome.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }
        updateAdapterData(currentIndex, itemsPerPage)
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