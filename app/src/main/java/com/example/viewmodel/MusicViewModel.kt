package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleDownloadData
import com.example.data.SampleMusicData
import com.example.model.ArtistRecommendation
import com.example.model.DownloadStatus
import com.example.model.DownloadTask
import com.example.model.EarbudsState
import com.example.model.EqualizerState
import com.example.model.GenreStats
import com.example.model.ListeningHistoryItem
import com.example.model.Mp3AudioQuality
import com.example.model.NavigationTab
import com.example.model.NoiseControlMode
import com.example.model.Playlist
import com.example.model.RecommendedSongGroup
import com.example.model.RepeatMode
import com.example.model.Song
import com.example.model.UserProfile
import com.example.model.YouTubeMusicVideo
import com.example.service.AudioSynthesizerEngine
import com.example.service.CloudSyncService
import com.example.service.RecommendationEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

data class MusicUiState(
  val allSongs: List<Song> = SampleMusicData.songs,
  val currentSongIndex: Int = 0,
  val queue: List<Song> = SampleMusicData.songs,
  val isPlaying: Boolean = false,
  val currentPositionMs: Long = 0L,
  val isShuffleEnabled: Boolean = false,
  val repeatMode: RepeatMode = RepeatMode.OFF,
  val isFullPlayerExpanded: Boolean = false,
  val isLyricsViewActive: Boolean = false,
  val selectedTab: NavigationTab = NavigationTab.HOME,
  val searchQuery: String = "",
  val selectedGenre: String = "الكل",
  val playlists: List<Playlist> = SampleMusicData.initialPlaylists,
  val equalizerState: EqualizerState = EqualizerState(),
  val sleepTimerSecondsRemaining: Long? = null,
  val isSleepTimerActive: Boolean = false,
  val aiMoodText: String = "",
  val isAiGenerating: Boolean = false,
  val bannerNotification: String? = null,
  // User Profile & Cross-Device Sync State
  val userProfile: UserProfile = UserProfile(),
  val isSyncDialogVisible: Boolean = false,
  // Recommendation System & History
  val listeningHistory: List<ListeningHistoryItem> = listOf(
    ListeningHistoryItem("song_1", System.currentTimeMillis() - 3600000L * 2, 252),
    ListeningHistoryItem("song_2", System.currentTimeMillis() - 3600000L * 5, 210),
    ListeningHistoryItem("song_4", System.currentTimeMillis() - 3600000L * 8, 340),
    ListeningHistoryItem("song_7", System.currentTimeMillis() - 3600000L * 12, 195),
    ListeningHistoryItem("song_8", System.currentTimeMillis() - 3600000L * 24, 260)
  ),
  val genreStats: List<GenreStats> = emptyList(),
  val artistRecommendations: List<ArtistRecommendation> = emptyList(),
  val recommendedGroups: List<RecommendedSongGroup> = emptyList(),
  val isStatsDialogVisible: Boolean = false,
  // Custom Playlist Management
  val selectedPlaylistForDetail: Playlist? = null,
  val isPlaylistEditorVisible: Boolean = false,
  val playlistBeingEdited: Playlist? = null,
  val isAddSongToPlaylistPickerVisible: Boolean = false,
  // Earbuds Battery & iOS Connection Popup
  val earbudsState: EarbudsState = EarbudsState(),
  val isEarbudsPopupVisible: Boolean = false,
  // YouTube MP3 Downloader
  val youtubeVideos: List<YouTubeMusicVideo> = SampleDownloadData.trendingVideos,
  val downloadTasks: List<DownloadTask> = emptyList(),
  val selectedVideoForDownload: YouTubeMusicVideo? = null,
  val isDownloadFormatDialogVisible: Boolean = false
) {
  val currentSong: Song?
    get() = queue.getOrNull(currentSongIndex) ?: allSongs.firstOrNull()
}

class MusicViewModel : ViewModel() {

  private val audioEngine = AudioSynthesizerEngine(viewModelScope)

  private val _uiState = MutableStateFlow(MusicUiState())
  val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()

  val visualizerAmplitudes: StateFlow<List<Float>> = audioEngine.visualizerAmplitudes

  private var sleepTimerJob: Job? = null

