package com.pombiss.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.R
import com.pombiss.app.data.Feed
import com.pombiss.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedDetailScreen(feed: Feed, onBack: () -> Unit) {
    val context = LocalContext.current

    // تشخیص هوشمند لوگو
    val logoResId = when {
        feed.title.contains("UEFA", ignoreCase = true) && !feed.title.contains("Europa", ignoreCase = true) && !feed.title.contains("Nations", ignoreCase = true) -> R.drawable.logo_uefa
        feed.title.contains("Europa League", ignoreCase = true) -> R.drawable.logo_europaleague
        feed.title.contains("Nations League", ignoreCase = true) -> R.drawable.logo_uefa 
        
        feed.title.contains("Premier League", ignoreCase = true) -> R.drawable.logo_premierleague
        feed.title.contains("La Liga", ignoreCase = true) || feed.title.contains("LaLiga", ignoreCase = true) -> R.drawable.logo_laliga
        feed.title.contains("Serie A", ignoreCase = true) -> R.drawable.logo_seriea
        feed.title.contains("Bundesliga", ignoreCase = true) -> R.drawable.logo_bundesliga
        feed.title.contains("Ligue 1", ignoreCase = true) -> R.drawable.logo_ligue1
        
        feed.title.contains("World Cup", ignoreCase = true) || feed.title.contains("FIFA", ignoreCase = true) -> R.drawable.logo_worldcup
        feed.title.contains("Euro", ignoreCase = true) && !feed.title.contains("Europa", ignoreCase = true) -> R.drawable.logo_euro
        feed.title.contains("Copa", ignoreCase = true) || feed.title.contains("CONMEBOL", ignoreCase = true) -> R.drawable.logo_copaamerica
        
        feed.title.contains("AFC", ignoreCase = true) || feed.title.contains("Asian", ignoreCase = true) -> R.drawable.logo_afc
        feed.title.contains("CAF", ignoreCase = true) || feed.title.contains("African", ignoreCase = true) -> R.drawable.logo_caf
        feed.title.contains("Saudi", ignoreCase = true) -> R.drawable.logo_saudileague
        feed.title.contains("Egypt", ignoreCase = true) -> R.drawable.logo_egyptleague
        feed.title.contains("Iran", ignoreCase = true) -> R.drawable.logo_iranleague

        feed.title.contains("Tennis", ignoreCase = true) -> R.drawable.logo_tennis
        feed.title.contains("Basketball", ignoreCase = true) -> R.drawable.logo_basketball
        feed.title.contains("Handbal", ignoreCase = true) -> R.drawable.logo_handball
        feed.title.contains("Vollyball", ignoreCase = true) || feed.title.contains("Volleyball", ignoreCase = true) -> R.drawable.logo_volleyball
        feed.title.contains("Darts", ignoreCase = true) -> R.drawable.logo_darts
        feed.title.contains("Figure Skating", ignoreCase = true) -> R.drawable.logo_figureskating
        feed.title.contains("Water Sport", ignoreCase = true) -> R.drawable.logo_watersport
        feed.title.contains("Motorsport", ignoreCase = true) -> R.drawable.logo_motorsport
        feed.title.contains("Boxing", ignoreCase = true) -> R.drawable.logo_boxing
        feed.title.contains("NFL", ignoreCase = true) -> R.drawable.logo_nfl
        feed.title.contains("UFC", ignoreCase = true) -> R.drawable.logo_ufc
        feed.title.contains("Football", ignoreCase = true) -> R.drawable.logo_football
        else -> null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feed Details", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ۱. عنوان فید
            Text(
                text = feed.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // ۲. نام ماهواره
            Text(
                text = feed.sat,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // ۳. مشخصات فرکانس
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(text = feed.freq, fontSize = 34.sp, fontWeight = FontWeight.Black, color = NeonYellow)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "•", fontSize = 34.sp, color = TextGray)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = feed.sr, fontSize = 30.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "•", fontSize = 34.sp, color = TextGray)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = feed.pol, fontSize = 30.sp, fontWeight = FontWeight.Black, color = NeonGreen)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ۴. اسم فید
            Text(
                text = if (feed.feedId.isNotBlank()) feed.feedId else "No Feed ID",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ۵. فرمت
            Text(
                text = feed.type,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOrange
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ۶. کد فید و دکمه کپی
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CW: ${feed.cw}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPink
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("CW", feed.cw)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "کد کپی شد!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy CW", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ۷. تاریخ
            Text(text = feed.datetime, fontSize = 16.sp, color = TextGray)

            // ۸. نمایش اطلاعات اضافی (Event/Teams)
            // اگر فیلد event خالی نبود، اون رو نشون بده
            if (feed.event.isNotBlank()) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Match Info",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = feed.event,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ۹. لوگو
            if (logoResId != null) {
                Image(
                    painter = painterResource(id = logoResId),
                    contentDescription = "Sport Logo",
                    modifier = Modifier.size(100.dp), 
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // فوتر تبلیغاتی
            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = "Developed by @Shr776", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "Channel: @VuSolo | Support: @PomSat", color = TextGray, fontSize = 12.sp)
            Text(text = "Feeds: @PomFeed | @FeedHunter", color = TextGray, fontSize = 12.sp)
            Text(text = "YouTube: @VUSOLO | GitHub: Shr776 / PomBiss", color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
