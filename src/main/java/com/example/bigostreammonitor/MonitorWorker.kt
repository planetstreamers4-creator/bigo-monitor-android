package com.example.bigostreammonitor

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MonitorWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val streamers = inputData.getStringArray("STREAMER_IDS")?.toList() ?: emptyList()
        val client = OkHttpClient()

        streamers.forEach { bigoId ->
            try {
                val url = "https://www.bigo.tv/$bigoId"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful && response.body?.string()?.contains("live|正在直播|AO VIVO", ignoreCase = true) == true) {
                    // Pega M3U8 (simplificado: em produção, use JS injection ou proxy)
                    val m3u8Url = "https://example-bigo.m3u8/$bigoId/master.m3u8" // Substitua por detecção real via response

                    recordLive(m3u8Url, bigoId)
                    Log.d("MonitorWorker", "@$bigoId está ao vivo! Gravando...")
                }
            } catch (e: Exception) {
                Log.e("MonitorWorker", "Erro ao checar $bigoId: ${e.message}")
            }
        }

        Result.success(workDataOf("status" to "checked"))
    }

    private fun recordLive(m3u8: String, bigoId: String) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val output = File("/sdcard/Download/${bigoId}_live_$timestamp.mp4")

        // FFmpeg via Runtime (assuma binário em assets ou Termux)
        val cmd = arrayOf(
            "ffmpeg", "-i", m3u8, "-c", "copy", "-t", "1800", // 30 min
            "-bsf:a", "aac_adtstoasc", output.absolutePath, "-y"
        )
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
    }
}
