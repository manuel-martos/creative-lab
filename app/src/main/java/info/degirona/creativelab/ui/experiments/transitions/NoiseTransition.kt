package info.degirona.creativelab.ui.experiments.transitions

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import info.degirona.creativelab.R
import info.degirona.creativelab.ui.experiments.shaders.NoiseTransitionShader
import info.degirona.creativelab.ui.theme.CreativeLabTheme
import info.degirona.creativelab.ui.utils.CaptureComposable
import info.degirona.creativelab.ui.utils.FrameEffect

@Composable
fun NoiseTransition(
    modifier: Modifier = Modifier,
    block1: @Composable (Modifier) -> Unit,
    block2: @Composable (Modifier) -> Unit,
) {
    var alterBitmap by remember { mutableStateOf<Bitmap?>(null) }
    alterBitmap?.run {
        val noiseTransitionShader = remember { NoiseTransitionShader(this) }
        var time by remember { mutableFloatStateOf(0f) }
        FrameEffect(Unit) {
            time = it
        }
        block1(
            modifier
                .onSizeChanged {
                    noiseTransitionShader.updateResolution(it.toSize())
                }
                .graphicsLayer {
                    noiseTransitionShader.updateTime(time)
                    renderEffect = noiseTransitionShader.renderEffect()
                    clip = true
                }
        )
    } ?: CaptureComposable(onBitmapCaptured = { alterBitmap = it }) {
        block2(modifier)
    }
}

@Composable
fun TreeGrowingDayLight(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .requiredWidth(240.dp)
            .wrapContentHeight()
            .background(Color.LightGray, RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.tree_growing_near_a_country_road),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Tree growing near a country road"
        )
        Text(
            text = "Tree growing near a country road",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun TreeGrowingAtNight(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .requiredWidth(240.dp)
            .wrapContentHeight()
            .background(Color.LightGray, RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.growing_tree_near_a_road_at_night),
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Tree growing near a country road at night"
        )
        Text(
            text = "Tree growing near a country road at night",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun NoiseTransitionPreview() {
    CreativeLabTheme {
        TreeGrowingAtNight()
    }
}
