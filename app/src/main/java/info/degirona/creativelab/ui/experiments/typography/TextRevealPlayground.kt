package info.degirona.creativelab.ui.experiments.typography

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToLong

private const val text = "Hello from Creative Lab! I'm going to make this text as large as possible to capture your attention and make it more engaging"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextRevealPlayground(
    modifier: Modifier = Modifier,
) {
    var id by remember { mutableIntStateOf(0) }
    var textRevealEffect by remember { mutableStateOf<TextRevealEffect>(TextRevealEffect.ScaleEffect(true)) }
    var letterDelay by remember { mutableLongStateOf(50L) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
    ) {
        key(id) {
            TextReveal(
                text = text,
                textStyle = MaterialTheme.typography.headlineSmall,
                textRevealEffect = textRevealEffect,
                letterDelay = letterDelay,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Text("Reveal Effect Type", style = MaterialTheme.typography.titleMedium)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                FilterChip(
                    selected = (textRevealEffect as? TextRevealEffect.ScaleEffect)?.shrink ?: false,
                    onClick = { textRevealEffect = TextRevealEffect.ScaleEffect(shrink = true); id++ },
                    label = { Text("Shrink") }
                )
                FilterChip(
                    selected = (textRevealEffect as? TextRevealEffect.ScaleEffect)?.shrink?.not() ?: false,
                    onClick = { textRevealEffect = TextRevealEffect.ScaleEffect(shrink = false); id++ },
                    label = { Text("Expand") }
                )
                FilterChip(
                    selected = textRevealEffect is TextRevealEffect.RotateEffect,
                    onClick = { textRevealEffect = TextRevealEffect.RotateEffect; id++ },
                    label = { Text("Rotate") }
                )
                FilterChip(
                    selected = textRevealEffect is TextRevealEffect.CurtainEffect,
                    onClick = { textRevealEffect = TextRevealEffect.CurtainEffect; id++ },
                    label = { Text("Curtain") }
                )
                FilterChip(
                    selected = textRevealEffect is TextRevealEffect.WheelEffect,
                    onClick = { textRevealEffect = TextRevealEffect.WheelEffect; id++ },
                    label = { Text("Wheel") }
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Text("Letter Delay", style = MaterialTheme.typography.titleMedium)
            SlidingFactor(
                value = letterDelay,
                onValueChanged = { letterDelay = it; id++ },
            )
        }
        Button(
            onClick = { id++ },
        ) {
            Text("Restart")
        }
    }
}

@Composable
private fun SlidingFactor(
    value: Long,
    onValueChanged: (Long) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: LongRange = 1L..250L,
) {
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChanged(it.roundToLong()) },
        valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
        modifier = modifier.fillMaxWidth()
    )
}

private fun LongRange.mapToBias(value: Long) =
    (2f * (value - start).toFloat() / (endInclusive.toFloat() - start.toFloat())) - 1f
