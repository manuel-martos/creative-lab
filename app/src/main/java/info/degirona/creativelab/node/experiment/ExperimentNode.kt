package info.degirona.creativelab.node.experiment

import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.utils.interop.rx2.connectable.Connectable
import com.bumble.appyx.utils.interop.rx2.connectable.NodeConnector

internal class ExperimentNode(
    buildContext: BuildContext,
    experimentView: ExperimentView,
) : Node(
    buildContext = buildContext,
    view = experimentView
), Experiment, Connectable<Experiment.Input, Experiment.Output> by NodeConnector()