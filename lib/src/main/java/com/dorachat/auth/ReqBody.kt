package com.dorachat.auth

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Keep
internal data class ReqBody(
    @SerializedName("mode")
    val mode: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("data")
    val data: String = "",
    @SerializedName("sign")
    val sign: String = "",
) {

    fun toRequestBody() : RequestBody {
        return Gson().toJson(this).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}