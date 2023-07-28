package info.degirona.creativelab.node.experiments_list

import com.bumble.appyx.utils.interop.rx2.connectable.Connectable
import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.model.ExperimentModel.ExperimentTypeModel
import info.degirona.creativelab.node.experiments_list.ExperimentsList.Input
import info.degirona.creativelab.node.experiments_list.ExperimentsList.Output

interface ExperimentsList : Connectable<Input, Output> {
    sealed class Input {
    }

    sealed class Output {
        data class ListExperiments(val experimentModels: List<ExperimentModel>) : Output()
        data class OpenExperimentType(val typeModel: ExperimentTypeModel) : Output()
    }

    interface Dependency {
        val experiments: List<ExperimentModel>
    }
}