  init {
    audioEngine.onTrackCompletion = {
      handleTrackEnded()
    }

    // Sync playback engine state with UI
    viewModelScope.launch {
      audioEngine.isPlaying.collect { playing ->
        _uiState.update { it.copy(isPlaying = playing) }
      }
    }

    viewModelScope.launch {
      audioEngine.currentPositionMs.collect { pos ->
        _uiState.update { it.copy(currentPositionMs = pos) }
      }
    }

    // Initialize recommendation engine data
    refreshRecommendations()
  }

  fun playSong(song: Song) {
    val currentIndex = _uiState.value.queue.indexOfFirst { it.id == song.id }
    val newQueue = if (currentIndex == -1) {
      _uiState.value.queue + song
    } else {
      _uiState.value.queue
    }
    val targetIndex = if (currentIndex != -1) currentIndex else newQueue.lastIndex

    _uiState.update {
      it.copy(
        queue = newQueue,
        currentSongIndex = targetIndex,
        currentPositionMs = 0L
      )
    }
    audioEngine.playSong(song, 0L)
    recordSongPlay(song)
  }

  fun playPlaylist(playlist: Playlist) {
    val songsInPlaylist = _uiState.value.allSongs.filter { playlist.songIds.contains(it.id) }
    if (songsInPlaylist.isNotEmpty()) {
      _uiState.update {
        it.copy(
          queue = songsInPlaylist,
          currentSongIndex = 0,
          currentPositionMs = 0L
        )
      }
      audioEngine.playSong(songsInPlaylist.first(), 0L)
      recordSongPlay(songsInPlaylist.first())
    }
  }

  fun togglePlayPause() {
    val state = _uiState.value
    if (state.isPlaying) {
      audioEngine.pause()
    } else {
      val song = state.currentSong
      if (song != null) {
        if (state.currentPositionMs > 0) {
          audioEngine.resume()
        } else {
          audioEngine.playSong(song, 0L)
          recordSongPlay(song)
        }
      }
    }
  }

  fun playNext() {
    val state = _uiState.value
    if (state.queue.isEmpty()) return

    val nextIndex = if (state.isShuffleEnabled) {
      (0 until state.queue.size).random()
    } else {
      (state.currentSongIndex + 1) % state.queue.size
    }

    _uiState.update { it.copy(currentSongIndex = nextIndex, currentPositionMs = 0L) }
    state.queue.getOrNull(nextIndex)?.let {
      audioEngine.playSong(it, 0L)
      recordSongPlay(it)
    }
  }

  fun playPrevious() {
    val state = _uiState.value
    if (state.queue.isEmpty()) return

    // If more than 3 seconds in, restart track
    if (state.currentPositionMs > 3000L) {
      audioEngine.seekTo(0L)
      return
    }

    val prevIndex = if (state.currentSongIndex - 1 < 0) {
      state.queue.size - 1
    } else {
      state.currentSongIndex - 1
    }

    _uiState.update { it.copy(currentSongIndex = prevIndex, currentPositionMs = 0L) }
    state.queue.getOrNull(prevIndex)?.let {
      audioEngine.playSong(it, 0L)
      recordSongPlay(it)
    }
  }

  fun seekTo(positionMs: Long) {
    audioEngine.seekTo(positionMs)
    _uiState.update { it.copy(currentPositionMs = positionMs) }
  }

  fun toggleShuffle() {
    _uiState.update { it.copy(isShuffleEnabled = !it.isShuffleEnabled) }
  }

  fun cycleRepeatMode() {
    val nextMode = when (_uiState.value.repeatMode) {
      RepeatMode.OFF -> RepeatMode.ALL
      RepeatMode.ALL -> RepeatMode.ONE
      RepeatMode.ONE -> RepeatMode.OFF
    }
    _uiState.update { it.copy(repeatMode = nextMode) }
  }

  fun toggleFavorite(songId: String) {
    _uiState.update { state ->
      val updatedSongs = state.allSongs.map { song ->
        if (song.id == songId) song.copy(isFavorite = !song.isFavorite) else song
      }
      val updatedQueue = state.queue.map { song ->
        if (song.id == songId) song.copy(isFavorite = !song.isFavorite) else song
      }

      // Update favorites playlist
      val favIds = updatedSongs.filter { it.isFavorite }.map { it.id }
      val updatedPlaylists = state.playlists.map { pl ->
        if (pl.id == "pl_favorites") pl.copy(songIds = favIds) else pl
      }

      state.copy(
        allSongs = updatedSongs,
        queue = updatedQueue,
        playlists = updatedPlaylists
      )
    }
    refreshRecommendations()
  }

