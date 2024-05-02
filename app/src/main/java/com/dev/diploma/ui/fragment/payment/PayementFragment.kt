package com.dev.diploma.ui.fragment.payment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.data.network.dateFromJson.DateFromJson
import com.dev.diploma.databinding.FragmentPaymentBinding
import com.dev.diploma.domain.model.User
import com.dev.diploma.ui.activity.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var user = FirebaseAuth.getInstance().currentUser
    private var uid = user?.uid
    private var myRef = uid?.let {
        FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users").child(it)
    }
    private var isPaymentOnlineCardViewClicked = false
    private var isPaymentOfflineNal = false
    private var isPaymentOfflineCardViewClicked = false
    private var isTime = false
    private var isTimeTwo = false
    private var isTimeThree = false

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButton()
        setNameAndMail()
        binding.txSnap.setOnClickListener {
            openDatePicker()
        }
        setAddress()
        switchDate()
        changeDataTime()

        binding.btPlaceOrder.setOnClickListener {
            val isDateSelected = binding.txSnap.text.toString() != "Выберите дату"
            val isOrderReady =
                (isTime || isTimeThree || isTimeTwo) &&
                        (isPaymentOfflineCardViewClicked || isPaymentOfflineNal || isPaymentOnlineCardViewClicked)
                        && (binding.tvMailUser.text.isNotBlank())
                        && (binding.tvNameUser.text.isNotBlank())
                        && (binding.edNumberFlat.text.isNotBlank())
                        && (binding.edHome.text.isNotBlank())
                        && (binding.edNumberEntrance.text.isNotBlank())
                        && (isDateSelected)
            if (isOrderReady) {
                val navController = findNavController()
                navController.popBackStack(R.id.paymentFragment, false)
                navController.navigate(R.id.homeFragment)
                placeOrder()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Заполните все данные",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun changeDataTime() {
        binding.btnDateOne.setOnClickListener {
            isTime = !isTime
            if (isTime) {
                binding.btnDateOne.setBackgroundResource(R.color.custom_yellow)
                binding.btnDateTwo.setBackgroundResource(R.color.white)
                binding.btnDateThree.setBackgroundResource(R.color.white)
                isTimeTwo = false
                isTimeThree = false

            } else {
                binding.btnDateOne.setBackgroundResource(R.color.white)
            }
        }
        binding.btnDateTwo.setOnClickListener {
            isTimeTwo = !isTimeTwo
            if (isTimeTwo) {
                binding.btnDateTwo.setBackgroundResource(R.color.custom_yellow)
                binding.btnDateOne.setBackgroundResource(R.color.white)
                binding.btnDateThree.setBackgroundResource(R.color.white)
                isTime = false
                isTimeThree = false

            } else {
                binding.btnDateTwo.setBackgroundResource(R.color.white)
            }
        }

        binding.btnDateThree.setOnClickListener {
            isTimeThree = !isTimeThree
            if (isTimeThree) {
                binding.btnDateThree.setBackgroundResource(R.color.custom_yellow)
                binding.btnDateTwo.setBackgroundResource(R.color.white)
                binding.btnDateOne.setBackgroundResource(R.color.white)
                isTime = false
                isTimeTwo = false

            } else {
                binding.btnDateThree.setBackgroundResource(R.color.white)
            }
        }

    }

    private fun switchDate() {
        binding.cvCardPayment.setOnClickListener {
            isPaymentOnlineCardViewClicked = !isPaymentOnlineCardViewClicked
            if (isPaymentOnlineCardViewClicked) {
                binding.cvCardPayment.setBackgroundResource(R.color.custom_yellow)
                binding.cvCardOffline.setBackgroundResource(R.color.white)
                binding.cvCardUponReceipt.setBackgroundResource(R.color.white)
                isPaymentOfflineNal = false
                isPaymentOfflineCardViewClicked = false

            } else {
                binding.cvCardPayment.setBackgroundResource(R.color.white)
            }
        }
        binding.cvCardOffline.setOnClickListener {
            isPaymentOfflineNal = !isPaymentOfflineNal
            if (isPaymentOfflineNal) {
                binding.cvCardOffline.setBackgroundResource(R.color.custom_yellow)
                binding.cvCardPayment.setBackgroundResource(R.color.white)
                binding.cvCardUponReceipt.setBackgroundResource(R.color.white)
                isPaymentOnlineCardViewClicked = false
                isPaymentOfflineCardViewClicked = false

            } else {
                binding.cvCardOffline.setBackgroundResource(R.color.white)
            }
        }

        binding.cvCardUponReceipt.setOnClickListener {
            isPaymentOfflineCardViewClicked = !isPaymentOfflineCardViewClicked
            if (isPaymentOfflineCardViewClicked) {
                binding.cvCardUponReceipt.setBackgroundResource(R.color.custom_yellow)
                binding.cvCardPayment.setBackgroundResource(R.color.white)
                binding.cvCardOffline.setBackgroundResource(R.color.white)
                isPaymentOfflineNal = false
                isPaymentOnlineCardViewClicked = false

            } else {
                binding.cvCardUponReceipt.setBackgroundResource(R.color.white)
            }
        }

    }

    private fun setAddress() {
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val address = user?.address
                if (address != null) {
                    binding.txAddressDelivery.text = "Адрес: $address"
                } else {
                    binding.txAddressDelivery.text = "Заполните адресс в профиле"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Ошибка,повторите попытку позже",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                if (selectedDate.before(calendar) || isToday(selectedDate)) {
                    Toast.makeText(
                        requireContext(),
                        "Доставка на сегодня и на прошедшие дни не доступна",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.txSnap.text =
                        SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.getDefault()
                        ).format(selectedDate.time)
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun isToday(calendar: Calendar): Boolean {
        val todayCalendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun setNameAndMail() {
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val name = user?.firstName
                val emailUser = user?.email
                if (name != null) {
                    binding.tvNameUser.text = name
                } else {
                    binding.tvNameUser.text = ""
                }
                if (emailUser != null) {
                    binding.tvMailUser.text = emailUser
                } else {
                    binding.txAddress.text = ""
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Ошибка,повторите попытку позже", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setButton() {
        if (sharedViewModel.isWeekButtonClicked.value == true && sharedViewModel.isButtonClickedTwo.value == true) {
            addDataToDatabase("4 блюда ,1 неделя")
            binding.tvPriceOrder.text = "5499Р"
        }
        if (sharedViewModel.isTwoWeekMonthButtonClicked.value == true && sharedViewModel.isButtonClickedTwo.value == true) {
            addDataToDatabase("4 блюда ,2 недели")
            binding.tvPriceOrder.text = "9999Р"
        }
        if (sharedViewModel.isMonthButtonClicked.value == true && sharedViewModel.isButtonClickedTwo.value == true) {
            addDataToDatabase("4 блюда ,1 месяц")
            binding.tvPriceOrder.text = "11999Р"
        }
        if (sharedViewModel.isWeekButtonClicked.value == true && sharedViewModel.isButtonClicked.value == true) {
            addDataToDatabase("3 блюда ,1 неделя")
            binding.tvPriceOrder.text = "3499Р"
        }
        if (sharedViewModel.isTwoWeekMonthButtonClicked.value == true && sharedViewModel.isButtonClicked.value == true) {
            addDataToDatabase("3 блюда ,2 недели")
            binding.tvPriceOrder.text = "7999Р"
        }
        if (sharedViewModel.isMonthButtonClicked.value == true && sharedViewModel.isButtonClicked.value == true) {
            addDataToDatabase("3 блюда ,1 месяц")
            binding.tvPriceOrder.text = "9999Р"
        }
    }

    private fun addDataToDatabase(data: String) {
        val databaseReference =
            FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        uid?.let { userId ->
            val userReference = databaseReference.child(userId)
            val updates = mutableMapOf<String, Any>()
            updates["time_product"] = data
            userReference.updateChildren(updates)
                .addOnSuccessListener {
                    Log.d("Success", "Данные добавлены")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Failed to add time product data: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    private fun placeOrder() {
        val dateFromJson = DateFromJson()
        val mealItems = dateFromJson.loadMealItemsFromJson(requireContext())
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val database =
                FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
            val reference = database.getReference("users").child(uid)
            val updates = mutableMapOf<String, Any>()
            updates["products"] = mealItems
            reference.updateChildren(updates)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Доставка офомлена, посмотрите в ваших товарах",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Failed to place order: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } ?: run {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT)
                .show()
        }
    }


}
