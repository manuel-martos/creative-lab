package info.degirona.creativelab

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.model.ExperimentModel.ExperimentTypeModel
import info.degirona.creativelab.model.ExperimentModel.ExperimentListModel
import info.degirona.creativelab.model.ExperimentType
import info.degirona.creativelab.node.experiments_container.ExperimentsContainer
import info.degirona.creativelab.node.experiments_container.ExperimentsContainerBuilder
import info.degirona.creativelab.ui.theme.CreativeLabTheme

val mainExperiments =
    listOf(
        ExperimentListModel(
            title = "Typography",
            description = "Play around different rendering and reveal techniques.",
            experimentModels = listOf(
                ExperimentTypeModel(
                    title = "Simple Stroke",
                    description = "Render stroked font.",
                    type = ExperimentType.Typography.SimpleStroke,
                ),
                ExperimentTypeModel(
                    title = "Stroke + Animation V1",
                    description = "Apply a simple AGSL animated shader to fill up stroked font.",
                    type = ExperimentType.Typography.StrokeAndAnimationV1,
                ),
                ExperimentTypeModel(
                    title = "Stroke + Animation V2",
                    description = "Variation of previous experiment using simple stroked text as input shader.",
                    type = ExperimentType.Typography.StrokeAndAnimationV2,
                ),
                ExperimentTypeModel(
                    title = "Stroked Reveal",
                    description = "Compose clock rendered using previous stroke typography revealed with nice effect.",
                    type = ExperimentType.Typography.StrokedReveal,
                ),
                ExperimentTypeModel(
                    title = "Noise Reveal",
                    description = "Applies Simplex noise function to reveal some text.",
                    type = ExperimentType.Typography.NoiseReveal,
                ),
            )
        ),
        ExperimentListModel(
            title = "Scrolling",
            description = "Show cases different ways of scrolling content like text or images.",
            experimentModels = listOf(
                ExperimentTypeModel(
                    title = "StarWars",
                    description = "StarWars classical text scrolling effect.",
                    type = ExperimentType.Scrolling.StarWars,
                )

            )
        ),
        ExperimentListModel(
            title = "Particles",
            description = "Some experiments with particle systems.",
            experimentModels = listOf(
                ExperimentTypeModel(
                    title = "Gravity",
                    description = "Observe the captivating interplay of attraction and repulsion between particles, unfolding in an exquisite choreography.",
                    type = ExperimentType.Particles.Gravity,
                )

            )
        ),
        ExperimentListModel(
            title = "Animation",
            description = "Show cases different ways of scrolling content like text or images.",
            experimentModels = listOf(
                ExperimentTypeModel(
                    title = "Chemical Beaker",
                    description = "Animated version of the chemical beaker shape shown along every experiment.",
                    type = ExperimentType.Animation.ChemicalBeaker,
                ),
                ExperimentTypeModel(
                    title = "File Encryption",
                    description = "Take on a challenge I found it in Twitter.",
                    type = ExperimentType.Animation.FileEncryption,
                ),
            )
        ),
        ExperimentListModel(
            title = "Moire Patterns",
            description = "Interference patterns creating mesmerizing visual effects.",
            experimentModels = listOf(
                ExperimentTypeModel(
                    title = "Dots",
                    description = "Witness the fascinating result of two overlapping sets of rotating dots.",
                    type = ExperimentType.MoirePatterns.Lines,
                )

            )
        ),
    )

class MainActivity : NodeComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CreativeLabTheme {
                NodeHost(
                    integrationPoint = appyxIntegrationPoint,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)

                ) {
                    ExperimentsContainerBuilder(
                        object : ExperimentsContainer.Dependency {
                            override val initialExperiments: List<ExperimentModel> =
                                mainExperiments
                        }
                    ).build(it)
                }
            }
        }
    }
}
