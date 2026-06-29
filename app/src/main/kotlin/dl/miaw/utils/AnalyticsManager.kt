package dl.miaw.utils

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

object AnalyticsManager {
    private var mixpanel: MixpanelAPI? = null

    fun init(context: Context, token: String) {
        try {
            mixpanel = MixpanelAPI.getInstance(context, token, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun trackEvent(eventName: String, properties: Map<String, Any>? = null) {
        try {
            val jsonProps = JSONObject()
            properties?.forEach { (key, value) ->
                jsonProps.put(key, value)
            }
            mixpanel?.track(eventName, jsonProps)
            mixpanel?.flush() // Flush immediately for real-time dashboard
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
