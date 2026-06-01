package com.mikeeckels.obdash.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.mikeeckels.obdash.BuildConfig
import com.mikeeckels.obdash.update.model.UpdateInfo
//import timber.log.Timber
import java.io.IOException

class UpdateChecker(private val client: OkHttpClient = OkHttpClient()) {
    suspend fun fetchLatestRelease(): Result<UpdateInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(BuildConfig.RELEASE_URL)
                    .header("Accept", "application/vnd.github+json")
                    .header("X-GitHub-Api-Version", "2022-11-28")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext Result.failure(IOException("HTTP ${response.code}"))
                    }

                    val body = response.body.string()
                    if (body.isEmpty()) {
                        return@withContext Result.failure(IOException("Empty response body"))
                    }

                    val json = JSONObject(body)
                    val version = json.getString("tag_name").removePrefix("v")
                    val assets = json.optJSONArray("assets")
                        ?: return@withContext Result.failure(IOException("Missing or invalid 'assets' key"))

                    if (assets.length() == 0) {
                        return@withContext Result.failure(IOException("Assets array empty"))
                    }

                    val downloadUrl = assets.getJSONObject(0).getString("browser_download_url")

                    Result.success(UpdateInfo(version = version, downloadUrl = downloadUrl))
                }

            } catch (e: Exception) {
                //Timber.e(e, "Update check failed")
                Result.failure(e)
            }
        }
    }

    fun isNewAvailable(current: String, latest: String): Boolean {
        val currentParts = current.split(".").mapNotNull { it.toIntOrNull() }
        val latestParts = latest.split(".").mapNotNull { it.toIntOrNull() }
        val maxSize = maxOf(currentParts.size, latestParts.size)

        for (i in 0 until maxSize) {
            val currentPart = currentParts.getOrElse(i) { 0 }
            val latestPart = latestParts.getOrElse(i) { 0 }

            when {
                latestPart > currentPart -> return true
                latestPart < currentPart -> return false
            }
        }
        return false
    }
}