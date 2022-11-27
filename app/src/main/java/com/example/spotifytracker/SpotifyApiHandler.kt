package com.example.spotifytracker

import android.R.id
import com.adamratzman.spotify.*
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.javainterop.SpotifyContinuation
import com.adamratzman.spotify.models.*
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.utils.Market
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking


class SpotifyApiHandler(val token: Token) {
    private val clientID = "9609905ad0f54f66b8d574d367aee504"
    private val clientSecret = "7c07ad46a3b940c48b9858688574f3b6"
    private val REDIRECT_URI = "com.example.spotifytracker://callback"
    private var api: SpotifyClientApi? = null
    private val url: String = getSpotifyAuthorizationUrl(
        SpotifyScope.PLAYLIST_READ_PRIVATE,
        SpotifyScope.PLAYLIST_MODIFY_PRIVATE,
        SpotifyScope.USER_LIBRARY_READ,
        SpotifyScope.USER_LIBRARY_MODIFY,
        SpotifyScope.USER_FOLLOW_READ,
        SpotifyScope.USER_READ_RECENTLY_PLAYED,
        SpotifyScope.USER_READ_EMAIL,
        SpotifyScope.USER_READ_PRIVATE,
        SpotifyScope.USER_TOP_READ,
        clientId = clientID,
        redirectUri = REDIRECT_URI
    )

    /// Pulls the developer ClientID and ClientSecret tokens provided
    /// by Spotify and builds them into an object that can easily
    /// call public Spotify APIs.

    suspend fun buildSearchApi() {
        //val auth: SpotifyUserAuthorization = SpotifyUserAuthorization(null, token)
        println("debug: Access token is $token")

        api = spotifyClientApi(
            clientID,
            clientSecret,
            url,
            token
        ).build(true)

        //println("debug: " + api.browse.getNewReleases())
        //println("debug: " + api.personalization.getTopArtists(limit = 1)[0].name)
        //println("debug: " + api.personalization.getTopTracks(limit = 5).items.map { it.name })
        //println("debug: " + api)
        //println("debug: Recently Played:" + api!!.player.getRecentlyPlayed(limit = 5))

        api!!.spotifyApiOptions.enableDebugMode = true
    }

    fun getApi(): SpotifyClientApi? {
        return api
    }

    // Performs Spotify database query for queries related to user information. Returns
    // the results as a SpotifyPublicUser object.
    suspend fun userSearch(userQuery: String): SpotifyPublicUser? {
        return api!!.users.getProfile(userQuery)
    }

    // Performs Spotify database query for queries related to track information. Returns
    // the results as a SpotifySearchResult object.
    suspend fun trackSearch(searchQuery: String): SpotifySearchResult {
        return api!!.search.searchAllTypes(searchQuery, 50, 1, market = Market.US)
    }

    // Performs Spotify database query for queries related to user top tracks. Returns
    suspend fun userTopTracks(): List<Track> {
        if(api!!.spotifyApiOptions.enableDebugMode) {
            print("DEBUG MODE: APP: userTopTracks(): ")
            println("debug: " + api!!.personalization.getTopTracks(limit = 5, timeRange = ClientPersonalizationApi.TimeRange.LONG_TERM).items.map { it.name })
        }
        return api!!.personalization.getTopTracks(limit = 5, timeRange = ClientPersonalizationApi.TimeRange.LONG_TERM).items
    }

    suspend fun userTopArtists(): List<Artist> {
        if(api!!.spotifyApiOptions.enableDebugMode) {
            print("DEBUG MODE: APP: userTopArtists(): ")
            println("debug: " + api!!.personalization.getTopArtists(limit = 5, timeRange = ClientPersonalizationApi.TimeRange.LONG_TERM).items)
        }
        return api!!.personalization.getTopArtists(limit = 5, timeRange = ClientPersonalizationApi.TimeRange.LONG_TERM).items
    }

    suspend fun userTopGenres(): ArrayList<String> {
        if(api!!.spotifyApiOptions.enableDebugMode) {
            print("DEBUG MODE: APP: userTopGenres(): ")
            println("debug: " + api!!.personalization.getTopArtists(limit = 5, timeRange = ClientPersonalizationApi.TimeRange.LONG_TERM).items.map { it.genres})
        }
        val myGenres : ArrayList<String> = arrayListOf()

        api!!.personalization.getTopArtists(limit = 5, timeRange = ClientPersonalizationApi.TimeRange.LONG_TERM).items.map { myGenres.addAll(it.genres) }
        return myGenres
    }

    suspend fun userRecentlyPlayed(): List<PlayHistory> {
        if(api!!.spotifyApiOptions.enableDebugMode){
            print("DEBUG MODE: APP: userRecentlyPlayed(): ")
            println("debug: " + api!!.player.getRecentlyPlayed(limit = 5).items)
        }

        return api!!.player.getRecentlyPlayed(limit = 5).items
    }


    suspend fun userName(): String? {
        if(api!!.spotifyApiOptions.enableDebugMode){
            print("DEBUG MODE: APP: userName(): ")
            println("debug: " + api!!.users.getClientProfile().displayName)
        }

        return api!!.users.getClientProfile().displayName
    }

    fun userSuggested(): List<Track> {
        if(api!!.spotifyApiOptions.enableDebugMode){
            print("DEBUG MODE: APP: userSuggested(): ")
            //Received Status Code 400. Error cause: At least one seed (genre, artist, track) must be provided.
            //println(api!!.browse.getRecommendations().tracks)
            //println()
        }

        return arrayListOf()
    }
}