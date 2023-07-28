package info.degirona.creativelab.node.experiments_list

import com.bumble.appyx.navigation.builder.SimpleBuilder
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node

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