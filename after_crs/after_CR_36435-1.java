/*Preserve tree expansion state in the Eclipse lint view

Change-Id:Ic09bb914e46f64225da215c66832d54e3a0bb3d8*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintList.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintList.java
//Synthetic comment -- index c40f3b7..561c5fd 100644

//Synthetic comment -- @@ -48,7 +48,6 @@
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
//Synthetic comment -- @@ -64,6 +63,8 @@
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//Synthetic comment -- @@ -83,6 +84,7 @@
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
//Synthetic comment -- @@ -103,6 +105,7 @@
private final IWorkbenchPartSite mSite;
private final TreeViewer mTreeViewer;
private final Tree mTree;
    private Set<String> mExpandedIds;
private ContentProvider mContentProvider;
private String mSelectedId;
private List<? extends IResource> mResources;
//Synthetic comment -- @@ -179,6 +182,34 @@
}
}
});
        mTree.addTreeListener(new TreeListener() {
            @Override
            public void treeExpanded(TreeEvent e) {
                Object data = e.item.getData();
                if (data instanceof IMarker) {
                    String id = EclipseLintClient.getId((IMarker) data);
                    if (id != null) {
                        if (mExpandedIds == null) {
                            mExpandedIds = new HashSet<String>();
                        }
                        mExpandedIds.add(id);
                    }
                }
            }

            @Override
            public void treeCollapsed(TreeEvent e) {
                if (mExpandedIds != null) {
                    Object data = e.item.getData();
                    if (data instanceof IMarker) {
                        String id = EclipseLintClient.getId((IMarker) data);
                        if (id != null) {
                            mExpandedIds.remove(id);
                        }
                    }
                }
            }
        });
}

private boolean treePainted;
//Synthetic comment -- @@ -479,9 +510,6 @@
return Status.CANCEL_STATUS;
}

mTreeViewer.setInput(null);
List<IMarker> markerList = getMarkers();
if (markerList.size() == 0) {
//Synthetic comment -- @@ -504,8 +532,21 @@
mTreeViewer.setInput(markerList);
mTreeViewer.refresh();

            if (mExpandedIds != null) {
                List<IMarker> expanded = new ArrayList<IMarker>(mExpandedIds.size());
                IMarker[] topMarkers = mContentProvider.getTopMarkers();
                if (topMarkers != null) {
                    for (IMarker marker : topMarkers) {
                        String id = EclipseLintClient.getId(marker);
                        if (id != null && mExpandedIds.contains(id)) {
                            expanded.add(marker);
                        }
                    }
                }
                if (!expanded.isEmpty()) {
                    mTreeViewer.setExpandedElements(expanded.toArray());
                }
            }

if (mSelectedId != null) {
IMarker[] topMarkers = mContentProvider.getTopMarkers();







