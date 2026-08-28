package com.example.model

enum class Mp3AudioQuality(
  val label: String,
  val bitrate: String,
  val estimatedSize: String,
  val formatDescription: String
) {
  HQ_320("جودة صوت فائقة (Ultra HQ)", "320 kbps", "9.8 MB", "صيغة MP3 أصلية بنقاء صوت كريستالي"),
  HQ_256("جودة صوت عالية (HQ)", "256 kbps", "7.4 MB", "صيغة MP3 قياسية بتوازن ممتاز"),
  HQ_128("جودة اقتصادية (Standard)", "128 kbps", "4.1 MB", "صيغة MP3 خفيفة وسريعة التنزيل")
}

enum class DownloadStatus {
  QUEUED,
  DOWNLOADING,
  CONVERTING_TO_MP3,
  COMPLETED,
  FAILED
}

data class YouTubeMusicVideo(
  val id: String,
  val title: String,
  val channelTitle: String,
  val viewCount: String,
  val publishedTime: String,
  val duration: String,
  val durationMs: Long,
  val category: String,
  val gradientTheme: List<Long>,
  val isDownloaded: Boolean = false,
  val matchedSongId: String? = null
)

data class DownloadTask(
  val id: String,
  val video: YouTubeMusicVideo,
  val quality: Mp3AudioQuality,
  val progress: Float = 0f,
  val status: DownloadStatus = DownloadStatus.QUEUED,
  val downloadedSizeMb: Float = 0f,
  val totalSizeMb: Float = 8.5f,
  val downloadedSongId: String? = null
)
