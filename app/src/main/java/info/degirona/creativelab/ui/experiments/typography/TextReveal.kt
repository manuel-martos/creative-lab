package info.degirona.creativelab.ui.experiments.typography

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val HelloText =
    "Hello Creative lab, i am gonna make this text as larger as possible to make it more " + "interesting"
val SplitText = HelloText.split(" ")
const val LetterDelay = 150

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun TextReveal(modifier: Modifier = Modifier) {

    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        SplitText.forEachIndexed { index, word ->
            val startIndex by remember {
                derivedStateOf {
                    SplitText.take(index).joinToString(" ").lastIndex
                }
            }
            val wordStartIndex by remember {
                derivedStateOf {
                    HelloText.indexOf(
                        string = word, startIndex = startIndex, ignoreCase = true
                    )
                }
            }
            WordRevealWheel(text = word, wordDelay = wordStartIndex * LetterDelay, letterDelay = LetterDelay)
        }
    }
}

/**
 * Characters are shown one by one
 * An optional [transitionSpec] can be provided to specify (potentially different) animation for
 * each pair of initialState and targetState. [FiniteAnimationSpec] includes any non-infinite
 * animation, such as [tween], [spring], [keyframes] and even [repeatable], but not
 * [infiniteRepeatable]. By default, [transitionSpec] uses a [spring] animation for all transition
 * destinations.
 * @param wordDelay before arrive this letter
 * @param letterDelay between each one
 * @param shrink for scaling
 *  */
@Composable
fun WordReveal(
    text: String,
    wordDelay: Int,
    letterDelay: Int,
    modifier: Modifier = Modifier,
    shrink: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    transitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = {
        spring(
            Spring.DampingRatioLowBouncy,
            Spring.StiffnessLow,
        )
    },
) {
    Row(modifier, verticalAlignment = Alignment.Bottom) {
        text.forEachIndexed { index, char ->
            var isVisible by remember {
                mutableStateOf(false)
            }

            val transition = updateTransition(targetState = isVisible, label = "visibility transition")

            LaunchedEffect(key1 = Unit) {
                delay(timeMillis = index * letterDelay.toLong() + wordDelay)
                isVisible = true
            }

            val alpha by transition.animateFloat(
                label = "alpha animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 1f else 0f
            }

            val scale by transition.animateFloat(
                label = "scale animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 1f else {
                   if (shrink) 2f else .4f
                }
            }

            Text(
                text = "$char",
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha.coerceIn(0f, 1f)
                    scaleX = scale
                    scaleY = scale
                },
                style = textStyle,
            )
        }

    }
}

/**
 * Characters are shown one by one
 * An optional [transitionSpec] can be provided to specify (potentially different) animation for
 * each pair of initialState and targetState. [FiniteAnimationSpec] includes any non-infinite
 * animation, such as [tween], [spring], [keyframes] and even [repeatable], but not
 * [infiniteRepeatable]. By default, [transitionSpec] uses a [spring] animation for all transition
 * destinations.
 * @param wordDelay before arrive this letter
 * @param letterDelay between each one
 *  */
@Composable
fun WordRevealWithRotation(
    text: String,
    wordDelay: Int,
    letterDelay: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    transitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = {
        tween(
            durationMillis = LetterDelay, easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
        )
    },
) {
    Row(modifier, verticalAlignment = Alignment.Bottom) {
        text.forEachIndexed { index, char ->
            var isVisible by remember {
                mutableStateOf(false)
            }

            val transition = updateTransition(targetState = isVisible, label = "visibility transition")

            LaunchedEffect(key1 = Unit) {
                delay(timeMillis = index * letterDelay.toLong() + wordDelay)
                isVisible = true
            }

            val alpha by transition.animateFloat(
                label = "alpha animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 1f else 0f
            }

            val scale by transition.animateFloat(
                label = "scale animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 1f else .8f
            }

            val degrees by transition.animateFloat(
                label = "degrees animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 0f else -180f
            }

            val camera by transition.animateFloat(
                label = "degrees animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 8f else 7f
            }

            Text(
                text = "$char",
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha.coerceIn(0f, 1f)
                    scaleX = scale
                    scaleY = scale
                    this.rotationY = degrees
                    cameraDistance = camera
                    transformOrigin = TransformOrigin(.5f, 0f)
                },
                style = textStyle,
            )
        }

    }
}

