package dam.A50829.dogbreedxplorer.model

data class DogImage(
    val id: String,
    val url: String,
    val breed: String,
    val subBreed: String? = null,
    var isFavourite: Boolean = false
)
