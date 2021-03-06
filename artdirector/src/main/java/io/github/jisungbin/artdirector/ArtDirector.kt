package io.github.jisungbin.artdirector

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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
    val media = remember { mutableStateListOf<Media>() }

    LaunchedEffect(media) {
        if (mediaType == MediaType.All) {
            media.addAll(MediaUtil.getAll(context, MediaType.Video))
            media.addAll(MediaUtil.getAll(context, MediaType.Image))
        } else {
            media.addAll(MediaUtil.getAll(context, mediaType))
        }
    }

    val countedMediaUri = remember { mutableStateListOf<Uri>() }

    LazyVerticalGrid(
        cells = GridCells.Fixed(showCount),
        content = {
            items(media.sortedByDescending { it.date }) { media ->
                if (media.type == MediaType.Image) {
                    Box(
                        modifier = Modifier.aspectRatio(1f),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Image(
                            bitmap = media.getImageBitmap(context)!!.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        CountableChip(
                            modifier = Modifier.clickable {
                                if (countedMediaUri.contains(media.uri)) {
                                    countedMediaUri.remove(media.uri)
                                } else {
                                    countedMediaUri.add(media.uri)
                                }
                            },
                            count = countedMediaUri.indexOf(media.uri)
                        )
                    }
                } else { // MediaType.Video
                    val thumbnail = media.getVideoThumbnail(context)
                    val shape = RoundedCornerShape(2.dp)

                    Box(
                        modifier = Modifier.aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = thumbnail.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(shape)
                                .background(color = Color.White, shape = shape)
                                .border(width = 1.dp, color = Color.LightGray, shape = shape)
                                .padding(end = 4.dp, top = 2.dp, bottom = 2.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_round_play_arrow_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            Text(
                                text = media.durationToString() ?: "00:00",
                                modifier = Modifier.padding(start = 2.dp),
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        }
    )
}
