plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.atlantis.aquawalls"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.atlantis.aquawalls"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
   
    // Force Java 17 for both Kotlin + Java
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Jetpack Compose BOM (keeps versions aligned)
    val composeBom = platform("androidx.compose:compose-bom:2024.09.01")
    implementation(composeBom)

    // Core Compose
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Material Design 3 (Compose)
    implementation("androidx.compose.material3:material3:1.3.0")

    // AndroidX Navigation (for preview screen)
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Image loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Retrofit for fetching wallpapers
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Optional: classic Material library (for XML themes)
    implementation("com.google.android.material:material:1.12.0")
}
