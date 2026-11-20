package com.pombiss.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pombiss.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HeaderBar(
    feedCount: Int,
    onRefreshClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isFavoriteView: Boolean,
    onToggleFavoriteView: () -> Unit
) {
    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://raw.githubusercontent.com/Shr776/PomBiss/main/picon/PomBiss_logo.png",
                contentDescription = "PomBiss Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(40.dp)
                    .width(130.dp)
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currentTime, 
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.primary 
                )
                Text(text = currentDate, fontSize = 12.sp, color = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$feedCount feeds",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary 
            )

            Row {
                // دکمه ستاره (فیووریت‌ها)
                IconButton(onClick = onToggleFavoriteView) {
                    if (isFavoriteView) {
                        Icon(Icons.Filled.Star, contentDescription = "Show All", tint = Color(0xFFFFD700))
                    } else {
                        Icon(Icons.Outlined.StarBorder, contentDescription = "Show Favorites", tint = TextWhite)
                    }
                }
                
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TextWhite)
                }
                IconButton(onClick = onRefreshClick) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextWhite)
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextWhite)
                }
            }
        }
        
        HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), thickness = 1.dp)
    }
}
