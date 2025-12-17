package eu.kanade.presentation.more

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.R

@Composable
fun LogoHeader() {

    var darkThemeSelected = isSystemInDarkTheme() || (getDefaultNightMode() == MODE_NIGHT_YES)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(if(darkThemeSelected) R.drawable.ic_gihon_white else R.drawable.ic_gihon),
            contentDescription = null,
            //tint = MaterialTheme.colorScheme.onSurface,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(vertical = 56.dp)
                .size(64.dp),
        )

        HorizontalDivider()
    }
}
