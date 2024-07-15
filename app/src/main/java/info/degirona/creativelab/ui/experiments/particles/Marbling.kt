package info.degirona.creativelab.ui.experiments.particles

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val MAX_DROPS = 35
private const val DROP_RESOLUTION = 400

private data class Drop(
    val center: Offset,
    val radii: Float,
    val color: Color,
    val vertices: List<Offset>,
) {

    constructor(
        center: Offset,
        radii: Float,
        color: Color,
    ) : this(
        center = center,
        radii = radii,
        color = color,
        vertices = buildList {
            for (idx in 0..DROP_RESOLUTION) {
                val angle = (2 * Math.PI * idx) / DROP_RESOLUTION
                val x = center.x + radii * cos(angle).toFloat()
                val y = center.y + radii * sin(angle).toFloat()
                add(Offset(x, y))
            }
        }
    )

    fun marble(other: Drop): Drop =
        copy(
            vertices = List(vertices.size) { idx ->
                val p = vertices[idx] - other.center
                val lengthSquared = p.getDistanceSquared()
                val root = sqrt(1f + (other.radii * other.radii) / lengthSquared)
                p * root + other.center
            }
        )
}

@Composable
fun Marbling(
    modifier: Modifier = Modifier,
) {
    val currentDrops = remember { mutableStateListOf<Drop>() }
    val newDrops = remember { mutableStateListOf<Drop>() }
    var evictionDrops by remember { mutableStateOf<Pair<Drop, Drop>?>(null) }
    val animatable = remember { Animatable(initialValue = 0f) }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { point ->
                    if (!animatable.isRunning) {
                        val newDrop = Drop(
                            center = point,
                            radii = 100f,
                            color = Color.hsv(
                                hue = 360f * Math
                                    .random()
                                    .toFloat(),
                                saturation = 1f,
                                value = 1f
                            ),
                        )
                        currentDrops.clear()
                        currentDrops.addAll(newDrops)
                        newDrops.clear()
                        newDrops.addAll(List(currentDrops.size) { idx ->
                            currentDrops[idx].marble(newDrop)
                        })
                        newDrops.add(newDrop)
                        if (newDrops.size > MAX_DROPS) {
                            evictionDrops = currentDrops.removeFirst() to newDrops.removeFirst()
                        }

                        coroutineScope.launch {
                            animatable.snapTo(targetValue = 0f)
                            animatable.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(easing = EaseOutQuart),
                            )
                        }

                    }
                }
            }
            .drawBehind {
                evictionDrops?.run {
                    morphBetweenDrops(
                        curDrop = first,
                        newDrop = second,
                        animationProgress = animatable.value,
                        color = first.color.copy(alpha = 1f - animatable.value),
                    )
                }
                currentDrops.forEachIndexed { index, drop ->
                    morphBetweenDrops(
                        curDrop = drop,
                        newDrop = newDrops[index],
                        animationProgress = animatable.value,
                        color = drop.color,
                    )
                }
                newDrops
                    .lastOrNull()
                    ?.run {
                        addDrop(
                            drop = this,
                            animationProgress = animatable.value,
                            color = color,
                        )
                    }
            }
    )
}

private fun DrawScope.morphBetweenDrops(
    curDrop: Drop,
    newDrop: Drop,
    animationProgress: Float,
    color: Color,
) {
    Path().apply {
        moveTo(
            x = lerp(curDrop.vertices[0].x, newDrop.vertices[0].x, animationProgress),
            y = lerp(curDrop.vertices[0].y, newDrop.vertices[0].y, animationProgress),
        )
        for (idx in 1..curDrop.vertices.lastIndex) {
            lineTo(
                x = lerp(curDrop.vertices[idx].x, newDrop.vertices[idx].x, animationProgress),
                y = lerp(curDrop.vertices[idx].y, newDrop.vertices[idx].y, animationProgress),
            )
        }
        close()
        drawPath(
            path = this,
            color = color,
        )
    }
}

private fun DrawScope.addDrop(
    drop: Drop,
    animationProgress: Float,
    color: Color,
) {
    Path().apply {
        moveTo(
            x = lerp(drop.center.x, drop.vertices[0].x, animationProgress),
            y = lerp(drop.center.y, drop.vertices[0].y, animationProgress),
        )
        for (idx in 1..drop.vertices.lastIndex) {
            lineTo(
                x = lerp(drop.center.x, drop.vertices[idx].x, animationProgress),
                y = lerp(drop.center.y, drop.vertices[idx].y, animationProgress),
            )
        }
        close()
        drawPath(
            path = this,
            color = color,
        )
    }
}