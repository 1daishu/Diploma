package com.dev.diploma.ui.fragment.login

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.cli.Options
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mAuth: FirebaseAuth
    private var verificationId: String = ""


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
        appendTextPin()
        observeAppendText()
        observeAppendTextPin()
        mAuth = FirebaseAuth.getInstance()
        binding.verivCodetxt.setOnClickListener {
            if (TextUtils.isEmpty(binding.editTextPhone.text.toString())) {
                Toast.makeText(requireContext(), "Введите номер", Toast.LENGTH_SHORT).show()
            } else {
                val phone = "+7" + binding.editTextPhone.text.toString()
                sendVerificationCode(phone)
            }
        }
        binding.checkCode.setOnClickListener {
            if (TextUtils.isEmpty(binding.verivCodetxt.text.toString())) {
                Toast.makeText(requireContext(), "Введите код", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(binding.pinView.text.toString())
            }
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    findNavController().navigate(R.id.homeFragment)
                } else {

                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun sendVerificationCode(number: String) {

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)     // Номер телефона для проверки
            .setTimeout(60L, TimeUnit.SECONDS) // Тайм-аут и единица времени
            .setActivity(requireActivity())          // Активность (для привязки обратного вызова)
            .setCallbacks(mCallBack)    // Обратные вызовы OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(
            s: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(s, forceResendingToken)

            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                binding.pinView.setText(code)
                verifyCode(code)
            } else {
                Log.e("verify", "Received null code in onVerificationCompleted")
                Toast.makeText(
                    requireContext(),
                    "Received null code in onVerificationCompleted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
    }


    fun observeAppendText() {
        viewModel.textLiViewModel.observe(viewLifecycleOwner) { number ->
            binding.editTextPhone.setText(number)
        }
    }

    fun observeAppendTextPin() {
        viewModel.verifyCodeLiveData.observe(viewLifecycleOwner) { number ->
            binding.pinView.setText(number)
        }
    }

    private fun appendText() {
        binding.editTextPhone.setOnClickListener {
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
    }

    private fun appendTextPin() {
        binding.pinView.setOnClickListener {
            binding.button0.setOnClickListener { viewModel.appendTextPin("0") }
            binding.button1.setOnClickListener { viewModel.appendTextPin("1") }
            binding.button2.setOnClickListener { viewModel.appendTextPin("2") }
            binding.button3.setOnClickListener { viewModel.appendTextPin("3") }
            binding.button4.setOnClickListener { viewModel.appendTextPin("4") }
            binding.button5.setOnClickListener { viewModel.appendTextPin("5") }
            binding.button6.setOnClickListener { viewModel.appendTextPin("6") }
            binding.button7.setOnClickListener { viewModel.appendTextPin("7") }
            binding.button8.setOnClickListener { viewModel.appendTextPin("8") }
            binding.button9.setOnClickListener { viewModel.appendTextPin("9") }
            binding.buttonPlus.setOnClickListener { viewModel.appendTextPin("+") }
            binding.imageView.setOnClickListener { viewModel.clearTextPin() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
