package io.github.jisungbin.artdirector

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

internal object MediaUtil {
    fun getAllImages(context: Context): List<Media> {
        val imageList = mutableListOf<Media>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATA
        )
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val indexColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)
                val path = cursor.getString(indexColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                imageList += Media(
                    uri = contentUri,
                    path = path,
                    name = name,
                    size = size
                )
            }
        }

        return imageList
    }

    fun getAllVideos(context: Context): List<Media> {
        val imageList = mutableListOf<Media>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATA
        )
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
        val query = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val indexColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val path = cursor.getString(indexColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                imageList += Media(
                    uri = contentUri,
                    path = path,
                    name = name,
                    size = size,
                    duration = duration
                )
            }
        }

        return imageList
    }
}
