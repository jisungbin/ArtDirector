package io.github.jisungbin.artdirector

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore

sealed class MediaType(val projection: List<String>) {
    object Image : MediaType(
        listOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED
        )
    )

    object Video : MediaType(
        listOf(
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.DURATION // only video
        )
    )

    object All : MediaType(emptyList())

    fun isVideo() = this == Video
}

internal object MediaUtil {
    fun getAll(context: Context, type: MediaType): List<Media> {
        val mediaList = mutableListOf<Media>()
        val projection = type.projection
        val externalContentUri =
            if (type.isVideo()) MediaStore.Video.Media.EXTERNAL_CONTENT_URI else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val sortOrder = "${type.projection.first()} ASC" // ${DISPLAY_NAME} ASC
        val query = context.contentResolver.query(
            externalContentUri,
            projection.toTypedArray(),
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val nameColumn = cursor.getColumnIndexOrThrow(projection[0])
            val idColumn = cursor.getColumnIndexOrThrow(projection[1])
            val sizeColumn = cursor.getColumnIndexOrThrow(projection[2])
            val indexColumn = cursor.getColumnIndexOrThrow(projection[3])
            val dateColumn = cursor.getColumnIndexOrThrow(projection[4])
            val durationColumn =
                if (type.isVideo()) cursor.getColumnIndexOrThrow(projection[5]) else null

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameColumn)
                val id = cursor.getLong(idColumn)
                val size = cursor.getInt(sizeColumn)
                val path = cursor.getString(indexColumn)
                val date = cursor.getLong(dateColumn)
                val duration = durationColumn?.let { cursor.getInt(it) }

                val contentUri = ContentUris.withAppendedId(
                    externalContentUri,
                    id
                )

                mediaList += Media(
                    uri = contentUri,
                    path = path,
                    name = name,
                    size = size,
                    date = date,
                    duration = duration
                )
            }
        }

        return mediaList
    }
}
