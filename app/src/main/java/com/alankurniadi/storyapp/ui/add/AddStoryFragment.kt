package com.alankurniadi.storyapp.ui.add

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.data.Result
import com.alankurniadi.storyapp.databinding.FragmentAddStoryBinding
import com.alankurniadi.storyapp.ui.home.ListStoryViewModel
import com.alankurniadi.storyapp.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile

            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
            binding.ivStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())

            getFile = myFile

            binding.ivStory.setImageURI(selectedImg)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(requireContext(), "Tidak mendapatkan permission", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val addStoryVm: AddStoryViewModel by viewModels { factory }
        val listStoryVm: ListStoryViewModel by viewModels { factory }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }

        with(binding) {
            btnCamera.setOnClickListener { startCameraX() }
            btnGalery.setOnClickListener { startGallery() }

            buttonAdd.setOnClickListener {
                val description = edAddDescription.text.toString().trim()
                if (description.isEmpty()) {
                    edAddDescription.error = getString(R.string.label_error_description)
                } else {
                    binding.addStoryProgressbar.visibility = View.VISIBLE
                    if (getFile != null) {
                        val file = reduceFileImage(getFile as File)

                        val storyText =
                            edAddDescription.text.toString().trim()
                                .toRequestBody("text/plain".toMediaType())
                        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageMultipart: MultipartBody.Part =
                            MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

                        //getToken
                        listStoryVm.getToken().observe(viewLifecycleOwner) { token ->
                            if (token != null) {
                                addStoryVm.postNewStory(token, storyText, imageMultipart)
                                    .observe(viewLifecycleOwner) { result ->
                                        if (result != null) {
                                            when (result) {
                                                is Result.Loading -> {
                                                    addStoryProgressbar.visibility = View.VISIBLE
                                                }
                                                is Result.Success -> {
                                                    addStoryProgressbar.visibility = View.GONE
                                                    val data = result.data
                                                    if (data.error != true) {
                                                        Toast.makeText(
                                                            context,
                                                            data.message,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        findNavController().navigate(R.id.action_addStoryFragment_to_listStoryFragment)
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            data.message,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                                is Result.Error -> {
                                                    addStoryProgressbar.visibility = View.GONE
                                                    Toast.makeText(
                                                        context,
                                                        result.error,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    }
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Silakan lengkapi foto dan cerita",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
}
