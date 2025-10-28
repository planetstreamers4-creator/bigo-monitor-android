package com.example.bigostreammonitor

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StreamerRepository(private val streamerDao: StreamerDao) {
    val allStreamers: LiveData<List<Streamer>> = streamerDao.getAllStreamers()

    suspend fun insert(streamer: Streamer) = withContext(Dispatchers.IO) {
        streamerDao.insertStreamer(streamer)
    }

    suspend fun delete(streamer: Streamer) = withContext(Dispatchers.IO) {
        streamerDao.deleteStreamer(streamer)
    }

    suspend fun updateMonitoring(id: Int, monitoring: Boolean) = withContext(Dispatchers.IO) {
        streamerDao.updateMonitoring(id, monitoring)
    }
}
