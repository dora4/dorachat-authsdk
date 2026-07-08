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
```
```kt
// Add data binding support
android {
    buildFeatures {
        dataBinding = true
    }
}
// Add the following code to the build.gradle file in the app module
dependencies {
    implementation("com.github.dora4:dorachat-authsdk:1.1.3")
    implementation("com.github.dora4:dora:1.3.68")
    implementation("com.github.dora4:dora-arouter-support:1.11")
    kapt("com.alibaba:arouter-compiler:1.5.2")
    implementation("com.github.dora4:dcache-android:3.6.16")
    implementation("com.github.dora4:dview-loading-dialog:1.7")
    implementation("com.github.dora4:dora-walletconnect-support:2.1.37") {
        exclude(group = "com.madgag.spongycastle", module = "core")
    }
}
```
#### Automatic Injection of Lifecycle Callback Functions
```xml
<application>
    <meta-data
        android:name="dora.lifecycle.config.ARouterGlobalConfig"
        android:value="GlobalConfig" />
</application>
```
#### Initialize a series of components in the Application
```kt
    private fun initHttp() {
        RetrofitManager.initConfig {
            okhttp {
                // ...
                flow(true) // Important
                addInterceptor(AuthInterceptor())
                build()
            }
            // ...
            mappingBaseUrl(AuthService::class.java, AppConfig.AUTH_SDK_SERVER_URL)
        }
    }
```
```kt
    private fun initAuth() {
        val config = DoraChatConfig.Builder(
            apiBaseUrl = AppConfig.AUTH_SDK_SERVER_URL,
            partitionId = "petwords",
            appName = "Pet Words",
            themeColor = ContextCompat.getColor(this, R.color.primary)
        )
            .enableLog(true)
            .showBrand(true)
            .loadAccessToken(false)
            .autoRefreshToken(true)
            .build()
        DoraChatSDK.init(this, config)
    }
```
```kt
     private fun initPay() {
        DoraFund.init(this, APP_NAME,
            getString(R.string.app_desc), URL_DOMAIN,
            arrayOf(EVMChains.ETHEREUM, EVMChains.POLYGON, EVMChains.AVALANCHE, EVMChains.BSC), themeColor,
            object : DoraFund.PayListener {
                override fun onPayFailure(orderId: String, msg: String) {
                }

                override fun onSendTransactionToBlockchain(
                    orderId: String,
                    transactionHash: String
                ) {
                }
            })
    }
```
#### ProGuard/R8
```
-keepclassmembers class com.dorachat.auth.ReqBody {
    <fields>;
}
-keep class com.dorachat.auth.ApiResult {
    <fields>;
}
-keep class com.dorachat.auth.DoraUser {
    <fields>;
}
-keep class com.dorachat.auth.DoraUserInfo {
    <fields>;
}
-keepclassmembers class com.dorachat.auth.BaseReq {
    <fields>;
}
-keep class * extends com.dorachat.auth.BaseReq {
    <fields>;
}
-keep class com.dorachat.auth.SignInEvent {
    *;
}
-keep class com.dorachat.auth.SignOutEvent {
    *;
}
```
