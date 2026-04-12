# 06 — Architecture

## Pattern: MVVM (Model-View-ViewModel)

This application follows the **MVVM** architectural pattern as recommended by Android Jetpack guidelines.

---

## Layer Diagram

```
┌─────────────────────────────────────────────────┐
│                     VIEW                        │
│   MainActivity / ImageDetailsActivity           │
│   XML Layouts / RecyclerView Adapters           │
│   Observes LiveData from ViewModel              │
└───────────────────┬─────────────────────────────┘
                    │ observes LiveData
                    ▼
┌─────────────────────────────────────────────────┐
│                  VIEWMODEL                      │
│   DogViewModel                                  │
│   - Holds UI state (images, favourites, errors) │
│   - Calls Repository methods                    │
│   - Exposes LiveData to the View                │
└───────────────────┬─────────────────────────────┘
                    │ calls
                    ▼
┌─────────────────────────────────────────────────┐
│                 REPOSITORY                      │
│   DogRepository                                 │
│   - Decides: fetch from API or use cache        │
│   - Manages the local in-memory cache           │
│   - Manages the favourites FIFO queue           │
└──────────┬────────────────────┬─────────────────┘
           │ calls              │ reads/writes
           ▼                    ▼
┌──────────────────┐   ┌────────────────────────┐
│   API SERVICE    │   │     LOCAL CACHE         │
│  DogApiService   │   │  (in-memory, max 50)    │
│  (Retrofit)      │   │  + Favourites (max 5)   │
│  dog.ceo API     │   │  ArrayDeque / LinkedList│
└──────────────────┘   └────────────────────────┘
```

---

## Components

### View
- `MainActivity` — displays the image list, favourites bar, handles refresh
- `ImageDetailsActivity` — displays image detail, handles favourite toggle
- `DogImageAdapter` — `RecyclerView.Adapter` for the main image grid
- `FavouritesAdapter` — `RecyclerView.Adapter` for the horizontal favourites bar

### ViewModel
- `DogViewModel` — single ViewModel shared by both activities (scoped to Application or passed via Intent result)
  - `LiveData<List<DogImage>> images` — current list of images
  - `LiveData<List<DogImage>> favourites` — current list of favourites (max 5)
  - `LiveData<Boolean> isLoading` — loading state
  - `LiveData<String?> errorMessage` — error message or null

### Repository
- `DogRepository`
  - `fetchImages(count: Int)` — calls API, maps response to `DogImage`, stores in cache
  - `getCachedImages()` — returns cached items
  - `addFavourite(image: DogImage)` — adds to FIFO queue (removes oldest if > 5)
  - `removeFavourite(image: DogImage)` — removes from favourites
  - `getFavourites()` — returns current list of favourites

### API Service
- `DogApiService` — Retrofit interface for Dog CEO API calls

### Cache
- In-memory `ArrayDeque<DogImage>` with max 50 items (FIFO)
- Separate `ArrayDeque<DogImage>` for favourites with max 5 items (FIFO)

---

## Key Libraries

| Library       | Purpose                          |
|---------------|----------------------------------|
| Retrofit 2    | HTTP client for API calls        |
| Gson          | JSON deserialisation             |
| Glide         | Image loading and caching        |
| LiveData      | Reactive UI updates              |
| ViewModel     | Lifecycle-aware state holder     |
| Coroutines    | Background thread management     |
