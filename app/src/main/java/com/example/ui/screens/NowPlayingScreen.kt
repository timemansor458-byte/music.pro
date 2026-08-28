package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RepeatMode
import com.example.model.Song
import com.example.ui.components.SyncedLyricsView
import com.example.ui.components.VinylRecordArt
import com.example.ui.components.VisualizerBar
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurfaceVariant
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
  song: Song,
  isPlaying: Boolean,
  currentPositionMs: Long,
  visualizerAmplitudes: List<Float>,
  isShuffleEnabled: Boolean,
  repeatMode: RepeatMode,
  isLyricsActive: Boolean,
  isSleepTimerActive: Boolean,
  onTogglePlayPause: () -> Unit,
  onPlayNext: () -> Unit,
  onPlayPrevious: () -> Unit,
  onSeekTo: (Long) -> Unit,
  onToggleShuffle: () -> Unit,
  onCycleRepeatMode: () -> Unit,
  onToggleFavorite: () -> Unit,
  onToggleLyrics: () -> Unit,
  onOpenSleepTimer: () -> Unit,
  onOpenEqualizer: () -> Unit,
  onOpenAddToPlaylist: () -> Unit,
  onCollapse: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isSeeking by remember { mutableStateOf(false) }
  var seekPosition by remember { mutableFloatStateOf(0f) }
  var showMenu by remember { mutableStateOf(false) }

  val progressFraction = if (song.durationMs > 0) {
    if (isSeeking) seekPosition else (currentPositionMs.toFloat() / song.durationMs.toFloat()).coerceIn(0f, 1f)
  } else 0f

  val currentDisplayMs = if (isSeeking) (seekPosition * song.durationMs).toLong() else currentPositionMs

  fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .statusBarsPadding()
      .padding(horizontal = 24.dp, vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Header matching design mock:
    // header class='flex justify-between items-center px-6 pt-12 pb-4'
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onCollapse,
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(VibrantDarkSurfaceVariant)
          .testTag("collapse_now_playing_button")
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = "تصغير المشغل",
          tint = VibrantTextPrimary
        )
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "مشغل الآن",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          letterSpacing = 1.5.sp,
          color = VibrantPurple,
          style = MaterialTheme.typography.labelSmall
        )
        Text(
          text = song.album,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = VibrantTextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Box {
        IconButton(
          onClick = { showMenu = true },
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(VibrantDarkSurfaceVariant)
            .testTag("now_playing_menu_button")
        ) {
          Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "خيارات إضافية",
            tint = VibrantTextPrimary
          )
        }

        DropdownMenu(
          expanded = showMenu,
          onDismissRequest = { showMenu = false },
          modifier = Modifier.background(VibrantDarkActiveSurface)
        ) {
          DropdownMenuItem(
            text = { Text("إضافة إلى قائمة تشغيل", color = VibrantTextPrimary) },
            leadingIcon = { Icon(Icons.Default.PlaylistAdd, contentDescription = null, tint = VibrantPurple) },
            onClick = {
              showMenu = false
              onOpenAddToPlaylist()
            }
          )
          DropdownMenuItem(
            text = { Text("معادل الصوت (Equalizer)", color = VibrantTextPrimary) },
            leadingIcon = { Icon(Icons.Default.Equalizer, contentDescription = null, tint = VibrantPurple) },
            onClick = {
              showMenu = false
              onOpenEqualizer()
            }
          )
          DropdownMenuItem(
            text = { Text("مؤقت النوم", color = VibrantTextPrimary) },
            leadingIcon = { Icon(Icons.Default.Bedtime, contentDescription = null, tint = VibrantPurple) },
            onClick = {
              showMenu = false
              onOpenSleepTimer()
            }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Main Centerpiece: Vinyl Record Art OR Synced Lyrics
    AnimatedContent(
      targetState = isLyricsActive,
      transitionSpec = { fadeIn() togetherWith fadeOut() },
      label = "artwork_or_lyrics"
    ) { showLyrics ->
      if (showLyrics) {
        SyncedLyricsView(
          lyrics = song.lyrics,
          currentPositionMs = currentPositionMs,
          onSeekToLyric = onSeekTo,
          modifier = Modifier.padding(horizontal = 4.dp)
        )
      } else {
        VinylRecordArt(
          isPlaying = isPlaying,
          genreEmoji = when {
            song.genre.contains("طرب") -> "🎻"
            song.genre.contains("عود") -> "🪕"
            song.genre.contains("بوب") -> "⚡"
            song.genre.contains("هادئة") -> "🌙"
            else -> "♪"
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Song Title & Artist info
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = song.title,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.5).sp,
        color = VibrantTextPrimary,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = song.artist,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = VibrantTextSecondary,
        textAlign = TextAlign.Center
      )
    }

    // Audio Visualizer spectrum wave
    VisualizerBar(
      amplitudes = visualizerAmplitudes,
      isPlaying = isPlaying,
      modifier = Modifier.height(32.dp)
    )

    // Progress Bar & Duration numbers
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      Slider(
        value = progressFraction,
        onValueChange = {
          isSeeking = true
          seekPosition = it
        },
        onValueChangeFinished = {
          onSeekTo((seekPosition * song.durationMs).toLong())
          isSeeking = false
        },
        colors = SliderDefaults.colors(
          thumbColor = VibrantPurple,
          activeTrackColor = VibrantPurple,
          inactiveTrackColor = VibrantDarkSurfaceVariant
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("song_progress_slider")
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = formatTime(currentDisplayMs),
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          color = VibrantTextMuted
        )
        Text(
          text = song.formattedDuration,
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          color = VibrantTextMuted
        )
      }
    }

    // Playback Controls matching mockup:
    // flex items-center justify-between w-full px-4
    // ⇄ (Shuffle) | ⏮ (Prev) | ▶ (Play 80px) | ⏭ (Next) | ↺ (Repeat)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Shuffle
      IconButton(
        onClick = onToggleShuffle,
        modifier = Modifier.testTag("shuffle_button")
      ) {
        Icon(
          imageVector = Icons.Default.Shuffle,
          contentDescription = "خلط عشوائي",
          tint = if (isShuffleEnabled) VibrantPurple else VibrantTextSecondary,
          modifier = Modifier.size(26.dp)
        )
      }

      // Previous
      IconButton(
        onClick = onPlayPrevious,
        modifier = Modifier.testTag("previous_button")
      ) {
        Icon(
          imageVector = Icons.Default.SkipPrevious,
          contentDescription = "السابق",
          tint = Color.White,
          modifier = Modifier.size(34.dp)
        )
      }

      // Big Main Play / Pause button matching HTML:
      // w-20 h-20 rounded-full bg-[#D0BCFF] text-[#381E72] flex items-center justify-center text-4xl shadow-lg active:scale-95
      Box(
        modifier = Modifier
          .size(76.dp)
          .clip(CircleShape)
          .background(VibrantPurple)
          .shadow(elevation = 12.dp, shape = CircleShape)
          .clickable(onClick = onTogglePlayPause)
          .testTag("main_play_pause_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
          contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
          tint = VibrantPurpleContainer,
          modifier = Modifier.size(42.dp)
        )
      }

      // Next
      IconButton(
        onClick = onPlayNext,
        modifier = Modifier.testTag("next_button")
      ) {
        Icon(
          imageVector = Icons.Default.SkipNext,
          contentDescription = "التالي",
          tint = Color.White,
          modifier = Modifier.size(34.dp)
        )
      }

      // Repeat
      IconButton(
        onClick = onCycleRepeatMode,
        modifier = Modifier.testTag("repeat_button")
      ) {
        Icon(
          imageVector = when (repeatMode) {
            RepeatMode.ONE -> Icons.Default.RepeatOne
            else -> Icons.Default.Repeat
          },
          contentDescription = "تكرار",
          tint = if (repeatMode != RepeatMode.OFF) VibrantPurple else VibrantTextSecondary,
          modifier = Modifier.size(26.dp)
        )
      }
    }

    // Quick Bottom Feature Shortcuts (Favorite, Lyrics toggle, Equalizer, Timer)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(VibrantDarkActiveSurface.copy(alpha = 0.6f))
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onToggleFavorite, modifier = Modifier.testTag("favorite_toggle_button")) {
        Icon(
          imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
          contentDescription = "المفضلة",
          tint = if (song.isFavorite) VibrantAccentPink else VibrantTextMuted
        )
      }

      IconButton(onClick = onToggleLyrics, modifier = Modifier.testTag("lyrics_toggle_button")) {
        Icon(
          imageVector = Icons.Default.FormatQuote,
          contentDescription = "الكلمات",
          tint = if (isLyricsActive) VibrantPurple else VibrantTextMuted
        )
      }

      IconButton(onClick = onOpenEqualizer, modifier = Modifier.testTag("equalizer_shortcut_button")) {
        Icon(
          imageVector = Icons.Default.Equalizer,
          contentDescription = "معادل الصوت",
          tint = VibrantTextMuted
        )
      }

      IconButton(onClick = onOpenSleepTimer, modifier = Modifier.testTag("sleep_timer_shortcut_button")) {
        Icon(
          imageVector = Icons.Default.Bedtime,
          contentDescription = "مؤقت النوم",
          tint = if (isSleepTimerActive) VibrantPurple else VibrantTextMuted
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))
  }
}
