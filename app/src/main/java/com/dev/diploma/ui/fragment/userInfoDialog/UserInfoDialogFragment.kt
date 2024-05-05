package com.dev.diploma.ui.fragment.userInfoDialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dev.diploma.R
import com.dev.diploma.databinding.FragmentUserInfoDialogBinding
import com.dev.diploma.domain.model.UserInfoAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserInfoDialogFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoDialogBinding
    private var aut: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference
    private var user = FirebaseAuth.getInstance().currentUser
    private var uid = user?.uid
    private var myRef = uid?.let {
        FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users_num_auth").child(it)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeInfoUser()
        binding.tvBackChangeDate.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        setNumber()
        binding.edPaymentCvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val sb = StringBuilder(it)
                    for (i in start until start + count) {
                        sb.setCharAt(i, '*')
                    }
                    binding.edPaymentCvv.removeTextChangedListener(this)
                    binding.edPaymentCvv.setText(sb)
                    binding.edPaymentCvv.setSelection(start + count)
                    binding.edPaymentCvv.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.edPaymentTerm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length == 2 && count == 1) {
                        binding.edPaymentTerm.setText("$s/")
                        binding.edPaymentTerm.setSelection(binding.edPaymentTerm.text.length)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setNumber() {
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val numberUser = snapshot.getValue(UserInfoAuth::class.java)
                val number = numberUser?.number
                if (number != null) {
                    binding.edNumberPhone.text = "Номер телефона\n$number"
                } else {
                    binding.edNumberPhone.text = ""
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Повторите попытку позже", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    private fun changeInfoUser() {
        val uid = aut.currentUser?.uid
        databaseReference =
            FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")
        binding.saveButton.setOnClickListener {
            val firstName = binding.edName.text.toString()
            val lastName = binding.edLastName.text.toString()
            val email = binding.edMail.text.toString()
            val address = binding.edAddress.text.toString()
            val payment = binding.edPayment.text.toString()
            val cvv = binding.edPaymentCvv.text.toString()
            val term = binding.edPaymentTerm.text.toString()
            val updates = mutableMapOf<String, Any>()

            if (firstName.isNotEmpty()) {
                updates["firstName"] = firstName
            }

            if (lastName.isNotEmpty()) {
                updates["lastName"] = lastName
            }

            if (email.isNotEmpty()) {
                updates["email"] = email
            }

            if (address.isNotEmpty()) {
                updates["address"] = address
            }

            if (payment.isNotEmpty()) {
                updates["payment"] = payment
            }

            if (cvv.isNotEmpty()) {
                updates["cvv"] = cvv
            }

            if (term.isNotEmpty()) {
                updates["term"] = term
            }

            if (uid != null) {
                try {
                    databaseReference.child(uid).updateChildren(updates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Данные успешно обновлены",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Не удалось обновить данные",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}