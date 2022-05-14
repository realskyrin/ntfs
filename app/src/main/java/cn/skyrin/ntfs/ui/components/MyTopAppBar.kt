package cn.skyrin.ntfs.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.skyrin.ntfs.R
import cn.skyrin.ntfs.ui.theme.NtfsTheme
import cn.skyrin.ntfs.ui.theme.Shapes

@Preview
@Composable
fun AppBarPreview() {
    NtfsTheme {
        MyTopAppBar(
            languageZh = true,
            onDarkThemeClick = {},
            onLanguageClick = {},
            onSettingsClick = {})
    }
}

@Composable
fun MyTopAppBar(
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