package info.degirona.creativelab.ui.experiments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.model.ExperimentModel.ExperimentTypeModel
import info.degirona.creativelab.model.ExperimentType
import info.degirona.creativelab.ui.experiments.animation.ChemicalBeaker
import info.degirona.creativelab.ui.experiments.animation.FileEncryption
import info.degirona.creativelab.ui.experiments.moirepatterns.Lines
import info.degirona.creativelab.ui.experiments.particles.Gravity
import info.degirona.creativelab.ui.experiments.particles.Marbling
import info.degirona.creativelab.ui.experiments.scrolling.StarWars
import info.degirona.creativelab.ui.experiments.transitions.NoiseTransition
import info.degirona.creativelab.ui.experiments.transitions.TreeGrowingAtNight
import info.degirona.creativelab.ui.experiments.transitions.TreeGrowingDayLight
import info.degirona.creativelab.ui.experiments.typography.NoiseReveal
import info.degirona.creativelab.ui.experiments.typography.SimpleStroke
import info.degirona.creativelab.ui.experiments.typography.StrokeAndAnimationV1
import info.degirona.creativelab.ui.experiments.typography.StrokeAndAnimationV2
import info.degirona.creativelab.ui.experiments.typography.StrokedReveal
import info.degirona.creativelab.ui.theme.CreativeLabTheme
import info.degirona.creativelab.ui.theme.DarkTheme

@Composable
fun Experiments(
    experiments: List<ExperimentModel>,
    onClick: (ExperimentModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp)
    ) {
        item {
            Spacer(modifier = Modifier.statusBarsPadding())
        }
        items(experiments) {
            ExperimentItem(
                experiment = it,
                onClick = onClick,
            )
        }
        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
fun ExperimentItem(
    experiment: ExperimentModel,
    onClick: (ExperimentModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 80.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable { onClick(experiment) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = experiment.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = experiment.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp, end = 48.dp)
                )
            }
            ChemicalBeaker(
                modifier = Modifier
                    .requiredWidth(72.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 24.dp)
            )
        }
    }
}

@Composable
fun ExperimentHolder(
    typeModel: ExperimentTypeModel,
    modifier: Modifier = Modifier
) {
    typeModel.ExperimentTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            typeModel.Composable(
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}

@Composable
fun ExperimentTypeModel.Composable(modifier: Modifier) {
    when (this.type) {
        is ExperimentType.Typography.SimpleStroke -> SimpleStroke(modifier)
        is ExperimentType.Typography.StrokeAndAnimationV1 -> StrokeAndAnimationV1(modifier)
        is ExperimentType.Typography.StrokeAndAnimationV2 -> StrokeAndAnimationV2(modifier)
        is ExperimentType.Typography.StrokedReveal -> StrokedReveal(modifier)
        is ExperimentType.Typography.NoiseReveal -> NoiseReveal(modifier)
        is ExperimentType.Scrolling.StarWars -> StarWars(modifier)
        is ExperimentType.Particles.Gravity -> Gravity(modifier)
        is ExperimentType.Particles.Marbling -> Marbling(modifier)
        is ExperimentType.Animation.ChemicalBeaker -> ChemicalBeaker(modifier.fillMaxSize(0.75f))
        is ExperimentType.Animation.FileEncryption -> FileEncryption(modifier)
        is ExperimentType.MoirePatterns.Lines -> Lines(modifier)
        is ExperimentType.Transitions.NoiseTransition -> NoiseTransition(
            modifier = modifier,
            block1 = { TreeGrowingDayLight(it) },
            block2 = { TreeGrowingAtNight(it) }
        )
    }
}

@Composable
fun ExperimentTypeModel.ExperimentTheme(content: @Composable () -> Unit) {
    when (this.type) {
        is ExperimentType.Scrolling.StarWars -> DarkTheme { content() }
        is ExperimentType.Animation.FileEncryption -> DarkTheme { content() }
        is ExperimentType.Particles.Gravity -> {
            val systemUiController: SystemUiController = rememberSystemUiController()
            systemUiController.isSystemBarsVisible = false
            systemUiController.systemBarsDarkContentEnabled = false
            DisposableEffect(systemUiController) {
                onDispose {
                    systemUiController.isSystemBarsVisible = true
                    systemUiController.systemBarsDarkContentEnabled = true
                }
            }
            DarkTheme { content() }
        }

        else -> CreativeLabTheme { content() }
    }
}