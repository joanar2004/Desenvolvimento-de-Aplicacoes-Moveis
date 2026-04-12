# Prompts Log

This file logs the main prompts used with AntiGravity during the development of DogBreeds Explorer.

---

## Prompt 1

**Goal:** Generate the data model classes.

**Prompt used:**
```
Read docs/04_data_model.md and docs/06_architecture.md.
Generate the Kotlin data classes DogImage and ApiResponse as described.
Place them in the model/ package. Use only Kotlin, no Java.
```

**Result:** Generated `DogImage.kt` and `ApiResponse.kt` with correct fields and Kotlin data class syntax.

---

## Prompt 2

**Goal:** Set up Retrofit API service.

**Prompt used:**
```
Read docs/07_api_usage.md and docs/06_architecture.md.
Generate the Retrofit interface DogApiService and the RetrofitInstance singleton.
Base URL is https://dog.ceo/api/. Use suspend functions and Gson converter.
```

**Result:** Generated `DogApiService.kt` and `RetrofitInstance.kt` with correct Retrofit setup.

---

## Prompt 3

**Goal:** Implement the repository layer.

**Prompt used:**
```
Read docs/06_architecture.md and docs/08_implementation_plan.md Step 4.
Generate DogRepository.kt with an in-memory cache (ArrayDeque, max 50) and a
favourites FIFO queue (ArrayDeque, max 5). Include fetchImages(), getCachedImages(),
addFavourite(), removeFavourite(), getFavourites(), and isFavourite() methods.
Also include a private helper to extract breed and sub-breed from a URL.
```

**Result:** Generated `DogRepository.kt` with complete cache and favourites management logic.

---

## Prompt 4

**Goal:** Generate the ViewModel.

**Prompt used:**
```
Read docs/06_architecture.md and Step 5 in docs/08_implementation_plan.md.
Generate DogViewModel.kt extending ViewModel.
Include LiveData for images, favourites, isLoading, and errorMessage.
Implement loadImages() with coroutines and toggleFavourite(). Use viewModelScope.
```

**Result:** Generated `DogViewModel.kt` with all LiveData fields and coroutine-based data loading.

---

## Prompt 5

**Goal:** Generate the RecyclerView adapters.

**Prompt used:**
```
Read docs/03_screens.md and Step 6 in docs/08_implementation_plan.md.
Generate DogImageAdapter.kt for a 2-column grid with Glide image loading,
a favourite icon indicator, and click callbacks onImageClick and onFavouriteClick.
Also generate FavouritesAdapter.kt for a horizontal list of favourite thumbnails.
```

**Result:** Generated both adapters with ViewHolder pattern and DiffUtil for efficient updates.

---

## Prompt 6

**Goal:** Design the main activity layout.

**Prompt used:**
```
Read docs/03_screens.md and Step 7 in docs/08_implementation_plan.md.
Generate activity_main.xml with a Toolbar, a horizontal RecyclerView for favourites,
a SwipeRefreshLayout wrapping a vertical RecyclerView (2 columns), a centred ProgressBar,
and a TextView for offline/error messages. Use ConstraintLayout as root.
```

**Result:** Generated `activity_main.xml` with all required components and correct constraints.

---

## Prompt 7

**Goal:** Implement MainActivity logic.

**Prompt used:**
```
Read docs/05_navigation.md and Step 8 in docs/08_implementation_plan.md.
Generate MainActivity.kt that connects DogViewModel, observes all LiveData,
sets up both RecyclerView adapters, handles swipe-to-refresh, and launches
ImageDetailsActivity on image tap using ActivityResultLauncher.
```

**Result:** Generated `MainActivity.kt` with complete ViewModel connection and navigation logic.

---

## Prompt 8

**Goal:** Implement the image details screen.

**Prompt used:**
```
Read docs/03_screens.md and Steps 9–10 in docs/08_implementation_plan.md.
Generate activity_image_details.xml with a Toolbar, large ImageView, breed TextViews,
URL TextView, and a favourite FloatingActionButton.
Then generate ImageDetailsActivity.kt that loads the DogImage from the Intent,
displays it with Glide, and handles the favourite toggle returning the result to MainActivity.
```

**Result:** Generated both the layout and the activity with correct Intent handling and result callback.

---

_Add more prompts below as the project evolves._