  // ==========================================
  // CUSTOM PLAYLIST MANAGEMENT (Full CRUD + Reordering)
  // ==========================================

  fun createPlaylist(name: String, description: String = "قائمة تشغيل مخصصة", iconEmoji: String = "🎵") {
    val newPl = Playlist(
      id = "pl_${UUID.randomUUID().toString().take(8)}",
      name = name.ifBlank { "قائمتي الجديدة" },
      description = description,
      songIds = emptyList(),
      iconEmoji = iconEmoji,
      isCustom = true,
      updatedAt = System.currentTimeMillis()
    )
    _uiState.update {
      it.copy(
        playlists = it.playlists + newPl,
        bannerNotification = "تم إنشاء قائمة \"${newPl.name}\" بنجاح"
      )
    }
  }

  fun updatePlaylistDetails(playlistId: String, newName: String, newDescription: String, newIcon: String) {
    _uiState.update { state ->
      val updated = state.playlists.map { pl ->
        if (pl.id == playlistId) {
          pl.copy(
            name = newName.ifBlank { pl.name },
            description = newDescription,
            iconEmoji = newIcon,
            updatedAt = System.currentTimeMillis()
          )
        } else pl
      }
      val currentSelected = if (state.selectedPlaylistForDetail?.id == playlistId) {
        updated.find { it.id == playlistId }
      } else state.selectedPlaylistForDetail

      state.copy(
        playlists = updated,
        selectedPlaylistForDetail = currentSelected,
        isPlaylistEditorVisible = false,
        bannerNotification = "تم حفظ تعديلات القائمة بنجاح"
      )
    }
  }

  fun deletePlaylist(playlistId: String) {
    _uiState.update { state ->
      val target = state.playlists.find { it.id == playlistId }
      val updated = state.playlists.filterNot { it.id == playlistId }
      val currentSelected = if (state.selectedPlaylistForDetail?.id == playlistId) null else state.selectedPlaylistForDetail

      state.copy(
        playlists = updated,
        selectedPlaylistForDetail = currentSelected,
        bannerNotification = "تم حذف قائمة \"${target?.name ?: ""}\""
      )
    }
  }

  fun addSongToPlaylist(playlistId: String, songId: String) {
    _uiState.update { state ->
      val updated = state.playlists.map { pl ->
        if (pl.id == playlistId && !pl.songIds.contains(songId)) {
          pl.copy(
            songIds = pl.songIds + songId,
            updatedAt = System.currentTimeMillis()
          )
        } else pl
      }
      val currentSelected = if (state.selectedPlaylistForDetail?.id == playlistId) {
        updated.find { it.id == playlistId }
      } else state.selectedPlaylistForDetail

      state.copy(
        playlists = updated,
        selectedPlaylistForDetail = currentSelected,
        bannerNotification = "تمت إضافة الأغنية إلى القائمة"
      )
    }
  }

  fun removeSongFromPlaylist(playlistId: String, songId: String) {
    _uiState.update { state ->
      val updated = state.playlists.map { pl ->
        if (pl.id == playlistId) {
          pl.copy(
            songIds = pl.songIds.filterNot { it == songId },
            updatedAt = System.currentTimeMillis()
          )
        } else pl
      }
      val currentSelected = if (state.selectedPlaylistForDetail?.id == playlistId) {
        updated.find { it.id == playlistId }
      } else state.selectedPlaylistForDetail

      state.copy(
        playlists = updated,
        selectedPlaylistForDetail = currentSelected,
        bannerNotification = "تمت إزالة الأغنية من القائمة"
      )
    }
  }

  fun moveSongUpInPlaylist(playlistId: String, songId: String) {
    _uiState.update { state ->
      val updated = state.playlists.map { pl ->
        if (pl.id == playlistId) {
          val list = pl.songIds.toMutableList()
          val index = list.indexOf(songId)
          if (index > 0) {
            val temp = list[index]
            list[index] = list[index - 1]
            list[index - 1] = temp
          }
          pl.copy(songIds = list, updatedAt = System.currentTimeMillis())
        } else pl
      }
      val currentSelected = if (state.selectedPlaylistForDetail?.id == playlistId) {
        updated.find { it.id == playlistId }
      } else state.selectedPlaylistForDetail

      state.copy(playlists = updated, selectedPlaylistForDetail = currentSelected)
    }
  }

