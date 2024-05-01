package com.dev.diploma.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.diploma.R
import com.dev.diploma.data.network.dateFromJson.DateFromJson
import com.dev.diploma.databinding.FragmentHomeBinding
import com.dev.diploma.domain.model.User
import com.dev.diploma.ui.activity.MainActivity
import com.dev.diploma.ui.activity.SharedViewModel
import com.dev.diploma.ui.adapter.OrderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private var homeAdapter: OrderAdapter = OrderAdapter()
    private var isButtonClicked = false
    private var isButtonClickedTwo = false
    private var startIndex = 0
    private var isWeekButtonClicked = false
    private var isMonthButtonClicked = false
    private var isTwoWeekMonthButtonClicked = false
    private var endIndex = 3
    private var user = FirebaseAuth.getInstance().currentUser
    private var uid = user?.uid
    private var myRef = uid?.let {
        FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users").child(it)
    }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extracted()
        observeButtonVisible()
        updateDateBtOrders()
        prepareRecyclerView()
        switchMeals()
        setNameUser()
        switchDate()
        binding.btnOrder.setOnClickListener {
            val isOrderReady =
                (isButtonClicked || isButtonClickedTwo) && (isWeekButtonClicked || isMonthButtonClicked || isTwoWeekMonthButtonClicked)
            if (isOrderReady) {
                checkForExistingOrder()
            } else {
                Toast.makeText(requireContext(), "Выберите интервал или время", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkForExistingOrder() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            val ordersRef =
                FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users")
                    .child(currentUserUid)
                    .child("products") // Узел с заказами пользователя
            ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(
                            requireContext(),
                            "У вас уже есть активный заказ",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        findNavController().navigate(R.id.paymentFragment)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка при проверке заказа: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Пройдите регистрацию", Toast.LENGTH_SHORT).show()
        }
    }


    private fun switchDate() {
        binding.btOneWeek.setOnClickListener {
            isWeekButtonClicked = !isWeekButtonClicked
            if (isWeekButtonClicked) {
                binding.btOneWeek.setBackgroundResource(R.color.custom_yellow)
                binding.materialButton2.setBackgroundResource(R.color.white)
                binding.materialButton3.setBackgroundResource(R.color.white)
                isTwoWeekMonthButtonClicked = false
                isMonthButtonClicked = false
                sharedViewModel.setWeekButtonClicked(true)
                sharedViewModel.setMonthButtonClicked(false)
                sharedViewModel.setTwoWeekMonthButtonClicked(false)
            } else {
                binding.btOneWeek.setBackgroundResource(R.color.white)
            }
        }
        binding.materialButton2.setOnClickListener {
            isMonthButtonClicked = !isMonthButtonClicked
            if (isMonthButtonClicked) {
                binding.materialButton2.setBackgroundResource(R.color.custom_yellow)
                binding.btOneWeek.setBackgroundResource(R.color.white)
                binding.materialButton3.setBackgroundResource(R.color.white)
                isWeekButtonClicked = false
                isTwoWeekMonthButtonClicked = false
                sharedViewModel.setWeekButtonClicked(false)
                sharedViewModel.setMonthButtonClicked(false)
                sharedViewModel.setTwoWeekMonthButtonClicked(true)
            } else {
                binding.materialButton2.setBackgroundResource(R.color.white)
            }
        }

        binding.materialButton3.setOnClickListener {
            isTwoWeekMonthButtonClicked = !isTwoWeekMonthButtonClicked
            if (isTwoWeekMonthButtonClicked) {
                binding.materialButton3.setBackgroundResource(R.color.custom_yellow)
                binding.btOneWeek.setBackgroundResource(R.color.white)
                binding.materialButton2.setBackgroundResource(R.color.white)
                isWeekButtonClicked = false
                isMonthButtonClicked = false
                sharedViewModel.setWeekButtonClicked(false)
                sharedViewModel.setMonthButtonClicked(true)
                sharedViewModel.setTwoWeekMonthButtonClicked(false)
            } else {
                binding.materialButton3.setBackgroundResource(R.color.white)
            }
        }
    }

    private fun switchMeals() {
        binding.btnDishesThree.setOnClickListener {
            isButtonClicked = !isButtonClicked
            if (isButtonClicked) {
                binding.btnDishesThree.setBackgroundResource(R.color.custom_yellow)
                binding.btnDishesFour.setBackgroundResource(R.color.white)
                isButtonClickedTwo = false
                sharedViewModel.setButtonClicked(true)
                sharedViewModel.setButtonClickedTwo(false)
            } else {
                binding.btnDishesThree.setBackgroundResource(R.color.white)
            }
        }
        binding.btnDishesFour.setOnClickListener {
            isButtonClickedTwo = true
            isButtonClicked = false
            if (isButtonClickedTwo) {
                binding.btnDishesThree.setBackgroundResource(R.color.white)
                binding.btnDishesFour.setBackgroundResource(R.color.custom_yellow)
                sharedViewModel.setButtonClicked(false)
                sharedViewModel.setButtonClickedTwo(true)
            } else {
                binding.btnDishesFour.setBackgroundResource(R.color.white)
            }
        }
    }

    private fun setNameUser() {
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val name = user?.firstName
                val address = user?.address
                if (name != null) {
                    binding.txtGreeting.text = "Привет, $name!"
                } else {
                    binding.txtGreeting.text = "Привет!"
                }
                if (address != null) {
                    binding.txAddress.text = address.toString()
                } else {
                    binding.txAddress.text = "Адрес не указан"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ошибка,повторите попытку позже", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateDateBtOrders() {
        binding.dateButton.setOnClickListener {
            if (isButtonClicked) {
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
            if (isButtonClicked) {
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
            if (isButtonClicked) {
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

}