/**
 * Characters are shown one by one
 * An optional [transitionSpec] can be provided to specify (potentially different) animation for
 * each pair of initialState and targetState. [FiniteAnimationSpec] includes any non-infinite
 * animation, such as [tween], [spring], [keyframes] and even [repeatable], but not
 * [infiniteRepeatable]. By default, [transitionSpec] uses a [spring] animation for all transition
 * destinations.
 * @param wordDelay before arrive this letter
 * @param letterDelay between each one
 *  */
@Composable
fun WordRevealCurtain(
    text: String,
    wordDelay: Int,
    letterDelay: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    transitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = {
        spring(
            Spring.DampingRatioLowBouncy,
            Spring.StiffnessLow,
        )
    },
) {
    Row(modifier, verticalAlignment = Alignment.Bottom) {
        text.forEachIndexed { index, char ->
            var isVisible by remember {
                mutableStateOf(false)
            }

            val transition = updateTransition(targetState = isVisible, label = "visibility transition")

            LaunchedEffect(key1 = Unit) {
                delay(timeMillis = index * letterDelay.toLong() + wordDelay)
                isVisible = true
            }

            val alpha by transition.animateFloat(
                label = "alpha animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 1f else 0.8f
            }

            val degrees by transition.animateFloat(
                label = "degrees animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 0f else -90f
            }

            val camera by transition.animateFloat(
                label = "degrees animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 8f else 7f
            }

            Text(
                text = "$char",
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha.coerceIn(0f, 1f)
                    this.rotationX = degrees
                    cameraDistance = camera
                    this.transformOrigin = TransformOrigin(0f, .5f)
                },
                style = textStyle,
            )
        }

    }
}

/**
 * Characters are shown one by one
 * An optional [transitionSpec] can be provided to specify (potentially different) animation for
 * each pair of initialState and targetState. [FiniteAnimationSpec] includes any non-infinite
 * animation, such as [tween], [spring], [keyframes] and even [repeatable], but not
 * [infiniteRepeatable]. By default, [transitionSpec] uses a [spring] animation for all transition
 * destinations.
 * @param wordDelay before arrive this letter
 * @param letterDelay between each one
 *  */
@Composable
fun WordRevealWheel(
    text: String,
    wordDelay: Int,
    letterDelay: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    transitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = {
        spring(
            Spring.DampingRatioLowBouncy,
            Spring.StiffnessLow,
        )
    },
) {
    Row(modifier, verticalAlignment = Alignment.Bottom) {
        text.forEachIndexed { index, char ->
            var isVisible by remember {
                mutableStateOf(false)
            }

            val transition = updateTransition(targetState = isVisible, label = "visibility transition")

            LaunchedEffect(key1 = Unit) {
                delay(timeMillis = index * letterDelay.toLong() + wordDelay)
                isVisible = true
            }

            val alpha by transition.animateFloat(
                label = "alpha animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 1f else 0f
            }

            val degrees by transition.animateFloat(
                label = "degrees animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 0f else 180f
            }

            val density = LocalDensity.current

            val translationX by transition.animateFloat(
                label = "degrees animation", transitionSpec = transitionSpec
            ) { visible ->
                if (visible) 0f else with(density) { textStyle.fontSize.toPx() * 2 }
            }

            Text(
                text = "$char",
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha.coerceIn(0f, 1f)
                    this.rotationZ = degrees
                    this.translationX = translationX
                },
                style = textStyle,
            )
        }

    }
}
