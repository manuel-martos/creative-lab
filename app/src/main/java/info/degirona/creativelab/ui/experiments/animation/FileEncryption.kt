package info.degirona.creativelab.ui.experiments.animation

import android.content.pm.ActivityInfo
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import info.degirona.creativelab.R
import info.degirona.creativelab.ui.utils.LockScreenOrientation
import info.degirona.creativelab.ui.theme.passportTypography as TextStyles

@Composable
fun FileEncryption(
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var passportSize by remember { mutableStateOf(IntSize.Zero) }
    val animationSpec = remember {
        repeatable<Float>(
            iterations = Int.MAX_VALUE,
            animation = tween(10000, easing = LinearEasing),
        )
    }
    val passportAnimation = remember(size, passportSize) {
        val totalOffset = size.width + passportSize.width
        TargetBasedAnimation(
            animationSpec = animationSpec,
            typeConverter = Float.VectorConverter,
            initialValue = -totalOffset / 2f + size.width / 4f,
            targetValue = totalOffset / 2f + size.width / 4f,
        )
    }
    val encryptedAnimation = remember(size, passportSize) {
        val totalOffset = size.width + passportSize.width
        TargetBasedAnimation(
            animationSpec = animationSpec,
            typeConverter = Float.VectorConverter,
            initialValue = -totalOffset / 2f - size.width / 4f,
            targetValue = totalOffset / 2f - size.width / 4f,
        )
    }
    var playTime by remember { mutableStateOf(0L) }
    LaunchedEffect(encryptedAnimation) {
        val startTime = withFrameNanos { it }
        do {
            playTime = withFrameNanos { it } - startTime
        } while (true)
    }
    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .onSizeChanged { size = it }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .clipToBounds()
                .align(Alignment.CenterStart)
        ) {
            Passport(
                modifier = Modifier
                    .onSizeChanged { passportSize = it }
                    .graphicsLayer {
                        translationX = passportAnimation.getValueFromNanos(playTime)
                    }
            )
        }

        Box(
            modifier = Modifier
                .size(300.dp, 300.dp)
                .zIndex(10f)
                .scale(0.05f, 1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawOval(
                    brush = Brush.radialGradient(
                        0.0f to Color.White,
                        0.5f to Color.White,
                        0.6f to Color(0.718f, 0.667f, 0.843f),
                        1.0f to Color.Transparent,
                    ),
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .clipToBounds()
                .align(Alignment.CenterEnd)
        ) {
            Encrypted(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = encryptedAnimation.getValueFromNanos(playTime)
                    }
            )
        }
    }
}

@Composable
fun Passport(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .requiredSize(360.dp, 240.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "PASSPORT",
            style = TextStyles.headline,
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(6f / 7f)
                )
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                ) {
                    PassportData(title = "SURNAME", value = "DOE")
                    PassportData(title = "NAME", value = "JOHN")
                    PassportData(title = "NATIONALITY", value = "LUXENBURG")
                    PassportData(title = "DATE OF ISSUE", value = "18 MAY 2014")
                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                ) {
                    PassportData(title = "CARD NUMBER", value = "IN12349812")
                    PassportData(title = "DATE OF BIRTH", value = "29 JANUARY 1995")
                    PassportData(title = "EXPIRATION", value = "17 MAY 2024")
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "PIN12349812<<<<DOE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<",
                style = TextStyles.ocrText,
            )
            Text(
                text = "568AD198<<<<JOHN<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<",
                style = TextStyles.ocrText,
            )
        }
    }
}

@Composable
fun PassportData(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(bottom = 8.dp),
    ) {
        Text(
            text = title,
            style = TextStyles.title,
        )
        Text(
            text = value,
            style = TextStyles.value,
        )
    }
}

@Composable
fun Encrypted(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .requiredSize(360.dp, 240.dp)
            .padding(4.dp)
    ) {
        Text(text = "7EPQ4X2H9TBY8C5ZG0F1IA6L7O3SKV1Q9O3NI2LM6W", style = TextStyles.encryptedText)
        Text(text = "6V9XL5Y3UO4H7Z8RQ2MC0JI6W1K4B7N0DC8T5YU3I9", style = TextStyles.encryptedText)
        Text(text = "N8PQ2W5Y6I9X4L7O3Z8J1K4B7G0F1HV9C3D6T4S2E5", style = TextStyles.encryptedText)
        Text(text = "3X9C5V7B2N4M6K8L1J0H4G6F3D2S5A8PO5I9U3Y6T1", style = TextStyles.encryptedText)
        Text(text = "Q3W4E5R6T7Y8U9I0O1P2A3S4D5F6G7H8J9K0L1Z2X3", style = TextStyles.encryptedText)
        Text(text = "B2N4M6J8K0L2H4G6F8D0S2A4Q6W8E0R2T4Y6U8I0O2", style = TextStyles.encryptedText)
        Text(text = "9M2N4B6V8C0X1Z3L5K7J9H2G4F6D8S0A2Q4W6E8R0T", style = TextStyles.encryptedText)
        Text(text = "W3E5R7T9Y1U3I5O7P9A1S3D5F7G9H1J3K5L7Z9X1C3", style = TextStyles.encryptedText)
        Text(text = "I1O3P5L7K9J1H3G5F7D9S1A3Q5W7E9R1T3Y5U7M9N1", style = TextStyles.encryptedText)
        Text(text = "5B6N7M8V9C0X1Z2L3K4J5H6G7F8D9S0A1Q2W3E4R5T", style = TextStyles.encryptedText)
        Text(text = "H9J0K1L2Z3X4C5V6B7N8M9Q0W1E2R3T4Y5U6I7O8P9", style = TextStyles.encryptedText)
        Text(text = "1X2C3V4B5N6M7L8K9J0H1G2F3D4S5A6Q7W8E9R0T1Y", style = TextStyles.encryptedText)
    }
}

@Preview
@Composable
fun PassportPreview() {
    Passport()
}