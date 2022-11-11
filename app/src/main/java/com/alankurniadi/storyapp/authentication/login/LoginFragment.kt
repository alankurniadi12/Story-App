package com.alankurniadi.storyapp.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.data.Result
import com.alankurniadi.storyapp.databinding.FragmentLoginBinding
import com.alankurniadi.storyapp.home.ListStoryViewModel
import com.alankurniadi.storyapp.utils.ViewModelFactory
import com.alankurniadi.storyapp.utils.setDisableButton
import com.alankurniadi.storyapp.utils.setEnableButton

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val loginViewModel: LoginViewModel by viewModels { factory }
        val storyViewModel: ListStoryViewModel by viewModels { factory }

        storyViewModel.saveToken("")
        with(binding) {
            edLoginEmail.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    setEnableButton(btnLogin)
                } else {
                    setDisableButton(btnLogin)
                }
            }

            edLoginPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    setEnableButton(btnLogin)
                } else {
                    setDisableButton(btnLogin)
                }
            }

            btnLogin.setOnClickListener {
                loginProgressbar.visibility = View.VISIBLE
                val email = edLoginEmail.text.toString().trim()
                val pass = edLoginPassword.text.toString().trim()

                if (email.isEmpty()) {
                    edLoginEmail.error = getString(R.string.label_error_message_email_empty)
                    setDisableButton(btnLogin)
                    loginProgressbar.visibility = View.GONE
                    return@setOnClickListener
                }

                if (pass.isEmpty()) {
                    edLoginPassword.error = getString(R.string.label_error_message_password_empty)
                    setDisableButton(btnLogin)
                    loginProgressbar.visibility = View.GONE
                    return@setOnClickListener
                }

                loginViewModel.postLogin(email, pass).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                loginProgressbar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                loginProgressbar.visibility = View.GONE
                                val data = result.data
                                if (data.error != true) {
                                    storyViewModel.saveToken(data.loginResult?.token!!)
                                    findNavController().navigate(R.id.action_loginFragment_to_listStoryFragment)
                                } else {
                                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            is Result.Error -> {
                                loginProgressbar.visibility = View.GONE
                                Toast.makeText(
                                    context,
                                    "Terjadi kesalahan (${result.error})",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            tvRegister.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }
}
