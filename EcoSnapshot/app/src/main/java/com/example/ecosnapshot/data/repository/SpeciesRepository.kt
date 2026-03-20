package com.example.ecosnapshot.data.repository

import android.content.Context
import com.example.ecosnapshot.R
import com.example.ecosnapshot.data.model.Species
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Repositório que gere a persistência dos dados das espécies.
 */
object SpeciesRepository {

    private var speciesList = mutableListOf<Species>()
    private const val FILE_NAME = "species_data.json"
    private var isInitialized = false

    /**
     * Inicializa o repositório carregando os dados do ficheiro ou usando dados iniciais.
     */
    fun initialize(context: Context) {
        if (isInitialized) return
        
        val file = File(context.filesDir, FILE_NAME)
        if (file.exists()) {
            loadFromFile(file)
        } else {
            setupInitialData()
            saveToFile(context)
        }
        isInitialized = true
    }

    private fun setupInitialData() {
        speciesList = mutableListOf(
            Species(1, "Lince-ibérico", "Lynx pardinus", "O lince-ibérico é o felino mais ameaçado do mundo.", "Matagal mediterrânico", "Em Perigo", "Animal", R.drawable.ic_species_placeholder),
            Species(2, "Águia-imperial-ibérica", "Aquila adalberti", "Ave de rapina de grande porte.", "Planícies e montados", "Vulnerável", "Animal", R.drawable.ic_species_placeholder),
            Species(3, "Sobreiro", "Quercus suber", "Árvore emblemática de Portugal.", "Florestas mediterrânicas", "Pouco Preocupante", "Planta", R.drawable.ic_species_placeholder),
            Species(4, "Lobo-ibérico", "Canis lupus signatus", "Subespécie do lobo-cinzento.", "Montanhas do norte", "Em Perigo", "Animal", R.drawable.ic_species_placeholder),
            Species(5, "Azinheira", "Quercus ilex", "Árvore de folha perene.", "Montados alentejanos", "Pouco Preocupante", "Planta", R.drawable.ic_species_placeholder)
        )
    }

    fun getAllSpecies(): List<Species> = speciesList.toList()

    fun getSpeciesById(id: Int): Species? = speciesList.find { it.id == id }

    fun addPlaceholderSpecies(context: Context, category: String): Species {
        val nextId = (speciesList.maxOfOrNull { it.id } ?: 0) + 1
        val newSpecies = Species(
            id = nextId,
            commonName = "Nova Espécie ($category)",
            scientificName = "Species novus",
            description = "Detalhes a preencher.",
            habitat = "A determinar",
            rarityLevel = "Desconhecido",
            category = category,
            imageResId = R.drawable.ic_species_placeholder
        )
        speciesList.add(newSpecies)
        saveToFile(context)
        return newSpecies
    }

    fun updateSpecies(context: Context, updatedSpecies: Species) {
        val index = speciesList.indexOfFirst { it.id == updatedSpecies.id }
        if (index != -1) {
            speciesList[index] = updatedSpecies
            saveToFile(context)
        }
    }

    fun deleteSpecies(context: Context, id: Int) {
        speciesList.removeAll { it.id == id }
        saveToFile(context)
    }

    private fun saveToFile(context: Context) {
        val jsonArray = JSONArray()
        speciesList.forEach { species ->
            val jsonObject = JSONObject().apply {
                put("id", species.id)
                put("commonName", species.commonName)
                put("scientificName", species.scientificName)
                put("description", species.description)
                put("habitat", species.habitat)
                put("rarityLevel", species.rarityLevel)
                put("category", species.category)
                put("imageResId", species.imageResId ?: -1)
                put("imageUri", species.imageUri ?: "")
            }
            jsonArray.put(jsonObject)
        }
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(jsonArray.toString().toByteArray())
        }
    }

    private fun loadFromFile(file: File) {
        val jsonString = file.readText()
        val jsonArray = JSONArray(jsonString)
        val newList = mutableListOf<Species>()
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            newList.add(Species(
                id = json.getInt("id"),
                commonName = json.getString("commonName"),
                scientificName = json.getString("scientificName"),
                description = json.getString("description"),
                habitat = json.getString("habitat"),
                rarityLevel = json.getString("rarityLevel"),
                category = json.getString("category"),
                imageResId = if (json.getInt("imageResId") == -1) null else json.getInt("imageResId"),
                imageUri = if (json.getString("imageUri").isEmpty()) null else json.getString("imageUri")
            ))
        }
        speciesList = newList
    }
}
