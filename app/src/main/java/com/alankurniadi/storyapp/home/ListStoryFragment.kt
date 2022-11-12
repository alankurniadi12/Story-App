package com.alankurniadi.storyapp.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.data.Result
import com.alankurniadi.storyapp.databinding.FragmentListStoryBinding
import com.alankurniadi.storyapp.utils.ViewModelFactory

class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterStory: ListStoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        android.view.Window.FEATURE_CONTENT_TRANSITIONS
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel: ListStoryViewModel by viewModels {
            factory
        }

        //get token
        viewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token != "") {
                viewModel.getAllStory(token).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressList.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                with(binding) {
                                    progressList.visibility = View.GONE
                                    actionLogout.visibility = View.VISIBLE
                                    btnAddStory.visibility = View.VISIBLE
                                    rvStory.visibility = View.VISIBLE
                                    val data = result.data
                                    if (data.error != true) {
                                        adapterStory.setData(data.listStory!!)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "${data.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }

                            }
                            is Result.Error -> {
                                binding.progressList.visibility = View.GONE
                                Toast.makeText(
                                    context,
                                    "Terjadi Kesalahan,(${result.error})",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                findNavController().navigate(R.id.action_listStoryFragment_to_loginFragment)
            }
        }

        adapterStory = ListStoryAdapter(itemClicked = { viewItem, data ->
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                Pair.create(viewItem.ivItemPhoto, "photo"),
                Pair.create(viewItem.tvItemName, "name"),
                Pair.create(viewItem.tvItemDesc, "desc")
            )
            val extras = ActivityNavigatorExtras(options)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.findNavController().navigate(
                    ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryActivity(data),
                    extras
                )
            } else {
                view.findNavController().navigate(
                    ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryActivity(data)
                )
            }

        })

        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterStory
        }

        binding.actionLogout.setOnClickListener {
            viewModel.saveToken("")
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
