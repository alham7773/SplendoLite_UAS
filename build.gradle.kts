plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Tambahkan baris ini tepat di bawah alias, jangan di dalam blok baru
    id("com.google.gms.google-services") version "4.4.2" apply false
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
