package info.degirona.creativelab.ui.experiments.typography

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
import androidx.compose.ui.graphics.graphicsLayer
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
                brush = shaderBrush,
            ),
            modifier = Modifier
                .onSizeChanged {
                    bandShader.updateResolution(it.toSize())
                }
                .graphicsLayer {
                    bandShader.updateTime(time = time)
                },
        )
    }
}
