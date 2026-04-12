# 04 — Data Model

## DogImage

Represents a single dog image item in the app.

| Field       | Type    | Description                                      |
|-------------|---------|--------------------------------------------------|
| `id`        | String  | Unique identifier (derived from the image URL)   |
| `url`       | String  | Full URL to the dog image                        |
| `breed`     | String  | Breed name extracted from the URL path           |
| `subBreed`  | String? | Sub-breed name, if present (nullable)            |
| `isFavourite` | Boolean | Whether this image is marked as a favourite    |

### Example

```kotlin
data class DogImage(
    val id: String,
    val url: String,
    val breed: String,
    val subBreed: String? = null,
    val isFavourite: Boolean = false
)
```

---

## ApiResponse

Represents the raw JSON response from the Dog CEO API.

| Field      | Type           | Description                            |
|------------|----------------|----------------------------------------|
| `message`  | List\<String\> | List of image URLs                     |
| `status`   | String         | Response status (`"success"` or error) |

### Example

```kotlin
data class ApiResponse(
    val message: List<String>,
    val status: String
)
```

---

## Data Flow

```
API (JSON)
   ↓
ApiResponse.message (List<String> of URLs)
   ↓
Mapped to List<DogImage> (breed extracted from URL)
   ↓
Stored in cache (up to 50 items)
   ↓
Observed by UI via LiveData
```

---

## Breed Extraction Logic

The breed (and optional sub-breed) is extracted from the image URL path.

Example URL:
```
https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg
```

- `breed` → `hound`
- `subBreed` → `afghan`

Extraction rule: split the URL by `/breeds/`, take the second part, split by `/`, take index 0, then split by `-` to separate breed and sub-breed.
