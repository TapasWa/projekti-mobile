# Tankopedia App (Android Jetpack Compose)

This Android application is a "Tankopedia" that allows users to browse a list of tanks, view their details, mark them as favorites, and watch related YouTube videos. The app is built entirely using Jetpack Compose, Android's modern toolkit for building native UI.

## Features

*   **Tank List:** Displays a scrollable list of tanks, each showing its image, flag, and name.
*   **Search Functionality:** Users can search for tanks by name.
*   **Favorite System:**
    *   Mark/unmark tanks as favorites directly from the list or detail screen.
    *   Filter the list to show only favorite tanks.
*   **Tank Detail Screen:**
    *   Shows a larger image of the tank, its name, country flag, and a detailed description.
    *   Displays key specifications (Weight, Armor, Main Armament, Speed).
    *   Includes an embedded YouTube player to watch a video related to the tank.
*   **Random Tank:** A button to navigate to a random tank from the currently filtered list.
*   **Modern UI:**
    *   Built with Jetpack Compose.
    *   Features a blurred image background for a modern aesthetic.
    *   Edge-to-edge display, utilizing the full screen space.
    *   Animated transitions between the list and detail screens.

## How the App Works

The application is structured around a single `MainActivity` that hosts Jetpack Compose UI.

### Core Components:

1.  **`MainActivity.kt`:**
    *   Sets up the edge-to-edge display using `WindowCompat.setDecorFitsSystemWindows(window, false)` and makes system bars transparent.
    *   The main UI content is driven by the `AppContent` composable.

2.  **`AppContent` Composable:**
    *   **State Management:**
        *   `selectedTank`: Holds the currently selected `Tank` object to navigate to the detail screen. If `null`, the list screen is shown.
        *   `tanks`: A mutable list of `Tank` data objects, initialized with predefined tank information. This list is updated when a tank's favorite status changes.
    *   **Background:**
        *   A `Box` layout is used to layer UI elements.
        *   An `Image` composable at the bottom of the stack displays a blurred background image (`R.drawable.kf_51`) that fills the entire screen.
    *   **Navigation & Animation:**
        *   Uses `AnimatedContent` to provide a smooth scaling animation when switching between the `TankListScreen` and `TankDetailScreen` based on the `selectedTank` state.
        *   Padding for system bars (`WindowInsets.systemBars.asPaddingValues()`) is applied to the `AnimatedContent` to ensure main UI elements don't overlap with the status or navigation bars.
    *   **`updateFavorite` Function:** A helper function to update the favorite status of a tank in the main `tanks` list and also updates `selectedTank` if it's the one being modified.

3.  **`TankListScreen` Composable:**
    *   **Parameters:** Takes the list of `tanks`, a callback `onTankSelected` (to navigate to details), and `onFavoriteToggle`.
    *   **UI Elements:**
        *   `OutlinedTextField`: For search functionality. Filters the `tanks` list based on user input.
        *   `IconButton` (Star Icon): Toggles the `showOnlyFavorites` state, which further filters the list.
        *   `TextButton` ("Random"): Selects and navigates to a random tank from the currently filtered list.
        *   `LazyColumn`: Efficiently displays the list of filtered tanks.
        *   Each item in the `LazyColumn` is a `Card` containing:
            *   Tank image, flag image, and tank name.
            *   An `IconButton` (Star Icon) to toggle the favorite status of that specific tank using the `onFavoriteToggle` callback.
    *   **Filtering:** The displayed list (`filteredTanks`) is derived by filtering the main `tanks` list based on the `searchQuery` and `showOnlyFavorites` state.

4.  **`TankDetailScreen` Composable:**
    *   **Parameters:** Takes the selected `Tank` object, an `onBack` callback (to return to the list), and `onFavoriteToggle`.
    *   **UI Elements:**
        *   `TextButton` ("Back"): Navigates back to the `TankListScreen`.
        *   Displays the tank's flag, name (with a favorite toggle `IconButton`), large image, description, and specifications.
        *   Specifications are displayed in a formatted list.
        *   **`YoutubePlayer` Composable:** Embeds a YouTube player to display a video related to the tank. The video ID is extracted from the `youtubeURL` in the `Tank` data.
    *   **Local Favorite State:** Manages a local `isFavorite` state for the detail screen's favorite button, which calls `onFavoriteToggle` to update the global state.

5.  **`Tank` Data Class:**
    *   A simple data class holding all information about a tank:
        *   `name: String`
        *   `imageRes: Int` (Drawable resource ID for the tank image)
        *   `description: String`
        *   `country: String`
        *   `flagRes: Int` (Drawable resource ID for the country flag)
        *   `specs: Map<String, String>` (Key-value pairs for specifications)
        *   `youtubeURL: String?` (URL for the YouTube video)
        *   `isFavorite: Boolean` (Indicates if the tank is a favorite, defaults to `false`)

6.  **`YoutubePlayer` Composable (External Library):**
    *   This composable is likely implemented using an external library like `com.pierfrancescosoffritti.androidyoutubeplayer:core`. It takes a `videoId` and displays the YouTube video within the app.
    *   *(Note: The actual implementation of `YoutubePlayer` is not in the provided `MainActivity.kt` but is used there. The project's `build.gradle(.kts)` file would include this dependency.)*

### Data Flow for Favorites:

1.  User clicks the favorite icon (either in `TankListScreen` or `TankDetailScreen`).
2.  The respective `onFavoriteToggle` callback is invoked with the tank's name and its new favorite status.
3.  This callback points to the `updateFavorite` function in `AppContent`.
4.  `updateFavorite` creates a new list of tanks with the updated `isFavorite` status for the specific tank.
5.  This new list is assigned back to the `tanks` state variable in `AppContent`, triggering recomposition of `TankListScreen` (and `TankDetailScreen` if it's the one being modified and its `tank` prop is updated).

## Setup & Build

1.  Clone the repository.
2.  Open the project in Android Studio (latest stable version recommended).
3.  Ensure you have the Android SDK installed and configured.
4.  The project uses Gradle for building. Android Studio should handle the build process automatically.
5.  You might need an internet connection for Gradle to download dependencies, including the YouTube player library.
6.  The app uses drawable resources for images (e.g., `R.drawable.tiger1`, `R.drawable.saksa`). These images need to be present in the `app/src/main/res/drawable` folder.

## Dependencies

*   Jetpack Compose (UI, Material3, Foundation, Animation)
*   AndroidX Core KTX, Activity Compose, Lifecycle Runtime KTX
*   `com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0` (for YouTube playback)

## Potential Improvements / Future Work

*   Persist favorite tanks using SharedPreferences, Room database, or DataStore so they are saved across app sessions.
*   Fetch tank data from a remote API instead of hardcoding it.
*   Add more detailed error handling.
*   Implement unit and UI tests.
*   Add more sorting or filtering options for the tank list.
*   Allow users to add their own tanks or notes.
