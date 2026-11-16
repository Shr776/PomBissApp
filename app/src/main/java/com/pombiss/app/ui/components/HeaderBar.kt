package com.pombiss.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onSearchClick: () -> Unit
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
            // بخش لوگو و اسم
            Row(verticalAlignment = Alignment.CenterVertically) {
                // لود کردن لوگوی اصلی پلاگین از گیت‌هاب
                AsyncImage(
                    model = "https://raw.githubusercontent.com/Shr776/PomBiss/main/picon/PomBiss_logo.png",
                    contentDescription = "PomBiss Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(40.dp)
                        .width(130.dp)
                )
            }

            // ساعت و تاریخ
            Column(horizontalAlignment = Alignment.End) {
                Text(text = currentTime, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextCyan)
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
                color = NeonGreen
            )

            Row {
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TextWhite)
                }
                IconButton(onClick = onRefreshClick) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextWhite)
                }
            }
        }
        
        HorizontalDivider(color = NeonBlue.copy(alpha = 0.3f), thickness = 1.dp)
    }
}
