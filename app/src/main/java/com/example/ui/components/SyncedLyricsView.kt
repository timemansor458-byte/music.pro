package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LyricLine
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary

@Composable
fun SyncedLyricsView(
  lyrics: List<LyricLine>,
  currentPositionMs: Long,
  onSeekToLyric: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  if (lyrics.isEmpty()) {
    Box(
      modifier = modifier
        .fillMaxWidth()
        .height(300.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(VibrantDarkSurface.copy(alpha = 0.7f))
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "كلمات الأغنية غير متوفرة لهذا المقطع",
        style = MaterialTheme.typography.bodyLarge,
        color = VibrantTextMuted
      )
    }
    return
  }

  val activeIndex = lyrics.indexOfLast { currentPositionMs >= it.timeMs }.coerceAtLeast(0)
  val listState = rememberLazyListState()

  LaunchedEffect(activeIndex) {
    if (activeIndex in lyrics.indices) {
      listState.animateScrollToItem((activeIndex - 1).coerceAtLeast(0))
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(320.dp)
      .clip(RoundedCornerShape(32.dp))
      .background(VibrantDarkSurface.copy(alpha = 0.85f))
      .padding(horizontal = 20.dp, vertical = 16.dp)
      .testTag("synced_lyrics_view")
  ) {
    LazyColumn(
      state = listState,
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      item { Spacer(modifier = Modifier.height(32.dp)) }

      itemsIndexed(lyrics) { index, line ->
        val isActive = index == activeIndex
        val textColor by animateColorAsState(
          targetValue = if (isActive) VibrantPurple else VibrantTextMuted.copy(alpha = 0.5f),
          animationSpec = tween(300),
          label = "lyric_color"
        )
        val fontSize = if (isActive) 20.sp else 16.sp
        val fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSeekToLyric(line.timeMs) }
            .padding(vertical = 10.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = line.text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
          )
        }
      }

      item { Spacer(modifier = Modifier.height(64.dp)) }
    }

    // Top fade overlay
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(36.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.verticalGradient(
            colors = listOf(VibrantDarkSurface, Color.Transparent)
          )
        )
    )

    // Bottom fade overlay
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(36.dp)
        .align(Alignment.BottomCenter)
        .background(
          Brush.verticalGradient(
            colors = listOf(Color.Transparent, VibrantDarkSurface)
          )
        )
    )
  }
}
