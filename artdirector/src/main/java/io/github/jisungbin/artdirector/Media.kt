package io.github.jisungbin.artdirector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.util.Size
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

data class Media(
    val uri: Uri,
    val path: String,
    val name: String,
    val size: Int,
    val date: Long,
    val duration: Int? = null
) {
    fun getImageBitmap(context: Context): Bitmap? {
        return context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.fileDescriptor)
        }
    }

    fun durationToString(): String? {
        return duration?.toLong()?.let { duration ->
            String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            )
        }
    }

    fun dateToString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String? =
        SimpleDateFormat(pattern, Locale.getDefault()).format(date * 1000)

    @SuppressLint("NewApi")
    fun getVideoThumbnail(context: Context): Bitmap {
        val defaultBitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
        Canvas(defaultBitmap).drawColor(Color.LTGRAY)

        return try {
            context.contentResolver.loadThumbnail(uri, Size(640, 480), null)
        } catch (ignored: Exception) {
            defaultBitmap
        }
    }

    val type = if (duration != null) MediaType.Video else MediaType.Image
}
