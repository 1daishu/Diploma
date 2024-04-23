package com.dev.diploma.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentProfileBinding
import com.dev.diploma.domain.model.User
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
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid
    val myRef = uid?.let {
        FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users").child(it)
    }

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
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val address = user?.address
                val numberCard = user?.payment
                val name = user?.firstName.toString()
                val lastName = user?.lastName.toString()
                binding.tvAddressProfile.text = address.toString()
                binding.numberCard.text = numberCard.toString()
                binding.nameAndLastName.text = name + " " + lastName
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки при чтении данных
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}