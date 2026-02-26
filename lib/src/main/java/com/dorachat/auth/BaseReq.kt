package com.dorachat.auth

import com.google.gson.Gson
import dora.util.GlobalContext
import dora.util.LanguageUtils
import java.lang.reflect.Modifier
import java.util.Locale

abstract class BaseReq {

    var lang: String = ""

    var payload: String = ""

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
