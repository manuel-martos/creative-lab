package info.degirona.creativelab.node.experiments_container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.navigation.node.ParentNodeView

class ExperimentsContainerView(
    private val backStack: BackStack<ExperimentsContainerNode.NavTarget>,
) : ParentNodeView<ExperimentsContainerNode.NavTarget> {

    @Composable
    override fun ParentNode<ExperimentsContainerNode.NavTarget>.NodeView(modifier: Modifier) {
        ExperimentsContainerScreen(
            modifier = modifier
                .fillMaxSize()
        ) {
            AppyxComponent(
                appyxComponent = backStack,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
internal fun ExperimentsContainerScreen(
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit
) {
    Column(modifier) {
        children()
    }
}
