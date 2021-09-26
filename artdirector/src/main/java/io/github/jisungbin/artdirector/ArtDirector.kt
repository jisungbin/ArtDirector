package io.github.jisungbin.artdirector

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtDirector(
    modifier: Modifier = Modifier,
    mediaType: MediaType = MediaType.All,
    showCount: Int = 3,
    clickableCount: Int = 1,
    result: (List<Media>) -> Unit
) {
    val context = LocalContext.current
    val media = mutableListOf<Media>()
    if (mediaType == MediaType.All) {
        media.addAll(MediaUtil.getAll(context, MediaType.Video))
        media.addAll(MediaUtil.getAll(context, MediaType.Image))
    } else {
        media.addAll(MediaUtil.getAll(context, mediaType))
    }

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        content = {
            items(media.sortedByDescending { it.date }) { media ->
                if (media.type == MediaType.Image) {
                    Image(
                        bitmap = media.getImageBitmap(context)!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                } else { // MediaType.Video
                    val thumbnail = media.getVideoThumbnail(context)
                    Box(modifier = Modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
                        Image(
                            bitmap = thumbnail.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_round_play_arrow_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    )
}
