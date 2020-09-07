import com.android.build.gradle.*

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
allprojects {

    repositories {
        google()
        jcenter()
    }
}

subprojects {
    // Accessing the `PluginContainer` in order to use `whenPluginAdded` function
    project.plugins.configure(project = project)
}
// Extension function on `PluginContainer`
fun PluginContainer.configure(project: Project) {
    whenPluginAdded {
        when (this) {
            is AppPlugin -> project.extensions.getByType<AppExtension>().apply {
                applyAppCommons()
            }

            is LibraryPlugin -> project.extensions.getByType<LibraryExtension>().apply {
                applyLibraryCommons()
            }
        }
    }
}

fun AppExtension.applyAppCommons() = apply {
    defaultConfig { applicationId = Const.applicationId }
    applyBaseCommons()

}

fun LibraryExtension.applyLibraryCommons() = apply {
    applyBaseCommons()

    onVariants.withBuildType("debug") {
        androidTest {
            enabled = false
        }
    }
}

fun BaseExtension.applyBaseCommons() = apply {
    compileSdkVersion(Const.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Const.minSdkVersion)
        targetSdkVersion(Const.targetSdkVersion)
        versionCode = Const.versionCode
        versionName = Const.versionName
        testInstrumentationRunner = Const.runner
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}


object Const {
    const val applicationId = "flywith24.android.detail"
    const val compileSdkVersion = 29
    const val buildToolsVersion = "30.0.0"
    const val minSdkVersion = 24
    const val targetSdkVersion = 30
    const val versionCode = 1
    const val versionName = "1.0"
    const val runner = "androidx.test.runner.AndroidJUnitRunner"
    const val implementation = "implementation"
    const val testImplementation = "testImplementation"
    const val androidTestImplementation = "androidTestImplementation"

    object Testing {
        const val jUnit = "junit:junit:4.12"
        const val androidJunit = "androidx.test.ext:junit:1.1.1"
        const val androidRunner = "androidx.test:runner:1.2.0"
        const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
