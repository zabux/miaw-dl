package dl.miaw.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

@Serializable
data class HistoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val caption: String,
    val sourceUrl: String,
    val timestamp: Long = System.currentTimeMillis(),
    val platform: String,
    val mediaUrl: String = "",
    val mediaType: String = "video"
)

object HistoryManager {
    private const val PREFS_NAME = "download_history"
    private const val KEY_HISTORY = "history_list"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getHistory(context: Context): List<HistoryItem> {
        val prefs = getPrefs(context)
        val jsonStr = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            Json.decodeFromString<List<HistoryItem>>(jsonStr)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addToHistory(context: Context, caption: String, sourceUrl: String, platform: String, mediaUrl: String, mediaType: String) {
        val currentHistory = getHistory(context).toMutableList()
        // Avoid duplicates
        if (currentHistory.any { it.sourceUrl == sourceUrl && it.mediaUrl == mediaUrl }) {
            return
        }
        val newItem = HistoryItem(
            caption = caption.trim(),
            sourceUrl = sourceUrl.trim(),
            platform = platform,
            mediaUrl = mediaUrl,
            mediaType = mediaType
        )
        currentHistory.add(0, newItem) // add to top
        if (currentHistory.size > 50) {
            currentHistory.removeLast()
        }
        val jsonStr = Json.encodeToString(currentHistory)
        getPrefs(context).edit().putString(KEY_HISTORY, jsonStr).apply()
    }

    fun removeFromHistory(context: Context, id: String) {
        val currentHistory = getHistory(context).toMutableList()
        currentHistory.removeAll { it.id == id }
        val jsonStr = Json.encodeToString(currentHistory)
        getPrefs(context).edit().putString(KEY_HISTORY, jsonStr).apply()
    }

    fun clearHistory(context: Context) {
        getPrefs(context).edit().remove(KEY_HISTORY).apply()
    }
}
