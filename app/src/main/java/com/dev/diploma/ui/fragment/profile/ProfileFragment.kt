package com.dev.diploma.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentProfileBinding
import com.dev.diploma.domain.model.User
import com.dev.diploma.domain.model.UserInfoAuth
import com.dev.diploma.ui.activity.MainActivity
import com.dev.diploma.ui.activity.SharedViewModel
import com.dev.diploma.ui.fragment.userInfoDialog.UserInfoDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var userInfoDialogFragment: UserInfoDialogFragment
    private val user = FirebaseAuth.getInstance().currentUser
    private val uid = user?.uid
    private val myRef = uid?.let {
        FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users").child(it)
    }
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userInfoDialogFragment = UserInfoDialogFragment()
        observeButtonVisibleProfile()
        binding.btExitProfile.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
            sharedViewModel.navigateToHome()
        }
        binding.btnChangeDate.setOnClickListener {
            findNavController().navigate(R.id.userInfoDialogFragment)
        }
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val address = user?.address
                val numberCard = user?.payment
                val name = user?.firstName
                val lastName = user?.lastName
                if (name != null && lastName != null) {
                    binding.nameAndLastName.text = "$name $lastName"
                } else {
                    binding.nameAndLastName.text = ""
                }

                if (address != null) {
                    binding.tvAddressProfile.text = address.toString()
                } else {
                    binding.tvAddressProfile.text = ""
                }

                if (numberCard != null) {
                    binding.numberCard.text = numberCard.toString()
                } else {
                    binding.numberCard.text = ""
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Повторите попытку позже", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun observeButtonVisibleProfile() {
        sharedViewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                (activity as? MainActivity)?.noShowButtonProfileToLogin()
                sharedViewModel.onHomeNavigated()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
