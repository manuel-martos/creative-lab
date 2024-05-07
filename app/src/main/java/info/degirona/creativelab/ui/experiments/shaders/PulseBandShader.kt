package info.degirona.creativelab.ui.experiments.shaders

import android.graphics.RuntimeShader
import androidx.compose.ui.geometry.Size

class PulseBandShader(
    angleInRads: Float = -0.35f,
    velocity: Float = 0.2f,
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
                
                mat2 rotate(float angleInRads) {
                    return mat2(cos(angleInRads), sin(angleInRads), -sin(angleInRads), cos(angleInRads)); 
                }
                
                float cubicPulse(float c, float w, float x)
                {
                    x = abs(x - c);
                    if( x>w ) return 0.0;
                    x /= w;
                    return 1.0 - x*x*(3.0-2.0*x);
                }
                
                float band(vec2 fragCoord, float angleInRads) {
                    fragCoord *= rotate(angleInRads);
                    float value = cubicPulse(-0.2 + mod(iTime, 1), 0.2, fragCoord.y / iResolution.y);
                    if (value > 0.7) {
                        return 1;
                    } else if (value > 0) {
                        return 0.25;
                    } else {
                        return 0;
                    }
                }
                
                half4 evaluate(vec2 fragCoord) {
                    float color = 1 - band(fragCoord, -0.35);
                    return half4(color, color, color, 1);
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