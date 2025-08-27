import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.2.0"
}

val secretsFile = rootProject.file("secrets.properties")
val secrets = Properties().apply {
    if (secretsFile.exists()) {
        secretsFile.inputStream().reader(Charsets.UTF_8).use { reader ->
            load(reader)
        }
    }
}

fun String.escapeForBuildConfig() = this.replace("\"", "\\\"")

android {
    android.buildFeatures.buildConfig = true
    namespace = "com.cdkentertainment.mobilny_usos_enhanced"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cdkentertainment.mobilny_usos_enhanced"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val keyPart1 = "ключчасть"
        val keyPart2 = "частьключа"
        val keyPart3 = "ключевойэлементключа"
        val keyPart4 = "пампарамбам"
        val keyPart5 = "приветмир"
        val keySecretPart1 = "секретныйсекрет"
        val keySecretPart2 = "абсолютныйключ"
        val keySecretPart3 = "ключевойабсолют"
        val keySecretPart4 = "покадорогоймир"
        val keySecretPart5 = "каждаяисторияимеетсвойконец"

        val part1 = secrets.getProperty(keyPart1) ?: ""
        val part2 = secrets.getProperty(keyPart2) ?: ""
        val part3 = secrets.getProperty(keyPart3) ?: ""
        val part4 = secrets.getProperty(keyPart4) ?: ""
        val part5 = secrets.getProperty(keyPart5) ?: ""
        val secretPart1 = secrets.getProperty(keySecretPart1) ?: ""
        val secretPart2 = secrets.getProperty(keySecretPart2) ?: ""
        val secretPart3 = secrets.getProperty(keySecretPart3) ?: ""
        val secretPart4 = secrets.getProperty(keySecretPart4) ?: ""
        val secretPart5 = secrets.getProperty(keySecretPart5) ?: ""
        val testAccessToken = secrets.getProperty("testAccessToken") ?: ""
        val testAccessSecret = secrets.getProperty("testAccessSecret") ?: ""

        // inject into BuildConfig (note the escaped quotes)
        buildConfigField("String", keyPart1, "\"${part1.escapeForBuildConfig()}\"")
        buildConfigField("String", keyPart2, "\"${part2.escapeForBuildConfig()}\"")
        buildConfigField("String", keyPart3, "\"${part3.escapeForBuildConfig()}\"")
        buildConfigField("String", keyPart4, "\"${part4.escapeForBuildConfig()}\"")
        buildConfigField("String", keyPart5, "\"${part5.escapeForBuildConfig()}\"")
        buildConfigField("String", keySecretPart1, "\"${secretPart1.escapeForBuildConfig()}\"")
        buildConfigField("String", keySecretPart2, "\"${secretPart2.escapeForBuildConfig()}\"")
        buildConfigField("String", keySecretPart3, "\"${secretPart3.escapeForBuildConfig()}\"")
        buildConfigField("String", keySecretPart4, "\"${secretPart4.escapeForBuildConfig()}\"")
        buildConfigField("String", keySecretPart5, "\"${secretPart5.escapeForBuildConfig()}\"")
        buildConfigField("String", "testAccessToken", "\"${testAccessToken.escapeForBuildConfig()}\"")
        buildConfigField("String", "testAccessSecret", "\"${testAccessSecret.escapeForBuildConfig()}\"")

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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation("io.ktor:ktor-client-android:3.2.2")
    implementation(platform("io.github.jan-tennert.supabase:bom:3.2.2"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation(libs.vico.compose)
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.1.7")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.1.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("androidx.compose.ui:ui:latest_version")
    implementation("androidx.compose.animation:animation:latest_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")
    implementation("com.github.scribejava:scribejava-apis:8.3.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.datastore.preferences.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}