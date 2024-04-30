package com.dev.diploma.ui.fragment.currentProduct

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentCurrentProductsBinding
import com.dev.diploma.domain.model.Product
import com.dev.diploma.domain.model.User
import com.dev.diploma.ui.adapter.CurrentProductsAdapter
import com.dev.diploma.ui.fragment.orders.OrdersViewModel
import com.dev.diploma.ui.fragment.payment.PaymentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale


class CurrentProductsFragment : Fragment() {
    private var _binding: FragmentCurrentProductsBinding? = null
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var currentProductAdapter: CurrentProductsAdapter
    private var startIndex = 0
    private var endIndex = 3

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extracted()
        prepareRecyclerView()
        productWithdrawal()
        setChangeMeals()
        getProductFirebase()
        binding.btNewOrder.postDelayed({
            binding.btNewOrder.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.homeFragment)
            }
        }, 2000)
    }

    private fun getProductFirebase() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val myRef = uid?.let {
            FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(it)
        }

        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val countMeal = user?.time_product
                if (countMeal?.contains("4 блюда") == true) {
                    binding.dateButton.setOnClickListener {
                        startIndex = 0
                        endIndex = 4
                        productWithdrawal()

                    }
                    binding.dateButtonNext.setOnClickListener {
                        startIndex = 5
                        endIndex = 9
                        productWithdrawal()

                    }
                    binding.dateButtonNextNext.setOnClickListener {
                        startIndex = 10
                        endIndex = 14
                        productWithdrawal()

                    }
                }
                if (countMeal?.contains("3 блюда") == true) {
                    binding.dateButton.setOnClickListener {
                        startIndex = 0
                        endIndex = 3
                        productWithdrawal()

                    }
                    binding.dateButtonNext.setOnClickListener {
                        startIndex = 4
                        endIndex = 7
                        productWithdrawal()
                    }
                    binding.dateButtonNextNext.setOnClickListener {
                        startIndex = 8
                        endIndex = 11
                        productWithdrawal()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to read count meal value.", error.toException())
            }
        })
    }

    private fun setChangeMeals() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val myRef = uid?.let {
            FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(it)
        }
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val time_product = user?.time_product
                binding.txUserChange.text = time_product
            }

            override fun onCancelled(error: DatabaseError) {
                TODO()
            }
        })
    }

    private fun productWithdrawal() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val database =
            FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
        val userProductsRef = database.getReference("users").child(uid!!).child("products")
        userProductsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (_binding != null) {
                    val productList = mutableListOf<Product>()
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let { productList.add(it) }
                    }

                    if (productList.isEmpty()) {
                        _binding!!.cvYourChanceOrder.visibility = View.GONE
                        _binding!!.constraintCurrent.visibility = View.GONE
                        _binding!!.btCancelOrders.visibility = View.GONE
                        _binding!!.cvNotHaveOrderMessage.visibility = View.VISIBLE
                        _binding!!.imgNewOrder.visibility = View.VISIBLE
                        _binding!!.cvMealsOrder.visibility = View.GONE
                        _binding!!.btNewOrder.visibility = View.VISIBLE
                    } else {
                        _binding!!.cvYourChanceOrder.visibility = View.VISIBLE
                        _binding!!.constraintCurrent.visibility = View.VISIBLE
                        _binding!!.btCancelOrders.visibility = View.VISIBLE
                        _binding!!.cvNotHaveOrderMessage.visibility = View.GONE
                        _binding!!.imgNewOrder.visibility = View.GONE
                        _binding!!.cvMealsOrder.visibility = View.VISIBLE
                        _binding!!.btNewOrder.visibility = View.GONE
                        currentProductAdapter.submitList(productList)

                        val filteredList = productList.filterIndexed { index, _ ->
                            index in startIndex until endIndex
                        }
                        currentProductAdapter.submitList(filteredList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки, если требуется
            }
        })
    }

    private fun prepareRecyclerView() {
        currentProductAdapter = CurrentProductsAdapter()
        binding.rvOrder.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = currentProductAdapter
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