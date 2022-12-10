package info.degirona.creativelab.node.experiments_container

import com.bumble.appyx.core.builder.SimpleBuilder
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack

class ExperimentsContainerBuilder(
    private val dependency: ExperimentsContainer.Dependency,
) : SimpleBuilder() {
    override fun build(buildContext: BuildContext): Node {
        val backStack = backStack(buildContext)
        return ExperimentsContainerNode(
            buildContext = buildContext,
            experimentsContainerView = ExperimentsContainerView(backStack),
            backStack = backStack,
        )
    }

    private fun backStack(
        buildContext: BuildContext
    ): BackStack<ExperimentsContainerNode.NavTarget> =
        BackStack(
            initialElement = ExperimentsContainerNode.NavTarget.ExperimentList(dependency.initialExperiments),
            savedStateMap = buildContext.savedStateMap,
        )
}