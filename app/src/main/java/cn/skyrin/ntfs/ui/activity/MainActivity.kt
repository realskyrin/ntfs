package cn.skyrin.ntfs.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
    LocaleUtils.setLocale(ctx, languageZh)

    Box(Modifier.fillMaxSize()) {
        val connected by isNotificationListenerConnected.observeAsState(initial = false)
        if (connected) {
            Notifications(
                notifications,
                refreshAction = {
                    sendRefreshIntent()
                }
            ) { uid, key ->
                sendSnoozeIntent(uid, key)
                viewModel.updateSnoozeStatus(uid, true, Date(), snoozeDurationMs)
            }
        } else {
            PermissionScreen {
                ctx.openNotificationServiceSettings()
            }
        }

        AppBar(
            languageZh = languageZh,
            onDarkThemeClick = {
                store.darkTheme = store.darkTheme.not()
                viewModel.darkTheme.postValue(store.darkTheme)
            },
            onLanguageClick = {
                store.languageZh = store.languageZh.not()
                viewModel.languageZh.postValue(store.languageZh)
            },
            onSettingsClick = {
                ctx.openNotificationServiceSettings()
            }
        )
    }
}

fun sendRefreshIntent() {
    val intent = Intent(Constants.ACTION_REFRESH_NOTIFICATION)
    app.sendBroadcast(intent)
}

@Preview
@Composable
fun AppBarPreview() {
    NtfsTheme {
        AppBar(
            languageZh = true,
            onDarkThemeClick = {},
            onLanguageClick = {},
            onSettingsClick = {})
    }
}

@Composable
fun AppBar(
    languageZh: Boolean,
    onDarkThemeClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = if (languageZh) stringResource(R.string.app_name) else stringResource(R.string.app_name))
        },
        Modifier
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .graphicsLayer(
                alpha = 0.8f,
                shadowElevation = 8.dp.value,
                shape = Shapes.medium,
                clip = true
            ),
        backgroundColor = MaterialTheme.colors.background,
        actions = {
            IconButton(onClick = {
                onDarkThemeClick()
            }) {
                Icon(
                    imageVector = Icons.Filled.DarkMode,
                    contentDescription = "DarkMode",
                )
            }
            IconButton(onClick = {
                onLanguageClick()
            }) {
                Icon(
                    imageVector = Icons.Filled.Language,
                    contentDescription = "Language",
                )
            }
            IconButton(onClick = {
                onSettingsClick()
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                )
            }
        }
    )
}

@Composable
fun Notifications(
    ntfs: List<OngoingNotification> = emptyList(),
    refreshAction: (() -> Unit)? = null,
    snoozeAction: (uid: String, key: String) -> Unit
) {
    val ctx = LocalContext.current
    Surface(color = MaterialTheme.colors.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.height(94.dp))
            }
            items(items = ntfs) { notification ->
                NtfsCard(notification = notification, snoozeAction = snoozeAction)
            }
            item {
                NtfhCard {
                    openNtfh(ctx)
                }
            }
            item {
                RefreshCard {
                    refreshAction?.invoke()
                    ctx.showToast(R.string.refresh_success)
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun openNtfh(ctx: Context) {
    runCatching {
        ctx.startActivity(getIntentWithMarket(MARKET_NTFH_APP_SCHEME))
    }.getOrElse {
        // market app not installed, open web browser
        ctx.openUrlOnBrowser(URL.NTFH_APP_URL)
    }
}

@Composable
fun NtfhCard(onClick: () -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(
            vertical = 4.dp, horizontal = 8.dp
        )
    ) {
        Image(painter = painterResource(id = R.drawable.pic_ntfh),
            contentDescription = "通知助手",
            Modifier.clickable { onClick() }
        )
    }
}

@Composable
fun RefreshCard(onClick: () -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(
            vertical = 4.dp, horizontal = 8.dp
        )
    ) {
        Text(
            text = stringResource(R.string.refresh),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .clickable {
                    onClick()
                })
    }
}

@Preview
@Composable
fun NtfhCardPreview() {
    NtfsTheme {
        RefreshCard {}
    }
}

@Composable
fun NtfsCard(notification: OngoingNotification, snoozeAction: (uid: String, key: String) -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(
            vertical = 4.dp, horizontal = 8.dp
        )
    ) {
        NotificationCardContent(notification, snoozeAction = snoozeAction)
    }
}

@Composable
fun NotificationCardContent(
    notification: OngoingNotification,
    snoozeAction: (uid: String, key: String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colors.primary,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                )
        ) {
            Column(
                Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Row {
                    DrawableWrapper(
                        drawableStart = LocalContext.current.getDrawableFromPkgName(notification.pkg)
                            ?.toBitmap(width = 48, height = 48)?.asImageBitmap(),
                    ) {
                        Text(
                            text = "${notification.title}",
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }
                Text(
                    text = "${notification.text}", style = MaterialTheme.typography.body1
                )

                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (notification.isSnoozed) {
                        val resumeDate =
                            Date(System.currentTimeMillis() + notification.snoozeDurationMs)
                        Text(
                            text = stringResource(
                                R.string.reboot_to_un_snoozed,
                                getDay(resumeDate)
                            )
                        )
                    } else {
                        Text(text = stringResource(R.string.showing))
                    }
                }
            }

            IconButton(onClick = {
                if (notification.isSnoozed.not()) {
                    snoozeAction(notification.uid, notification.key)
                }
            }) {
                Icon(
                    imageVector = if (notification.isSnoozed) Icons.Filled.Snooze else Icons.Filled.NotificationsActive,
                    contentDescription = stringResource(R.string.snoozed),
                )
            }

            IconButton(
                onClick = { expanded = !expanded },
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) stringResource(R.string.show_less) else stringResource(
                        R.string.show_more
                    )
                )
            }
        }
    }
}

