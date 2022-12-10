package info.degirona.creativelab.node.experiment

import info.degirona.creativelab.node.experiment.Experiment.Input
import info.degirona.creativelab.node.experiment.Experiment.Output
import com.bumble.appyx.interop.rx2.connectable.Connectable
import info.degirona.creativelab.model.ExperimentModel

interface Experiment : Connectable<Input, Output> {
    sealed class Input {
    }

    sealed class Output {

    }

    interface Dependency {
        val typeModel: ExperimentModel.ExperimentTypeModel
    }
}