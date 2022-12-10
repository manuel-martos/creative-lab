package info.degirona.creativelab.node.experiments_list

import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.node.experiments_list.ExperimentsList.Output
import info.degirona.creativelab.node.experiments_list.ExperimentsList.Output.ListExperiments
import info.degirona.creativelab.node.experiments_list.ExperimentsList.Output.OpenExperimentType
import info.degirona.creativelab.node.experiments_list.ExperimentsListView.Event
import info.degirona.creativelab.node.experiments_list.ExperimentsListView.Event.Clicked

object EventToOutput : (Event) -> Output {

    override fun invoke(event: Event): Output =
        when (event) {
            is Clicked -> event.experimentModel.toOutput()
        }

    private fun ExperimentModel.toOutput() =
        when (this) {
            is ExperimentModel.ExperimentListModel -> ListExperiments(experimentModels)
            is ExperimentModel.ExperimentTypeModel -> OpenExperimentType(this)
        }
}