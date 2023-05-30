package info.degirona.creativelab.ui.experiments.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.bumble.appyx.core.collections.ImmutableList
import com.bumble.appyx.core.collections.toImmutableList
import info.degirona.creativelab.R
import info.degirona.creativelab.ui.utils.FrameEffect
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

@Immutable
private data class Bubble(
    val initialTime: Float,
    val posX: Float,
    val posY: Float,
    val curX: Float,
    val curY: Float,
    val radius: Float,
    val speed: Float,
)

@Composable
fun ChemicalBeaker(
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var path by remember(size) { mutableStateOf<Path?>(null) }
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.chemical_beaker_flat),
            contentDescription = "Chemical beaker",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.BottomCenter,
            colorFilter = ColorFilter.tint(Color(0.6f, 0.6f, 0.6f)),
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
                .onSizeChanged { size = it }
        )
        Canvas(
            modifier = Modifier
                .requiredSize(
                    LocalDensity.current.run {
                        size
                            .toSize()
                            .toDpSize()
                    }
                )
                .align(Alignment.BottomCenter)
        ) {
            if (path == null && size != IntSize.Zero) {
                val newPath = Path()
                newPath.moveTo(0.48f * size.width, 0.05f * size.height)
                newPath.lineTo(0.48f * size.width, 0.50f * size.height)

                newPath.lineTo(0.25f * size.width, 0.80f * size.height)
                newPath.lineTo(0.25f * size.width, 0.90f * size.height)
                newPath.lineTo(0.75f * size.width, 0.90f * size.height)
                newPath.lineTo(0.75f * size.width, 0.80f * size.height)

                newPath.lineTo(0.52f * size.width, 0.50f * size.height)
                newPath.lineTo(0.52f * size.width, 0.05f * size.height)
                path = newPath
            }
        }
        path?.let {
            Bubbles(
                path = it,
                modifier = Modifier
                    .requiredSize(
                        LocalDensity.current.run {
                            size
                                .toSize()
                                .toDpSize()
                        }
                    )
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun Bubbles(
    path: Path,
    modifier: Modifier = Modifier,
) {
    var time by remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    val bubbles = remember(size) { mutableStateListOf<Bubble>() }
    FrameEffect(size) {
        if (bubbles.size < 30 && it.roundToInt() != time.roundToInt()) {
            bubbles.add(createBubble(it, size))
        }
        time = it
        bubbles.replaceAll { bubble -> bubble.updateBubble(time, size, path) }
    }
    Bubbles(
        bubbles = bubbles.toImmutableList(),
        modifier = modifier.onSizeChanged { size = it }
    )
}

@Composable
private fun Bubbles(
    bubbles: ImmutableList<Bubble>,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        bubbles.forEach { bubble ->
            drawCircle(
                color = Color(0.4f, 0.4f, 0.4f),
                center = Offset(bubble.curX, bubble.curY),
                radius = bubble.radius,
                blendMode = BlendMode.DstIn,
            )
            drawCircle(
                color = Color(0.6f, 0.6f, 0.6f),
                center = Offset(bubble.curX, bubble.curY),
                radius = bubble.radius,
                blendMode = BlendMode.Exclusion,
            )
        }
    }
}

private fun createBubble(curTime: Float, size: IntSize): Bubble {
    val radius = when (Random.nextInt() % 3) {
        0 -> 0.020f
        1 -> 0.024f
        else -> 0.028f
    } * size.width
    val posX = size.width * (0.25f + 0.5f * Random.nextFloat())
    val posY = size.height * (0.80f + 0.1f * Random.nextFloat())
    return Bubble(
        initialTime = curTime,
        posX = posX,
        posY = posY,
        curX = posX,
        curY = posY,
        radius = radius,
        speed = 8f * radius,
    )
}

private fun Bubble.updateBubble(time: Float, size: IntSize, path: Path): Bubble =
    copy(
        curX = posX + radius * sin(time - initialTime),
        curY = posY - speed * (time - initialTime),
    ).run {
        val bubblePath = Path()
        bubblePath.addOval(
            Rect(curX - radius, curY - radius, curX + radius, curY + radius)
        )
        val newPath = Path()
        newPath.op(bubblePath, path, PathOperation.Intersect)
        if (newPath.isEmpty) {
            createBubble(time, size)
        } else {
            this
        }
    }
