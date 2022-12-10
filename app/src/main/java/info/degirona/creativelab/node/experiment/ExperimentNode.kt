package info.degirona.creativelab.node.experiment

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.interop.rx2.connectable.Connectable
import com.bumble.appyx.interop.rx2.connectable.NodeConnector

internal class ExperimentNode(
    buildContext: BuildContext,
    experimentView: ExperimentView,
) : Node(
    buildContext = buildContext,
    view = experimentView
), Experiment, Connectable<Experiment.Input, Experiment.Output> by NodeConnector()