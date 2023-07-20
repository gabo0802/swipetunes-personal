package com.example.swipetunes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_table")
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "trackID") val trackID: String?,
    @ColumnInfo(name = "artist") val artist: String?,
    @ColumnInfo(name = "albumCover") val albumCover: String?,
    @ColumnInfo(name = "preview") val preview: String?
)