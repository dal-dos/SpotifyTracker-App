
<p align="center">
 <img width="100px" src="https://user-images.githubusercontent.com/32851308/205771027-c520d818-bbfd-4ebe-88c5-9b2c91000e1c.png" align="center"/>
</p>

<p align="center">
 <img width="500px" src="https://user-images.githubusercontent.com/32851308/205770120-0311a4da-e0d3-4935-9b61-e9cc022bf81f.png" align="center"/>
 <h3 align="center">Track all of your spotify stats and more!</h3>
</p>

<p align="center">
<img width="25px" src="https://user-images.githubusercontent.com/32851308/205768605-835090e4-d9d7-4781-ad8a-c136025ceb2e.png" align="center"/>
<img width="25px" src="https://user-images.githubusercontent.com/32851308/205768613-8eb78961-14ec-42ee-a579-e63b0828bcbf.png" align="center"/>
<img width="25px" src="https://user-images.githubusercontent.com/32851308/205768600-ecccca3e-d1e7-478e-a6e0-805f523ce9c0.png" align="center"/>
</p>

# Table of Contents
-   [Features](#features)
-   [About](#about)
-   [Build](#build)
-   [Run](#run)
-   [Contributors](#contributors)

# Features
-   [Home](#home)
    -   [Recently Played](#recently-played)
    -   [Suggested By Spotify](#suggested-by-spotify)
    -   [Favorite Tracks](#favorite-tracks)
    -   [Favorite Artists](#favorite-artists)
    -   [Favorite Genres](#favorite-genres)
-   [Stats](#stats)
    -   [Playtime past 24 hours](#playtime-past-24-hours)
    -   [Playtime past week](#playtime-past-week)
    -   [Popularity of Top Artists](#popularity-of-top-artists)
    -   [Playtime of favorite tracks](#playtime-of-favorite-tracks)
-   [Playlists](#playlists)
    -   [Suggested Today](#suggested-today)
    -   [Suggested Future](#suggested-future)
    -   [All Playlists](#all-playlists)
-   [Settings](#settings)
    -   [Recently Played Settings](#recently-played-settings)
    -   [Suggested By Spotify Settings](#suggested-by-spotify-settings)
    -   [Favorite Tracks Settings](#favorite-tracks-settings)
    -   [Favorite Artists Settings](#favorite-artists-settings)
    -   [Favorite Genres Settings](#favorite-genres-settings)
    -   [Playtime past 24 hours Settings](#playtime-past-24-hours-settings)
    -   [Playtime past week Settings](#playtime-past-week-settings)
    -   [Popularity of Top Artists Settings](#popularity-of-top-artists-settings)
    -   [Playtime of favorite tracks Settings](#playtime-of-favorite-tracks-settings)
    -   [Suggested Today Settings](#suggested-today-settings)
    -   [Suggested Future Settings](#suggested-future-settings)
    -   [All Playlists Settings](#all-playlists-settings)

# About
We used Spotify Api to create SpotifyTracker! This app tracks user statistics and more. It is able to login securely using spotify authentication. It allows analyzing and viewing of Spotify user data and statistics which Spotify does not show easily or often directly to the user, some these are: Listening time, Recently played, Favorite genre, Favorite artists. Music will be suggested based on local weather, and songs that are recommended and tailored to the user's interests by Spotify. 

# Build
Settings used to build the app:
- Kotlin version: 1.6.10
- App Version: 1.00
- Android Studio Version: Chipmunk

# Run
**A spotify account is _required_ to use any functionality of the app.** Google Play Services is required to use the playlists feature of the app.

# Home
The home page lists all data that we retrieve from spotify
## Recently Played 
Displays the songs that the user has recently played. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Recently Played Settings](#recently-played-settings)
## Suggested By Spotify
Displays the songs that are suggested by spotify based on the music you have listened to. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Suggested By Spotify Settings](#suggested-by-spotify-settings)
## Favorite Tracks
Displays the songs that the user has listened to the most in descending order. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Favorite Tracks Settings](#favorite-tracks-settings)
## Favorite Artists
Displays the artists that the user has listened to the most in descending order. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Favorite Artists Settings](#favorite-artists-settings)
## Favorite Genres
Displays the genres that the user has listened to the most in descending order. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Favorite Genres Settings](#favorite-genres-settings)
# Stats
## Playtime past 24 hours
Displays the minutes that the user has played in the past 24 hours. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Playtime past 24 hours Settings](#playtime-past-24-hours-settings)
## Playtime past week
Displays the minutes that the user has played in the past 7 days. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Playtime past week Settings](#playtime-past-week-settings)
## Popularity of Top Artists
Displays the users Favorite Artists popularity world wide in a pie chart. Users are able to click on the piechart to find the percent value compared to their other favorite artists. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Popularity of Top Artists Settings](#popularity-of-top-artists-settings)
## Playtime of favorite tracks
Displays the users favorite tracks playtime in a pie chart. Users are able to click on a the piechart to find how many hours they have played their favorite tracks for. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Playtime of favorite tracks Settings](#playtime-of-favorite-tracks-settings)
# Playlists
## Suggested Today
Displays suggested tracks for the user based on the current weather and history. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Suggested Today Settings](#suggested-today-settings)
## Suggested Future
Displays suggested tracks for the user based on the future weather and history. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [Suggested Future Settings](#suggested-future-settings)
## All Playlists
Displays all suggested playlists based on the weather and history. Users are able to collapse/expand the cardview by clicking on the arrow next to the heading which will always retain its form. For configurations check [All Playlists Settings](#all-playlists-settings)
# Settings
## Recently Played Settings
- Show/Hide the entire Recently Played Cardview (Default: Show)
- Number of tracks that are shown in recently played (Default: 5, Min: 1, Max: 50)
- Tracks that you listened to before a chosen date (Default: Current Day)
- Tracks that you listened to after a chosen date (Default: Beginning of time)
## Suggested By Spotify Settings
- Show/Hide the entire Suggested By Spotify Cardview (Default: Show)
- Number of tracks that are shown in suggested by spotify (Default: 5, Min: 1, Max: 50)
## Favorite Tracks Settings
- Show/Hide the entire Favorite Tracks Cardview (Default: Show)
- Number of tracks that are shown in favorite tracks (Default: 5, Min: 1, Max: 50)
- How far back you want the app to scan your music to determine your favorite tracks (Default: All-time)
## Favorite Artists Settings
- Show/Hide the entire Favorite Artists Cardview (Default: Show)
- Number of tracks that are shown in favorite artists (Default: 5, Min: 1, Max: 50)
- How far back you want the app to scan your music to determine your favorite artists (Default: All-time)
## Favorite Genres Settings
- Show/Hide the entire Favorite Genres Cardview (Default: Show)
- Number of tracks that are shown in favorite genres (Default: 5, Min: 1, Max: 50)
- How far back you want the app to scan your music to determine your favorite genres (Default: All-time)
## Playtime past 24 hours Settings
- Show/Hide the entire Playtime past 24 hours Cardview (Default: Show)
## Playtime past week Settings
- Show/Hide the entire Playtime past week Cardview (Default: Show)
## Popularity of Top Artists Settings
- Show/Hide the entire Popularity of Top Artists Cardview (Default: Show)
## Playtime of favorite tracks Settings
- Show/Hide the entire Playtime of favorite tracks Cardview (Default: Show)
## Suggested Today Settings
- Show/Hide the entire Suggested Today Cardview (Default: Show)
## Suggested Future Settings
- Show/Hide the entire Suggested Future Cardview (Default: Show)
## All Playlists Settings
- Show/Hide the entire All Playlists Cardview (Default: Show)

# Contributors
-[Dalveer Dosanjh](https://github.com/dal-dos)
-[Daven Chohan](https://github.com/davenchohan)
-[Arjun Shokar](https://github.com/ashokar19)
-[Keene Upathamp](https://github.com/kupa-fp3)
