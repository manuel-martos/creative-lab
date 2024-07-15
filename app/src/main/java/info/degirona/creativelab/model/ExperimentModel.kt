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
        data object SimpleStroke : Typography()

        @Parcelize
        data object StrokeAndAnimationV1 : Typography()

        @Parcelize
        data object StrokeAndAnimationV2 : Typography()

        @Parcelize
        data object StrokedReveal : Typography()

        @Parcelize
        data object NoiseReveal : Typography()
    }

    @Parcelize
    sealed class Scrolling : ExperimentType() {
        @Parcelize
        data object StarWars : Scrolling()
    }

    @Parcelize
    sealed class Particles : ExperimentType() {
        @Parcelize
        data object Gravity : Particles()
        @Parcelize
        data object Marbling : Particles()
    }

    @Parcelize
    sealed class Animation : ExperimentType() {
        @Parcelize
        data object ChemicalBeaker : Animation()

        @Parcelize
        data object FileEncryption : Animation()
    }

    @Parcelize
    sealed class MoirePatterns : ExperimentType() {
        @Parcelize
        data object Lines : MoirePatterns()
    }

    @Parcelize
    sealed class Transitions : ExperimentType() {
        @Parcelize
        data object NoiseTransition : Transitions()
    }

}