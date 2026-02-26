package com.dorachat.auth

import com.google.gson.Gson
import dora.util.ToastUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class AuthInterceptor : Interceptor {

    private val refreshLock = ReentrantLock()

    private fun shouldRefresh(
        response: Response,
        request: Request
    ): Boolean {
        val config = DoraChatSDK.getConfig()
        if (config?.autoRefreshToken != true) return false
        if (response.code != 401) return false
        val path = request.url.encodedPath
        if (path.contains("/auth/refresh")) return false
        if (TokenStore.refreshToken().isNullOrEmpty()) return false
        return true
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = TokenStore.accessToken()
        if (!accessToken.isNullOrEmpty()) {
            request = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }
        val response = chain.proceed(request)
        if (response.code == 401) {
            if (!shouldRefresh(response, request)) {
                return signOut(request)
            }
            response.close()
            refreshLock.withLock {
                val latest = TokenStore.accessToken()
                if (!latest.isNullOrEmpty() && request.header("Authorization") != "Bearer $latest") {
                    request = request.newBuilder()
                        .header("Authorization", "Bearer $latest")
                        .build()
                    return chain.proceed(request)
                }
                val refreshToken = TokenStore.refreshToken() ?: return signOut(request)
                val newAccess = refreshAccessToken(refreshToken) ?: return signOut(request)
                request = request.newBuilder()
                    .header("Authorization", "Bearer $newAccess")
                    .build()
                return chain.proceed(request)
            }
        }
        return response
    }

    private fun refreshAccessToken(refreshToken: String): String? {
        return try {
            val req = ReqToken(refreshToken)
            val body = SecureRequestBuilder.build(req, SecureRequestBuilder.SecureMode.ENC)
            val json = Gson().toJson(body)
            val requestBody = json.toRequestBody("application/json".toMediaType())
            val baseUrl = DoraChatSDK.getConfig()?.apiBaseUrl
            val request = Request.Builder()
                .url("${baseUrl}auth/refresh")
                .post(requestBody)
                .build()
            val client = OkHttpClient()
            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val str = resp.body?.string() ?: return null
                val root = JSONObject(str)
                val code = root.optString("code")
                val msg = root.optString("msg")
                if (code != ApiCode.SUCCESS) {
                    if (code == ApiCode.ERROR_SIGN_IN_EXPIRED) {
                        ToastUtils.showLong(msg)
                        signOut(request)
                    }
                    return null
                }
                val data = root.optJSONObject("data") ?: return null
                val newAccessToken = data.optString("accessToken")
                val newRefreshToken = data.optString("refreshToken")
                if (newAccessToken.isNullOrEmpty() || newRefreshToken.isNullOrEmpty()) return null
                TokenStore.save(newAccessToken, newRefreshToken)
                newAccessToken
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun signOut(request: Request): Response {
        TokenStore.clear()
        SignInExpiredBus.postOnce()
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("Token expired")
            .body("{}".toResponseBody())
            .build()
    }
}