const val snoozeDurationMs: Long = 1000 * 60 * 60 * 24 * 30L // 30 days

fun sendSnoozeIntent(uid: String, key: String) {
    val intent = Intent(Constants.ACTION_SNOOZE_NOTIFICATION)
    intent.putExtra(Constants.EXTRA_NOTIFICATION_UID, uid)
    intent.putExtra(Constants.EXTRA_NOTIFICATION_KEY, key)
    intent.putExtra(Constants.EXTRA_SNOOZE_DURATION_MS, snoozeDurationMs)
    app.sendBroadcast(intent)
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "NtfsCardPreview"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun NtfsCardPreview() {
    NtfsTheme {
        val notification = OngoingNotification(
            id = 0,
            uid = "uidaoisduasdasduad",
            key = "key",
            title = "微信通知",
            text = "微信通知正在后台运行",
            pkg = "com.tencent.mm",
            label = "微信",
            channelId = "10001",
            isSnoozed = true,
            snoozeAt = Date(),
            snoozeDurationMs = 5000,
            recordAt = Date(),
            updateAt = Date(),
        )
        NtfsCard(notification = notification, snoozeAction = { _, _ -> })
    }
}

@Composable
fun PermissionScreen(onContinueClicked: () -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.need_permission_tips))
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text(stringResource(R.string.get_access))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun PermissionScreenPreview() {
    NtfsTheme {
        PermissionScreen(onContinueClicked = {})
    }
}

@Composable
fun DrawableWrapper(
    modifier: Modifier = Modifier,
    @DrawableRes drawableTop: Int? = null,
    @DrawableRes drawableStart: Int? = null,
    @DrawableRes drawableBottom: Int? = null,
    @DrawableRes drawableEnd: Int? = null,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier) {
        val (refImgStart, refImgTop, refImgBottom, refImgEnd, refContent) = createRefs()
        Box(Modifier.constrainAs(refContent) {
            top.linkTo(drawableTop?.let { refImgTop.bottom } ?: parent.top)
            bottom.linkTo(drawableBottom?.let { refImgBottom.top } ?: parent.bottom)
            start.linkTo(drawableStart?.let { refImgStart.end } ?: parent.start)
            end.linkTo(drawableEnd?.let { refImgEnd.start } ?: parent.end)
        }) {
            content()
        }
        drawableTop?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                Modifier.constrainAs(refImgTop) {
                    top.linkTo(parent.top)
                    start.linkTo(refContent.start)
                    end.linkTo(refContent.end)
                }
            )
        }
        drawableStart?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                Modifier.constrainAs(refImgStart) {
                    top.linkTo(refContent.top)
                    bottom.linkTo(refContent.bottom)
                    start.linkTo(parent.start)
                }
            )
        }
        drawableBottom?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                Modifier.constrainAs(refImgBottom) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(refContent.start)
                    end.linkTo(refContent.end)
                }
            )
        }
        drawableEnd?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                Modifier.constrainAs(refImgEnd) {
                    top.linkTo(refContent.top)
                    bottom.linkTo(refContent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
fun DrawableWrapper(
    modifier: Modifier = Modifier,
    drawableTop: ImageBitmap? = null,
    drawableStart: ImageBitmap? = null,
    drawableBottom: ImageBitmap? = null,
    drawableEnd: ImageBitmap? = null,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier) {
        val (refImgStart, refImgTop, refImgBottom, refImgEnd, refContent) = createRefs()
        Box(Modifier.constrainAs(refContent) {
            top.linkTo(drawableTop?.let { refImgTop.bottom } ?: parent.top)
            bottom.linkTo(drawableBottom?.let { refImgBottom.top } ?: parent.bottom)
            start.linkTo(drawableStart?.let { refImgStart.end } ?: parent.start)
            end.linkTo(drawableEnd?.let { refImgEnd.start } ?: parent.end)
        }) {
            content()
        }
        drawableTop?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                Modifier.constrainAs(refImgTop) {
                    top.linkTo(parent.top)
                    start.linkTo(refContent.start)
                    end.linkTo(refContent.end)
                }
            )
        }
        drawableStart?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                Modifier.constrainAs(refImgStart) {
                    top.linkTo(refContent.top)
                    bottom.linkTo(refContent.bottom)
                    start.linkTo(parent.start)
                }
            )
        }
        drawableBottom?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                Modifier.constrainAs(refImgBottom) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(refContent.start)
                    end.linkTo(refContent.end)
                }
            )
        }
        drawableEnd?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                Modifier.constrainAs(refImgEnd) {
                    top.linkTo(refContent.top)
                    bottom.linkTo(refContent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}
