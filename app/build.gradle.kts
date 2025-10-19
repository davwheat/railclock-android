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
        ).seconds / 86400) * 49.0 + 49.0
    val twoDigitSuffix = seconds.toInt()

    return Integer.parseInt(df.format(Date()) + String.format("%02d", twoDigitSuffix))
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "dev.davwheat.railclock"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "dev.davwheat.railclock"
        minSdk = 31
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "17"
        // https://github.com/Kotlin/kotlinx.serialization/issues/2145
        // https://issuetracker.google.com/issues/250197571#comment28
        freeCompilerArgs = listOf("-Xstring-concat=inline", "-Xjvm-default=all-compatibility")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.ui:ui-viewbinding")
}
