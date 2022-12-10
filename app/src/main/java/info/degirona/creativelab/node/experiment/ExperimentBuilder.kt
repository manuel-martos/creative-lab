package info.degirona.creativelab.node.experiment

import com.bumble.appyx.core.builder.SimpleBuilder
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

class ExperimentBuilder(private val dependency: Experiment.Dependency) : SimpleBuilder() {
    override fun build(buildContext: BuildContext): Node {
        return ExperimentNode(
            buildContext = buildContext,
            experimentView = ExperimentView(dependency.typeModel),
        )
    }
}