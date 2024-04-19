package com.dev.diploma.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentHomeBinding
import com.dev.diploma.ui.activity.MainActivity
import com.dev.diploma.ui.activity.SharedViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
        observeButtonVisible()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navController = findNavController()
            navController.popBackStack(R.id.loginFragment, false)
        }
    }

    private fun observeButtonVisible() {
        sharedViewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                (activity as? MainActivity)?.showButton()
                sharedViewModel.onHomeNavigated()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}