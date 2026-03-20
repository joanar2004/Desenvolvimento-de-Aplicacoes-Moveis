package com.example.ecosnapshot.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecosnapshot.data.model.Species
import com.example.ecosnapshot.data.repository.SpeciesRepository

/**
 * ViewModel para o ecrã de lista de biodiversidade.
 */
class BiodiversityListViewModel(application: Application) : AndroidViewModel(application) {

    private val _speciesList = MutableLiveData<List<Species>>()
    val speciesList: LiveData<List<Species>> get() = _speciesList

    private val _navigateToDetail = MutableLiveData<Int?>()
    val navigateToDetail: LiveData<Int?> get() = _navigateToDetail

    private var currentCategory: String = "Animal"

    init {
        loadSpecies()
    }

    fun setCategory(category: String) {
        currentCategory = category
        loadSpecies()
    }

    fun loadSpecies() {
        val all = SpeciesRepository.getAllSpecies()
        _speciesList.value = all.filter { it.category == currentCategory }
    }

    /**
     * Adiciona um novo registo na categoria especificada.
     */
    fun addNewRecord(category: String) {
        val newSpecies = SpeciesRepository.addPlaceholderSpecies(getApplication(), category)
        loadSpecies()
        _navigateToDetail.value = newSpecies.id
    }

    fun onNavigatedToDetail() {
        _navigateToDetail.value = null
    }
}