  fun moveSongDownInPlaylist(playlistId: String, songId: String) {
    _uiState.update { state ->
      val updated = state.playlists.map { pl ->
        if (pl.id == playlistId) {
          val list = pl.songIds.toMutableList()
          val index = list.indexOf(songId)
          if (index in 0 until list.size - 1) {
            val temp = list[index]
            list[index] = list[index + 1]
            list[index + 1] = temp
          }
          pl.copy(songIds = list, updatedAt = System.currentTimeMillis())
        } else pl
      }
      val currentSelected = if (state.selectedPlaylistForDetail?.id == playlistId) {
        updated.find { it.id == playlistId }
      } else state.selectedPlaylistForDetail

      state.copy(playlists = updated, selectedPlaylistForDetail = currentSelected)
    }
  }

  fun openPlaylistDetail(playlist: Playlist) {
    _uiState.update { it.copy(selectedPlaylistForDetail = playlist) }
  }

  fun closePlaylistDetail() {
    _uiState.update { it.copy(selectedPlaylistForDetail = null) }
  }

  fun showPlaylistEditor(show: Boolean, playlist: Playlist? = null) {
    _uiState.update {
      it.copy(
        isPlaylistEditorVisible = show,
        playlistBeingEdited = playlist ?: it.selectedPlaylistForDetail
      )
    }
  }

  fun showAddSongPicker(show: Boolean) {
    _uiState.update { it.copy(isAddSongToPlaylistPickerVisible = show) }
  }

  // ==========================================
  // CROSS-DEVICE SYNCHRONIZATION
  // ==========================================

  fun triggerCloudSync() {
    val state = _uiState.value
    if (state.userProfile.isSyncing) return

    _uiState.update {
      it.copy(userProfile = it.userProfile.copy(isSyncing = true))
    }

    viewModelScope.launch {
      val favIds = _uiState.value.allSongs.filter { it.isFavorite }.map { it.id }
      val result = CloudSyncService.syncWithCloud(
        _uiState.value.userProfile,
        _uiState.value.playlists,
        favIds
      )

      _uiState.update {
        it.copy(
          userProfile = it.userProfile.copy(
            isSyncing = false,
            lastSyncTime = result.lastSyncTimestamp,
            connectedDevices = result.updatedDevices
          ),
          bannerNotification = "تمت المزامنة السحابية بنجاح عبر كافة أجهزتك (${result.syncedPlaylistsCount} قوائم تشغيل)"
        )
      }
    }
  }

  fun toggleAutoSync() {
    _uiState.update {
      it.copy(
        userProfile = it.userProfile.copy(
          autoSyncEnabled = !it.userProfile.autoSyncEnabled
        )
      )
    }
  }

  fun showSyncDialog(show: Boolean) {
    _uiState.update { it.copy(isSyncDialogVisible = show) }
  }

  fun updateUserProfile(name: String, email: String) {
    _uiState.update {
      it.copy(
        userProfile = it.userProfile.copy(
          name = name.ifBlank { it.userProfile.name },
          email = email.ifBlank { it.userProfile.email }
        ),
        bannerNotification = "تم تحديث الملف الشخصي"
      )
    }
  }

  // ==========================================
  // RECOMMENDATION SYSTEM & LISTENING HISTORY
  // ==========================================

  fun recordSongPlay(song: Song) {
    val newHistoryItem = ListeningHistoryItem(
      songId = song.id,
      timestampMs = System.currentTimeMillis(),
      listenDurationSec = song.durationMs / 1000
    )

    _uiState.update { state ->
      val updatedHistory = listOf(newHistoryItem) + state.listeningHistory.take(49)
      val updatedSongs = state.allSongs.map { s ->
        if (s.id == song.id) s.copy(playCount = s.playCount + 1) else s
      }
      state.copy(
        listeningHistory = updatedHistory,
        allSongs = updatedSongs
      )
    }

    refreshRecommendations()
  }

