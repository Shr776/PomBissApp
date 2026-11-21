package com.pombiss.app.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pombiss.app.data.LicenseManager
import com.pombiss.app.ui.theme.*
import java.io.File
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
    
    val username = LicenseManager.getUsername(context)
    val countryCode = LicenseManager.getCountryCode(context)

    // لود کردن عکس پروفایل از حافظه
    val profilePic = remember {
        val file = File(context.filesDir, "profile_pic.jpg")
        if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
        } else null
    }

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
            // سمت چپ: پروفایل کاربر (عکس + اسم + کد کشور)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // نمایش عکس پروفایل
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (profilePic != null) {
                        Image(
                            bitmap = profilePic,
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = username.take(1).uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "@$username",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = countryCode,
                        fontSize = 10.sp,
                        color = TextGray
                    )
                }
            }

            // وسط: لوگوی برنامه
            AsyncImage(
                model = "https://raw.githubusercontent.com/Shr776/PomBiss/main/picon/PomBiss_logo.png",
                contentDescription = "PomBiss Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
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
