package com.mikeeckels.obdash.shared.models

import android.content.Context
import androidx.annotation.StringRes

sealed interface TextSource {
    fun resolve(context: Context): String

    data class Literal(val text: String) : TextSource {
        override fun resolve(context: Context): String = text
    }

    data class Resource(@param:StringRes val resId: Int) : TextSource {
        override fun resolve(context: Context): String = context.getString(resId)
    }
}

val String.asSource: TextSource get() = TextSource.Literal(this)
val Int.asSource: TextSource get() = TextSource.Resource(this)