  fun refreshRecommendations() {
    val songs = _uiState.value.allSongs
    val history = _uiState.value.listeningHistory

    val genreStats = RecommendationEngine.calculateGenreStats(songs, history)
    val artistRecs = RecommendationEngine.getArtistRecommendations(songs, history)
    val songGroups = RecommendationEngine.generateRecommendations(songs, history)

    _uiState.update {
      it.copy(
        genreStats = genreStats,
        artistRecommendations = artistRecs,
        recommendedGroups = songGroups
      )
    }
  }

  fun showStatsDialog(show: Boolean) {
    _uiState.update { it.copy(isStatsDialogVisible = show) }
  }

  // ==========================================
  // NAVIGATION & PLAYER UTILITIES
  // ==========================================

  fun selectTab(tab: NavigationTab) {
    _uiState.update { it.copy(selectedTab = tab, selectedPlaylistForDetail = null) }
  }

  fun setFullPlayerExpanded(expanded: Boolean) {
    _uiState.update { it.copy(isFullPlayerExpanded = expanded) }
  }

  fun toggleLyricsView() {
    _uiState.update { it.copy(isLyricsViewActive = !it.isLyricsViewActive) }
  }

  fun setSearchQuery(query: String) {
    _uiState.update { it.copy(searchQuery = query) }
  }

  fun selectGenre(genre: String) {
    _uiState.update { it.copy(selectedGenre = genre) }
  }

  fun setSleepTimer(minutes: Int) {
    sleepTimerJob?.cancel()
    if (minutes <= 0) {
      _uiState.update { it.copy(sleepTimerSecondsRemaining = null, isSleepTimerActive = false) }
      return
    }

    val totalSeconds = minutes * 60L
    _uiState.update { it.copy(sleepTimerSecondsRemaining = totalSeconds, isSleepTimerActive = true) }

    sleepTimerJob = viewModelScope.launch {
      var remaining = totalSeconds
      while (isActive && remaining > 0) {
        delay(1000)
        remaining--
        _uiState.update { it.copy(sleepTimerSecondsRemaining = remaining) }
      }
      audioEngine.pause()
      _uiState.update {
        it.copy(
          sleepTimerSecondsRemaining = null,
          isSleepTimerActive = false,
          bannerNotification = "انتهى مؤقت النوم وتم إيقاف الموسيقى"
        )
      }
    }
  }

  fun applyEqualizerPreset(presetName: String) {
    val gains = EqualizerState.presets[presetName] ?: return
    val currentBands = _uiState.value.equalizerState.bands
    val updatedBands = currentBands.mapIndexed { index, band ->
      band.copy(gainDb = gains.getOrElse(index) { 0f })
    }
    _uiState.update {
      it.copy(
        equalizerState = it.equalizerState.copy(
          selectedPreset = presetName,
          bands = updatedBands
        )
      )
    }
  }

  fun updateEqualizerBand(index: Int, gainDb: Float) {
    val currentBands = _uiState.value.equalizerState.bands.toMutableList()
    if (index in currentBands.indices) {
      currentBands[index] = currentBands[index].copy(gainDb = gainDb)
      _uiState.update {
        it.copy(
          equalizerState = it.equalizerState.copy(
            selectedPreset = "مخصص (Custom)",
            bands = currentBands
          )
        )
      }
    }
  }

  fun setBassBoost(value: Float) {
    _uiState.update {
      it.copy(equalizerState = it.equalizerState.copy(bassBoost = value))
    }
  }

  fun setVirtualizer3d(value: Float) {
    _uiState.update {
      it.copy(equalizerState = it.equalizerState.copy(virtualizer3d = value))
    }
  }

  fun toggleEqualizer(enabled: Boolean) {
    _uiState.update {
      it.copy(equalizerState = it.equalizerState.copy(isEnabled = enabled))
    }
  }

