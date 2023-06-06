# Weather Application - Android Studio

This is a weather application developed in Android Studio using Kotlin programming language. The application fetches weather data from an API and displays the current weather information for a given location.

## Live preview

https://github.com/PiotrPabichCode/weather-app-kotlin/assets/87279370/4f5aee27-5567-4fa5-a1ec-5d74659c41db

## Features

- Fetches weather data from a third-party API(https://openweathermap.org/api).
- Allows to set favourite city to quickly access its weather information.
- Displays the current weather conditions, including temperature, humidity, wind speed etc.
- Supports searching for weather information for different locations.
- Fetches weather data at regular intervals to provide up-to-date information.
- Provides a user-friendly interface to view the weather details.

## Installation

To use the Weather Application, follow these steps:

1. Clone the repository or download the source code files.

   ```shell
   git clone https://github.com/PiotrPabichCode/weather-app-kotlin
   ```

2. Open the project in Android Studio.
3. Build and run the application on an Android emulator or a physical device.

## Configuration

Before running the application, you need to obtain an API key for weather data. Follow these steps to configure the API key:

1. Register an account on the weather data provider's website.

2. Obtain an API key for accessing weather data.

3. Open the `utils/Constants.kt` file in the project.

4. Replace the placeholder value for `API_KEY` with your actual API key.

   ```kotlin
   const val API_KEY = "YOUR_API_KEY"
   ```

## Usage

The Weather Application provides a simple and intuitive user interface to interact with. Here's how you can use it:

1. Launch the application on your Android device.

2. Allow the application to access your device's location (optional).

3. The application will display the current weather information for your location by default.

4. To search for weather information for a different location, tap the search icon.

5. Enter the name of the location you want to search for and tap the search button.

6. The application will fetch the weather data for the specified location and display it on the screen.

## Technologies

The Weather Application relies on the following dependencies:

- OkHttpClient: HTTP client for making API calls.
- Gson: A library for converting JSON responses to Kotlin objects.
- Glide: A fast and efficient image loading library.
- RecyclerView: A flexible view for displaying a collection of items.
- Room: An Android Jetpack database to store data
- DataStore: An Android Jetpack storage to store user preferences
