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
                    description = "Variation of previous experiment using simple stroked text as input shader.",
                    type = ExperimentType.Typography.StrokedReveal,
                ),
                ExperimentTypeModel(
                    title = "Noise Reveal",
                    description = "Applies Simplex noise function to reveal some text.",
                    type = ExperimentType.Typography.NoiseReveal,
                ),
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
