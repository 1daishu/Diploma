package com.dev.diploma.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}