//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.TreeItem;

public class ProfileView extends Composite implements Observer {
    
    private TreeViewer mTreeViewer;
    private Text mSearchBox;
    private SelectionController mSelectionController;

    public void addTreeListener(ProfileData md) {
        if (md != null) {
            ProfileNode[] nodes = md.getProfileNodes();
            if (nodes != null) {
                mTreeViewer.setExpandedState(md, true);
                if (mTreeViewer != null) {
                    for (ProfileNode node : nodes) {
                        if (node != null && !node.isRecursive()) {
                            mTreeViewer.setExpandedState(node, true);
                        }
                    }
                }
            }
        }
    }
}

//<End of snippet n. 0>