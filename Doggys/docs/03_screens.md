# 03 — Screens

## Screen 1: Main Screen (`MainActivity`)

The main entry point of the application.

### Components
- **Toolbar** — App title ("DogBreeds Explorer")
- **Favourites bar** — A horizontal `RecyclerView` at the top showing up to 5 favourite dog thumbnails; tapping one opens the Image Details screen
- **RecyclerView (grid, 2 columns)** — Displays the list of fetched dog images
- **ProgressBar** — Shown while loading images from the API (centred, overlays the list)
- **Refresh Button / SwipeRefreshLayout** — Triggers a new fetch from the API
- **Error message / Snackbar** — Displayed when the API call fails

### Behaviour
- On launch: fetch random images and display them
- On refresh: fetch a new batch and replace the current list
- While loading: show `ProgressBar`, hide it when done
- On error: show error message; if cache exists, display cached items
- On no network: show offline banner, load from cache

---

## Screen 2: Image Details Screen (`ImageDetailsActivity`)

Opened when the user taps any image in the main list or in the favourites bar.

### Components
- **Back button** — Returns to the Main Screen
- **Full-size ImageView** — Shows the selected dog image
- **Breed name TextView** — Extracted from the image URL (e.g. `hound/afghan`)
- **Image URL TextView** — Displays the full URL of the image
- **Favourite toggle button** — Star/heart icon to add/remove from favourites
  - If already a favourite: show filled icon; tapping removes it
  - If not a favourite: show empty icon; tapping adds it (respecting the FIFO queue of 5)

### Behaviour
- Receives the selected `DogImage` object via Intent
- Displays image using Glide
- Updates favourite state in the ViewModel
