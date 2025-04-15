/*Fix possible NPE in Traceview.

Change-Id:I7834a4fee69ae103a5a39416aacea689e2010519*/




//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileView.java b/traceview/src/com/android/traceview/ProfileView.java
//Synthetic comment -- index e48cb56..71dedfd 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
import org.eclipse.swt.widgets.TreeItem;

public class ProfileView extends Composite implements Observer {

private TreeViewer mTreeViewer;
private Text mSearchBox;
private SelectionController mSelectionController;
//Synthetic comment -- @@ -199,7 +199,7 @@
}
}
});

// Add a tree listener so that we can expand the parents and children
// of a method when a method is expanded.
mTreeViewer.addTreeListener(new ITreeViewerListener() {
//Synthetic comment -- @@ -300,9 +300,11 @@
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







