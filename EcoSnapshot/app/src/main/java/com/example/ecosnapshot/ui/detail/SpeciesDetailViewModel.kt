package com.example.ecosnapshot.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecosnapshot.data.model.Species
import com.example.ecosnapshot.data.repository.SpeciesRepository

/**
 * ViewModel para o ecrã de detalhes da espécie com persistência de dados.
 */
class SpeciesDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _species = MutableLiveData<Species?>()
    val species: LiveData<Species?> get() = _species

    fun loadSpecies(speciesId: Int) {
        _species.value = SpeciesRepository.getSpeciesById(speciesId)
    }

    fun updateSpecies(updatedSpecies: Species) {
        SpeciesRepository.updateSpecies(getApplication(), updatedSpecies)
        _species.value = updatedSpecies
    }

    fun deleteSpecies(speciesId: Int) {
        SpeciesRepository.deleteSpecies(getApplication(), speciesId)
    }
}
