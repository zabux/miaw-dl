package dl.miaw.utils

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

object AnalyticsManager {
    private var mixpanel: MixpanelAPI? = null

    fun init(context: Context, token: String) {
        try {
            mixpanel = MixpanelAPI.getInstance(context, token, true) // optOutTrackingDefault=true for GDPR or just normal init? We will use standard init.
            // Wait, MixpanelAPI.getInstance(context, token) is standard.
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Standard initialization without optOut param for broader compatibility
    fun initMixpanel(context: Context, token: String) {
        try {
            mixpanel = MixpanelAPI.getInstance(context, token)
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
