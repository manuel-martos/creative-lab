package info.degirona.creativelab.ui.experiments.typography

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import info.degirona.creativelab.ui.theme.fontFamily

@Composable
fun SimpleStroke(
    modifier: Modifier = Modifier
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
                width = 5f,
                join = StrokeJoin.Round,
            )
        ),
        modifier = modifier,
    )
}
