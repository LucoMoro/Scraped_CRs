/*Renaming and adding  //$NON-NLS-?$

Change-Id:I9f5fa1625af4b35499cfc87996d0b3a39841ba31*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java
//Synthetic comment -- index 8996cdb..8f94c65 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.hierarchyviewer;

import com.android.ddmlib.Log;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;
//Synthetic comment -- @@ -40,14 +39,14 @@
*/
public class HierarchyViewerPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.android.ide.eclipse.hierarchyviewer"; //$NON-NLS-1$

    public static final String ADB_LOCATION = PLUGIN_ID + ".adb"; //$NON-NLS-1$

// The shared instance
    private static HierarchyViewerPlugin sPlugin;

    private Color mRedColor;

/**
* The constructor
//Synthetic comment -- @@ -58,18 +57,18 @@
@Override
public void start(BundleContext context) throws Exception {
super.start(context);
        sPlugin = this;


// set the consoles.
        final MessageConsole messageConsole = new MessageConsole("Hierarchy Viewer", null); //$NON-NLS-1$
ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] {
messageConsole
});

final MessageConsoleStream consoleStream = messageConsole.newMessageStream();
final MessageConsoleStream errorConsoleStream = messageConsole.newMessageStream();
        mRedColor = new Color(Display.getDefault(), 0xFF, 0x00, 0x00);

// because this can be run, in some cases, by a non UI thread, and
// because
//Synthetic comment -- @@ -78,7 +77,7 @@
// in the UI thread.
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                errorConsoleStream.setColor(mRedColor);
}
});

//Synthetic comment -- @@ -131,10 +130,10 @@
*/
@Override
public void stop(BundleContext context) throws Exception {
        sPlugin = null;
super.stop(context);

        mRedColor.dispose();

HierarchyViewerDirector director = HierarchyViewerDirector.getDirector();
director.stopListenForDevices();
//Synthetic comment -- @@ -148,7 +147,7 @@
* @return the shared instance
*/
public static HierarchyViewerPlugin getPlugin() {
        return sPlugin;
}

/**
//Synthetic comment -- @@ -160,7 +159,7 @@
public static void setAdb(String adb, boolean startAdb) {
if (adb != null) {
// store the location for future ddms only start.
            sPlugin.getPreferenceStore().setValue(ADB_LOCATION, adb);

// starts the server in a thread in case this is blocking.
if (startAdb) {
//Synthetic comment -- @@ -201,9 +200,9 @@
Calendar c = Calendar.getInstance();

if (tag == null) {
            return String.format("[%1$tF %1$tT]", c); //$NON-NLS-1$
}

        return String.format("[%1$tF %1$tT - %2$s]", c, tag); //$NON-NLS-1$
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java
//Synthetic comment -- index f1374ae..7104784 100644

//Synthetic comment -- @@ -48,11 +48,11 @@
}
};
job.setPriority(Job.SHORT);
        job.setRule(mSchedulingRule);
job.schedule();
}

    private ISchedulingRule mSchedulingRule = new ISchedulingRule() {
public boolean contains(ISchedulingRule rule) {
return rule == this;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/PixelPerfectPespective.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/PixelPerfectPespective.java
//Synthetic comment -- index bcaf8f9..def2864 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
public class PixelPerfectPespective implements IPerspectiveFactory {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.PixelPerfectPespective"; //$NON-NLS-1$

public void createInitialLayout(IPageLayout layout) {
layout.setEditorAreaVisible(false);
//Synthetic comment -- @@ -37,14 +37,14 @@
String editorArea = layout.getEditorArea();
IFolderLayout folder;

        folder = layout.createFolder("tree", IPageLayout.LEFT, 0.25f, editorArea); //$NON-NLS-1$
folder.addView(DeviceSelectorView.ID);
folder.addView(PixelPerfectTreeView.ID);

        folder = layout.createFolder("overview", IPageLayout.RIGHT, 0.4f, editorArea); //$NON-NLS-1$
folder.addView(PixelPerfectView.ID);

        folder = layout.createFolder("main", IPageLayout.RIGHT, 0.35f, editorArea); //$NON-NLS-1$
folder.addView(PixelPerfectLoupeView.ID);


//Synthetic comment -- @@ -53,7 +53,7 @@
layout.addShowViewShortcut(PixelPerfectLoupeView.ID);
layout.addShowViewShortcut(PixelPerfectView.ID);

        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective"); //$NON-NLS-1$
layout.addPerspectiveShortcut(TreeViewPerspective.ID);
layout.addPerspectiveShortcut(Perspective.ID);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/TreeViewPerspective.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/TreeViewPerspective.java
//Synthetic comment -- index 7022509..09fca66 100644

//Synthetic comment -- @@ -29,7 +29,7 @@

public class TreeViewPerspective implements IPerspectiveFactory {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.TreeViewPerspective"; //$NON-NLS-1$

public void createInitialLayout(IPageLayout layout) {
layout.setEditorAreaVisible(false);
//Synthetic comment -- @@ -37,18 +37,18 @@
String editorArea = layout.getEditorArea();
IFolderLayout folder;

        folder = layout.createFolder("properties", IPageLayout.LEFT, 0.10f, editorArea); //$NON-NLS-1$
folder.addView(DeviceSelectorView.ID);
folder.addView(PropertyView.ID);

        folder = layout.createFolder("main", IPageLayout.RIGHT, 0.24f, "properties"); //$NON-NLS-1$ //$NON-NLS-2$
folder.addView(TreeViewView.ID);

        folder = layout.createFolder("panel-top", IPageLayout.RIGHT, 0.7f, "main"); //$NON-NLS-1$ //$NON-NLS-2$
folder.addView(TreeOverviewView.ID);


        folder = layout.createFolder("panel-bottom", IPageLayout.BOTTOM, 0.5f, "panel-top"); //$NON-NLS-1$ //$NON-NLS-2$
folder.addView(LayoutView.ID);

layout.addShowViewShortcut(DeviceSelectorView.ID);
//Synthetic comment -- @@ -57,7 +57,7 @@
layout.addShowViewShortcut(LayoutView.ID);
layout.addShowViewShortcut(TreeViewView.ID);

        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective"); //$NON-NLS-1$
layout.addPerspectiveShortcut(PixelPerfectPespective.ID);
layout.addPerspectiveShortcut(Perspective.ID);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/DeviceSelectorView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/DeviceSelectorView.java
//Synthetic comment -- index c884115..e2fac78 100644

//Synthetic comment -- @@ -36,9 +36,9 @@
public class DeviceSelectorView extends ViewPart implements IPerspectiveListener {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.DeviceSelectorView"; //$NON-NLS-1$

    private DeviceSelector mDeviceSelector;

@Override
public void createPartControl(Composite parent) {
//Synthetic comment -- @@ -53,7 +53,7 @@
} else if (perspective.getId().equals(TreeViewPerspective.ID)) {
doPixelPerfectStuff = false;
}
        mDeviceSelector = new DeviceSelector(parent, doTreeViewStuff, doPixelPerfectStuff);

placeActions(doTreeViewStuff, doPixelPerfectStuff);

//Synthetic comment -- @@ -93,18 +93,18 @@

@Override
public void setFocus() {
        mDeviceSelector.setFocus();
}

public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
if (perspective.getId().equals(PixelPerfectPespective.ID)) {
            mDeviceSelector.setMode(false, true);
placeActions(false, true);
} else if (perspective.getId().equals(TreeViewPerspective.ID)) {
            mDeviceSelector.setMode(true, false);
placeActions(true, false);
} else {
            mDeviceSelector.setMode(true, true);
placeActions(true, true);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/LayoutView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/LayoutView.java
//Synthetic comment -- index be401b6..2fb51a3 100644

//Synthetic comment -- @@ -36,68 +36,68 @@

public class LayoutView extends ViewPart implements TreeChangeListener {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.LayoutView"; //$NON-NLS-1$

    private LayoutViewer mLayoutViewer;

    private Image mOnBlack;

    private Image mOnWhite;

    private Action mShowExtrasAction = new Action("Show &Extras", Action.AS_CHECK_BOX) {
@Override
public void run() {
            mLayoutViewer.setShowExtras(isChecked());
}
};

    private Action mLoadAllViewsAction = new Action("Load All &Views") {
@Override
public void run() {
HierarchyViewerDirector.getDirector().loadAllViews();
            mShowExtrasAction.setChecked(true);
            mLayoutViewer.setShowExtras(true);
}
};

    private Action mOnBlackWhiteAction = new Action("Change Background &Color") {
@Override
public void run() {
            boolean newValue = !mLayoutViewer.getOnBlack();
            mLayoutViewer.setOnBlack(newValue);
if (newValue) {
                setImageDescriptor(ImageDescriptor.createFromImage(mOnWhite));
} else {
                setImageDescriptor(ImageDescriptor.createFromImage(mOnBlack));
}
}
};

@Override
public void createPartControl(Composite parent) {
        mShowExtrasAction.setAccelerator(SWT.MOD1 + 'E');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("show-extras.png", Display.getDefault()); //$NON-NLS-1$
        mShowExtrasAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        mShowExtrasAction.setToolTipText("Show images");
        mShowExtrasAction.setEnabled(TreeViewModel.getModel().getTree() != null);

        mOnWhite = imageLoader.loadImage("on-white.png", Display.getDefault()); //$NON-NLS-1$
        mOnBlack = imageLoader.loadImage("on-black.png", Display.getDefault()); //$NON-NLS-1$

        mOnBlackWhiteAction.setAccelerator(SWT.MOD1 + 'C');
        mOnBlackWhiteAction.setImageDescriptor(ImageDescriptor.createFromImage(mOnWhite));
        mOnBlackWhiteAction.setToolTipText("Change layout viewer background color");

        mLoadAllViewsAction.setAccelerator(SWT.MOD1 + 'V');
        image = imageLoader.loadImage("load-all-views.png", Display.getDefault()); //$NON-NLS-1$
        mLoadAllViewsAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        mLoadAllViewsAction.setToolTipText("Load all view images");
        mLoadAllViewsAction.setEnabled(TreeViewModel.getModel().getTree() != null);

parent.setLayout(new FillLayout());

        mLayoutViewer = new LayoutViewer(parent);

placeActions();

//Synthetic comment -- @@ -109,15 +109,15 @@

IMenuManager mm = actionBars.getMenuManager();
mm.removeAll();
        mm.add(mOnBlackWhiteAction);
        mm.add(mShowExtrasAction);
        mm.add(mLoadAllViewsAction);

IToolBarManager tm = actionBars.getToolBarManager();
tm.removeAll();
        tm.add(mOnBlackWhiteAction);
        tm.add(mShowExtrasAction);
        tm.add(mLoadAllViewsAction);
}

@Override
//Synthetic comment -- @@ -128,7 +128,7 @@

@Override
public void setFocus() {
        mLayoutViewer.setFocus();
}

public void selectionChanged() {
//Synthetic comment -- @@ -138,8 +138,8 @@
public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mLoadAllViewsAction.setEnabled(TreeViewModel.getModel().getTree() != null);
                mShowExtrasAction.setEnabled(TreeViewModel.getModel().getTree() != null);
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectLoupeView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectLoupeView.java
//Synthetic comment -- index 05777c4..1ec1a6f 100644

//Synthetic comment -- @@ -41,24 +41,24 @@
public class PixelPerfectLoupeView extends ViewPart implements ImageChangeListener {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectLoupeView"; //$NON-NLS-1$

    private PixelPerfectLoupe mPixelPerfectLoupe;

    private Action mShowInLoupeAction = new Action("&Show Overlay", Action.AS_CHECK_BOX) {
@Override
public void run() {
            mPixelPerfectLoupe.setShowOverlay(isChecked());
}
};
@Override
public void createPartControl(Composite parent) {
        mShowInLoupeAction.setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("show-overlay.png", Display.getDefault()); //$NON-NLS-1$
        mShowInLoupeAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        mShowInLoupeAction.setToolTipText("Show the overlay in the loupe view");
        mShowInLoupeAction.setEnabled(PixelPerfectModel.getModel().getOverlayImage() != null);
PixelPerfectModel.getModel().addImageChangeListener(this);

GridLayout loupeLayout = new GridLayout();
//Synthetic comment -- @@ -75,8 +75,8 @@
pixelPerfectLoupeBorderGridLayout.verticalSpacing = 0;
pixelPerfectLoupeBorder.setLayout(pixelPerfectLoupeBorderGridLayout);

        mPixelPerfectLoupe = new PixelPerfectLoupe(pixelPerfectLoupeBorder);
        mPixelPerfectLoupe.setLayoutData(new GridData(GridData.FILL_BOTH));

PixelPerfectPixelPanel pixelPerfectPixelPanel =
new PixelPerfectPixelPanel(pixelPerfectLoupeBorder);
//Synthetic comment -- @@ -95,12 +95,12 @@
IMenuManager mm = actionBars.getMenuManager();
mm.removeAll();
mm.add(PixelPerfectAutoRefreshAction.getAction());
        mm.add(mShowInLoupeAction);

IToolBarManager tm = actionBars.getToolBarManager();
tm.removeAll();
tm.add(PixelPerfectAutoRefreshAction.getAction());
        tm.add(mShowInLoupeAction);
}

@Override
//Synthetic comment -- @@ -111,7 +111,7 @@

@Override
public void setFocus() {
        mPixelPerfectLoupe.setFocus();
}

public void crosshairMoved() {
//Synthetic comment -- @@ -130,7 +130,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
                mShowInLoupeAction.setEnabled(overlayImage != null);
}
});
}
//Synthetic comment -- @@ -138,7 +138,8 @@
public void overlayChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mShowInLoupeAction
                        .setEnabled(PixelPerfectModel.getModel().getOverlayImage() != null);
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectTreeView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectTreeView.java
//Synthetic comment -- index 811b8fc..f3591f7 100644

//Synthetic comment -- @@ -29,14 +29,14 @@
public class PixelPerfectTreeView extends ViewPart {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectTreeView"; //$NON-NLS-1$

    private PixelPerfectTree mPixelPerfectTree;

@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());
        mPixelPerfectTree = new PixelPerfectTree(parent);

placeActions();
}
//Synthetic comment -- @@ -55,7 +55,7 @@

@Override
public void setFocus() {
        mPixelPerfectTree.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectView.java
//Synthetic comment -- index a09eb5c..943a6c8 100644

//Synthetic comment -- @@ -31,14 +31,14 @@
public class PixelPerfectView extends ViewPart {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectView"; //$NON-NLS-1$

    private PixelPerfect mPixelPerfect;

@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());
        mPixelPerfect = new PixelPerfect(parent);

placeActions();
}
//Synthetic comment -- @@ -61,7 +61,7 @@

@Override
public void setFocus() {
        mPixelPerfect.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PropertyView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PropertyView.java
//Synthetic comment -- index bd6d462..74f50fd 100644

//Synthetic comment -- @@ -24,20 +24,20 @@

public class PropertyView extends ViewPart {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.PropertyView"; //$NON-NLS-1$

    private PropertyViewer mPropertyViewer;

@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());

        mPropertyViewer = new PropertyViewer(parent);
}

@Override
public void setFocus() {
        mPropertyViewer.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeOverviewView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeOverviewView.java
//Synthetic comment -- index 5363c1b..e18771b 100644

//Synthetic comment -- @@ -25,19 +25,19 @@
public class TreeOverviewView extends ViewPart {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.TreeOverviewView"; //$NON-NLS-1$

    private TreeViewOverview mTreeViewOverview;
@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());

        mTreeViewOverview = new TreeViewOverview(parent);
}

@Override
public void setFocus() {
        mTreeViewOverview.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeViewView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeViewView.java
//Synthetic comment -- index 76d9b54..f24f420 100644

//Synthetic comment -- @@ -39,9 +39,9 @@
// Awesome name.
public class TreeViewView extends ViewPart {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.TreeViewView"; //$NON-NLS-1$

    private TreeView mTreeView;

@Override
public void createPartControl(Composite parent) {
//Synthetic comment -- @@ -54,7 +54,7 @@
treeViewContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
treeViewContainer.setLayout(new FillLayout());

        mTreeView = new TreeView(treeViewContainer);

TreeViewControls treeViewControls = new TreeViewControls(parent);
treeViewControls.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -91,7 +91,7 @@

@Override
public void setFocus() {
        mTreeView.setFocus();
}

}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/AboutDialog.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/AboutDialog.java
//Synthetic comment -- index 54edbc8..3f973e7 100644

//Synthetic comment -- @@ -34,15 +34,15 @@
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends Dialog {
    private Image mAboutImage;

    private Image mSmallImage;

public AboutDialog(Shell shell) {
super(shell);
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mSmallImage = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault()); //$NON-NLS-1$
        mAboutImage = imageLoader.loadImage("about.jpg", Display.getDefault()); //$NON-NLS-1$
}

@Override
//Synthetic comment -- @@ -58,14 +58,14 @@
imageControl.setLayout(new FillLayout());
imageControl.setLayoutData(new GridData(GridData.FILL_VERTICAL));
Label imageLabel = new Label(imageControl, SWT.CENTER);
        imageLabel.setImage(mAboutImage);

CLabel textLabel = new CLabel(control, SWT.NONE);
textLabel
.setText("Hierarchy Viewer\nCopyright 2010, The Android Open Source Project\nAll Rights Reserved.");
textLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
getShell().setText("About...");
        getShell().setImage(mSmallImage);
return control;

}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index f5c6e98..df4f08f 100644

//Synthetic comment -- @@ -39,8 +39,8 @@
import com.android.hierarchyviewerlib.actions.SaveTreeViewAction;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.ui.DeviceSelector;
import com.android.hierarchyviewerlib.ui.LayoutViewer;
import com.android.hierarchyviewerlib.ui.PixelPerfect;
//Synthetic comment -- @@ -84,74 +84,74 @@

private static final int INITIAL_HEIGHT = 768;

    private static HierarchyViewerApplication mApp;

// Images for moving between the 3 main windows.

    private Image mDeviceViewImage;

    private Image mPixelPerfectImage;

    private Image mTreeViewImage;

    private Image mDeviceViewSelectedImage;

    private Image mPixelPerfectSelectedImage;

    private Image mTreeViewSelectedImage;

// And their buttons

    private Button mTreeViewButton;

    private Button mPixelPerfectButton;

    private Button mDeviceViewButton;

    private Label mProgressLabel;

    private ProgressBar mProgressBar;

    private String mProgressString;

    private Composite mDeviceSelectorPanel;

    private Composite mTreeViewPanel;

    private Composite mPixelPerfectPanel;

    private StackLayout mMainWindowStackLayout;

    private DeviceSelector mDeviceSelector;

    private Composite mStatusBar;

    private TreeView mTreeView;

    private Composite mMainWindow;

    private Image mOnBlackImage;

    private Image mOnWhiteImage;

    private Button mOnBlackWhiteButton;

    private Button mShowExtras;

    private LayoutViewer mLayoutViewer;

    private PixelPerfectLoupe mPixelPerfectLoupe;

    private Composite mTreeViewControls;

public static final HierarchyViewerApplication getApp() {
        return mApp;
}

public HierarchyViewerApplication() {
super(null);

        mApp = this;

addMenuBar();
}
//Synthetic comment -- @@ -161,7 +161,7 @@
super.configureShell(shell);
shell.setText("Hierarchy Viewer");
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault()); //$NON-NLS-1$
shell.setImage(image);
}

//Synthetic comment -- @@ -203,17 +203,17 @@

private void loadResources() {
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mTreeViewImage = imageLoader.loadImage("tree-view.png", Display.getDefault()); //$NON-NLS-1$
        mTreeViewSelectedImage =
                imageLoader.loadImage("tree-view-selected.png", Display.getDefault()); //$NON-NLS-1$
        mPixelPerfectImage = imageLoader.loadImage("pixel-perfect-view.png", Display.getDefault()); //$NON-NLS-1$
        mPixelPerfectSelectedImage =
                imageLoader.loadImage("pixel-perfect-view-selected.png", Display.getDefault()); //$NON-NLS-1$
        mDeviceViewImage = imageLoader.loadImage("device-view.png", Display.getDefault()); //$NON-NLS-1$
        mDeviceViewSelectedImage =
                imageLoader.loadImage("device-view-selected.png", Display.getDefault()); //$NON-NLS-1$
        mOnBlackImage = imageLoader.loadImage("on-black.png", Display.getDefault()); //$NON-NLS-1$
        mOnWhiteImage = imageLoader.loadImage("on-white.png", Display.getDefault()); //$NON-NLS-1$
}

@Override
//Synthetic comment -- @@ -225,14 +225,14 @@
mainLayout.marginHeight = mainLayout.marginWidth = 0;
mainLayout.verticalSpacing = mainLayout.horizontalSpacing = 0;
control.setLayout(mainLayout);
        mMainWindow = new Composite(control, SWT.NONE);
        mMainWindow.setLayoutData(new GridData(GridData.FILL_BOTH));
        mMainWindowStackLayout = new StackLayout();
        mMainWindow.setLayout(mMainWindowStackLayout);

        buildDeviceSelectorPanel(mMainWindow);
        buildTreeViewPanel(mMainWindow);
        buildPixelPerfectPanel(mMainWindow);

buildStatusBar(control);

//Synthetic comment -- @@ -243,78 +243,78 @@


private void buildStatusBar(Composite parent) {
        mStatusBar = new Composite(parent, SWT.NONE);
        mStatusBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

FormLayout statusBarLayout = new FormLayout();
statusBarLayout.marginHeight = statusBarLayout.marginWidth = 2;

        mStatusBar.setLayout(statusBarLayout);

        mDeviceViewButton = new Button(mStatusBar, SWT.TOGGLE);
        mDeviceViewButton.setImage(mDeviceViewImage);
        mDeviceViewButton.setToolTipText("Switch to the window selection view");
        mDeviceViewButton.addSelectionListener(deviceViewButtonSelectionListener);
FormData deviceViewButtonFormData = new FormData();
deviceViewButtonFormData.left = new FormAttachment();
        mDeviceViewButton.setLayoutData(deviceViewButtonFormData);

        mTreeViewButton = new Button(mStatusBar, SWT.TOGGLE);
        mTreeViewButton.setImage(mTreeViewImage);
        mTreeViewButton.setEnabled(false);
        mTreeViewButton.setToolTipText("Switch to the tree view");
        mTreeViewButton.addSelectionListener(treeViewButtonSelectionListener);
FormData treeViewButtonFormData = new FormData();
        treeViewButtonFormData.left = new FormAttachment(mDeviceViewButton, 2);
        mTreeViewButton.setLayoutData(treeViewButtonFormData);

        mPixelPerfectButton = new Button(mStatusBar, SWT.TOGGLE);
        mPixelPerfectButton.setImage(mPixelPerfectImage);
        mPixelPerfectButton.setEnabled(false);
        mPixelPerfectButton.setToolTipText("Switch to the pixel perfect view");
        mPixelPerfectButton.addSelectionListener(pixelPerfectButtonSelectionListener);
FormData pixelPerfectButtonFormData = new FormData();
        pixelPerfectButtonFormData.left = new FormAttachment(mTreeViewButton, 2);
        mPixelPerfectButton.setLayoutData(pixelPerfectButtonFormData);

// Tree View control panel...
        mTreeViewControls = new TreeViewControls(mStatusBar);
FormData treeViewControlsFormData = new FormData();
        treeViewControlsFormData.left = new FormAttachment(mPixelPerfectButton, 2);
        treeViewControlsFormData.top = new FormAttachment(mTreeViewButton, 0, SWT.CENTER);
treeViewControlsFormData.width = 552;
        mTreeViewControls.setLayoutData(treeViewControlsFormData);

// Progress stuff
        mProgressLabel = new Label(mStatusBar, SWT.RIGHT);

        mProgressBar = new ProgressBar(mStatusBar, SWT.HORIZONTAL | SWT.INDETERMINATE | SWT.SMOOTH);
FormData progressBarFormData = new FormData();
progressBarFormData.right = new FormAttachment(100, 0);
        progressBarFormData.top = new FormAttachment(mTreeViewButton, 0, SWT.CENTER);
        mProgressBar.setLayoutData(progressBarFormData);

FormData progressLabelFormData = new FormData();
        progressLabelFormData.right = new FormAttachment(mProgressBar, -2);
        progressLabelFormData.top = new FormAttachment(mTreeViewButton, 0, SWT.CENTER);
        mProgressLabel.setLayoutData(progressLabelFormData);

        if (mProgressString == null) {
            mProgressLabel.setVisible(false);
            mProgressBar.setVisible(false);
} else {
            mProgressLabel.setText(mProgressString);
}
}

private void buildDeviceSelectorPanel(Composite parent) {
        mDeviceSelectorPanel = new Composite(parent, SWT.NONE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = gridLayout.marginHeight = 0;
gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        mDeviceSelectorPanel.setLayout(gridLayout);

        Composite buttonPanel = new Composite(mDeviceSelectorPanel, SWT.NONE);
buttonPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

GridLayout buttonLayout = new GridLayout();
//Synthetic comment -- @@ -341,20 +341,20 @@
new ActionButton(innerButtonPanel, InspectScreenshotAction.getAction());
inspectScreenshotButton.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite deviceSelectorContainer = new Composite(mDeviceSelectorPanel, SWT.BORDER);
deviceSelectorContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
deviceSelectorContainer.setLayout(new FillLayout());
        mDeviceSelector = new DeviceSelector(deviceSelectorContainer, true, true);
}

public void buildTreeViewPanel(Composite parent) {
        mTreeViewPanel = new Composite(parent, SWT.NONE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = gridLayout.marginHeight = 0;
gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        mTreeViewPanel.setLayout(gridLayout);

        Composite buttonPanel = new Composite(mTreeViewPanel, SWT.NONE);
buttonPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

GridLayout buttonLayout = new GridLayout();
//Synthetic comment -- @@ -392,11 +392,11 @@
new ActionButton(innerButtonPanel, RequestLayoutAction.getAction());
requestLayout.setLayoutData(new GridData(GridData.FILL_BOTH));

        SashForm mainSash = new SashForm(mTreeViewPanel, SWT.HORIZONTAL | SWT.SMOOTH);
mainSash.setLayoutData(new GridData(GridData.FILL_BOTH));
Composite treeViewContainer = new Composite(mainSash, SWT.BORDER);
treeViewContainer.setLayout(new FillLayout());
        mTreeView = new TreeView(treeViewContainer);

SashForm sideSash = new SashForm(mainSash, SWT.VERTICAL | SWT.SMOOTH);

//Synthetic comment -- @@ -436,15 +436,15 @@
rowLayout.center = true;
buttonBar.setLayout(rowLayout);

        mOnBlackWhiteButton = new Button(buttonBar, SWT.PUSH);
        mOnBlackWhiteButton.setImage(mOnWhiteImage);
        mOnBlackWhiteButton.addSelectionListener(onBlackWhiteSelectionListener);
        mOnBlackWhiteButton.setToolTipText("Change layout viewer background color");

        mShowExtras = new Button(buttonBar, SWT.CHECK);
        mShowExtras.setText("Show Extras");
        mShowExtras.addSelectionListener(showExtrasSelectionListener);
        mShowExtras.setToolTipText("Show images");

ActionButton loadAllViewsButton =
new ActionButton(fullButtonBar, LoadAllViewsAction.getAction());
//Synthetic comment -- @@ -454,7 +454,7 @@
Composite layoutViewerMainContainer = new Composite(layoutViewerContainer, SWT.BORDER);
layoutViewerMainContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
layoutViewerMainContainer.setLayout(new FillLayout());
        mLayoutViewer = new LayoutViewer(layoutViewerMainContainer);

sideSash.SASH_WIDTH = 4;
sideSash.setWeights(new int[] {
//Synthetic comment -- @@ -464,13 +464,13 @@
}

private void buildPixelPerfectPanel(Composite parent) {
        mPixelPerfectPanel = new Composite(parent, SWT.NONE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = gridLayout.marginHeight = 0;
gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        mPixelPerfectPanel.setLayout(gridLayout);

        Composite buttonPanel = new Composite(mPixelPerfectPanel, SWT.NONE);
buttonPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

GridLayout buttonLayout = new GridLayout();
//Synthetic comment -- @@ -509,7 +509,7 @@
new ActionButton(innerButtonPanel, PixelPerfectAutoRefreshAction.getAction());
autoRefresh.setLayoutData(new GridData(GridData.FILL_BOTH));

        SashForm mainSash = new SashForm(mPixelPerfectPanel, SWT.HORIZONTAL | SWT.SMOOTH);
mainSash.setLayoutData(new GridData(GridData.FILL_BOTH));
mainSash.SASH_WIDTH = 4;

//Synthetic comment -- @@ -532,8 +532,8 @@
pixelPerfectLoupeBorderGridLayout.verticalSpacing = 0;
pixelPerfectLoupeBorder.setLayout(pixelPerfectLoupeBorderGridLayout);

        mPixelPerfectLoupe = new PixelPerfectLoupe(pixelPerfectLoupeBorder);
        mPixelPerfectLoupe.setLayoutData(new GridData(GridData.FILL_BOTH));

PixelPerfectPixelPanel pixelPerfectPixelPanel =
new PixelPerfectPixelPanel(pixelPerfectLoupeBorder);
//Synthetic comment -- @@ -555,19 +555,19 @@
}

public void showOverlayInLoupe(boolean value) {
        mPixelPerfectLoupe.setShowOverlay(value);
}

// Shows the progress notification...
public void startTask(final String taskName) {
        mProgressString = taskName;
Display.getDefault().syncExec(new Runnable() {
public void run() {
                if (mProgressLabel != null && mProgressBar != null) {
                    mProgressLabel.setText(taskName);
                    mProgressLabel.setVisible(true);
                    mProgressBar.setVisible(true);
                    mStatusBar.layout();
}
}
});
//Synthetic comment -- @@ -575,12 +575,12 @@

// And hides it!
public void endTask() {
        mProgressString = null;
Display.getDefault().syncExec(new Runnable() {
public void run() {
                if (mProgressLabel != null && mProgressBar != null) {
                    mProgressLabel.setVisible(false);
                    mProgressBar.setVisible(false);
}
}
});
//Synthetic comment -- @@ -610,22 +610,22 @@

mm.updateAll(true);

        mDeviceViewButton.setSelection(true);
        mDeviceViewButton.setImage(mDeviceViewSelectedImage);

        mTreeViewButton.setSelection(false);
        mTreeViewButton.setImage(mTreeViewImage);

        mPixelPerfectButton.setSelection(false);
        mPixelPerfectButton.setImage(mPixelPerfectImage);

        mMainWindowStackLayout.topControl = mDeviceSelectorPanel;

        mMainWindow.layout();

        mDeviceSelector.setFocus();

        mTreeViewControls.setVisible(false);
}

public void showTreeView() {
//Synthetic comment -- @@ -657,22 +657,22 @@

mm.updateAll(true);

        mDeviceViewButton.setSelection(false);
        mDeviceViewButton.setImage(mDeviceViewImage);

        mTreeViewButton.setSelection(true);
        mTreeViewButton.setImage(mTreeViewSelectedImage);

        mPixelPerfectButton.setSelection(false);
        mPixelPerfectButton.setImage(mPixelPerfectImage);

        mMainWindowStackLayout.topControl = mTreeViewPanel;

        mMainWindow.layout();

        mTreeView.setFocus();

        mTreeViewControls.setVisible(true);
}

public void showPixelPerfect() {
//Synthetic comment -- @@ -703,22 +703,22 @@

mm.updateAll(true);

        mDeviceViewButton.setSelection(false);
        mDeviceViewButton.setImage(mDeviceViewImage);

        mTreeViewButton.setSelection(false);
        mTreeViewButton.setImage(mTreeViewImage);

        mPixelPerfectButton.setSelection(true);
        mPixelPerfectButton.setImage(mPixelPerfectSelectedImage);

        mMainWindowStackLayout.topControl = mPixelPerfectPanel;

        mMainWindow.layout();

        mPixelPerfectLoupe.setFocus();

        mTreeViewControls.setVisible(false);
}

private SelectionListener deviceViewButtonSelectionListener = new SelectionListener() {
//Synthetic comment -- @@ -727,7 +727,7 @@
}

public void widgetSelected(SelectionEvent e) {
            mDeviceViewButton.setSelection(true);
showDeviceSelector();
}
};
//Synthetic comment -- @@ -738,7 +738,7 @@
}

public void widgetSelected(SelectionEvent e) {
            mTreeViewButton.setSelection(true);
showTreeView();
}
};
//Synthetic comment -- @@ -749,7 +749,7 @@
}

public void widgetSelected(SelectionEvent e) {
            mPixelPerfectButton.setSelection(true);
showPixelPerfect();
}
};
//Synthetic comment -- @@ -760,12 +760,12 @@
}

public void widgetSelected(SelectionEvent e) {
            if (mLayoutViewer.getOnBlack()) {
                mLayoutViewer.setOnBlack(false);
                mOnBlackWhiteButton.setImage(mOnBlackImage);
} else {
                mLayoutViewer.setOnBlack(true);
                mOnBlackWhiteButton.setImage(mOnWhiteImage);
}
}
};
//Synthetic comment -- @@ -776,7 +776,7 @@
}

public void widgetSelected(SelectionEvent e) {
            mLayoutViewer.setShowExtras(mShowExtras.getSelection());
}
};

//Synthetic comment -- @@ -786,12 +786,12 @@
}

public void widgetSelected(SelectionEvent e) {
            mShowExtras.setSelection(true);
showExtrasSelectionListener.widgetSelected(null);
}
};

    private ITreeChangeListener treeChangeListener = new ITreeChangeListener() {
public void selectionChanged() {
// pass
}
//Synthetic comment -- @@ -801,10 +801,10 @@
public void run() {
if (TreeViewModel.getModel().getTree() == null) {
showDeviceSelector();
                        mTreeViewButton.setEnabled(false);
} else {
showTreeView();
                        mTreeViewButton.setEnabled(true);
}
}
});
//Synthetic comment -- @@ -819,7 +819,7 @@
}
};

    private IImageChangeListener imageChangeListener = new IImageChangeListener() {

public void crosshairMoved() {
// pass
//Synthetic comment -- @@ -837,10 +837,10 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
if (PixelPerfectModel.getModel().getImage() == null) {
                        mPixelPerfectButton.setEnabled(false);
showDeviceSelector();
} else {
                        mPixelPerfectButton.setEnabled(true);
showPixelPerfect();
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index f26cc2c..23b6210 100644

//Synthetic comment -- @@ -28,16 +28,16 @@
*/
public class HierarchyViewerApplicationDirector extends HierarchyViewerDirector {

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

public static HierarchyViewerDirector createDirector() {
        return sDirector = new HierarchyViewerApplicationDirector();
}

@Override
public void terminate() {
super.terminate();
        mExecutor.shutdown();
}

/*
//Synthetic comment -- @@ -46,7 +46,7 @@
*/
@Override
public String getAdbLocation() {
        String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir"); //$NON-NLS-1$
if (hvParentLocation != null && hvParentLocation.length() != 0) {
return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}
//Synthetic comment -- @@ -60,7 +60,7 @@
*/
@Override
public void executeInBackground(final String taskName, final Runnable task) {
        mExecutor.execute(new Runnable() {
public void run() {
HierarchyViewerApplication.getApp().startTask(taskName);
task.run();








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/AboutAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/AboutAction.java
//Synthetic comment -- index c6abe57..0c7c7b2 100644

//Synthetic comment -- @@ -30,35 +30,35 @@

public class AboutAction extends Action implements ImageAction {

    private static AboutAction sAction;

    private Image mImage;

    private Shell mShell;

private AboutAction(Shell shell) {
super("&About");
        this.mShell = shell;
setAccelerator(SWT.MOD1 + 'A');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("about-small.jpg", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Shows the about dialog");
}

public static AboutAction getAction(Shell shell) {
        if (sAction == null) {
            sAction = new AboutAction(shell);
}
        return sAction;
}

@Override
public void run() {
        new AboutDialog(mShell).open();
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/LoadAllViewsAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/LoadAllViewsAction.java
//Synthetic comment -- index 5007e1e..33e044d 100644

//Synthetic comment -- @@ -28,24 +28,24 @@

public class LoadAllViewsAction extends TreeViewEnabledAction implements ImageAction {

    private static LoadAllViewsAction sAction;

    private Image mImage;

private LoadAllViewsAction() {
super("Load All &Views");
setAccelerator(SWT.MOD1 + 'V');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("load-all-views.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Load all view images");
}

public static LoadAllViewsAction getAction() {
        if (sAction == null) {
            sAction = new LoadAllViewsAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -54,6 +54,6 @@
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/QuitAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/QuitAction.java
//Synthetic comment -- index 693d55a..3e9598d 100644

//Synthetic comment -- @@ -23,7 +23,7 @@

public class QuitAction extends Action {

    private static QuitAction sAction;

private QuitAction() {
super("E&xit");
//Synthetic comment -- @@ -31,10 +31,10 @@
}

public static QuitAction getAction() {
        if (sAction == null) {
            sAction = new QuitAction();
}
        return sAction;
}

@Override








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/ShowOverlayAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/ShowOverlayAction.java
//Synthetic comment -- index e4695cd..987386d 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.actions.ImageAction;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -29,37 +29,37 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ShowOverlayAction extends Action implements ImageAction, IImageChangeListener {

    private static ShowOverlayAction sAction;

    private Image mImage;

private ShowOverlayAction() {
super("Show In &Loupe", Action.AS_CHECK_BOX);
setAccelerator(SWT.MOD1 + 'L');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("show-overlay.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Show the overlay in the loupe view");
setEnabled(PixelPerfectModel.getModel().getOverlayImage() != null);
PixelPerfectModel.getModel().addImageChangeListener(this);
}

public static ShowOverlayAction getAction() {
        if (sAction == null) {
            sAction = new ShowOverlayAction();
}
        return sAction;
}

@Override
public void run() {
        HierarchyViewerApplication.getApp().showOverlayInLoupe(sAction.isChecked());
}

public Image getImage() {
        return mImage;
}

public void crosshairMoved() {








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java
//Synthetic comment -- index cf8d5e2..4681c40 100644

//Synthetic comment -- @@ -28,35 +28,35 @@
import org.eclipse.swt.widgets.Composite;

public class ActionButton implements IPropertyChangeListener, SelectionListener {
    private Button mButton;

    private Action mAction;

public ActionButton(Composite parent, ImageAction action) {
        this.mAction = (Action) action;
        if (this.mAction.getStyle() == Action.AS_CHECK_BOX) {
            mButton = new Button(parent, SWT.CHECK);
} else {
            mButton = new Button(parent, SWT.PUSH);
}
        mButton.setText(action.getText());
        mButton.setImage(action.getImage());
        this.mAction.addPropertyChangeListener(this);
        mButton.addSelectionListener(this);
        mButton.setToolTipText(action.getToolTipText());
        mButton.setEnabled(this.mAction.isEnabled());
}

public void propertyChange(PropertyChangeEvent e) {
        if (e.getProperty().toUpperCase().equals("ENABLED")) { //$NON-NLS-1$
            mButton.setEnabled((Boolean) e.getNewValue());
        } else if (e.getProperty().toUpperCase().equals("CHECKED")) { //$NON-NLS-1$
            mButton.setSelection(mAction.isChecked());
}
}

public void setLayoutData(Object data) {
        mButton.setLayoutData(data);
}

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -64,13 +64,13 @@
}

public void widgetSelected(SelectionEvent e) {
        if (mAction.getStyle() == Action.AS_CHECK_BOX) {
            mAction.setChecked(mButton.getSelection());
}
        mAction.run();
}

public void addSelectionListener(SelectionListener listener) {
        mButton.addSelectionListener(listener);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 1264d60..63b30c0 100644

//Synthetic comment -- @@ -59,33 +59,33 @@
public abstract class HierarchyViewerDirector implements IDeviceChangeListener,
IWindowChangeListener {

    protected static HierarchyViewerDirector sDirector;

public static final String TAG = "hierarchyviewer";

    private int mPixelPerfectRefreshesInProgress = 0;

    private Timer mPixelPerfectRefreshTimer = new Timer();

    private boolean mAutoRefresh = false;

public static final int DEFAULT_PIXEL_PERFECT_AUTOREFRESH_INTERVAL = 5;

    private int mPixelPerfectAutoRefreshInterval = DEFAULT_PIXEL_PERFECT_AUTOREFRESH_INTERVAL;

    private PixelPerfectAutoRefreshTask mCurrentAutoRefreshTask;

    private String mFilterText = ""; //$NON-NLS-1$

public void terminate() {
WindowUpdater.terminate();
        mPixelPerfectRefreshTimer.cancel();
}

public abstract String getAdbLocation();

public static HierarchyViewerDirector getDirector() {
        return sDirector;
}

public void initDebugBridge() {
//Synthetic comment -- @@ -174,7 +174,7 @@
Window treeViewWindow = TreeViewModel.getModel().getWindow();
if (treeViewWindow != null && treeViewWindow.getDevice() == device) {
TreeViewModel.getModel().setData(null, null);
                    mFilterText = ""; //$NON-NLS-1$
}
}
});
//Synthetic comment -- @@ -219,9 +219,9 @@
// want it to refresh following the last focus change.
boolean proceed = false;
synchronized (this) {
                if (mPixelPerfectRefreshesInProgress <= 1) {
proceed = true;
                    mPixelPerfectRefreshesInProgress++;
}
}
if (proceed) {
//Synthetic comment -- @@ -232,7 +232,7 @@
PixelPerfectModel.getModel().setImage(screenshotImage);
}
synchronized (HierarchyViewerDirector.this) {
                            mPixelPerfectRefreshesInProgress--;
}
}

//Synthetic comment -- @@ -307,7 +307,7 @@
executeInBackground("Loading view hierarchy", new Runnable() {
public void run() {

                mFilterText = ""; //$NON-NLS-1$

ViewNode viewNode = DeviceBridge.loadWindowData(window);
if (viewNode != null) {
//Synthetic comment -- @@ -324,7 +324,7 @@
public void run() {
FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
fileDialog.setFilterExtensions(new String[] {
                    "*.jpg;*.jpeg;*.png;*.gif;*.bmp" //$NON-NLS-1$
});
fileDialog.setFilterNames(new String[] {
"Image (*.jpg, *.jpeg, *.png, *.gif, *.bmp)"
//Synthetic comment -- @@ -430,7 +430,7 @@
if (viewNode != null) {
FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
fileDialog.setFilterExtensions(new String[] {
                        "*.png" //$NON-NLS-1$
});
fileDialog.setFilterNames(new String[] {
"Portable Network Graphics File (*.png)"
//Synthetic comment -- @@ -446,8 +446,8 @@
image.getImageData()
};
String extensionedFileName = fileName;
                                if (!extensionedFileName.toLowerCase().endsWith(".png")) { //$NON-NLS-1$
                                    extensionedFileName += ".png"; //$NON-NLS-1$
}
try {
imageLoader.save(extensionedFileName, SWT.IMAGE_PNG);
//Synthetic comment -- @@ -472,7 +472,7 @@
final ImageData imageData = untouchableImage.getImageData();
FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
fileDialog.setFilterExtensions(new String[] {
                        "*.png" //$NON-NLS-1$
});
fileDialog.setFilterNames(new String[] {
"Portable Network Graphics File (*.png)"
//Synthetic comment -- @@ -487,8 +487,8 @@
imageData
};
String extensionedFileName = fileName;
                                if (!extensionedFileName.toLowerCase().endsWith(".png")) { //$NON-NLS-1$
                                    extensionedFileName += ".png"; //$NON-NLS-1$
}
try {
imageLoader.save(extensionedFileName, SWT.IMAGE_PNG);
//Synthetic comment -- @@ -511,7 +511,7 @@
if (window != null) {
FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
fileDialog.setFilterExtensions(new String[] {
                        "*.psd" //$NON-NLS-1$
});
fileDialog.setFilterNames(new String[] {
"Photoshop Document (*.psd)"
//Synthetic comment -- @@ -524,8 +524,8 @@
PsdFile psdFile = DeviceBridge.captureLayers(window);
if (psdFile != null) {
String extensionedFileName = fileName;
                                    if (!extensionedFileName.toLowerCase().endsWith(".psd")) { //$NON-NLS-1$
                                        extensionedFileName += ".psd"; //$NON-NLS-1$
}
try {
psdFile.write(new FileOutputStream(extensionedFileName));
//Synthetic comment -- @@ -596,7 +596,7 @@
}

public void filterNodes(String filterText) {
        this.mFilterText = filterText;
DrawableViewNode tree = TreeViewModel.getModel().getTree();
if (tree != null) {
tree.viewNode.filter(filterText);
//Synthetic comment -- @@ -606,7 +606,7 @@
}

public String getFilterText() {
        return mFilterText;
}

private static class PixelPerfectAutoRefreshTask extends TimerTask {
//Synthetic comment -- @@ -617,44 +617,44 @@
};

public void setPixelPerfectAutoRefresh(boolean value) {
        synchronized (mPixelPerfectRefreshTimer) {
            if (value == mAutoRefresh) {
return;
}
            mAutoRefresh = value;
            if (mAutoRefresh) {
                mCurrentAutoRefreshTask = new PixelPerfectAutoRefreshTask();
                mPixelPerfectRefreshTimer.schedule(mCurrentAutoRefreshTask,
                        mPixelPerfectAutoRefreshInterval * 1000,
                        mPixelPerfectAutoRefreshInterval * 1000);
} else {
                mCurrentAutoRefreshTask.cancel();
                mCurrentAutoRefreshTask = null;
}
}
}

public void setPixelPerfectAutoRefreshInterval(int value) {
        synchronized (mPixelPerfectRefreshTimer) {
            if (mPixelPerfectAutoRefreshInterval == value) {
return;
}
            mPixelPerfectAutoRefreshInterval = value;
            if (mAutoRefresh) {
                mCurrentAutoRefreshTask.cancel();
long timeLeft =
                        Math.max(0, mPixelPerfectAutoRefreshInterval
* 1000
                                - (System.currentTimeMillis() - mCurrentAutoRefreshTask
.scheduledExecutionTime()));
                mCurrentAutoRefreshTask = new PixelPerfectAutoRefreshTask();
                mPixelPerfectRefreshTimer.schedule(mCurrentAutoRefreshTask, timeLeft,
                        mPixelPerfectAutoRefreshInterval * 1000);
}
}
}

public int getPixelPerfectAutoRefreshInverval() {
        return mPixelPerfectAutoRefreshInterval;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/CapturePSDAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/CapturePSDAction.java
//Synthetic comment -- index 240ced1..b62ba7e 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class CapturePSDAction extends TreeViewEnabledAction implements ImageAction {

    private static CapturePSDAction sAction;

    private Image mImage;

    private Shell mShell;

private CapturePSDAction(Shell shell) {
super("&Capture Layers");
        this.mShell = shell;
setAccelerator(SWT.MOD1 + 'C');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("capture-psd.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Capture the window layers as a photoshop document");
}

public static CapturePSDAction getAction(Shell shell) {
        if (sAction == null) {
            sAction = new CapturePSDAction(shell);
}
        return sAction;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().capturePSD(mShell);
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DisplayViewAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DisplayViewAction.java
//Synthetic comment -- index 4fc8024..e637216 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class DisplayViewAction extends SelectedNodeEnabledAction implements ImageAction {

    private static DisplayViewAction sAction;

    private Image mImage;

    private Shell mShell;

private DisplayViewAction(Shell shell) {
super("&Display View");
        this.mShell = shell;
setAccelerator(SWT.MOD1 + 'D');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("display.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Display the selected view image in a separate window");
}

public static DisplayViewAction getAction(Shell shell) {
        if (sAction == null) {
            sAction = new DisplayViewAction(shell);
}
        return sAction;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().showCapture(mShell);
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InspectScreenshotAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InspectScreenshotAction.java
//Synthetic comment -- index 7ef7109..72280f9 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.IWindowChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -29,18 +29,18 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class InspectScreenshotAction extends Action implements ImageAction, IWindowChangeListener {

    private static InspectScreenshotAction sAction;

    private Image mImage;

private InspectScreenshotAction() {
super("Inspect &Screenshot");
setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("inspect-screenshot.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Inspect a screenshot in the pixel perfect view");
setEnabled(
DeviceSelectionModel.getModel().getSelectedDevice() != null);
//Synthetic comment -- @@ -48,10 +48,10 @@
}

public static InspectScreenshotAction getAction() {
        if (sAction == null) {
            sAction = new InspectScreenshotAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -60,7 +60,7 @@
}

public Image getImage() {
        return mImage;
}

public void deviceChanged(IDevice device) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InvalidateAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InvalidateAction.java
//Synthetic comment -- index aaf0ff0..83da0ce 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class InvalidateAction extends SelectedNodeEnabledAction implements ImageAction {

    private static InvalidateAction sAction;

    private Image mImage;

private InvalidateAction() {
super("&Invalidate Layout");
setAccelerator(SWT.MOD1 + 'I');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("invalidate.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Invalidate the layout for the current window");
}

public static InvalidateAction getAction() {
        if (sAction == null) {
            sAction = new InvalidateAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadOverlayAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadOverlayAction.java
//Synthetic comment -- index c948914..81c1b02 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class LoadOverlayAction extends PixelPerfectEnabledAction implements ImageAction {

    private static LoadOverlayAction sAction;

    private Image mImage;

    private Shell mShell;

private LoadOverlayAction(Shell shell) {
super("Load &Overlay");
        this.mShell = shell;
setAccelerator(SWT.MOD1 + 'O');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("load-overlay.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Load an image to overlay the screenshot");
}

public static LoadOverlayAction getAction(Shell shell) {
        if (sAction == null) {
            sAction = new LoadOverlayAction(shell);
}
        return sAction;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().loadOverlay(mShell);
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadViewHierarchyAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadViewHierarchyAction.java
//Synthetic comment -- index d26b2ef..9629716 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.IWindowChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -29,18 +29,18 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class LoadViewHierarchyAction extends Action implements ImageAction, IWindowChangeListener {

    private static LoadViewHierarchyAction sAction;

    private Image mImage;

private LoadViewHierarchyAction() {
super("Load View &Hierarchy");
setAccelerator(SWT.MOD1 + 'H');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Load the view hierarchy into the tree view");
setEnabled(
DeviceSelectionModel.getModel().getSelectedWindow() != null);
//Synthetic comment -- @@ -48,10 +48,10 @@
}

public static LoadViewHierarchyAction getAction() {
        if (sAction == null) {
            sAction = new LoadViewHierarchyAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -60,7 +60,7 @@
}

public Image getImage() {
        return mImage;
}

public void deviceChanged(IDevice device) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectAutoRefreshAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectAutoRefreshAction.java
//Synthetic comment -- index 5e31829..e104b03 100644

//Synthetic comment -- @@ -27,32 +27,32 @@

public class PixelPerfectAutoRefreshAction extends PixelPerfectEnabledAction implements ImageAction {

    private static PixelPerfectAutoRefreshAction sAction;

    private Image mImage;

private PixelPerfectAutoRefreshAction() {
super("Auto &Refresh", Action.AS_CHECK_BOX);
setAccelerator(SWT.MOD1 + 'R');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("auto-refresh.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Automatically refresh the screenshot");
}

public static PixelPerfectAutoRefreshAction getAction() {
        if (sAction == null) {
            sAction = new PixelPerfectAutoRefreshAction();
}
        return sAction;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().setPixelPerfectAutoRefresh(sAction.isChecked());
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectEnabledAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectEnabledAction.java
//Synthetic comment -- index 9423d10..b423d14 100644

//Synthetic comment -- @@ -17,12 +17,12 @@
package com.android.hierarchyviewerlib.actions;

import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectEnabledAction extends Action implements IImageChangeListener {
public PixelPerfectEnabledAction(String name) {
super(name);
setEnabled(PixelPerfectModel.getModel().getImage() != null);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectAction.java
//Synthetic comment -- index a5d7514..2e06bbd 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RefreshPixelPerfectAction extends PixelPerfectEnabledAction implements ImageAction {

    private static RefreshPixelPerfectAction sAction;

    private Image mImage;

private RefreshPixelPerfectAction() {
super("&Refresh Screenshot");
setAccelerator(SWT.F5);
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("refresh-windows.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Refresh the screenshot");
}

public static RefreshPixelPerfectAction getAction() {
        if (sAction == null) {
            sAction = new RefreshPixelPerfectAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectTreeAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectTreeAction.java
//Synthetic comment -- index 41214df..8c17760 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RefreshPixelPerfectTreeAction extends PixelPerfectEnabledAction implements ImageAction {

    private static RefreshPixelPerfectTreeAction sAction;

    private Image mImage;

private RefreshPixelPerfectTreeAction() {
super("Refresh &Tree");
setAccelerator(SWT.MOD1 + 'T');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Refresh the tree");
}

public static RefreshPixelPerfectTreeAction getAction() {
        if (sAction == null) {
            sAction = new RefreshPixelPerfectTreeAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshViewAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshViewAction.java
//Synthetic comment -- index 06c48ee..8f15c1c 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RefreshViewAction extends TreeViewEnabledAction implements ImageAction {

    private static RefreshViewAction sAction;

    private Image mImage;

private RefreshViewAction() {
super("Load View &Hierarchy");
setAccelerator(SWT.MOD1 + 'H');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Reload the view hierarchy");
}

public static RefreshViewAction getAction() {
        if (sAction == null) {
            sAction = new RefreshViewAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshWindowsAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshWindowsAction.java
//Synthetic comment -- index 47d692a..6097ad3 100644

//Synthetic comment -- @@ -27,24 +27,24 @@

public class RefreshWindowsAction extends Action implements ImageAction {

    private static RefreshWindowsAction sAction;

    private Image mImage;

private RefreshWindowsAction() {
super("&Refresh");
setAccelerator(SWT.F5);
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("refresh-windows.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Refresh the list of devices");
}

public static RefreshWindowsAction getAction() {
        if (sAction == null) {
            sAction = new RefreshWindowsAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -53,6 +53,6 @@
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RequestLayoutAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RequestLayoutAction.java
//Synthetic comment -- index e3d6f8f..5a79933 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RequestLayoutAction extends SelectedNodeEnabledAction implements ImageAction {

    private static RequestLayoutAction sAction;

    private Image mImage;

private RequestLayoutAction() {
super("Request &Layout");
setAccelerator(SWT.MOD1 + 'L');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("request-layout.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Request the view to lay out");
}

public static RequestLayoutAction getAction() {
        if (sAction == null) {
            sAction = new RequestLayoutAction();
}
        return sAction;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SavePixelPerfectAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SavePixelPerfectAction.java
//Synthetic comment -- index 9781d42..45d6da5 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class SavePixelPerfectAction extends PixelPerfectEnabledAction implements ImageAction {

    private static SavePixelPerfectAction sAction;

    private Image mImage;

    private Shell mShell;

private SavePixelPerfectAction(Shell shell) {
super("&Save as PNG");
        this.mShell = shell;
setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("save.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Save the screenshot as a PNG image");
}

public static SavePixelPerfectAction getAction(Shell shell) {
        if (sAction == null) {
            sAction = new SavePixelPerfectAction(shell);
}
        return sAction;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().savePixelPerfect(mShell);
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SaveTreeViewAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SaveTreeViewAction.java
//Synthetic comment -- index 094b101..69df7e0 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class SaveTreeViewAction extends TreeViewEnabledAction implements ImageAction {

    private static SaveTreeViewAction sAction;

    private Image mImage;

    private Shell mShell;

private SaveTreeViewAction(Shell shell) {
super("&Save as PNG");
        this.mShell = shell;
setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("save.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
setToolTipText("Save the tree view as a PNG image");
}

public static SaveTreeViewAction getAction(Shell shell) {
        if (sAction == null) {
            sAction = new SaveTreeViewAction(shell);
}
        return sAction;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().saveTreeView(mShell);
}

public Image getImage() {
        return mImage;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SelectedNodeEnabledAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SelectedNodeEnabledAction.java
//Synthetic comment -- index 86f75a4..f051f69 100644

//Synthetic comment -- @@ -17,12 +17,12 @@
package com.android.hierarchyviewerlib.actions;

import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

public class SelectedNodeEnabledAction extends Action implements ITreeChangeListener {
public SelectedNodeEnabledAction(String name) {
super(name);
setEnabled(TreeViewModel.getModel().getTree() != null








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/TreeViewEnabledAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/TreeViewEnabledAction.java
//Synthetic comment -- index 9ac7fb1..7354ed5 100644

//Synthetic comment -- @@ -17,12 +17,12 @@
package com.android.hierarchyviewerlib.actions;

import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

public class TreeViewEnabledAction extends Action implements ITreeChangeListener {
public TreeViewEnabledAction(String name) {
super(name);
setEnabled(TreeViewModel.getModel().getTree() != null);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 399e470..d00c4dc 100644

//Synthetic comment -- @@ -60,14 +60,14 @@

private static final int SERVICE_CODE_IS_SERVER_RUNNING = 3;

    private static AndroidDebugBridge sBridge;

    private static final HashMap<IDevice, Integer> sDevicePortMap = new HashMap<IDevice, Integer>();

    private static final HashMap<IDevice, ViewServerInfo> sViewServerInfo =
new HashMap<IDevice, ViewServerInfo>();

    private static int sNextLocalPort = DEFAULT_SERVER_PORT;

public static class ViewServerInfo {
public final int protocolVersion;
//Synthetic comment -- @@ -81,11 +81,11 @@
}

public static void initDebugBridge(String adbLocation) {
        if (sBridge == null) {
AndroidDebugBridge.init(false /* debugger support */);
}
        if (sBridge == null || !sBridge.isConnected()) {
            sBridge = AndroidDebugBridge.createBridge(adbLocation, true);
}
}

//Synthetic comment -- @@ -94,10 +94,10 @@
}

public static IDevice[] getDevices() {
        if (sBridge == null) {
return new IDevice[0];
}
        return sBridge.getDevices();
}

/*
//Synthetic comment -- @@ -121,12 +121,12 @@
* @param device
*/
public static void setupDeviceForward(IDevice device) {
        synchronized (sDevicePortMap) {
if (device.getState() == IDevice.DeviceState.ONLINE) {
                int localPort = sNextLocalPort++;
try {
device.createForward(localPort, DEFAULT_SERVER_PORT);
                    sDevicePortMap.put(device, localPort);
} catch (TimeoutException e) {
Log.e(TAG, "Timeout setting up port forwarding for " + device);
} catch (AdbCommandRejectedException e) {
//Synthetic comment -- @@ -141,12 +141,12 @@
}

public static void removeDeviceForward(IDevice device) {
        synchronized (sDevicePortMap) {
            final Integer localPort = sDevicePortMap.get(device);
if (localPort != null) {
try {
device.removeForward(localPort, DEFAULT_SERVER_PORT);
                    sDevicePortMap.remove(device);
} catch (TimeoutException e) {
Log.e(TAG, "Timeout removing port forwarding for " + device);
} catch (AdbCommandRejectedException e) {
//Synthetic comment -- @@ -160,8 +160,8 @@
}

public static int getDeviceLocalPort(IDevice device) {
        synchronized (sDevicePortMap) {
            Integer port = sDevicePortMap.get(device);
if (port != null) {
return port;
}
//Synthetic comment -- @@ -235,15 +235,15 @@
}

private static String buildStartServerShellCommand(int port) {
        return String.format("service call window %d i32 %d", SERVICE_CODE_START_SERVER, port); //$NON-NLS-1$
}

private static String buildStopServerShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_STOP_SERVER); //$NON-NLS-1$
}

private static String buildIsServerRunningShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_IS_SERVER_RUNNING); //$NON-NLS-1$
}

private static class BooleanResultReader extends MultiLineReceiver {
//Synthetic comment -- @@ -256,7 +256,7 @@
@Override
public void processNewLines(String[] strings) {
if (strings.length > 0) {
                Pattern pattern = Pattern.compile(".*?\\([0-9]{8} ([0-9]{8}).*"); //$NON-NLS-1$
Matcher matcher = pattern.matcher(strings[0]);
if (matcher.matches()) {
if (Integer.parseInt(matcher.group(1)) == 1) {
//Synthetic comment -- @@ -277,7 +277,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(device);
            connection.sendCommand("SERVER"); //$NON-NLS-1$
String line = connection.getInputStream().readLine();
if (line != null) {
server = Integer.parseInt(line);
//Synthetic comment -- @@ -292,7 +292,7 @@
connection = null;
try {
connection = new DeviceConnection(device);
            connection.sendCommand("PROTOCOL"); //$NON-NLS-1$
String line = connection.getInputStream().readLine();
if (line != null) {
protocol = Integer.parseInt(line);
//Synthetic comment -- @@ -308,21 +308,21 @@
return null;
}
ViewServerInfo returnValue = new ViewServerInfo(server, protocol);
        synchronized (sViewServerInfo) {
            sViewServerInfo.put(device, returnValue);
}
return returnValue;
}

public static ViewServerInfo getViewServerInfo(IDevice device) {
        synchronized (sViewServerInfo) {
            return sViewServerInfo.get(device);
}
}

public static void removeViewServerInfo(IDevice device) {
        synchronized (sViewServerInfo) {
            sViewServerInfo.remove(device);
}
}

//Synthetic comment -- @@ -336,11 +336,11 @@
ViewServerInfo serverInfo = getViewServerInfo(device);
try {
connection = new DeviceConnection(device);
            connection.sendCommand("LIST"); //$NON-NLS-1$
BufferedReader in = connection.getInputStream();
String line;
while ((line = in.readLine()) != null) {
                if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$
break;
}

//Synthetic comment -- @@ -391,7 +391,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(device);
            connection.sendCommand("GET_FOCUS"); //$NON-NLS-1$
String line = connection.getInputStream().readLine();
if (line == null || line.length() == 0) {
return -1;
//Synthetic comment -- @@ -411,7 +411,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("DUMP " + window.encode()); //$NON-NLS-1$
BufferedReader in = connection.getInputStream();
ViewNode currentNode = null;
int currentDepth = -1;
//Synthetic comment -- @@ -457,11 +457,11 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("PROFILE " + window.encode() + " " + viewNode.toString()); //$NON-NLS-1$
BufferedReader in = connection.getInputStream();
int protocol;
            synchronized (sViewServerInfo) {
                protocol = sViewServerInfo.get(window.getDevice()).protocolVersion;
}
if (protocol < 3) {
return loadProfileData(viewNode, in);
//Synthetic comment -- @@ -485,8 +485,8 @@

private static boolean loadProfileData(ViewNode node, BufferedReader in) throws IOException {
String line;
        if ((line = in.readLine()) == null || line.equalsIgnoreCase("-1 -1 -1") //$NON-NLS-1$
                || line.equalsIgnoreCase("DONE.")) { //$NON-NLS-1$
return false;
}
String[] data = line.split(" ");
//Synthetic comment -- @@ -513,7 +513,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("CAPTURE " + window.encode() + " " + viewNode.toString()); //$NON-NLS-1$
return new Image(Display.getDefault(), connection.getSocket().getInputStream());
} catch (Exception e) {
Log.e(TAG, "Unable to capture data for node " + viewNode + " in window "
//Synthetic comment -- @@ -533,7 +533,7 @@
try {
connection = new DeviceConnection(window.getDevice());

            connection.sendCommand("CAPTURE_LAYERS " + window.encode()); //$NON-NLS-1$

in =
new DataInputStream(new BufferedInputStream(connection.getSocket()
//Synthetic comment -- @@ -604,7 +604,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(viewNode.window.getDevice());
            connection.sendCommand("INVALIDATE " + viewNode.window.encode() + " " + viewNode); //$NON-NLS-1$
} catch (Exception e) {
Log.e(TAG, "Unable to invalidate view " + viewNode + " in window " + viewNode.window
+ " on device " + viewNode.window.getDevice());
//Synthetic comment -- @@ -617,7 +617,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(viewNode.window.getDevice());
            connection.sendCommand("REQUEST_LAYOUT " + viewNode.window.encode() + " " + viewNode); //$NON-NLS-1$
} catch (Exception e) {
Log.e(TAG, "Unable to request layout for node " + viewNode + " in window "
+ viewNode.window + " on device " + viewNode.window.getDevice());








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceConnection.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceConnection.java
//Synthetic comment -- index cda1a77..f750d5c 100644

//Synthetic comment -- @@ -34,42 +34,42 @@
public class DeviceConnection {

// Now a socket channel, since socket channels are friendly with interrupts.
    private SocketChannel mSocketChannel;

    private BufferedReader mIn;

    private BufferedWriter mOut;

public DeviceConnection(IDevice device) throws IOException {
        mSocketChannel = SocketChannel.open();
int port = DeviceBridge.getDeviceLocalPort(device);

if (port == -1) {
throw new IOException();
}

        mSocketChannel.connect(new InetSocketAddress("127.0.0.1", port)); //$NON-NLS-1$
        mSocketChannel.socket().setSoTimeout(40000);
}

public BufferedReader getInputStream() throws IOException {
        if (mIn == null) {
            mIn = new BufferedReader(new InputStreamReader(mSocketChannel.socket().getInputStream()));
}
        return mIn;
}

public BufferedWriter getOutputStream() throws IOException {
        if (mOut == null) {
            mOut =
                    new BufferedWriter(new OutputStreamWriter(mSocketChannel.socket()
.getOutputStream()));
}
        return mOut;
}

public Socket getSocket() {
        return mSocketChannel.socket();
}

public void sendCommand(String command) throws IOException {
//Synthetic comment -- @@ -81,19 +81,19 @@

public void close() {
try {
            if (mIn != null) {
                mIn.close();
}
} catch (IOException e) {
}
try {
            if (mOut != null) {
                mOut.close();
}
} catch (IOException e) {
}
try {
            mSocketChannel.close();
} catch (IOException e) {
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
//Synthetic comment -- index 6457159..d925404 100644

//Synthetic comment -- @@ -183,58 +183,58 @@
}
});

        id = namedProperties.get("mID").value; //$NON-NLS-1$

left =
 namedProperties.containsKey("mLeft") ? getInt("mLeft", 0) : getInt("layout:mLeft", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
0);
        top = namedProperties.containsKey("mTop") ? getInt("mTop", 0) : getInt("layout:mTop", 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
width =
                namedProperties.containsKey("getWidth()") ? getInt("getWidth()", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "layout:getWidth()", 0); //$NON-NLS-1$
height =
                namedProperties.containsKey("getHeight()") ? getInt("getHeight()", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "layout:getHeight()", 0); //$NON-NLS-1$
scrollX =
                namedProperties.containsKey("mScrollX") ? getInt("mScrollX", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "scrolling:mScrollX", 0); //$NON-NLS-1$
scrollY =
                namedProperties.containsKey("mScrollY") ? getInt("mScrollY", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "scrolling:mScrollY", 0); //$NON-NLS-1$
paddingLeft =
                namedProperties.containsKey("mPaddingLeft") ? getInt("mPaddingLeft", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "padding:mPaddingLeft", 0); //$NON-NLS-1$
paddingRight =
                namedProperties.containsKey("mPaddingRight") ? getInt("mPaddingRight", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "padding:mPaddingRight", 0); //$NON-NLS-1$
paddingTop =
                namedProperties.containsKey("mPaddingTop") ? getInt("mPaddingTop", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "padding:mPaddingTop", 0); //$NON-NLS-1$
paddingBottom =
                namedProperties.containsKey("mPaddingBottom") ? getInt("mPaddingBottom", 0) //$NON-NLS-1$ //$NON-NLS-2$
                        : getInt("padding:mPaddingBottom", 0); //$NON-NLS-1$
marginLeft =
                namedProperties.containsKey("layout_leftMargin") ? getInt("layout_leftMargin", //$NON-NLS-1$ //$NON-NLS-2$
                        Integer.MIN_VALUE) : getInt("layout:layout_leftMargin", Integer.MIN_VALUE); //$NON-NLS-1$
marginRight =
                namedProperties.containsKey("layout_rightMargin") ? getInt("layout_rightMargin", //$NON-NLS-1$ //$NON-NLS-2$
                        Integer.MIN_VALUE) : getInt("layout:layout_rightMargin", Integer.MIN_VALUE); //$NON-NLS-1$
marginTop =
                namedProperties.containsKey("layout_topMargin") ? getInt("layout_topMargin", //$NON-NLS-1$ //$NON-NLS-2$
                        Integer.MIN_VALUE) : getInt("layout:layout_topMargin", Integer.MIN_VALUE); //$NON-NLS-1$
marginBottom =
                namedProperties.containsKey("layout_bottomMargin") ? getInt("layout_bottomMargin", //$NON-NLS-1$ //$NON-NLS-2$
Integer.MIN_VALUE)
                        : getInt("layout:layout_bottomMargin", Integer.MIN_VALUE); //$NON-NLS-1$
baseline =
                namedProperties.containsKey("getBaseline()") ? getInt("getBaseline()", 0) : getInt( //$NON-NLS-1$ //$NON-NLS-2$
                        "layout:getBaseline()", 0); //$NON-NLS-1$
willNotDraw =
                namedProperties.containsKey("willNotDraw()") ? getBoolean("willNotDraw()", false) //$NON-NLS-1$ //$NON-NLS-2$
                        : getBoolean("drawing:willNotDraw()", false); //$NON-NLS-1$
hasFocus =
                namedProperties.containsKey("hasFocus()") ? getBoolean("hasFocus()", false) //$NON-NLS-1$ //$NON-NLS-2$
                        : getBoolean("focus:hasFocus()", false); //$NON-NLS-1$

hasMargins =
marginLeft != Integer.MIN_VALUE && marginRight != Integer.MIN_VALUE
//Synthetic comment -- @@ -307,9 +307,9 @@
int dotIndex = name.lastIndexOf('.');
String shortName = (dotIndex == -1) ? name : name.substring(dotIndex + 1);
filtered =
                !text.equals("") //$NON-NLS-1$
&& (shortName.toLowerCase().contains(text.toLowerCase()) || (!id
                                .equals("NO_ID") && id.toLowerCase().contains(text.toLowerCase()))); //$NON-NLS-1$
final int N = children.size();
for (int i = 0; i < N; i++) {
children.get(i).filter(text);
//Synthetic comment -- @@ -342,7 +342,7 @@

@Override
public String toString() {
        return name + "@" + hashCode; //$NON-NLS-1$
}

public static class Property {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/Window.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/Window.java
//Synthetic comment -- index 1869a51..af79081 100644

//Synthetic comment -- @@ -24,37 +24,37 @@
*/
public class Window {

    private String mTitle;

    private int mHashCode;

    private IDevice mDevice;

public Window(IDevice device, String title, int hashCode) {
        this.mDevice = device;
        this.mTitle = title;
        this.mHashCode = hashCode;
}

public String getTitle() {
        return mTitle;
}

public int getHashCode() {
        return mHashCode;
}

public String encode() {
        return Integer.toHexString(mHashCode);
}

@Override
public String toString() {
        return mTitle;
}

public IDevice getDevice() {
        return mDevice;
}

public static Window getFocusedWindow(IDevice device) {
//Synthetic comment -- @@ -69,8 +69,8 @@
@Override
public boolean equals(Object other) {
if (other instanceof Window) {
            return mHashCode == ((Window) other).mHashCode
                    && mDevice.getSerialNumber().equals(((Window) other).mDevice.getSerialNumber());
}
return false;
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/WindowUpdater.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/WindowUpdater.java
//Synthetic comment -- index 26797d2..a0cc635 100644

//Synthetic comment -- @@ -29,10 +29,10 @@
* It notifies all it's listeners of changes.
*/
public class WindowUpdater {
    private static HashMap<IDevice, ArrayList<IWindowChangeListener>> sWindowChangeListeners =
new HashMap<IDevice, ArrayList<IWindowChangeListener>>();

    private static HashMap<IDevice, Thread> sListeningThreads = new HashMap<IDevice, Thread>();

public static interface IWindowChangeListener {
public void windowsChanged(IDevice device);
//Synthetic comment -- @@ -41,58 +41,58 @@
}

public static void terminate() {
        synchronized (sListeningThreads) {
            for (IDevice device : sListeningThreads.keySet()) {
                sListeningThreads.get(device).interrupt();

}
}
}

public static void startListenForWindowChanges(IWindowChangeListener listener, IDevice device) {
        synchronized (sWindowChangeListeners) {
// In this case, a listening thread already exists, so we don't need
// to create another one.
            if (sWindowChangeListeners.containsKey(device)) {
                sWindowChangeListeners.get(device).add(listener);
return;
}
ArrayList<IWindowChangeListener> listeners = new ArrayList<IWindowChangeListener>();
listeners.add(listener);
            sWindowChangeListeners.put(device, listeners);
}
// Start listening
Thread listeningThread = new Thread(new WindowChangeMonitor(device));
        synchronized (sListeningThreads) {
            sListeningThreads.put(device, listeningThread);
}
listeningThread.start();
}

public static void stopListenForWindowChanges(IWindowChangeListener listener, IDevice device) {
        synchronized (sWindowChangeListeners) {
            ArrayList<IWindowChangeListener> listeners = sWindowChangeListeners.get(device);
listeners.remove(listener);
// There are more listeners, so don't stop the listening thread.
if (listeners.size() != 0) {
return;
}
            sWindowChangeListeners.remove(device);
}
// Everybody left, so the party's over!
Thread listeningThread;
        synchronized (sListeningThreads) {
            listeningThread = sListeningThreads.get(device);
            sListeningThreads.remove(device);
}
listeningThread.interrupt();
}

private static IWindowChangeListener[] getWindowChangeListenersAsArray(IDevice device) {
IWindowChangeListener[] listeners;
        synchronized (sWindowChangeListeners) {
ArrayList<IWindowChangeListener> windowChangeListenerList =
                    sWindowChangeListeners.get(device);
if (windowChangeListenerList == null) {
return null;
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java
//Synthetic comment -- index 09dfe76..d029d39 100644

//Synthetic comment -- @@ -29,84 +29,84 @@
*/
public class DeviceSelectionModel {

    private final HashMap<IDevice, Window[]> mDeviceMap = new HashMap<IDevice, Window[]>();

    private final HashMap<IDevice, Integer> mFocusedWindowHashes = new HashMap<IDevice, Integer>();

    private final ArrayList<IDevice> mDeviceList = new ArrayList<IDevice>();

    private final ArrayList<IWindowChangeListener> mWindowChangeListeners =
            new ArrayList<IWindowChangeListener>();

    private IDevice mSelectedDevice;

    private Window mSelectedWindow;

    private static DeviceSelectionModel sModel;

public static DeviceSelectionModel getModel() {
        if (sModel == null) {
            sModel = new DeviceSelectionModel();
}
        return sModel;
}

public boolean containsDevice(IDevice device) {
        synchronized (mDeviceMap) {
            return mDeviceMap.containsKey(device);
}
}

public void addDevice(IDevice device, Window[] windows) {
        synchronized (mDeviceMap) {
            mDeviceMap.put(device, windows);
            mDeviceList.add(device);
}
notifyDeviceConnected(device);
}

public void removeDevice(IDevice device) {
boolean selectionChanged = false;
        synchronized (mDeviceMap) {
            mDeviceList.remove(device);
            if (!mDeviceList.contains(device)) {
                mDeviceMap.remove(device);
                mFocusedWindowHashes.remove(device);
                if (mSelectedDevice == device) {
                    mSelectedDevice = null;
                    mSelectedWindow = null;
selectionChanged = true;
}
}
}
notifyDeviceDisconnected(device);
if (selectionChanged) {
            notifySelectionChanged(mSelectedDevice, mSelectedWindow);
}
}

public void updateDevice(IDevice device, Window[] windows) {
boolean selectionChanged = false;
        synchronized (mDeviceMap) {
            mDeviceMap.put(device, windows);
// If the selected window no longer exists, we clear the selection.
            if (mSelectedDevice == device && mSelectedWindow != null) {
boolean windowStillExists = false;
for (int i = 0; i < windows.length && !windowStillExists; i++) {
                    if (windows[i].equals(mSelectedWindow)) {
windowStillExists = true;
}
}
if (!windowStillExists) {
                    mSelectedDevice = null;
                    mSelectedWindow = null;
selectionChanged = true;
}
}
}
notifyDeviceChanged(device);
if (selectionChanged) {
            notifySelectionChanged(mSelectedDevice, mSelectedWindow);
}
}

//Synthetic comment -- @@ -115,8 +115,8 @@
*/
public void updateFocusedWindow(IDevice device, int focusedWindow) {
Integer oldValue = null;
        synchronized (mDeviceMap) {
            oldValue = mFocusedWindowHashes.put(device, new Integer(focusedWindow));
}
// Only notify if the values are different. It would be cool if Java
// containers accepted basic types like int.
//Synthetic comment -- @@ -125,7 +125,7 @@
}
}

    public static interface IWindowChangeListener {
public void deviceConnected(IDevice device);

public void deviceChanged(IDevice device);
//Synthetic comment -- @@ -137,21 +137,21 @@
public void selectionChanged(IDevice device, Window window);
}

    private IWindowChangeListener[] getWindowChangeListenerList() {
        IWindowChangeListener[] listeners = null;
        synchronized (mWindowChangeListeners) {
            if (mWindowChangeListeners.size() == 0) {
return null;
}
listeners =
                    mWindowChangeListeners.toArray(new IWindowChangeListener[mWindowChangeListeners
.size()]);
}
return listeners;
}

private void notifyDeviceConnected(IDevice device) {
        IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].deviceConnected(device);
//Synthetic comment -- @@ -160,7 +160,7 @@
}

private void notifyDeviceChanged(IDevice device) {
        IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].deviceChanged(device);
//Synthetic comment -- @@ -169,7 +169,7 @@
}

private void notifyDeviceDisconnected(IDevice device) {
        IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].deviceDisconnected(device);
//Synthetic comment -- @@ -178,7 +178,7 @@
}

private void notifyFocusChanged(IDevice device) {
        IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].focusChanged(device);
//Synthetic comment -- @@ -187,7 +187,7 @@
}

private void notifySelectionChanged(IDevice device, Window window) {
        IWindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].selectionChanged(device, window);
//Synthetic comment -- @@ -195,28 +195,28 @@
}
}

    public void addWindowChangeListener(IWindowChangeListener listener) {
        synchronized (mWindowChangeListeners) {
            mWindowChangeListeners.add(listener);
}
}

    public void removeWindowChangeListener(IWindowChangeListener listener) {
        synchronized (mWindowChangeListeners) {
            mWindowChangeListeners.remove(listener);
}
}

public IDevice[] getDevices() {
        synchronized (mDeviceMap) {
            return mDeviceList.toArray(new IDevice[mDeviceList.size()]);
}
}

public Window[] getWindows(IDevice device) {
Window[] windows;
        synchronized (mDeviceMap) {
            windows = mDeviceMap.get(device);
}
return windows;
}
//Synthetic comment -- @@ -225,8 +225,8 @@
// that a window with hashcode -1 gets highlighted. If you remember, this is
// the infamous <Focused Window>
public int getFocusedWindow(IDevice device) {
        synchronized (mDeviceMap) {
            Integer focusedWindow = mFocusedWindowHashes.get(device);
if (focusedWindow == null) {
return -1;
}
//Synthetic comment -- @@ -235,22 +235,22 @@
}

public void setSelection(IDevice device, Window window) {
        synchronized (mDeviceMap) {
            mSelectedDevice = device;
            mSelectedWindow = window;
}
notifySelectionChanged(device, window);
}

public IDevice getSelectedDevice() {
        synchronized (mDeviceMap) {
            return mSelectedDevice;
}
}

public Window getSelectedWindow() {
        synchronized (mDeviceMap) {
            return mSelectedWindow;
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java
//Synthetic comment -- index e52db14..004adb2 100644

//Synthetic comment -- @@ -35,52 +35,52 @@

public static final int DEFAULT_OVERLAY_TRANSPARENCY_PERCENTAGE = 50;

    private IDevice mDevice;

    private Image mImage;

    private Point mCrosshairLocation;

    private ViewNode mViewNode;

    private ViewNode mSelectedNode;

    private int mZoom;

    private final ArrayList<IImageChangeListener> mImageChangeListeners =
            new ArrayList<IImageChangeListener>();

    private Image mOverlayImage;

    private double mOverlayTransparency = DEFAULT_OVERLAY_TRANSPARENCY_PERCENTAGE / 100.0;

    private static PixelPerfectModel sModel;

public static PixelPerfectModel getModel() {
        if (sModel == null) {
            sModel = new PixelPerfectModel();
}
        return sModel;
}

public void setData(final IDevice device, final Image image, final ViewNode viewNode) {
        final Image toDispose = this.mImage;
        final Image toDispose2 = this.mOverlayImage;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.mDevice = device;
                    PixelPerfectModel.this.mImage = image;
                    PixelPerfectModel.this.mViewNode = viewNode;
if (image != null) {
                        PixelPerfectModel.this.mCrosshairLocation =
new Point(image.getBounds().width / 2, image.getBounds().height / 2);
} else {
                        PixelPerfectModel.this.mCrosshairLocation = null;
}
                    mOverlayImage = null;
                    PixelPerfectModel.this.mSelectedNode = null;
                    mZoom = DEFAULT_ZOOM;
}
}
});
//Synthetic comment -- @@ -104,14 +104,14 @@

public void setCrosshairLocation(int x, int y) {
synchronized (this) {
            mCrosshairLocation = new Point(x, y);
}
notifyCrosshairMoved();
}

public void setSelected(ViewNode selected) {
synchronized (this) {
            this.mSelectedNode = selected;
}
notifySelectionChanged();
}
//Synthetic comment -- @@ -120,8 +120,8 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.mViewNode = viewNode;
                    PixelPerfectModel.this.mSelectedNode = null;
}
}
});
//Synthetic comment -- @@ -129,11 +129,11 @@
}

public void setImage(final Image image) {
        final Image toDispose = this.mImage;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.mImage = image;
}
}
});
//Synthetic comment -- @@ -155,17 +155,17 @@
if (newZoom > MAX_ZOOM) {
newZoom = MAX_ZOOM;
}
            mZoom = newZoom;
}
notifyZoomChanged();
}

public void setOverlayImage(final Image overlayImage) {
        final Image toDispose = this.mOverlayImage;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.mOverlayImage = overlayImage;
}
}
});
//Synthetic comment -- @@ -183,60 +183,60 @@
synchronized (this) {
value = Math.max(value, 0);
value = Math.min(value, 1);
            mOverlayTransparency = value;
}
notifyOverlayTransparencyChanged();
}

public ViewNode getViewNode() {
synchronized (this) {
            return mViewNode;
}
}

public Point getCrosshairLocation() {
synchronized (this) {
            return mCrosshairLocation;
}
}

public Image getImage() {
synchronized (this) {
            return mImage;
}
}

public ViewNode getSelected() {
synchronized (this) {
            return mSelectedNode;
}
}

public IDevice getDevice() {
synchronized (this) {
            return mDevice;
}
}

public int getZoom() {
synchronized (this) {
            return mZoom;
}
}

public Image getOverlayImage() {
synchronized (this) {
            return mOverlayImage;
}
}

public double getOverlayTransparency() {
synchronized (this) {
            return mOverlayTransparency;
}
}

    public static interface IImageChangeListener {
public void imageLoaded();

public void imageChanged();
//Synthetic comment -- @@ -254,21 +254,21 @@
public void overlayTransparencyChanged();
}

    private IImageChangeListener[] getImageChangeListenerList() {
        IImageChangeListener[] listeners = null;
        synchronized (mImageChangeListeners) {
            if (mImageChangeListeners.size() == 0) {
return null;
}
listeners =
                    mImageChangeListeners.toArray(new IImageChangeListener[mImageChangeListeners
.size()]);
}
return listeners;
}

public void notifyImageLoaded() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].imageLoaded();
//Synthetic comment -- @@ -277,7 +277,7 @@
}

public void notifyImageChanged() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].imageChanged();
//Synthetic comment -- @@ -286,7 +286,7 @@
}

public void notifyCrosshairMoved() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].crosshairMoved();
//Synthetic comment -- @@ -295,7 +295,7 @@
}

public void notifySelectionChanged() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].selectionChanged();
//Synthetic comment -- @@ -304,7 +304,7 @@
}

public void notifyTreeChanged() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].treeChanged();
//Synthetic comment -- @@ -313,7 +313,7 @@
}

public void notifyZoomChanged() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].zoomChanged();
//Synthetic comment -- @@ -322,7 +322,7 @@
}

public void notifyOverlayChanged() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].overlayChanged();
//Synthetic comment -- @@ -331,7 +331,7 @@
}

public void notifyOverlayTransparencyChanged() {
        IImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].overlayTransparencyChanged();
//Synthetic comment -- @@ -339,15 +339,15 @@
}
}

    public void addImageChangeListener(IImageChangeListener listener) {
        synchronized (mImageChangeListeners) {
            mImageChangeListeners.add(listener);
}
}

    public void removeImageChangeListener(IImageChangeListener listener) {
        synchronized (mImageChangeListeners) {
            mImageChangeListeners.remove(listener);
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
//Synthetic comment -- index ba0fa57..279b5fd 100644

//Synthetic comment -- @@ -29,58 +29,58 @@

public static final double MIN_ZOOM = 0.2;

    private Window mWindow;

    private DrawableViewNode mTree;

    private DrawableViewNode mSelectedNode;

    private Rectangle mViewport;

    private double mZoom;

    private final ArrayList<ITreeChangeListener> mTreeChangeListeners =
            new ArrayList<ITreeChangeListener>();

    private static TreeViewModel sModel;

public static TreeViewModel getModel() {
        if (sModel == null) {
            sModel = new TreeViewModel();
}
        return sModel;
}

public void setData(Window window, ViewNode viewNode) {
synchronized (this) {
            if (mTree != null) {
                mTree.viewNode.dispose();
}
            this.mWindow = window;
if (viewNode == null) {
                mTree = null;
} else {
                mTree = new DrawableViewNode(viewNode);
                mTree.setLeft();
                mTree.placeRoot();
}
            mViewport = null;
            mZoom = 1;
            mSelectedNode = null;
}
notifyTreeChanged();
}

public void setSelection(DrawableViewNode selectedNode) {
synchronized (this) {
            this.mSelectedNode = selectedNode;
}
notifySelectionChanged();
}

public void setViewport(Rectangle viewport) {
synchronized (this) {
            this.mViewport = viewport;
}
notifyViewportChanged();
}
//Synthetic comment -- @@ -88,9 +88,9 @@
public void setZoom(double newZoom) {
Point zoomPoint = null;
synchronized (this) {
            if (mTree != null && mViewport != null) {
zoomPoint =
                        new Point(mViewport.x + mViewport.width / 2, mViewport.y + mViewport.height / 2);
}
}
zoomOnPoint(newZoom, zoomPoint);
//Synthetic comment -- @@ -98,18 +98,18 @@

public void zoomOnPoint(double newZoom, Point zoomPoint) {
synchronized (this) {
            if (mTree != null && this.mViewport != null) {
if (newZoom < MIN_ZOOM) {
newZoom = MIN_ZOOM;
}
if (newZoom > MAX_ZOOM) {
newZoom = MAX_ZOOM;
}
                mViewport.x = zoomPoint.x - (zoomPoint.x - mViewport.x) * mZoom / newZoom;
                mViewport.y = zoomPoint.y - (zoomPoint.y - mViewport.y) * mZoom / newZoom;
                mViewport.width = mViewport.width * mZoom / newZoom;
                mViewport.height = mViewport.height * mZoom / newZoom;
                mZoom = newZoom;
}
}
notifyZoomChanged();
//Synthetic comment -- @@ -117,35 +117,35 @@

public DrawableViewNode getTree() {
synchronized (this) {
            return mTree;
}
}

public Window getWindow() {
synchronized (this) {
            return mWindow;
}
}

public Rectangle getViewport() {
synchronized (this) {
            return mViewport;
}
}

public double getZoom() {
synchronized (this) {
            return mZoom;
}
}

public DrawableViewNode getSelection() {
synchronized (this) {
            return mSelectedNode;
}
}

    public static interface ITreeChangeListener {
public void treeChanged();

public void selectionChanged();
//Synthetic comment -- @@ -155,20 +155,20 @@
public void zoomChanged();
}

    private ITreeChangeListener[] getTreeChangeListenerList() {
        ITreeChangeListener[] listeners = null;
        synchronized (mTreeChangeListeners) {
            if (mTreeChangeListeners.size() == 0) {
return null;
}
listeners =
                    mTreeChangeListeners.toArray(new ITreeChangeListener[mTreeChangeListeners.size()]);
}
return listeners;
}

public void notifyTreeChanged() {
        ITreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].treeChanged();
//Synthetic comment -- @@ -177,7 +177,7 @@
}

public void notifySelectionChanged() {
        ITreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].selectionChanged();
//Synthetic comment -- @@ -186,7 +186,7 @@
}

public void notifyViewportChanged() {
        ITreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].viewportChanged();
//Synthetic comment -- @@ -195,7 +195,7 @@
}

public void notifyZoomChanged() {
        ITreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].zoomChanged();
//Synthetic comment -- @@ -203,15 +203,15 @@
}
}

    public void addTreeChangeListener(ITreeChangeListener listener) {
        synchronized (mTreeChangeListeners) {
            mTreeChangeListeners.add(listener);
}
}

    public void removeTreeChangeListener(ITreeChangeListener listener) {
        synchronized (mTreeChangeListeners) {
            mTreeChangeListeners.remove(listener);
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java
//Synthetic comment -- index ecee8d0..6ff8125 100644

//Synthetic comment -- @@ -40,172 +40,172 @@
import org.eclipse.swt.widgets.Shell;

public class CaptureDisplay {
    private static Shell sShell;

    private static Canvas sCanvas;

    private static Image sImage;

    private static ViewNode sViewNode;

    private static Composite sButtonBar;

    private static Button sOnWhite;

    private static Button sOnBlack;

    private static Button sShowExtras;

public static void show(Shell parentShell, ViewNode viewNode, Image image) {
        if (sShell == null) {
createShell();
}
        if (sShell.isVisible() && CaptureDisplay.sViewNode != null) {
            CaptureDisplay.sViewNode.dereferenceImage();
}
        CaptureDisplay.sImage = image;
        CaptureDisplay.sViewNode = viewNode;
viewNode.referenceImage();
        sShell.setText(viewNode.name);

        boolean shellVisible = sShell.isVisible();
if (!shellVisible) {
            sShell.setSize(0, 0);
}
Rectangle bounds =
                sShell.computeTrim(0, 0, Math.max(sButtonBar.getBounds().width,
                        image.getBounds().width), sButtonBar.getBounds().height
+ image.getBounds().height + 5);
        sShell.setSize(bounds.width, bounds.height);
if (!shellVisible) {
            sShell.setLocation(parentShell.getBounds().x
+ (parentShell.getBounds().width - bounds.width) / 2, parentShell.getBounds().y
+ (parentShell.getBounds().height - bounds.height) / 2);
}
        sShell.open();
if (shellVisible) {
            sCanvas.redraw();
}
}

private static void createShell() {
        sShell = new Shell(Display.getDefault(), SWT.CLOSE | SWT.TITLE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = 0;
gridLayout.marginHeight = 0;
        sShell.setLayout(gridLayout);

        sButtonBar = new Composite(sShell, SWT.NONE);
RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
rowLayout.pack = true;
rowLayout.center = true;
        sButtonBar.setLayout(rowLayout);
        Composite buttons = new Composite(sButtonBar, SWT.NONE);
buttons.setLayout(new FillLayout());

        sOnWhite = new Button(buttons, SWT.TOGGLE);
        sOnWhite.setText("On White");
        sOnBlack = new Button(buttons, SWT.TOGGLE);
        sOnBlack.setText("On Black");
        sOnBlack.setSelection(true);
        sOnWhite.addSelectionListener(sWhiteSelectionListener);
        sOnBlack.addSelectionListener(sBlackSelectionListener);

        sShowExtras = new Button(sButtonBar, SWT.CHECK);
        sShowExtras.setText("Show Extras");
        sShowExtras.addSelectionListener(sExtrasSelectionListener);

        sCanvas = new Canvas(sShell, SWT.NONE);
        sCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
        sCanvas.addPaintListener(sPaintListener);

        sShell.addShellListener(sShellListener);

ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("display.png", Display.getDefault()); //$NON-NLS-1$
        sShell.setImage(image);
}

    private static PaintListener sPaintListener = new PaintListener() {

public void paintControl(PaintEvent e) {
            if (sOnWhite.getSelection()) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
} else {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
}
            e.gc.fillRectangle(0, 0, sCanvas.getBounds().width, sCanvas.getBounds().height);
            if (sImage != null) {
                int width = sImage.getBounds().width;
                int height = sImage.getBounds().height;
                int x = (sCanvas.getBounds().width - width) / 2;
                int y = (sCanvas.getBounds().height - height) / 2;
                e.gc.drawImage(sImage, x, y);
                if (sShowExtras.getSelection()) {
                    if ((sViewNode.paddingLeft | sViewNode.paddingRight | sViewNode.paddingTop | sViewNode.paddingBottom) != 0) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
                        e.gc.drawRectangle(x + sViewNode.paddingLeft, y + sViewNode.paddingTop, width
                                - sViewNode.paddingLeft - sViewNode.paddingRight - 1, height
                                - sViewNode.paddingTop - sViewNode.paddingBottom - 1);
}
                    if (sViewNode.hasMargins) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
                        e.gc.drawRectangle(x - sViewNode.marginLeft, y - sViewNode.marginTop, width
                                + sViewNode.marginLeft + sViewNode.marginRight - 1, height
                                + sViewNode.marginTop + sViewNode.marginBottom - 1);
}
                    if (sViewNode.baseline != -1) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                        e.gc.drawLine(x, y + sViewNode.baseline, x + width - 1, sViewNode.baseline);
}
}
}
}
};

    private static ShellAdapter sShellListener = new ShellAdapter() {
@Override
public void shellClosed(ShellEvent e) {
e.doit = false;
            sShell.setVisible(false);
            if (sViewNode != null) {
                sViewNode.dereferenceImage();
}
}

};

    private static SelectionListener sWhiteSelectionListener = new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
// pass
}

public void widgetSelected(SelectionEvent e) {
            sOnWhite.setSelection(true);
            sOnBlack.setSelection(false);
            sCanvas.redraw();
}
};

    private static SelectionListener sBlackSelectionListener = new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
// pass
}

public void widgetSelected(SelectionEvent e) {
            sOnBlack.setSelection(true);
            sOnWhite.setSelection(false);
            sCanvas.redraw();
}
};

    private static SelectionListener sExtrasSelectionListener = new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
// pass
}

public void widgetSelected(SelectionEvent e) {
            sCanvas.redraw();
}
};
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java
//Synthetic comment -- index 4e0748f..4f2e17e 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.IWindowChangeListener;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
//Synthetic comment -- @@ -47,29 +47,29 @@
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class DeviceSelector extends Composite implements IWindowChangeListener, SelectionListener {
    private TreeViewer mTreeViewer;

    private Tree mTree;

    private DeviceSelectionModel mModel;

    private Font mBoldFont;

    private Image mDeviceImage;

    private Image mEmulatorImage;

private final static int ICON_WIDTH = 16;

    private boolean mDoTreeViewStuff;

    private boolean mDoPixelPerfectStuff;

private class ContentProvider implements ITreeContentProvider, ILabelProvider, IFontProvider {
public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof IDevice && mDoTreeViewStuff) {
                Window[] list = mModel.getWindows((IDevice) parentElement);
if (list != null) {
return list;
}
//Synthetic comment -- @@ -85,8 +85,8 @@
}

public boolean hasChildren(Object element) {
            if (element instanceof IDevice && mDoTreeViewStuff) {
                Window[] list = mModel.getWindows((IDevice) element);
if (list != null) {
return list.length != 0;
}
//Synthetic comment -- @@ -96,7 +96,7 @@

public Object[] getElements(Object inputElement) {
if (inputElement instanceof DeviceSelectionModel) {
                return mModel.getDevices();
}
return new Object[0];
}
//Synthetic comment -- @@ -112,9 +112,9 @@
public Image getImage(Object element) {
if (element instanceof IDevice) {
if (((IDevice) element).isEmulator()) {
                    return mEmulatorImage;
}
                return mDeviceImage;
}
return null;
}
//Synthetic comment -- @@ -130,9 +130,9 @@

public Font getFont(Object element) {
if (element instanceof Window) {
                int focusedWindow = mModel.getFocusedWindow(((Window) element).getDevice());
if (focusedWindow == ((Window) element).getHashCode()) {
                    return mBoldFont;
}
}
return null;
//Synthetic comment -- @@ -154,28 +154,28 @@

public DeviceSelector(Composite parent, boolean doTreeViewStuff, boolean doPixelPerfectStuff) {
super(parent, SWT.NONE);
        this.mDoTreeViewStuff = doTreeViewStuff;
        this.mDoPixelPerfectStuff = doPixelPerfectStuff;
setLayout(new FillLayout());
        mTreeViewer = new TreeViewer(this, SWT.SINGLE);
        mTreeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

        mTree = mTreeViewer.getTree();
        mTree.setLinesVisible(true);
        mTree.addSelectionListener(this);

        addDisposeListener(mDisposeListener);

loadResources();

        mModel = DeviceSelectionModel.getModel();
ContentProvider contentProvider = new ContentProvider();
        mTreeViewer.setContentProvider(contentProvider);
        mTreeViewer.setLabelProvider(contentProvider);
        mModel.addWindowChangeListener(this);
        mTreeViewer.setInput(mModel);

        addControlListener(mControlListener);
}

public void loadResources() {
//Synthetic comment -- @@ -189,38 +189,38 @@
.getStyle()
| SWT.BOLD);
}
        mBoldFont = new Font(Display.getDefault(), newFontData);

ImageLoader loader = ImageLoader.getDdmUiLibLoader();
        mDeviceImage =
                loader.loadImage(display, "device.png", ICON_WIDTH, ICON_WIDTH, display //$NON-NLS-1$
.getSystemColor(SWT.COLOR_RED));

        mEmulatorImage =
                loader.loadImage(display, "emulator.png", ICON_WIDTH, ICON_WIDTH, display //$NON-NLS-1$
.getSystemColor(SWT.COLOR_BLUE));
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeWindowChangeListener(DeviceSelector.this);
            mBoldFont.dispose();
}
};

// If the window gets too small, hide the data, otherwise SWT throws an
// ERROR.

    private ControlListener mControlListener = new ControlAdapter() {
private boolean noInput = false;

@Override
public void controlResized(ControlEvent e) {
if (getBounds().height <= 38) {
                mTreeViewer.setInput(null);
noInput = true;
} else if (noInput) {
                mTreeViewer.setInput(mModel);
noInput = false;
}
}
//Synthetic comment -- @@ -228,20 +228,20 @@

@Override
public boolean setFocus() {
        return mTree.setFocus();
}

public void setMode(boolean doTreeViewStuff, boolean doPixelPerfectStuff) {
        if (this.mDoTreeViewStuff != doTreeViewStuff
                || this.mDoPixelPerfectStuff != doPixelPerfectStuff) {
            final boolean expandAll = !this.mDoTreeViewStuff && doTreeViewStuff;
            this.mDoTreeViewStuff = doTreeViewStuff;
            this.mDoPixelPerfectStuff = doPixelPerfectStuff;
Display.getDefault().syncExec(new Runnable() {
public void run() {
                    mTreeViewer.refresh();
if (expandAll) {
                        mTreeViewer.expandAll();
}
}
});
//Synthetic comment -- @@ -251,8 +251,8 @@
public void deviceConnected(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mTreeViewer.refresh();
                mTreeViewer.setExpandedState(device, true);
}
});
}
//Synthetic comment -- @@ -260,11 +260,11 @@
public void deviceChanged(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                TreeSelection selection = (TreeSelection) mTreeViewer.getSelection();
                mTreeViewer.refresh(device);
if (selection.getFirstElement() instanceof Window
&& ((Window) selection.getFirstElement()).getDevice() == device) {
                    mTreeViewer.setSelection(selection, true);
}
}
});
//Synthetic comment -- @@ -273,7 +273,7 @@
public void deviceDisconnected(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mTreeViewer.refresh();
}
});
}
//Synthetic comment -- @@ -281,11 +281,11 @@
public void focusChanged(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                TreeSelection selection = (TreeSelection) mTreeViewer.getSelection();
                mTreeViewer.refresh(device);
if (selection.getFirstElement() instanceof Window
&& ((Window) selection.getFirstElement()).getDevice() == device) {
                    mTreeViewer.setSelection(selection, true);
}
}
});
//Synthetic comment -- @@ -297,9 +297,9 @@

public void widgetDefaultSelected(SelectionEvent e) {
Object selection = ((TreeItem) e.item).getData();
        if (selection instanceof IDevice && mDoPixelPerfectStuff) {
HierarchyViewerDirector.getDirector().loadPixelPerfectData((IDevice) selection);
        } else if (selection instanceof Window && mDoTreeViewStuff) {
HierarchyViewerDirector.getDirector().loadViewTreeData((Window) selection);
}
}
//Synthetic comment -- @@ -307,9 +307,9 @@
public void widgetSelected(SelectionEvent e) {
Object selection = ((TreeItem) e.item).getData();
if (selection instanceof IDevice) {
            mModel.setSelection((IDevice) selection, null);
} else if (selection instanceof Window) {
            mModel.setSelection(((Window) selection).getDevice(), (Window) selection);
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java
//Synthetic comment -- index 1a13e48..94c63aa 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;

//Synthetic comment -- @@ -40,66 +40,66 @@

import java.util.ArrayList;

public class LayoutViewer extends Canvas implements ITreeChangeListener {

    private TreeViewModel mModel;

    private DrawableViewNode mTree;

    private DrawableViewNode mSelectedNode;

    private Transform mTransform;

    private Transform mInverse;

    private double mScale;

    private boolean mShowExtras = false;

    private boolean mOnBlack = true;

public LayoutViewer(Composite parent) {
super(parent, SWT.NONE);
        mModel = TreeViewModel.getModel();
        mModel.addTreeChangeListener(this);

        addDisposeListener(mDisposeListener);
        addPaintListener(mPaintListener);
        addListener(SWT.Resize, mResizeListener);
        addMouseListener(mMouseListener);

        mTransform = new Transform(Display.getDefault());
        mInverse = new Transform(Display.getDefault());

treeChanged();
}

public void setShowExtras(boolean show) {
        mShowExtras = show;
doRedraw();
}

public void setOnBlack(boolean value) {
        mOnBlack = value;
doRedraw();
}

public boolean getOnBlack() {
        return mOnBlack;
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeTreeChangeListener(LayoutViewer.this);
            mTransform.dispose();
            mInverse.dispose();
            if (mSelectedNode != null) {
                mSelectedNode.viewNode.dereferenceImage();
}
}
};

    private Listener mResizeListener = new Listener() {
public void handleEvent(Event e) {
synchronized (this) {
setTransform();
//Synthetic comment -- @@ -107,12 +107,12 @@
}
};

    private MouseListener mMouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
            if (mSelectedNode != null) {
HierarchyViewerDirector.getDirector()
                        .showCapture(getShell(), mSelectedNode.viewNode);
}
}

//Synthetic comment -- @@ -120,21 +120,21 @@
boolean selectionChanged = false;
DrawableViewNode newSelection = null;
synchronized (LayoutViewer.this) {
                if (mTree != null) {
float[] pt = {
e.x, e.y
};
                    mInverse.transform(pt);
newSelection =
                            updateSelection(mTree, pt[0], pt[1], 0, 0, 0, 0, mTree.viewNode.width,
                                    mTree.viewNode.height);
                    if (mSelectedNode != newSelection) {
selectionChanged = true;
}
}
}
if (selectionChanged) {
                mModel.setSelection(newSelection);
}
}

//Synthetic comment -- @@ -175,29 +175,29 @@
return node;
}

    private PaintListener mPaintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (LayoutViewer.this) {
                if (mOnBlack) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
} else {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
}
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                if (mTree != null) {
                    e.gc.setLineWidth((int) Math.ceil(0.3 / mScale));
                    e.gc.setTransform(mTransform);
                    if (mOnBlack) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
} else {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
}
Rectangle parentClipping = e.gc.getClipping();
                    e.gc.setClipping(0, 0, mTree.viewNode.width + (int) Math.ceil(0.3 / mScale),
                            mTree.viewNode.height + (int) Math.ceil(0.3 / mScale));
                    paintRecursive(e.gc, mTree, 0, 0, true);

                    if (mSelectedNode != null) {
e.gc.setClipping(parentClipping);

// w00t, let's be nice and display the whole path in
//Synthetic comment -- @@ -205,8 +205,8 @@
ArrayList<Point> rightLeftDistances = new ArrayList<Point>();
int left = 0;
int top = 0;
                        DrawableViewNode currentNode = mSelectedNode;
                        while (currentNode != mTree) {
left += currentNode.viewNode.left;
top += currentNode.viewNode.top;
currentNode = currentNode.parent;
//Synthetic comment -- @@ -215,7 +215,7 @@
rightLeftDistances.add(new Point(left, top));
}
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
                        currentNode = mSelectedNode.parent;
final int N = rightLeftDistances.size();
for (int i = 0; i < N; i++) {
e.gc.drawRectangle((int) (left - rightLeftDistances.get(i).x),
//Synthetic comment -- @@ -224,23 +224,23 @@
currentNode = currentNode.parent;
}

                        if (mShowExtras && mSelectedNode.viewNode.image != null) {
                            e.gc.drawImage(mSelectedNode.viewNode.image, left, top);
                            if (mOnBlack) {
e.gc.setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_WHITE));
} else {
e.gc.setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_BLACK));
}
                            paintRecursive(e.gc, mSelectedNode, left, top, true);

}

e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                        e.gc.setLineWidth((int) Math.ceil(2 / mScale));
                        e.gc.drawRectangle(left, top, mSelectedNode.viewNode.width,
                                mSelectedNode.viewNode.height);
}
}
}
//Synthetic comment -- @@ -260,11 +260,11 @@
int x1 = Math.max(parentClipping.x, left);
int x2 =
Math.min(parentClipping.x + parentClipping.width, left + node.viewNode.width
                        + (int) Math.ceil(0.3 / mScale));
int y1 = Math.max(parentClipping.y, top);
int y2 =
Math.min(parentClipping.y + parentClipping.height, top + node.viewNode.height
                        + (int) Math.ceil(0.3 / mScale));

// Clipping is weird... You set it to -5 and it comes out 17 or
// something.
//Synthetic comment -- @@ -293,38 +293,38 @@
}

private void setTransform() {
        if (mTree != null) {
Rectangle bounds = getBounds();
int leftRightPadding = bounds.width <= 30 ? 0 : 5;
int topBottomPadding = bounds.height <= 30 ? 0 : 5;
            mScale =
                    Math.min(1.0 * (bounds.width - leftRightPadding * 2) / mTree.viewNode.width, 1.0
                            * (bounds.height - topBottomPadding * 2) / mTree.viewNode.height);
            int scaledWidth = (int) Math.ceil(mTree.viewNode.width * mScale);
            int scaledHeight = (int) Math.ceil(mTree.viewNode.height * mScale);

            mTransform.identity();
            mInverse.identity();
            mTransform.translate((bounds.width - scaledWidth) / 2.0f,
(bounds.height - scaledHeight) / 2.0f);
            mInverse.translate((bounds.width - scaledWidth) / 2.0f,
(bounds.height - scaledHeight) / 2.0f);
            mTransform.scale((float) mScale, (float) mScale);
            mInverse.scale((float) mScale, (float) mScale);
if (bounds.width != 0 && bounds.height != 0) {
                mInverse.invert();
}
}
}

public void selectionChanged() {
synchronized (this) {
            if (mSelectedNode != null) {
                mSelectedNode.viewNode.dereferenceImage();
}
            mSelectedNode = mModel.getSelection();
            if (mSelectedNode != null) {
                mSelectedNode.viewNode.referenceImage();
}
}
doRedraw();
//Synthetic comment -- @@ -335,13 +335,13 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    if (mSelectedNode != null) {
                        mSelectedNode.viewNode.dereferenceImage();
}
                    mTree = mModel.getTree();
                    mSelectedNode = mModel.getSelection();
                    if (mSelectedNode != null) {
                        mSelectedNode.viewNode.referenceImage();
}
setTransform();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java
//Synthetic comment -- index 42bcc59..90e19c6 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
//Synthetic comment -- @@ -39,72 +39,72 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfect extends ScrolledComposite implements IImageChangeListener {
    private Canvas mCanvas;

    private PixelPerfectModel mModel;

    private Image mImage;

    private Color mCrosshairColor;

    private Color mMarginColor;

    private Color mBorderColor;

    private Color mPaddingColor;

    private int mWidth;

    private int mHeight;

    private Point mCrosshairLocation;

    private ViewNode mSelectedNode;

    private Image mOverlayImage;

    private double mOverlayTransparency;

public PixelPerfect(Composite parent) {
super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        mCanvas = new Canvas(this, SWT.NONE);
        setContent(mCanvas);
setExpandHorizontal(true);
setExpandVertical(true);
        mModel = PixelPerfectModel.getModel();
        mModel.addImageChangeListener(this);

        mCanvas.addPaintListener(mPaintListener);
        mCanvas.addMouseListener(mMouseListener);
        mCanvas.addMouseMoveListener(mMouseMoveListener);
        mCanvas.addKeyListener(mKeyListener);

        addDisposeListener(mDisposeListener);

        mCrosshairColor = new Color(Display.getDefault(), new RGB(0, 255, 255));
        mBorderColor = new Color(Display.getDefault(), new RGB(255, 0, 0));
        mMarginColor = new Color(Display.getDefault(), new RGB(0, 255, 0));
        mPaddingColor = new Color(Display.getDefault(), new RGB(0, 0, 255));

imageLoaded();
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeImageChangeListener(PixelPerfect.this);
            mCrosshairColor.dispose();
            mBorderColor.dispose();
            mPaddingColor.dispose();
}
};

@Override
public boolean setFocus() {
        return mCanvas.setFocus();
}

    private MouseListener mMouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
// pass
//Synthetic comment -- @@ -120,7 +120,7 @@

};

    private MouseMoveListener mMouseMoveListener = new MouseMoveListener() {
public void mouseMove(MouseEvent e) {
if (e.stateMask != 0) {
handleMouseEvent(e);
//Synthetic comment -- @@ -130,49 +130,49 @@

private void handleMouseEvent(MouseEvent e) {
synchronized (PixelPerfect.this) {
            if (mImage == null) {
return;
}
            int leftOffset = mCanvas.getSize().x / 2 - mWidth / 2;
            int topOffset = mCanvas.getSize().y / 2 - mHeight / 2;
e.x -= leftOffset;
e.y -= topOffset;
e.x = Math.max(e.x, 0);
            e.x = Math.min(e.x, mWidth - 1);
e.y = Math.max(e.y, 0);
            e.y = Math.min(e.y, mHeight - 1);
}
        mModel.setCrosshairLocation(e.x, e.y);
}

    private KeyListener mKeyListener = new KeyListener() {

public void keyPressed(KeyEvent e) {
boolean crosshairMoved = false;
synchronized (PixelPerfect.this) {
                if (mImage != null) {
switch (e.keyCode) {
case SWT.ARROW_UP:
                            if (mCrosshairLocation.y != 0) {
                                mCrosshairLocation.y--;
crosshairMoved = true;
}
break;
case SWT.ARROW_DOWN:
                            if (mCrosshairLocation.y != mHeight - 1) {
                                mCrosshairLocation.y++;
crosshairMoved = true;
}
break;
case SWT.ARROW_LEFT:
                            if (mCrosshairLocation.x != 0) {
                                mCrosshairLocation.x--;
crosshairMoved = true;
}
break;
case SWT.ARROW_RIGHT:
                            if (mCrosshairLocation.x != mWidth - 1) {
                                mCrosshairLocation.x++;
crosshairMoved = true;
}
break;
//Synthetic comment -- @@ -180,7 +180,7 @@
}
}
if (crosshairMoved) {
                mModel.setCrosshairLocation(mCrosshairLocation.x, mCrosshairLocation.y);
}
}

//Synthetic comment -- @@ -190,43 +190,43 @@

};

    private PaintListener mPaintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (PixelPerfect.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                e.gc.fillRectangle(0, 0, mCanvas.getSize().x, mCanvas.getSize().y);
                if (mImage != null) {
// Let's be cool and put it in the center...
                    int leftOffset = mCanvas.getSize().x / 2 - mWidth / 2;
                    int topOffset = mCanvas.getSize().y / 2 - mHeight / 2;
                    e.gc.drawImage(mImage, leftOffset, topOffset);
                    if (mOverlayImage != null) {
                        e.gc.setAlpha((int) (mOverlayTransparency * 255));
int overlayTopOffset =
                                mCanvas.getSize().y / 2 + mHeight / 2
                                        - mOverlayImage.getBounds().height;
                        e.gc.drawImage(mOverlayImage, leftOffset, overlayTopOffset);
e.gc.setAlpha(255);
}

                    if (mSelectedNode != null) {
// If the screen is in landscape mode, the
// coordinates are backwards.
int leftShift = 0;
int topShift = 0;
                        int nodeLeft = mSelectedNode.left;
                        int nodeTop = mSelectedNode.top;
                        int nodeWidth = mSelectedNode.width;
                        int nodeHeight = mSelectedNode.height;
                        int nodeMarginLeft = mSelectedNode.marginLeft;
                        int nodeMarginTop = mSelectedNode.marginTop;
                        int nodeMarginRight = mSelectedNode.marginRight;
                        int nodeMarginBottom = mSelectedNode.marginBottom;
                        int nodePadLeft = mSelectedNode.paddingLeft;
                        int nodePadTop = mSelectedNode.paddingTop;
                        int nodePadRight = mSelectedNode.paddingRight;
                        int nodePadBottom = mSelectedNode.paddingBottom;
                        ViewNode cur = mSelectedNode;
while (cur.parent != null) {
leftShift += cur.parent.left - cur.parent.scrollX;
topShift += cur.parent.top - cur.parent.scrollY;
//Synthetic comment -- @@ -235,44 +235,44 @@

// Everything is sideways.
if (cur.width > cur.height) {
                            e.gc.setForeground(mPaddingColor);
                            e.gc.drawRectangle(leftOffset + mWidth - nodeTop - topShift - nodeHeight
+ nodePadBottom,
topOffset + leftShift + nodeLeft + nodePadLeft, nodeHeight
- nodePadBottom - nodePadTop, nodeWidth - nodePadRight
- nodePadLeft);
                            e.gc.setForeground(mMarginColor);
                            e.gc.drawRectangle(leftOffset + mWidth - nodeTop - topShift - nodeHeight
- nodeMarginBottom, topOffset + leftShift + nodeLeft
- nodeMarginLeft,
nodeHeight + nodeMarginBottom + nodeMarginTop, nodeWidth
+ nodeMarginRight + nodeMarginLeft);
                            e.gc.setForeground(mBorderColor);
e.gc.drawRectangle(
                                    leftOffset + mWidth - nodeTop - topShift - nodeHeight, topOffset
+ leftShift + nodeLeft, nodeHeight, nodeWidth);
} else {
                            e.gc.setForeground(mPaddingColor);
e.gc.drawRectangle(leftOffset + leftShift + nodeLeft + nodePadLeft,
topOffset + topShift + nodeTop + nodePadTop, nodeWidth
- nodePadRight - nodePadLeft, nodeHeight
- nodePadBottom - nodePadTop);
                            e.gc.setForeground(mMarginColor);
e.gc.drawRectangle(leftOffset + leftShift + nodeLeft - nodeMarginLeft,
topOffset + topShift + nodeTop - nodeMarginTop, nodeWidth
+ nodeMarginRight + nodeMarginLeft, nodeHeight
+ nodeMarginBottom + nodeMarginTop);
                            e.gc.setForeground(mBorderColor);
e.gc.drawRectangle(leftOffset + leftShift + nodeLeft, topOffset
+ topShift + nodeTop, nodeWidth, nodeHeight);
}
}
                    if (mCrosshairLocation != null) {
                        e.gc.setForeground(mCrosshairColor);
                        e.gc.drawLine(leftOffset, topOffset + mCrosshairLocation.y, leftOffset
                                + mWidth - 1, topOffset + mCrosshairLocation.y);
                        e.gc.drawLine(leftOffset + mCrosshairLocation.x, topOffset, leftOffset
                                + mCrosshairLocation.x, topOffset + mHeight - 1);
}
}
}
//Synthetic comment -- @@ -282,21 +282,21 @@
private void doRedraw() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mCanvas.redraw();
}
});
}

private void loadImage() {
        mImage = mModel.getImage();
        if (mImage != null) {
            mWidth = mImage.getBounds().width;
            mHeight = mImage.getBounds().height;
} else {
            mWidth = 0;
            mHeight = 0;
}
        setMinSize(mWidth, mHeight);
}

public void imageLoaded() {
//Synthetic comment -- @@ -304,10 +304,10 @@
public void run() {
synchronized (this) {
loadImage();
                    mCrosshairLocation = mModel.getCrosshairLocation();
                    mSelectedNode = mModel.getSelected();
                    mOverlayImage = mModel.getOverlayImage();
                    mOverlayTransparency = mModel.getOverlayTransparency();
}
}
});
//Synthetic comment -- @@ -327,14 +327,14 @@

public void crosshairMoved() {
synchronized (this) {
            mCrosshairLocation = mModel.getCrosshairLocation();
}
doRedraw();
}

public void selectionChanged() {
synchronized (this) {
            mSelectedNode = mModel.getSelected();
}
doRedraw();
}
//Synthetic comment -- @@ -344,7 +344,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    mSelectedNode = mModel.getSelected();
}
}
});
//Synthetic comment -- @@ -357,15 +357,15 @@

public void overlayChanged() {
synchronized (this) {
            mOverlayImage = mModel.getOverlayImage();
            mOverlayTransparency = mModel.getOverlayTransparency();
}
doRedraw();
}

public void overlayTransparencyChanged() {
synchronized (this) {
            mOverlayTransparency = mModel.getOverlayTransparency();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectControls.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectControls.java
//Synthetic comment -- index 5a593f6..3114d34 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -34,13 +34,13 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

public class PixelPerfectControls extends Composite implements IImageChangeListener {

    private Slider mOverlaySlider;

    private Slider mZoomSlider;

    private Slider mAutoRefreshSlider;

public PixelPerfectControls(Composite parent) {
super(parent, SWT.NONE);
//Synthetic comment -- @@ -114,56 +114,56 @@
zoomLeftData.left = new FormAttachment(zoom, 2);
zoomLeft.setLayoutData(zoomLeftData);

        mOverlaySlider = new Slider(this, SWT.HORIZONTAL);
        mOverlaySlider.setMinimum(0);
        mOverlaySlider.setMaximum(101);
        mOverlaySlider.setThumb(1);
        mOverlaySlider.setSelection((int) Math.round(PixelPerfectModel.getModel()
.getOverlayTransparency() * 100));

Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
        mOverlaySlider.setEnabled(overlayImage != null);
FormData overlaySliderData = new FormData();
overlaySliderData.right = new FormAttachment(overlayTransparencyRight, -4);
overlaySliderData.top = new FormAttachment(0, 2);
overlaySliderData.left = new FormAttachment(overlayTransparencyLeft, 4);
        mOverlaySlider.setLayoutData(overlaySliderData);

        mOverlaySlider.addSelectionListener(overlaySliderSelectionListener);

        mAutoRefreshSlider = new Slider(this, SWT.HORIZONTAL);
        mAutoRefreshSlider.setMinimum(1);
        mAutoRefreshSlider.setMaximum(41);
        mAutoRefreshSlider.setThumb(1);
        mAutoRefreshSlider.setSelection(HierarchyViewerDirector.getDirector()
.getPixelPerfectAutoRefreshInverval());
FormData refreshSliderData = new FormData();
refreshSliderData.right = new FormAttachment(overlayTransparencyRight, -4);
refreshSliderData.top = new FormAttachment(overlayTransparencyRight, 2);
        refreshSliderData.left = new FormAttachment(mOverlaySlider, 0, SWT.LEFT);
        mAutoRefreshSlider.setLayoutData(refreshSliderData);

        mAutoRefreshSlider.addSelectionListener(mRefreshSliderSelectionListener);

        mZoomSlider = new Slider(this, SWT.HORIZONTAL);
        mZoomSlider.setMinimum(2);
        mZoomSlider.setMaximum(25);
        mZoomSlider.setThumb(1);
        mZoomSlider.setSelection(PixelPerfectModel.getModel().getZoom());
FormData zoomSliderData = new FormData();
zoomSliderData.right = new FormAttachment(overlayTransparencyRight, -4);
zoomSliderData.top = new FormAttachment(refreshRight, 2);
        zoomSliderData.left = new FormAttachment(mOverlaySlider, 0, SWT.LEFT);
        mZoomSlider.setLayoutData(zoomSliderData);

        mZoomSlider.addSelectionListener(mZoomSliderSelectionListener);

        addDisposeListener(mDisposeListener);

PixelPerfectModel.getModel().addImageChangeListener(this);
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
PixelPerfectModel.getModel().removeImageChangeListener(PixelPerfectControls.this);
}
//Synthetic comment -- @@ -177,7 +177,7 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = mOverlaySlider.getSelection();
if (oldValue != newValue) {
PixelPerfectModel.getModel().removeImageChangeListener(PixelPerfectControls.this);
PixelPerfectModel.getModel().setOverlayTransparency(newValue / 100.0);
//Synthetic comment -- @@ -187,7 +187,7 @@
}
};

    private SelectionListener mRefreshSliderSelectionListener = new SelectionListener() {
private int oldValue;

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -195,14 +195,14 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = mAutoRefreshSlider.getSelection();
if (oldValue != newValue) {
HierarchyViewerDirector.getDirector().setPixelPerfectAutoRefreshInterval(newValue);
}
}
};

    private SelectionListener mZoomSliderSelectionListener = new SelectionListener() {
private int oldValue;

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -210,7 +210,7 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = mZoomSlider.getSelection();
if (oldValue != newValue) {
PixelPerfectModel.getModel().removeImageChangeListener(PixelPerfectControls.this);
PixelPerfectModel.getModel().setZoom(newValue);
//Synthetic comment -- @@ -236,10 +236,10 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
                mOverlaySlider.setEnabled(overlayImage != null);
if (PixelPerfectModel.getModel().getImage() == null) {
} else {
                    mZoomSlider.setSelection(PixelPerfectModel.getModel().getZoom());
}
}
});
//Synthetic comment -- @@ -249,7 +249,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
                mOverlaySlider.setEnabled(overlayImage != null);
}
});
}
//Synthetic comment -- @@ -257,7 +257,7 @@
public void overlayTransparencyChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mOverlaySlider.setSelection((int) (PixelPerfectModel.getModel()
.getOverlayTransparency() * 100));
}
});
//Synthetic comment -- @@ -270,7 +270,7 @@
public void zoomChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mZoomSlider.setSelection(PixelPerfectModel.getModel().getZoom());
}
});
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java
//Synthetic comment -- index 53afc9e..129dc4d 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.hierarchyviewerlib.ui;

import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -42,72 +42,72 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectLoupe extends Canvas implements IImageChangeListener {
    private PixelPerfectModel mModel;

    private Image mImage;

    private Image mGrid;

    private Color mCrosshairColor;

    private int mWidth;

    private int mHeight;

    private Point mCrosshairLocation;

    private int mZoom;

    private Transform mTransform;

    private int mCanvasWidth;

    private int mCanvasHeight;

    private Image mOverlayImage;

    private double mOverlayTransparency;

    private boolean mShowOverlay = false;

public PixelPerfectLoupe(Composite parent) {
super(parent, SWT.NONE);
        mModel = PixelPerfectModel.getModel();
        mModel.addImageChangeListener(this);

        addPaintListener(mPaintListener);
        addMouseListener(mMouseListener);
        addMouseWheelListener(mMouseWheelListener);
        addDisposeListener(mDisposeListener);
        addKeyListener(mKeyListener);

        mCrosshairColor = new Color(Display.getDefault(), new RGB(255, 94, 254));

        mTransform = new Transform(Display.getDefault());

imageLoaded();
}

public void setShowOverlay(boolean value) {
synchronized (this) {
            mShowOverlay = value;
}
doRedraw();
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeImageChangeListener(PixelPerfectLoupe.this);
            mCrosshairColor.dispose();
            mTransform.dispose();
            if (mGrid != null) {
                mGrid.dispose();
}
}
};

    private MouseListener mMouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
// pass
//Synthetic comment -- @@ -123,20 +123,20 @@

};

    private MouseWheelListener mMouseWheelListener = new MouseWheelListener() {
public void mouseScrolled(MouseEvent e) {
int newZoom = -1;
synchronized (PixelPerfectLoupe.this) {
                if (mImage != null && mCrosshairLocation != null) {
if (e.count > 0) {
                        newZoom = mZoom + 1;
} else {
                        newZoom = mZoom - 1;
}
}
}
if (newZoom != -1) {
                mModel.setZoom(newZoom);
}
}
};
//Synthetic comment -- @@ -145,51 +145,51 @@
int newX = -1;
int newY = -1;
synchronized (PixelPerfectLoupe.this) {
            if (mImage == null) {
return;
}
            int zoomedX = -mCrosshairLocation.x * mZoom - mZoom / 2 + getBounds().width / 2;
            int zoomedY = -mCrosshairLocation.y * mZoom - mZoom / 2 + getBounds().height / 2;
            int x = (e.x - zoomedX) / mZoom;
            int y = (e.y - zoomedY) / mZoom;
            if (x >= 0 && x < mWidth && y >= 0 && y < mHeight) {
newX = x;
newY = y;
}
}
if (newX != -1) {
            mModel.setCrosshairLocation(newX, newY);
}
}

    private KeyListener mKeyListener = new KeyListener() {

public void keyPressed(KeyEvent e) {
boolean crosshairMoved = false;
synchronized (PixelPerfectLoupe.this) {
                if (mImage != null) {
switch (e.keyCode) {
case SWT.ARROW_UP:
                            if (mCrosshairLocation.y != 0) {
                                mCrosshairLocation.y--;
crosshairMoved = true;
}
break;
case SWT.ARROW_DOWN:
                            if (mCrosshairLocation.y != mHeight - 1) {
                                mCrosshairLocation.y++;
crosshairMoved = true;
}
break;
case SWT.ARROW_LEFT:
                            if (mCrosshairLocation.x != 0) {
                                mCrosshairLocation.x--;
crosshairMoved = true;
}
break;
case SWT.ARROW_RIGHT:
                            if (mCrosshairLocation.x != mWidth - 1) {
                                mCrosshairLocation.x++;
crosshairMoved = true;
}
break;
//Synthetic comment -- @@ -197,7 +197,7 @@
}
}
if (crosshairMoved) {
                mModel.setCrosshairLocation(mCrosshairLocation.x, mCrosshairLocation.y);
}
}

//Synthetic comment -- @@ -207,70 +207,69 @@

};

    private PaintListener mPaintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (PixelPerfectLoupe.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getSize().x, getSize().y);
                if (mImage != null && mCrosshairLocation != null) {
                    int zoomedX = -mCrosshairLocation.x * mZoom - mZoom / 2 + getBounds().width / 2;
                    int zoomedY = -mCrosshairLocation.y * mZoom - mZoom / 2 + getBounds().height / 2;
                    mTransform.translate(zoomedX, zoomedY);
                    mTransform.scale(mZoom, mZoom);
e.gc.setInterpolation(SWT.NONE);
                    e.gc.setTransform(mTransform);
                    e.gc.drawImage(mImage, 0, 0);
                    if (mShowOverlay && mOverlayImage != null) {
                        e.gc.setAlpha((int) (mOverlayTransparency * 255));
                        e.gc.drawImage(mOverlayImage, 0, mHeight - mOverlayImage.getBounds().height);
e.gc.setAlpha(255);
}

                    mTransform.identity();
                    e.gc.setTransform(mTransform);

// If the size of the canvas has changed, we need to make
// another grid.
                    if (mGrid != null
                            && (mCanvasWidth != getBounds().width || mCanvasHeight != getBounds().height)) {
                        mGrid.dispose();
                        mGrid = null;
}
                    mCanvasWidth = getBounds().width;
                    mCanvasHeight = getBounds().height;
                    if (mGrid == null) {
// Make a transparent image;
ImageData imageData =
                                new ImageData(mCanvasWidth + mZoom + 1, mCanvasHeight + mZoom + 1, 1,
new PaletteData(new RGB[] {
new RGB(0, 0, 0)
}));
imageData.transparentPixel = 0;

// Draw the grid.
                        mGrid = new Image(Display.getDefault(), imageData);
                        GC gc = new GC(mGrid);
gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                        for (int x = 0; x <= mCanvasWidth + mZoom; x += mZoom) {
                            gc.drawLine(x, 0, x, mCanvasHeight + mZoom);
}
                        for (int y = 0; y <= mCanvasHeight + mZoom; y += mZoom) {
                            gc.drawLine(0, y, mCanvasWidth + mZoom, y);
}
gc.dispose();
}

                    e.gc.setClipping(new Rectangle(zoomedX, zoomedY, mWidth * mZoom + 1, mHeight
                            * mZoom + 1));
e.gc.setAlpha(76);
                    e.gc.drawImage(mGrid, (mCanvasWidth / 2 - mZoom / 2) % mZoom - mZoom,
                            (mCanvasHeight / 2 - mZoom / 2) % mZoom - mZoom);
e.gc.setAlpha(255);

                    e.gc.setForeground(mCrosshairColor);
                    e.gc.drawLine(0, mCanvasHeight / 2, mCanvasWidth - 1, mCanvasHeight / 2);
                    e.gc.drawLine(mCanvasWidth / 2, 0, mCanvasWidth / 2, mCanvasHeight - 1);
}
}
}
//Synthetic comment -- @@ -285,13 +284,13 @@
}

private void loadImage() {
        mImage = mModel.getImage();
        if (mImage != null) {
            mWidth = mImage.getBounds().width;
            mHeight = mImage.getBounds().height;
} else {
            mWidth = 0;
            mHeight = 0;
}
}

//Synthetic comment -- @@ -301,10 +300,10 @@
public void run() {
synchronized (this) {
loadImage();
                    mCrosshairLocation = mModel.getCrosshairLocation();
                    mZoom = mModel.getZoom();
                    mOverlayImage = mModel.getOverlayImage();
                    mOverlayTransparency = mModel.getOverlayTransparency();
}
}
});
//Synthetic comment -- @@ -324,7 +323,7 @@

public void crosshairMoved() {
synchronized (this) {
            mCrosshairLocation = mModel.getCrosshairLocation();
}
doRedraw();
}
//Synthetic comment -- @@ -341,14 +340,14 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    if (mGrid != null) {
// To notify that the zoom level has changed, we get rid
// of the
// grid.
                        mGrid.dispose();
                        mGrid = null;
}
                    mZoom = mModel.getZoom();
}
}
});
//Synthetic comment -- @@ -357,15 +356,15 @@

public void overlayChanged() {
synchronized (this) {
            mOverlayImage = mModel.getOverlayImage();
            mOverlayTransparency = mModel.getOverlayTransparency();
}
doRedraw();
}

public void overlayTransparencyChanged() {
synchronized (this) {
            mOverlayTransparency = mModel.getOverlayTransparency();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectPixelPanel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectPixelPanel.java
//Synthetic comment -- index afe3dc8..6680523 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.hierarchyviewerlib.ui;

import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -32,14 +32,14 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectPixelPanel extends Canvas implements IImageChangeListener {
    private PixelPerfectModel mModel;

    private Image mImage;

    private Image mOverlayImage;

    private Point mCrosshairLocation;

public static final int PREFERRED_WIDTH = 180;

//Synthetic comment -- @@ -47,11 +47,11 @@

public PixelPerfectPixelPanel(Composite parent) {
super(parent, SWT.NONE);
        mModel = PixelPerfectModel.getModel();
        mModel.addImageChangeListener(this);

        addPaintListener(mPaintListener);
        addDisposeListener(mDisposeListener);

imageLoaded();
}
//Synthetic comment -- @@ -63,21 +63,21 @@
return new Point(width, height);
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeImageChangeListener(PixelPerfectPixelPanel.this);
}
};

    private PaintListener mPaintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (PixelPerfectPixelPanel.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                if (mImage != null) {
RGB pixel =
                            mImage.getImageData().palette.getRGB(mImage.getImageData().getPixel(
                                    mCrosshairLocation.x, mCrosshairLocation.y));
Color rgbColor = new Color(Display.getDefault(), pixel);
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
e.gc.setBackground(rgbColor);
//Synthetic comment -- @@ -97,19 +97,19 @@
e.gc.drawText(Integer.toString(pixel.blue), 97, 35, true);
e.gc.drawText("X:", 132, 4, true);
e.gc.drawText("Y:", 132, 20, true);
                    e.gc.drawText(Integer.toString(mCrosshairLocation.x) + " px", 149, 4, true);
                    e.gc.drawText(Integer.toString(mCrosshairLocation.y) + " px", 149, 20, true);

                    if (mOverlayImage != null) {
                        int xInOverlay = mCrosshairLocation.x;
int yInOverlay =
                                mCrosshairLocation.y
                                        - (mImage.getBounds().height - mOverlayImage.getBounds().height);
if (xInOverlay >= 0 && yInOverlay >= 0
                                && xInOverlay < mOverlayImage.getBounds().width
                                && yInOverlay < mOverlayImage.getBounds().height) {
pixel =
                                    mOverlayImage.getImageData().palette.getRGB(mOverlayImage
.getImageData().getPixel(xInOverlay, yInOverlay));
rgbColor = new Color(Display.getDefault(), pixel);
e.gc
//Synthetic comment -- @@ -146,30 +146,30 @@

public void crosshairMoved() {
synchronized (this) {
            mCrosshairLocation = mModel.getCrosshairLocation();
}
doRedraw();
}

public void imageChanged() {
synchronized (this) {
            mImage = mModel.getImage();
}
doRedraw();
}

public void imageLoaded() {
synchronized (this) {
            mImage = mModel.getImage();
            mCrosshairLocation = mModel.getCrosshairLocation();
            mOverlayImage = mModel.getOverlayImage();
}
doRedraw();
}

public void overlayChanged() {
synchronized (this) {
            mOverlayImage = mModel.getOverlayImage();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectTree.java
//Synthetic comment -- index d34dcf2..da7cd62 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.IImageChangeListener;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
//Synthetic comment -- @@ -40,17 +40,17 @@

import java.util.List;

public class PixelPerfectTree extends Composite implements IImageChangeListener, SelectionListener {

    private TreeViewer mTreeViewer;

    private Tree mTree;

    private PixelPerfectModel mModel;

    private Image mFolderImage;

    private Image mFileImage;

private class ContentProvider implements ITreeContentProvider, ILabelProvider {
public Object[] getChildren(Object element) {
//Synthetic comment -- @@ -99,9 +99,9 @@
public Image getImage(Object element) {
if (element instanceof ViewNode) {
if (hasChildren(element)) {
                    return mFolderImage;
}
                return mFileImage;
}
return null;
}
//Synthetic comment -- @@ -130,47 +130,47 @@
public PixelPerfectTree(Composite parent) {
super(parent, SWT.NONE);
setLayout(new FillLayout());
        mTreeViewer = new TreeViewer(this, SWT.SINGLE);
        mTreeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

        mTree = mTreeViewer.getTree();
        mTree.addSelectionListener(this);

loadResources();

        addDisposeListener(mDisposeListener);

        mModel = PixelPerfectModel.getModel();
ContentProvider contentProvider = new ContentProvider();
        mTreeViewer.setContentProvider(contentProvider);
        mTreeViewer.setLabelProvider(contentProvider);
        mTreeViewer.setInput(mModel);
        mModel.addImageChangeListener(this);

}

private void loadResources() {
ImageLoader loader = ImageLoader.getDdmUiLibLoader();
        mFileImage = loader.loadImage("file.png", Display.getDefault());
        mFolderImage = loader.loadImage("folder.png", Display.getDefault());
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeImageChangeListener(PixelPerfectTree.this);
}
};

@Override
public boolean setFocus() {
        return mTree.setFocus();
}

public void imageLoaded() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mTreeViewer.refresh();
                mTreeViewer.expandAll();
}
});
}
//Synthetic comment -- @@ -197,10 +197,10 @@

public void widgetSelected(SelectionEvent e) {
// To combat phantom selection...
        if (((TreeSelection) mTreeViewer.getSelection()).isEmpty()) {
            mModel.setSelected(null);
} else {
            mModel.setSelected((ViewNode) e.item.getData());
}
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java
//Synthetic comment -- index 14068a3..4396a1f 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.device.ViewNode.Property;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.TreeColumnResizer;

//Synthetic comment -- @@ -45,25 +45,25 @@

import java.util.ArrayList;

public class PropertyViewer extends Composite implements ITreeChangeListener {
    private TreeViewModel mModel;

    private TreeViewer mTreeViewer;

    private Tree mTree;

    private DrawableViewNode mSelectedNode;

    private Font mSmallFont;

private class ContentProvider implements ITreeContentProvider, ITableLabelProvider {

public Object[] getChildren(Object parentElement) {
synchronized (PropertyViewer.this) {
                if (mSelectedNode != null && parentElement instanceof String) {
String category = (String) parentElement;
ArrayList<Property> returnValue = new ArrayList<Property>();
                    for (Property property : mSelectedNode.viewNode.properties) {
if (category.equals(ViewNode.MISCELLANIOUS)) {
if (property.name.indexOf(':') == -1) {
returnValue.add(property);
//Synthetic comment -- @@ -82,8 +82,8 @@

public Object getParent(Object element) {
synchronized (PropertyViewer.this) {
                if (mSelectedNode != null && element instanceof Property) {
                    if (mSelectedNode.viewNode.categories.size() == 0) {
return null;
}
String name = ((Property) element).name;
//Synthetic comment -- @@ -99,9 +99,9 @@

public boolean hasChildren(Object element) {
synchronized (PropertyViewer.this) {
                if (mSelectedNode != null && element instanceof String) {
String category = (String) element;
                    for (String name : mSelectedNode.viewNode.namedProperties.keySet()) {
if (category.equals(ViewNode.MISCELLANIOUS)) {
if (name.indexOf(':') == -1) {
return true;
//Synthetic comment -- @@ -119,13 +119,13 @@

public Object[] getElements(Object inputElement) {
synchronized (PropertyViewer.this) {
                if (mSelectedNode != null && inputElement instanceof TreeViewModel) {
                    if (mSelectedNode.viewNode.categories.size() == 0) {
                        return mSelectedNode.viewNode.properties
                                .toArray(new Property[mSelectedNode.viewNode.properties.size()]);
} else {
                        return mSelectedNode.viewNode.categories
                                .toArray(new String[mSelectedNode.viewNode.categories.size()]);
}
}
return new Object[0];
//Synthetic comment -- @@ -146,7 +146,7 @@

public String getColumnText(Object element, int column) {
synchronized (PropertyViewer.this) {
                if (mSelectedNode != null) {
if (element instanceof String && column == 0) {
String category = (String) element;
return Character.toUpperCase(category.charAt(0)) + category.substring(1);
//Synthetic comment -- @@ -184,32 +184,32 @@
public PropertyViewer(Composite parent) {
super(parent, SWT.NONE);
setLayout(new FillLayout());
        mTreeViewer = new TreeViewer(this, SWT.NONE);

        mTree = mTreeViewer.getTree();
        mTree.setLinesVisible(true);
        mTree.setHeaderVisible(true);

        TreeColumn propertyColumn = new TreeColumn(mTree, SWT.NONE);
propertyColumn.setText("Property");
        TreeColumn valueColumn = new TreeColumn(mTree, SWT.NONE);
valueColumn.setText("Value");

        mModel = TreeViewModel.getModel();
ContentProvider contentProvider = new ContentProvider();
        mTreeViewer.setContentProvider(contentProvider);
        mTreeViewer.setLabelProvider(contentProvider);
        mTreeViewer.setInput(mModel);
        mModel.addTreeChangeListener(this);

loadResources();
        addDisposeListener(mDisposeListener);

        mTree.setFont(mSmallFont);

new TreeColumnResizer(this, propertyColumn, valueColumn);

        addControlListener(mControlListener);

treeChanged();
}
//Synthetic comment -- @@ -222,20 +222,20 @@
for (int i = 0; i < fontData.length; i++) {
newFontData[i] = new FontData(fontData[i].getName(), 8, fontData[i].getStyle());
}
        mSmallFont = new Font(Display.getDefault(), newFontData);
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeTreeChangeListener(PropertyViewer.this);
            mSmallFont.dispose();
}
};

// If the window gets too small, hide the data, otherwise SWT throws an
// ERROR.

    private ControlListener mControlListener = new ControlAdapter() {
private boolean noInput = false;

private boolean noHeader = false;
//Synthetic comment -- @@ -243,17 +243,17 @@
@Override
public void controlResized(ControlEvent e) {
if (getBounds().height <= 20) {
                mTree.setHeaderVisible(false);
noHeader = true;
} else if (noHeader) {
                mTree.setHeaderVisible(true);
noHeader = false;
}
if (getBounds().height <= 38) {
                mTreeViewer.setInput(null);
noInput = true;
} else if (noInput) {
                mTreeViewer.setInput(mModel);
noInput = false;
}
}
//Synthetic comment -- @@ -261,14 +261,14 @@

public void selectionChanged() {
synchronized (this) {
            mSelectedNode = mModel.getSelection();
}
doRefresh();
}

public void treeChanged() {
synchronized (this) {
            mSelectedNode = mModel.getSelection();
}
doRefresh();
}
//Synthetic comment -- @@ -284,7 +284,7 @@
private void doRefresh() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mTreeViewer.refresh();
}
});
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index ea53ee4..3b90629 100644

//Synthetic comment -- @@ -16,12 +16,11 @@

package com.android.hierarchyviewerlib.ui;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.ViewNode.ProfileRating;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Rectangle;
//Synthetic comment -- @@ -53,67 +52,67 @@

import java.text.DecimalFormat;

public class TreeView extends Canvas implements ITreeChangeListener {

    private TreeViewModel mModel;

    private DrawableViewNode mTree;

    private DrawableViewNode mSelectedNode;

    private Rectangle mViewport;

    private Transform mTransform;

    private Transform mInverse;

    private double mZoom;

    private Point mLastPoint;

    private boolean mAlreadySelectedOnMouseDown;

    private boolean mDoubleClicked;

    private boolean mNodeMoved;

    private DrawableViewNode mDraggedNode;

public static final int LINE_PADDING = 10;

public static final float BEZIER_FRACTION = 0.35f;

    private static Image sRedImage;

    private static Image sYellowImage;

    private static Image sGreenImage;

    private static Image sNotSelectedImage;

    private static Image sSelectedImage;

    private static Image sFilteredImage;

    private static Image sFilteredSelectedImage;

    private static Font sSystemFont;

    private Color mBoxColor;

    private Color mTextBackgroundColor;

    private Rectangle mSelectedRectangleLocation;

    private Point mButtonCenter;

private static final int BUTTON_SIZE = 13;

    private Image mScaledSelectedImage;

    private boolean mButtonClicked;

    private DrawableViewNode mLastDrawnSelectedViewNode;

// The profile-image box needs to be moved to,
// so add some dragging leeway.
//Synthetic comment -- @@ -149,87 +148,87 @@
public TreeView(Composite parent) {
super(parent, SWT.NONE);

        mModel = TreeViewModel.getModel();
        mModel.addTreeChangeListener(this);

        addPaintListener(mPaintListener);
        addMouseListener(mMouseListener);
        addMouseMoveListener(mMouseMoveListener);
        addMouseWheelListener(mMouseWheelListener);
        addListener(SWT.Resize, mResizeListener);
        addDisposeListener(mDisposeListener);
        addKeyListener(mKeyListener);

loadResources();

        mTransform = new Transform(Display.getDefault());
        mInverse = new Transform(Display.getDefault());

loadAllData();
}

private void loadResources() {
ImageLoader loader = ImageLoader.getLoader(this.getClass());
        sRedImage = loader.loadImage("red.png", Display.getDefault()); //$NON-NLS-1$
        sYellowImage = loader.loadImage("yellow.png", Display.getDefault()); //$NON-NLS-1$
        sGreenImage = loader.loadImage("green.png", Display.getDefault()); //$NON-NLS-1$
        sNotSelectedImage = loader.loadImage("not-selected.png", Display.getDefault()); //$NON-NLS-1$
        sSelectedImage = loader.loadImage("selected.png", Display.getDefault()); //$NON-NLS-1$
        sFilteredImage = loader.loadImage("filtered.png", Display.getDefault()); //$NON-NLS-1$
        sFilteredSelectedImage = loader.loadImage("selected-filtered.png", Display.getDefault()); //$NON-NLS-1$
        mBoxColor = new Color(Display.getDefault(), new RGB(225, 225, 225));
        mTextBackgroundColor = new Color(Display.getDefault(), new RGB(82, 82, 82));
        if (mScaledSelectedImage != null) {
            mScaledSelectedImage.dispose();
}
        sSystemFont = Display.getDefault().getSystemFont();
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeTreeChangeListener(TreeView.this);
            mTransform.dispose();
            mInverse.dispose();
            mBoxColor.dispose();
            mTextBackgroundColor.dispose();
            if (mTree != null) {
                mModel.setViewport(null);
}
}
};

    private Listener mResizeListener = new Listener() {
public void handleEvent(Event e) {
synchronized (TreeView.this) {
                if (mTree != null && mViewport != null) {

// Keep the center in the same place.
Point viewCenter =
                            new Point(mViewport.x + mViewport.width / 2, mViewport.y + mViewport.height
/ 2);
                    mViewport.width = getBounds().width / mZoom;
                    mViewport.height = getBounds().height / mZoom;
                    mViewport.x = viewCenter.x - mViewport.width / 2;
                    mViewport.y = viewCenter.y - mViewport.height / 2;
}
}
            if (mViewport != null) {
                mModel.setViewport(mViewport);
}
}
};

    private KeyListener mKeyListener = new KeyListener() {

public void keyPressed(KeyEvent e) {
boolean selectionChanged = false;
DrawableViewNode clickedNode = null;
synchronized (TreeView.this) {
                if (mTree != null && mViewport != null && mSelectedNode != null) {
switch (e.keyCode) {
case SWT.ARROW_LEFT:
                            if (mSelectedNode.parent != null) {
                                mSelectedNode = mSelectedNode.parent;
selectionChanged = true;
}
break;
//Synthetic comment -- @@ -238,7 +237,7 @@
// On up and down, it is cool to go up and down only
// the leaf nodes.
// It goes well with the layout viewer
                            DrawableViewNode currentNode = mSelectedNode;
while (currentNode.parent != null && currentNode.viewNode.index == 0) {
currentNode = currentNode.parent;
}
//Synthetic comment -- @@ -254,11 +253,11 @@
}
}
if (selectionChanged) {
                                mSelectedNode = currentNode;
}
break;
case SWT.ARROW_DOWN:
                            currentNode = mSelectedNode;
while (currentNode.parent != null
&& currentNode.viewNode.index + 1 == currentNode.parent.children
.size()) {
//Synthetic comment -- @@ -274,45 +273,45 @@
}
}
if (selectionChanged) {
                                mSelectedNode = currentNode;
}
break;
case SWT.ARROW_RIGHT:
DrawableViewNode rightNode = null;
double mostOverlap = 0;
                            final int N = mSelectedNode.children.size();

// We consider all the children and pick the one
// who's tree overlaps the most.
for (int i = 0; i < N; i++) {
                                DrawableViewNode child = mSelectedNode.children.get(i);
DrawableViewNode topMostChild = child;
while (topMostChild.children.size() != 0) {
topMostChild = topMostChild.children.get(0);
}
double overlap =
Math.min(DrawableViewNode.NODE_HEIGHT, Math.min(
                                                mSelectedNode.top + DrawableViewNode.NODE_HEIGHT
- topMostChild.top, topMostChild.top
                                                        + child.treeHeight - mSelectedNode.top));
if (overlap > mostOverlap) {
mostOverlap = overlap;
rightNode = child;
}
}
if (rightNode != null) {
                                mSelectedNode = rightNode;
selectionChanged = true;
}
break;
case SWT.CR:
                            clickedNode = mSelectedNode;
break;
}
}
}
if (selectionChanged) {
                mModel.setSelection(mSelectedNode);
}
if (clickedNode != null) {
HierarchyViewerDirector.getDirector().showCapture(getShell(), clickedNode.viewNode);
//Synthetic comment -- @@ -323,71 +322,72 @@
}
};

    private MouseListener mMouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
DrawableViewNode clickedNode = null;
synchronized (TreeView.this) {
                if (mTree != null && mViewport != null) {
Point pt = transformPoint(e.x, e.y);
                    clickedNode = mTree.getSelected(pt.x, pt.y);
}
}
if (clickedNode != null) {
HierarchyViewerDirector.getDirector().showCapture(getShell(), clickedNode.viewNode);
                mDoubleClicked = true;
}
}

public void mouseDown(MouseEvent e) {
boolean selectionChanged = false;
synchronized (TreeView.this) {
                if (mTree != null && mViewport != null) {
Point pt = transformPoint(e.x, e.y);

// Ignore profiling rectangle, except for...
                    if (mSelectedRectangleLocation != null
                            && pt.x >= mSelectedRectangleLocation.x
                            && pt.x < mSelectedRectangleLocation.x
                                    + mSelectedRectangleLocation.width
                            && pt.y >= mSelectedRectangleLocation.y
                            && pt.y < mSelectedRectangleLocation.y
                                    + mSelectedRectangleLocation.height) {

// the small button!
                        if ((pt.x - mButtonCenter.x) * (pt.x - mButtonCenter.x)
                                + (pt.y - mButtonCenter.y) * (pt.y - mButtonCenter.y) <= (BUTTON_SIZE * BUTTON_SIZE) / 4) {
                            mButtonClicked = true;
doRedraw();
}
return;
}
                    mDraggedNode = mTree.getSelected(pt.x, pt.y);

// Update the selection.
                    if (mDraggedNode != null && mDraggedNode != mSelectedNode) {
                        mSelectedNode = mDraggedNode;
selectionChanged = true;
                        mAlreadySelectedOnMouseDown = false;
                    } else if (mDraggedNode != null) {
                        mAlreadySelectedOnMouseDown = true;
}

// Can't drag the root.
                    if (mDraggedNode == mTree) {
                        mDraggedNode = null;
}

                    if (mDraggedNode != null) {
                        mLastPoint = pt;
} else {
                        mLastPoint = new Point(e.x, e.y);
}
                    mNodeMoved = false;
                    mDoubleClicked = false;
}
}
if (selectionChanged) {
                mModel.setSelection(mSelectedNode);
}
}

//Synthetic comment -- @@ -397,8 +397,8 @@
boolean viewportChanged = false;
boolean selectionChanged = false;
synchronized (TreeView.this) {
                if (mTree != null && mViewport != null && mLastPoint != null) {
                    if (mDraggedNode == null) {
// The viewport moves.
handleMouseDrag(new Point(e.x, e.y));
viewportChanged = true;
//Synthetic comment -- @@ -412,36 +412,36 @@
// double click event.
// During a double click, we don't want to deselect.
Point pt = transformPoint(e.x, e.y);
                    DrawableViewNode mouseUpOn = mTree.getSelected(pt.x, pt.y);
                    if (mouseUpOn != null && mouseUpOn == mSelectedNode
                            && mAlreadySelectedOnMouseDown && !mNodeMoved && !mDoubleClicked) {
                        mSelectedNode = null;
selectionChanged = true;
}
                    mLastPoint = null;
                    mDraggedNode = null;
redraw = true;
}

// Just clicked the button here.
                if (mButtonClicked) {
HierarchyViewerDirector.getDirector().showCapture(getShell(),
                            mSelectedNode.viewNode);
                    mButtonClicked = false;
redrawButton = true;
}
}

// Complicated.
if (viewportChanged) {
                mModel.setViewport(mViewport);
} else if (redraw) {
                mModel.removeTreeChangeListener(TreeView.this);
                mModel.notifyViewportChanged();
if (selectionChanged) {
                    mModel.setSelection(mSelectedNode);
}
                mModel.addTreeChangeListener(TreeView.this);
doRedraw();
} else if (redrawButton) {
doRedraw();
//Synthetic comment -- @@ -450,13 +450,13 @@

};

    private MouseMoveListener mMouseMoveListener = new MouseMoveListener() {
public void mouseMove(MouseEvent e) {
boolean redraw = false;
boolean viewportChanged = false;
synchronized (TreeView.this) {
                if (mTree != null && mViewport != null && mLastPoint != null) {
                    if (mDraggedNode == null) {
handleMouseDrag(new Point(e.x, e.y));
viewportChanged = true;
} else {
//Synthetic comment -- @@ -466,11 +466,11 @@
}
}
if (viewportChanged) {
                mModel.setViewport(mViewport);
} else if (redraw) {
                mModel.removeTreeChangeListener(TreeView.this);
                mModel.notifyViewportChanged();
                mModel.addTreeChangeListener(TreeView.this);
doRedraw();
}
}
//Synthetic comment -- @@ -479,102 +479,102 @@
private void handleMouseDrag(Point pt) {

// Case 1: a node is dragged. DrawableViewNode knows how to handle this.
        if (mDraggedNode != null) {
            if (mLastPoint.y - pt.y != 0) {
                mNodeMoved = true;
}
            mDraggedNode.move(mLastPoint.y - pt.y);
            mLastPoint = pt;
return;
}

// Case 2: the viewport is dragged. We have to make sure we respect the
// bounds - don't let the user drag way out... + some leeway for the
// profiling box.
        double xDif = (mLastPoint.x - pt.x) / mZoom;
        double yDif = (mLastPoint.y - pt.y) / mZoom;

        double treeX = mTree.bounds.x - DRAG_LEEWAY;
        double treeY = mTree.bounds.y - DRAG_LEEWAY;
        double treeWidth = mTree.bounds.width + 2 * DRAG_LEEWAY;
        double treeHeight = mTree.bounds.height + 2 * DRAG_LEEWAY;

        if (mViewport.width > treeWidth) {
            if (xDif < 0 && mViewport.x + mViewport.width > treeX + treeWidth) {
                mViewport.x = Math.max(mViewport.x + xDif, treeX + treeWidth - mViewport.width);
            } else if (xDif > 0 && mViewport.x < treeX) {
                mViewport.x = Math.min(mViewport.x + xDif, treeX);
}
} else {
            if (xDif < 0 && mViewport.x > treeX) {
                mViewport.x = Math.max(mViewport.x + xDif, treeX);
            } else if (xDif > 0 && mViewport.x + mViewport.width < treeX + treeWidth) {
                mViewport.x = Math.min(mViewport.x + xDif, treeX + treeWidth - mViewport.width);
}
}
        if (mViewport.height > treeHeight) {
            if (yDif < 0 && mViewport.y + mViewport.height > treeY + treeHeight) {
                mViewport.y = Math.max(mViewport.y + yDif, treeY + treeHeight - mViewport.height);
            } else if (yDif > 0 && mViewport.y < treeY) {
                mViewport.y = Math.min(mViewport.y + yDif, treeY);
}
} else {
            if (yDif < 0 && mViewport.y > treeY) {
                mViewport.y = Math.max(mViewport.y + yDif, treeY);
            } else if (yDif > 0 && mViewport.y + mViewport.height < treeY + treeHeight) {
                mViewport.y = Math.min(mViewport.y + yDif, treeY + treeHeight - mViewport.height);
}
}
        mLastPoint = pt;
}

private Point transformPoint(double x, double y) {
float[] pt = {
(float) x, (float) y
};
        mInverse.transform(pt);
return new Point(pt[0], pt[1]);
}

    private MouseWheelListener mMouseWheelListener = new MouseWheelListener() {
public void mouseScrolled(MouseEvent e) {
Point zoomPoint = null;
synchronized (TreeView.this) {
                if (mTree != null && mViewport != null) {
                    mZoom += Math.ceil(e.count / 3.0) * 0.1;
zoomPoint = transformPoint(e.x, e.y);
}
}
if (zoomPoint != null) {
                mModel.zoomOnPoint(mZoom, zoomPoint);
}
}
};

    private PaintListener mPaintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (TreeView.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                if (mTree != null && mViewport != null) {

// Easy stuff!
                    e.gc.setTransform(mTransform);
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
Path connectionPath = new Path(Display.getDefault());
                    paintRecursive(e.gc, mTransform, mTree, mSelectedNode, connectionPath);
e.gc.drawPath(connectionPath);
connectionPath.dispose();

// Draw the profiling box.
                    if (mSelectedNode != null) {

e.gc.setAlpha(200);

// Draw the little triangle
                        int x = mSelectedNode.left + DrawableViewNode.NODE_WIDTH / 2;
                        int y = (int) mSelectedNode.top + 4;
                        e.gc.setBackground(mBoxColor);
e.gc.fillPolygon(new int[] {
x, y, x - 11, y - 11, x + 11, y - 11
});
//Synthetic comment -- @@ -583,22 +583,22 @@
y -= 10 + RECT_HEIGHT;
e.gc.fillRoundRectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT, 30,
30);
                        mSelectedRectangleLocation =
new Rectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT);

e.gc.setAlpha(255);

// Draw the button
                        mButtonCenter =
new Point(x - BUTTON_RIGHT_OFFSET + (RECT_WIDTH - BUTTON_SIZE) / 2,
y + BUTTON_TOP_OFFSET + BUTTON_SIZE / 2);

                        if (mButtonClicked) {
e.gc
.setBackground(Display.getDefault().getSystemColor(
SWT.COLOR_BLACK));
} else {
                            e.gc.setBackground(mTextBackgroundColor);

}
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//Synthetic comment -- @@ -614,36 +614,36 @@
y += 15;

// If there is an image, draw it.
                        if (mSelectedNode.viewNode.image != null
                                && mSelectedNode.viewNode.image.getBounds().height != 1
                                && mSelectedNode.viewNode.image.getBounds().width != 1) {

// Scaling the image to the right size takes lots of
// time, so we want to do it only once.

// If the selection changed, get rid of the old
// image.
                            if (mLastDrawnSelectedViewNode != mSelectedNode) {
                                if (mScaledSelectedImage != null) {
                                    mScaledSelectedImage.dispose();
                                    mScaledSelectedImage = null;
}
                                mLastDrawnSelectedViewNode = mSelectedNode;
}

                            if (mScaledSelectedImage == null) {
double ratio =
                                        1.0 * mSelectedNode.viewNode.image.getBounds().width
                                                / mSelectedNode.viewNode.image.getBounds().height;
int newWidth, newHeight;
if (ratio > 1.0 * IMAGE_WIDTH / IMAGE_HEIGHT) {
newWidth =
                                            Math.min(IMAGE_WIDTH, mSelectedNode.viewNode.image
.getBounds().width);
newHeight = (int) (newWidth / ratio);
} else {
newHeight =
                                            Math.min(IMAGE_HEIGHT, mSelectedNode.viewNode.image
.getBounds().height);
newWidth = (int) (newHeight * ratio);
}
//Synthetic comment -- @@ -653,33 +653,34 @@
// resolution under zoom.
newWidth = Math.max(newWidth * 2, 1);
newHeight = Math.max(newHeight * 2, 1);
                                mScaledSelectedImage =
new Image(Display.getDefault(), newWidth, newHeight);
                                GC gc = new GC(mScaledSelectedImage);
                                gc.setBackground(mTextBackgroundColor);
gc.fillRectangle(0, 0, newWidth, newHeight);
                                gc.drawImage(mSelectedNode.viewNode.image, 0, 0,
                                        mSelectedNode.viewNode.image.getBounds().width,
                                        mSelectedNode.viewNode.image.getBounds().height, 0, 0,
newWidth, newHeight);
gc.dispose();
}

// Draw the background rectangle
                            e.gc.setBackground(mTextBackgroundColor);
                            e.gc.fillRoundRectangle(x - mScaledSelectedImage.getBounds().width / 4
- IMAGE_OFFSET, y
                                    + (IMAGE_HEIGHT - mScaledSelectedImage.getBounds().height / 2)
                                    / 2 - IMAGE_OFFSET, mScaledSelectedImage.getBounds().width / 2
                                    + 2 * IMAGE_OFFSET, mScaledSelectedImage.getBounds().height / 2
+ 2 * IMAGE_OFFSET, IMAGE_ROUNDING, IMAGE_ROUNDING);

// Under max zoom, we want the image to be
// untransformed. So, get back to the identity
// transform.
                            int imageX = x - mScaledSelectedImage.getBounds().width / 4;
int imageY =
                                    y
                                            + (IMAGE_HEIGHT - mScaledSelectedImage.getBounds().height / 2)
/ 2;

Transform untransformedTransform = new Transform(Display.getDefault());
//Synthetic comment -- @@ -687,15 +688,15 @@
float[] pt = new float[] {
imageX, imageY
};
                            mTransform.transform(pt);
                            e.gc.drawImage(mScaledSelectedImage, 0, 0, mScaledSelectedImage
                                    .getBounds().width, mScaledSelectedImage.getBounds().height,
                                    (int) pt[0], (int) pt[1], (int) (mScaledSelectedImage
.getBounds().width
                                            * mZoom / 2),
                                    (int) (mScaledSelectedImage.getBounds().height * mZoom / 2));
untransformedTransform.dispose();
                            e.gc.setTransform(mTransform);
}

// Text stuff
//Synthetic comment -- @@ -706,24 +707,24 @@
e.gc.setFont(font);

String text =
                                mSelectedNode.viewNode.viewCount + " view"
                                        + (mSelectedNode.viewNode.viewCount != 1 ? "s" : "");
DecimalFormat formatter = new DecimalFormat("0.000");

String measureText =
"Measure: "
                                        + (mSelectedNode.viewNode.measureTime != -1 ? formatter
                                                .format(mSelectedNode.viewNode.measureTime)
+ " ms" : "n/a");
String layoutText =
"Layout: "
                                        + (mSelectedNode.viewNode.layoutTime != -1 ? formatter
                                                .format(mSelectedNode.viewNode.layoutTime)
+ " ms" : "n/a");
String drawText =
"Draw: "
                                        + (mSelectedNode.viewNode.drawTime != -1 ? formatter
                                                .format(mSelectedNode.viewNode.drawTime)
+ " ms" : "n/a");

org.eclipse.swt.graphics.Point titleExtent = e.gc.stringExtent(text);
//Synthetic comment -- @@ -740,7 +741,7 @@
+ layoutExtent.y + TEXT_SPACING + drawExtent.y + 2
* TEXT_TOP_OFFSET;

                        e.gc.setBackground(mTextBackgroundColor);
e.gc.fillRoundRectangle(x - boxWidth / 2, y, boxWidth, boxHeight,
TEXT_ROUNDING, TEXT_ROUNDING);

//Synthetic comment -- @@ -767,8 +768,8 @@

font.dispose();
} else {
                        mSelectedRectangleLocation = null;
                        mButtonCenter = null;
}
}
}
//Synthetic comment -- @@ -778,13 +779,13 @@
private static void paintRecursive(GC gc, Transform transform, DrawableViewNode node,
DrawableViewNode selectedNode, Path connectionPath) {
if (selectedNode == node && node.viewNode.filtered) {
            gc.drawImage(sFilteredSelectedImage, node.left, (int) Math.round(node.top));
} else if (selectedNode == node) {
            gc.drawImage(sSelectedImage, node.left, (int) Math.round(node.top));
} else if (node.viewNode.filtered) {
            gc.drawImage(sFilteredImage, node.left, (int) Math.round(node.top));
} else {
            gc.drawImage(sNotSelectedImage, node.left, (int) Math.round(node.top));
}

int fontHeight = gc.getFontMetrics().getHeight();
//Synthetic comment -- @@ -816,44 +817,44 @@
y =
node.top + DrawableViewNode.NODE_HEIGHT
- DrawableViewNode.CONTENT_TOP_BOTTOM_PADDING
                            - sRedImage.getBounds().height;
x +=
                    (contentWidth - (sRedImage.getBounds().width * 3 + 2 * DrawableViewNode.CONTENT_INTER_PADDING)) / 2;
switch (node.viewNode.measureRating) {
case GREEN:
                    gc.drawImage(sGreenImage, (int) x, (int) y);
break;
case YELLOW:
                    gc.drawImage(sYellowImage, (int) x, (int) y);
break;
case RED:
                    gc.drawImage(sRedImage, (int) x, (int) y);
break;
}

            x += sRedImage.getBounds().width + DrawableViewNode.CONTENT_INTER_PADDING;
switch (node.viewNode.layoutRating) {
case GREEN:
                    gc.drawImage(sGreenImage, (int) x, (int) y);
break;
case YELLOW:
                    gc.drawImage(sYellowImage, (int) x, (int) y);
break;
case RED:
                    gc.drawImage(sRedImage, (int) x, (int) y);
break;
}

            x += sRedImage.getBounds().width + DrawableViewNode.CONTENT_INTER_PADDING;
switch (node.viewNode.drawRating) {
case GREEN:
                    gc.drawImage(sGreenImage, (int) x, (int) y);
break;
case YELLOW:
                    gc.drawImage(sYellowImage, (int) x, (int) y);
break;
case RED:
                    gc.drawImage(sRedImage, (int) x, (int) y);
break;
}
}
//Synthetic comment -- @@ -953,7 +954,7 @@
}

private static Font getFont(int size, boolean bold) {
        FontData[] fontData = sSystemFont.getFontData();
for (int i = 0; i < fontData.length; i++) {
fontData[i].setHeight(size);
if (bold) {
//Synthetic comment -- @@ -972,17 +973,17 @@
}

public void loadAllData() {
        boolean newViewport = mViewport == null;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    mTree = mModel.getTree();
                    mSelectedNode = mModel.getSelection();
                    mViewport = mModel.getViewport();
                    mZoom = mModel.getZoom();
                    if (mTree != null && mViewport == null) {
                        mViewport =
                                new Rectangle(0, mTree.top + DrawableViewNode.NODE_HEIGHT / 2
- getBounds().height / 2, getBounds().width,
getBounds().height);
} else {
//Synthetic comment -- @@ -992,7 +993,7 @@
}
});
if (newViewport) {
            mModel.setViewport(mViewport);
}
}

//Synthetic comment -- @@ -1002,37 +1003,37 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    mTree = mModel.getTree();
                    mSelectedNode = mModel.getSelection();
                    if (mTree == null) {
                        mViewport = null;
} else {
                        mViewport =
                                new Rectangle(0, mTree.top + DrawableViewNode.NODE_HEIGHT / 2
- getBounds().height / 2, getBounds().width,
getBounds().height);
}
}
}
});
        if (mViewport != null) {
            mModel.setViewport(mViewport);
} else {
doRedraw();
}
}

private void setTransform() {
        if (mViewport != null && mTree != null) {
// Set the transform.
            mTransform.identity();
            mInverse.identity();

            mTransform.scale((float) mZoom, (float) mZoom);
            mInverse.scale((float) mZoom, (float) mZoom);
            mTransform.translate((float) -mViewport.x, (float) -mViewport.y);
            mInverse.translate((float) -mViewport.x, (float) -mViewport.y);
            mInverse.invert();
}
}

//Synthetic comment -- @@ -1041,8 +1042,8 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    mViewport = mModel.getViewport();
                    mZoom = mModel.getZoom();
setTransform();
}
}
//Synthetic comment -- @@ -1056,10 +1057,10 @@

public void selectionChanged() {
synchronized (this) {
            mSelectedNode = mModel.getSelection();
            if (mSelectedNode != null && mSelectedNode.viewNode.image == null) {
HierarchyViewerDirector.getDirector()
                        .loadCaptureInBackground(mSelectedNode.viewNode);
}
}
doRedraw();








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewControls.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewControls.java
//Synthetic comment -- index 08117b5..5c794e4 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -35,11 +35,11 @@
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

public class TreeViewControls extends Composite implements ITreeChangeListener {

    private Text mFilterText;

    private Slider mZoomSlider;

public TreeViewControls(Composite parent) {
super(parent, SWT.NONE);
//Synthetic comment -- @@ -52,44 +52,44 @@
filterLabel.setText("Filter by class or id:");
filterLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));

        mFilterText = new Text(this, SWT.LEFT | SWT.SINGLE);
        mFilterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mFilterText.addModifyListener(mFilterTextModifyListener);
        mFilterText.setText(HierarchyViewerDirector.getDirector().getFilterText());

Label smallZoomLabel = new Label(this, SWT.NONE);
smallZoomLabel.setText(" 20%");
smallZoomLabel
.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));

        mZoomSlider = new Slider(this, SWT.HORIZONTAL);
GridData zoomSliderGridData = new GridData(GridData.CENTER, GridData.CENTER, false, false);
zoomSliderGridData.widthHint = 190;
        mZoomSlider.setLayoutData(zoomSliderGridData);
        mZoomSlider.setMinimum((int) (TreeViewModel.MIN_ZOOM * 10));
        mZoomSlider.setMaximum((int) (TreeViewModel.MAX_ZOOM * 10 + 1));
        mZoomSlider.setThumb(1);
        mZoomSlider.setSelection((int) Math.round(TreeViewModel.getModel().getZoom() * 10));

        mZoomSlider.addSelectionListener(mZoomSliderSelectionListener);

Label largeZoomLabel = new Label(this, SWT.NONE);
largeZoomLabel
.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
largeZoomLabel.setText("200%");

        addDisposeListener(mDisposeListener);

TreeViewModel.getModel().addTreeChangeListener(this);
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
TreeViewModel.getModel().removeTreeChangeListener(TreeViewControls.this);
}
};

    private SelectionListener mZoomSliderSelectionListener = new SelectionListener() {
private int oldValue;

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -97,7 +97,7 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = mZoomSlider.getSelection();
if (oldValue != newValue) {
TreeViewModel.getModel().removeTreeChangeListener(TreeViewControls.this);
TreeViewModel.getModel().setZoom(newValue / 10.0);
//Synthetic comment -- @@ -107,9 +107,9 @@
}
};

    private ModifyListener mFilterTextModifyListener = new ModifyListener() {
public void modifyText(ModifyEvent e) {
            HierarchyViewerDirector.getDirector().filterNodes(mFilterText.getText());
}
};

//Synthetic comment -- @@ -121,10 +121,10 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
if (TreeViewModel.getModel().getTree() != null) {
                    mZoomSlider.setSelection((int) Math
.round(TreeViewModel.getModel().getZoom() * 10));
}
                mFilterText.setText(""); //$NON-NLS-1$
}
});
}
//Synthetic comment -- @@ -136,7 +136,7 @@
public void zoomChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                mZoomSlider.setSelection((int) Math.round(TreeViewModel.getModel().getZoom() * 10));
}
});
};








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java
//Synthetic comment -- index fb01b86..34167dd 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Rectangle;
//Synthetic comment -- @@ -41,72 +41,72 @@
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TreeViewOverview extends Canvas implements ITreeChangeListener {

    private TreeViewModel mModel;

    private DrawableViewNode mTree;

    private Rectangle mViewport;

    private Transform mTransform;

    private Transform mInverse;

    private Rectangle mBounds = new Rectangle();

    private double mScale;

    private boolean mDragging = false;

    private DrawableViewNode mSelectedNode;

    private static Image sNotSelectedImage;

    private static Image sSelectedImage;

    private static Image sFilteredImage;

    private static Image sFilteredSelectedImage;

public TreeViewOverview(Composite parent) {
super(parent, SWT.NONE);

        mModel = TreeViewModel.getModel();
        mModel.addTreeChangeListener(this);

loadResources();

        addPaintListener(mPaintListener);
        addMouseListener(mMouseListener);
        addMouseMoveListener(mMouseMoveListener);
        addListener(SWT.Resize, mResizeListener);
        addDisposeListener(mDisposeListener);

        mTransform = new Transform(Display.getDefault());
        mInverse = new Transform(Display.getDefault());

loadAllData();
}

private void loadResources() {
ImageLoader loader = ImageLoader.getLoader(this.getClass());
        sNotSelectedImage = loader.loadImage("not-selected.png", Display.getDefault()); //$NON-NLS-1$
        sSelectedImage = loader.loadImage("selected-small.png", Display.getDefault()); //$NON-NLS-1$
        sFilteredImage = loader.loadImage("filtered.png", Display.getDefault()); //$NON-NLS-1$
        sFilteredSelectedImage =
                loader.loadImage("selected-filtered-small.png", Display.getDefault()); //$NON-NLS-1$
}

    private DisposeListener mDisposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            mModel.removeTreeChangeListener(TreeViewOverview.this);
            mTransform.dispose();
            mInverse.dispose();
}
};

    private MouseListener mMouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
// pass
//Synthetic comment -- @@ -115,16 +115,16 @@
public void mouseDown(MouseEvent e) {
boolean redraw = false;
synchronized (TreeViewOverview.this) {
                if (mTree != null && mViewport != null) {
                    mDragging = true;
redraw = true;
handleMouseEvent(transformPoint(e.x, e.y));
}
}
if (redraw) {
                mModel.removeTreeChangeListener(TreeViewOverview.this);
                mModel.setViewport(mViewport);
                mModel.addTreeChangeListener(TreeViewOverview.this);
doRedraw();
}
}
//Synthetic comment -- @@ -132,8 +132,8 @@
public void mouseUp(MouseEvent e) {
boolean redraw = false;
synchronized (TreeViewOverview.this) {
                if (mTree != null && mViewport != null) {
                    mDragging = false;
redraw = true;
handleMouseEvent(transformPoint(e.x, e.y));

//Synthetic comment -- @@ -145,47 +145,47 @@
}
}
if (redraw) {
                mModel.removeTreeChangeListener(TreeViewOverview.this);
                mModel.setViewport(mViewport);
                mModel.addTreeChangeListener(TreeViewOverview.this);
doRedraw();
}
}

};

    private MouseMoveListener mMouseMoveListener = new MouseMoveListener() {
public void mouseMove(MouseEvent e) {
boolean moved = false;
synchronized (TreeViewOverview.this) {
                if (mDragging) {
moved = true;
handleMouseEvent(transformPoint(e.x, e.y));
}
}
if (moved) {
                mModel.removeTreeChangeListener(TreeViewOverview.this);
                mModel.setViewport(mViewport);
                mModel.addTreeChangeListener(TreeViewOverview.this);
doRedraw();
}
}
};

private void handleMouseEvent(Point pt) {
        mViewport.x = pt.x - mViewport.width / 2;
        mViewport.y = pt.y - mViewport.height / 2;
        if (mViewport.x < mBounds.x) {
            mViewport.x = mBounds.x;
}
        if (mViewport.y < mBounds.y) {
            mViewport.y = mBounds.y;
}
        if (mViewport.x + mViewport.width > mBounds.x + mBounds.width) {
            mViewport.x = mBounds.x + mBounds.width - mViewport.width;
}
        if (mViewport.y + mViewport.height > mBounds.y + mBounds.height) {
            mViewport.y = mBounds.y + mBounds.height - mViewport.height;
}
}

//Synthetic comment -- @@ -193,11 +193,11 @@
float[] pt = {
(float) x, (float) y
};
        mInverse.transform(pt);
return new Point(pt[0], pt[1]);
}

    private Listener mResizeListener = new Listener() {
public void handleEvent(Event arg0) {
synchronized (TreeViewOverview.this) {
setTransform();
//Synthetic comment -- @@ -206,33 +206,33 @@
}
};

    private PaintListener mPaintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (TreeViewOverview.this) {
                if (mTree != null) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                    e.gc.setTransform(mTransform);
                    e.gc.setLineWidth((int) Math.ceil(0.7 / mScale));
Path connectionPath = new Path(Display.getDefault());
                    paintRecursive(e.gc, mTree, connectionPath);
e.gc.drawPath(connectionPath);
connectionPath.dispose();

                    if (mViewport != null) {
e.gc.setAlpha(50);
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                        e.gc.fillRectangle((int) mViewport.x, (int) mViewport.y, (int) Math
                                .ceil(mViewport.width), (int) Math.ceil(mViewport.height));

e.gc.setAlpha(255);
e.gc
.setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_DARK_GRAY));
                        e.gc.setLineWidth((int) Math.ceil(2 / mScale));
                        e.gc.drawRectangle((int) mViewport.x, (int) mViewport.y, (int) Math
                                .ceil(mViewport.width), (int) Math.ceil(mViewport.height));
}
}
}
//Synthetic comment -- @@ -240,14 +240,14 @@
};

private void paintRecursive(GC gc, DrawableViewNode node, Path connectionPath) {
        if (mSelectedNode == node && node.viewNode.filtered) {
            gc.drawImage(sFilteredSelectedImage, node.left, (int) Math.round(node.top));
        } else if (mSelectedNode == node) {
            gc.drawImage(sSelectedImage, node.left, (int) Math.round(node.top));
} else if (node.viewNode.filtered) {
            gc.drawImage(sFilteredImage, node.left, (int) Math.round(node.top));
} else {
            gc.drawImage(sNotSelectedImage, node.left, (int) Math.round(node.top));
}
int N = node.children.size();
if (N == 0) {
//Synthetic comment -- @@ -284,9 +284,9 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    mTree = mModel.getTree();
                    mSelectedNode = mModel.getSelection();
                    mViewport = mModel.getViewport();
setBounds();
setTransform();
}
//Synthetic comment -- @@ -299,9 +299,9 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    mTree = mModel.getTree();
                    mSelectedNode = mModel.getSelection();
                    mViewport = mModel.getViewport();
setBounds();
setTransform();
}
//Synthetic comment -- @@ -311,48 +311,48 @@
}

private void setBounds() {
        if (mViewport != null && mTree != null) {
            mBounds.x = Math.min(mViewport.x, mTree.bounds.x);
            mBounds.y = Math.min(mViewport.y, mTree.bounds.y);
            mBounds.width =
                    Math.max(mViewport.x + mViewport.width, mTree.bounds.x + mTree.bounds.width)
                            - mBounds.x;
            mBounds.height =
                    Math.max(mViewport.y + mViewport.height, mTree.bounds.y + mTree.bounds.height)
                            - mBounds.y;
        } else if (mTree != null) {
            mBounds.x = mTree.bounds.x;
            mBounds.y = mTree.bounds.y;
            mBounds.width = mTree.bounds.x + mTree.bounds.width - mBounds.x;
            mBounds.height = mTree.bounds.y + mTree.bounds.height - mBounds.y;
}
}

private void setTransform() {
        if (mTree != null) {

            mTransform.identity();
            mInverse.identity();
final Point size = new Point();
size.x = getBounds().width;
size.y = getBounds().height;
            if (mBounds.width == 0 || mBounds.height == 0 || size.x == 0 || size.y == 0) {
                mScale = 1;
} else {
                mScale = Math.min(size.x / mBounds.width, size.y / mBounds.height);
}
            mTransform.scale((float) mScale, (float) mScale);
            mInverse.scale((float) mScale, (float) mScale);
            mTransform.translate((float) -mBounds.x, (float) -mBounds.y);
            mInverse.translate((float) -mBounds.x, (float) -mBounds.y);
            if (size.x / mBounds.width < size.y / mBounds.height) {
                mTransform.translate(0, (float) (size.y / mScale - mBounds.height) / 2);
                mInverse.translate(0, (float) (size.y / mScale - mBounds.height) / 2);
} else {
                mTransform.translate((float) (size.x / mScale - mBounds.width) / 2, 0);
                mInverse.translate((float) (size.x / mScale - mBounds.width) / 2, 0);
}
            mInverse.invert();
}
}

//Synthetic comment -- @@ -360,7 +360,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    mViewport = mModel.getViewport();
setBounds();
setTransform();
}
//Synthetic comment -- @@ -375,7 +375,7 @@

public void selectionChanged() {
synchronized (this) {
            mSelectedNode = mModel.getSelection();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java
//Synthetic comment -- index 7570676..b196aaf 100644

//Synthetic comment -- @@ -86,7 +86,7 @@

@Override
public String toString() {
            return "{" + x + ", " + y + ", " + width + ", " + height + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
}

}
//Synthetic comment -- @@ -104,7 +104,7 @@

@Override
public String toString() {
            return "(" + x + ", " + y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/PsdFile.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/PsdFile.java
//Synthetic comment -- index 275ea36..2c1154b 100644

//Synthetic comment -- @@ -160,7 +160,7 @@

static final short MODE_LAB = 9;

        final byte[] mSignature = "8BPS".getBytes(); //$NON-NLS-1$

final short mVersion = 1;

//Synthetic comment -- @@ -214,7 +214,7 @@

int mLength = 0;

        final byte[] mSignature = "8BIM".getBytes(); //$NON-NLS-1$

final short mResourceId = RESOURCE_RESOLUTION_INFO;

//Synthetic comment -- @@ -342,9 +342,9 @@

final Channel[] mChannelInfo = new Channel[mChannelCount];

        final byte[] mBlendSignature = "8BIM".getBytes(); //$NON-NLS-1$

        final byte[] mBlendMode = "norm".getBytes(); //$NON-NLS-1$

final byte mOpacity = OPACITY_OPAQUE;

//Synthetic comment -- @@ -362,9 +362,9 @@

final byte[] mName;

        final byte[] mLayerExtraSignature = "8BIM".getBytes(); //$NON-NLS-1$

        final byte[] mLayerExtraKey = "luni".getBytes(); //$NON-NLS-1$

int mLayerExtraLength;

//Synthetic comment -- @@ -391,7 +391,7 @@
byte[] data = name.getBytes();

try {
                mLayerExtraLength = 4 + mOriginalName.getBytes("UTF-16").length; //$NON-NLS-1$
} catch (UnsupportedEncodingException e) {
e.printStackTrace();
}
//Synthetic comment -- @@ -465,7 +465,7 @@
out.write(mLayerExtraKey);
out.writeInt(mLayerExtraLength);
out.writeInt(mOriginalName.length() + 1);
            out.write(mOriginalName.getBytes("UTF-16")); //$NON-NLS-1$
}

void writeImageData(DataOutputStream out) throws IOException {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/TreeColumnResizer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/TreeColumnResizer.java
//Synthetic comment -- index 6b20366..e03394a 100644

//Synthetic comment -- @@ -24,24 +24,24 @@

public class TreeColumnResizer {

    private TreeColumn mColumn1;

    private TreeColumn mColumn2;

    private Composite mControl;

    private int mColumn1Width;

    private int mColumn2Width;

private final static int MIN_COLUMN1_WIDTH = 18;

private final static int MIN_COLUMN2_WIDTH = 3;

public TreeColumnResizer(Composite control, TreeColumn column1, TreeColumn column2) {
        this.mControl = control;
        this.mColumn1 = column1;
        this.mColumn2 = column2;
control.addListener(SWT.Resize, resizeListener);
column1.addListener(SWT.Resize, column1ResizeListener);
column2.setResizable(false);
//Synthetic comment -- @@ -49,64 +49,64 @@

private Listener resizeListener = new Listener() {
public void handleEvent(Event e) {
            if (mColumn1Width == 0 && mColumn2Width == 0) {
                mColumn1Width = (mControl.getBounds().width - 18) / 2;
                mColumn2Width = (mControl.getBounds().width - 18) / 2;
} else {
                int dif = mControl.getBounds().width - 18 - (mColumn1Width + mColumn2Width);
                int columnDif = Math.abs(mColumn1Width - mColumn2Width);
int mainColumnChange = Math.min(Math.abs(dif), columnDif);
int left = Math.max(0, Math.abs(dif) - columnDif);
if (dif < 0) {
                    if (mColumn1Width > mColumn2Width) {
                        mColumn1Width -= mainColumnChange;
} else {
                        mColumn2Width -= mainColumnChange;
}
                    mColumn1Width -= left / 2;
                    mColumn2Width -= left - left / 2;
} else {
                    if (mColumn1Width > mColumn2Width) {
                        mColumn2Width += mainColumnChange;
} else {
                        mColumn1Width += mainColumnChange;
}
                    mColumn1Width += left / 2;
                    mColumn2Width += left - left / 2;
}
}
            mColumn1.removeListener(SWT.Resize, column1ResizeListener);
            mColumn1.setWidth(mColumn1Width);
            mColumn2.setWidth(mColumn2Width);
            mColumn1.addListener(SWT.Resize, column1ResizeListener);
}
};

private Listener column1ResizeListener = new Listener() {
public void handleEvent(Event e) {
            int widthDif = mColumn1Width - mColumn1.getWidth();
            mColumn1Width -= widthDif;
            mColumn2Width += widthDif;
boolean column1Changed = false;

// Strange, but these constants make the columns look the same.

            if (mColumn1Width < MIN_COLUMN1_WIDTH) {
                mColumn2Width -= MIN_COLUMN1_WIDTH - mColumn1Width;
                mColumn1Width += MIN_COLUMN1_WIDTH - mColumn1Width;
column1Changed = true;
}
            if (mColumn2Width < MIN_COLUMN2_WIDTH) {
                mColumn1Width += mColumn2Width - MIN_COLUMN2_WIDTH;
                mColumn2Width = MIN_COLUMN2_WIDTH;
column1Changed = true;
}
if (column1Changed) {
                mColumn1.removeListener(SWT.Resize, this);
                mColumn1.setWidth(mColumn1Width);
                mColumn1.addListener(SWT.Resize, this);
}
            mColumn2.setWidth(mColumn2Width);
}
};
}







