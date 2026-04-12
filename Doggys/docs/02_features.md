# 02 — Features

## Core Features

1. **Fetch dog images from the Dog CEO API**
   - Retrieve random dog images on app launch and on demand

2. **Display images in a scrollable list/grid**
   - Use a `RecyclerView` with a grid layout to show dog images

3. **Refresh images**
   - A refresh button (or swipe-to-refresh) fetches a new set of random images

4. **Loading indicator**
   - A `ProgressBar` is shown while images are being fetched from the API

5. **Image details screen**
   - Tapping an image opens a detail view with the breed name, image URL, and favourite toggle

6. **Favourite items (FIFO queue, max 5)**
   - Users can mark up to 5 images as favourites
   - If a 6th is added, the oldest favourite is removed automatically
   - Favourites are accessible from any screen via a dedicated section/bar

7. **Local cache (up to 50 items)**
   - The last 50 fetched images are stored locally (excluding favourites)
   - At least 10 items ahead and 10 behind the current scroll position are pre-loaded

8. **Offline access**
   - If no network is available, cached images (including favourites) are displayed
   - A message informs the user they are in offline mode

9. **Error handling**
   - API and network errors are caught and shown to the user via a `Snackbar` or message view
   - The app does not crash on failure

## Optional / Extended Features

- Support for browsing images by specific breed
- Animated transitions between screens
