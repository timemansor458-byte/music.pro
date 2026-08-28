package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantNavPill
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun SleepTimerDialog(
  isActive: Boolean,
  secondsRemaining: Long?,
  onSetTimer: (Int) -> Unit,
  onCancelTimer: () -> Unit,
  onDismiss: () -> Unit
) {
  val options = listOf(
    15 to "15 دقيقة",
    30 to "30 دقيقة",
    45 to "45 دقيقة",
    60 to "ساعة واحدة",
    90 to "ساعة ونصف"
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .testTag("sleep_timer_dialog"),
      colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
      shape = RoundedCornerShape(28.dp)
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Bedtime,
              contentDescription = null,
              tint = VibrantPurple
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "مؤقت النوم",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = VibrantTextPrimary
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = VibrantTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isActive && secondsRemaining != null) {
          val minutes = secondsRemaining / 60
          val seconds = secondsRemaining % 60
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(VibrantDarkActiveSurface)
              .padding(16.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "الموسيقى ستتوقف بعد:",
                color = VibrantTextSecondary,
                style = MaterialTheme.typography.bodyMedium
              )
              Text(
                text = String.format("%02d:%02d", minutes, seconds),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantPurple
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = {
              onCancelTimer()
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFCF6679),
              contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("إلغاء المؤقت", fontWeight = FontWeight.Bold)
          }
        } else {
          Text(
            text = "اختر المدة التي ترغب بعدها بإيقاف تشغيل الموسيقى تلقائياً:",
            color = VibrantTextSecondary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(16.dp))

          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            options.forEach { (mins, label) ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(14.dp))
                  .background(VibrantDarkActiveSurface)
                  .clickable {
                    onSetTimer(mins)
                    onDismiss()
                  }
                  .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = label, color = VibrantTextPrimary, fontWeight = FontWeight.Medium)
                Icon(Icons.Default.Timer, contentDescription = null, tint = VibrantPurple)
              }
            }
          }
        }
      }
    }
  }
}
