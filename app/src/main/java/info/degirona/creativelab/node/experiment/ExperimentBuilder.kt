package info.degirona.creativelab.node.experiment

import com.bumble.appyx.navigation.builder.SimpleBuilder
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node

class ExperimentBuilder(private val dependency: Experiment.Dependency) : SimpleBuilder() {
    override fun build(buildContext: BuildContext): Node {
        return ExperimentNode(
            buildContext = buildContext,
            experimentView = ExperimentView(dependency.typeModel),
        )
    }
}