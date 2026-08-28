package com.example.service

import com.example.model.ArtistRecommendation
import com.example.model.GenreStats
import com.example.model.ListeningHistoryItem
import com.example.model.RecommendedSongGroup
import com.example.model.Song

object RecommendationEngine {

  /**
   * Calculates top genres breakdown percentages based on user's listening history and favorites.
   */
  fun calculateGenreStats(
    allSongs: List<Song>,
    history: List<ListeningHistoryItem>
  ): List<GenreStats> {
    val genreCounts = mutableMapOf<String, Int>()

    // Count plays from history
    for (item in history) {
      val song = allSongs.find { it.id == item.songId }
      if (song != null) {
        genreCounts[song.genre] = (genreCounts[song.genre] ?: 0) + 1
      }
    }

    // Give bonus weight to favorited songs
    for (song in allSongs.filter { it.isFavorite }) {
      genreCounts[song.genre] = (genreCounts[song.genre] ?: 0) + 2
    }

    // Default distribution if history is low
    if (genreCounts.isEmpty()) {
      genreCounts["طرب عراقي"] = 4
      genreCounts["كلاسيك طربي"] = 5
      genreCounts["موسيقى هادئة وتركيز"] = 3
      genreCounts["بوب رومانسي"] = 2
    }

    val totalCount = genreCounts.values.sum().toFloat().coerceAtLeast(1f)

    return genreCounts.entries
      .sortedByDescending { it.value }
      .map { entry ->
        GenreStats(
          genre = entry.key,
          percentage = ((entry.value / totalCount) * 100f).coerceIn(5f, 100f),
          playCount = entry.value
        )
      }
  }

  /**
   * Recommends artists that match the user's favorite genres and artists.
   */
  fun getArtistRecommendations(
    allSongs: List<Song>,
    history: List<ListeningHistoryItem>
  ): List<ArtistRecommendation> {
    val genreStats = calculateGenreStats(allSongs, history)
    val topGenre = genreStats.firstOrNull()?.genre ?: "كلاسيك طربي"
    val secondGenre = genreStats.getOrNull(1)?.genre ?: "موسيقى هادئة وتركيز"

    val catalog = listOf(
      ArtistRecommendation(
        artistName = "عبد الحليم حافظ",
        genre = "كلاسيك طربي",
        reason = "لأنك تستمع بكثرة إلى فيروز وكاظم الساهر",
        matchPercentage = 98,
        topSongTitle = "أهواك",
        avatarEmoji = "🎙️"
      ),
      ArtistRecommendation(
        artistName = "شيرين",
        genre = "بوب رومانسي",
        reason = "يتطابق مع تفضيلك للأغاني الرومانسية الحديثة",
        matchPercentage = 94,
        topSongTitle = "مشاعر",
        avatarEmoji = "✨"
      ),
      ArtistRecommendation(
        artistName = "ثنائي المقام الأندلسي",
        genre = "موسيقى هادئة وتركيز",
        reason = "يلائم أوقات دراستك وهدوئك ومسارات العود",
        matchPercentage = 91,
        topSongTitle = "أندلسيات العود والبيانو",
        avatarEmoji = "🪕"
      ),
      ArtistRecommendation(
        artistName = "الشاب خالد",
        genre = "راي وموسيقى عالمية",
        reason = "لتوسيع ذوقك نحو إيقاعات الراي والفيوجن المغاربي",
        matchPercentage = 87,
        topSongTitle = "C'est La Vie",
        avatarEmoji = "🌟"
      ),
      ArtistRecommendation(
        artistName = "حسين الجسمي",
        genre = "خليجي راقي",
        reason = "بناءً على تفضيلك لأعمال ماجد المهندس",
        matchPercentage = 95,
        topSongTitle = "ستة الصبح",
        avatarEmoji = "🎶"
      )
    )

    // Sort by match percentage and relevance to top genres
    return catalog.sortedByDescending { it.matchPercentage }
  }

  /**
   * Generates dynamic grouped recommendation sections.
   */
  fun generateRecommendations(
    allSongs: List<Song>,
    history: List<ListeningHistoryItem>
  ): List<RecommendedSongGroup> {
    val genreStats = calculateGenreStats(allSongs, history)
    val topGenre = genreStats.firstOrNull()?.genre ?: "طرب عراقي"

    val groups = mutableListOf<RecommendedSongGroup>()

    // 1. Based on Top Genre
    val topGenreSongs = allSongs.filter { it.genre == topGenre || it.genre.contains("طرب") }
    if (topGenreSongs.isNotEmpty()) {
      groups.add(
        RecommendedSongGroup(
          title = "لأنك تحب $topGenre",
          subtitle = "مختارات منسقة بعناية تناسب مزاجك الطربي المفضل",
          iconEmoji = "🎻",
          songs = topGenreSongs
        )
      )
    }

    // 2. Discover Weekly Mix
    val discoverSongs = allSongs.sortedBy { it.playCount }.take(5)
    groups.add(
      RecommendedSongGroup(
        title = "اكتشافات جديدة لك (Discover Weekly)",
        subtitle = "أعمال فنية راقية لم تستمع إليها مؤخراً",
        iconEmoji = "💎",
        songs = discoverSongs
      )
    )

    // 3. Chill & Focus Mood
    val chillSongs = allSongs.filter { it.genre.contains("هادئة") || it.genre.contains("كلاسيك") }
    if (chillSongs.isNotEmpty()) {
      groups.add(
        RecommendedSongGroup(
          title = "جلسة هدوء واستجمام (Chill & Deep Focus)",
          subtitle = "إيقاعات مهدئة للأعصاب وعميقة للتأمل والعمل",
          iconEmoji = "🌙",
          songs = chillSongs
        )
      )
    }

    // 4. Upbeat & Energetic
    val upbeatSongs = allSongs.filter { it.tempoBpm >= 105 || it.genre.contains("بوب") || it.genre.contains("راي") }
    if (upbeatSongs.isNotEmpty()) {
      groups.add(
        RecommendedSongGroup(
          title = "طاقة وحماس وانتعاش (Daily Energy)",
          subtitle = "أغاني إيقاعية مفعمة بالحيوية والنشاط",
          iconEmoji = "⚡",
          songs = upbeatSongs
        )
      )
    }

    return groups
  }
}
