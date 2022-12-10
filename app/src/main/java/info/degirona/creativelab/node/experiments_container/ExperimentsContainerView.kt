package info.degirona.creativelab.node.experiments_container

import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.navigation.NavModel
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.ParentNodeView
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.transitionhandler.rememberBackstackSlider

class ExperimentsContainerView(
    private val backStack: NavModel<ExperimentsContainerNode.NavTarget, BackStack.State>,
) : ParentNodeView<ExperimentsContainerNode.NavTarget> {

    @Composable
    override fun ParentNode<ExperimentsContainerNode.NavTarget>.NodeView(modifier: Modifier) {
        ExperimentsContainerScreen(
            modifier = modifier
                .fillMaxSize()
        ) {
            Children(
                navModel = backStack,
                transitionHandler = rememberBackstackSlider(
                    transitionSpec = { spring() },
                ),
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
