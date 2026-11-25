package com.pombiss.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
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
fun FeedDetailScreen(
    feed: Feed,
    onBack: () -> Unit,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current

    val logoResId = when {
        feed.title.contains("UEFA", ignoreCase = true) && !feed.title.contains("Europa", ignoreCase = true) && !feed.title.contains("Nations", ignoreCase = true) -> R.drawable.logo_uefa
        feed.title.contains("Europa League", ignoreCase = true) -> R.drawable.logo_europaleague
        feed.title.contains("Nations League", ignoreCase = true) -> R.drawable.logo_uefa 
        feed.title.contains("Premier League", ignoreCase = true) || feed.title.contains("England", ignoreCase = true) || feed.title.contains("English", ignoreCase = true) -> R.drawable.logo_premierleague
        feed.title.contains("La Liga", ignoreCase = true) || feed.title.contains("LaLiga", ignoreCase = true) || feed.title.contains("Spain", ignoreCase = true) || feed.title.contains("Spanish", ignoreCase = true) -> R.drawable.logo_laliga
        feed.title.contains("Serie A", ignoreCase = true) || feed.title.contains("Italy", ignoreCase = true) || feed.title.contains("Italian", ignoreCase = true) -> R.drawable.logo_seriea
        feed.title.contains("Bundesliga", ignoreCase = true) || feed.title.contains("German", ignoreCase = true) || feed.title.contains("Germany", ignoreCase = true) -> R.drawable.logo_bundesliga
        feed.title.contains("Ligue 1", ignoreCase = true) || feed.title.contains("France", ignoreCase = true) || feed.title.contains("French", ignoreCase = true) -> R.drawable.logo_ligue1
        feed.title.contains("World Cup", ignoreCase = true) || feed.title.contains("FIFA", ignoreCase = true) -> R.drawable.logo_worldcup
        feed.title.contains("Euro", ignoreCase = true) && !feed.title.contains("Europa", ignoreCase = true) -> R.drawable.logo_euro
        feed.title.contains("Copa", ignoreCase = true) || feed.title.contains("CONMEBOL", ignoreCase = true) || feed.title.contains("South America", ignoreCase = true) -> R.drawable.logo_copaamerica
        feed.title.contains("AFC", ignoreCase = true) || feed.title.contains("Asian", ignoreCase = true) || feed.title.contains("Asia", ignoreCase = true) -> R.drawable.logo_afc
        feed.title.contains("CAF", ignoreCase = true) || feed.title.contains("African", ignoreCase = true) || feed.title.contains("Africa", ignoreCase = true) -> R.drawable.logo_caf
        feed.title.contains("Saudi", ignoreCase = true) -> R.drawable.logo_saudileague
        feed.title.contains("Egypt", ignoreCase = true) -> R.drawable.logo_egyptleague
        feed.title.contains("Iran", ignoreCase = true) || feed.title.contains("Persian Gulf", ignoreCase = true) -> R.drawable.logo_iranleague
        feed.title.contains("Tennis", ignoreCase = true) -> R.drawable.logo_tennis
        feed.title.contains("Basketball", ignoreCase = true) -> R.drawable.logo_basketball
        feed.title.contains("Handbal", ignoreCase = true) -> R.drawable.logo_handball
        feed.title.contains("Vollyball", ignoreCase = true) || feed.title.contains("Volleyball", ignoreCase = true) -> R.drawable.logo_volleyball
        feed.title.contains("Darts", ignoreCase = true) -> R.drawable.logo_darts
        feed.title.contains("Figure Skating", ignoreCase = true) -> R.drawable.logo_figureskating
        feed.title.contains("Water Sport", ignoreCase = true) || feed.title.contains("Swimming", ignoreCase = true) -> R.drawable.logo_watersport
        feed.title.contains("Motorsport", ignoreCase = true) || feed.title.contains("Racing", ignoreCase = true) || feed.title.contains("Formula", ignoreCase = true) -> R.drawable.logo_motorsport
        feed.title.contains("Boxing", ignoreCase = true) -> R.drawable.logo_boxing
        feed.title.contains("NFL", ignoreCase = true) -> R.drawable.logo_nfl
        feed.title.contains("UFC", ignoreCase = true) -> R.drawable.logo_ufc
        feed.title.contains("Football", ignoreCase = true) -> R.drawable.logo_football
        else -> R.drawable.pombiss_logo 
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
                actions = {
                    IconButton(
                        onClick = onPrevious,
                        enabled = canGoPrevious
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Previous Feed",
                            tint = if (canGoPrevious) TextWhite else TextGray.copy(alpha = 0.35f)
                        )
                    }
                    IconButton(
                        onClick = onNext,
                        enabled = canGoNext
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Next Feed",
                            tint = if (canGoNext) TextWhite else TextGray.copy(alpha = 0.35f)
                        )
                    }
                    IconButton(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, feed.toShareString())
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Feed via..."))
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = TextWhite)
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
            Text(
                text = feed.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = feed.sat,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))

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

            Text(
                text = if (feed.feedId.isNotBlank()) feed.feedId else "No Feed ID",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = feed.type,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOrange
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                            val clip = ClipData.newPlainText("Feed Info", feed.toShareString())
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "کل اطلاعات کپی شد!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy All", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = feed.datetime, fontSize = 16.sp, color = TextGray)

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

            Image(
                painter = painterResource(id = logoResId),
                contentDescription = "Logo",
                modifier = Modifier.size(140.dp), 
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

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
