package com.dev.diploma.ui.fragment.userInfoDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.diploma.databinding.FragmentUserInfoDialogBinding
import com.dev.diploma.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserInfoDialogFragment : Fragment() {
    private var _binding: FragmentUserInfoDialogBinding? = null
    private lateinit var aut: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInfoDialogBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aut = FirebaseAuth.getInstance()
        val uid = aut.currentUser?.uid
        databaseReference =
            FirebaseDatabase.getInstance("https://safeauthfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")
        binding.saveButton.setOnClickListener {
            val userName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val user = User(userName, lastName, email)
            if (uid != null) {
                try {
                    databaseReference.child(uid).setValue(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "user add", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "unlucky", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}