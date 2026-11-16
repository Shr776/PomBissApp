package com.pombiss.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.data.Feed
import com.pombiss.app.ui.theme.*

@Composable
fun FeedCard(feed: Feed, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.8f)),
        border = BorderStroke(1.dp, NeonBlue.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // فرکانس، SR و پلاریزاسیون (بزرگ و برجسته)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = feed.freq,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonYellow
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "•", color = TextGray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = feed.sr,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "•", color = TextGray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = feed.pol,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonGreen
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // اسم فید
            Text(
                text = if (feed.feedId.isNotBlank()) feed.feedId else "Unknown Feed",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // فرمت و تاریخ (کوچک‌تر)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = feed.type,
                    fontSize = 14.sp,
                    color = NeonOrange,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = feed.datetime,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }
    }
}
