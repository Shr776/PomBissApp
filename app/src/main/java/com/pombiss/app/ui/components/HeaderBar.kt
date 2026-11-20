package com.pombiss.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pombiss.app.data.LicenseManager
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
    val context = LocalContext.current
    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    
    // گرفتن اسم کاربر و کد کشور از حافظه
    val username = LicenseManager.getUsername(context)
    val countryCode = LicenseManager.getCountryCode(context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // ردیف اول: اسم کاربر (چپ)، لوگو (وسط)، ساعت (راست)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // سمت چپ: باکس پروفایل کاربر
            Column(
                modifier = Modifier
                    .background(SurfaceDark, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "@$username",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = countryCode, // نمایش کد کشور (مثلا IR)
                    fontSize = 11.sp,
                    color = TextGray
                )
            }

            // وسط: لوگوی برنامه
            AsyncImage(
                model = "https://raw.githubusercontent.com/Shr776/PomBiss/main/picon/PomBiss_logo.png",
                contentDescription = "PomBiss Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(40.dp)
                    .width(110.dp)
            )

            // سمت راست: ساعت و تاریخ
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currentTime, 
                    fontSize = 20.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.primary 
                )
                Text(text = currentDate, fontSize = 10.sp, color = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ردیف دوم: تعداد فیدها و دکمه‌ها
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
