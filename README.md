# Media Downloader

Aplikasi Android sumber terbuka untuk mengunduh media (video, audio, dan gambar) dari lebih dari 20 platform media sosial — didukung oleh **miaw-dl**.

## Fitur

- **Mendukung lebih dari 20 platform**: TikTok, Instagram, Twitter/X, YouTube, Facebook, CapCut, Google Drive, Pinterest, Xiaohongshu (Rednote), Douyin, SnackVideo, Cocofun, Spotify, SoundCloud, Threads, Kuaishou, MediaFire, dan lainnya.
- **Deteksi platform otomatis** — cukup tempelkan tautan.
- **Metadata lengkap** — judul, penulis, durasi, jumlah tayangan, thumbnail, dan lainnya.
- **Progres unduhan secara langsung** — bilah progres waktu nyata (0–100%).
- **Pencarian YouTube** — cari video dan dapatkan tautan unduhan MP3/MP4.
- **Mode gelap (Dark Mode).**

## Instalasi

### Aplikasi Android (APK)

**Opsi 1 — Unduh APK**

Unduh APK terbaru dari halaman **Releases** dan instal di perangkat Android Anda.

**Opsi 2 — Bangun dari kode sumber**

```bash
git clone https://github.com/hostinger-bot/miaw-dl.git
cd miaw-dl

# Membangun APK Rilis (ditandatangani secara otomatis dan siap dipasang)
./gradlew :app:assembleRelease
# APK akan dibuat di app/build/outputs/apk/release/app-release.apk

# Membangun APK Debug
./gradlew :app:assembleDebug
# APK akan dibuat di app/build/outputs/apk/debug/app-debug.apk
```

### Pustaka Klien Ktor (Kotlin/JVM)

Tambahkan ke file `build.gradle.kts` Anda:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.miaw:miaw-downloader-ktor:1.0.0")
}
```

## Memulai dengan Cepat

```kotlin
// Membuat klien
val api = MiawDownloader()

// Mengunduh video TikTok
val tiktok = api.ttdl("https://www.tiktok.com/@user/video/123")
println(tiktok.title)

// Mengunduh video YouTube
val youtube = api.youtube("https://youtu.be/abc123")
println(youtube.mp4)

// Mencari video di YouTube
val results = api.yts("cats")
println(results.all)

// Mendapatkan respons JSON mentah
val raw: String = api.raw("instagram", "https://www.instagram.com/p/abc123/")
```

### Konfigurasi Kustom

```kotlin
val api = MiawDownloader(
    MiawConfig(
        baseUrl = "https://backend1.tioo.eu.org",
        timeoutMs = 30_000,
        logLevel = LogLevel.HEADERS,
    )
)
```

## Lisensi

MIT
