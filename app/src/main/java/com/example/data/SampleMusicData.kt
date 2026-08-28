package com.example.data

import com.example.model.LyricLine
import com.example.model.Playlist
import com.example.model.Song

object SampleMusicData {
  val songs = listOf(
    Song(
      id = "song_1",
      title = "سر الوجود",
      artist = "أصيل هميم",
      album = "أغاني مختارة",
      durationMs = 252000L, // 04:12
      genre = "طرب عراقي",
      isFavorite = true,
      tempoBpm = 95,
      baseFrequencies = listOf(220.00f, 261.63f, 329.63f, 392.00f, 440.00f),
      lyrics = listOf(
        LyricLine(0L, "أنت أول حبيب وأنت آخر غرام"),
        LyricLine(12000L, "من أشوفك أضيع وما أعرف الكلام"),
        LyricLine(28000L, "حبك مثل الهوا لا أعيش بدونه"),
        LyricLine(45000L, "يا كل العمر وسر الوجود وعيونه"),
        LyricLine(62000L, "تدري عيوني ما تنام إلا بوجودك"),
        LyricLine(80000L, "عاشق طيبتك وبداية كل حدودك"),
        LyricLine(100000L, "أنت النبض في القلب وأنت الأمان"),
        LyricLine(125000L, "مالي بهالدنيا سواك بأي زمان"),
        LyricLine(160000L, "يا سر الوجود وكل الحنان")
      )
    ),
    Song(
      id = "song_2",
      title = "نسم علينا الهوى",
      artist = "فيروز",
      album = "قصائد وألحان الرحباني",
      durationMs = 210000L, // 03:30
      genre = "كلاسيك طربي",
      isFavorite = true,
      tempoBpm = 100,
      baseFrequencies = listOf(261.63f, 293.66f, 329.63f, 349.23f, 392.00f),
      lyrics = listOf(
        LyricLine(0L, "نسم علينا الهوى من مفرق الوادي"),
        LyricLine(15000L, "يا هوى دخل الهوى خدني على بلادي"),
        LyricLine(35000L, "يا هوى يا هوى يللي طاير بالهوا"),
        LyricLine(55000L, "في منتورة طاقة وباب ناطرين الأحباب"),
        LyricLine(78000L, "يا هوى دخل الهوى خدني على بلادي")
      )
    ),
    Song(
      id = "song_3",
      title = "تملي معاك",
      artist = "عمرو دياب",
      album = "تملي معاك",
      durationMs = 270000L, // 04:30
      genre = "بوب رومانسي",
      isFavorite = false,
      tempoBpm = 115,
      baseFrequencies = listOf(196.00f, 246.94f, 293.66f, 369.99f, 440.00f),
      lyrics = listOf(
        LyricLine(0L, "تملي معاك ولو حتى بعيد عني في قلبي هواك"),
        LyricLine(20000L, "تملي معاك تملي في بالي وفي قلبي ولا بنساك"),
        LyricLine(42000L, "تملي واحشني لو حتى بكون وياك"),
        LyricLine(65000L, "وحبيبي كل حياتي معاك ومعاك الأيام بتحلى"),
        LyricLine(90000L, "وعيوني تعشق نظرة رضاك")
      )
    ),
    Song(
      id = "song_4",
      title = "أنا وليلى",
      artist = "كاظم الساهر",
      album = "حبيبتي والمطر",
      durationMs = 340000L, // 05:40
      genre = "قصائد وطرب",
      isFavorite = true,
      tempoBpm = 85,
      baseFrequencies = listOf(174.61f, 220.00f, 261.63f, 329.63f, 392.00f),
      lyrics = listOf(
        LyricLine(0L, "ماتت بمحراب عينيكِ ابتهالاتي"),
        LyricLine(22000L, "واستسلمت لرياح اليأس راياتي"),
        LyricLine(48000L, "جفت على شفتي المكسور أغنيتي"),
        LyricLine(75000L, "وجئت أرجوك أن تعفو عن الشاكي")
      )
    ),
    Song(
      id = "song_5",
      title = "تناديك",
      artist = "ماجد المهندس",
      album = "أغاني منفردة",
      durationMs = 285000L, // 04:45
      genre = "خليجي راقي",
      isFavorite = false,
      tempoBpm = 105,
      baseFrequencies = listOf(220.00f, 277.18f, 329.63f, 415.30f, 440.00f),
      lyrics = listOf(
        LyricLine(0L, "تناديك رغبة بالمحبة وتستعطفك"),
        LyricLine(18000L, "وتدعيك عيني باهتمام وتراعيك"),
        LyricLine(40000L, "أحبك وكل ما فيني يبيك ويعشقك"),
        LyricLine(65000L, "ولا عاد باقي شي فيني ما يناديك")
      )
    ),
    Song(
      id = "song_6",
      title = "تقوى الهجر",
      artist = "خالد عبد الرحمن",
      album = "جلسات طربية",
      durationMs = 310000L, // 05:10
      genre = "عود وجلسات",
      isFavorite = false,
      tempoBpm = 90,
      baseFrequencies = listOf(196.00f, 220.00f, 261.63f, 293.66f, 349.23f),
      lyrics = listOf(
        LyricLine(0L, "تقوى الهجر ما قوى الهجر قلبي"),
        LyricLine(24000L, "لا تظن إني بعد فرقاك مرتاح"),
        LyricLine(50000L, "حبك سكن روحي وساري في دمي"),
        LyricLine(80000L, "والشوق في غيبتك لا زاد ما راح")
      )
    ),
    Song(
      id = "song_7",
      title = "Midnight Horizon (موسيقى هادئة)",
      artist = "Lofi Vibes & Oud",
      album = "Chill Beats Arabesque",
      durationMs = 195000L, // 03:15
      genre = "موسيقى هادئة وتركيز",
      isFavorite = true,
      playCount = 14,
      tempoBpm = 80,
      baseFrequencies = listOf(146.83f, 220.00f, 261.63f, 329.63f, 440.00f),
      lyrics = listOf(
        LyricLine(0L, "♪ ألحان هادئة للاسترخاء والتركيز ♪"),
        LyricLine(30000L, "♪ انسجام النغمات الشرقية والحديثة ♪"),
        LyricLine(90000L, "♪ إيقاعات عميقة وراحة ذهنية ♪")
      )
    ),
    Song(
      id = "song_8",
      title = "أهواك",
      artist = "عبد الحليم حافظ",
      album = "روائع العندليب",
      durationMs = 260000L,
      genre = "كلاسيك طربي",
      isFavorite = false,
      playCount = 18,
      tempoBpm = 92,
      baseFrequencies = listOf(220.00f, 277.18f, 329.63f, 415.30f, 493.88f),
      lyrics = listOf(
        LyricLine(0L, "أهواك وأتمنى لو أنساك"),
        LyricLine(20000L, "وأنسى روحي وياك وإن ضاعت تبقى فداك"),
        LyricLine(50000L, "لو كان الحب باختياري ما كنت عشقتك يالغالي")
      )
    ),
    Song(
      id = "song_9",
      title = "مشاعر",
      artist = "شيرين",
      album = "أغاني الدراما",
      durationMs = 240000L,
      genre = "بوب رومانسي",
      isFavorite = true,
      playCount = 22,
      tempoBpm = 88,
      baseFrequencies = listOf(196.00f, 246.94f, 293.66f, 349.23f, 440.00f),
      lyrics = listOf(
        LyricLine(0L, "مشاعر تشاور تودع تسافر"),
        LyricLine(18000L, "مشاعر تموت وتحيا مشاعر"),
        LyricLine(42000L, "يادي المشاعر اللي بتلعب بينا")
      )
    ),
    Song(
      id = "song_10",
      title = "ستة الصبح",
      artist = "حسين الجسمي",
      album = "أغاني سينجل",
      durationMs = 215000L,
      genre = "خليجي راقي",
      isFavorite = false,
      playCount = 9,
      tempoBpm = 120,
      baseFrequencies = listOf(261.63f, 329.63f, 392.00f, 523.25f, 587.33f),
      lyrics = listOf(
        LyricLine(0L, "الصبح تضحك عيونك وتشرق الدنيا"),
        LyricLine(20000L, "يا أغلى ما شافت عيوني بهالكون كله")
      )
    ),
    Song(
      id = "song_11",
      title = "C'est La Vie",
      artist = "الشاب خالد",
      album = "C'est La Vie",
      durationMs = 230000L,
      genre = "راي وموسيقى عالمية",
      isFavorite = false,
      playCount = 11,
      tempoBpm = 125,
      baseFrequencies = listOf(246.94f, 293.66f, 369.99f, 440.00f, 493.88f),
      lyrics = listOf(
        LyricLine(0L, "On va s'aimer, on va danser"),
        LyricLine(15000L, "Oui c'est la vie, lala lalala")
      )
    ),
    Song(
      id = "song_12",
      title = "أندلسيات العود والبيانو",
      artist = "ثنائي المقام الأندلسي",
      album = "رحلة في قرطبة",
      durationMs = 280000L,
      genre = "موسيقى هادئة وتركيز",
      isFavorite = true,
      playCount = 16,
      tempoBpm = 85,
      baseFrequencies = listOf(174.61f, 220.00f, 261.63f, 329.63f, 392.00f),
      lyrics = listOf(
        LyricLine(0L, "♪ صدى الموشحات الأندلسية على أوتار العود ♪"),
        LyricLine(40000L, "♪ تناغم ساحر بين الشرق والغرب ♪")
      )
    )
  )

