package com.dorachat.auth

import androidx.annotation.ColorInt

class DoraChatConfig private constructor(
    val apiBaseUrl: String,
    val partitionId: String,
    val appName: String,
    @ColorInt val themeColor: Int,
    val enableLog: Boolean,
    val showBrand: Boolean,
    val loadAccessToken: Boolean,
    val autoRefreshToken: Boolean
) {

    class Builder(
        private val apiBaseUrl: String,
        private val partitionId: String,
        private val appName: String,
        @ColorInt private val themeColor: Int
    ) {

        private var enableLog: Boolean = false
        private var showBrand: Boolean = true
        private var loadAccessToken: Boolean = false
        private var autoRefreshToken: Boolean = true

        fun enableLog(enable: Boolean) = apply {
            this.enableLog = enable
        }

        fun showBrand(enable: Boolean) = apply {
            this.showBrand = enable
        }

        fun loadAccessToken(enable: Boolean) = apply {
            this.loadAccessToken = enable
        }

        fun autoRefreshToken(enable: Boolean) = apply {
            this.autoRefreshToken = enable
        }

        fun build(): DoraChatConfig {
            require(apiBaseUrl.isNotBlank()) { "apiBaseUrl cannot be blank" }
            require(partitionId.isNotBlank()) { "partitionId cannot be blank" }
            require(appName.isNotBlank()) { "appName cannot be blank" }

            return DoraChatConfig(
                apiBaseUrl = apiBaseUrl,
                partitionId = partitionId,
                appName = appName,
                themeColor = themeColor,
                enableLog = enableLog,
                showBrand = showBrand,
                loadAccessToken = loadAccessToken,
                autoRefreshToken = autoRefreshToken
            )
        }
    }
}
