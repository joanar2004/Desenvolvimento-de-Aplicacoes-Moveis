# 01 — Application Overview

## Purpose

DogBreeds Explorer is a mobile application that allows users to browse random dog images fetched from a public REST API. Users can refresh the image feed, view details about each image, mark images as favourites, and access the app offline using a local cache.

## Target Users

- Dog lovers of all ages
- Students learning Android development
- Anyone looking for a simple and fun image browsing experience

## How the System Works

1. On launch, the app requests a list of random dog images from the Dog CEO API.
2. The images are displayed in a scrollable grid/list on the main screen.
3. The user can refresh the list to fetch new images.
4. Tapping an image opens a detail screen with more information.
5. The user can mark images as favourites (up to 5, FIFO queue).
6. Previously fetched images are cached locally (up to 50) to support offline access.
7. The app handles API errors and network failures gracefully, falling back to the cache when available.
