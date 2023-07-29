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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import cn.skyrin.common.compat.getDrawableFromPkgName
import cn.skyrin.common.util.getDate
import cn.skyrin.common.util.getDay
import cn.skyrin.ntfs.R
import cn.skyrin.ntfs.data.bean.OngoingNotification
import cn.skyrin.ntfs.ui.helper.DrawableWrapper
import cn.skyrin.ntfs.ui.theme.NtfsTheme
import cn.skyrin.ntfs.util.toast.showToast
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.util.*

@Composable
fun NotificationsScreen(
    ntfs: List<OngoingNotification> = emptyList(),
    snoozeAction: (uid: String, key: String, snoozeDuration: Long) -> Unit
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
    name = "Night Mode"
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
        NtfsCard(notification = notification, snoozeAction = { _, _, _ -> })
    }
}

@Composable
fun NtfsCard(
    notification: OngoingNotification,
    snoozeAction: (uid: String, key: String, snoozeDuration: Long) -> Unit
) {
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
    snoozeAction: (uid: String, key: String, snoozeDuration: Long) -> Unit
) {
    val ctx = LocalContext.current
    var expanded by rememberSaveable { mutableStateOf(false) }
    var expandedDropMenu by remember { mutableStateOf(false) }

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = rememberDrawablePainter(ctx.getDrawableFromPkgName(notification.pkg)),
                        contentDescription = "app icon",
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${notification.title}",
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        maxLines = 1,
                        softWrap = false,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${notification.text}", style = MaterialTheme.typography.body1
                )

                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (notification.isSnoozed) {
                        val resumeDate =
                            Date(
                                (notification.snoozeAt?.time
                                    ?: System.currentTimeMillis())
                                        + notification.snoozeDurationMs
                            )
                        Text(
                            text = if (notification.snoozeDurationMs == 0L) stringResource(id = R.string.snoozed) else stringResource(
                                R.string.reboot_to_un_snoozed,
                                getDate(resumeDate)
                            )
                        )
                    } else {
                        Text(text = stringResource(R.string.showing))
                    }
                }
            }

            IconButton(
                onClick = {
                    if (notification.isSnoozed.not()) {
                        expandedDropMenu = true
                    } else {
                        ctx.showToast(R.string.snoozed_tips)
                    }
                }
            ) {
                Icon(
                    imageVector = if (notification.isSnoozed) Icons.Filled.Snooze else Icons.Filled.NotificationsActive,
                    contentDescription = stringResource(R.string.snoozed),
                )

                MyDropdownMenu(
                    expanded = expandedDropMenu,
                    offset = DpOffset(10.dp, 0.dp),
                    onDismissRequest = {
                        expandedDropMenu = false
                    }
                ) { snoozeDurationMs, textId ->
                    snoozeAction(notification.uid, notification.key, snoozeDurationMs)
                    ctx.showToast(textId)
                }
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
fun MyDropdownMenuPreview() {
    NtfsTheme {
        MyDropdownMenu(onDismissRequest = {

        }) { _, _ ->

        }
    }
}

@Composable
fun MyDropdownMenu(
    expanded: Boolean = false,
    offset: DpOffset = DpOffset.Zero,
    onDismissRequest: () -> Unit,
    onClick: (Long, Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        offset = offset,
        onDismissRequest = { onDismissRequest() }
    ) {
        snoozeDurationMsMap.forEach { (k, v) ->
            SnoozeDurationItem(
                durationText = stringResource(id = k),
                onClick = {
                    onClick(v, k)
                    onDismissRequest()
                }
            )
        }
    }
}

const val snoozeDurationDays360: Long = 1000 * 60 * 60 * 24 * 360L // 360 days
const val snoozeDurationDays30: Long = 1000 * 60 * 60 * 24 * 30L // 30 days
const val snoozeDurationDays15: Long = 1000 * 60 * 60 * 24 * 15L // 15 days
const val snoozeDurationDays7: Long = 1000 * 60 * 60 * 24 * 7L // 7 days
const val snoozeDurationDays1: Long = 1000 * 60 * 60 * 24 // 1 days
const val snoozeDurationHour8: Long = 1000 * 60 * 60 * 8 // 8 hour
const val snoozeDurationHour1: Long = 1000 * 60 * 60 // 1 hour
const val snoozeDurationMin30: Long = 1000 * 60 * 30 // 30 minute
const val snoozeDurationMin1: Long = 1000 * 60 // 1 minute

val snoozeDurationMsMap = mapOf(
    R.string.days_360 to snoozeDurationDays360,
    R.string.days_30 to snoozeDurationDays30,
    R.string.days_15 to snoozeDurationDays15,
    R.string.days_7 to snoozeDurationDays7,
    R.string.days_1 to snoozeDurationDays1,
    R.string.hour_8 to snoozeDurationHour8,
    R.string.hour_1 to snoozeDurationHour1,
    R.string.min_30 to snoozeDurationMin30,
    R.string.min_1 to snoozeDurationMin1
)

@Composable
fun SnoozeDurationItem(durationText: String, onClick: () -> Unit) {
    DropdownMenuItem(onClick = { onClick() }) {
        Icon(imageVector = Icons.Filled.Snooze, contentDescription = "Snooze")
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = durationText)
    }
}

@Preview
@Composable
fun NtfhCardPreview() {
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