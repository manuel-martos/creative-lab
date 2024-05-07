package info.degirona.creativelab.ui.experiments.particles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.collections.ImmutableList
import com.bumble.appyx.core.collections.toImmutableList
import info.degirona.creativelab.ui.utils.FrameEffectDiff
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@Immutable
private data class Particle(
    val offset: Vector,
    val velocity: Vector,
) {
    data class Vector(
        val x: Double,
        val y: Double,
    )
}

@Immutable
private data class Config(
    val rMin: Float,
    val rMax: Float,
    val attraction: Float,
) {
    companion object {
        @Stable
        val MassiveBody = Config(
            rMin = 0.001f,
            rMax = 0.85f,
            attraction = -0.001f,
        )

        @Stable
        val TinnyBodies = Config(
            rMin = 0.01f,
            rMax = 0.3f,
            attraction = 0.3f,
        )

        @Stable
        fun random() = Config(
            rMin = 0.04f * Random.nextFloat(),
            rMax = 0.041f + 1.5f * Random.nextFloat(),
            attraction = Random.nextFloat() - 0.5f,
        )
    }
}

@Composable
fun Gravity(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        var config by remember { mutableStateOf(Config.MassiveBody) }
        var particles by remember { mutableStateOf(generateParticles()) }
        FrameEffectDiff(Unit) {
            particles = particles.next(it, config)
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clickable { particles = generateParticles() }
        ) {
            val halfMinDimension = size.minDimension / 2.0f
            drawParticles(particles, center + Offset(0f, size.minDimension), halfMinDimension)
            drawParticles(particles, center + Offset(0f, -size.minDimension), halfMinDimension)
            drawParticles(particles, center, halfMinDimension)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = { config = Config.MassiveBody }) {
                Text(text = "Preset #1")
            }
            Button(onClick = { config = Config.TinnyBodies }) {
                Text(text = "Preset #2")
            }
            Button(onClick = { config = Config.random() }) {
                Text(text = "Random")
            }
        }
    }
}

private fun DrawScope.drawParticles(
    particles: ImmutableList<Particle>,
    center: Offset,
    halfMinDimension: Float,
) {
    particles.forEach {
        drawCircle(
            center = Offset(
                (center.x + it.offset.x * halfMinDimension).toFloat(),
                (center.y - it.offset.y * halfMinDimension).toFloat(),
            ),
            color = Color(
                red = 0.5f * cos(it.velocity.x + it.velocity.y).toFloat() + 0.5f,
                green = 0.5f * sin(it.velocity.x - it.velocity.y).toFloat() + 0.5f,
                blue = 0.5f * sin((it.velocity.x - it.velocity.y)).toFloat() + 0.5f,
                alpha = 1f,
            ),
            radius = size.minDimension * 0.0025f,
        )
    }
}

private fun generateParticles(
    numParticles: Int = 750,
): ImmutableList<Particle> {
    val result = mutableListOf<Particle>()
    while (result.size != numParticles) {
        val particle = Particle(
            offset = Particle.Vector(
                2.0 * Random.nextFloat() - 1.0,
                2.0 * Random.nextFloat() - 1.0,
            ),
            velocity = Particle.Vector(
                0.06 * Random.nextFloat() - 0.03,
                0.06 * Random.nextFloat() - 0.03,
            ),
        )
        result.add(particle)
    }
    return result.toImmutableList()
}

private fun ImmutableList<Particle>.next(elapsed: Float, config: Config): ImmutableList<Particle> {
    val result = mutableListOf<Particle>()
    forEachIndexed { outer, outerParticle ->
        var forceOffset = Offset(0f, 0f)
        forEachIndexed { inner, innerParticle ->
            if (outer != inner) {
                val diffX = wrap(outerParticle.offset.x - innerParticle.offset.x)
                val diffY = wrap(outerParticle.offset.y - innerParticle.offset.y)
                val distance = sqrt(diffX * diffX + diffY * diffY)
                if (distance != 0.0 && distance < config.rMax) {
                    val force: Double = when {
                        distance < config.rMin -> distance / config.rMin - 1
                        else -> config.attraction * (1.0 - abs(1.0 + config.rMin - 2.0 * distance) / (1.0 - config.rMin))
                    }
                    forceOffset += Offset(
                        (force * diffX / distance).toFloat(),
                        (force * diffY / distance).toFloat(),
                    )
                }
            }
        }


        // Calc new velocity: make it decay over time
        val newVelocityX =
            0.5.pow(elapsed / 10.0) * outerParticle.velocity.x + config.rMax * elapsed * forceOffset.x
        val newVelocityY =
            0.5.pow(elapsed / 10.0) * outerParticle.velocity.y + config.rMax * elapsed * forceOffset.y

        // Calc new position
        val newX: Double = wrap(outerParticle.offset.x + elapsed * newVelocityX)
        val newY: Double = wrap(outerParticle.offset.y + elapsed * newVelocityY)
        result.add(
            Particle(
                Particle.Vector(newX, newY),
                Particle.Vector(newVelocityX, newVelocityY),
            ),
        )
    }
    return result.toImmutableList()
}

fun wrap(input: Double): Double =
    when {
        input > 1.0 -> input - 2.0
        input < -1.0 -> input + 2.0
        else -> input
    }
