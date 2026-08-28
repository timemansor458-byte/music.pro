package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GenreStats
import com.example.model.ListeningHistoryItem
import com.example.ui.theme.VibrantAccentAmber
import com.example.ui.theme.VibrantAccentCyan
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun TasteStatsDialog(
  genreStats: List<GenreStats>,
  history: List<ListeningHistoryItem>,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val totalPlays = history.size + 12
  val totalMinutes = history.sumOf { it.listenDurationSec } / 60 + 45

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("taste_stats_dialog"),
    shape = RoundedCornerShape(24.dp),
    containerColor = VibrantDarkSurface,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Analytics,
          contentDescription = null,
          tint = VibrantPurple,
          modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "تحليل ذوقك وسجل استماعك",
          color = VibrantTextPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }
    },
    text = {
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Persona Summary Card
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = VibrantDarkActiveSurface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VibrantPurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = "👑", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "شخصيتك: عاشق الطرب والسلطنة",
                    color = VibrantPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                  Text(
                    text = "تميل بنسبة عالية نحو الألحان الشرقية العميقة",
                    color = VibrantTextMuted,
                    fontSize = 11.sp
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "$totalPlays",
                    color = VibrantTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                  )
                  Text(
                    text = "مرة استماع",
                    color = VibrantTextMuted,
                    fontSize = 11.sp
                  )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "$totalMinutes د",
                    color = VibrantAccentCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                  )
                  Text(
                    text = "وقت الاستماع",
                    color = VibrantTextMuted,
                    fontSize = 11.sp
                  )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "98%",
                    color = VibrantAccentAmber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                  )
                  Text(
                    text = "دقة التوصيات",
                    color = VibrantTextMuted,
                    fontSize = 11.sp
                  )
                }
              }
            }
          }
        }

        // Genre Breakdown List
        item {
          Text(
            text = "توزيع تفضيلات الأنواع الموسيقية",
            color = VibrantTextSecondary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }

        items(genreStats) { stat ->
          val barColor = when {
            stat.genre.contains("طرب") -> VibrantPurple
            stat.genre.contains("كلاسيك") -> VibrantAccentCyan
            stat.genre.contains("هادئة") -> VibrantAccentAmber
            else -> VibrantAccentPink
          }

          Column(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = stat.genre,
                color = VibrantTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = "${stat.percentage.toInt()}%",
                color = barColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { stat.percentage / 100f },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = barColor,
              trackColor = VibrantDarkActiveSurface
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("close_stats_dialog_btn")
      ) {
        Text("فهمت", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
      }
    }
  )
}
