package com.example.ecosnapshot.data.model

/**
 * Representa uma espécie de biodiversidade.
 *
 * @property id Identificador único.
 * @property commonName Nome comum.
 * @property scientificName Nome científico.
 * @property description Descrição detalhada.
 * @property habitat Habitat natural.
 * @property rarityLevel Nível de raridade.
 * @property category Categoria: "Animal" ou "Planta".
 * @property imageResId Resource ID da imagem local.
 * @property imageUri URI da imagem capturada.
 */
data class Species(
    val id: Int,
    val commonName: String,
    val scientificName: String,
    val description: String,
    val habitat: String,
    val rarityLevel: String,
    val category: String,
    val imageResId: Int? = null,
    val imageUri: String? = null
)
