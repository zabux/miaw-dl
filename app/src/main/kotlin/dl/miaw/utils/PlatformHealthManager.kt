package dl.miaw.utils

import io.btch.downloader.BtchDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

enum class HealthStatus {
    ONLINE,    // 🟢 Green - Service responding normally
    DEGRADED,  // 🟡 Yellow - Slow / status note returned
    OFFLINE,   // 🔴 Red - HTTP 500 or Connection failed
    CHECKING   // ⏳ Grey - Status check in progress
}

data class PlatformHealthInfo(
    val status: HealthStatus,
    val timestamp: Long = System.currentTimeMillis(),
    val message: String? = null
)

object PlatformHealthManager {

    private val cache = ConcurrentHashMap<String, PlatformHealthInfo>()

    // Sample test inputs for each platform for fast status pinging
    private val testInputs = mapOf(
        "ttdl"              to "https://vt.tiktok.com/sample",
        "igdl"              to "https://www.instagram.com/p/sample",
        "twitter"           to "https://twitter.com/user/status/123",
        "youtube"           to "https://youtu.be/sample",
        "fbdown"            to "https://www.facebook.com/watch/?v=123",
        "capcut"            to "https://www.capcut.com/t/sample",
        "gdrive"            to "https://drive.google.com/file/d/sample",
        "pinterest"         to "wallpaper",
        "rednote"            to "http://xhslink.com/sample",
        "rednote-profile"    to "https://www.xiaohongshu.com/user/profile/sample",
        "douyin"            to "https://v.douyin.com/sample",
        "snackvideo"        to "https://s.snackvideo.com/p/sample",
        "cocofun"           to "https://www.icocofun.com/share/post/123",
        "spotify"           to "https://open.spotify.com/track/sample",
        "yts"               to "test query",
        "soundcloud"        to "https://soundcloud.com/sample/track",
        "threads"           to "https://www.threads.net/@user/post/sample",
        "kuaishou"          to "https://v.kuaishou.com/sample",
        "mediafire"         to "https://www.mediafire.com/file/sample/file.zip",
        "aio"               to "https://vt.tiktok.com/sample"
    )

    fun getStatus(platformKey: String): HealthStatus {
        return cache[platformKey]?.status ?: HealthStatus.CHECKING
    }

    fun getAllStatuses(): Map<String, HealthStatus> {
        return cache.mapValues { it.value.status }
    }

    suspend fun checkPlatform(api: BtchDownloader, platformKey: String): HealthStatus = withContext(Dispatchers.IO) {
        val testUrl = testInputs[platformKey] ?: "test"
        cache[platformKey] = PlatformHealthInfo(HealthStatus.CHECKING)
        
        try {
            val rawResponse = api.raw(platformKey, testUrl)
            val isError = rawResponse.contains("\"status\":false") || 
                          rawResponse.contains("\"status\": 500") ||
                          rawResponse.contains("Failed request") ||
                          rawResponse.contains("Internal Server Error")
            
            val newStatus = if (isError) HealthStatus.DEGRADED else HealthStatus.ONLINE
            cache[platformKey] = PlatformHealthInfo(newStatus)
            newStatus
        } catch (e: Exception) {
            val status = if (e.message?.contains("500") == true) HealthStatus.OFFLINE else HealthStatus.DEGRADED
            cache[platformKey] = PlatformHealthInfo(status, message = e.message)
            status
        }
    }

    suspend fun checkAllPlatforms(
        api: BtchDownloader,
        platforms: List<String>,
        onStatusUpdate: (Map<String, HealthStatus>) -> Unit
    ) = withContext(Dispatchers.IO) {
        // Set all to checking initially if not already in cache
        platforms.forEach { key ->
            if (!cache.containsKey(key)) {
                cache[key] = PlatformHealthInfo(HealthStatus.CHECKING)
            }
        }
        onStatusUpdate(getAllStatuses())

        // Run health checks concurrently (chunks of 5 to avoid overwhelming)
        platforms.chunked(5).forEach { batch ->
            batch.map { key ->
                async {
                    checkPlatform(api, key)
                }
            }.awaitAll()
            onStatusUpdate(getAllStatuses())
        }
    }
}
