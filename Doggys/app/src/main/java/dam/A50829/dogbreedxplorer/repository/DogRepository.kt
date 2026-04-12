package dam.A50829.dogbreedxplorer.repository

import dam.A50829.dogbreedxplorer.api.RetrofitInstance
import dam.A50829.dogbreedxplorer.model.DogImage
import java.util.UUID

class DogRepository {

    private val cache = ArrayDeque<DogImage>()
    private val favourites = ArrayDeque<DogImage>()
    private val api = RetrofitInstance.api

    var isOffline: Boolean = false
        private set

    suspend fun fetchImages(count: Int = 20): List<DogImage> {
        try {
            val response = api.getRandomDogImages(count)
            isOffline = false
            if (response.status == "success") {
                val fetchedImages = response.message.map { url ->
                    val (breed, subBreed) = extractBreedFromUrl(url)
                    DogImage(
                        id = UUID.randomUUID().toString(),
                        url = url,
                        breed = breed,
                        subBreed = subBreed,
                        isFavourite = false
                    )
                }

                // Sync with favourites
                fetchedImages.forEach { image ->
                    image.isFavourite = isFavourite(image)
                }

                // Update cache (FIFO with max 50 items)
                fetchedImages.forEach { image ->
                    if (cache.size >= 50) {
                        cache.removeFirst()
                    }
                    cache.addLast(image)
                }
                return fetchedImages
            }
            return emptyList()
        } catch (e: java.io.IOException) {
            isOffline = true
            if (cache.isNotEmpty()) {
                return cache.toList()
            }
            throw java.io.IOException("No internet connection and no cached images available.")
        } catch (e: Exception) {
            throw Exception("API Error: ${e.message}")
        }
    }

    fun getCachedImages(): List<DogImage> {
        return cache.toList()
    }

    fun addFavourite(image: DogImage) {
        if (!isFavourite(image)) {
            // Remove oldest if max capacity (5) reached
            if (favourites.size >= 5) {
                val oldest = favourites.removeFirst()
                cache.find { it.url == oldest.url }?.isFavourite = false
            }
            
            val imageToAdd = if (cache.any { it.url == image.url }) {
                cache.find { it.url == image.url }!!
            } else {
                image
            }

            imageToAdd.isFavourite = true
            favourites.addLast(imageToAdd)
        }
    }

    fun removeFavourite(image: DogImage) {
        val wasRemoved = favourites.removeAll { it.url == image.url }
        if (wasRemoved) {
            image.isFavourite = false
            cache.find { it.url == image.url }?.isFavourite = false
        }
    }

    fun getFavourites(): List<DogImage> {
        return favourites.toList()
    }

    fun isFavourite(image: DogImage): Boolean {
        return favourites.any { it.url == image.url }
    }

    private fun extractBreedFromUrl(url: String): Pair<String, String?> {
        val breedSegment = url.substringAfter("/breeds/").substringBefore("/")
        val parts = breedSegment.split("-")
        val breed = parts.getOrNull(0) ?: "unknown"
        val subBreed = if (parts.size > 1) parts.drop(1).joinToString("-") else null
        return Pair(breed, subBreed)
    }
}
