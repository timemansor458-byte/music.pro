package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleDark

@Composable
fun VisualizerBar(
  amplitudes: List<Float>,
  isPlaying: Boolean,
  modifier: Modifier = Modifier,
  barCount: Int = 16,
  height: Dp = 48.dp
) {
  val infiniteTransition = rememberInfiniteTransition(label = "visualizer_idle")
  val idleAnimation by infiniteTransition.animateFloat(
    initialValue = 0.2f,
    targetValue = 0.7f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "idle_anim"
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
      .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 0 until barCount) {
      val amp = if (isPlaying) {
        amplitudes.getOrElse(i) { 0.2f }
      } else {
        (idleAnimation * (0.3f + (i % 3) * 0.1f)).coerceIn(0.1f, 0.4f)
      }

      Box(
        modifier = Modifier
          .width(4.dp)
          .fillMaxHeight(fraction = amp.coerceIn(0.08f, 1.0f))
          .clip(RoundedCornerShape(4.dp))
          .background(
            Brush.verticalGradient(
              colors = listOf(VibrantPurple, VibrantPurpleDark)
            )
          )
      )
    }
  }
}
