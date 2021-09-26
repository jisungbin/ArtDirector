package io.github.jisungbin.artdirector

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

internal object CountableConstants {
    const val None = -1
}

@Composable
internal fun CountableChip(modifier: Modifier = Modifier, count: Int = CountableConstants.None) {
    val counted = count != CountableConstants.None
    val primaryColor =
        animateColorAsState(if (counted) MaterialTheme.colors.primary else Color.White).value
    val backgroundColor = animateColorAsState(if (counted) Color.White else Color.Transparent).value

    Box(
        modifier = modifier
            .padding(10.dp)
            .size(25.dp)
            .clip(CircleShape)
            .border(width = 1.dp, color = primaryColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (count != CountableConstants.None) "${count + 1}" else "",
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor, shape = CircleShape),
            color = primaryColor,
            textAlign = TextAlign.Center
        )
    }
}
