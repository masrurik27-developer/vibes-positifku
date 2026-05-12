# 🚀 Android GitHub Actions — Build APK & AAB Otomatis

Workflow ini membangun project Android Studio menjadi **APK** dan **AAB**
langsung di GitHub, tanpa perlu Android Studio di komputer.

---

## ✨ Fitur Workflow

| Fitur | Keterangan |
|-------|-----------|
| 📱 Build Debug APK | Untuk testing internal |
| 📦 Build Release APK | Signed, siap distribusi langsung |
| 🏪 Build Release AAB | Signed, siap upload ke Play Store |
| 🎯 Toggle Google Ads | ON/OFF tanpa edit kode manual |
| 💳 Toggle Billing | ON/OFF tanpa edit kode manual |
| 🏷️ Auto version code | Dari timestamp otomatis |
| 📋 Build summary | Ringkasan hasil build di GitHub |

---

## 📁 Struktur File yang Perlu Ada

```
project-kamu/
├── .github/
│   └── workflows/
│       └── build-android.yml    ← File workflow utama
├── app/
│   ├── build.gradle.kts         ← Sudah dikonfigurasi untuk signing
│   ├── google-services.json     ← JANGAN commit! Simpan di Secrets
│   └── src/
│       └── main/java/.../
│           └── MainActivity.kt  ← Pakai marker ADS_START/END & BILLING_START/END
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew                      ← Wajib ada!
├── gradle/wrapper/
│   ├── gradle-wrapper.jar       ← Wajib ada!
│   └── gradle-wrapper.properties
└── .gitignore                   ← Pastikan *.jks & google-services.json di sini
```

---

## ⚙️ Cara Pakai Marker ADS & BILLING di Kode

Tambahkan komentar marker di file Kotlin/Java dan build.gradle kamu:

### Di Kotlin / Java:
```kotlin
// ADS_START
import com.google.android.gms.ads.MobileAds
// ADS_END

// Semua kode antara ADS_START dan ADS_END
// akan di-comment jika enable_ads = false
```

### Di build.gradle.kts:
```kotlin
// ADS_START
implementation("com.google.android.gms:play-services-ads:23.3.0")
// ADS_END

// BILLING_START
implementation("com.android.billingclient:billing-ktx:7.1.1")
// BILLING_END
```

---

## 🔐 Secrets yang Harus Disiapkan

Buka GitHub repo → **Settings → Secrets and variables → Actions**

| Secret Name | Cara Mendapatkan |
|-------------|-----------------|
| `GOOGLE_SERVICES_JSON` | Isi file google-services.json (1 baris, minified) |
| `KEYSTORE_BASE64` | File .jks di-encode base64 |
| `SIGNING_KEY_ALIAS` | Alias keystore (contoh: `my-key`) |
| `SIGNING_KEY_PASSWORD` | Password key |
| `SIGNING_STORE_PASSWORD` | Password store |

### Cara encode keystore ke base64:
```bash
# Mac / Linux
base64 -i release.jks | tr -d '\n'

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.jks"))
```

### Cara minify google-services.json (jadi 1 baris):
```bash
# Mac / Linux
cat google-services.json | python3 -c "import sys,json; print(json.dumps(json.load(sys.stdin)))"

# Atau copy isi file ke: jsonminifier.net → klik Minify
```

---

## 🚀 Cara Menjalankan Workflow

1. Buka repo di GitHub
2. Klik tab **"Actions"**
3. Pilih **"🚀 Build Android APK & AAB"** di menu kiri
4. Klik tombol **"Run workflow"** (pojok kanan)
5. Isi pilihan:
   - **enable_ads**: `true` atau `false`
   - **enable_billing**: `true` atau `false`
   - **version_name**: contoh `1.0.0`
   - **build_notes**: catatan opsional
6. Klik **"Run workflow"** warna hijau
7. Tunggu 5–15 menit
8. Setelah selesai → klik nama workflow → scroll ke bawah → **Artifacts**
9. Download file APK atau AAB

---

## 📥 Hasil Build (Artifacts)

Setelah build selesai, file bisa didownload dari tab Actions:

| Artifact | Isi | Kegunaan |
|----------|-----|----------|
| `release-APK-v1.0.0-adsfalse-billingfalse` | `.apk` signed | Install langsung di HP |
| `release-AAB-v1.0.0-adsfalse-billingfalse` | `.aab` signed | Upload ke Play Store |
| `debug-apk-v1.0.0` | `.apk` debug | Testing internal |

---

## 📤 Cara Upload Project ke GitHub (Pertama Kali)

### Langkah 1 — Install Git
- **Windows**: download dari **git-scm.com** → install
- **Mac**: buka Terminal → ketik `git --version` → install jika diminta

### Langkah 2 — Buat Repository di GitHub
1. Login ke **github.com**
2. Klik **"+"** → **"New repository"**
3. Isi nama repo, pilih **Private**
4. Jangan centang "Add README"
5. Klik **"Create repository"**

### Langkah 3 — Upload dari Android Studio
**Cara termudah lewat Android Studio:**
1. Buka project di Android Studio
2. Menu **VCS → Enable Version Control Integration → Git**
3. Menu **Git → GitHub → Share Project on GitHub**
4. Login GitHub → isi nama repo → klik **Share**
5. ✅ Selesai! Semua file terupload

**Atau lewat Terminal:**
```bash
# Masuk ke folder project
cd /path/ke/project-kamu

# Inisialisasi git
git init

# Tambah semua file (kecuali yang di .gitignore)
git add .

# Beri pesan commit
git commit -m "Upload project pertama kali"

# Hubungkan ke GitHub (ganti URL dengan milikmu)
git remote add origin https://github.com/USERNAME/NAMA-REPO.git

# Upload!
git push -u origin main
```

### Langkah 4 — Tambah File Workflow
Salin file `.github/workflows/build-android.yml` ini ke dalam folder project kamu, lalu:
```bash
git add .github/
git commit -m "Tambah GitHub Actions workflow"
git push
```

### Langkah 5 — Tambah Secrets
Lihat tabel Secrets di atas → isi semua di GitHub repo Settings

### Langkah 6 — Jalankan!
Ikuti langkah "Cara Menjalankan Workflow" di atas.

---

## ❓ Troubleshooting

### Build gagal: "google-services.json not found"
→ Pastikan secret `GOOGLE_SERVICES_JSON` sudah diisi dengan benar (harus 1 baris JSON valid)

### Build gagal: "Keystore was tampered / incorrect password"
→ Cek secret `SIGNING_STORE_PASSWORD` dan `SIGNING_KEY_PASSWORD` sudah benar

### Build gagal: "gradlew: Permission denied"
→ Jalankan sekali di lokal: `git update-index --chmod=+x gradlew` lalu push ulang

### APK tidak tersigned
→ Pastikan semua 4 secret signing sudah diisi (`KEYSTORE_BASE64`, `SIGNING_KEY_ALIAS`, `SIGNING_KEY_PASSWORD`, `SIGNING_STORE_PASSWORD`)

### Ads/Billing tidak ter-comment
→ Pastikan kode kamu sudah pakai marker `// ADS_START` dan `// ADS_END` persis seperti contoh
