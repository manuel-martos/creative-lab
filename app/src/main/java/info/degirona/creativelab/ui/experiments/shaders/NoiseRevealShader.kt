package info.degirona.creativelab.ui.experiments.shaders

import android.graphics.RuntimeShader
import androidx.compose.ui.geometry.Size

class NoiseRevealShader : RuntimeShader(shader) {

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

                vec3 permute(vec3 x) { return mod(((x*34.0)+1.0)*x, 289.0); }
                
                float snoise(vec2 v){
                  const vec4 C = vec4(0.211324865405187, 0.366025403784439,
                           -0.577350269189626, 0.024390243902439);
                  vec2 i  = floor(v + dot(v, C.yy) );
                  vec2 x0 = v - i + dot(i, C.xx);
                  vec2 i1;
                  i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
                  vec4 x12 = x0.xyxy + C.xxzz;
                  x12.xy -= i1;
                  i = mod(i, 289.0);
                  vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))
                  + i.x + vec3(0.0, i1.x, 1.0 ));
                  vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy),
                    dot(x12.zw,x12.zw)), 0.0);
                  m = m*m ;
                  m = m*m ;
                  vec3 x = 2.0 * fract(p * C.www) - 1.0;
                  vec3 h = abs(x) - 0.5;
                  vec3 ox = floor(x + 0.5);
                  vec3 a0 = x - ox;
                  m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );
                  vec3 g;
                  g.x  = a0.x  * x0.x  + h.x  * x0.y;
                  g.yz = a0.yz * x12.xz + h.yz * x12.yw;
                  return 130.0 * dot(m, g);
                }
                
                half4 evaluate(vec2 fragcoord) {
                  float value = 0.5 + 0.5 * (snoise(8 * fragcoord.xy / iResolution.xy));
                  float t1 = mod(iTime, 2);
                  float t2 = t1 - 1;
                  if (value < t1) {
                    value = value < t2 ? 1 : 0;
                  } else {
                    value = 1;
                  }
                  return half4(value, value, value, 1);
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