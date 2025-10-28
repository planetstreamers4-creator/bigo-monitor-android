package com.example.bigostreammonitor

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StreamerDao {
    @Query("SELECT * FROM streamers")
    fun getAllStreamers(): LiveData<List<Streamer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStreamer(streamer: Streamer)

    @Delete
    suspend fun deleteStreamer(streamer: Streamer)

    @Query("UPDATE streamers SET isMonitoring = :monitoring WHERE id = :id")
    suspend fun updateMonitoring(id: Int, monitoring: Boolean)
}
