package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.SpatialAudioOff
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EarbudsState
import com.example.model.NoiseControlMode
import com.example.ui.theme.VibrantAccentAmber
import com.example.ui.theme.VibrantAccentCyan
import com.example.ui.theme.VibrantAccentGreen
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantDarkSurfaceVariant
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun AirPodsPopupCard(
  earbudsState: EarbudsState,
  onDismiss: () -> Unit,
  onToggleConnection: () -> Unit,
  onSetNoiseControl: (NoiseControlMode) -> Unit,
  onToggleSpatialAudio: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.96f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(Color.Black.copy(alpha = 0.65f))
      .clickable(onClick = onDismiss)
      .padding(16.dp),
    contentAlignment = Alignment.BottomCenter
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(enabled = false) {}
        .shadow(28.dp, RoundedCornerShape(32.dp))
        .clip(RoundedCornerShape(32.dp))
        .border(1.dp, VibrantPurple.copy(alpha = 0.35f), RoundedCornerShape(32.dp))
        .testTag("airpods_ios_popup_card"),
      colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
      shape = RoundedCornerShape(32.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(
                VibrantDarkActiveSurface,
                VibrantDarkSurface
              )
            )
          )
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top Header with Close and Connection Status
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (earbudsState.isConnected) VibrantAccentGreen else VibrantAccentAmber)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (earbudsState.isConnected) "متصلة بالبلوتوث" else "تم فتح العلبة",
              color = VibrantAccentGreen,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(VibrantDarkSurfaceVariant)
              .testTag("close_airpods_popup_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = VibrantTextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Device Title
        Text(
          text = earbudsState.deviceName,
          color = VibrantTextPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "مؤشرات الشحن اللحظية والتحكم بالصوت المكاني",
          color = VibrantTextMuted,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3D Visual Representation of Earbuds and Case
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Left Earbud
          EarbudBatteryGauge(
            label = "اليسار (L)",
            emoji = "🪄",
            batteryPercent = earbudsState.leftBattery,
            isCharging = earbudsState.isLeftCharging,
            testTag = "battery_gauge_left"
          )

          // Center: Charging Case (علبة السماعات)
          CaseBatteryGauge(
            label = "علبة الشحن",
            batteryPercent = earbudsState.caseBattery,
            isCharging = earbudsState.isCaseCharging,
            pulseScale = pulseScale,
            testTag = "battery_gauge_case"
          )

          // Right Earbud
          EarbudBatteryGauge(
            label = "اليمين (R)",
            emoji = "🪄",
            batteryPercent = earbudsState.rightBattery,
            isCharging = earbudsState.isRightCharging,
            testTag = "battery_gauge_right"
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Noise Control Mode (إلغاء الضوضاء - شفافية الصوت - متوقف)
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(VibrantDarkBackground)
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Hearing,
                contentDescription = null,
                tint = VibrantPurple,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "التحكم بالضجيج (Noise Control)",
                color = VibrantTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            Text(
              text = earbudsState.noiseControlMode.labelAr,
              color = VibrantPurple,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Segmented Control
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(VibrantDarkActiveSurface)
              .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            NoiseControlMode.values().forEach { mode ->
              val isSelected = earbudsState.noiseControlMode == mode
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) VibrantPurple else Color.Transparent)
                  .clickable { onSetNoiseControl(mode) }
                  .padding(vertical = 8.dp)
                  .testTag("noise_mode_${mode.name}"),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = mode.labelAr,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) VibrantDarkBackground else VibrantTextMuted
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Spatial Audio & Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Spatial Audio Toggle
          Surface(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = onToggleSpatialAudio)
              .testTag("toggle_spatial_audio_btn"),
            color = if (earbudsState.isSpatialAudioActive) VibrantPurpleContainer else VibrantDarkBackground
          ) {
            Row(
              modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = if (earbudsState.isSpatialAudioActive) Icons.Default.SurroundSound else Icons.Default.SpatialAudioOff,
                contentDescription = null,
                tint = if (earbudsState.isSpatialAudioActive) VibrantPurple else VibrantTextMuted,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (earbudsState.isSpatialAudioActive) "الصوت المكاني: مفعّل" else "الصوت المكاني: متوقف",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (earbudsState.isSpatialAudioActive) VibrantPurple else VibrantTextMuted
              )
            }
          }

          // Done Button
          Button(
            onClick = onDismiss,
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("airpods_done_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple)
          ) {
            Text(
              text = "تم",
              color = VibrantDarkBackground,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun EarbudBatteryGauge(
  label: String,
  emoji: String,
  batteryPercent: Int,
  isCharging: Boolean,
  testTag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.testTag(testTag)
  ) {
    Box(
      modifier = Modifier
        .size(68.dp)
        .clip(CircleShape)
        .background(
          Brush.radialGradient(
            listOf(
              VibrantPurpleContainer.copy(alpha = 0.7f),
              VibrantDarkSurfaceVariant
            )
          )
        )
        .border(1.5.dp, VibrantPurple.copy(alpha = 0.4f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "🎧",
        fontSize = 28.sp
      )

      if (isCharging) {
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .size(20.dp)
            .clip(CircleShape)
            .background(VibrantAccentGreen),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "جاري الشحن",
            tint = VibrantDarkBackground,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "$batteryPercent%",
      color = if (batteryPercent > 20) VibrantAccentGreen else VibrantAccentPink,
      fontWeight = FontWeight.Bold,
      fontSize = 15.sp
    )

    Text(
      text = label,
      color = VibrantTextMuted,
      fontSize = 11.sp
    )
  }
}

@Composable
private fun CaseBatteryGauge(
  label: String,
  batteryPercent: Int,
  isCharging: Boolean,
  pulseScale: Float,
  testTag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.testTag(testTag)
  ) {
    Box(
      modifier = Modifier
        .scale(pulseScale)
        .size(78.dp)
        .clip(RoundedCornerShape(22.dp))
        .background(
          Brush.verticalGradient(
            listOf(
              Color(0xFF2C2D35),
              Color(0xFF1B1B22)
            )
          )
        )
        .border(2.dp, VibrantAccentCyan.copy(alpha = 0.5f), RoundedCornerShape(22.dp)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "🔋",
        fontSize = 32.sp
      )

      if (isCharging) {
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .size(22.dp)
            .clip(CircleShape)
            .background(VibrantAccentGreen),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "شحن العلبة",
            tint = VibrantDarkBackground,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "$batteryPercent%",
      color = VibrantAccentCyan,
      fontWeight = FontWeight.Bold,
      fontSize = 15.sp
    )

    Text(
      text = label,
      color = VibrantTextMuted,
      fontSize = 11.sp
    )
  }
}
