package com.alankurniadi.storyapp.authentication.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.authentication.register.RegisterFragment
import com.alankurniadi.storyapp.dataStore
import com.alankurniadi.storyapp.databinding.FragmentLoginBinding
import com.alankurniadi.storyapp.home.ListStoryViewModel
import com.alankurniadi.storyapp.utils.SettingPreferences
import com.alankurniadi.storyapp.utils.ViewModelFactory
import com.alankurniadi.storyapp.utils.isEmailValid

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "LoginFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val loginVm = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
        val listStoryVm =
            ViewModelProvider(this, ViewModelFactory(pref))[ListStoryViewModel::class.java]

        with(binding) {
            edLoginEmail.doOnTextChanged { text, _, _, _ ->
                if (!isEmailValid(text.toString())) {
                    tilLoginEmail.isErrorEnabled = true
                    tilLoginEmail.error = getString(R.string.label_error_message_email_failed)
                } else {
                    tilLoginEmail.isErrorEnabled = false
                }
            }

            edLoginPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.length < 6) {
                    tilLoginPassword.isErrorEnabled = true
                    tilLoginPassword.error = getString(R.string.label_error_message_password)
                } else {
                    tilLoginPassword.isErrorEnabled = false
                }
            }

            btnLogin.setOnClickListener {
                loginProgressbar.visibility = View.VISIBLE
                val email = edLoginEmail.text.toString().trim()
                val pass = edLoginPassword.text.toString().trim()

                if (email.isEmpty()) {
                    tilLoginEmail.isErrorEnabled = true
                    tilLoginEmail.error = getString(R.string.label_error_message_email_empty)
                    return@setOnClickListener
                }

                if (pass.isEmpty()) {
                    tilLoginPassword.isErrorEnabled = true
                    tilLoginPassword.error = getString(R.string.label_error_message_password_empty)
                    return@setOnClickListener
                }

                loginVm.postLogin(email, pass)
            }

            tvRegister.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }


            loginVm.login.observe(viewLifecycleOwner) {
                loginProgressbar.visibility = View.GONE
                val token = it.loginResult?.token.toString()
                val name = it.loginResult?.name.toString()

                listStoryVm.saveToken(token)
                listStoryVm.saveUsername(name)

                if (it.message == "success") {
                    findNavController().navigate(R.id.action_loginFragment_to_listStoryFragment)
                }
            }

            loginVm.message.observe(viewLifecycleOwner) {
                loginProgressbar.visibility = View.GONE
                Log.e(TAG, "onViewCreated message.observe:$it", )
                Toast.makeText(requireContext(), "$it,\nUser tidak dikenal, coba lagi atau daftar baru ", Toast.LENGTH_LONG).show()
            }
        }
    }
}
