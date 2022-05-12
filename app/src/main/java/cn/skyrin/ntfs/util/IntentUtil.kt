package cn.skyrin.ntfs.util

import android.content.Intent
import android.net.Uri
import cn.skyrin.ntfs.app.URL

object IntentUtil {

    fun getIntentWithMarket(appMarketId: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(appMarketId)
            setPackage(URL.MARKET_ID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return intent
    }
}
