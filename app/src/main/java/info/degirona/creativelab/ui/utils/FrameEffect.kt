package info.degirona.creativelab.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive

@Composable
fun FrameEffect(
    key1: Any?,
    initialValue: Float = 0f,
    block: (time: Float) -> Unit
) {
    LaunchedEffect(key1) {
        var firstFrame = 0f
        while (isActive) {
            val lastFrame = awaitFrame() / 1_000_000_000f
            if (firstFrame == 0f) {
                firstFrame = lastFrame
            }
            block(lastFrame - firstFrame + initialValue)
        }
    }
}

@Composable
fun FrameEffectDiff(
    key1: Any?,
    block: (period: Float) -> Unit
) {
    LaunchedEffect(key1) {
        var lastFrame = 0f
        while (isActive) {
            with(awaitFrame() / 1_000_000_000f) {
                if (lastFrame != 0f) {
                    block(this - lastFrame)
                }
                lastFrame = this
            }
        }
    }
}
