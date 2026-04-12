# 05 — Navigation

## Navigation Map

```
MainActivity
     │
     ├──► ImageDetailsActivity  (tap image in RecyclerView)
     │         │
     │         └──► MainActivity  (back button)
     │
     └──► ImageDetailsActivity  (tap favourite thumbnail in favourites bar)
               │
               └──► MainActivity  (back button)
```

## Navigation Details

### MainActivity → ImageDetailsActivity

**Trigger:** User taps a `DogImage` item in the main `RecyclerView` or in the favourites bar.

**Mechanism:** Explicit `Intent` with the selected `DogImage` object passed as a serializable/parcelable extra.

```kotlin
val intent = Intent(this, ImageDetailsActivity::class.java)
intent.putExtra("DOG_IMAGE", dogImage)
startActivity(intent)
```

### ImageDetailsActivity → MainActivity

**Trigger:** User taps the system back button or the toolbar back arrow.

**Mechanism:** `finish()` is called, returning to the previous activity on the back stack.

**Side effect:** If the user toggled the favourite state, the result is communicated back via `ActivityResultLauncher` so the main list updates accordingly.

## Back Stack Behaviour

- Standard Android back stack is used.
- No custom back stack manipulation is needed.
- The app does not use fragments or Navigation Component (kept simple with Activities).
