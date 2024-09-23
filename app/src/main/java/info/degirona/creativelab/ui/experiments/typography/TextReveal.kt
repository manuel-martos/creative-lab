package info.degirona.creativelab.ui.experiments.typography

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
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
    textRevealEffect: TextRevealEffect,
    modifier: Modifier = Modifier,
    initialDelay: Long = 500L,
    letterDelay: Long = 50L,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(letterSpacing = 0.sp),
    animationSpec: FiniteAnimationSpec<Float> = spring(
        Spring.DampingRatioLowBouncy,
        Spring.StiffnessLow,
    ),
) {
    FlowRow(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        val splitText = text.split(" ")
        splitText.fastForEachIndexed { index, word ->
            val startIndex by remember {
                derivedStateOf {
                    splitText.take(index).joinToString(" ").lastIndex
                }
            }
            val wordStartIndex by remember {
                derivedStateOf {
                    text.indexOf(
                        string = word, startIndex = startIndex, ignoreCase = true
                    )
                }
            }
            WordReveal(
                word = word,
                initialDelay = initialDelay + wordStartIndex * letterDelay,
                letterDelay = letterDelay,
                textStyle = textStyle,
                textRevealEffect = textRevealEffect,
                animationSpec = animationSpec,
            )
        }
    }
}

private enum class AnimationStatus {
    Idle,
    Running,
    Finished,
}

/**
 * Characters are shown one by one
 * @param initialDelay before arrive this letter
 * @param letterDelay between each one
 *  */
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
    var animationStatus by remember { mutableStateOf(AnimationStatus.Idle) }

    LaunchedEffect(key1 = word) {
        delay(timeMillis = initialDelay)
        animationStatus = AnimationStatus.Running
    }

    if (animationStatus == AnimationStatus.Running) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Bottom,
        ) {
            word.forEachIndexed { index, char ->
                var isVisible by remember { mutableStateOf(false) }
                val transition = updateTransition(targetState = isVisible, label = "visibility transition")

                LaunchedEffect(index, transition.currentState, transition.targetState) {
                    if (index == word.indices.last && transition.currentState == transition.targetState && isVisible) {
                        animationStatus = AnimationStatus.Finished
                    }
                }

                LaunchedEffect(key1 = index) {
                    delay(timeMillis = index * letterDelay)
                    isVisible = true
                }

                LetterRevealWithEffect(
                    letter = char.toString(),
                    transition = transition,
                    animationSpec = animationSpec,
                    textStyle = textStyle,
                    textRevealEffect = textRevealEffect,
                )
            }
        }
    } else {
        Text(
            text = word,
            style = textStyle,
            modifier = Modifier.alpha(if (animationStatus == AnimationStatus.Finished) 1f else 0f)
        )
    }
}

@Composable
private fun LetterRevealWithEffect(
    letter: String,
    transition: Transition<Boolean>,
    animationSpec: FiniteAnimationSpec<Float>,
    textRevealEffect: TextRevealEffect,
    textStyle: TextStyle,
) {
    when (textRevealEffect) {
        is TextRevealEffect.ScaleEffect ->
            LetterRevealScaled(
                letter = letter,
                transition = transition,
                animationSpec = animationSpec,
                shrink = textRevealEffect.shrink,
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
