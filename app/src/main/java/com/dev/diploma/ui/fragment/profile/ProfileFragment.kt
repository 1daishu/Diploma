package com.dev.diploma.ui.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentProfileBinding
import com.dev.diploma.ui.activity.MainActivity
import com.dev.diploma.ui.activity.SharedViewModel
import com.dev.diploma.ui.fragment.userInfoDialog.UserInfoDialogFragment


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var userInfoDialogFragment: UserInfoDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfoDialogFragment = UserInfoDialogFragment()
    }

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btExitProfile.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
            sharedViewModel.navigateToHome()
            observeButtonVisibleProfile()
        }
        binding.btnChangeDate.setOnClickListener {
            findNavController().navigate(R.id.userInfoDialogFragment)
        }
    }

    private fun observeButtonVisibleProfile() {
        sharedViewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                (activity as? MainActivity)?.noShowButtonProfileToLogin()
                sharedViewModel.onHomeNavigated()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}