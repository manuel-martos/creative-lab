package info.degirona.creativelab.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.viewinterop.AndroidView

// Based on this article:
// https://proandroiddev.com/create-bitmaps-from-jetpack-composables-bdb2c95db51

@Composable
fun CaptureComposable(
    onBitmapCaptured: (Bitmap) -> Unit,
    widthInPixels: Int = -1,
    content: @Composable () -> Unit,
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),
        factory = { ctx ->
            CaptureComposableView(
                ctx = ctx,
                onBitmapCaptured = onBitmapCaptured,
                widthInPixels = widthInPixels,
            ) {
                content()
            }
        },
    )
}

@SuppressLint("ViewConstructor")
private class CaptureComposableView(
    private val ctx: Context,
    private val onBitmapCaptured: (bitmap: Bitmap) -> Unit,
    private val widthInPixels: Int = -1,
    private val content: @Composable () -> Unit,
) : LinearLayout(ctx) {

    init {
        val view = ComposeView(ctx)
        val width = 256
        val height = 256
        view.visibility = View.GONE
        view.layoutParams = LayoutParams(width, height)
        this.addView(view)

        view.setContent {
            content()
        }

        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    createBitmapFromView(view)?.let { onBitmapCaptured(it) }
                }
            }
        )
    }

    private fun createBitmapFromView(view: View): Bitmap? {
        view.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        view.measure(
            MeasureSpec.makeMeasureSpec(
                if (widthInPixels == -1) ctx.resources.displayMetrics.widthPixels else widthInPixels,
                MeasureSpec.AT_MOST
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        if (view.measuredWidth != 0 && view.measuredHeight != 0) {
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            val canvas = Canvas()
            val bitmap =
                Bitmap.createBitmap(
                    view.measuredWidth,
                    view.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )

            canvas.setBitmap(bitmap)
            view.draw(canvas)

            return bitmap
        }
        return null
    }
}
