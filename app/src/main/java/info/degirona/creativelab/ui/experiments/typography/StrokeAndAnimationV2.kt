package info.degirona.creativelab.ui.experiments.typography

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import info.degirona.creativelab.ui.experiments.shaders.MaskedBandShader
import info.degirona.creativelab.ui.theme.fontFamily
import info.degirona.creativelab.ui.utils.CaptureComposable
import info.degirona.creativelab.ui.utils.FrameEffect
import kotlin.math.roundToInt

@Composable
fun StrokeAndAnimationV2(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        var maskedBandShader by remember { mutableStateOf<MaskedBandShader?>(null) }
        maskedBandShader?.let {
            StrokeAndAnimation(maskedBandShader = it)
        } ?: CaptureBitmapMask {
            maskedBandShader = MaskedBandShader(bitmapMask = it)
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun StrokeAndAnimation(
    maskedBandShader: MaskedBandShader,
) {
    var time by remember { mutableStateOf(0f) }
    FrameEffect(Unit) {
        time = it
    }
    Text(
        text = "Hello from Creative Lab!",
        color = MaterialTheme.colorScheme.onSurface,
        style = TextStyle.Default.copy(
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            drawStyle = Stroke(
                miter = 10f,
                width = 5f,
                join = StrokeJoin.Round
            )
        )
    )
    Text(
        text = "Hello from Creative Lab!",
        color = MaterialTheme.colorScheme.onSurface,
        style = TextStyle.Default.copy(
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            brush = ShaderBrush(maskedBandShader.apply { updateTime(time = time) })
        ),
        modifier = Modifier
            .onSizeChanged {
                maskedBandShader.updateResolution(it.toSize())
            }
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun CaptureBitmapMask(
    onBitmapCaptured: (Bitmap) -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    CaptureComposable(
        onBitmapCaptured = onBitmapCaptured,
        widthInPixels = (density.density * (configuration.screenWidthDp - 64)).roundToInt(),
    ) {
        Text(
            text = "Hello from Creative Lab!",
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle.Default.copy(
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                drawStyle = Stroke(
                    miter = 10f,
                    width = 8f,
                    join = StrokeJoin.Round,
                )
            ),
        )
    }
}
