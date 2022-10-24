package com.alankurniadi.storyapp.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.authentication.login.LoginViewModel
import com.alankurniadi.storyapp.dataStore
import com.alankurniadi.storyapp.databinding.FragmentListStoryBinding
import com.alankurniadi.storyapp.databinding.StoryItemBinding
import com.alankurniadi.storyapp.utils.SettingPreferences
import com.alankurniadi.storyapp.utils.ViewModelFactory

class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterStory: ListStoryAdapter

    companion object {
        const val TAG = "ListStoryFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val listStoryVm =
            ViewModelProvider(this, ViewModelFactory(pref))[ListStoryViewModel::class.java]


        listStoryVm.getToken().observe(viewLifecycleOwner) { tokenPref ->
            Log.e(TAG, "onViewCreated getToken():$tokenPref")
            if (tokenPref != "") {
                listStoryVm.getAllStory(tokenPref)
            } else {
                findNavController().navigate(R.id.action_listStoryFragment_to_loginFragment)
            }

        }

        listStoryVm.getUserName().observe(viewLifecycleOwner) { name ->
            Log.e(TAG, "onViewCreated getUserName():$name")
            binding.tvUserName.text = name
        }

        listStoryVm.listStory.observe(viewLifecycleOwner) {
            with(binding) {
                Log.e(TAG, "onViewCreated listStory.observe:$it")
                if (it.listStory != null) {
                    tvLogin.visibility = View.GONE
                    tvEmptyState.visibility = View.GONE

                    actionLogout.visibility = View.VISIBLE
                    loginState.visibility = View.VISIBLE
                    btnAddStory.visibility = View.VISIBLE

                    rvStory.visibility = View.VISIBLE
                    adapterStory.setData(it.listStory)
                }
            }
        }

        adapterStory = ListStoryAdapter(itemClicked = { itemView, data ->
//            view.findNavController().navigate(
//                ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(data)
//            )
            findNavController().navigate(
                ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(data),
                FragmentNavigatorExtras(
                    itemView.ivItemPhoto to "photo_profile",
                    itemView.tvItemName to "user_name"
                )
            )
        })

        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterStory
        }

        binding.tvLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_listStoryFragment_to_loginFragment)
        }

        binding.actionLogout.setOnClickListener {
            listStoryVm.saveToken("")
            listStoryVm.saveUsername("")
            it.findNavController().navigate(R.id.action_listStoryFragment_to_loginFragment)
        }

        binding.btnAddStory.setOnClickListener {
            it.findNavController().navigate(R.id.action_listStoryFragment_to_addStoryFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
