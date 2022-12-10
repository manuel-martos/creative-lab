package info.degirona.creativelab.node.experiment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.node.NodeView
import info.degirona.creativelab.model.ExperimentModel.ExperimentTypeModel
import info.degirona.creativelab.ui.experiments.ExperimentHolder

class ExperimentView(
    private val typeModel: ExperimentTypeModel,
) : NodeView {

    @Composable
    override fun View(modifier: Modifier) {
        ExperimentScreen(
            typeModel = typeModel,
            modifier = modifier
                .fillMaxSize(),
        )
    }

}

@Composable
internal fun ExperimentScreen(
    typeModel: ExperimentTypeModel,
    modifier: Modifier = Modifier,
) {
    ExperimentHolder(
        typeModel = typeModel,
        modifier = modifier,
    )
}
