package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EqualizerState
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun EqualizerScreen(
  equalizerState: EqualizerState,
  onToggleEnabled: (Boolean) -> Unit,
  onSelectPreset: (String) -> Unit,
  onUpdateBand: (Int, Float) -> Unit,
  onUpdateBassBoost: (Float) -> Unit,
  onUpdateVirtualizer: (Float) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .statusBarsPadding()
      .verticalScroll(scrollState)
      .padding(horizontal = 20.dp)
      .padding(bottom = 120.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header with On/Off Switch
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "معادل الصوت وتأثيرات الاستماع",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = VibrantTextPrimary
        )
        Text(
          text = "Equalizer & Studio Audio FX",
          style = MaterialTheme.typography.bodySmall,
          color = VibrantTextMuted
        )
      }

      Switch(
        checked = equalizerState.isEnabled,
        onCheckedChange = onToggleEnabled,
        colors = SwitchDefaults.colors(
          checkedThumbColor = VibrantPurpleContainer,
          checkedTrackColor = VibrantPurple,
          uncheckedThumbColor = VibrantTextMuted,
          uncheckedTrackColor = VibrantDarkSurface
        ),
        modifier = Modifier.testTag("equalizer_power_switch")
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Presets Row
    Text(
      text = "الأوضاع المسبقة (Presets):",
      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
      color = VibrantTextPrimary
    )

    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(EqualizerState.presets.keys.toList()) { presetName ->
        val isSelected = equalizerState.selectedPreset == presetName
        FilterChip(
          selected = isSelected,
          onClick = { onSelectPreset(presetName) },
          label = { Text(presetName, fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = VibrantPurple,
            selectedLabelColor = VibrantPurpleContainer,
            containerColor = VibrantDarkSurface,
            labelColor = VibrantTextSecondary
          ),
          border = null,
          shape = RoundedCornerShape(16.dp),
          enabled = equalizerState.isEnabled,
          modifier = Modifier.testTag("preset_$presetName")
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // 5-Band Graphic Sliders
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
      shape = RoundedCornerShape(24.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = VibrantPurple)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "حزم التردد (5-Band Frequency)",
              fontWeight = FontWeight.Bold,
              color = VibrantTextPrimary
            )
          }
          Text(
            text = equalizerState.selectedPreset,
            style = MaterialTheme.typography.bodySmall,
            color = VibrantPurple
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        equalizerState.bands.forEachIndexed { index, band ->
          Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = band.label, color = VibrantTextSecondary, fontSize = 13.sp)
              Text(
                text = "${if (band.gainDb > 0) "+" else ""}${band.gainDb.toInt()} dB",
                color = VibrantPurple,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Slider(
              value = band.gainDb,
              onValueChange = { onUpdateBand(index, it) },
              valueRange = -10f..10f,
              steps = 19,
              enabled = equalizerState.isEnabled,
              colors = SliderDefaults.colors(
                thumbColor = VibrantPurple,
                activeTrackColor = VibrantPurple,
                inactiveTrackColor = VibrantDarkActiveSurface
              ),
              modifier = Modifier.testTag("band_slider_$index")
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Bass Boost & Virtualizer 3D Controls
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
      shape = RoundedCornerShape(24.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        // Bass Boost
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(VibrantPurpleContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "مضخم الصوت الجهير (Bass Boost)", color = VibrantTextPrimary, fontWeight = FontWeight.Medium)
              Text(text = "${equalizerState.bassBoost.toInt()}%", color = VibrantPurple, fontWeight = FontWeight.Bold)
            }
            Slider(
              value = equalizerState.bassBoost,
              onValueChange = onUpdateBassBoost,
              valueRange = 0f..100f,
              enabled = equalizerState.isEnabled,
              colors = SliderDefaults.colors(
                thumbColor = VibrantPurple,
                activeTrackColor = VibrantPurple,
                inactiveTrackColor = VibrantDarkActiveSurface
              ),
              modifier = Modifier.testTag("bass_boost_slider")
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Virtualizer 3D
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(VibrantPurpleContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.SurroundSound, contentDescription = null, tint = VibrantPurple, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "الصوت المحيطي ثلاثي الأبعاد (3D Virtualizer)", color = VibrantTextPrimary, fontWeight = FontWeight.Medium)
              Text(text = "${equalizerState.virtualizer3d.toInt()}%", color = VibrantPurple, fontWeight = FontWeight.Bold)
            }
            Slider(
              value = equalizerState.virtualizer3d,
              onValueChange = onUpdateVirtualizer,
              valueRange = 0f..100f,
              enabled = equalizerState.isEnabled,
              colors = SliderDefaults.colors(
                thumbColor = VibrantPurple,
                activeTrackColor = VibrantPurple,
                inactiveTrackColor = VibrantDarkActiveSurface
              ),
              modifier = Modifier.testTag("virtualizer_slider")
            )
          }
        }
      }
    }
  }
}
