package info.degirona.creativelab.node.experiments_list

import com.bumble.appyx.core.builder.SimpleBuilder
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

class ExperimentsListBuilder(
    private val dependency: ExperimentsList.Dependency,
) : SimpleBuilder() {
    override fun build(buildContext: BuildContext): Node {
        return ExperimentsListNode(
            buildContext = buildContext,
            experimentsListView = ExperimentsListView(dependency.experiments),
        )
    }
}