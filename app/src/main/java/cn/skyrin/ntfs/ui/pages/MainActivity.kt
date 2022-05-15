package cn.skyrin.ntfs.ui.pages

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import cn.skyrin.common.compat.getDrawableFromPkgName
import cn.skyrin.common.compat.openNotificationServiceSettings
import cn.skyrin.common.extentions.openUrlOnBrowser
import cn.skyrin.common.util.getDay
import cn.skyrin.ntfs.R
import cn.skyrin.ntfs.app.Constants
import cn.skyrin.ntfs.app.URL
import cn.skyrin.ntfs.app.URL.MARKET_NTFH_APP_SCHEME
import cn.skyrin.ntfs.app.app
import cn.skyrin.ntfs.app.isNotificationListenerConnected
import cn.skyrin.ntfs.data.bean.OngoingNotification
import cn.skyrin.ntfs.store.AppStore
import cn.skyrin.ntfs.store.Store.store
import cn.skyrin.ntfs.ui.components.MyTopAppBar
import cn.skyrin.ntfs.ui.helper.DrawableWrapper
import cn.skyrin.ntfs.ui.theme.NtfsTheme
import cn.skyrin.ntfs.ui.theme.Shapes
import cn.skyrin.ntfs.util.IntentUtil.getIntentWithMarket
import cn.skyrin.ntfs.util.LocaleUtils
import cn.skyrin.ntfs.util.toast.showToast
import cn.skyrin.ntfs.viewmodel.MainViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val store by lazy { AppStore(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            viewModel.darkTheme.postValue(store.darkTheme)
            val darkTheme by viewModel.darkTheme.observeAsState(initial = false)
            val systemUiController: SystemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = darkTheme.not()
            )

            NtfsTheme(darkTheme = darkTheme) {
                MyApp(viewModel)
            }
        }
    }
}

@Composable
fun MyApp(viewModel: MainViewModel) {
    val ctx = LocalContext.current
    val languageZh by viewModel.languageZh.observeAsState(initial = store.languageZh)
    val notifications by viewModel.ongoingNotifications.observeAsState(initial = listOf())
    val connected by isNotificationListenerConnected.observeAsState(initial = false)
    LocaleUtils.setLocale(ctx, languageZh)

    HostScreen(connected, notifications, viewModel, ctx, languageZh)
}

fun sendRefreshIntent() {
    val intent = Intent(Constants.ACTION_REFRESH_NOTIFICATION)
    app.sendBroadcast(intent)
}

fun openNtfh(ctx: Context) {
    runCatching {
        ctx.startActivity(getIntentWithMarket(MARKET_NTFH_APP_SCHEME))
    }.getOrElse {
        // market app not installed, open web browser
        ctx.openUrlOnBrowser(URL.NTFH_APP_URL)
    }
}

fun sendSnoozeIntent(uid: String, key: String, snoozeDurationMs: Long) {
    val intent = Intent(Constants.ACTION_SNOOZE_NOTIFICATION)
    intent.putExtra(Constants.EXTRA_NOTIFICATION_UID, uid)
    intent.putExtra(Constants.EXTRA_NOTIFICATION_KEY, key)
    intent.putExtra(Constants.EXTRA_SNOOZE_DURATION_MS, snoozeDurationMs)
    app.sendBroadcast(intent)
}
