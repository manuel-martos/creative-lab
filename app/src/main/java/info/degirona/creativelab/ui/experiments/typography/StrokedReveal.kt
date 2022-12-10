package info.degirona.creativelab.ui.experiments.typography

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import info.degirona.creativelab.ui.experiments.shaders.PulseBandShader
import info.degirona.creativelab.ui.theme.fontFamily
import info.degirona.creativelab.ui.utils.FrameEffect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun StrokedReveal(modifier: Modifier = Modifier) {
    var localDate by remember { mutableStateOf(LocalDateTime.now()) }
    val formatter = remember { DateTimeFormatter.ofPattern("HH:mm:ss") }
    val formattedTime by remember { derivedStateOf { formatter.format(localDate) } }
    var time by remember { mutableStateOf(0f) }
    FrameEffect(Unit, initialValue = localDate.nano / 1_000_000_000f) {
        time = it
        localDate = LocalDateTime.now()
    }
    StrokedClock(
        clock = formattedTime,
        time = time,
        modifier = modifier,
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun StrokedClock(
    clock: String,
    time: Float,
    modifier: Modifier,
) {
    val bandShader = remember { PulseBandShader() }
    val shaderBrush = remember { ShaderBrush(bandShader) }
    Box(modifier = modifier) {
        Text(
            text = clock,
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle.Default.copy(
                textAlign = TextAlign.Center,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                drawStyle = Stroke(
                    miter = 10f,
                    width = 5f,
                    join = StrokeJoin.Round
                ),
                brush = shaderBrush.also {
                    bandShader.updateTime(time = time)
                }
            ),
            modifier = Modifier
                .onSizeChanged {
                    bandShader.updateResolution(it.toSize())
                },
        )
    }
}


//@Composable
//fun StrokedReveal(modifier: Modifier = Modifier) {
//    Box(modifier = modifier) {
//        var maskedBandShader by remember { mutableStateOf<MaskedBandShader?>(null) }
//        maskedBandShader?.let {
//            StrokeAndAnimation(maskedBandShader = it)
//        } ?: CaptureBitmapMask {
//            maskedBandShader = MaskedBandShader(bitmapMask = it, velocity = 1f)
//        }
//    }
//}
//
//@OptIn(ExperimentalTextApi::class)
//@Composable
//private fun StrokeAndAnimation(
//    maskedBandShader: MaskedBandShader,
//) {
//    var time by remember { mutableStateOf(0f) }
//    val bandShader = remember { BandShader(velocity = 1f) }
//    val strokeShaderBrush = remember { ShaderBrush(bandShader) }
//    val fillShaderBrush = remember { ShaderBrush(maskedBandShader) }
//    FrameEffect(Unit) {
//        time = it
//    }
//    bandShader.apply { updateTime(time = time) }
//    maskedBandShader.apply { updateTime(time = time) }
//    Text(
//        text = "12:45:00",
//        color = MaterialTheme.colorScheme.onSurface,
//        style = TextStyle.Default.copy(
//            textAlign = TextAlign.Center,
//            fontSize = 72.sp,
//            fontWeight = FontWeight.Bold,
//            fontFamily = fontFamily,
//            drawStyle = Stroke(
//                miter = 10f,
//                width = 5f,
//                join = StrokeJoin.Round
//            ),
//            brush = strokeShaderBrush,
//        ),
//        modifier = Modifier
//            .onSizeChanged {
//                bandShader.updateResolution(it.toSize())
//            }
//    )
//    Text(
//        text = "12:45:00",
//        color = MaterialTheme.colorScheme.onSurface,
//        style = TextStyle.Default.copy(
//            textAlign = TextAlign.Center,
//            fontSize = 72.sp,
//            fontFamily = fontFamily,
//            fontWeight = FontWeight.Bold,
//            brush = fillShaderBrush,
//        ),
//        modifier = Modifier
//            .onSizeChanged {
//                maskedBandShader.updateResolution(it.toSize())
//            }
//    )
//}
//
//@OptIn(ExperimentalTextApi::class)
//@Composable
//private fun CaptureBitmapMask(
//    onBitmapCaptured: (Bitmap) -> Unit
//) {
//    val configuration = LocalConfiguration.current
//    val density = LocalDensity.current
//    CaptureComposable(
//        onBitmapCaptured = onBitmapCaptured,
//        widthInPixels = (density.density * (configuration.screenWidthDp - 64)).roundToInt(),
//    ) {
//        Text(
//            text = "12:45:00",
//            color = MaterialTheme.colorScheme.onSurface,
//            style = TextStyle.Default.copy(
//                textAlign = TextAlign.Center,
//                fontSize = 72.sp,
//                fontWeight = FontWeight.Bold,
//                fontFamily = fontFamily,
//                drawStyle = Stroke(
//                    miter = 10f,
//                    width = 8f,
//                    join = StrokeJoin.Round,
//                )
//            ),
//        )
//    }
//}
