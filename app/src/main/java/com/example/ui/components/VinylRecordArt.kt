package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantPurpleDark

@Composable
fun VinylRecordArt(
  isPlaying: Boolean,
  genreEmoji: String = "♪",
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "vinyl_rotation")
  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 12000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotation_deg"
  )

  val currentRotation = if (isPlaying) rotation else 0f

  // Outer container matching the HTML mockup:
  // w-full aspect-square max-w-[320px] rounded-[40px] overflow-hidden shadow-2xl ring-1 ring-white/10
  Box(
    modifier = modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .clip(RoundedCornerShape(40.dp))
      .background(VibrantDarkSurface)
      .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(40.dp))
      .shadow(elevation = 24.dp, shape = RoundedCornerShape(40.dp))
      .testTag("album_art_container"),
    contentAlignment = Alignment.Center
  ) {
    // Gradient overlay backdrop
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.linearGradient(
            colors = listOf(
              VibrantPurpleDark.copy(alpha = 0.45f),
              VibrantPurple.copy(alpha = 0.25f),
              Color.Transparent
            ),
            start = Offset(0f, 0f),
            end = Offset(800f, 800f)
          )
        )
    )

    // Decorative vinyl concentric circles
    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
      val center = Offset(size.width / 2f, size.height / 2f)
      val maxRadius = size.width * 0.44f

      // Vinyl groove rings
      for (r in listOf(0.95f, 0.85f, 0.75f, 0.65f)) {
        drawCircle(
          color = Color.White.copy(alpha = 0.05f),
          radius = maxRadius * r,
          center = center,
          style = Stroke(width = 1.5f)
        )
      }
    }

    // Center Vinyl center disc with rotation
    Box(
      modifier = Modifier
        .size(164.dp)
        .rotate(currentRotation)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(
            colors = listOf(VibrantPurple, VibrantPurpleContainer),
            start = Offset(0f, 0f),
            end = Offset(300f, 300f)
          )
        )
        .border(8.dp, VibrantDarkBackground, CircleShape)
        .testTag("rotating_vinyl_record"),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = genreEmoji,
        fontSize = 62.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White.copy(alpha = 0.95f)
      )
    }
  }
}
