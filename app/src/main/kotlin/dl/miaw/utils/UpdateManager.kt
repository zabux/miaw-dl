package dl.miaw.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(val version: String, val changelog: String, val downloadUrl: String)

object UpdateManager {
    private const val REPO = "zabux/miaw-dl"
    
    suspend fun checkForUpdate(currentVersionName: String): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.github.com/repos/$REPO/releases/latest")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
            
            if (conn.responseCode == 200) {
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                val tagName = json.getString("tag_name").removePrefix("v")
                val currentVersion = currentVersionName.removePrefix("v")
                
                if (compareVersions(tagName, currentVersion) > 0) {
                    val assets = json.getJSONArray("assets")
                    var downloadUrl = ""
                    for (i in 0 until assets.length()) {
                        val asset = assets.getJSONObject(i)
                        if (asset.getString("name").endsWith(".apk")) {
                            downloadUrl = asset.getString("browser_download_url")
                            break
                        }
                    }
                    if (downloadUrl.isNotEmpty()) {
                        val rawBody = json.optString("body", "Tidak ada catatan pembaruan.")
                        val cleanBody = rawBody.lines()
                            .filter { !it.contains("github.com", ignoreCase = true) }
                            .joinToString("\n")
                            .replace("What's Changed", "Apa yang baru:")
                            .replace("New Contributors", "Kontributor Baru:")
                            .trim()
                            
                        return@withContext UpdateInfo(
                            version = tagName,
                            changelog = cleanBody.ifEmpty { "Pembaruan sistem dan perbaikan bug." },
                            downloadUrl = downloadUrl
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.split(".").map { it.toIntOrNull() ?: 0 }
        val parts2 = v2.split(".").map { it.toIntOrNull() ?: 0 }
        val length = maxOf(parts1.size, parts2.size)
        for (i in 0 until length) {
            val p1 = parts1.getOrElse(i) { 0 }
            val p2 = parts2.getOrElse(i) { 0 }
            if (p1 != p2) return p1.compareTo(p2)
        }
        return 0
    }

    suspend fun downloadAndInstall(context: Context, url: String, onProgress: (Float) -> Unit) = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            val fileLength = connection.contentLength
            val apkFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "update.apk")
            
            val input = connection.inputStream
            val output = apkFile.outputStream()
            
            val data = ByteArray(4096)
            var total: Long = 0
            var count: Int
            while (input.read(data).also { count = it } != -1) {
                total += count
                if (fileLength > 0) {
                    onProgress(total.toFloat() / fileLength)
                }
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()
            
            installApk(context, apkFile)
        } catch (e: Exception) {
            e.printStackTrace()
            onProgress(-1f)
        }
    }

    private fun installApk(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
