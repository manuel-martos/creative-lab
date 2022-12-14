package info.degirona.creativelab.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ExperimentModel : Parcelable {
    abstract val title: String
    abstract val description: String

    @Parcelize
    data class ExperimentListModel(
        override val title: String,
        override val description: String,
        val experimentModels: List<ExperimentModel>
    ) : ExperimentModel()

    @Parcelize
    data class ExperimentTypeModel(
        override val title: String,
        override val description: String,
        val type: ExperimentType,
    ) : ExperimentModel()

}

@Parcelize
sealed class ExperimentType : Parcelable {
    @Parcelize
    sealed class Typography : ExperimentType() {
        @Parcelize
        object SimpleStroke : Typography()
        @Parcelize
        object StrokeAndAnimationV1 : Typography()
        @Parcelize
        object StrokeAndAnimationV2 : Typography()
        @Parcelize
        object StrokedReveal : Typography()
        @Parcelize
        object NoiseReveal : Typography()
    }
}