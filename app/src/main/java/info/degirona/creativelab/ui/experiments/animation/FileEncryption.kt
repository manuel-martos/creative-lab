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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import info.degirona.creativelab.R
import info.degirona.creativelab.ui.utils.FrameEffect
import info.degirona.creativelab.ui.utils.LockScreenOrientation
import kotlin.random.Random
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
    var playTime by remember { mutableLongStateOf(0L) }
    val encryptedId by remember(playTime) { derivedStateOf { playTime / 200_000_000L } }
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
        StartField(
            maxStars = 250,
            modifier = Modifier.fillMaxSize()
        )
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
                .size(320.dp, 320.dp)
                .zIndex(10f)
                .scale(0.15f, 1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawOval(
                    brush = Brush.radialGradient(
                        0f to Color(0.718f, 0.667f, 0.843f),
                        1f to Color.Transparent,
                    ),
                )
            }
        }
        Box(
            modifier = Modifier
                .size(280.dp, 280.dp)
                .zIndex(10f)
                .scale(0.02f, 1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawOval(
                    brush = Brush.radialGradient(
                        0f to Color.White,
                        1f to Color(0.157f, 0.07f, 0.306f, 0.2f),
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
                encryptedId = encryptedId,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = encryptedAnimation.getValueFromNanos(playTime)
                    }
            )
        }
    }
}

@Composable
fun StartField(
    maxStars: Int,
    modifier: Modifier = Modifier
) {
    var time by remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    val stars = remember(maxStars, size) {
        MutableList(maxStars) {
            Offset(size.width * Random.nextFloat(), size.height * Random.nextFloat())
        }.toMutableStateList()
    }
    FrameEffect(size) {
        time = it
        var index = 0
        stars.replaceAll { offset ->
            index += 1
            offset.copy(x = (offset.x + 4f * ((index % 4) + 1)) % size.width)
        }
    }
    Canvas(modifier = modifier
        .onSizeChanged { size = it }) {
        stars.forEachIndexed { index, it ->
            drawCircle(
                color = Color.White,
                center = it,
                radius = (1f * (index % 3 + 1)),
                alpha = 0.3f + 0.6f * (((index + 1) % 5) / 4f)
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
                    PassportData(title = "NATIONALITY", value = "SPANIARD")
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
                    PassportData(title = "EXPIRATION", value = "24 JULY 2024")
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
    encryptedId: Long,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .requiredSize(360.dp, 240.dp)
            .padding(4.dp)
    ) {
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
        EncryptedText(encryptedId = encryptedId, maxLength = 42)
    }
}

@Composable
private fun EncryptedText(
    encryptedId: Long,
    maxLength: Int,
    modifier: Modifier = Modifier,
) {
    val text = remember(encryptedId) {
        (0 until maxLength).map {
            if (Random.nextFloat() > 0.9f) {
                Random.nextInt(0, 10).toString()
            } else {
                'A' + Random.nextInt(0, 26)
            }
        }.joinToString("")
    }
    val annotatedText = remember(text) {
        buildAnnotatedString {
            if (Random.nextFloat() > 0.1f) {
                val idx = Random.nextInt(0, text.length)
                append(text, 0, idx)
                withStyle(TextStyles.highlightEncryptedText.toSpanStyle()) {
                    append(text[idx])
                }
                append(text, idx + 1, text.length)
            } else {
                append(text)
            }
        }
    }
    Text(
        text = annotatedText,
        style = TextStyles.encryptedText,
        modifier = modifier
    )
}

@Preview
@Composable
fun PassportPreview() {
    Passport()
}