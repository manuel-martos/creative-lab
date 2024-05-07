package info.degirona.creativelab.node.experiments_container

import com.bumble.appyx.interop.rx2.connectable.Connectable
import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.node.experiments_container.ExperimentsContainer.Input
import info.degirona.creativelab.node.experiments_container.ExperimentsContainer.Output

interface ExperimentsContainer : Connectable<Input, Output> {
    sealed class Input

    sealed class Output

    interface Dependency {
        val initialExperiments: List<ExperimentModel>
    }
}