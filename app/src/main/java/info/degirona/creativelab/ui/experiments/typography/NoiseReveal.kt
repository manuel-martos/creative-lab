package info.degirona.creativelab.ui.experiments.typography

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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import info.degirona.creativelab.ui.experiments.shaders.NoiseRevealShader
import info.degirona.creativelab.ui.theme.fontFamily
import info.degirona.creativelab.ui.utils.FrameEffect

@Composable
fun NoiseReveal(
    modifier: Modifier = Modifier
) {
    var time by remember { mutableFloatStateOf(0f) }
    FrameEffect(Unit) {
        time = it
    }
    val noiseShader = remember { NoiseRevealShader() }
    val shaderBrush = remember(time) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                noiseShader.updateTime(time)
                return noiseShader
            }
        }
    }
    Text(
        text = "Hello from Creative Lab!",
        color = MaterialTheme.colorScheme.onSurface,
        style = TextStyle.Default.copy(
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            brush = shaderBrush,
        ),
        modifier = modifier
            .onSizeChanged {
                noiseShader.updateResolution(it.toSize())
            }
    )
}
