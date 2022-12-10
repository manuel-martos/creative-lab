package info.degirona.creativelab.node.experiments_list

import com.badoo.binder.using
import com.badoo.mvicore.android.lifecycle.startStop
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.interop.rx2.connectable.Connectable
import com.bumble.appyx.interop.rx2.connectable.NodeConnector

internal class ExperimentsListNode(
    buildContext: BuildContext,
    private val experimentsListView: ExperimentsListView,
) : Node(
    buildContext = buildContext,
    view = experimentsListView
), ExperimentsList, Connectable<ExperimentsList.Input, ExperimentsList.Output> by NodeConnector() {
    override fun onBuilt() {
        super.onBuilt()

        lifecycle.startStop {
            bind(experimentsListView to output using EventToOutput)
        }
    }
}