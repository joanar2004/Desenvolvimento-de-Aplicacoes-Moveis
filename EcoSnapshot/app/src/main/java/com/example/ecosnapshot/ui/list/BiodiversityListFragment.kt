package com.example.ecosnapshot.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecosnapshot.R
import com.example.ecosnapshot.databinding.FragmentBiodiversityListBinding
import com.google.android.material.tabs.TabLayout

/**
 * Fragment principal que apresenta a lista de espécies num RecyclerView com separação por categorias.
 */
class BiodiversityListFragment : Fragment() {

    private var _binding: FragmentBiodiversityListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BiodiversityListViewModel by viewModels()
    private lateinit var adapter: SpeciesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBiodiversityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSpecies()
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = when (tab?.position) {
                    0 -> "Animal"
                    1 -> "Planta"
                    else -> "Animal"
                }
                viewModel.setCategory(category)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = SpeciesAdapter { species ->
            navigateToDetail(species.id, false)
        }
        binding.recyclerSpecies.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAddRecord.setOnClickListener {
            showAddCategoryDialog()
        }
    }

    private fun showAddCategoryDialog() {
        val categories = arrayOf(
            getString(R.string.category_animal),
            getString(R.string.category_plant)
        )

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_add_category_title)
            .setItems(categories) { _, which ->
                val selectedCategory = when (which) {
                    0 -> "Animal"
                    1 -> "Planta"
                    else -> "Animal"
                }
                viewModel.addNewRecord(selectedCategory)
            }
            .setNegativeButton(R.string.action_cancel, null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.speciesList.observe(viewLifecycleOwner) { species ->
            adapter.submitList(species)
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner) { speciesId ->
            speciesId?.let {
                navigateToDetail(it, true)
                viewModel.onNavigatedToDetail()
            }
        }
    }

    private fun navigateToDetail(speciesId: Int, isEditing: Boolean) {
        val bundle = bundleOf(
            "speciesId" to speciesId,
            "isEditing" to isEditing
        )
        findNavController().navigate(R.id.action_list_to_detail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
