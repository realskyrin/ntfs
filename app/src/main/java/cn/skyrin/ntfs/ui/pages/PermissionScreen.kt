package cn.skyrin.ntfs.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.skyrin.ntfs.R
import cn.skyrin.ntfs.ui.theme.NtfsTheme

@Composable
fun PermissionScreen(onContinueClicked: () -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.need_permission_tips),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text(stringResource(R.string.get_access))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview() {
    NtfsTheme {
        PermissionScreen(onContinueClicked = {})
    }
}
