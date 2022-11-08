package com.example.spotifytracker

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SpotifyDataTable")
data class SpotifyDataEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "temp_column")
    var Placeholder: Int = 0
)