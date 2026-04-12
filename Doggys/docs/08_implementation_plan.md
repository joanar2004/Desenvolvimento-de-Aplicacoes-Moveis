# Implementation Plan

## Step 1 - Project Setup

Create a new Android project with Kotlin, minimum SDK API 24, and XML Views (no Compose).
Package name: `dam.AXXXX.dogbreedexplorer`

Dependencies to add in `build.gradle`:
```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.github.bumptech.glide:glide:4.16.0'
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
implementation 'androidx.activity:activity-ktx:1.9.0'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
```

Also add INTERNET permission to `AndroidManifest.xml`.

---

## Step 2 - Data Model

Create two data classes:

`DogImage.kt` — represents a single dog image with id, url, breed, optional sub-breed, and a favourite flag.

`ApiResponse.kt` — maps the JSON response from the API, which has a list of image URLs and a status string.

---

## Step 3 - Retrofit Setup

Create `DogApiService.kt` with an interface for the endpoint that fetches random images by count.

Create `RetrofitInstance.kt` as a singleton with base URL `https://dog.ceo/api/`.

---

## Step 4 - Repository

Create `DogRepository.kt` to handle all data operations:
- Cache of up to 50 images using an ArrayDeque (oldest removed when full)
- Favourites list of up to 5 images, also using ArrayDeque with FIFO logic
- Methods: fetchImages, getCachedImages, addFavourite, removeFavourite, getFavourites, isFavourite
- Private helper to extract breed and sub-breed from the image URL

---

## Step 5 - ViewModel

Create `DogViewModel.kt`:
- LiveData for images, favourites, loading state, and error messages
- loadImages() using viewModelScope and coroutines
- toggleFavourite() to add or remove a favourite and update the LiveData

---

## Step 6 - Adapters

Create `DogImageAdapter.kt` for the main grid (2 columns), loading images with Glide and showing a favourite icon. Needs click callbacks for image tap and favourite tap.

Create `FavouritesAdapter.kt` for the horizontal favourites bar at the top, showing up to 5 thumbnails.

---

## Step 7 - Main Layout

Create `activity_main.xml` with:
- Toolbar at the top
- Horizontal RecyclerView for favourites (hidden when there are none)
- SwipeRefreshLayout with a 2-column RecyclerView for the image grid
- ProgressBar in the centre (shown while loading)
- TextView for error or offline messages

---

## Step 8 - MainActivity

Connect the ViewModel and observe all LiveData. On launch and on swipe-refresh, call loadImages(). Handle image taps to open ImageDetailsActivity, and favourite taps to toggle the favourite state.

---

## Step 9 - Details Layout

Create `activity_image_details.xml` with:
- Toolbar with back button
- Large ImageView
- TextViews for breed, sub-breed (hidden if null), and image URL
- FAB for the favourite toggle

---

## Step 10 - ImageDetailsActivity

Get the DogImage from the Intent, load the image with Glide, show all the info, and handle the favourite toggle. Use ActivityResultLauncher to send the updated state back to MainActivity.

---

## Step 11 - Error Handling and Offline

Wrap the API call in try/catch in the repository. If there's no connection, fall back to the cache and show an offline message. Add ACCESS_NETWORK_STATE permission.

---

## Step 12 - Strings and Resources

Add all strings to `strings.xml` in English and Portuguese (`values-pt`). Add placeholder/error drawables for Glide. Update the app icon.

---

## Step 13 - Testing

Test on emulator in portrait and landscape. Check that images load, favourites work correctly (max 5, FIFO), cache holds up to 50 items, offline mode works, and error messages show up properly. Fix anything that looks off and do a final commit.
