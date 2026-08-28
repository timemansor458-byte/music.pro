package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.FiberSmartRecord
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DownloadStatus
import com.example.model.DownloadTask
import com.example.model.Song
import com.example.model.YouTubeMusicVideo
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloaderScreen(
  videos: List<YouTubeMusicVideo>,
  downloadTasks: List<DownloadTask>,
  downloadedSongs: List<Song>,
  currentPlayingSong: Song?,
  isPlaying: Boolean,
  onSearchQueryChange: (String) -> Unit,
  onCategorySelect: (String) -> Unit,
  onStartDownload: (YouTubeMusicVideo) -> Unit,
  onPlaySong: (Song) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("الكل") }
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Explore YouTube, 1: Download Tasks

  val categories = listOf("الكل", "تريند اليوم 🔥", "طرب وسلطنة 🎻", "كلاسيكيات 📻", "موسيقى هادئة 🌙", "بوب وموسيقى 🎧", "خليجي راقي 🌟")

  val filteredVideos = videos.filter { video ->
    val matchesQuery = searchQuery.isBlank() ||
      video.title.contains(searchQuery, ignoreCase = true) ||
      video.channelTitle.contains(searchQuery, ignoreCase = true)

    val matchesCategory = selectedCategory == "الكل" || video.category.contains(selectedCategory.take(4))
    matchesQuery && matchesCategory
  }

  val activeTasks = downloadTasks.filter { it.status != DownloadStatus.COMPLETED }
  val completedTasks = downloadTasks.filter { it.status == DownloadStatus.COMPLETED }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .padding(horizontal = 16.dp)
      .testTag("downloader_screen")
  ) {
    Spacer(modifier = Modifier.height(10.dp))

    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFFF0000)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "تنزيل الأغاني (MP3)",
            color = VibrantTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
          )
        }
        Text(
          text = "استكشف يوتيوب وحمّل الصوت بصيغة MP3 حصراً",
          color = VibrantTextMuted,
          fontSize = 12.sp
        )
      }

      // MP3 Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(10.dp))
          .background(VibrantPurpleContainer)
          .padding(horizontal = 10.dp, vertical = 5.dp)
      ) {
        Text(
          text = "صوت MP3 فقط",
          color = VibrantPurple,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Navigation Tabs: Explore vs Downloads Queue
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = VibrantDarkSurface,
      contentColor = VibrantPurple,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = VibrantPurple
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.SmartDisplay, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("استكشاف يوتيوب", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        },
        selectedContentColor = VibrantPurple,
        unselectedContentColor = VibrantTextMuted
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Downloading, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("قائمة التنزيلات (${downloadTasks.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        },
        selectedContentColor = VibrantPurple,
        unselectedContentColor = VibrantTextMuted
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (selectedTab == 0) {
      // YouTube Exploration & Search
      OutlinedTextField(
        value = searchQuery,
        onValueChange = {
          searchQuery = it
          onSearchQueryChange(it)
        },
        placeholder = { Text("ابحث في يوتيوب عن أغنية، فنان، أو رابط...", color = VibrantTextMuted, fontSize = 13.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = VibrantPurple)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = ""; onSearchQueryChange("") }) {
              Icon(Icons.Default.Close, contentDescription = "مسح", tint = VibrantTextMuted)
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = VibrantDarkSurface,
          unfocusedContainerColor = VibrantDarkSurface,
          focusedBorderColor = VibrantPurple,
          unfocusedBorderColor = VibrantDarkSurfaceVariant,
          focusedTextColor = VibrantTextPrimary,
          unfocusedTextColor = VibrantTextPrimary
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("youtube_search_input")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Category Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(categories) { category ->
          val isSelected = selectedCategory == category
          FilterChip(
            selected = isSelected,
            onClick = {
              selectedCategory = category
              onCategorySelect(category)
            },
            label = {
              Text(
                text = category,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) VibrantDarkBackground else VibrantTextPrimary
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = VibrantPurple,
              containerColor = VibrantDarkSurface
            ),
            border = null,
            shape = RoundedCornerShape(10.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Active Downloads Mini Bar (if any downloading)
      if (activeTasks.isNotEmpty()) {
        val currentActive = activeTasks.first()
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = VibrantPurpleContainer),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedTab = 1 }
            .testTag("active_download_banner")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(VibrantPurple),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Downloading, contentDescription = null, tint = VibrantDarkBackground, modifier = Modifier.size(18.dp))
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "جاري تحميل: ${currentActive.video.title}",
                  color = VibrantPurple,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  maxLines = 1
                )
                Text(
                  text = "تحويل إلى MP3 (${(currentActive.progress * 100).toInt()}%)",
                  color = VibrantTextMuted,
                  fontSize = 10.sp
                )
              }
            }
            Text("عرض التفاصيل", color = VibrantPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      // YouTube Videos List
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(filteredVideos) { video ->
          val isAlreadyDownloaded = downloadTasks.any { it.video.id == video.id && it.status == DownloadStatus.COMPLETED }

          YouTubeVideoCard(
            video = video,
            isDownloaded = isAlreadyDownloaded,
            onDownloadClick = { onStartDownload(video) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    } else {
      // Downloads Queue & Completed MP3s
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (downloadTasks.isEmpty()) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "📥", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = "لا توجد تنزيلات حالية",
                  color = VibrantTextPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp
                )
                Text(
                  text = "استكشف يوتيوب وحمّل أغانيك المفضلة بصيغة MP3",
                  color = VibrantTextMuted,
                  fontSize = 12.sp
                )
              }
            }
          }
        } else {
          item {
            Text(
              text = "مهام التنزيل والتحويل الصوتي",
              color = VibrantTextSecondary,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          items(downloadTasks) { task ->
            DownloadTaskCard(
              task = task,
              onPlaySong = {
                // Find matching song and play
                task.downloadedSongId?.let { sId ->
                  downloadedSongs.find { it.id == sId }?.let { song ->
                    onPlaySong(song)
                  }
                }
              }
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }
}

@Composable
private fun YouTubeVideoCard(
  video: YouTubeMusicVideo,
  isDownloaded: Boolean,
  onDownloadClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("yt_video_card_${video.id}")
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Thumbnail & Duration Chip
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .background(
            Brush.horizontalGradient(
              video.gradientTheme.map { Color(it) }
            )
          )
      ) {
        // Subtle decorative icon
        Icon(
          imageVector = Icons.Default.Audiotrack,
          contentDescription = null,
          tint = Color.White.copy(alpha = 0.15f),
          modifier = Modifier
            .size(100.dp)
            .align(Alignment.Center)
        )

        // Duration Badge
        Box(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black.copy(alpha = 0.8f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = video.duration,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Category Tag
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(8.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(VibrantDarkBackground.copy(alpha = 0.75f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = video.category,
            color = VibrantAccentCyan,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Details and Action Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = video.title,
            color = VibrantTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 2
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${video.channelTitle} • ${video.viewCount}",
            color = VibrantTextMuted,
            fontSize = 11.sp
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        if (isDownloaded) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(VibrantAccentGreen.copy(alpha = 0.15f))
              .padding(horizontal = 10.dp, vertical = 8.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VibrantAccentGreen, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("تم التنزيل", color = VibrantAccentGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        } else {
          Button(
            onClick = onDownloadClick,
            colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("download_btn_${video.id}")
          ) {
            Icon(Icons.Default.Download, contentDescription = null, tint = VibrantDarkBackground, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("تنزيل MP3", color = VibrantDarkBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
private fun DownloadTaskCard(
  task: DownloadTask,
  onPlaySong: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("download_task_${task.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                when (task.status) {
                  DownloadStatus.COMPLETED -> VibrantAccentGreen.copy(alpha = 0.2f)
                  DownloadStatus.DOWNLOADING, DownloadStatus.CONVERTING_TO_MP3 -> VibrantPurpleContainer
                  else -> VibrantDarkSurfaceVariant
                }
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = when (task.status) {
                DownloadStatus.COMPLETED -> Icons.Default.DownloadDone
                DownloadStatus.CONVERTING_TO_MP3 -> Icons.Default.FiberSmartRecord
                else -> Icons.Default.Downloading
              },
              contentDescription = null,
              tint = when (task.status) {
                DownloadStatus.COMPLETED -> VibrantAccentGreen
                else -> VibrantPurple
              },
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = task.video.title,
              color = VibrantTextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              maxLines = 1
            )
            Text(
              text = "صيغة MP3 (${task.quality.bitrate}) • ${task.video.channelTitle}",
              color = VibrantTextMuted,
              fontSize = 11.sp
            )
          }
        }

        if (task.status == DownloadStatus.COMPLETED) {
          Button(
            onClick = onPlaySong,
            colors = ButtonDefaults.buttonColors(containerColor = VibrantAccentGreen),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("play_downloaded_${task.id}")
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = VibrantDarkBackground, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("تشغيل", color = VibrantDarkBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Status indicator & progress bar
      if (task.status != DownloadStatus.COMPLETED) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = when (task.status) {
              DownloadStatus.DOWNLOADING -> "جاري تنزيل ملف الصوت..."
              DownloadStatus.CONVERTING_TO_MP3 -> "جاري التحويل والترميز إلى MP3 320kbps..."
              DownloadStatus.QUEUED -> "في قائمة الانتظار..."
              else -> "فشل التحميل"
            },
            color = VibrantPurple,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = "${(task.progress * 100).toInt()}%",
            color = VibrantAccentCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
          progress = { task.progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = VibrantPurple,
          trackColor = VibrantDarkActiveSurface
        )
      } else {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(VibrantDarkActiveSurface)
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VibrantAccentGreen, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "تم التنزيل والتحويل إلى MP3 وإضافتها لمكتبتك بنجاح 🎵",
              color = VibrantAccentGreen,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}
