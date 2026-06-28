package dl.miaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dl.miaw.ui.MainScreen
import dl.miaw.utils.AnalyticsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Init Mixpanel Analytics
        AnalyticsManager.init(this, "3c21db73c2b1e4722241404604e60b54")
        AnalyticsManager.trackEvent("App_Opened")

        setContent {
            MainScreen()
        }
    }
}
