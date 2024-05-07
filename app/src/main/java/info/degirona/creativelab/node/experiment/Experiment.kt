package info.degirona.creativelab.node.experiment

import com.bumble.appyx.interop.rx2.connectable.Connectable
import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.node.experiment.Experiment.Input
import info.degirona.creativelab.node.experiment.Experiment.Output

interface Experiment : Connectable<Input, Output> {
    sealed class Input

    sealed class Output

    interface Dependency {
        val typeModel: ExperimentModel.ExperimentTypeModel
    }
}