  fun generateAiPlaylist(moodPrompt: String) {
    if (moodPrompt.isBlank()) return
    _uiState.update { it.copy(isAiGenerating = true) }

    viewModelScope.launch {
      delay(1200) // Realistic AI synthesis latency

      val lowerPrompt = moodPrompt.lowercase()
      val matchedSongs = _uiState.value.allSongs.filter { song ->
        when {
          lowerPrompt.contains("طرب") || lowerPrompt.contains("سلطنة") || lowerPrompt.contains("قديم") ->
            song.genre.contains("طرب") || song.genre.contains("قصائد") || song.artist.contains("فيروز") || song.artist.contains("كاظم") || song.artist.contains("عبد الحليم")
          lowerPrompt.contains("هدوء") || lowerPrompt.contains("دراسة") || lowerPrompt.contains("نوم") || lowerPrompt.contains("تركيز") ->
            song.genre.contains("هادئة") || song.artist.contains("Lofi") || song.artist.contains("فيروز") || song.artist.contains("أندلسي")
          lowerPrompt.contains("حماس") || lowerPrompt.contains("رياضة") || lowerPrompt.contains("جيم") || lowerPrompt.contains("سريع") ->
            song.genre.contains("بوب") || song.genre.contains("راي") || song.tempoBpm >= 105
          lowerPrompt.contains("حب") || lowerPrompt.contains("رومانسي") || lowerPrompt.contains("شوق") ->
            song.genre.contains("رومانسي") || song.title.contains("سر الوجود") || song.title.contains("تناديك") || song.title.contains("مشاعر")
          else -> true
        }
      }.ifEmpty { _uiState.value.allSongs.shuffled().take(4) }

      val newAiPlaylist = Playlist(
        id = "ai_pl_${System.currentTimeMillis()}",
        name = "قائمة ذكية: $moodPrompt",
        description = "تم إنشاؤها بالذكاء الاصطناعي لتلائم مزاج: $moodPrompt",
        songIds = matchedSongs.map { it.id },
        iconEmoji = "🤖",
        isCustom = true
      )

      _uiState.update {
        it.copy(
          isAiGenerating = false,
          playlists = listOf(newAiPlaylist) + it.playlists,
          bannerNotification = "تم إنشاء قائمة الذكاء الاصطناعي: ${newAiPlaylist.name}"
        )
      }
    }
  }

  // ==========================================
  // AIRPODS & BLUETOOTH EARBUDS (iOS Pop-up Card)
  // ==========================================

  fun showEarbudsPopup(show: Boolean) {
    _uiState.update { it.copy(isEarbudsPopupVisible = show) }
  }

  fun triggerEarbudsCaseOpen() {
    _uiState.update {
      it.copy(
        isEarbudsPopupVisible = true,
        earbudsState = it.earbudsState.copy(isCaseOpen = true, isConnected = true)
      )
    }
  }

  fun toggleEarbudsConnection() {
    _uiState.update {
      val newConnected = !it.earbudsState.isConnected
      it.copy(
        earbudsState = it.earbudsState.copy(isConnected = newConnected),
        bannerNotification = if (newConnected) "تم توصيل ${it.earbudsState.deviceName} بنجاح" else "تم قطع اتصال السماعة"
      )
    }
  }

  fun setEarbudsNoiseControl(mode: NoiseControlMode) {
    _uiState.update {
      it.copy(
        earbudsState = it.earbudsState.copy(noiseControlMode = mode),
        bannerNotification = "تم تغيير وضع عزل الضجيج إلى: ${mode.labelAr}"
      )
    }
  }

  fun toggleEarbudsSpatialAudio() {
    _uiState.update {
      val newSpatial = !it.earbudsState.isSpatialAudioActive
      it.copy(
        earbudsState = it.earbudsState.copy(isSpatialAudioActive = newSpatial),
        bannerNotification = if (newSpatial) "تم تفعيل ميزة الصوت المكاني 3D" else "تم إيقاف الصوت المكاني"
      )
    }
  }

  // ==========================================
  // YOUTUBE MP3 MUSIC DOWNLOADER (Strictly MP3 Audio)
  // ==========================================

  fun showDownloadFormatDialog(video: YouTubeMusicVideo?) {
    _uiState.update {
      it.copy(
        selectedVideoForDownload = video,
        isDownloadFormatDialogVisible = video != null
      )
    }
  }

