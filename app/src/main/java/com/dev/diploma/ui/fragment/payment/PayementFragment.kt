package com.dev.diploma.ui.fragment.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.data.network.dateFromJson.DateFromJson
import com.dev.diploma.databinding.FragmentPaymentBinding
import com.dev.diploma.domain.model.MealItem
import com.dev.diploma.ui.activity.TimeIntervalDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null


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

    }

    private fun placeOrder() {
        val dateFromJson = DateFromJson()
        val mealItems = dateFromJson.loadMealItemsFromJson(requireContext())
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val database =
                FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
            val reference = database.getReference("users").child(uid).child("products")
            reference.setValue(mealItems)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Order placed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navigate to success screen or perform other actions as needed
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
