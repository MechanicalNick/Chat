package com.tinkoff.homework.utils

import android.content.Context
import android.net.Uri

object FileUtils {
    fun getBytes(uri: Uri, context: Context): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        var bytes: ByteArray? = null
        inputStream?.let {
            bytes = it.readBytes()
            it.close()
        }
        return bytes ?: throw IllegalStateException()
    }

    fun getFileNameFromURL(url: Uri): String? {
        val fileName: String = url.path ?: throw IllegalStateException()
        return fileName.substring(fileName.lastIndexOf('/') + 1)
    }
}