package com.dev.diploma.ui.fragment.login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentLoginBinding
import com.dev.diploma.domain.model.UserInfoAuth
import com.dev.diploma.ui.activity.SharedViewModel
import com.dev.diploma.ui.fragment.home.HomeFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit


class LoginFragment : Fragment() {
    interface OnBackPressedListener {
        fun onBackPressed()
    }

    private var onBackPressedListener: HomeFragment.OnBackPressedListener? = null

    private var _binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mAuth: FirebaseAuth
    private var verificationId: String = ""
    private val sharedViewModel: SharedViewModel by activityViewModels()


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
        binding.txtCodeVerify.setOnClickListener {
            if (TextUtils.isEmpty(binding.editTextPhone.text.toString())) {
                Toast.makeText(requireContext(), "Введите номер", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Код отправлен", Toast.LENGTH_SHORT).show()
                val phone = binding.editTextPhone.text.toString()
                sendVerificationCode(phone)
            }
        }
        binding.checkCode.setOnClickListener {
            if (TextUtils.isEmpty(binding.txtCodeVerify.text.toString())) {
                Toast.makeText(requireContext(), "Введите код", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(binding.pinView.text.toString())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressedListener?.onBackPressed()
        }
    }

    private fun onLoginSuccess() {
        sharedViewModel.navigateToHome()
    }

    private fun verifyCode(code: String) {
        if (TextUtils.isEmpty(code) && code.length < 6) {
            Toast.makeText(requireContext(), "Введите код", Toast.LENGTH_SHORT).show()
        } else {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            signInWithCredential(credential)
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        val userNumber = binding.editTextPhone.text.toString()
        val userInfoNumberAuth = UserInfoAuth(userNumber)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = mAuth.currentUser
                val uid = currentUser?.uid
                if (uid != null) {
                    val databaseReference =
                        FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("users_num_auth").child(uid)
                    databaseReference.setValue(userInfoNumberAuth)
                        .addOnCompleteListener { userDataTask ->
                            if (userDataTask.isSuccessful) {
                                onLoginSuccess()
                                val navController = findNavController()
                                navController.navigate(R.id.action_loginFragment_to_homeFragment)
                                viewModel.clearText()
                                viewModel.clearTextPin()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Ошибка при добавлении пользователя в базу данных",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    TODO()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    task.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallBack)
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
                Toast.makeText(
                    requireContext(),
                    "Неверный код, повторите попытку заново",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeAppendText() {
        viewModel.textLiViewModel.observe(viewLifecycleOwner) { number ->
            binding.editTextPhone.setText(number)
        }
    }

    private fun observeAppendTextPin() {
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
