# Assignment 2 — DogBreeds Explorer

**Course:** Desenvolvimento de Aplicações Móveis (DAM)
**Student:** Joana Ramos (50829)
**Date:** April 2026
**Repository URL:** _(to be filled)_

---

## 1. Introduction

This assignment involved building an Android application called DogBreeds Explorer. The goal was to create a functional mobile app that fetches and displays random dog images from a public REST API, with support for favourites, offline caching, and a modern user interface.

The project also required using AI-assisted development through the AntiGravity IDE, following a planning-first workflow where the app was fully specified in Markdown documentation before any code was generated.

## 2. System Overview

DogBreeds Explorer is an Android app that lets users browse random dog images fetched from the Dog CEO API. Users can scroll through a grid of images, tap on any image to see more details about the breed, mark images as favourites, and access the app offline using a local cache.

The app defaults to loading 20 random images on launch and supports swipe-to-refresh to load a new batch. Favourites are stored in a FIFO queue with a maximum of 5, accessible from a horizontal bar at the top of the main screen. The last 50 fetched images are cached in memory for offline access.

## 3. Architecture and Design

The app follows the MVVM (Model-View-ViewModel) architectural pattern, as recommended by Android Jetpack guidelines.

The architecture is organised into the following layers:

- **View** — `MainActivity` and `ImageDetailsActivity`, using XML layouts and RecyclerView adapters
- **ViewModel** — `DogViewModel`, which holds all UI state using LiveData and communicates with the repository
- **Repository** — `DogRepository`, which manages API calls, the in-memory cache, and the favourites queue
- **API Service** — `DogApiService`, a Retrofit interface that communicates with the Dog CEO API

For image loading, Glide was used. Retrofit with Gson was used for HTTP requests and JSON parsing. Coroutines with `viewModelScope` were used for background operations.

## 4. Implementation

The implementation followed the 13-step plan defined in `docs/08_implementation_plan.md`:

- Data model classes `DogImage` and `ApiResponse`
- Retrofit setup with `DogApiService` and `RetrofitInstance`
- `DogRepository` with in-memory cache (max 50, FIFO) and favourites queue (max 5, FIFO)
- `DogViewModel` with LiveData for images, favourites, loading state, and error messages
- `DogImageAdapter` for the main 2-column grid and `FavouritesAdapter` for the horizontal bar
- `activity_main.xml` with a Toolbar, favourites bar, SwipeRefreshLayout, ProgressBar, and error message view
- `activity_image_details.xml` with the dog image, breed info labels, and a FAB for favourite toggle
- Error handling and offline mode using `ConnectivityManager` and try/catch in the repository
- String resources in English and Portuguese

The UI was redesigned after the initial implementation to use MaterialCardView with rounded corners, heart icon overlays, a modern toolbar, and a cleaner details screen with clearly labelled fields for breed, sub-breed, and image URL.

## 5. Testing and Validation

The app was tested on a physical Android device. The following scenarios were validated:

- Images load correctly from the Dog CEO API on launch
- Swipe-to-refresh loads a new set of images
- Tapping an image opens the details screen with the correct breed, sub-breed (when available), and URL
- Marking a favourite adds it to the favourites bar; adding a 6th removes the oldest automatically
- Turning off the network and reopening the app shows cached images with an offline message
- The ProgressBar appears while loading and disappears when done
- Error messages appear via Snackbar when the API is unreachable

## 6. Usage Instructions

1. Clone the repository and open it in Android Studio or AntiGravity IDE
2. Sync Gradle dependencies
3. Run the app on an emulator or physical device (minimum API 24)
4. On launch, the app fetches 20 random dog images automatically
5. Swipe down to refresh and load new images
6. Tap any image to see breed details and toggle the favourite
7. Favourites appear in the horizontal bar at the top of the main screen

---

# Autonomous Software Engineering Sections

## 7. Prompting Strategy

The prompting strategy followed a planning-first approach. Before any code was generated, the full project was specified in a set of Markdown files covering the app overview, features, screens, data model, navigation, architecture, API usage, and implementation plan.

The AI agent (AntiGravity) was instructed to read all documentation files before starting, and to generate code strictly one step at a time, waiting for approval before moving to the next step. This gave full control over what was being generated and made it easy to catch errors early.

For bug fixes, the Logcat error was always included in the prompt so the agent had the exact context needed to find the root cause. Vague prompts were avoided in favour of specific, technical descriptions of the problem and the expected fix.

