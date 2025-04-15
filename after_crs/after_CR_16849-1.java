/*Fix broken plug-in

Change-Id:I8a6c8bd4b3d5ce0d7bcda5154c0187c256ab3db9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java
//Synthetic comment -- index 7104784..419a63b 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
public class HierarchyViewerPluginDirector extends HierarchyViewerDirector {

public static HierarchyViewerDirector createDirector() {
        return sDirector = new HierarchyViewerPluginDirector();
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/LayoutView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/LayoutView.java
//Synthetic comment -- index 2fb51a3..62f2043 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.ui.LayoutViewer;

import org.eclipse.jface.action.Action;
//Synthetic comment -- @@ -34,7 +34,7 @@
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class LayoutView extends ViewPart implements ITreeChangeListener {

public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.LayoutView"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectLoupeView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectLoupeView.java
//Synthetic comment -- index 1ec1a6f..f7e7789 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.actions.PixelPerfectAutoRefreshAction;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;
import com.android.hierarchyviewerlib.ui.PixelPerfectControls;
import com.android.hierarchyviewerlib.ui.PixelPerfectLoupe;
import com.android.hierarchyviewerlib.ui.PixelPerfectPixelPanel;
//Synthetic comment -- @@ -38,7 +38,7 @@
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class PixelPerfectLoupeView extends ViewPart implements IImageChangeListener {

public static final String ID =
"com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectLoupeView"; //$NON-NLS-1$







