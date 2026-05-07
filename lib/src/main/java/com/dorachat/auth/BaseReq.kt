package com.dorachat.auth

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dora.util.GlobalContext
import dora.util.LanguageUtils
import java.lang.reflect.Modifier
import java.util.Locale

@Keep
abstract class BaseReq {

    @SerializedName("lang")
    var lang: String = ""

    @SerializedName("payload")
    var payload: String = ""

    @SerializedName("timestamp")
    var timestamp: String = ""

    init {
        lang = LanguageUtils.getLangTag(GlobalContext.get()).ifEmpty { Locale.getDefault().language }
        timestamp = (System.currentTimeMillis() / 1000).toString()
    }

    fun sort(): String {
        val map = sortedMapOf<String, Any?>()
        var clazz: Class<*>? = this.javaClass
        while (clazz != null && clazz != BaseReq::class.java) {
            clazz.declaredFields
                .filter { field ->
                    !field.isSynthetic &&
                            !Modifier.isStatic(field.modifiers)
                }
                .forEach { field ->
                    field.isAccessible = true
                    map[field.name] = field[this]
                }
            clazz = clazz.superclass
        }
        map["lang"] = lang
        map["payload"] = payload
        map["timestamp"] = timestamp
        return Gson().toJson(map)
    }
}
