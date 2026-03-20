package com.example.ecosnapshot.ui.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosnapshot.data.model.Species
import com.example.ecosnapshot.databinding.ItemSpeciesBinding

/**
 * Adapter do RecyclerView para apresentar a lista de espécies.
 */
class SpeciesAdapter(
    private val onItemClick: (Species) -> Unit
) : ListAdapter<Species, SpeciesAdapter.SpeciesViewHolder>(SpeciesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeciesViewHolder {
        val binding = ItemSpeciesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SpeciesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpeciesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SpeciesViewHolder(
        private val binding: ItemSpeciesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(species: Species) {
            binding.textCommonName.text = species.commonName
            binding.textScientificName.text = species.scientificName
            
            if (species.imageUri != null) {
                binding.imageSpecies.setImageURI(Uri.parse(species.imageUri))
            } else if (species.imageResId != null) {
                binding.imageSpecies.setImageResource(species.imageResId)
            }

            binding.root.setOnClickListener {
                onItemClick(species)
            }
        }
    }

    private class SpeciesDiffCallback : DiffUtil.ItemCallback<Species>() {
        override fun areItemsTheSame(oldItem: Species, newItem: Species): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Species, newItem: Species): Boolean {
            return oldItem == newItem
        }
    }
}
