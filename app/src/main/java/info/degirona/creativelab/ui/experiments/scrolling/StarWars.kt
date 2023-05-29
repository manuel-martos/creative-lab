package info.degirona.creativelab.ui.experiments.scrolling

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import info.degirona.creativelab.ui.utils.FrameEffect
import info.degirona.creativelab.ui.utils.LockScreenOrientation
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun StarWars(
    modifier: Modifier = Modifier,
) {
    var time by remember { mutableStateOf(0f) }
    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    FrameEffect(Unit) {
        time = it
    }
    StarWars(
        time = time,
        modifier = modifier
            .background(Color.Black)
            .clipToBounds(),
        text = "\n\n" +
                "\n\n" +
                "\nCreative Labs!!\n\n" +
                "A long time ago, in a galaxy not so far away, there existed a realm where the " +
                "power of creation dwelled. Within the vast expanse of this technological " +
                "universe, a new force emerged, known as Jetpack Compose. Like a beacon of light " +
                "amidst the darkness, Jetpack Compose offered developers a path to forge " +
                "incredible user interfaces with unprecedented ease.\n" +
                "\n" +
                "In the midst of a great intergalactic journey, developers found themselves " +
                "grappling with the complexity of building beautiful and responsive UIs. They " +
                "yearned for a simpler way, a language that could transcend the boundaries of " +
                "conventional design. And thus, Jetpack Compose descended from the heavens, " +
                "bestowing its mighty abilities upon the developers of the universe.\n" +
                "\n" +
                "With Jetpack Compose, the once daunting task of crafting user interfaces became " +
                "an adventure of infinite possibilities. Like a Jedi harnessing the Force, " +
                "developers could now summon the elements of design with a mere flick of their " +
                "coding fingertips. They unleashed an arsenal of widgets, effortlessly arranging " +
                "them in harmonious compositions that danced across the screens of their users.\n" +
                "\n" +
                "No longer bound by the chains of tedious XML layouts, developers embarked upon " +
                "a new era of freedom and creativity. The intuitive syntax of Jetpack Compose " +
                "allowed them to articulate their visions with unparalleled clarity. They weaved " +
                "together elements of text, images, and buttons, transforming the ordinary into " +
                "the extraordinary.\n" +
                "\n" +
                "Just as a master painter wields a brush, developers unleashed a symphony of " +
                "colors and gradients upon their creations. With Jetpack Compose's declarative " +
                "nature, they effortlessly choreographed breathtaking animations that mesmerized " +
                "and captivated their audience. Transitions became seamless, and interactions " +
                "became fluid as the user's journey through the cosmos became a truly immersive " +
                "experience.\n" +
                "\n" +
                "Like the ancient scribes of old, developers could now express the deepest " +
                "essence of their apps with simplicity and elegance. They no longer had to " +
                "descend into the labyrinthine depths of intricate layouts and convoluted code. " +
                "Instead, they focused on their visions, channeling their creativity into " +
                "building intuitive and delightful user interfaces.\n" +
                "\n" +
                "As the news of Jetpack Compose spread across the galaxies, a wave of excitement " +
                "rippled through the developer community. They flocked to the power of this " +
                "newfound ally, eager to join the ranks of those who dared to shape the future " +
                "of user interfaces. Through forums and tutorials, they shared their knowledge, " +
                "empowering others to embrace the extraordinary capabilities of Jetpack Compose.\n" +
                "\n" +
                "In the wake of this revolution, the universe of app design was forever " +
                "transformed. Jetpack Compose became a force to be reckoned with, igniting the " +
                "imaginations of developers far and wide. With its remarkable simplicity and " +
                "boundless potential, Jetpack Compose ushered in a new era of UI creation, where " +
                "the impossible became possible, and the extraordinary became the norm.\n" +
                "\n" +
                "And so, dear developer, as you venture forth on your own coding odyssey, " +
                "remember the tale of Jetpack Compose. Embrace its power, wield it with care, " +
                "and let your creativity soar to the farthest reaches of the cosmos. For in your " +
                "hands lies the ability to build incredible UIs, and shape the destiny of the " +
                "digital universe. May the Force of Jetpack Compose be with you always.",
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun StarWars(
    text: String,
    time: Float,
    modifier: Modifier = Modifier,
    angle: Float = 50f,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val textMeasure = rememberTextMeasurer()
    val textMeasureResult = remember(size) {
        textMeasure.measure(
            text = text,
            constraints = Constraints.fixedWidth((size.width / 2f).roundToInt())
        )
    }
    Canvas(
        modifier = modifier
            .onSizeChanged { size = it }
            .graphicsLayer {
                rotationX = angle
            }
            .clipToBounds(),
    ) {
        if (size != IntSize.Zero) {
            (0 until textMeasureResult.lineCount).map {
                val rect = Rect(
                    left = textMeasureResult.getLineLeft(it),
                    right = textMeasureResult.getLineRight(it),
                    top = textMeasureResult.getLineTop(it),
                    bottom = textMeasureResult.getLineBottom(it),
                ).run {
                    translate(
                        translateX = (size.width - width) / 2f,
                        translateY = (size.height * sin(PI.toFloat() * angle / 180f)) - 25f * time,
                    )
                }
                val curParagraph = text.substring(
                    startIndex = textMeasureResult.getLineStart(it),
                    endIndex = textMeasureResult.getLineEnd(it),
                )
                drawText(
                    text = curParagraph,
                    textMeasurer = textMeasure,
                    size = rect.size,
                    topLeft = rect.topLeft,
                    overflow = TextOverflow.Visible,
                    style = TextStyle.Default.copy(Color(255, 196, 0))
                )
            }
        }

        drawRect(
            brush = linearGradient(
                0f to Color.Black,
                1f to Color.Transparent,
                start = Offset.Zero,
                end = Offset(0f, 0.25f * size.height)
            ),
            size = Size(size.width.toFloat(), 0.25f * size.height),
        )
        drawRect(
            brush = linearGradient(
                0f to Color.Transparent,
                1f to Color.Black,
                start = Offset(0f, 0.83f * size.height),
                end = Offset(0f, 0.87f * size.height),
            ),
            topLeft = Offset(0f, 0.83f * size.height),
            size = Size(size.width.toFloat(), 0.05f * size.height),
        )
    }
}