  fun startMp3Download(video: YouTubeMusicVideo, quality: Mp3AudioQuality) {
    val taskId = "dl_${UUID.randomUUID().toString().take(8)}"
    val task = DownloadTask(
      id = taskId,
      video = video,
      quality = quality,
      progress = 0.05f,
      status = DownloadStatus.DOWNLOADING,
      totalSizeMb = when (quality) {
        Mp3AudioQuality.HQ_320 -> 9.8f
        Mp3AudioQuality.HQ_256 -> 7.4f
        Mp3AudioQuality.HQ_128 -> 4.1f
      }
    )

    _uiState.update {
      it.copy(
        downloadTasks = listOf(task) + it.downloadTasks,
        isDownloadFormatDialogVisible = false,
        selectedVideoForDownload = null,
        bannerNotification = "بدأ تنزيل وتحويل \"${video.title}\" بصيغة MP3..."
      )
    }

    viewModelScope.launch {
      // Step 1: Downloading Audio Stream
      for (p in 10..70 step 15) {
        delay(400)
        _uiState.update { state ->
          val updated = state.downloadTasks.map { t ->
            if (t.id == taskId) t.copy(
              progress = p / 100f,
              downloadedSizeMb = (p / 100f) * t.totalSizeMb,
              status = DownloadStatus.DOWNLOADING
            ) else t
          }
          state.copy(downloadTasks = updated)
        }
      }

      // Step 2: Converting & Tagging to MP3
      _uiState.update { state ->
        val updated = state.downloadTasks.map { t ->
          if (t.id == taskId) t.copy(
            progress = 0.85f,
            status = DownloadStatus.CONVERTING_TO_MP3
          ) else t
        }
        state.copy(downloadTasks = updated)
      }
      delay(600)

      // Step 3: Creation of Local MP3 Song in Library
      val newSongId = "song_dl_${UUID.randomUUID().toString().take(8)}"
      val downloadedSong = Song(
        id = newSongId,
        title = video.title.substringBefore("-").trim().ifBlank { video.title },
        artist = if (video.title.contains("-")) video.title.substringBefore("-").trim() else video.channelTitle,
        album = "التنزيلات (MP3)",
        durationMs = video.durationMs,
        genre = video.category,
        releaseYear = "2024",
        isFavorite = false,
        playCount = 0,
        lyrics = listOf(
          com.example.model.LyricLine(0L, "♪ تم التنزيل بصيغة MP3 فائقة النقاء (${quality.bitrate}) ♪"),
          com.example.model.LyricLine(15000L, "♪ جودة الصوت: ${quality.label} ♪")
        ),
        baseFrequencies = listOf(220.00f, 261.63f, 329.63f, 392.00f, 493.88f),
        tempoBpm = 100
      )

      _uiState.update { state ->
        val updatedTasks = state.downloadTasks.map { t ->
          if (t.id == taskId) t.copy(
            progress = 1.0f,
            status = DownloadStatus.COMPLETED,
            downloadedSongId = newSongId
          ) else t
        }

        // Add to downloads playlist or create it
        val downloadsPl = state.playlists.find { it.id == "pl_downloads" }
        val updatedPlaylists = if (downloadsPl != null) {
          state.playlists.map { pl ->
            if (pl.id == "pl_downloads") pl.copy(songIds = pl.songIds + newSongId) else pl
          }
        } else {
          state.playlists + Playlist(
            id = "pl_downloads",
            name = "الأغاني المحملة (MP3)",
            description = "الأغاني التي تم تنزيلها بصيغة MP3 من يوتيوب",
            songIds = listOf(newSongId),
            iconEmoji = "📥",
            isCustom = true
          )
        }

        state.copy(
          downloadTasks = updatedTasks,
          allSongs = listOf(downloadedSong) + state.allSongs,
          playlists = updatedPlaylists,
          bannerNotification = "اكتمل التنزيل! أضيفت \"${downloadedSong.title}\" كملف MP3 إلى مكتبتك 🎵"
        )
      }
    }
  }

  fun dismissBanner() {
    _uiState.update { it.copy(bannerNotification = null) }
  }

  private fun handleTrackEnded() {
    val state = _uiState.value
    when (state.repeatMode) {
      RepeatMode.ONE -> {
        state.currentSong?.let { audioEngine.playSong(it, 0L) }
      }
      RepeatMode.ALL -> {
        playNext()
      }
      RepeatMode.OFF -> {
        if (state.currentSongIndex < state.queue.size - 1) {
          playNext()
        } else {
          audioEngine.pause()
        }
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    audioEngine.release()
  }
}
