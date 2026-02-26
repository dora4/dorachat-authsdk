plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    namespace = "com.dorachat.auth"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.github.dora4:dora:1.3.57")
    implementation("com.github.dora4:dora-arouter-support:1.6")
    implementation("com.alibaba:arouter-api:1.5.2")
    kapt("com.alibaba:arouter-compiler:1.5.2")
    implementation("com.github.dora4:dcache-android:3.6.3")
    implementation("com.github.dora4:dview-loading-dialog:1.5")
    implementation("com.github.dora4:dora-walletconnect-support:2.1.34") {
        exclude(group = "com.madgag.spongycastle", module = "core")
    }
}

kapt {
    generateStubs = true
    correctErrorTypes = true
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

kotlin {
    jvmToolchain(17)
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["release"])
                groupId = "com.github.dora4"
                artifactId = rootProject.project.name
                version = "1.0"
            }
        }
    }
}