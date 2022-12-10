package info.degirona.creativelab.ui.experiments.shaders

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import androidx.compose.ui.geometry.Size

class MaskedBandShader(
    angleInRads: Float = -0.35f,
    velocity: Float = 0.2f,
    bitmapMask: Bitmap,
) : RuntimeShader(shader) {

    init {
        setFloatUniform(
            /* uniformName = */ "angleInRads",
            /* value = */ angleInRads,
        )
        setFloatUniform(
            /* uniformName = */ "velocity",
            /* value = */ velocity,
        )
        setInputShader(
            /* shaderName = */ "inputShader",
            /* shader = */ BitmapShader(
                /* bitmap = */ bitmapMask,
                /* tileX = */ TileMode.REPEAT,
                /* tileY = */ TileMode.REPEAT
            )
        )

    }

    fun updateResolution(size: Size) {
        setFloatUniform(
            /* uniformName = */ "iResolution",
            /* value1 = */ size.width,
            /* value2 = */ size.width,
        )
    }

    fun updateTime(time: Float) {
        setFloatUniform(
            /* uniformName = */ "iTime",
            /* value = */ time,
        )
    }

    private companion object {
        private val shader =
            """
                uniform float2 iResolution;
                uniform float  iTime;
                uniform float  angleInRads;
                uniform float  velocity;
                uniform shader inputShader;
                
                mat2 rotate(float angleInRads) {
                    return mat2(cos(angleInRads), sin(angleInRads), -sin(angleInRads), cos(angleInRads)); 
                }
                
                float band(vec2 fragCoord, float angleInRads) {
                    fragCoord *= rotate(angleInRads);
                    float value = sin(10 * (fragCoord.y / iResolution.y - iTime * velocity));
                    if (value > 0.7) {
                        return 1;
                    } else if (value > 0) {
                        return 0.8;
                    } else {
                        return 0;
                    }
                }
                
                half4 evaluate(vec2 fragCoord) {
                    half4 mask = inputShader.eval(fragCoord);
                    float color;
                    if (mask.a != 1) {
                        color = band(fragCoord, angleInRads);                    
                    } else {
                        color = 1;
                    }
                    return half4(color, color, color, 1 - mask.a);
                }
                
                half4 main(vec2 fragCoord) {
                    return (evaluate(fragCoord + vec2(0.25, 0.25)) +
                        evaluate(fragCoord + vec2(-0.25, 0.25)) + 
                        evaluate(fragCoord + vec2(-0.25, -0.25)) + 
                        evaluate(fragCoord + vec2(0.25, -0.25))) / 4;
                }
            """.trimIndent()
    }
}