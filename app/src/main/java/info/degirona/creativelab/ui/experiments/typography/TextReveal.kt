package info.degirona.creativelab.ui.experiments.typography

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import kotlinx.coroutines.delay

sealed interface TextRevealEffect {
    data class ScaleEffect(val shrink: Boolean) : TextRevealEffect

    data object RotateEffect : TextRevealEffect

    data object CurtainEffect : TextRevealEffect

    data object WheelEffect : TextRevealEffect
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextReveal(
    text: String,
    textStyle: TextStyle,
    textRevealEffect: TextRevealEffect,
    modifier: Modifier = Modifier,
    initialDelay: Long = 500L,
    letterDelay: Long = 50L,
    animationSpec: FiniteAnimationSpec<Float> = tween(
        durationMillis = 250,
        easing = FastOutSlowInEasing,
    ),
) {
    val zeroSpacingTextStyle = textStyle.copy(letterSpacing = 0.sp)
    FlowRow(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = calcSpaceWidth(zeroSpacingTextStyle),
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        val splitText = text.split(" ")
        val wordStartIndices = remember(text) {
            splitText.runningFold(0) { acc, word -> acc + word.length + 1 }.dropLast(1)
        }
        splitText.fastForEachIndexed { index, word ->
            val wordStartIndex = wordStartIndices[index]
            WordReveal(
                word = word,
                initialDelay = initialDelay + wordStartIndex * letterDelay,
                letterDelay = letterDelay,
                textStyle = zeroSpacingTextStyle,
                textRevealEffect = textRevealEffect,
                animationSpec = animationSpec,
            )
        }
    }
}

@Composable
private fun calcSpaceWidth(textStyle: TextStyle): Dp {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = remember(textMeasurer) { textMeasurer.measure(" ", textStyle) }
    return with(LocalDensity.current) { textLayoutResult.size.width.toDp() }
}

private enum class AnimationState {
    Idle, Animating, Revealed
}

@Composable
private fun WordReveal(
    word: String,
    initialDelay: Long,
    letterDelay: Long,
    textStyle: TextStyle,
    textRevealEffect: TextRevealEffect,
    animationSpec: FiniteAnimationSpec<Float>,
    modifier: Modifier = Modifier,
) {
    var animationState by remember { mutableStateOf(AnimationState.Idle) }

    LaunchedEffect(key1 = word) {
        delay(timeMillis = initialDelay)
        animationState = AnimationState.Animating
    }

    if (animationState == AnimationState.Animating) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Bottom,
        ) {
            word.forEachIndexed { index, char ->
                var isVisible by remember { mutableStateOf(false) }
                val transition = updateTransition(targetState = isVisible, label = "visibility transition")

                LaunchedEffect(index, transition.currentState, transition.targetState) {
                    if (index == word.indices.last && transition.currentState == transition.targetState && isVisible) {
                        animationState = AnimationState.Revealed
                    }
                }

                LaunchedEffect(key1 = index) {
                    delay(timeMillis = index * letterDelay)
                    isVisible = true
                }

                textRevealEffect.animateLetter(
                    letter = char.toString(),
                    transition = transition,
                    animationSpec = animationSpec,
                    textStyle = textStyle,
                )
            }
        }
    } else {
        val alpha = if (animationState == AnimationState.Revealed) 1f else 0f
        Text(
            text = word,
            style = textStyle,
            modifier = Modifier.alpha(alpha),
        )
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun TextRevealEffect.animateLetter(
    letter: String,
    transition: Transition<Boolean>,
    animationSpec: FiniteAnimationSpec<Float>,
    textStyle: TextStyle,
) {
    when (this) {
        is TextRevealEffect.ScaleEffect ->
            LetterRevealScaled(
                letter = letter,
                transition = transition,
                animationSpec = animationSpec,
                shrink = shrink,
                textStyle = textStyle,
            )

        is TextRevealEffect.RotateEffect ->
            LetterRevealRotated(
                letter = letter,
                transition = transition,
                animationSpec = animationSpec,
                textStyle = textStyle,
            )

        is TextRevealEffect.CurtainEffect ->
            LetterRevealCurtain(
                letter = letter,
                transition = transition,
                animationSpec = animationSpec,
                textStyle = textStyle,
            )

        is TextRevealEffect.WheelEffect ->
            LetterRevealWheel(
                letter = letter,
                transition = transition,
                animationSpec = animationSpec,
                textStyle = textStyle,
            )
    }
}

@Composable
private fun LetterRevealScaled(
    letter: String,
    transition: Transition<Boolean>,
    animationSpec: FiniteAnimationSpec<Float>,
    shrink: Boolean,
    textStyle: TextStyle,
) {
    val alpha by transition.animateFloat(
        label = "alpha animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 1f else 0f
    }

    val scale by transition.animateFloat(
        label = "scale animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        val initialScale = if (shrink) 2f else .4f
        if (visible) 1f else initialScale
    }

    Text(
        text = letter,
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.coerceIn(0f, 1f)
            this.scaleX = scale
            this.scaleY = scale
        },
        style = textStyle,
    )
}

@Composable
private fun LetterRevealRotated(
    letter: String,
    transition: Transition<Boolean>,
    animationSpec: FiniteAnimationSpec<Float>,
    textStyle: TextStyle,
) {
    val alpha by transition.animateFloat(
        label = "alpha animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 1f else 0f
    }

    val scale by transition.animateFloat(
        label = "scale animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 1f else .8f
    }

    val degrees by transition.animateFloat(
        label = "degrees animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 0f else -180f
    }

    val camera by transition.animateFloat(
        label = "degrees animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 8f else 7f
    }

    Text(
        text = letter,
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.coerceIn(0f, 1f)
            this.scaleX = scale
            this.scaleY = scale
            this.rotationY = degrees
            this.cameraDistance = camera
            this.transformOrigin = TransformOrigin(.5f, 0f)
        },
        style = textStyle,
    )
}

@Composable
private fun LetterRevealCurtain(
    letter: String,
    transition: Transition<Boolean>,
    animationSpec: FiniteAnimationSpec<Float>,
    textStyle: TextStyle,
) {
    val alpha by transition.animateFloat(
        label = "alpha animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 1f else 0.8f
    }

    val degrees by transition.animateFloat(
        label = "degrees animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 0f else -90f
    }

    val camera by transition.animateFloat(
        label = "degrees animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 8f else 7f
    }

    Text(
        text = letter,
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.coerceIn(0f, 1f)
            this.rotationX = degrees
            this.cameraDistance = camera
            this.transformOrigin = TransformOrigin(0f, .5f)
        },
        style = textStyle,
    )
}


@Composable
private fun LetterRevealWheel(
    letter: String,
    transition: Transition<Boolean>,
    animationSpec: FiniteAnimationSpec<Float>,
    textStyle: TextStyle,
) {
    val alpha by transition.animateFloat(
        label = "alpha animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 1f else 0f
    }

    val degrees by transition.animateFloat(
        label = "degrees animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 0f else 180f
    }

    val density = LocalDensity.current

    val translationX by transition.animateFloat(
        label = "degrees animation",
        transitionSpec = { animationSpec },
    ) { visible ->
        if (visible) 0f else with(density) { textStyle.fontSize.toPx() * 2 }
    }

    Text(
        text = letter,
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.coerceIn(0f, 1f)
            this.rotationZ = degrees
            this.translationX = translationX
        },
        style = textStyle,
    )
}
