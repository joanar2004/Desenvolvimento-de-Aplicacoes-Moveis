# Assignment - Cool Weather App
Course: LEIM  
Student(s): Joana Ramos  
Date: April 2024  
Repository URL: https://github.com/joanar2004/Cool-Weather-App.git

---

## 1. Introduction
The **Cool Weather App** is an Android application developed as part of the LEIM course. The primary goal of this project is to create a functional weather application that consumes a REST API (Open-Meteo) to provide real-time weather data based on the user's geographic location. The project explores core Android concepts such as Activity lifecycle, UI layouts (ConstraintLayout), permissions, GPS location services, JSON parsing with Gson, and dynamic theming.

## 2. System Overview
The application provides the following features:
*   **Automatic Location**: Upon startup, the app requests GPS permissions and retrieves the device's current latitude and longitude.
*   **Real-time Weather**: Fetches temperature, wind speed, wind direction, and atmospheric pressure from the Open-Meteo API.
*   **Manual Update**: Users can manually enter coordinates to check the weather in different parts of the world.
*   **Dynamic Theming**: The app automatically switches between **Day** and **Night** themes based on the `is_day` status returned by the weather API.
*   **Adaptive Layout**: Supports both Portrait and Landscape orientations with optimized UI for each.
*   **Resource-based Weather Mapping**: Weather descriptions and icons are mapped using a centralized XML resource file (`weather_codes.xml`) instead of hardcoded values.

## 3. Architecture and Design
The project follows a standard Android architectural pattern:
*   **View Layer**: XML-based layouts using `ConstraintLayout` for the main structure and `LinearLayout` for data groupings. Different layout files are used for `layout` and `layout-land`.
*   **Data Layer**: 
    *   `WeatherData.kt`: Contains data classes designed to match the JSON response structure from the Open-Meteo API.
    *   `weather_codes.xml`: An array resource that maps WMO weather codes to human-readable descriptions and drawable resource names.
*   **Controller Layer**: `MainActivity.kt` handles the logic for permission requests, GPS location retrieval, network calls (via a background thread), and UI updates.
*   **Theming**: Custom themes defined in `themes.xml` (Day/Night) that change the background and widget styles dynamically.

## 4. Implementation
### Key Components:
*   **GPS Integration**: Uses `FusedLocationProviderClient` from Google Play Services to obtain high-accuracy coordinates.
*   **Network Operations**: API calls are executed on a separate `Thread` to avoid blocking the Main UI Thread.
*   **JSON Parsing**: Uses the `Gson` library to deserialize the API response into Kotlin objects.
*   **Theme Switching**: When the weather data indicates a change from day to night (or vice-versa), the Activity restarts with a specific Intent containing the cached weather data to ensure a seamless visual transition.
*   **Weather Code Mapping**: Implements a parser that reads the `weather_codes` string-array, splits the entries (Code;Image;Description), and updates the `ImageView` and description `TextView` accordingly.

## 5. Testing and Validation
*   **Permission Handling**: Verified that the app correctly asks for `ACCESS_FINE_LOCATION` and handles cases where the user denies permission (fallback to default coordinates).
*   **Orientation Changes**: Tested the transition between Portrait and Landscape to ensure that the input fields and weather data are preserved.
*   **Edge Cases**:
    *   Empty input fields for manual updates show a Toast/Validation.
    *   No internet connection handling (basic exception catching).
    *   GPS unavailable (emulator/indoor) fallback to Lisbon coordinates.

## 6. Usage Instructions
### Requirements:
*   Android Studio Hedgehog (or newer).
*   Android Device or Emulator with API 24 (Nougat) or higher.
*   Internet connection.
*   Google Play Services installed (for GPS).

### Setup:
1. Clone the repository: `git clone https://github.com/joanar2004/Cool-Weather-App.git`
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Build and Run on your device/emulator.

---

# Development Process

## 12. Version Control and Commit History
The project utilized Git for version control. The commit history reflects an incremental development process:
1. Initial UI setup and layout design.
2. Integration of the Open-Meteo API.
3. Implementation of Day/Night themes.
4. Addition of GPS location services.
5. Refactoring of weather code logic to use XML resources.

## 13. Difficulties and Lessons Learned
*   **Gradle Conflicts**: Encountered issues with configuration mutations after resolution, solved by aligning Gradle and AGP versions.
*   **Activity Recreation**: Managing the state of the app while switching themes dynamically required careful use of Intents to pass data back to the "new" instance of the Activity.
*   **GPS Latency**: Learning how to handle the asynchronous nature of location requests and ensuring the UI updates only when the coordinates are available.

## 14. Future Improvements
*   **Location Search**: Implement a Geocoding search bar to allow users to search for cities by name instead of coordinates.
*   **Forecast**: Add a horizontal RecyclerView to show a 7-day or 24-hour weather forecast.
*   **Unit Testing**: Implement JUnit tests for the weather code parsing logic.
*   **WorkManager**: Use WorkManager to periodically update a home screen widget with the current weather.

---

## 15. AI Usage Disclosure (Mandatory)
I used AI tools (specifically a coding assistant) to:
*   Debug and fix Gradle build errors related to configuration resolution.
*   Generate the boilerplate code for `FusedLocationProviderClient`.
*   Help structure the logic for parsing the `weather_codes.xml` string array.
*   Draft the initial structure of this README file.
All logic was reviewed, tested, and integrated manually to ensure it meets the project requirements and course standards. I remain responsible for all content and implementation.
