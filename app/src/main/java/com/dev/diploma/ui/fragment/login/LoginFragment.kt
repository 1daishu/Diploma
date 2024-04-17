package com.dev.diploma.ui.fragment.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginViewModel by activityViewModels()
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
        appendText()
        observeAppendText()
        extracted()
    }

    private fun extracted() {
        binding.editTextPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 12) {
                    navigateToNext()
                }
            }
        })
    }

    private fun navigateToNext() {
        if (binding.editTextPhone.length() == 12) {
            findNavController().navigate(R.id.verifyCodeFragment)
        }
    }

    private fun appendText() {
        binding.button0.setOnClickListener { viewModel.appendText("0") }
        binding.button1.setOnClickListener { viewModel.appendText("1") }
        binding.button2.setOnClickListener { viewModel.appendText("2") }
        binding.button3.setOnClickListener { viewModel.appendText("3") }
        binding.button4.setOnClickListener { viewModel.appendText("4") }
        binding.button5.setOnClickListener { viewModel.appendText("5") }
        binding.button6.setOnClickListener { viewModel.appendText("6") }
        binding.button7.setOnClickListener { viewModel.appendText("7") }
        binding.button8.setOnClickListener { viewModel.appendText("8") }
        binding.button9.setOnClickListener { viewModel.appendText("9") }
        binding.buttonPlus.setOnClickListener { viewModel.appendText("+") }
        binding.imageView.setOnClickListener { viewModel.clearText() }
    }

    fun observeAppendText() {
        viewModel.textLiViewModel.observe(viewLifecycleOwner) { number ->
            binding.editTextPhone.setText(number)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}