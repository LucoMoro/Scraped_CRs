//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.TreeItem;

public class ProfileView extends Composite implements Observer {
    
    private TreeViewer mTreeViewer;
    private Text mSearchBox;
    private SelectionController mSelectionController;
    private ModelData md; // Assuming md is declared somewhere in the class

    public void update(Observable o, Object arg) {
        if (mTreeViewer == null || md == null) return;

        ProfileNode[] nodes = md.getProfileNodes();
        if (nodes == null || nodes.length == 0) return;

        mTreeViewer.setExpandedState(md, true);
        for (ProfileNode node : nodes) {
            if (node != null && !node.isRecursive()) {
                mTreeViewer.setExpandedState(node, true);
            }
        }
    }
}

//<End of snippet n. 0>