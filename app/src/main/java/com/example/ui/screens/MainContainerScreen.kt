package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NavigationTab
import com.example.model.Song
import com.example.ui.components.AddSongToPlaylistPicker
import com.example.ui.components.AiPlaylistDialog
import com.example.ui.components.AirPodsPopupCard
import com.example.ui.components.DownloadFormatDialog
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.PlaylistEditorDialog
import com.example.ui.components.PlaylistSelectorDialog
import com.example.ui.components.SleepTimerDialog
import com.example.ui.components.SyncProfileDialog
import com.example.ui.components.TasteStatsDialog
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantNavPill
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantTextSecondary
import com.example.viewmodel.MusicViewModel

@Composable
fun MainContainerScreen(
  viewModel: MusicViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val visualizerAmplitudes by viewModel.visualizerAmplitudes.collectAsState()

  var showSleepTimerDialog by remember { mutableStateOf(false) }
  var showAiDialog by remember { mutableStateOf(false) }
  var songToAddToPlaylist by remember { mutableStateOf<Song?>(null) }
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.bannerNotification) {
    uiState.bannerNotification?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.dismissBanner()
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = VibrantDarkBackground,
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      if (!uiState.isFullPlayerExpanded && uiState.selectedPlaylistForDetail == null) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
        ) {
          // Docked Mini Player
          MiniPlayerBar(
            song = uiState.currentSong,
            isPlaying = uiState.isPlaying,
            currentPositionMs = uiState.currentPositionMs,
            onTogglePlayPause = { viewModel.togglePlayPause() },
            onPlayNext = { viewModel.playNext() },
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onClick = { viewModel.setFullPlayerExpanded(true) }
          )

          // Vibrant Palette Bottom Navigation Bar (5 tabs)
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(elevation = 16.dp, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
              .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
              .testTag("vibrant_bottom_nav"),
            color = VibrantDarkSurface
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceAround,
              verticalAlignment = Alignment.CenterVertically
            ) {
              NavItem(
                icon = Icons.Default.Home,
                label = "الرئيسية",
                isSelected = uiState.selectedTab == NavigationTab.HOME,
                onClick = { viewModel.selectTab(NavigationTab.HOME) },
                testTag = "nav_home"
              )
              NavItem(
                icon = Icons.Default.AutoAwesome,
                label = "توصيات",
                isSelected = uiState.selectedTab == NavigationTab.RECOMMENDATIONS,
                onClick = { viewModel.selectTab(NavigationTab.RECOMMENDATIONS) },
                testTag = "nav_recommendations"
              )
              NavItem(
                icon = Icons.Default.Download,
                label = "تنزيل",
                isSelected = uiState.selectedTab == NavigationTab.DOWNLOADER,
                onClick = { viewModel.selectTab(NavigationTab.DOWNLOADER) },
                testTag = "nav_downloader"
              )
              NavItem(
                icon = Icons.Default.Search,
                label = "بحث",
                isSelected = uiState.selectedTab == NavigationTab.SEARCH,
                onClick = { viewModel.selectTab(NavigationTab.SEARCH) },
                testTag = "nav_search"
              )
              NavItem(
                icon = Icons.Default.LibraryMusic,
                label = "المكتبة",
                isSelected = uiState.selectedTab == NavigationTab.LIBRARY,
                onClick = { viewModel.selectTab(NavigationTab.LIBRARY) },
                testTag = "nav_library"
              )
              NavItem(
                icon = Icons.Default.Equalizer,
                label = "المعادل",
                isSelected = uiState.selectedTab == NavigationTab.EQUALIZER,
                onClick = { viewModel.selectTab(NavigationTab.EQUALIZER) },
                testTag = "nav_equalizer"
              )
            }
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Main Content Screen
      if (uiState.selectedPlaylistForDetail != null) {
        val playlist = uiState.selectedPlaylistForDetail!!
        PlaylistDetailScreen(
          playlist = playlist,
          allSongs = uiState.allSongs,
          currentPlayingSong = uiState.currentSong,
          isPlaying = uiState.isPlaying,
          onBack = { viewModel.closePlaylistDetail() },
          onPlaySong = { viewModel.playSong(it) },
          onPlayPlaylist = { viewModel.playPlaylist(it) },
          onToggleFavorite = { viewModel.toggleFavorite(it) },
          onOpenEditor = { viewModel.showPlaylistEditor(true, playlist) },
          onOpenAddSongs = { viewModel.showAddSongPicker(true) },
          onRemoveSong = { sId -> viewModel.removeSongFromPlaylist(playlist.id, sId) },
          onMoveUp = { sId -> viewModel.moveSongUpInPlaylist(playlist.id, sId) },
          onMoveDown = { sId -> viewModel.moveSongDownInPlaylist(playlist.id, sId) },
          onDeletePlaylist = { pId -> viewModel.deletePlaylist(pId) }
        )
      } else {
        when (uiState.selectedTab) {
          NavigationTab.HOME -> {
            HomeScreen(
              songs = uiState.allSongs,
              playlists = uiState.playlists,
              currentPlayingSong = uiState.currentSong,
              isPlaying = uiState.isPlaying,
              userProfile = uiState.userProfile,
              earbudsState = uiState.earbudsState,
              onSongClick = { viewModel.playSong(it) },
              onPlaylistClick = { viewModel.openPlaylistDetail(it) },
              onToggleFavorite = { viewModel.toggleFavorite(it) },
              onOpenAiDialog = { showAiDialog = true },
              onOpenSyncProfile = { viewModel.showSyncDialog(true) },
              onOpenEarbudsPopup = { viewModel.showEarbudsPopup(true) },
              onOpenAddToPlaylist = { songToAddToPlaylist = it }
            )
          }
          NavigationTab.RECOMMENDATIONS -> {
            RecommendationsScreen(
              artistRecommendations = uiState.artistRecommendations,
              recommendedGroups = uiState.recommendedGroups,
              currentPlayingSong = uiState.currentSong,
              isPlaying = uiState.isPlaying,
              onPlaySong = { viewModel.playSong(it) },
              onToggleFavorite = { viewModel.toggleFavorite(it) },
              onOpenStats = { viewModel.showStatsDialog(true) },
              onRefreshRecommendations = { viewModel.refreshRecommendations() }
            )
          }
          NavigationTab.DOWNLOADER -> {
            DownloaderScreen(
              videos = uiState.youtubeVideos,
              downloadTasks = uiState.downloadTasks,
              downloadedSongs = uiState.allSongs,
              currentPlayingSong = uiState.currentSong,
              isPlaying = uiState.isPlaying,
              onSearchQueryChange = { /* Query handled inside */ },
              onCategorySelect = { /* Category handled inside */ },
              onStartDownload = { video -> viewModel.showDownloadFormatDialog(video) },
              onPlaySong = { viewModel.playSong(it) }
            )
          }
          NavigationTab.SEARCH -> {
            SearchScreen(
              songs = uiState.allSongs,
              searchQuery = uiState.searchQuery,
              selectedGenre = uiState.selectedGenre,
              currentPlayingSong = uiState.currentSong,
              onQueryChange = { viewModel.setSearchQuery(it) },
              onGenreSelect = { viewModel.selectGenre(it) },
              onSongClick = { viewModel.playSong(it) },
              onToggleFavorite = { viewModel.toggleFavorite(it) }
            )
          }
          NavigationTab.LIBRARY -> {
            LibraryScreen(
              playlists = uiState.playlists,
              songs = uiState.allSongs,
              onPlaylistClick = { viewModel.openPlaylistDetail(it) },
              onSongClick = { viewModel.playSong(it) },
              onCreatePlaylist = { viewModel.createPlaylist(it) },
              onToggleFavorite = { viewModel.toggleFavorite(it) }
            )
          }
          NavigationTab.EQUALIZER -> {
            EqualizerScreen(
              equalizerState = uiState.equalizerState,
              onToggleEnabled = { viewModel.toggleEqualizer(it) },
              onSelectPreset = { viewModel.applyEqualizerPreset(it) },
              onUpdateBand = { index, gain -> viewModel.updateEqualizerBand(index, gain) },
              onUpdateBassBoost = { viewModel.setBassBoost(it) },
              onUpdateVirtualizer = { viewModel.setVirtualizer3d(it) }
            )
          }
        }
      }

      // Full Player Screen Overlay
      AnimatedVisibility(
        visible = uiState.isFullPlayerExpanded,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
      ) {
        uiState.currentSong?.let { currentSong ->
          NowPlayingScreen(
            song = currentSong,
            isPlaying = uiState.isPlaying,
            currentPositionMs = uiState.currentPositionMs,
            visualizerAmplitudes = visualizerAmplitudes,
            isShuffleEnabled = uiState.isShuffleEnabled,
            repeatMode = uiState.repeatMode,
            isLyricsActive = uiState.isLyricsViewActive,
            isSleepTimerActive = uiState.isSleepTimerActive,
            onTogglePlayPause = { viewModel.togglePlayPause() },
            onPlayNext = { viewModel.playNext() },
            onPlayPrevious = { viewModel.playPrevious() },
            onSeekTo = { viewModel.seekTo(it) },
            onToggleShuffle = { viewModel.toggleShuffle() },
            onCycleRepeatMode = { viewModel.cycleRepeatMode() },
            onToggleFavorite = { viewModel.toggleFavorite(currentSong.id) },
            onToggleLyrics = { viewModel.toggleLyricsView() },
            onOpenSleepTimer = { showSleepTimerDialog = true },
            onOpenEqualizer = {
              viewModel.setFullPlayerExpanded(false)
              viewModel.selectTab(NavigationTab.EQUALIZER)
            },
            onOpenAddToPlaylist = { songToAddToPlaylist = currentSong },
            onCollapse = { viewModel.setFullPlayerExpanded(false) }
          )
        }
      }
    }
  }

  // Cross-Device Sync & Profile Dialog
  if (uiState.isSyncDialogVisible) {
    SyncProfileDialog(
      userProfile = uiState.userProfile,
      onDismiss = { viewModel.showSyncDialog(false) },
      onTriggerSync = { viewModel.triggerCloudSync() },
      onToggleAutoSync = { viewModel.toggleAutoSync() },
      onUpdateProfile = { name, email -> viewModel.updateUserProfile(name, email) }
    )
  }

  // Music Taste Analytics Dialog
  if (uiState.isStatsDialogVisible) {
    TasteStatsDialog(
      genreStats = uiState.genreStats,
      history = uiState.listeningHistory,
      onDismiss = { viewModel.showStatsDialog(false) }
    )
  }

  // Playlist Editor Dialog
  if (uiState.isPlaylistEditorVisible) {
    PlaylistEditorDialog(
      playlist = uiState.playlistBeingEdited,
      onDismiss = { viewModel.showPlaylistEditor(false) },
      onSave = { name, desc, emoji ->
        uiState.playlistBeingEdited?.let { pl ->
          viewModel.updatePlaylistDetails(pl.id, name, desc, emoji)
        } ?: viewModel.createPlaylist(name, desc, emoji)
      }
    )
  }

  // Add Songs to Playlist Picker
  if (uiState.isAddSongToPlaylistPickerVisible && uiState.selectedPlaylistForDetail != null) {
    val currentPl = uiState.selectedPlaylistForDetail!!
    AddSongToPlaylistPicker(
      playlist = currentPl,
      allSongs = uiState.allSongs,
      onDismiss = { viewModel.showAddSongPicker(false) },
      onToggleSong = { songId, isInPlaylist ->
        if (isInPlaylist) {
          viewModel.removeSongFromPlaylist(currentPl.id, songId)
        } else {
          viewModel.addSongToPlaylist(currentPl.id, songId)
        }
      }
    )
  }

  // Sleep Timer Dialog
  if (showSleepTimerDialog) {
    SleepTimerDialog(
      isActive = uiState.isSleepTimerActive,
      secondsRemaining = uiState.sleepTimerSecondsRemaining,
      onSetTimer = { viewModel.setSleepTimer(it) },
      onCancelTimer = { viewModel.setSleepTimer(0) },
      onDismiss = { showSleepTimerDialog = false }
    )
  }

  // AI Playlist Generator Dialog
  if (showAiDialog) {
    AiPlaylistDialog(
      isGenerating = uiState.isAiGenerating,
      onGenerate = { viewModel.generateAiPlaylist(it) },
      onDismiss = { showAiDialog = false }
    )
  }

  // Add to Playlist Quick Selector
  songToAddToPlaylist?.let { song ->
    PlaylistSelectorDialog(
      song = song,
      playlists = uiState.playlists,
      onAddToPlaylist = { plId, sId -> viewModel.addSongToPlaylist(plId, sId) },
      onCreateAndAdd = { plName, sId ->
        viewModel.createPlaylist(plName)
        val created = viewModel.uiState.value.playlists.lastOrNull()
        if (created != null) {
          viewModel.addSongToPlaylist(created.id, sId)
        }
      },
      onDismiss = { songToAddToPlaylist = null }
    )
  }

  // YouTube MP3 Format Quality Dialog
  if (uiState.isDownloadFormatDialogVisible && uiState.selectedVideoForDownload != null) {
    DownloadFormatDialog(
      video = uiState.selectedVideoForDownload!!,
      onDismiss = { viewModel.showDownloadFormatDialog(null) },
      onConfirmDownload = { quality ->
        viewModel.startMp3Download(uiState.selectedVideoForDownload!!, quality)
      }
    )
  }

  // iOS-Style AirPods / Earbuds Connection Popup Overlay
  AnimatedVisibility(
    visible = uiState.isEarbudsPopupVisible,
    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
  ) {
    AirPodsPopupCard(
      earbudsState = uiState.earbudsState,
      onDismiss = { viewModel.showEarbudsPopup(false) },
      onToggleConnection = { viewModel.toggleEarbudsConnection() },
      onSetNoiseControl = { viewModel.setEarbudsNoiseControl(it) },
      onToggleSpatialAudio = { viewModel.toggleEarbudsSpatialAudio() }
    )
  }
}

@Composable
private fun NavItem(
  icon: ImageVector,
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 6.dp, vertical = 2.dp)
      .testTag(testTag)
  ) {
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(if (isSelected) VibrantNavPill else Color.Transparent)
        .padding(horizontal = 14.dp, vertical = 4.dp),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = if (isSelected) VibrantPurple else VibrantTextSecondary,
        modifier = Modifier.size(20.dp)
      )
    }
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      fontSize = 10.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
      color = if (isSelected) VibrantPurple else VibrantTextSecondary
    )
  }
}
