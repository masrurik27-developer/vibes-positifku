plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Tambahkan plugin lain sesuai project kamu
}

// ── Baca version dari Gradle properties (di-inject oleh GitHub Actions) ──
val versionNameProp = project.findProperty("version.name")?.toString() ?: "1.0.0"
val versionCodeProp = project.findProperty("version.code")?.toString()?.toLongOrNull() ?: 1L

android {
    namespace = "com.example.myapp" // ← Ganti dengan package name kamu
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapp" // ← Ganti dengan package name kamu
        minSdk = 24
        targetSdk = 34
        versionCode = versionCodeProp.toInt()
        versionName = versionNameProp
    }

    // ── Signing Config — baca dari environment variable GitHub Actions ──
    signingConfigs {
        create("release") {
            // File keystore di-decode oleh workflow sebelum build
            storeFile = file("release.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD") ?: ""
            keyAlias     = System.getenv("SIGNING_KEY_ALIAS")       ?: ""
            keyPassword  = System.getenv("SIGNING_KEY_PASSWORD")    ?: ""
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true  // Hapus kalau tidak pakai Compose
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // ─────────────────────────────────────────────────────
    // ADS SECTION — Diberi marker ADS_START / ADS_END
    // Jika enable_ads = false, baris ini akan di-comment
    // otomatis oleh GitHub Actions workflow
    // ─────────────────────────────────────────────────────
    // ADS_START
    implementation("com.google.android.gms:play-services-ads:23.3.0")
    // ADS_END

    // ─────────────────────────────────────────────────────
    // BILLING SECTION — Diberi marker BILLING_START / BILLING_END
    // Jika enable_billing = false, baris ini akan di-comment
    // otomatis oleh GitHub Actions workflow
    // ─────────────────────────────────────────────────────
    // BILLING_START
    implementation("com.android.billingclient:billing-ktx:7.1.1")
    // BILLING_END

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
