package info.degirona.creativelab.node.experiments_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.node.NodeView
import com.jakewharton.rxrelay2.PublishRelay
import info.degirona.creativelab.model.ExperimentModel
import info.degirona.creativelab.ui.experiments.Experiments
import io.reactivex.ObservableSource

class ExperimentsListView(
    private val experiments: List<ExperimentModel>,
    private val events: PublishRelay<Event> = PublishRelay.create(),
) : NodeView, ObservableSource<ExperimentsListView.Event> by events {

    sealed class Event {
        data class Clicked(val experimentModel: ExperimentModel) : Event()
    }

    @Composable
    override fun View(modifier: Modifier) {
        ExperimentsListScreen(
            modifier = modifier,
            experiments = experiments,
            onClick = { events.accept(Event.Clicked(it)) }
        )
    }
}

@Composable
internal fun ExperimentsListScreen(
    experiments: List<ExperimentModel>,
    onClick: (ExperimentModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Experiments(
        experiments = experiments,
        onClick = onClick,
        modifier = modifier,
    )
}
