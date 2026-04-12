# 09 — Feature Extensions

This file documents features added after the initial implementation plan was completed.  
Each extension follows the controlled extension workflow: document → commit → implement → commit → test → commit.

---

## Extension 1 — MVVM Pattern

**Description:** Refactor all business logic out of Activities into a `DogViewModel`, ensuring clean separation between UI and data layers.

**Implementation tasks:**
- Extract fetch and favourite logic from `MainActivity` into `DogViewModel`
- Use `LiveData` for all UI-observable state
- Activities only observe and react — no business logic in Activities

**Expected UI changes:** None visible — internal refactor only.

**Status:** ✅ Included in the base implementation plan (Step 5).

---

## Extension 2 — Loading Indicator

**Description:** Show a `ProgressBar` while images are being fetched from the API. The indicator must be relative to which images are being loaded (initial load vs. cache pre-loading).

**Implementation tasks:**
- Add `isLoading: MutableLiveData<Boolean>` to `DogViewModel`
- Show `ProgressBar` in `MainActivity` when `isLoading = true`
- Hide `ProgressBar` and stop `SwipeRefreshLayout` indicator when loading completes
- Show a separate smaller indicator for background pre-loading of cache items

**Expected UI changes:** Centred `ProgressBar` overlay during full load; subtle indicator during background cache pre-load.

**Status:** ✅ Included in the base implementation plan (Step 8).

---

## Extension 3 — Image Details Screen

**Description:** A dedicated screen showing more information about a selected dog image, including breed, sub-breed, full URL, and favourite toggle.

**Implementation tasks:**
- Create `ImageDetailsActivity` and `activity_image_details.xml`
- Pass selected `DogImage` via Intent
- Display breed, sub-breed (if any), image URL, and favourite button
- Return updated favourite state to `MainActivity` via `ActivityResultLauncher`

**Expected UI changes:** New full-screen detail view accessible by tapping any image.

**Status:** ✅ Included in the base implementation plan (Steps 9–10).

---

## Extension 4 — Favourite Items (FIFO Queue, Max 5)

**Description:** Users can mark up to 5 images as favourites. Adding a 6th automatically removes the oldest. Favourites are accessible from any screen via a horizontal bar at the top of `MainActivity`.

**Implementation tasks:**
- Implement `ArrayDeque<DogImage>` for favourites in `DogRepository`
- `addFavourite()` enforces max 5 with FIFO eviction
- `FavouritesAdapter` renders horizontal thumbnails in `MainActivity`
- Favourite toggle available from both the main list and the detail screen
- Favourites persist in memory for the session (no database needed)

**Expected UI changes:**
- Horizontal favourites bar appears below the toolbar when at least 1 favourite exists
- Each favourite thumbnail is tappable → opens `ImageDetailsActivity`

**Status:** ✅ Included in the base implementation plan (Steps 4, 6, 8).

---

## Extension 5 — Cache (Max 50 Items, Smart Pre-loading)

**Description:** The app maintains a cache of up to 50 dog images (excluding favourites). While scrolling, the app pre-loads at least 10 items ahead of and 10 behind the current position. A loading indicator is shown relative to which items are being loaded.

**Implementation tasks:**
- Implement `ArrayDeque<DogImage>` for cache in `DogRepository` (max 50, FIFO)
- Detect scroll position in `RecyclerView` using `LinearLayoutManager.findLastVisibleItemPosition()`
- When fewer than 10 items remain ahead, trigger a background fetch from the API
- Post newly fetched items to `images` LiveData incrementally
- Show a subtle loading indicator during background fetch

**Expected UI changes:** Seamless scrolling with no visible gaps; subtle loading indicator at the bottom of the list during background fetch.

---

## Extension 6 — Offline Access

**Description:** When there is no network connection, the app displays cached images (including favourites) and informs the user they are in offline mode.

**Implementation tasks:**
- Check network connectivity before each API call using `ConnectivityManager`
- If offline: skip API call, load from cache, post `errorMessage` = `"Offline mode — showing cached images"`
- If online but API fails: catch `IOException`, fall back to cache, show error `Snackbar`
- Add `ACCESS_NETWORK_STATE` permission to `AndroidManifest.xml`

**Expected UI changes:**
- Offline banner (coloured `TextView` or `Snackbar`) shown when device has no connection
- Normal image list still shown with cached data

---

## Extension 7 — Graceful API Error Handling

**Description:** All API and network errors are caught and communicated to the user clearly, without crashing the app.

**Implementation tasks:**
- Wrap all API calls in `try/catch` in `DogRepository`
- Post meaningful error messages to `errorMessage` LiveData in `DogViewModel`
- Show errors via `Snackbar` in `MainActivity`
- Glide error placeholder drawable shown for images that fail to load

**Expected UI changes:** `Snackbar` at the bottom of the screen with a retry action when errors occur.
