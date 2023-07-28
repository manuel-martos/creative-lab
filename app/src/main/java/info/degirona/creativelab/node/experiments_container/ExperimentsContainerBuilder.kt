package info.degirona.creativelab.node.experiments_container

import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.ui.slider.BackStackSlider
import com.bumble.appyx.navigation.builder.SimpleBuilder
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node

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
            model = BackStackModel(
                initialTargets = listOf(ExperimentsContainerNode.NavTarget.ExperimentList(dependency.initialExperiments)),
                savedStateMap = buildContext.savedStateMap,
            ),
            motionController = { BackStackSlider(it) }
        )
}