package com.dev.diploma.ui.fragment.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev.diploma.data.network.dateFromJson.DateFromJson
import com.dev.diploma.databinding.FragmentPaymentBinding
import com.dev.diploma.ui.activity.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()


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
        binding.btPlaceOrder.setOnClickListener {
            placeOrder()
        }
        if (sharedViewModel.isWeekButtonClicked.value == true && sharedViewModel.isButtonClickedTwo.value == true) {
            addDataToDatabase("4 блюда ,1 неделя")
        }
        if (sharedViewModel.isTwoWeekMonthButtonClicked.value == true && sharedViewModel.isButtonClickedTwo.value == true) {
            addDataToDatabase("4 блюда ,2 недели")
        }
        if (sharedViewModel.isMonthButtonClicked.value == true && sharedViewModel.isButtonClickedTwo.value == true) {
            addDataToDatabase("4 блюда ,1 месяц")
        }
        if (sharedViewModel.isWeekButtonClicked.value == true && sharedViewModel.isButtonClicked.value == true) {
            addDataToDatabase("3 блюда ,1 неделя")
        }
        if (sharedViewModel.isTwoWeekMonthButtonClicked.value == true && sharedViewModel.isButtonClicked.value == true) {
            addDataToDatabase("3 блюда ,2 недели")
        }
        if (sharedViewModel.isMonthButtonClicked.value == true && sharedViewModel.isButtonClicked.value == true) {
            addDataToDatabase("3 блюда ,1 месяц")
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
                    Toast.makeText(
                        requireContext(),
                        "Time product data added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
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
                        "Order placed successfully",
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
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
