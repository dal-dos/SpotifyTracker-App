package com.example.spotifytracker

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SpotifyDataTable")
data class SpotifyDataEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "username_column")//Username
    var username: String = "",

    @ColumnInfo(name = "recent_column")//Recently played
    var recentlyPlayed: String = "[]",

    @ColumnInfo(name = "suggested_column")//Suggested music
    var suggested: String = "[]",

    @ColumnInfo(name = "favorite_genre_column")//Favorite genre
    var favoriteGenre: String = "[]",

    @ColumnInfo(name = "favorite_artist_column")//Favorite artist
    var favoriteArtist: String = "[]"
)

