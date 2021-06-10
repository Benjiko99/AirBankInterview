import org.gradle.api.JavaVersion

object AppConfig {
    const val compileSdkVersion = 29
    const val minSdkVersion = 21
    const val targetSdkVersion = 29
    const val buildToolsVersion = "30.0.2"

    const val versionCode = 1
    const val versionName = "1.0"

    const val id = "com.spiraclesoftware.androidsample"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    val jvmTarget = JavaVersion.VERSION_11
}

sealed class BuildType {
    companion object {
        const val debug = "debug"
        const val release = "release"
    }
}
