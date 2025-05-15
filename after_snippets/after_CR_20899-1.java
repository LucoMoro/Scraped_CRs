
//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.TreeItem;

public class ProfileView extends Composite implements Observer {

private TreeViewer mTreeViewer;
private Text mSearchBox;
private SelectionController mSelectionController;
}
}
});

// Add a tree listener so that we can expand the parents and children
// of a method when a method is expanded.
mTreeViewer.addTreeListener(new ITreeViewerListener() {
ProfileNode[] nodes = md.getProfileNodes();
mTreeViewer.setExpandedState(md, true);
// Also expand the "Parents" and "Children" nodes.
        if (nodes != null) {
            for (ProfileNode node : nodes) {
                if (node.isRecursive() == false)
                    mTreeViewer.setExpandedState(node, true);
            }
}
}
}

//<End of snippet n. 0>








