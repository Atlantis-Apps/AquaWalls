plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") 
    id("com.google.devtools.ksp") version "1.9.25-1.0.20"
}

android {
    namespace = "com.atlantis.aquawalls"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.atlantis.aquawalls"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

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
    // ✅ Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.09.01")
    implementation(composeBom)

    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3:1.3.1")

    // ✅ Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // ✅ Coil image loader
    implementation("io.coil-kt:coil-compose:2.6.0")

    // ✅ Material icons
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // ✅ Room database
    val room = "2.6.1"
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    ksp("androidx.room:room-compiler:$room")

    // ✅ ViewModel + LiveData for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // ✅ (Optional) Material Components for XML splash / system UI
    implementation("com.google.android.material:material:1.12.0")
    
    //FireBase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
}
