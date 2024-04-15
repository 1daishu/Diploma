package com.dev.diploma.ui.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private lateinit var auth: FirebaseAuth
    val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            val email = binding.mailEtc.text.toString()
            val password = binding.passwordEtc.text.toString()
            registerUser(email, password)
        }
        binding.btnGoLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                uid?.let {
                    saveRegistrationInfoToDatabase(it)
                }
                findNavController().navigate(R.id.homeFragment)

            } else {

            }
        }
    }

    private fun saveRegistrationInfoToDatabase(uid: String) {
        val database = FirebaseDatabase.getInstance()
            val registration = database.getReference("registrations").child(uid)
        val timetamp = System.currentTimeMillis()
        registration.setValue(timetamp)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
