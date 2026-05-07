package com.dorachat.auth

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DoraUser(
    @SerializedName("erc20")
    val erc20: String,
    @SerializedName("latestSignIn")
    val latestSignIn: Long,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
) {

    constructor(erc20: String, latestSignIn: Long) : this(
        erc20 = erc20,
        latestSignIn = latestSignIn,
        accessToken = "",
        refreshToken = ""
    )
}
