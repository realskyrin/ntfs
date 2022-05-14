package cn.skyrin.ntfs.ui.pages

import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import cn.skyrin.common.compat.getDrawableFromPkgName
import cn.skyrin.common.util.getDay
import cn.skyrin.ntfs.R
import cn.skyrin.ntfs.data.bean.OngoingNotification
import cn.skyrin.ntfs.ui.helper.DrawableWrapper
import cn.skyrin.ntfs.ui.theme.NtfsTheme
import java.util.*

@Composable
fun NotificationsScreen(
    ntfs: List<OngoingNotification> = emptyList(),
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
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
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

@Preview
@Composable
fun NtfhCardPreview(){
    NtfsTheme {
        NtfhCard {}
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