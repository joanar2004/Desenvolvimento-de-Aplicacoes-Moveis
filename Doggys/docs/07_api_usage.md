# 07 — API Usage

## Dog CEO API

- **Base URL:** `https://dog.ceo/api/`
- **Authentication:** None required (free, public API)
- **Format:** JSON
- **Documentation:** https://dog.ceo/dog-api/documentation

---

## Endpoints Used

### 1. Get Multiple Random Images

Fetches a specified number of random dog images from any breed.

| Property    | Value                                              |
|-------------|----------------------------------------------------|
| Method      | `GET`                                              |
| Endpoint    | `/breeds/image/random/{count}`                     |
| Parameters  | `count` — number of images to fetch (max 50)       |

**Example request:**
```
GET https://dog.ceo/api/breeds/image/random/20
```

**Example response:**
```json
{
  "message": [
    "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
    "https://images.dog.ceo/breeds/labrador/n02099712_5623.jpg",
    "https://images.dog.ceo/breeds/poodle-toy/n02113624_847.jpg"
  ],
  "status": "success"
}
```

---

### 2. Get Random Image by Breed (Optional / Extension)

Fetches a single random image from a specific breed.

| Property    | Value                                              |
|-------------|----------------------------------------------------|
| Method      | `GET`                                              |
| Endpoint    | `/breed/{breed}/images/random`                     |
| Parameters  | `breed` — breed name (e.g. `hound`, `labrador`)    |

**Example request:**
```
GET https://dog.ceo/api/breed/hound/images/random
```

**Example response:**
```json
{
  "message": "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
  "status": "success"
}
```

---

## Retrofit Interface (Kotlin)

```kotlin
interface DogApiService {

    @GET("breeds/image/random/{count}")
    suspend fun getRandomImages(
        @Path("count") count: Int
    ): ApiResponse

    @GET("breed/{breed}/images/random")
    suspend fun getRandomImageByBreed(
        @Path("breed") breed: String
    ): SingleImageResponse
}
```

---

## Data Classes for API Response

```kotlin
// For multiple images
data class ApiResponse(
    val message: List<String>,
    val status: String
)

// For single image (breed endpoint)
data class SingleImageResponse(
    val message: String,
    val status: String
)
```

---

## Breed Extraction from URL

The breed and sub-breed are not returned explicitly by the API but can be extracted from the image URL.

**URL pattern:**
```
https://images.dog.ceo/breeds/{breed}-{subbreed}/{filename}.jpg
```

**Extraction example (Kotlin):**
```kotlin
fun extractBreedFromUrl(url: String): Pair<String, String?> {
    val breedSegment = url.substringAfter("/breeds/").substringBefore("/")
    val parts = breedSegment.split("-")
    val breed = parts.getOrNull(0) ?: "unknown"
    val subBreed = if (parts.size > 1) parts.drop(1).joinToString("-") else null
    return Pair(breed, subBreed)
}
```

---

## Error Handling

| Situation              | Handling Strategy                                      |
|------------------------|--------------------------------------------------------|
| No internet connection | Catch `IOException`, load from cache, show offline banner |
| API returns error      | Check `status != "success"`, show Snackbar message     |
| Empty response         | Show "No images available" message                     |
| Image load fails       | Glide placeholder/error drawable shown automatically   |
