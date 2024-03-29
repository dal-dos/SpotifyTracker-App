
<h1>DISCONTINUED</h1>
<h2>This application is no longer being maintained.</h2>
<p align="center">
  <a href="https://github.com/dal-dos/SpotifyTracker-App">
  <img width="100px" src="https://user-images.githubusercontent.com/32851308/205771027-c520d818-bbfd-4ebe-88c5-9b2c91000e1c.png" align="center"/> 
  </a>
</p>
<p align="center">
  <a href="https://github.com/dal-dos/SpotifyTracker-App">
  <img width="500px" src="https://user-images.githubusercontent.com/36864092/205780211-90ad85f9-199f-4803-a64b-9811622fd392.png" align="center"/>
  </a>
  <h2></h2>
 <h3 align="center">Track all of your spotify stats and more!</h3>
 <h6 align="center"><a href="https://davenchohan.github.io/spotify_tracker/home.html">Website</a></h6>
</p>



<p  align="center">
  <a href="https://developer.spotify.com/">
  <img width="25px" src="https://user-images.githubusercontent.com/32851308/205781496-51cab538-e5b4-46f5-ab23-ca6c0fbb1d20.png" align="center"/>
  </a>
  <a href="https://openweathermap.org/guide">
  <img width="25px" src="https://user-images.githubusercontent.com/32851308/205768613-8eb78961-14ec-42ee-a579-e63b0828bcbf.png" align="center"/>
  </a>
  <a href="https://developers.google.com/maps">
  <img width="25px" src="https://user-images.githubusercontent.com/32851308/205768600-ecccca3e-d1e7-478e-a6e0-805f523ce9c0.png" align="center"/>
  </a>
</p>

<h6 align="center"> For best viewing experience please use github dark mode </h6>

# Table of Contents
-   [About](#about)
-   [Releases](#releases)
-   [Run](#run)
-   [Special Instructions](#special-instructions)
-   [Project Videos](#project-videos)
-   [Diagrams](#diagrams)
-   [Features](#features)
-   [Contributors](#contributors)



# About
We used Spotify Api to create SpotifyTracker! This app tracks user statistics and more. It is able to login securely using spotify authentication. It allows analyzing and viewing of Spotify user data and statistics which Spotify does not show easily or often directly to the user, some these are: Listening time, Recently played, Favorite genre, Favorite artists. Music will be suggested based on local weather, and songs that are recommended and tailored to the user's interests by Spotify. 

Settings we used to build the app:
- Kotlin version: 1.6.10
- App Version: 1.00
- Android Studio Version: Chipmunk

# Releases
Downloads:
- [Release Page](https://github.com/dal-dos/SpotifyTracker-App/releases/tag/v1.00)
- [SpotifyTracker_v1_00.apk](https://github.com/dal-dos/SpotifyTracker-App/releases/download/v1.00/SpotifyTracker_v1_00.apk)
- [Source Code](https://github.com/dal-dos/SpotifyTracker-App/archive/refs/tags/v1.00.zip)

# Run
**A spotify account is _required_ to use any functionality of the app.** Google Play Services is required to use the playlists feature of the app.

# Special Instructions
**A spotify account is _required_ to use any functionality of the app.** Google Play Services is required to use the playlists feature of the app.

Spotify api requires app approval in order for any spotify user to be able to login to our app, due to this please follow the steps below for the meantime:

Send an email to dsd6@sfu.ca which contains your spotify email address

**_NOTE: SPOTIFY WILL NOT LET YOU LOGIN UNLESS YOU PROVIDE US YOUR EMAIL OR JUST USE THE ACCOUNT BELOW!_**

Username: **_Hidden_** (Only on canvas readme)

Password: **_Hidden_** (Only on canvas readme)

# Project Videos
- [Project Pitch](https://youtu.be/zigB7aDhHVg)
- [Show & Tell 1](https://youtu.be/xBSlSaaJBiE)
- [Show & Tell 2](https://youtu.be/yAzpF4IPh28)
- [Final Presentation](https://youtu.be/3x_FC1wfgG8)

# Diagrams
## MVVM Diagram
![MVVM](https://user-images.githubusercontent.com/32851308/205789444-324c8acb-9d5b-4d45-b098-867d106dbcf3.png)

## Threads Diagram
![Thread1](https://user-images.githubusercontent.com/32851308/205789475-ab5fe21a-fde4-4b53-8f87-2aa74b0fc3b0.png)
![Thread2](https://user-images.githubusercontent.com/32851308/205789483-cf4ccfb0-0366-43e7-9e6f-37be7b4aad44.png)
![Thread3](https://user-images.githubusercontent.com/32851308/205789495-b86144e5-6d21-48ff-a5f0-f2b6a842db03.png)



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
- [Dalveer Dosanjh](https://github.com/dal-dos)
    -   Home, Stats, Playlists, Settings, Spotify API
- [Daven Chohan](https://github.com/davenchohan)
    -   Home, Playlists, Spotify API, Weather API
- [Arjun Singh](https://github.com/ashokar19)
    -   Home, Stats, Weather API
- [Keene Upathamp](https://github.com/kupa-fp3)
    -   Playlists, Research
