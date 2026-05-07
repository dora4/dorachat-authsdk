package com.dorachat.auth

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import dora.cache.data.adapter.Result

@Keep
class ApiResult<T> : Result<T> {

    @SerializedName("code")
    var code: String? = null
    @SerializedName("msg")
    var msg: String? = null
    @SerializedName("data")
    var data: T? = null
    @SerializedName("timestamp")
    val timestamp = System.currentTimeMillis()

    override fun getRealModel(): T? {
        return data
    }
}