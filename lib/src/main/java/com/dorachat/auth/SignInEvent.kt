package com.dorachat.auth

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class SignInEvent(
    @SerializedName("erc20") val erc20: String)
