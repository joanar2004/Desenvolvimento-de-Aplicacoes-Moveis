package dam.A50829.dogbreedxplorer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.A50829.dogbreedxplorer.model.DogImage
import dam.A50829.dogbreedxplorer.repository.DogRepository
import kotlinx.coroutines.launch
import java.io.IOException

class DogViewModel : ViewModel() {

    private val repository = DogRepository()

    private val _images = MutableLiveData<List<DogImage>>()
    val images: LiveData<List<DogImage>> get() = _images

    private val _favourites = MutableLiveData<List<DogImage>>()
    val favourites: LiveData<List<DogImage>> get() = _favourites

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadImages(isNetworkAvailable: Boolean = true) {
        _isLoading.value = true
        _errorMessage.value = null

        if (!isNetworkAvailable) {
            val cached = repository.getCachedImages()
            _images.value = cached
            if (cached.isNotEmpty()) {
                _errorMessage.value = "You are offline. Showing cached images."
            } else {
                _errorMessage.value = "No internet connection and no cached images."
            }
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            try {
                repository.fetchImages()
                _images.value = repository.getCachedImages()
                
                if (repository.isOffline) {
                    _errorMessage.value = "You are offline. Showing cached images."
                }
            } catch (e: Exception) {
                _images.value = repository.getCachedImages()
                val msg = e.message ?: "An unknown error occurred"
                // Post cleanly without prefix if not needed or adjust based on your needs
                _errorMessage.value = msg 
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavourite(image: DogImage) {
        if (repository.isFavourite(image)) {
            repository.removeFavourite(image)
        } else {
            repository.addFavourite(image)
        }
        
        // Update both LiveData streams so UI can reflect changes
        _favourites.value = repository.getFavourites()
        _images.value = repository.getCachedImages()
    }
}
