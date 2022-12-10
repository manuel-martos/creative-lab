package info.degirona.creativelab.node.experiments_container

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.interop.rx2.connectable.Connectable
import com.bumble.appyx.interop.rx2.connectable.NodeConnector
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.model.ExperimentModel.ExperimentTypeModel
import info.degirona.creativelab.node.experiment.Experiment
import info.degirona.creativelab.node.experiment.ExperimentBuilder
import info.degirona.creativelab.node.experiments_container.ExperimentsContainer.Input
import info.degirona.creativelab.node.experiments_container.ExperimentsContainer.Output
import info.degirona.creativelab.node.experiments_container.ExperimentsContainerNode.NavTarget
import info.degirona.creativelab.node.experiments_list.ExperimentsList
import info.degirona.creativelab.node.experiments_list.ExperimentsListBuilder
import info.degirona.creativelab.node.experiments_list.ExperimentsListNode
import info.degirona.creativelab.utils.asConsumer
import kotlinx.parcelize.Parcelize

class ExperimentsContainerNode internal constructor(
    buildContext: BuildContext,
    experimentsContainerView: ExperimentsContainerView,
    private val backStack: BackStack<NavTarget>,
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    view = experimentsContainerView,
    navModel = backStack
), ExperimentsContainer, Connectable<Input, Output> by NodeConnector() {

    sealed class NavTarget : Parcelable {
        @Parcelize
        data class ExperimentList(
            val experiments: List<ExperimentModel>
        ) : NavTarget()

        @Parcelize
        data class ExperimentType(
            val typeModel: ExperimentTypeModel
        ) : NavTarget()
    }

    override fun onBuilt() {
        super.onBuilt()

        whenChildAttached { commonLifecycle: Lifecycle, child: ExperimentsListNode ->
            commonLifecycle.createDestroy {
                bind(child.output to ::redirect.asConsumer())
            }
        }
    }


    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        when (navTarget) {
            is NavTarget.ExperimentList ->
                ExperimentsListBuilder(
                    object : ExperimentsList.Dependency {
                        override val experiments: List<ExperimentModel> = navTarget.experiments
                    }
                ).build(buildContext)

            is NavTarget.ExperimentType ->
                ExperimentBuilder(
                    object : Experiment.Dependency {
                        override val typeModel: ExperimentTypeModel = navTarget.typeModel
                    }
                ).build(buildContext)
        }

    private fun redirect(output: ExperimentsList.Output) {
        when (output) {
            is ExperimentsList.Output.ListExperiments ->
                backStack.push(NavTarget.ExperimentList(output.experimentModels))

            is ExperimentsList.Output.OpenExperimentType ->
                backStack.push(NavTarget.ExperimentType(output.typeModel))

        }
    }
}
