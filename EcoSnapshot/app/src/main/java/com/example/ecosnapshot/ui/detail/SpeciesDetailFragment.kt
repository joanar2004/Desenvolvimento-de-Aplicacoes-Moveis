package com.example.ecosnapshot.ui.detail

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecosnapshot.R
import com.example.ecosnapshot.data.model.Species
import com.example.ecosnapshot.databinding.FragmentSpeciesDetailBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment que apresenta os detalhes de uma espécie e permite a sua edição e eliminação.
 */
class SpeciesDetailFragment : Fragment() {

    private var _binding: FragmentSpeciesDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SpeciesDetailViewModel by viewModels()
    
    private var tempImageUri: Uri? = null
    private var currentPhotoPath: String? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCameraIntent()
        } else {
            Toast.makeText(requireContext(), "Permissão de câmara negada", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            tempImageUri?.let { uri ->
                binding.imageSpeciesDetail.setImageURI(uri)
                currentPhotoPath = uri.toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeciesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val speciesId = arguments?.getInt("speciesId") ?: return
        val startInEditMode = arguments?.getBoolean("isEditing") ?: false
        
        viewModel.loadSpecies(speciesId)

        setupButtons()
        observeViewModel()
        
        if (startInEditMode) {
            setEditMode(true)
        }
    }

    private fun setupButtons() {
        binding.buttonEdit.setOnClickListener {
            setEditMode(true)
        }

        binding.buttonSave.setOnClickListener {
            val currentSpecies = viewModel.species.value ?: return@setOnClickListener
            
            val updatedSpecies = currentSpecies.copy(
                commonName = binding.editCommonName.text.toString(),
                scientificName = binding.editScientificName.text.toString(),
                rarityLevel = binding.editRarity.text.toString(),
                habitat = binding.editHabitat.text.toString(),
                description = binding.editDescription.text.toString(),
                imageUri = currentPhotoPath ?: currentSpecies.imageUri
            )

            viewModel.updateSpecies(updatedSpecies)
            setEditMode(false)
            Toast.makeText(requireContext(), R.string.save_success_message, Toast.LENGTH_SHORT).show()
        }

        binding.buttonDelete.setOnClickListener {
            showDeleteConfirmation()
        }

        binding.fabCaptureImage.setOnClickListener {
            checkCameraPermissionAndOpen()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_confirm_title)
            .setMessage(R.string.delete_confirm_message)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                viewModel.species.value?.id?.let { id ->
                    viewModel.deleteSpecies(id)
                    Toast.makeText(requireContext(), R.string.delete_success_message, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
            .setNegativeButton(R.string.action_cancel, null)
            .show()
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCameraIntent()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCameraIntent() {
        val photoFile = createImageFile()
        photoFile?.let {
            val authority = "${requireContext().packageName}.fileprovider"
            tempImageUri = FileProvider.getUriForFile(requireContext(), authority, it)
            takePictureLauncher.launch(tempImageUri)
        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun setEditMode(isEditing: Boolean) {
        binding.layoutDisplay.isVisible = !isEditing
        binding.layoutEdit.isVisible = isEditing
        binding.fabCaptureImage.isVisible = isEditing
        binding.buttonDelete.isVisible = !isEditing // Esconder eliminar durante a edição para focar no save

        binding.buttonEdit.isVisible = !isEditing
        binding.buttonSave.isVisible = isEditing
        
        if (isEditing) {
            prepareEditFields()
        }
    }

    private fun prepareEditFields() {
        viewModel.species.value?.let {
            binding.editCommonName.setText(it.commonName)
            binding.editScientificName.setText(it.scientificName)
            binding.editRarity.setText(it.rarityLevel)
            binding.editHabitat.setText(it.habitat)
            binding.editDescription.setText(it.description)
            currentPhotoPath = it.imageUri
        }
    }

    private fun observeViewModel() {
        viewModel.species.observe(viewLifecycleOwner) { species ->
            species?.let {
                if (it.imageUri != null) {
                    binding.imageSpeciesDetail.setImageURI(Uri.parse(it.imageUri))
                } else if (it.imageResId != null) {
                    binding.imageSpeciesDetail.setImageResource(it.imageResId)
                }
                
                binding.textCommonNameDisplay.text = it.commonName
                binding.textScientificNameDisplay.text = it.scientificName
                binding.textRarityDisplay.text = it.rarityLevel
                binding.textHabitatDisplay.text = it.habitat
                binding.textDescriptionDisplay.text = it.description
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
