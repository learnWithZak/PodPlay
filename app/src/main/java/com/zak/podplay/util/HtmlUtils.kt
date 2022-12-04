package com.zak.podplay.util

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.Html
import android.text.Spanned

object HtmlUtils {

    fun htmlToSpannable(htmlDesc: String): Spanned {

        var newHtmlDesc = htmlDesc.replace("\n".toRegex(), "")
        newHtmlDesc = newHtmlDesc.replace("(<(/)img>)|(<img.+?>)".
        toRegex(), "")

        val descSpan: Spanned = if (VERSION.SDK_INT >= VERSION_CODES.N) {
            Html.fromHtml(newHtmlDesc, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(newHtmlDesc)
        }

        return descSpan
    }
}