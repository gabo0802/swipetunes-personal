package com.example.swipetunes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM song_table")
    fun getAll(): Flow<List<SongEntity>>

    @Insert
    fun insertAll(songs: List<SongEntity>)

    @Query("DELETE FROM song_table")
    fun deleteAll()
}