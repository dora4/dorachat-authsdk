dorachat-authsdk ![Release](https://jitpack.io/v/dora4/dorachat-authsdk.svg)
--------------------------------

#### Gradle Dependency Configuration

```groovy
// Add the following code to the build.gradle file in the project root directory
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

// Add the following code to the build.gradle file in the app module
dependencies {
    implementation 'com.github.dora4:dorachat-authsdk:1.0'
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
```