  val initialPlaylists = listOf(
    Playlist(
      id = "pl_favorites",
      name = "الأغاني المفضلة",
      description = "الأغاني التي نالت إعجابك وتكرر الاستماع إليها",
      songIds = listOf("song_1", "song_2", "song_4", "song_7", "song_9", "song_12"),
      iconEmoji = "❤️",
      isCustom = false
    ),
    Playlist(
      id = "pl_tarab",
      name = "جلسات وسلطنة",
      description = "أروع المختارات الطربية والأصوات الذهبية الأصيلة",
      songIds = listOf("song_1", "song_4", "song_6", "song_8"),
      iconEmoji = "🎻",
      isCustom = false
    ),
    Playlist(
      id = "pl_chill",
      name = "استرخاء وهدوء",
      description = "أنغام هادئة للدراسة والتركيز والتأمل",
      songIds = listOf("song_2", "song_7", "song_12"),
      iconEmoji = "🌙",
      isCustom = false
    ),
    Playlist(
      id = "pl_pop",
      name = "أجواء حماسية وتوب",
      description = "أشهر الأغاني الإيقاعية والعصرية",
      songIds = listOf("song_3", "song_5", "song_10", "song_11"),
      iconEmoji = "🔥",
      isCustom = false
    )
  )

  val genres = listOf(
    "الكل",
    "طرب عراقي",
    "كلاسيك طربي",
    "بوب رومانسي",
    "قصائد وطرب",
    "خليجي راقي",
    "عود وجلسات",
    "موسيقى هادئة وتركيز",
    "راي وموسيقى عالمية"
  )
}
