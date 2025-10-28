package com.example.bigostreammonitor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class StreamerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StreamerRepository
    val allStreamers: LiveData<List<Streamer>>

    init {
        val streamerDao = AppDatabase.getDatabase(application).streamerDao()
        repository = StreamerRepository(streamerDao)
        allStreamers = repository.allStreamers
    }

    fun insert(streamer: Streamer) = viewModelScope.launch {
        repository.insert(streamer)
        startMonitoring()
    }

    fun delete(streamer: Streamer) = viewModelScope.launch {
        repository.delete(streamer)
    }

    fun toggleMonitoring(id: Int, monitoring: Boolean) = viewModelScope.launch {
        repository.updateMonitoring(id, monitoring)
        if (monitoring) startMonitoring()
    }

    private fun startMonitoring() {
        val workRequest = PeriodicWorkRequestBuilder<MonitorWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
            "bigo_monitor", ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }
}
