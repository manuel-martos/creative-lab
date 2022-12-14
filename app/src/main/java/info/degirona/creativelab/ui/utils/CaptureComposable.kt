package info.degirona.creativelab.ui.utils

import android.graphics.Bitmap
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap

// References:
// https://proandroiddev.com/create-bitmaps-from-jetpack-composables-bdb2c95db51
// https://github.com/PatilShreyas/NotyKT/pull/269

@Composable
fun CaptureComposable(
    onBitmapCaptured: (Bitmap) -> Unit,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val composeView = remember { ComposeView(context).apply { visibility = View.GONE } }
    var rendered by remember { mutableStateOf(false) }

    fun captureBitmap(): Bitmap {
        return composeView.drawToBitmap()
    }

    LaunchedEffect(key1 = rendered) {
        if (rendered) {
            onBitmapCaptured(captureBitmap())
        }
    }

    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                    rendered = true
                }
            }
        }
    )
}
