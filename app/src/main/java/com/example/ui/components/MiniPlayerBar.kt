package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Song
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun MiniPlayerBar(
  song: Song?,
  isPlaying: Boolean,
  currentPositionMs: Long,
  onTogglePlayPause: () -> Unit,
  onPlayNext: () -> Unit,
  onToggleFavorite: (String) -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (song == null) return

  val progress = if (song.durationMs > 0) {
    (currentPositionMs.toFloat() / song.durationMs.toFloat()).coerceIn(0f, 1f)
  } else 0f

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(20.dp))
      .clickable(onClick = onClick)
      .testTag("mini_player_bar"),
    color = VibrantDarkSurface,
    tonalElevation = 8.dp,
    shadowElevation = 10.dp,
    shape = RoundedCornerShape(20.dp)
  ) {
    Column {
      // Progress line at very top
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(3.dp),
        color = VibrantPurple,
        trackColor = Color.White.copy(alpha = 0.1f)
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Mini Vinyl / Disc icon
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(VibrantPurpleContainer),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "♪", fontSize = 20.sp, color = VibrantPurple)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Song Info
        Column(
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = song.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            ),
            color = VibrantTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "${song.artist} • ${song.genre}",
            style = MaterialTheme.typography.bodySmall,
            color = VibrantTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        // Action Buttons
        IconButton(
          onClick = { onToggleFavorite(song.id) },
          modifier = Modifier.testTag("mini_fav_button")
        ) {
          Icon(
            imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "المفضلة",
            tint = if (song.isFavorite) VibrantAccentPink else VibrantTextMuted
          )
        }

        IconButton(
          onClick = onTogglePlayPause,
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(VibrantPurple)
            .testTag("mini_play_pause_button")
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
            tint = VibrantPurpleContainer,
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
          onClick = onPlayNext,
          modifier = Modifier.testTag("mini_skip_button")
        ) {
          Icon(
            imageVector = Icons.Default.SkipNext,
            contentDescription = "التالي",
            tint = VibrantTextPrimary
          )
        }
      }
    }
  }
}