## 8. Autonomous Agent Workflow

The workflow used was:

1. All Markdown specification files were written first (`docs/01` to `docs/09` and `agents.md`)
2. AntiGravity was opened with the project folder as the workspace
3. The agent was asked to read all docs and confirm understanding before generating any code
4. Code was generated one step at a time following `docs/08_implementation_plan.md`
5. Each step was reviewed and approved before moving to the next
6. Bugs found during testing were reported to the agent with the full Logcat output and fixed iteratively
7. A UI redesign prompt was issued after the base functionality was confirmed working
8. All prompts were logged in `prompts_log.md`

## 9. Verification of AI-Generated Artifacts

Every file generated by the agent was reviewed before being accepted. The main verification steps were:

- Checking that the generated Kotlin code matched the architecture defined in `docs/06_architecture.md`
- Confirming that LiveData observers were correctly set up in the Activities
- Verifying that the FIFO logic for the cache and favourites was correctly implemented in `DogRepository`
- Testing the app on a real device after each major step to catch runtime errors early
- Checking Logcat after each run to identify any crashes or warnings

Several issues were caught this way, including a missing SwipeRefreshLayout dependency, missing layout files referenced by the adapters, a theme conflict causing a crash on launch, and a NestedScrollView breaking the RecyclerView after the UI redesign.

## 10. Human vs AI Contribution

| Task | Human | AI |
|------|-------|----|
| Project specification and Markdown docs | ✅ | |
| Implementation plan | ✅ | |
| Gradle and project setup | ✅ (fixed errors) | ✅ (generated) |
| Kotlin code generation | | ✅ |
| XML layout generation | | ✅ |
| Bug identification and diagnosis | ✅ | |
| Bug fixing prompts | ✅ | ✅ |
| UI redesign specification | ✅ | |
| UI redesign implementation | | ✅ |
| Testing on real device | ✅ | |
| Final validation | ✅ | |

## 11. Ethical and Responsible Use

AI tools were used to assist and accelerate development, not to replace understanding. Every piece of generated code was read and reviewed before being accepted. When bugs appeared, the root cause was understood before asking the agent to fix it, rather than blindly accepting whatever it produced.

The use of AI was fully disclosed and all prompts were logged as required. The planning-first approach ensured that the design decisions were made by the developer, with the AI acting as an implementation assistant.

---

# Development Process

## 12. Version Control and Commit History

The repository follows the commit policy defined in the assignment. Commits were made regularly throughout the development process, corresponding to the steps in the implementation plan. Each commit represents a meaningful unit of work such as adding a new feature, fixing a bug, or updating documentation.

## 13. Difficulties and Lessons Learned

Several issues came up during development:

- **Gradle compatibility** — the default Gradle version was incompatible with Java 25 and had to be updated to Gradle 8.12
- **AndroidX not enabled** — `android.useAndroidX` was not set in `gradle.properties`, causing a build warning that required a manual fix
- **Missing dependencies** — SwipeRefreshLayout was not included in the initial dependency list and had to be added separately
- **Missing layout files** — the adapters referenced layout files that had not been created yet, causing compilation errors
- **Theme conflict** — the app theme included an ActionBar which conflicted with the custom Toolbar, causing a crash on launch
- **UI redesign breaking the RecyclerView** — wrapping the RecyclerView in a NestedScrollView during the redesign prevented images from loading

The main lesson learned was the importance of testing after every single step rather than waiting until the end. Most issues were easier to fix when caught early.

## 14. Future Improvements

- Add infinite scroll so new images load automatically when reaching the end of the list
- Allow browsing images by specific breed
- Persist favourites and cache across app restarts using Room database
- Add animated transitions between the main screen and the details screen
- Add a search bar to filter images by breed name

---

## 15. AI Usage Disclosure (Mandatory)

This project used **AntiGravity IDE** as the primary AI-assisted development tool throughout the implementation phase.

AI was used for:
- Generating Kotlin source files and XML layouts based on the Markdown specifications
- Fixing build errors and runtime crashes when provided with the exact error output
- Redesigning the UI based on a detailed prompt describing the desired changes

AI was NOT used for:
- Writing the project specifications and Markdown documentation
- Defining the architecture and implementation plan
- Testing the app on a real device
- Diagnosing bugs (the developer identified the issues; AI was asked to fix them)

All AI interactions were logged in `prompts_log.md` as part of the project repository.
