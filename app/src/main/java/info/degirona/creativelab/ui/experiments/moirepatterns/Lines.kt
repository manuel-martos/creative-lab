package info.degirona.creativelab.ui.experiments.moirepatterns

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import info.degirona.creativelab.ui.utils.FrameEffect
import kotlin.math.roundToInt

@Composable
fun Lines(
    modifier: Modifier = Modifier
) {
    var time by remember { mutableStateOf(0f) }
    FrameEffect(Unit) {
        time = it
    }
    Canvas(modifier = modifier.fillMaxSize().clipToBounds()) {
        drawDots(
            color = Color.Black.copy(alpha = 0.7f),
            dotRadius = 5.dp.toPx(),
            distance = 13.dp.toPx(),
        )
        rotate(-10f * time) {
            drawDots(
                color = Color.Black.copy(alpha = 0.7f),
                dotRadius = 6.dp.toPx(),
                distance = 13.dp.toPx(),
            )
        }
    }

//    Canvas(modifier = modifier.fillMaxSize()) {
//        rotate(10f * time) {
//            drawLinePattern(
//                color = Color.Gray.copy(alpha = 0.4f),
//                strokeWidth = 25.dp.toPx(),
//                lineWidth = 30.dp.toPx() * 1.8f,
//            )
//        }
//        rotate(120f - 9f * time) {
//            drawLinePattern(
//                color = Color.Gray.copy(alpha = 0.4f),
//                strokeWidth = 7.dp.toPx(),
//                lineWidth = 8.dp.toPx() * 1.1f,
//            )
//        }
//        rotate(8f * time + 270f) {
//            drawLinePattern(
//                color = Color.Gray.copy(alpha = 0.4f),
//                strokeWidth = 17.dp.toPx(),
//                lineWidth = 18.dp.toPx() * 1.1f,
//            )
//        }
//    }
}

private fun DrawScope.drawLinePattern(
    color: Color,
    strokeWidth: Float,
    lineWidth: Float,
) {
    val numLinesInWidth = (size.width / lineWidth).roundToInt()
    for (index in -numLinesInWidth..2 * numLinesInWidth) {
        val curX = index * lineWidth
        drawLine(
            color = color,
            start = Offset(curX, -size.height),
            end = Offset(curX, 2f * size.height),
            strokeWidth = strokeWidth,
        )
    }
}

private fun DrawScope.drawDots(
    color: Color,
    dotRadius: Float,
    distance: Float,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) {
    val dotsPerRow = (size.height / distance).roundToInt()
    val dotsPerColumn = (size.width / distance).roundToInt()
    for (row in -dotsPerRow..2 * dotsPerRow) {
        val displacement = if (row % 2 != 0) distance / 2f else 0f
        for (col in -dotsPerColumn..2 * dotsPerColumn) {
            val center = Offset(col * distance + displacement, row * distance)
            drawCircle(
                color = color,
                center = center,
                radius = dotRadius,
                blendMode = blendMode,
            )
        }
    }
}