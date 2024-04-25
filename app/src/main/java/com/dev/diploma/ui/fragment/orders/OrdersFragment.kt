package com.dev.diploma.ui.fragment.orders

import OrderAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentOrdersBinding
import com.dev.diploma.ui.fragment.currentProduct.CurrentProductsFragment
import com.dev.diploma.ui.fragment.historyProduct.HistoricalProductsFragment


class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null

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
        displayCurrentProductsFragment()
        switchFragments()
    }

    private fun displayCurrentProductsFragment() {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val currentFragment = CurrentProductsFragment()
        transaction.replace(
            R.id.currentFragmentContainer,
            currentFragment
        )
        transaction.commit()
    }

    private fun switchFragments() {
        binding.txCurrentProduct.setOnClickListener {
            replaceFragment(CurrentProductsFragment())
        }
        binding.txHistoryProduct.setOnClickListener {
            replaceFragment(HistoricalProductsFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.currentFragmentContainer, fragment)
        fragmentTransaction.replace(R.id.currentFragmentContainer, fragment)
        fragmentTransaction.commit()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}