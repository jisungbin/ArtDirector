package io.github.jisungbin.artdirector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

data class Media(
    val uri: Uri,
    val path: String,
    val name: String,
    val size: Int,
    val duration: Int? = null
) {
    fun getBitmapImage(context: Context): Bitmap? {
        return context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.fileDescriptor)
        }
    }
}
