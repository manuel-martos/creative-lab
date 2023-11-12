package info.degirona.creativelab.ui.experiments.typography

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import info.degirona.creativelab.ui.experiments.shaders.BandShader
import info.degirona.creativelab.ui.theme.fontFamily
import info.degirona.creativelab.ui.utils.FrameEffect

@Composable
fun StrokeAndAnimationV1(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }
    FrameEffect(Unit) {
        time = it
    }
    BoxWithConstraints(modifier = modifier) {
        val text = "Hello from Creative Lab!"
        val bandShader = remember {
            BandShader().also {
                it.updateResolution(
                    Size(
                        constraints.maxWidth.toFloat(),
                        constraints.maxHeight.toFloat()
                    )
                )
            }
        }
        val shaderBrush = remember(time) {
            object : ShaderBrush() {
                override fun createShader(size: Size): Shader {
                    bandShader.updateTime(time)
                    return bandShader
                }
            }
        }
        Text(
            text = text,
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
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle.Default.copy(
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                brush = shaderBrush,
            ),
        )
    }
}
