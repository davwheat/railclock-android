import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.Date
import java.util.Properties

val versionMajor = 0
val versionMinor = 1
val versionPatch = 0

fun getBuildNumber(): Int {
    val df = SimpleDateFormat("yyyyMMdd")
    val date = LocalDateTime.now()
    val seconds =
        (Duration.between(
            date.withSecond(0).withMinute(0).withHour(0),
            date
        ).seconds / 86400) * 49.0
    val twoDigitSuffix = seconds.toInt()

    return Integer.parseInt(df.format(Date()) + String.format("%02d", twoDigitSuffix))
}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "dev.davwheat.railclock"
    compileSdk {
        version = release(34)
    }

    defaultConfig {
        applicationId = "dev.davwheat.railclock"
        minSdk = 34
        targetSdk = 36
        versionCode = getBuildNumber()
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
    }

    signingConfigs {
        create("release") {
            val properties = Properties()
            properties.load(FileInputStream(project.rootProject.file("local.properties")))

            storeFile = file(properties.getProperty("signing.storeFilePath"))
            storePassword = properties.getProperty("signing.storePassword")
            keyAlias = properties.getProperty("signing.keyAlias")
            keyPassword = properties.getProperty("signing.keyPassword")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
