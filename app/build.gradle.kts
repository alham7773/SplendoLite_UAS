plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") // Tambahkan baris ini
}


android {
    namespace = "com.example.splendolite"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.splendolite"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

        // Firebase BoM (Bill of Materials) - Mengatur versi semua library Firebase agar cocok
        implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

        // Library Firebase yang diperlukan
        implementation("com.google.firebase:firebase-database-ktx")
        implementation("com.google.firebase:firebase-auth-ktx")

        // WAJIB: Tambahkan ini untuk Google Sign-In (Metode 2)
        // Kita gunakan versi direct agar tidak bentrok dengan Version Catalog kamu
        implementation("com.google.android.gms:play-services-auth:21.2.0")

        // Library bawaan Android (Version Catalog)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)

        // Testing
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }