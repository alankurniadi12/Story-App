package com.alankurniadi.storyapp.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.alankurniadi.storyapp.MainActivity
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.databinding.FragmentDetailStoryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
            interpolator = AccelerateDecelerateInterpolator()
        }

        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).data

        with(binding) {
            Glide.with(view.context)
                .load(data.photoUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                })
                .into(ivDetailPhoto)

            tvDetailName.text = data.name
            tvDetailDescription.text = data.description
        }
    }
}
