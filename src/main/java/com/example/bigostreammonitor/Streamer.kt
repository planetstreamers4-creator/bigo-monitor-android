package com.example.bigostreammonitor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streamers")
data class Streamer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bigoId: String,
    val name: String? = null,
    val isMonitoring: Boolean = true
)
