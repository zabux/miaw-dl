package dl.miaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.ads.MobileAds
import dl.miaw.ui.MainScreen
import dl.miaw.utils.AnalyticsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Init Mobile Ads
        MobileAds.initialize(this) {}
        
        // Init Firebase Analytics
        AnalyticsManager.init(this)
        AnalyticsManager.trackEvent("App_Opened")

        setContent {
            MainScreen()
        }
    }
}
