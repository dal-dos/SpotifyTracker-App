package com.example.dalveer_dosanjh_myruns4

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "SpotifyDataTable")
data class SpotifyDataEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "temp_column")
    var Placeholder: Int = 0
)