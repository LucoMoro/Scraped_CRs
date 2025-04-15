/*Renaming and adding  //$NON-NLS-?$

Change-Id:I9f5fa1625af4b35499cfc87996d0b3a39841ba31*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java
//Synthetic comment -- index 8996cdb..8f94c65 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.hierarchyviewer;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Log;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;
//Synthetic comment -- @@ -40,14 +39,14 @@
*/
public class HierarchyViewerPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.android.ide.eclipse.hierarchyviewer";

    public static final String ADB_LOCATION = PLUGIN_ID + ".adb";

// The shared instance
    private static HierarchyViewerPlugin plugin;

    private Color redColor;

/**
* The constructor
//Synthetic comment -- @@ -58,18 +57,18 @@
@Override
public void start(BundleContext context) throws Exception {
super.start(context);
        plugin = this;


// set the consoles.
        final MessageConsole messageConsole = new MessageConsole("Hierarchy Viewer", null); // $NON-NLS-1$
ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] {
messageConsole
});

final MessageConsoleStream consoleStream = messageConsole.newMessageStream();
final MessageConsoleStream errorConsoleStream = messageConsole.newMessageStream();
        redColor = new Color(Display.getDefault(), 0xFF, 0x00, 0x00);

// because this can be run, in some cases, by a non UI thread, and
// because
//Synthetic comment -- @@ -78,7 +77,7 @@
// in the UI thread.
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                errorConsoleStream.setColor(redColor);
}
});

//Synthetic comment -- @@ -131,10 +130,10 @@
*/
@Override
public void stop(BundleContext context) throws Exception {
        plugin = null;
super.stop(context);

        redColor.dispose();

HierarchyViewerDirector director = HierarchyViewerDirector.getDirector();
director.stopListenForDevices();
//Synthetic comment -- @@ -148,7 +147,7 @@
* @return the shared instance
*/
public static HierarchyViewerPlugin getPlugin() {
        return plugin;
}

/**
//Synthetic comment -- @@ -160,7 +159,7 @@
public static void setAdb(String adb, boolean startAdb) {
if (adb != null) {
// store the location for future ddms only start.
            plugin.getPreferenceStore().setValue(ADB_LOCATION, adb);

// starts the server in a thread in case this is blocking.
if (startAdb) {
//Synthetic comment -- @@ -201,9 +200,9 @@
Calendar c = Calendar.getInstance();

if (tag == null) {
            return String.format("[%1$tF %1$tT]", c);
}

        return String.format("[%1$tF %1$tT - %2$s]", c, tag);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPluginDirector.java
//Synthetic comment -- index f1374ae..7104784 100644

//Synthetic comment -- @@ -48,11 +48,11 @@
}
};
job.setPriority(Job.SHORT);
        job.setRule(schedulingRule);
job.schedule();
}

    private ISchedulingRule schedulingRule = new ISchedulingRule() {
public boolean contains(ISchedulingRule rule) {
return rule == this;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/PixelPerfectPespective.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/PixelPerfectPespective.java
//Synthetic comment -- index bcaf8f9..def2864 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
public class PixelPerfectPespective implements IPerspectiveFactory {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.PixelPerfectPespective";

public void createInitialLayout(IPageLayout layout) {
layout.setEditorAreaVisible(false);
//Synthetic comment -- @@ -37,14 +37,14 @@
String editorArea = layout.getEditorArea();
IFolderLayout folder;

        folder = layout.createFolder("tree", IPageLayout.LEFT, 0.25f, editorArea);
folder.addView(DeviceSelectorView.ID);
folder.addView(PixelPerfectTreeView.ID);

        folder = layout.createFolder("overview", IPageLayout.RIGHT, 0.4f, editorArea);
folder.addView(PixelPerfectView.ID);

        folder = layout.createFolder("main", IPageLayout.RIGHT, 0.35f, editorArea);
folder.addView(PixelPerfectLoupeView.ID);


//Synthetic comment -- @@ -53,7 +53,7 @@
layout.addShowViewShortcut(PixelPerfectLoupeView.ID);
layout.addShowViewShortcut(PixelPerfectView.ID);

        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");
layout.addPerspectiveShortcut(TreeViewPerspective.ID);
layout.addPerspectiveShortcut(Perspective.ID);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/TreeViewPerspective.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/TreeViewPerspective.java
//Synthetic comment -- index 7022509..09fca66 100644

//Synthetic comment -- @@ -29,7 +29,7 @@

public class TreeViewPerspective implements IPerspectiveFactory {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.TreeViewPerspective";

public void createInitialLayout(IPageLayout layout) {
layout.setEditorAreaVisible(false);
//Synthetic comment -- @@ -37,18 +37,18 @@
String editorArea = layout.getEditorArea();
IFolderLayout folder;

        folder = layout.createFolder("properties", IPageLayout.LEFT, 0.10f, editorArea);
folder.addView(DeviceSelectorView.ID);
folder.addView(PropertyView.ID);

        folder = layout.createFolder("main", IPageLayout.RIGHT, 0.24f, "properties");
folder.addView(TreeViewView.ID);

        folder = layout.createFolder("panel-top", IPageLayout.RIGHT, 0.7f, "main");
folder.addView(TreeOverviewView.ID);


        folder = layout.createFolder("panel-bottom", IPageLayout.BOTTOM, 0.5f, "panel-top");
folder.addView(LayoutView.ID);

layout.addShowViewShortcut(DeviceSelectorView.ID);
//Synthetic comment -- @@ -57,7 +57,7 @@
layout.addShowViewShortcut(LayoutView.ID);
layout.addShowViewShortcut(TreeViewView.ID);

        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");
layout.addPerspectiveShortcut(PixelPerfectPespective.ID);
layout.addPerspectiveShortcut(Perspective.ID);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/DeviceSelectorView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/DeviceSelectorView.java
//Synthetic comment -- index c884115..e2fac78 100644

//Synthetic comment -- @@ -36,9 +36,9 @@
public class DeviceSelectorView extends ViewPart implements IPerspectiveListener {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.DeviceSelectorView";

    private DeviceSelector deviceSelector;

@Override
public void createPartControl(Composite parent) {
//Synthetic comment -- @@ -53,7 +53,7 @@
} else if (perspective.getId().equals(TreeViewPerspective.ID)) {
doPixelPerfectStuff = false;
}
        deviceSelector = new DeviceSelector(parent, doTreeViewStuff, doPixelPerfectStuff);

placeActions(doTreeViewStuff, doPixelPerfectStuff);

//Synthetic comment -- @@ -93,18 +93,18 @@

@Override
public void setFocus() {
        deviceSelector.setFocus();
}

public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
if (perspective.getId().equals(PixelPerfectPespective.ID)) {
            deviceSelector.setMode(false, true);
placeActions(false, true);
} else if (perspective.getId().equals(TreeViewPerspective.ID)) {
            deviceSelector.setMode(true, false);
placeActions(true, false);
} else {
            deviceSelector.setMode(true, true);
placeActions(true, true);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/LayoutView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/LayoutView.java
//Synthetic comment -- index be401b6..2fb51a3 100644

//Synthetic comment -- @@ -36,68 +36,68 @@

public class LayoutView extends ViewPart implements TreeChangeListener {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.LayoutView";

    private LayoutViewer layoutViewer;

    private Image onBlack;

    private Image onWhite;

    private Action showExtrasAction = new Action("Show &Extras", Action.AS_CHECK_BOX) {
@Override
public void run() {
            layoutViewer.setShowExtras(isChecked());
}
};

    private Action loadAllViewsAction = new Action("Load All &Views") {
@Override
public void run() {
HierarchyViewerDirector.getDirector().loadAllViews();
            showExtrasAction.setChecked(true);
            layoutViewer.setShowExtras(true);
}
};

    private Action onBlackWhiteAction = new Action("Change Background &Color") {
@Override
public void run() {
            boolean newValue = !layoutViewer.getOnBlack();
            layoutViewer.setOnBlack(newValue);
if (newValue) {
                setImageDescriptor(ImageDescriptor.createFromImage(onWhite));
} else {
                setImageDescriptor(ImageDescriptor.createFromImage(onBlack));
}
}
};

@Override
public void createPartControl(Composite parent) {
        showExtrasAction.setAccelerator(SWT.MOD1 + 'E');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("show-extras.png", Display.getDefault());
        showExtrasAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        showExtrasAction.setToolTipText("Show images");
        showExtrasAction.setEnabled(TreeViewModel.getModel().getTree() != null);

        onWhite = imageLoader.loadImage("on-white.png", Display.getDefault());
        onBlack = imageLoader.loadImage("on-black.png", Display.getDefault());

        onBlackWhiteAction.setAccelerator(SWT.MOD1 + 'C');
        onBlackWhiteAction.setImageDescriptor(ImageDescriptor.createFromImage(onWhite));
        onBlackWhiteAction.setToolTipText("Change layout viewer background color");

        loadAllViewsAction.setAccelerator(SWT.MOD1 + 'V');
        image = imageLoader.loadImage("load-all-views.png", Display.getDefault());
        loadAllViewsAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        loadAllViewsAction.setToolTipText("Load all view images");
        loadAllViewsAction.setEnabled(TreeViewModel.getModel().getTree() != null);

parent.setLayout(new FillLayout());

        layoutViewer = new LayoutViewer(parent);

placeActions();

//Synthetic comment -- @@ -109,15 +109,15 @@

IMenuManager mm = actionBars.getMenuManager();
mm.removeAll();
        mm.add(onBlackWhiteAction);
        mm.add(showExtrasAction);
        mm.add(loadAllViewsAction);

IToolBarManager tm = actionBars.getToolBarManager();
tm.removeAll();
        tm.add(onBlackWhiteAction);
        tm.add(showExtrasAction);
        tm.add(loadAllViewsAction);
}

@Override
//Synthetic comment -- @@ -128,7 +128,7 @@

@Override
public void setFocus() {
        layoutViewer.setFocus();
}

public void selectionChanged() {
//Synthetic comment -- @@ -138,8 +138,8 @@
public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                loadAllViewsAction.setEnabled(TreeViewModel.getModel().getTree() != null);
                showExtrasAction.setEnabled(TreeViewModel.getModel().getTree() != null);
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectLoupeView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectLoupeView.java
//Synthetic comment -- index 05777c4..1ec1a6f 100644

//Synthetic comment -- @@ -41,24 +41,24 @@
public class PixelPerfectLoupeView extends ViewPart implements ImageChangeListener {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectLoupeView";

    private PixelPerfectLoupe pixelPerfectLoupe;

    private Action showInLoupeAction = new Action("&Show Overlay", Action.AS_CHECK_BOX) {
@Override
public void run() {
            pixelPerfectLoupe.setShowOverlay(isChecked());
}
};
@Override
public void createPartControl(Composite parent) {
        showInLoupeAction.setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("show-overlay.png", Display.getDefault());
        showInLoupeAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        showInLoupeAction.setToolTipText("Show the overlay in the loupe view");
        showInLoupeAction.setEnabled(PixelPerfectModel.getModel().getOverlayImage() != null);
PixelPerfectModel.getModel().addImageChangeListener(this);

GridLayout loupeLayout = new GridLayout();
//Synthetic comment -- @@ -75,8 +75,8 @@
pixelPerfectLoupeBorderGridLayout.verticalSpacing = 0;
pixelPerfectLoupeBorder.setLayout(pixelPerfectLoupeBorderGridLayout);

        pixelPerfectLoupe = new PixelPerfectLoupe(pixelPerfectLoupeBorder);
        pixelPerfectLoupe.setLayoutData(new GridData(GridData.FILL_BOTH));

PixelPerfectPixelPanel pixelPerfectPixelPanel =
new PixelPerfectPixelPanel(pixelPerfectLoupeBorder);
//Synthetic comment -- @@ -95,12 +95,12 @@
IMenuManager mm = actionBars.getMenuManager();
mm.removeAll();
mm.add(PixelPerfectAutoRefreshAction.getAction());
        mm.add(showInLoupeAction);

IToolBarManager tm = actionBars.getToolBarManager();
tm.removeAll();
tm.add(PixelPerfectAutoRefreshAction.getAction());
        tm.add(showInLoupeAction);
}

@Override
//Synthetic comment -- @@ -111,7 +111,7 @@

@Override
public void setFocus() {
        pixelPerfectLoupe.setFocus();
}

public void crosshairMoved() {
//Synthetic comment -- @@ -130,7 +130,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
                showInLoupeAction.setEnabled(overlayImage != null);
}
});
}
//Synthetic comment -- @@ -138,7 +138,8 @@
public void overlayChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                showInLoupeAction.setEnabled(PixelPerfectModel.getModel().getOverlayImage() != null);
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectTreeView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectTreeView.java
//Synthetic comment -- index 811b8fc..f3591f7 100644

//Synthetic comment -- @@ -29,14 +29,14 @@
public class PixelPerfectTreeView extends ViewPart {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectTreeView";

    private PixelPerfectTree pixelPerfectTree;

@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());
        pixelPerfectTree = new PixelPerfectTree(parent);

placeActions();
}
//Synthetic comment -- @@ -55,7 +55,7 @@

@Override
public void setFocus() {
        pixelPerfectTree.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PixelPerfectView.java
//Synthetic comment -- index a09eb5c..943a6c8 100644

//Synthetic comment -- @@ -31,14 +31,14 @@
public class PixelPerfectView extends ViewPart {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.PixelPerfectView";

    private PixelPerfect pixelPerfect;

@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());
        pixelPerfect = new PixelPerfect(parent);

placeActions();
}
//Synthetic comment -- @@ -61,7 +61,7 @@

@Override
public void setFocus() {
        pixelPerfect.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PropertyView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/PropertyView.java
//Synthetic comment -- index bd6d462..74f50fd 100644

//Synthetic comment -- @@ -24,20 +24,20 @@

public class PropertyView extends ViewPart {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.PropertyView";

    private PropertyViewer propertyViewer;

@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());

        propertyViewer = new PropertyViewer(parent);
}

@Override
public void setFocus() {
        propertyViewer.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeOverviewView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeOverviewView.java
//Synthetic comment -- index 5363c1b..e18771b 100644

//Synthetic comment -- @@ -25,19 +25,19 @@
public class TreeOverviewView extends ViewPart {

public static final String ID =
            "com.android.ide.eclipse.hierarchyviewer.views.TreeOverviewView";

    private TreeViewOverview treeViewOverview;
@Override
public void createPartControl(Composite parent) {
parent.setLayout(new FillLayout());

        treeViewOverview = new TreeViewOverview(parent);
}

@Override
public void setFocus() {
        treeViewOverview.setFocus();
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeViewView.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/views/TreeViewView.java
//Synthetic comment -- index 76d9b54..f24f420 100644

//Synthetic comment -- @@ -39,9 +39,9 @@
// Awesome name.
public class TreeViewView extends ViewPart {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.TreeViewView";

    private TreeView treeView;

@Override
public void createPartControl(Composite parent) {
//Synthetic comment -- @@ -54,7 +54,7 @@
treeViewContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
treeViewContainer.setLayout(new FillLayout());

        treeView = new TreeView(treeViewContainer);

TreeViewControls treeViewControls = new TreeViewControls(parent);
treeViewControls.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -91,7 +91,7 @@

@Override
public void setFocus() {
        treeView.setFocus();
}

}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/AboutDialog.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/AboutDialog.java
//Synthetic comment -- index 54edbc8..3f973e7 100644

//Synthetic comment -- @@ -34,15 +34,15 @@
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends Dialog {
    private Image aboutImage;

    private Image smallImage;

public AboutDialog(Shell shell) {
super(shell);
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        smallImage = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault());
        aboutImage = imageLoader.loadImage("about.jpg", Display.getDefault());
}

@Override
//Synthetic comment -- @@ -58,14 +58,14 @@
imageControl.setLayout(new FillLayout());
imageControl.setLayoutData(new GridData(GridData.FILL_VERTICAL));
Label imageLabel = new Label(imageControl, SWT.CENTER);
        imageLabel.setImage(aboutImage);

CLabel textLabel = new CLabel(control, SWT.NONE);
textLabel
.setText("Hierarchy Viewer\nCopyright 2010, The Android Open Source Project\nAll Rights Reserved.");
textLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
getShell().setText("About...");
        getShell().setImage(smallImage);
return control;

}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index f5c6e98..df4f08f 100644

//Synthetic comment -- @@ -39,8 +39,8 @@
import com.android.hierarchyviewerlib.actions.SaveTreeViewAction;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.ui.DeviceSelector;
import com.android.hierarchyviewerlib.ui.LayoutViewer;
import com.android.hierarchyviewerlib.ui.PixelPerfect;
//Synthetic comment -- @@ -84,74 +84,74 @@

private static final int INITIAL_HEIGHT = 768;

    private static HierarchyViewerApplication APP;

// Images for moving between the 3 main windows.

    private Image deviceViewImage;

    private Image pixelPerfectImage;

    private Image treeViewImage;

    private Image deviceViewSelectedImage;

    private Image pixelPerfectSelectedImage;

    private Image treeViewSelectedImage;

// And their buttons

    private Button treeViewButton;

    private Button pixelPerfectButton;

    private Button deviceViewButton;

    private Label progressLabel;

    private ProgressBar progressBar;

    private String progressString;

    private Composite deviceSelectorPanel;

    private Composite treeViewPanel;

    private Composite pixelPerfectPanel;

    private StackLayout mainWindowStackLayout;

    private DeviceSelector deviceSelector;

    private Composite statusBar;

    private TreeView treeView;

    private Composite mainWindow;

    private Image onBlackImage;

    private Image onWhiteImage;

    private Button onBlackWhiteButton;

    private Button showExtras;

    private LayoutViewer layoutViewer;

    private PixelPerfectLoupe pixelPerfectLoupe;

    private Composite treeViewControls;

public static final HierarchyViewerApplication getApp() {
        return APP;
}

public HierarchyViewerApplication() {
super(null);

        APP = this;

addMenuBar();
}
//Synthetic comment -- @@ -161,7 +161,7 @@
super.configureShell(shell);
shell.setText("Hierarchy Viewer");
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault());
shell.setImage(image);
}

//Synthetic comment -- @@ -203,17 +203,17 @@

private void loadResources() {
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        treeViewImage = imageLoader.loadImage("tree-view.png", Display.getDefault());
        treeViewSelectedImage =
                imageLoader.loadImage("tree-view-selected.png", Display.getDefault());
        pixelPerfectImage = imageLoader.loadImage("pixel-perfect-view.png", Display.getDefault());
        pixelPerfectSelectedImage =
                imageLoader.loadImage("pixel-perfect-view-selected.png", Display.getDefault());
        deviceViewImage = imageLoader.loadImage("device-view.png", Display.getDefault());
        deviceViewSelectedImage =
                imageLoader.loadImage("device-view-selected.png", Display.getDefault());
        onBlackImage = imageLoader.loadImage("on-black.png", Display.getDefault());
        onWhiteImage = imageLoader.loadImage("on-white.png", Display.getDefault());
}

@Override
//Synthetic comment -- @@ -225,14 +225,14 @@
mainLayout.marginHeight = mainLayout.marginWidth = 0;
mainLayout.verticalSpacing = mainLayout.horizontalSpacing = 0;
control.setLayout(mainLayout);
        mainWindow = new Composite(control, SWT.NONE);
        mainWindow.setLayoutData(new GridData(GridData.FILL_BOTH));
        mainWindowStackLayout = new StackLayout();
        mainWindow.setLayout(mainWindowStackLayout);

        buildDeviceSelectorPanel(mainWindow);
        buildTreeViewPanel(mainWindow);
        buildPixelPerfectPanel(mainWindow);

buildStatusBar(control);

//Synthetic comment -- @@ -243,78 +243,78 @@


private void buildStatusBar(Composite parent) {
        statusBar = new Composite(parent, SWT.NONE);
        statusBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

FormLayout statusBarLayout = new FormLayout();
statusBarLayout.marginHeight = statusBarLayout.marginWidth = 2;

        statusBar.setLayout(statusBarLayout);

        deviceViewButton = new Button(statusBar, SWT.TOGGLE);
        deviceViewButton.setImage(deviceViewImage);
        deviceViewButton.setToolTipText("Switch to the window selection view");
        deviceViewButton.addSelectionListener(deviceViewButtonSelectionListener);
FormData deviceViewButtonFormData = new FormData();
deviceViewButtonFormData.left = new FormAttachment();
        deviceViewButton.setLayoutData(deviceViewButtonFormData);

        treeViewButton = new Button(statusBar, SWT.TOGGLE);
        treeViewButton.setImage(treeViewImage);
        treeViewButton.setEnabled(false);
        treeViewButton.setToolTipText("Switch to the tree view");
        treeViewButton.addSelectionListener(treeViewButtonSelectionListener);
FormData treeViewButtonFormData = new FormData();
        treeViewButtonFormData.left = new FormAttachment(deviceViewButton, 2);
        treeViewButton.setLayoutData(treeViewButtonFormData);

        pixelPerfectButton = new Button(statusBar, SWT.TOGGLE);
        pixelPerfectButton.setImage(pixelPerfectImage);
        pixelPerfectButton.setEnabled(false);
        pixelPerfectButton.setToolTipText("Switch to the pixel perfect view");
        pixelPerfectButton.addSelectionListener(pixelPerfectButtonSelectionListener);
FormData pixelPerfectButtonFormData = new FormData();
        pixelPerfectButtonFormData.left = new FormAttachment(treeViewButton, 2);
        pixelPerfectButton.setLayoutData(pixelPerfectButtonFormData);

// Tree View control panel...
        treeViewControls = new TreeViewControls(statusBar);
FormData treeViewControlsFormData = new FormData();
        treeViewControlsFormData.left = new FormAttachment(pixelPerfectButton, 2);
        treeViewControlsFormData.top = new FormAttachment(treeViewButton, 0, SWT.CENTER);
treeViewControlsFormData.width = 552;
        treeViewControls.setLayoutData(treeViewControlsFormData);

// Progress stuff
        progressLabel = new Label(statusBar, SWT.RIGHT);

        progressBar = new ProgressBar(statusBar, SWT.HORIZONTAL | SWT.INDETERMINATE | SWT.SMOOTH);
FormData progressBarFormData = new FormData();
progressBarFormData.right = new FormAttachment(100, 0);
        progressBarFormData.top = new FormAttachment(treeViewButton, 0, SWT.CENTER);
        progressBar.setLayoutData(progressBarFormData);

FormData progressLabelFormData = new FormData();
        progressLabelFormData.right = new FormAttachment(progressBar, -2);
        progressLabelFormData.top = new FormAttachment(treeViewButton, 0, SWT.CENTER);
        progressLabel.setLayoutData(progressLabelFormData);

        if (progressString == null) {
            progressLabel.setVisible(false);
            progressBar.setVisible(false);
} else {
            progressLabel.setText(progressString);
}
}

private void buildDeviceSelectorPanel(Composite parent) {
        deviceSelectorPanel = new Composite(parent, SWT.NONE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = gridLayout.marginHeight = 0;
gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        deviceSelectorPanel.setLayout(gridLayout);

        Composite buttonPanel = new Composite(deviceSelectorPanel, SWT.NONE);
buttonPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

GridLayout buttonLayout = new GridLayout();
//Synthetic comment -- @@ -341,20 +341,20 @@
new ActionButton(innerButtonPanel, InspectScreenshotAction.getAction());
inspectScreenshotButton.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite deviceSelectorContainer = new Composite(deviceSelectorPanel, SWT.BORDER);
deviceSelectorContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
deviceSelectorContainer.setLayout(new FillLayout());
        deviceSelector = new DeviceSelector(deviceSelectorContainer, true, true);
}

public void buildTreeViewPanel(Composite parent) {
        treeViewPanel = new Composite(parent, SWT.NONE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = gridLayout.marginHeight = 0;
gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        treeViewPanel.setLayout(gridLayout);

        Composite buttonPanel = new Composite(treeViewPanel, SWT.NONE);
buttonPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

GridLayout buttonLayout = new GridLayout();
//Synthetic comment -- @@ -392,11 +392,11 @@
new ActionButton(innerButtonPanel, RequestLayoutAction.getAction());
requestLayout.setLayoutData(new GridData(GridData.FILL_BOTH));

        SashForm mainSash = new SashForm(treeViewPanel, SWT.HORIZONTAL | SWT.SMOOTH);
mainSash.setLayoutData(new GridData(GridData.FILL_BOTH));
Composite treeViewContainer = new Composite(mainSash, SWT.BORDER);
treeViewContainer.setLayout(new FillLayout());
        treeView = new TreeView(treeViewContainer);

SashForm sideSash = new SashForm(mainSash, SWT.VERTICAL | SWT.SMOOTH);

//Synthetic comment -- @@ -436,15 +436,15 @@
rowLayout.center = true;
buttonBar.setLayout(rowLayout);

        onBlackWhiteButton = new Button(buttonBar, SWT.PUSH);
        onBlackWhiteButton.setImage(onWhiteImage);
        onBlackWhiteButton.addSelectionListener(onBlackWhiteSelectionListener);
        onBlackWhiteButton.setToolTipText("Change layout viewer background color");

        showExtras = new Button(buttonBar, SWT.CHECK);
        showExtras.setText("Show Extras");
        showExtras.addSelectionListener(showExtrasSelectionListener);
        showExtras.setToolTipText("Show images");

ActionButton loadAllViewsButton =
new ActionButton(fullButtonBar, LoadAllViewsAction.getAction());
//Synthetic comment -- @@ -454,7 +454,7 @@
Composite layoutViewerMainContainer = new Composite(layoutViewerContainer, SWT.BORDER);
layoutViewerMainContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
layoutViewerMainContainer.setLayout(new FillLayout());
        layoutViewer = new LayoutViewer(layoutViewerMainContainer);

sideSash.SASH_WIDTH = 4;
sideSash.setWeights(new int[] {
//Synthetic comment -- @@ -464,13 +464,13 @@
}

private void buildPixelPerfectPanel(Composite parent) {
        pixelPerfectPanel = new Composite(parent, SWT.NONE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = gridLayout.marginHeight = 0;
gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        pixelPerfectPanel.setLayout(gridLayout);

        Composite buttonPanel = new Composite(pixelPerfectPanel, SWT.NONE);
buttonPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

GridLayout buttonLayout = new GridLayout();
//Synthetic comment -- @@ -509,7 +509,7 @@
new ActionButton(innerButtonPanel, PixelPerfectAutoRefreshAction.getAction());
autoRefresh.setLayoutData(new GridData(GridData.FILL_BOTH));

        SashForm mainSash = new SashForm(pixelPerfectPanel, SWT.HORIZONTAL | SWT.SMOOTH);
mainSash.setLayoutData(new GridData(GridData.FILL_BOTH));
mainSash.SASH_WIDTH = 4;

//Synthetic comment -- @@ -532,8 +532,8 @@
pixelPerfectLoupeBorderGridLayout.verticalSpacing = 0;
pixelPerfectLoupeBorder.setLayout(pixelPerfectLoupeBorderGridLayout);

        pixelPerfectLoupe = new PixelPerfectLoupe(pixelPerfectLoupeBorder);
        pixelPerfectLoupe.setLayoutData(new GridData(GridData.FILL_BOTH));

PixelPerfectPixelPanel pixelPerfectPixelPanel =
new PixelPerfectPixelPanel(pixelPerfectLoupeBorder);
//Synthetic comment -- @@ -555,19 +555,19 @@
}

public void showOverlayInLoupe(boolean value) {
        pixelPerfectLoupe.setShowOverlay(value);
}

// Shows the progress notification...
public void startTask(final String taskName) {
        progressString = taskName;
Display.getDefault().syncExec(new Runnable() {
public void run() {
                if (progressLabel != null && progressBar != null) {
                    progressLabel.setText(taskName);
                    progressLabel.setVisible(true);
                    progressBar.setVisible(true);
                    statusBar.layout();
}
}
});
//Synthetic comment -- @@ -575,12 +575,12 @@

// And hides it!
public void endTask() {
        progressString = null;
Display.getDefault().syncExec(new Runnable() {
public void run() {
                if (progressLabel != null && progressBar != null) {
                    progressLabel.setVisible(false);
                    progressBar.setVisible(false);
}
}
});
//Synthetic comment -- @@ -610,22 +610,22 @@

mm.updateAll(true);

        deviceViewButton.setSelection(true);
        deviceViewButton.setImage(deviceViewSelectedImage);

        treeViewButton.setSelection(false);
        treeViewButton.setImage(treeViewImage);

        pixelPerfectButton.setSelection(false);
        pixelPerfectButton.setImage(pixelPerfectImage);

        mainWindowStackLayout.topControl = deviceSelectorPanel;

        mainWindow.layout();

        deviceSelector.setFocus();

        treeViewControls.setVisible(false);
}

public void showTreeView() {
//Synthetic comment -- @@ -657,22 +657,22 @@

mm.updateAll(true);

        deviceViewButton.setSelection(false);
        deviceViewButton.setImage(deviceViewImage);

        treeViewButton.setSelection(true);
        treeViewButton.setImage(treeViewSelectedImage);

        pixelPerfectButton.setSelection(false);
        pixelPerfectButton.setImage(pixelPerfectImage);

        mainWindowStackLayout.topControl = treeViewPanel;

        mainWindow.layout();

        treeView.setFocus();

        treeViewControls.setVisible(true);
}

public void showPixelPerfect() {
//Synthetic comment -- @@ -703,22 +703,22 @@

mm.updateAll(true);

        deviceViewButton.setSelection(false);
        deviceViewButton.setImage(deviceViewImage);

        treeViewButton.setSelection(false);
        treeViewButton.setImage(treeViewImage);

        pixelPerfectButton.setSelection(true);
        pixelPerfectButton.setImage(pixelPerfectSelectedImage);

        mainWindowStackLayout.topControl = pixelPerfectPanel;

        mainWindow.layout();

        pixelPerfectLoupe.setFocus();

        treeViewControls.setVisible(false);
}

private SelectionListener deviceViewButtonSelectionListener = new SelectionListener() {
//Synthetic comment -- @@ -727,7 +727,7 @@
}

public void widgetSelected(SelectionEvent e) {
            deviceViewButton.setSelection(true);
showDeviceSelector();
}
};
//Synthetic comment -- @@ -738,7 +738,7 @@
}

public void widgetSelected(SelectionEvent e) {
            treeViewButton.setSelection(true);
showTreeView();
}
};
//Synthetic comment -- @@ -749,7 +749,7 @@
}

public void widgetSelected(SelectionEvent e) {
            pixelPerfectButton.setSelection(true);
showPixelPerfect();
}
};
//Synthetic comment -- @@ -760,12 +760,12 @@
}

public void widgetSelected(SelectionEvent e) {
            if (layoutViewer.getOnBlack()) {
                layoutViewer.setOnBlack(false);
                onBlackWhiteButton.setImage(onBlackImage);
} else {
                layoutViewer.setOnBlack(true);
                onBlackWhiteButton.setImage(onWhiteImage);
}
}
};
//Synthetic comment -- @@ -776,7 +776,7 @@
}

public void widgetSelected(SelectionEvent e) {
            layoutViewer.setShowExtras(showExtras.getSelection());
}
};

//Synthetic comment -- @@ -786,12 +786,12 @@
}

public void widgetSelected(SelectionEvent e) {
            showExtras.setSelection(true);
showExtrasSelectionListener.widgetSelected(null);
}
};

    private TreeChangeListener treeChangeListener = new TreeChangeListener() {
public void selectionChanged() {
// pass
}
//Synthetic comment -- @@ -801,10 +801,10 @@
public void run() {
if (TreeViewModel.getModel().getTree() == null) {
showDeviceSelector();
                        treeViewButton.setEnabled(false);
} else {
showTreeView();
                        treeViewButton.setEnabled(true);
}
}
});
//Synthetic comment -- @@ -819,7 +819,7 @@
}
};

    private ImageChangeListener imageChangeListener = new ImageChangeListener() {

public void crosshairMoved() {
// pass
//Synthetic comment -- @@ -837,10 +837,10 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
if (PixelPerfectModel.getModel().getImage() == null) {
                        pixelPerfectButton.setEnabled(false);
showDeviceSelector();
} else {
                        pixelPerfectButton.setEnabled(true);
showPixelPerfect();
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index f26cc2c..23b6210 100644

//Synthetic comment -- @@ -28,16 +28,16 @@
*/
public class HierarchyViewerApplicationDirector extends HierarchyViewerDirector {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

public static HierarchyViewerDirector createDirector() {
        return director = new HierarchyViewerApplicationDirector();
}

@Override
public void terminate() {
super.terminate();
        executor.shutdown();
}

/*
//Synthetic comment -- @@ -46,7 +46,7 @@
*/
@Override
public String getAdbLocation() {
        String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir");
if (hvParentLocation != null && hvParentLocation.length() != 0) {
return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}
//Synthetic comment -- @@ -60,7 +60,7 @@
*/
@Override
public void executeInBackground(final String taskName, final Runnable task) {
        executor.execute(new Runnable() {
public void run() {
HierarchyViewerApplication.getApp().startTask(taskName);
task.run();








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/AboutAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/AboutAction.java
//Synthetic comment -- index c6abe57..0c7c7b2 100644

//Synthetic comment -- @@ -30,35 +30,35 @@

public class AboutAction extends Action implements ImageAction {

    private static AboutAction action;

    private Image image;

    private Shell shell;

private AboutAction(Shell shell) {
super("&About");
        this.shell = shell;
setAccelerator(SWT.MOD1 + 'A');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("about-small.jpg", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Shows the about dialog");
}

public static AboutAction getAction(Shell shell) {
        if (action == null) {
            action = new AboutAction(shell);
}
        return action;
}

@Override
public void run() {
        new AboutDialog(shell).open();
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/LoadAllViewsAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/LoadAllViewsAction.java
//Synthetic comment -- index 5007e1e..33e044d 100644

//Synthetic comment -- @@ -28,24 +28,24 @@

public class LoadAllViewsAction extends TreeViewEnabledAction implements ImageAction {

    private static LoadAllViewsAction action;

    private Image image;

private LoadAllViewsAction() {
super("Load All &Views");
setAccelerator(SWT.MOD1 + 'V');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("load-all-views.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Load all view images");
}

public static LoadAllViewsAction getAction() {
        if (action == null) {
            action = new LoadAllViewsAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -54,6 +54,6 @@
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/QuitAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/QuitAction.java
//Synthetic comment -- index 693d55a..3e9598d 100644

//Synthetic comment -- @@ -23,7 +23,7 @@

public class QuitAction extends Action {

    private static QuitAction action;

private QuitAction() {
super("E&xit");
//Synthetic comment -- @@ -31,10 +31,10 @@
}

public static QuitAction getAction() {
        if (action == null) {
            action = new QuitAction();
}
        return action;
}

@Override








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/ShowOverlayAction.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/actions/ShowOverlayAction.java
//Synthetic comment -- index e4695cd..987386d 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.actions.ImageAction;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -29,37 +29,37 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ShowOverlayAction extends Action implements ImageAction, ImageChangeListener {

    private static ShowOverlayAction action;

    private Image image;

private ShowOverlayAction() {
super("Show In &Loupe", Action.AS_CHECK_BOX);
setAccelerator(SWT.MOD1 + 'L');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("show-overlay.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Show the overlay in the loupe view");
setEnabled(PixelPerfectModel.getModel().getOverlayImage() != null);
PixelPerfectModel.getModel().addImageChangeListener(this);
}

public static ShowOverlayAction getAction() {
        if (action == null) {
            action = new ShowOverlayAction();
}
        return action;
}

@Override
public void run() {
        HierarchyViewerApplication.getApp().showOverlayInLoupe(action.isChecked());
}

public Image getImage() {
        return image;
}

public void crosshairMoved() {








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/util/ActionButton.java
//Synthetic comment -- index cf8d5e2..4681c40 100644

//Synthetic comment -- @@ -28,35 +28,35 @@
import org.eclipse.swt.widgets.Composite;

public class ActionButton implements IPropertyChangeListener, SelectionListener {
    private Button button;

    private Action action;

public ActionButton(Composite parent, ImageAction action) {
        this.action = (Action) action;
        if (this.action.getStyle() == Action.AS_CHECK_BOX) {
            button = new Button(parent, SWT.CHECK);
} else {
            button = new Button(parent, SWT.PUSH);
}
        button.setText(action.getText());
        button.setImage(action.getImage());
        this.action.addPropertyChangeListener(this);
        button.addSelectionListener(this);
        button.setToolTipText(action.getToolTipText());
        button.setEnabled(this.action.isEnabled());
}

public void propertyChange(PropertyChangeEvent e) {
        if (e.getProperty().toUpperCase().equals("ENABLED")) {
            button.setEnabled((Boolean) e.getNewValue());
        } else if (e.getProperty().toUpperCase().equals("CHECKED")) {
            button.setSelection(action.isChecked());
}
}

public void setLayoutData(Object data) {
        button.setLayoutData(data);
}

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -64,13 +64,13 @@
}

public void widgetSelected(SelectionEvent e) {
        if (action.getStyle() == Action.AS_CHECK_BOX) {
            action.setChecked(button.getSelection());
}
        action.run();
}

public void addSelectionListener(SelectionListener listener) {
        button.addSelectionListener(listener);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 1264d60..63b30c0 100644

//Synthetic comment -- @@ -59,33 +59,33 @@
public abstract class HierarchyViewerDirector implements IDeviceChangeListener,
IWindowChangeListener {

    protected static HierarchyViewerDirector director;

public static final String TAG = "hierarchyviewer";

    private int pixelPerfectRefreshesInProgress = 0;

    private Timer pixelPerfectRefreshTimer = new Timer();

    private boolean autoRefresh = false;

public static final int DEFAULT_PIXEL_PERFECT_AUTOREFRESH_INTERVAL = 5;

    private int pixelPerfectAutoRefreshInterval = DEFAULT_PIXEL_PERFECT_AUTOREFRESH_INTERVAL;

    private PixelPerfectAutoRefreshTask currentAutoRefreshTask;

    private String filterText = "";

public void terminate() {
WindowUpdater.terminate();
        pixelPerfectRefreshTimer.cancel();
}

public abstract String getAdbLocation();

public static HierarchyViewerDirector getDirector() {
        return director;
}

public void initDebugBridge() {
//Synthetic comment -- @@ -174,7 +174,7 @@
Window treeViewWindow = TreeViewModel.getModel().getWindow();
if (treeViewWindow != null && treeViewWindow.getDevice() == device) {
TreeViewModel.getModel().setData(null, null);
                    filterText = "";
}
}
});
//Synthetic comment -- @@ -219,9 +219,9 @@
// want it to refresh following the last focus change.
boolean proceed = false;
synchronized (this) {
                if (pixelPerfectRefreshesInProgress <= 1) {
proceed = true;
                    pixelPerfectRefreshesInProgress++;
}
}
if (proceed) {
//Synthetic comment -- @@ -232,7 +232,7 @@
PixelPerfectModel.getModel().setImage(screenshotImage);
}
synchronized (HierarchyViewerDirector.this) {
                            pixelPerfectRefreshesInProgress--;
}
}

//Synthetic comment -- @@ -307,7 +307,7 @@
executeInBackground("Loading view hierarchy", new Runnable() {
public void run() {

                filterText = "";

ViewNode viewNode = DeviceBridge.loadWindowData(window);
if (viewNode != null) {
//Synthetic comment -- @@ -324,7 +324,7 @@
public void run() {
FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
fileDialog.setFilterExtensions(new String[] {
                    "*.jpg;*.jpeg;*.png;*.gif;*.bmp"
});
fileDialog.setFilterNames(new String[] {
"Image (*.jpg, *.jpeg, *.png, *.gif, *.bmp)"
//Synthetic comment -- @@ -430,7 +430,7 @@
if (viewNode != null) {
FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
fileDialog.setFilterExtensions(new String[] {
                        "*.png"
});
fileDialog.setFilterNames(new String[] {
"Portable Network Graphics File (*.png)"
//Synthetic comment -- @@ -446,8 +446,8 @@
image.getImageData()
};
String extensionedFileName = fileName;
                                if (!extensionedFileName.toLowerCase().endsWith(".png")) {
                                    extensionedFileName += ".png";
}
try {
imageLoader.save(extensionedFileName, SWT.IMAGE_PNG);
//Synthetic comment -- @@ -472,7 +472,7 @@
final ImageData imageData = untouchableImage.getImageData();
FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
fileDialog.setFilterExtensions(new String[] {
                        "*.png"
});
fileDialog.setFilterNames(new String[] {
"Portable Network Graphics File (*.png)"
//Synthetic comment -- @@ -487,8 +487,8 @@
imageData
};
String extensionedFileName = fileName;
                                if (!extensionedFileName.toLowerCase().endsWith(".png")) {
                                    extensionedFileName += ".png";
}
try {
imageLoader.save(extensionedFileName, SWT.IMAGE_PNG);
//Synthetic comment -- @@ -511,7 +511,7 @@
if (window != null) {
FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
fileDialog.setFilterExtensions(new String[] {
                        "*.psd"
});
fileDialog.setFilterNames(new String[] {
"Photoshop Document (*.psd)"
//Synthetic comment -- @@ -524,8 +524,8 @@
PsdFile psdFile = DeviceBridge.captureLayers(window);
if (psdFile != null) {
String extensionedFileName = fileName;
                                    if (!extensionedFileName.toLowerCase().endsWith(".psd")) {
                                        extensionedFileName += ".psd";
}
try {
psdFile.write(new FileOutputStream(extensionedFileName));
//Synthetic comment -- @@ -596,7 +596,7 @@
}

public void filterNodes(String filterText) {
        this.filterText = filterText;
DrawableViewNode tree = TreeViewModel.getModel().getTree();
if (tree != null) {
tree.viewNode.filter(filterText);
//Synthetic comment -- @@ -606,7 +606,7 @@
}

public String getFilterText() {
        return filterText;
}

private static class PixelPerfectAutoRefreshTask extends TimerTask {
//Synthetic comment -- @@ -617,44 +617,44 @@
};

public void setPixelPerfectAutoRefresh(boolean value) {
        synchronized (pixelPerfectRefreshTimer) {
            if (value == autoRefresh) {
return;
}
            autoRefresh = value;
            if (autoRefresh) {
                currentAutoRefreshTask = new PixelPerfectAutoRefreshTask();
                pixelPerfectRefreshTimer.schedule(currentAutoRefreshTask,
                        pixelPerfectAutoRefreshInterval * 1000,
                        pixelPerfectAutoRefreshInterval * 1000);
} else {
                currentAutoRefreshTask.cancel();
                currentAutoRefreshTask = null;
}
}
}

public void setPixelPerfectAutoRefreshInterval(int value) {
        synchronized (pixelPerfectRefreshTimer) {
            if (pixelPerfectAutoRefreshInterval == value) {
return;
}
            pixelPerfectAutoRefreshInterval = value;
            if (autoRefresh) {
                currentAutoRefreshTask.cancel();
long timeLeft =
                        Math.max(0, pixelPerfectAutoRefreshInterval
* 1000
                                - (System.currentTimeMillis() - currentAutoRefreshTask
.scheduledExecutionTime()));
                currentAutoRefreshTask = new PixelPerfectAutoRefreshTask();
                pixelPerfectRefreshTimer.schedule(currentAutoRefreshTask, timeLeft,
                        pixelPerfectAutoRefreshInterval * 1000);
}
}
}

public int getPixelPerfectAutoRefreshInverval() {
        return pixelPerfectAutoRefreshInterval;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/CapturePSDAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/CapturePSDAction.java
//Synthetic comment -- index 240ced1..b62ba7e 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class CapturePSDAction extends TreeViewEnabledAction implements ImageAction {

    private static CapturePSDAction action;

    private Image image;

    private Shell shell;

private CapturePSDAction(Shell shell) {
super("&Capture Layers");
        this.shell = shell;
setAccelerator(SWT.MOD1 + 'C');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("capture-psd.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Capture the window layers as a photoshop document");
}

public static CapturePSDAction getAction(Shell shell) {
        if (action == null) {
            action = new CapturePSDAction(shell);
}
        return action;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().capturePSD(shell);
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DisplayViewAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/DisplayViewAction.java
//Synthetic comment -- index 4fc8024..e637216 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class DisplayViewAction extends SelectedNodeEnabledAction implements ImageAction {

    private static DisplayViewAction action;

    private Image image;

    private Shell shell;

private DisplayViewAction(Shell shell) {
super("&Display View");
        this.shell = shell;
setAccelerator(SWT.MOD1 + 'D');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("display.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Display the selected view image in a separate window");
}

public static DisplayViewAction getAction(Shell shell) {
        if (action == null) {
            action = new DisplayViewAction(shell);
}
        return action;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().showCapture(shell);
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InspectScreenshotAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InspectScreenshotAction.java
//Synthetic comment -- index 7ef7109..72280f9 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.WindowChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -29,18 +29,18 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class InspectScreenshotAction extends Action implements ImageAction, WindowChangeListener {

    private static InspectScreenshotAction action;

    private Image image;

private InspectScreenshotAction() {
super("Inspect &Screenshot");
setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("inspect-screenshot.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Inspect a screenshot in the pixel perfect view");
setEnabled(
DeviceSelectionModel.getModel().getSelectedDevice() != null);
//Synthetic comment -- @@ -48,10 +48,10 @@
}

public static InspectScreenshotAction getAction() {
        if (action == null) {
            action = new InspectScreenshotAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -60,7 +60,7 @@
}

public Image getImage() {
        return image;
}

public void deviceChanged(IDevice device) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InvalidateAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/InvalidateAction.java
//Synthetic comment -- index aaf0ff0..83da0ce 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class InvalidateAction extends SelectedNodeEnabledAction implements ImageAction {

    private static InvalidateAction action;

    private Image image;

private InvalidateAction() {
super("&Invalidate Layout");
setAccelerator(SWT.MOD1 + 'I');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("invalidate.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Invalidate the layout for the current window");
}

public static InvalidateAction getAction() {
        if (action == null) {
            action = new InvalidateAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadOverlayAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadOverlayAction.java
//Synthetic comment -- index c948914..81c1b02 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class LoadOverlayAction extends PixelPerfectEnabledAction implements ImageAction {

    private static LoadOverlayAction action;

    private Image image;

    private Shell shell;

private LoadOverlayAction(Shell shell) {
super("Load &Overlay");
        this.shell = shell;
setAccelerator(SWT.MOD1 + 'O');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("load-overlay.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Load an image to overlay the screenshot");
}

public static LoadOverlayAction getAction(Shell shell) {
        if (action == null) {
            action = new LoadOverlayAction(shell);
}
        return action;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().loadOverlay(shell);
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadViewHierarchyAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/LoadViewHierarchyAction.java
//Synthetic comment -- index d26b2ef..9629716 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.WindowChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
//Synthetic comment -- @@ -29,18 +29,18 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class LoadViewHierarchyAction extends Action implements ImageAction, WindowChangeListener {

    private static LoadViewHierarchyAction action;

    private Image image;

private LoadViewHierarchyAction() {
super("Load View &Hierarchy");
setAccelerator(SWT.MOD1 + 'H');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Load the view hierarchy into the tree view");
setEnabled(
DeviceSelectionModel.getModel().getSelectedWindow() != null);
//Synthetic comment -- @@ -48,10 +48,10 @@
}

public static LoadViewHierarchyAction getAction() {
        if (action == null) {
            action = new LoadViewHierarchyAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -60,7 +60,7 @@
}

public Image getImage() {
        return image;
}

public void deviceChanged(IDevice device) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectAutoRefreshAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectAutoRefreshAction.java
//Synthetic comment -- index 5e31829..e104b03 100644

//Synthetic comment -- @@ -27,32 +27,32 @@

public class PixelPerfectAutoRefreshAction extends PixelPerfectEnabledAction implements ImageAction {

    private static PixelPerfectAutoRefreshAction action;

    private Image image;

private PixelPerfectAutoRefreshAction() {
super("Auto &Refresh", Action.AS_CHECK_BOX);
setAccelerator(SWT.MOD1 + 'R');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("auto-refresh.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Automatically refresh the screenshot");
}

public static PixelPerfectAutoRefreshAction getAction() {
        if (action == null) {
            action = new PixelPerfectAutoRefreshAction();
}
        return action;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().setPixelPerfectAutoRefresh(action.isChecked());
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectEnabledAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/PixelPerfectEnabledAction.java
//Synthetic comment -- index 9423d10..b423d14 100644

//Synthetic comment -- @@ -17,12 +17,12 @@
package com.android.hierarchyviewerlib.actions;

import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectEnabledAction extends Action implements ImageChangeListener {
public PixelPerfectEnabledAction(String name) {
super(name);
setEnabled(PixelPerfectModel.getModel().getImage() != null);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectAction.java
//Synthetic comment -- index a5d7514..2e06bbd 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RefreshPixelPerfectAction extends PixelPerfectEnabledAction implements ImageAction {

    private static RefreshPixelPerfectAction action;

    private Image image;

private RefreshPixelPerfectAction() {
super("&Refresh Screenshot");
setAccelerator(SWT.F5);
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("refresh-windows.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Refresh the screenshot");
}

public static RefreshPixelPerfectAction getAction() {
        if (action == null) {
            action = new RefreshPixelPerfectAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectTreeAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshPixelPerfectTreeAction.java
//Synthetic comment -- index 41214df..8c17760 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RefreshPixelPerfectTreeAction extends PixelPerfectEnabledAction implements ImageAction {

    private static RefreshPixelPerfectTreeAction action;

    private Image image;

private RefreshPixelPerfectTreeAction() {
super("Refresh &Tree");
setAccelerator(SWT.MOD1 + 'T');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Refresh the tree");
}

public static RefreshPixelPerfectTreeAction getAction() {
        if (action == null) {
            action = new RefreshPixelPerfectTreeAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshViewAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshViewAction.java
//Synthetic comment -- index 06c48ee..8f15c1c 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RefreshViewAction extends TreeViewEnabledAction implements ImageAction {

    private static RefreshViewAction action;

    private Image image;

private RefreshViewAction() {
super("Load View &Hierarchy");
setAccelerator(SWT.MOD1 + 'H');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("load-view-hierarchy.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Reload the view hierarchy");
}

public static RefreshViewAction getAction() {
        if (action == null) {
            action = new RefreshViewAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshWindowsAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RefreshWindowsAction.java
//Synthetic comment -- index 47d692a..6097ad3 100644

//Synthetic comment -- @@ -27,24 +27,24 @@

public class RefreshWindowsAction extends Action implements ImageAction {

    private static RefreshWindowsAction action;

    private Image image;

private RefreshWindowsAction() {
super("&Refresh");
setAccelerator(SWT.F5);
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("refresh-windows.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Refresh the list of devices");
}

public static RefreshWindowsAction getAction() {
        if (action == null) {
            action = new RefreshWindowsAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -53,6 +53,6 @@
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RequestLayoutAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/RequestLayoutAction.java
//Synthetic comment -- index e3d6f8f..5a79933 100644

//Synthetic comment -- @@ -26,24 +26,24 @@

public class RequestLayoutAction extends SelectedNodeEnabledAction implements ImageAction {

    private static RequestLayoutAction action;

    private Image image;

private RequestLayoutAction() {
super("Request &Layout");
setAccelerator(SWT.MOD1 + 'L');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("request-layout.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Request the view to lay out");
}

public static RequestLayoutAction getAction() {
        if (action == null) {
            action = new RequestLayoutAction();
}
        return action;
}

@Override
//Synthetic comment -- @@ -52,6 +52,6 @@
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SavePixelPerfectAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SavePixelPerfectAction.java
//Synthetic comment -- index 9781d42..45d6da5 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class SavePixelPerfectAction extends PixelPerfectEnabledAction implements ImageAction {

    private static SavePixelPerfectAction action;

    private Image image;

    private Shell shell;

private SavePixelPerfectAction(Shell shell) {
super("&Save as PNG");
        this.shell = shell;
setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("save.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Save the screenshot as a PNG image");
}

public static SavePixelPerfectAction getAction(Shell shell) {
        if (action == null) {
            action = new SavePixelPerfectAction(shell);
}
        return action;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().savePixelPerfect(shell);
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SaveTreeViewAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SaveTreeViewAction.java
//Synthetic comment -- index 094b101..69df7e0 100644

//Synthetic comment -- @@ -27,35 +27,35 @@

public class SaveTreeViewAction extends TreeViewEnabledAction implements ImageAction {

    private static SaveTreeViewAction action;

    private Image image;

    private Shell shell;

private SaveTreeViewAction(Shell shell) {
super("&Save as PNG");
        this.shell = shell;
setAccelerator(SWT.MOD1 + 'S');
ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        image = imageLoader.loadImage("save.png", Display.getDefault());
        setImageDescriptor(ImageDescriptor.createFromImage(image));
setToolTipText("Save the tree view as a PNG image");
}

public static SaveTreeViewAction getAction(Shell shell) {
        if (action == null) {
            action = new SaveTreeViewAction(shell);
}
        return action;
}

@Override
public void run() {
        HierarchyViewerDirector.getDirector().saveTreeView(shell);
}

public Image getImage() {
        return image;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SelectedNodeEnabledAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/SelectedNodeEnabledAction.java
//Synthetic comment -- index 86f75a4..f051f69 100644

//Synthetic comment -- @@ -17,12 +17,12 @@
package com.android.hierarchyviewerlib.actions;

import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

public class SelectedNodeEnabledAction extends Action implements TreeChangeListener {
public SelectedNodeEnabledAction(String name) {
super(name);
setEnabled(TreeViewModel.getModel().getTree() != null








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/TreeViewEnabledAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/TreeViewEnabledAction.java
//Synthetic comment -- index 9ac7fb1..7354ed5 100644

//Synthetic comment -- @@ -17,12 +17,12 @@
package com.android.hierarchyviewerlib.actions;

import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

public class TreeViewEnabledAction extends Action implements TreeChangeListener {
public TreeViewEnabledAction(String name) {
super(name);
setEnabled(TreeViewModel.getModel().getTree() != null);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 399e470..d00c4dc 100644

//Synthetic comment -- @@ -60,14 +60,14 @@

private static final int SERVICE_CODE_IS_SERVER_RUNNING = 3;

    private static AndroidDebugBridge bridge;

    private static final HashMap<IDevice, Integer> devicePortMap = new HashMap<IDevice, Integer>();

    private static final HashMap<IDevice, ViewServerInfo> viewServerInfo =
new HashMap<IDevice, ViewServerInfo>();

    private static int nextLocalPort = DEFAULT_SERVER_PORT;

public static class ViewServerInfo {
public final int protocolVersion;
//Synthetic comment -- @@ -81,11 +81,11 @@
}

public static void initDebugBridge(String adbLocation) {
        if (bridge == null) {
AndroidDebugBridge.init(false /* debugger support */);
}
        if (bridge == null || !bridge.isConnected()) {
            bridge = AndroidDebugBridge.createBridge(adbLocation, true);
}
}

//Synthetic comment -- @@ -94,10 +94,10 @@
}

public static IDevice[] getDevices() {
        if (bridge == null) {
return new IDevice[0];
}
        return bridge.getDevices();
}

/*
//Synthetic comment -- @@ -121,12 +121,12 @@
* @param device
*/
public static void setupDeviceForward(IDevice device) {
        synchronized (devicePortMap) {
if (device.getState() == IDevice.DeviceState.ONLINE) {
                int localPort = nextLocalPort++;
try {
device.createForward(localPort, DEFAULT_SERVER_PORT);
                    devicePortMap.put(device, localPort);
} catch (TimeoutException e) {
Log.e(TAG, "Timeout setting up port forwarding for " + device);
} catch (AdbCommandRejectedException e) {
//Synthetic comment -- @@ -141,12 +141,12 @@
}

public static void removeDeviceForward(IDevice device) {
        synchronized (devicePortMap) {
            final Integer localPort = devicePortMap.get(device);
if (localPort != null) {
try {
device.removeForward(localPort, DEFAULT_SERVER_PORT);
                    devicePortMap.remove(device);
} catch (TimeoutException e) {
Log.e(TAG, "Timeout removing port forwarding for " + device);
} catch (AdbCommandRejectedException e) {
//Synthetic comment -- @@ -160,8 +160,8 @@
}

public static int getDeviceLocalPort(IDevice device) {
        synchronized (devicePortMap) {
            Integer port = devicePortMap.get(device);
if (port != null) {
return port;
}
//Synthetic comment -- @@ -235,15 +235,15 @@
}

private static String buildStartServerShellCommand(int port) {
        return String.format("service call window %d i32 %d", SERVICE_CODE_START_SERVER, port);
}

private static String buildStopServerShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_STOP_SERVER);
}

private static String buildIsServerRunningShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_IS_SERVER_RUNNING);
}

private static class BooleanResultReader extends MultiLineReceiver {
//Synthetic comment -- @@ -256,7 +256,7 @@
@Override
public void processNewLines(String[] strings) {
if (strings.length > 0) {
                Pattern pattern = Pattern.compile(".*?\\([0-9]{8} ([0-9]{8}).*");
Matcher matcher = pattern.matcher(strings[0]);
if (matcher.matches()) {
if (Integer.parseInt(matcher.group(1)) == 1) {
//Synthetic comment -- @@ -277,7 +277,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(device);
            connection.sendCommand("SERVER");
String line = connection.getInputStream().readLine();
if (line != null) {
server = Integer.parseInt(line);
//Synthetic comment -- @@ -292,7 +292,7 @@
connection = null;
try {
connection = new DeviceConnection(device);
            connection.sendCommand("PROTOCOL");
String line = connection.getInputStream().readLine();
if (line != null) {
protocol = Integer.parseInt(line);
//Synthetic comment -- @@ -308,21 +308,21 @@
return null;
}
ViewServerInfo returnValue = new ViewServerInfo(server, protocol);
        synchronized (viewServerInfo) {
            viewServerInfo.put(device, returnValue);
}
return returnValue;
}

public static ViewServerInfo getViewServerInfo(IDevice device) {
        synchronized (viewServerInfo) {
            return viewServerInfo.get(device);
}
}

public static void removeViewServerInfo(IDevice device) {
        synchronized (viewServerInfo) {
            viewServerInfo.remove(device);
}
}

//Synthetic comment -- @@ -336,11 +336,11 @@
ViewServerInfo serverInfo = getViewServerInfo(device);
try {
connection = new DeviceConnection(device);
            connection.sendCommand("LIST");
BufferedReader in = connection.getInputStream();
String line;
while ((line = in.readLine()) != null) {
                if ("DONE.".equalsIgnoreCase(line)) {
break;
}

//Synthetic comment -- @@ -391,7 +391,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(device);
            connection.sendCommand("GET_FOCUS");
String line = connection.getInputStream().readLine();
if (line == null || line.length() == 0) {
return -1;
//Synthetic comment -- @@ -411,7 +411,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("DUMP " + window.encode());
BufferedReader in = connection.getInputStream();
ViewNode currentNode = null;
int currentDepth = -1;
//Synthetic comment -- @@ -457,11 +457,11 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("PROFILE " + window.encode() + " " + viewNode.toString());
BufferedReader in = connection.getInputStream();
int protocol;
            synchronized (viewServerInfo) {
                protocol = viewServerInfo.get(window.getDevice()).protocolVersion;
}
if (protocol < 3) {
return loadProfileData(viewNode, in);
//Synthetic comment -- @@ -485,8 +485,8 @@

private static boolean loadProfileData(ViewNode node, BufferedReader in) throws IOException {
String line;
        if ((line = in.readLine()) == null || line.equalsIgnoreCase("-1 -1 -1")
                || line.equalsIgnoreCase("DONE.")) {
return false;
}
String[] data = line.split(" ");
//Synthetic comment -- @@ -513,7 +513,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("CAPTURE " + window.encode() + " " + viewNode.toString());
return new Image(Display.getDefault(), connection.getSocket().getInputStream());
} catch (Exception e) {
Log.e(TAG, "Unable to capture data for node " + viewNode + " in window "
//Synthetic comment -- @@ -533,7 +533,7 @@
try {
connection = new DeviceConnection(window.getDevice());

            connection.sendCommand("CAPTURE_LAYERS " + window.encode());

in =
new DataInputStream(new BufferedInputStream(connection.getSocket()
//Synthetic comment -- @@ -604,7 +604,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(viewNode.window.getDevice());
            connection.sendCommand("INVALIDATE " + viewNode.window.encode() + " " + viewNode);
} catch (Exception e) {
Log.e(TAG, "Unable to invalidate view " + viewNode + " in window " + viewNode.window
+ " on device " + viewNode.window.getDevice());
//Synthetic comment -- @@ -617,7 +617,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(viewNode.window.getDevice());
            connection.sendCommand("REQUEST_LAYOUT " + viewNode.window.encode() + " " + viewNode);
} catch (Exception e) {
Log.e(TAG, "Unable to request layout for node " + viewNode + " in window "
+ viewNode.window + " on device " + viewNode.window.getDevice());








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceConnection.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceConnection.java
//Synthetic comment -- index cda1a77..f750d5c 100644

//Synthetic comment -- @@ -34,42 +34,42 @@
public class DeviceConnection {

// Now a socket channel, since socket channels are friendly with interrupts.
    private SocketChannel socketChannel;

    private BufferedReader in;

    private BufferedWriter out;

public DeviceConnection(IDevice device) throws IOException {
        socketChannel = SocketChannel.open();
int port = DeviceBridge.getDeviceLocalPort(device);

if (port == -1) {
throw new IOException();
}

        socketChannel.connect(new InetSocketAddress("127.0.0.1", port));
        socketChannel.socket().setSoTimeout(40000);
}

public BufferedReader getInputStream() throws IOException {
        if (in == null) {
            in = new BufferedReader(new InputStreamReader(socketChannel.socket().getInputStream()));
}
        return in;
}

public BufferedWriter getOutputStream() throws IOException {
        if (out == null) {
            out =
                    new BufferedWriter(new OutputStreamWriter(socketChannel.socket()
.getOutputStream()));
}
        return out;
}

public Socket getSocket() {
        return socketChannel.socket();
}

public void sendCommand(String command) throws IOException {
//Synthetic comment -- @@ -81,19 +81,19 @@

public void close() {
try {
            if (in != null) {
                in.close();
}
} catch (IOException e) {
}
try {
            if (out != null) {
                out.close();
}
} catch (IOException e) {
}
try {
            socketChannel.close();
} catch (IOException e) {
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
//Synthetic comment -- index 6457159..d925404 100644

//Synthetic comment -- @@ -183,58 +183,58 @@
}
});

        id = namedProperties.get("mID").value;

left =
                namedProperties.containsKey("mLeft") ? getInt("mLeft", 0) : getInt("layout:mLeft",
0);
        top = namedProperties.containsKey("mTop") ? getInt("mTop", 0) : getInt("layout:mTop", 0);
width =
                namedProperties.containsKey("getWidth()") ? getInt("getWidth()", 0) : getInt(
                        "layout:getWidth()", 0);
height =
                namedProperties.containsKey("getHeight()") ? getInt("getHeight()", 0) : getInt(
                        "layout:getHeight()", 0);
scrollX =
                namedProperties.containsKey("mScrollX") ? getInt("mScrollX", 0) : getInt(
                        "scrolling:mScrollX", 0);
scrollY =
                namedProperties.containsKey("mScrollY") ? getInt("mScrollY", 0) : getInt(
                        "scrolling:mScrollY", 0);
paddingLeft =
                namedProperties.containsKey("mPaddingLeft") ? getInt("mPaddingLeft", 0) : getInt(
                        "padding:mPaddingLeft", 0);
paddingRight =
                namedProperties.containsKey("mPaddingRight") ? getInt("mPaddingRight", 0) : getInt(
                        "padding:mPaddingRight", 0);
paddingTop =
                namedProperties.containsKey("mPaddingTop") ? getInt("mPaddingTop", 0) : getInt(
                        "padding:mPaddingTop", 0);
paddingBottom =
                namedProperties.containsKey("mPaddingBottom") ? getInt("mPaddingBottom", 0)
                        : getInt("padding:mPaddingBottom", 0);
marginLeft =
                namedProperties.containsKey("layout_leftMargin") ? getInt("layout_leftMargin",
                        Integer.MIN_VALUE) : getInt("layout:layout_leftMargin", Integer.MIN_VALUE);
marginRight =
                namedProperties.containsKey("layout_rightMargin") ? getInt("layout_rightMargin",
                        Integer.MIN_VALUE) : getInt("layout:layout_rightMargin", Integer.MIN_VALUE);
marginTop =
                namedProperties.containsKey("layout_topMargin") ? getInt("layout_topMargin",
                        Integer.MIN_VALUE) : getInt("layout:layout_topMargin", Integer.MIN_VALUE);
marginBottom =
                namedProperties.containsKey("layout_bottomMargin") ? getInt("layout_bottomMargin",
Integer.MIN_VALUE)
                        : getInt("layout:layout_bottomMargin", Integer.MIN_VALUE);
baseline =
                namedProperties.containsKey("getBaseline()") ? getInt("getBaseline()", 0) : getInt(
                        "layout:getBaseline()", 0);
willNotDraw =
                namedProperties.containsKey("willNotDraw()") ? getBoolean("willNotDraw()", false)
                        : getBoolean("drawing:willNotDraw()", false);
hasFocus =
                namedProperties.containsKey("hasFocus()") ? getBoolean("hasFocus()", false)
                        : getBoolean("focus:hasFocus()", false);

hasMargins =
marginLeft != Integer.MIN_VALUE && marginRight != Integer.MIN_VALUE
//Synthetic comment -- @@ -307,9 +307,9 @@
int dotIndex = name.lastIndexOf('.');
String shortName = (dotIndex == -1) ? name : name.substring(dotIndex + 1);
filtered =
                !text.equals("")
&& (shortName.toLowerCase().contains(text.toLowerCase()) || (!id
                                .equals("NO_ID") && id.toLowerCase().contains(text.toLowerCase())));
final int N = children.size();
for (int i = 0; i < N; i++) {
children.get(i).filter(text);
//Synthetic comment -- @@ -342,7 +342,7 @@

@Override
public String toString() {
        return name + "@" + hashCode;
}

public static class Property {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/Window.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/Window.java
//Synthetic comment -- index 1869a51..af79081 100644

//Synthetic comment -- @@ -24,37 +24,37 @@
*/
public class Window {

    private String title;

    private int hashCode;

    private IDevice device;

public Window(IDevice device, String title, int hashCode) {
        this.device = device;
        this.title = title;
        this.hashCode = hashCode;
}

public String getTitle() {
        return title;
}

public int getHashCode() {
        return hashCode;
}

public String encode() {
        return Integer.toHexString(hashCode);
}

@Override
public String toString() {
        return title;
}

public IDevice getDevice() {
        return device;
}

public static Window getFocusedWindow(IDevice device) {
//Synthetic comment -- @@ -69,8 +69,8 @@
@Override
public boolean equals(Object other) {
if (other instanceof Window) {
            return hashCode == ((Window) other).hashCode
                    && device.getSerialNumber().equals(((Window) other).device.getSerialNumber());
}
return false;
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/WindowUpdater.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/WindowUpdater.java
//Synthetic comment -- index 26797d2..a0cc635 100644

//Synthetic comment -- @@ -29,10 +29,10 @@
* It notifies all it's listeners of changes.
*/
public class WindowUpdater {
    private static HashMap<IDevice, ArrayList<IWindowChangeListener>> windowChangeListeners =
new HashMap<IDevice, ArrayList<IWindowChangeListener>>();

    private static HashMap<IDevice, Thread> listeningThreads = new HashMap<IDevice, Thread>();

public static interface IWindowChangeListener {
public void windowsChanged(IDevice device);
//Synthetic comment -- @@ -41,58 +41,58 @@
}

public static void terminate() {
        synchronized (listeningThreads) {
            for (IDevice device : listeningThreads.keySet()) {
                listeningThreads.get(device).interrupt();

}
}
}

public static void startListenForWindowChanges(IWindowChangeListener listener, IDevice device) {
        synchronized (windowChangeListeners) {
// In this case, a listening thread already exists, so we don't need
// to create another one.
            if (windowChangeListeners.containsKey(device)) {
                windowChangeListeners.get(device).add(listener);
return;
}
ArrayList<IWindowChangeListener> listeners = new ArrayList<IWindowChangeListener>();
listeners.add(listener);
            windowChangeListeners.put(device, listeners);
}
// Start listening
Thread listeningThread = new Thread(new WindowChangeMonitor(device));
        synchronized (listeningThreads) {
            listeningThreads.put(device, listeningThread);
}
listeningThread.start();
}

public static void stopListenForWindowChanges(IWindowChangeListener listener, IDevice device) {
        synchronized (windowChangeListeners) {
            ArrayList<IWindowChangeListener> listeners = windowChangeListeners.get(device);
listeners.remove(listener);
// There are more listeners, so don't stop the listening thread.
if (listeners.size() != 0) {
return;
}
            windowChangeListeners.remove(device);
}
// Everybody left, so the party's over!
Thread listeningThread;
        synchronized (listeningThreads) {
            listeningThread = listeningThreads.get(device);
            listeningThreads.remove(device);
}
listeningThread.interrupt();
}

private static IWindowChangeListener[] getWindowChangeListenersAsArray(IDevice device) {
IWindowChangeListener[] listeners;
        synchronized (windowChangeListeners) {
ArrayList<IWindowChangeListener> windowChangeListenerList =
                    windowChangeListeners.get(device);
if (windowChangeListenerList == null) {
return null;
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java
//Synthetic comment -- index 09dfe76..d029d39 100644

//Synthetic comment -- @@ -29,84 +29,84 @@
*/
public class DeviceSelectionModel {

    private final HashMap<IDevice, Window[]> deviceMap = new HashMap<IDevice, Window[]>();

    private final HashMap<IDevice, Integer> focusedWindowHashes = new HashMap<IDevice, Integer>();

    private final ArrayList<IDevice> deviceList = new ArrayList<IDevice>();

    private final ArrayList<WindowChangeListener> windowChangeListeners =
            new ArrayList<WindowChangeListener>();

    private IDevice selectedDevice;

    private Window selectedWindow;

    private static DeviceSelectionModel model;

public static DeviceSelectionModel getModel() {
        if (model == null) {
            model = new DeviceSelectionModel();
}
        return model;
}

public boolean containsDevice(IDevice device) {
        synchronized (deviceMap) {
            return deviceMap.containsKey(device);
}
}

public void addDevice(IDevice device, Window[] windows) {
        synchronized (deviceMap) {
            deviceMap.put(device, windows);
            deviceList.add(device);
}
notifyDeviceConnected(device);
}

public void removeDevice(IDevice device) {
boolean selectionChanged = false;
        synchronized (deviceMap) {
            deviceList.remove(device);
            if (!deviceList.contains(device)) {
                deviceMap.remove(device);
                focusedWindowHashes.remove(device);
                if (selectedDevice == device) {
                    selectedDevice = null;
                    selectedWindow = null;
selectionChanged = true;
}
}
}
notifyDeviceDisconnected(device);
if (selectionChanged) {
            notifySelectionChanged(selectedDevice, selectedWindow);
}
}

public void updateDevice(IDevice device, Window[] windows) {
boolean selectionChanged = false;
        synchronized (deviceMap) {
            deviceMap.put(device, windows);
// If the selected window no longer exists, we clear the selection.
            if (selectedDevice == device && selectedWindow != null) {
boolean windowStillExists = false;
for (int i = 0; i < windows.length && !windowStillExists; i++) {
                    if (windows[i].equals(selectedWindow)) {
windowStillExists = true;
}
}
if (!windowStillExists) {
                    selectedDevice = null;
                    selectedWindow = null;
selectionChanged = true;
}
}
}
notifyDeviceChanged(device);
if (selectionChanged) {
            notifySelectionChanged(selectedDevice, selectedWindow);
}
}

//Synthetic comment -- @@ -115,8 +115,8 @@
*/
public void updateFocusedWindow(IDevice device, int focusedWindow) {
Integer oldValue = null;
        synchronized (deviceMap) {
            oldValue = focusedWindowHashes.put(device, new Integer(focusedWindow));
}
// Only notify if the values are different. It would be cool if Java
// containers accepted basic types like int.
//Synthetic comment -- @@ -125,7 +125,7 @@
}
}

    public static interface WindowChangeListener {
public void deviceConnected(IDevice device);

public void deviceChanged(IDevice device);
//Synthetic comment -- @@ -137,21 +137,21 @@
public void selectionChanged(IDevice device, Window window);
}

    private WindowChangeListener[] getWindowChangeListenerList() {
        WindowChangeListener[] listeners = null;
        synchronized (windowChangeListeners) {
            if (windowChangeListeners.size() == 0) {
return null;
}
listeners =
                    windowChangeListeners.toArray(new WindowChangeListener[windowChangeListeners
.size()]);
}
return listeners;
}

private void notifyDeviceConnected(IDevice device) {
        WindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].deviceConnected(device);
//Synthetic comment -- @@ -160,7 +160,7 @@
}

private void notifyDeviceChanged(IDevice device) {
        WindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].deviceChanged(device);
//Synthetic comment -- @@ -169,7 +169,7 @@
}

private void notifyDeviceDisconnected(IDevice device) {
        WindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].deviceDisconnected(device);
//Synthetic comment -- @@ -178,7 +178,7 @@
}

private void notifyFocusChanged(IDevice device) {
        WindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].focusChanged(device);
//Synthetic comment -- @@ -187,7 +187,7 @@
}

private void notifySelectionChanged(IDevice device, Window window) {
        WindowChangeListener[] listeners = getWindowChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].selectionChanged(device, window);
//Synthetic comment -- @@ -195,28 +195,28 @@
}
}

    public void addWindowChangeListener(WindowChangeListener listener) {
        synchronized (windowChangeListeners) {
            windowChangeListeners.add(listener);
}
}

    public void removeWindowChangeListener(WindowChangeListener listener) {
        synchronized (windowChangeListeners) {
            windowChangeListeners.remove(listener);
}
}

public IDevice[] getDevices() {
        synchronized (deviceMap) {
            return deviceList.toArray(new IDevice[deviceList.size()]);
}
}

public Window[] getWindows(IDevice device) {
Window[] windows;
        synchronized (deviceMap) {
            windows = deviceMap.get(device);
}
return windows;
}
//Synthetic comment -- @@ -225,8 +225,8 @@
// that a window with hashcode -1 gets highlighted. If you remember, this is
// the infamous <Focused Window>
public int getFocusedWindow(IDevice device) {
        synchronized (deviceMap) {
            Integer focusedWindow = focusedWindowHashes.get(device);
if (focusedWindow == null) {
return -1;
}
//Synthetic comment -- @@ -235,22 +235,22 @@
}

public void setSelection(IDevice device, Window window) {
        synchronized (deviceMap) {
            selectedDevice = device;
            selectedWindow = window;
}
notifySelectionChanged(device, window);
}

public IDevice getSelectedDevice() {
        synchronized (deviceMap) {
            return selectedDevice;
}
}

public Window getSelectedWindow() {
        synchronized (deviceMap) {
            return selectedWindow;
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java
//Synthetic comment -- index e52db14..004adb2 100644

//Synthetic comment -- @@ -35,52 +35,52 @@

public static final int DEFAULT_OVERLAY_TRANSPARENCY_PERCENTAGE = 50;

    private IDevice device;

    private Image image;

    private Point crosshairLocation;

    private ViewNode viewNode;

    private ViewNode selected;

    private int zoom;

    private final ArrayList<ImageChangeListener> imageChangeListeners =
            new ArrayList<ImageChangeListener>();

    private Image overlayImage;

    private double overlayTransparency = DEFAULT_OVERLAY_TRANSPARENCY_PERCENTAGE / 100.0;

    private static PixelPerfectModel model;

public static PixelPerfectModel getModel() {
        if (model == null) {
            model = new PixelPerfectModel();
}
        return model;
}

public void setData(final IDevice device, final Image image, final ViewNode viewNode) {
        final Image toDispose = this.image;
        final Image toDispose2 = this.overlayImage;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.device = device;
                    PixelPerfectModel.this.image = image;
                    PixelPerfectModel.this.viewNode = viewNode;
if (image != null) {
                        PixelPerfectModel.this.crosshairLocation =
new Point(image.getBounds().width / 2, image.getBounds().height / 2);
} else {
                        PixelPerfectModel.this.crosshairLocation = null;
}
                    overlayImage = null;
                    PixelPerfectModel.this.selected = null;
                    zoom = DEFAULT_ZOOM;
}
}
});
//Synthetic comment -- @@ -104,14 +104,14 @@

public void setCrosshairLocation(int x, int y) {
synchronized (this) {
            crosshairLocation = new Point(x, y);
}
notifyCrosshairMoved();
}

public void setSelected(ViewNode selected) {
synchronized (this) {
            this.selected = selected;
}
notifySelectionChanged();
}
//Synthetic comment -- @@ -120,8 +120,8 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.viewNode = viewNode;
                    PixelPerfectModel.this.selected = null;
}
}
});
//Synthetic comment -- @@ -129,11 +129,11 @@
}

public void setImage(final Image image) {
        final Image toDispose = this.image;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.image = image;
}
}
});
//Synthetic comment -- @@ -155,17 +155,17 @@
if (newZoom > MAX_ZOOM) {
newZoom = MAX_ZOOM;
}
            zoom = newZoom;
}
notifyZoomChanged();
}

public void setOverlayImage(final Image overlayImage) {
        final Image toDispose = this.overlayImage;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (PixelPerfectModel.this) {
                    PixelPerfectModel.this.overlayImage = overlayImage;
}
}
});
//Synthetic comment -- @@ -183,60 +183,60 @@
synchronized (this) {
value = Math.max(value, 0);
value = Math.min(value, 1);
            overlayTransparency = value;
}
notifyOverlayTransparencyChanged();
}

public ViewNode getViewNode() {
synchronized (this) {
            return viewNode;
}
}

public Point getCrosshairLocation() {
synchronized (this) {
            return crosshairLocation;
}
}

public Image getImage() {
synchronized (this) {
            return image;
}
}

public ViewNode getSelected() {
synchronized (this) {
            return selected;
}
}

public IDevice getDevice() {
synchronized (this) {
            return device;
}
}

public int getZoom() {
synchronized (this) {
            return zoom;
}
}

public Image getOverlayImage() {
synchronized (this) {
            return overlayImage;
}
}

public double getOverlayTransparency() {
synchronized (this) {
            return overlayTransparency;
}
}

    public static interface ImageChangeListener {
public void imageLoaded();

public void imageChanged();
//Synthetic comment -- @@ -254,21 +254,21 @@
public void overlayTransparencyChanged();
}

    private ImageChangeListener[] getImageChangeListenerList() {
        ImageChangeListener[] listeners = null;
        synchronized (imageChangeListeners) {
            if (imageChangeListeners.size() == 0) {
return null;
}
listeners =
                    imageChangeListeners.toArray(new ImageChangeListener[imageChangeListeners
.size()]);
}
return listeners;
}

public void notifyImageLoaded() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].imageLoaded();
//Synthetic comment -- @@ -277,7 +277,7 @@
}

public void notifyImageChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].imageChanged();
//Synthetic comment -- @@ -286,7 +286,7 @@
}

public void notifyCrosshairMoved() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].crosshairMoved();
//Synthetic comment -- @@ -295,7 +295,7 @@
}

public void notifySelectionChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].selectionChanged();
//Synthetic comment -- @@ -304,7 +304,7 @@
}

public void notifyTreeChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].treeChanged();
//Synthetic comment -- @@ -313,7 +313,7 @@
}

public void notifyZoomChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].zoomChanged();
//Synthetic comment -- @@ -322,7 +322,7 @@
}

public void notifyOverlayChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].overlayChanged();
//Synthetic comment -- @@ -331,7 +331,7 @@
}

public void notifyOverlayTransparencyChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].overlayTransparencyChanged();
//Synthetic comment -- @@ -339,15 +339,15 @@
}
}

    public void addImageChangeListener(ImageChangeListener listener) {
        synchronized (imageChangeListeners) {
            imageChangeListeners.add(listener);
}
}

    public void removeImageChangeListener(ImageChangeListener listener) {
        synchronized (imageChangeListeners) {
            imageChangeListeners.remove(listener);
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
//Synthetic comment -- index ba0fa57..279b5fd 100644

//Synthetic comment -- @@ -29,58 +29,58 @@

public static final double MIN_ZOOM = 0.2;

    private Window window;

    private DrawableViewNode tree;

    private DrawableViewNode selectedNode;

    private Rectangle viewport;

    private double zoom;

    private final ArrayList<TreeChangeListener> treeChangeListeners =
            new ArrayList<TreeChangeListener>();

    private static TreeViewModel model;

public static TreeViewModel getModel() {
        if (model == null) {
            model = new TreeViewModel();
}
        return model;
}

public void setData(Window window, ViewNode viewNode) {
synchronized (this) {
            if (tree != null) {
                tree.viewNode.dispose();
}
            this.window = window;
if (viewNode == null) {
                tree = null;
} else {
                tree = new DrawableViewNode(viewNode);
                tree.setLeft();
                tree.placeRoot();
}
            viewport = null;
            zoom = 1;
            selectedNode = null;
}
notifyTreeChanged();
}

public void setSelection(DrawableViewNode selectedNode) {
synchronized (this) {
            this.selectedNode = selectedNode;
}
notifySelectionChanged();
}

public void setViewport(Rectangle viewport) {
synchronized (this) {
            this.viewport = viewport;
}
notifyViewportChanged();
}
//Synthetic comment -- @@ -88,9 +88,9 @@
public void setZoom(double newZoom) {
Point zoomPoint = null;
synchronized (this) {
            if (tree != null && viewport != null) {
zoomPoint =
                        new Point(viewport.x + viewport.width / 2, viewport.y + viewport.height / 2);
}
}
zoomOnPoint(newZoom, zoomPoint);
//Synthetic comment -- @@ -98,18 +98,18 @@

public void zoomOnPoint(double newZoom, Point zoomPoint) {
synchronized (this) {
            if (tree != null && this.viewport != null) {
if (newZoom < MIN_ZOOM) {
newZoom = MIN_ZOOM;
}
if (newZoom > MAX_ZOOM) {
newZoom = MAX_ZOOM;
}
                viewport.x = zoomPoint.x - (zoomPoint.x - viewport.x) * zoom / newZoom;
                viewport.y = zoomPoint.y - (zoomPoint.y - viewport.y) * zoom / newZoom;
                viewport.width = viewport.width * zoom / newZoom;
                viewport.height = viewport.height * zoom / newZoom;
                zoom = newZoom;
}
}
notifyZoomChanged();
//Synthetic comment -- @@ -117,35 +117,35 @@

public DrawableViewNode getTree() {
synchronized (this) {
            return tree;
}
}

public Window getWindow() {
synchronized (this) {
            return window;
}
}

public Rectangle getViewport() {
synchronized (this) {
            return viewport;
}
}

public double getZoom() {
synchronized (this) {
            return zoom;
}
}

public DrawableViewNode getSelection() {
synchronized (this) {
            return selectedNode;
}
}

    public static interface TreeChangeListener {
public void treeChanged();

public void selectionChanged();
//Synthetic comment -- @@ -155,20 +155,20 @@
public void zoomChanged();
}

    private TreeChangeListener[] getTreeChangeListenerList() {
        TreeChangeListener[] listeners = null;
        synchronized (treeChangeListeners) {
            if (treeChangeListeners.size() == 0) {
return null;
}
listeners =
                    treeChangeListeners.toArray(new TreeChangeListener[treeChangeListeners.size()]);
}
return listeners;
}

public void notifyTreeChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].treeChanged();
//Synthetic comment -- @@ -177,7 +177,7 @@
}

public void notifySelectionChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].selectionChanged();
//Synthetic comment -- @@ -186,7 +186,7 @@
}

public void notifyViewportChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].viewportChanged();
//Synthetic comment -- @@ -195,7 +195,7 @@
}

public void notifyZoomChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {
for (int i = 0; i < listeners.length; i++) {
listeners[i].zoomChanged();
//Synthetic comment -- @@ -203,15 +203,15 @@
}
}

    public void addTreeChangeListener(TreeChangeListener listener) {
        synchronized (treeChangeListeners) {
            treeChangeListeners.add(listener);
}
}

    public void removeTreeChangeListener(TreeChangeListener listener) {
        synchronized (treeChangeListeners) {
            treeChangeListeners.remove(listener);
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java
//Synthetic comment -- index ecee8d0..6ff8125 100644

//Synthetic comment -- @@ -40,172 +40,172 @@
import org.eclipse.swt.widgets.Shell;

public class CaptureDisplay {
    private static Shell shell;

    private static Canvas canvas;

    private static Image image;

    private static ViewNode viewNode;

    private static Composite buttonBar;

    private static Button onWhite;

    private static Button onBlack;

    private static Button showExtras;

public static void show(Shell parentShell, ViewNode viewNode, Image image) {
        if (shell == null) {
createShell();
}
        if (shell.isVisible() && CaptureDisplay.viewNode != null) {
            CaptureDisplay.viewNode.dereferenceImage();
}
        CaptureDisplay.image = image;
        CaptureDisplay.viewNode = viewNode;
viewNode.referenceImage();
        shell.setText(viewNode.name);

        boolean shellVisible = shell.isVisible();
if (!shellVisible) {
            shell.setSize(0, 0);
}
Rectangle bounds =
                shell.computeTrim(0, 0, Math.max(buttonBar.getBounds().width,
                        image.getBounds().width), buttonBar.getBounds().height
+ image.getBounds().height + 5);
        shell.setSize(bounds.width, bounds.height);
if (!shellVisible) {
            shell.setLocation(parentShell.getBounds().x
+ (parentShell.getBounds().width - bounds.width) / 2, parentShell.getBounds().y
+ (parentShell.getBounds().height - bounds.height) / 2);
}
        shell.open();
if (shellVisible) {
            canvas.redraw();
}
}

private static void createShell() {
        shell = new Shell(Display.getDefault(), SWT.CLOSE | SWT.TITLE);
GridLayout gridLayout = new GridLayout();
gridLayout.marginWidth = 0;
gridLayout.marginHeight = 0;
        shell.setLayout(gridLayout);

        buttonBar = new Composite(shell, SWT.NONE);
RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
rowLayout.pack = true;
rowLayout.center = true;
        buttonBar.setLayout(rowLayout);
        Composite buttons = new Composite(buttonBar, SWT.NONE);
buttons.setLayout(new FillLayout());

        onWhite = new Button(buttons, SWT.TOGGLE);
        onWhite.setText("On White");
        onBlack = new Button(buttons, SWT.TOGGLE);
        onBlack.setText("On Black");
        onBlack.setSelection(true);
        onWhite.addSelectionListener(whiteSelectionListener);
        onBlack.addSelectionListener(blackSelectionListener);

        showExtras = new Button(buttonBar, SWT.CHECK);
        showExtras.setText("Show Extras");
        showExtras.addSelectionListener(extrasSelectionListener);

        canvas = new Canvas(shell, SWT.NONE);
        canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
        canvas.addPaintListener(paintListener);

        shell.addShellListener(shellListener);

ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("display.png", Display.getDefault());
        shell.setImage(image);
}

    private static PaintListener paintListener = new PaintListener() {

public void paintControl(PaintEvent e) {
            if (onWhite.getSelection()) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
} else {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
}
            e.gc.fillRectangle(0, 0, canvas.getBounds().width, canvas.getBounds().height);
            if (image != null) {
                int width = image.getBounds().width;
                int height = image.getBounds().height;
                int x = (canvas.getBounds().width - width) / 2;
                int y = (canvas.getBounds().height - height) / 2;
                e.gc.drawImage(image, x, y);
                if (showExtras.getSelection()) {
                    if ((viewNode.paddingLeft | viewNode.paddingRight | viewNode.paddingTop | viewNode.paddingBottom) != 0) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
                        e.gc.drawRectangle(x + viewNode.paddingLeft, y + viewNode.paddingTop, width
                                - viewNode.paddingLeft - viewNode.paddingRight - 1, height
                                - viewNode.paddingTop - viewNode.paddingBottom - 1);
}
                    if (viewNode.hasMargins) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
                        e.gc.drawRectangle(x - viewNode.marginLeft, y - viewNode.marginTop, width
                                + viewNode.marginLeft + viewNode.marginRight - 1, height
                                + viewNode.marginTop + viewNode.marginBottom - 1);
}
                    if (viewNode.baseline != -1) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                        e.gc.drawLine(x, y + viewNode.baseline, x + width - 1, viewNode.baseline);
}
}
}
}
};

    private static ShellAdapter shellListener = new ShellAdapter() {
@Override
public void shellClosed(ShellEvent e) {
e.doit = false;
            shell.setVisible(false);
            if (viewNode != null) {
                viewNode.dereferenceImage();
}
}

};

    private static SelectionListener whiteSelectionListener = new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
// pass
}

public void widgetSelected(SelectionEvent e) {
            onWhite.setSelection(true);
            onBlack.setSelection(false);
            canvas.redraw();
}
};

    private static SelectionListener blackSelectionListener = new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
// pass
}

public void widgetSelected(SelectionEvent e) {
            onBlack.setSelection(true);
            onWhite.setSelection(false);
            canvas.redraw();
}
};

    private static SelectionListener extrasSelectionListener = new SelectionListener() {
public void widgetDefaultSelected(SelectionEvent e) {
// pass
}

public void widgetSelected(SelectionEvent e) {
            canvas.redraw();
}
};
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java
//Synthetic comment -- index 4e0748f..4f2e17e 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel.WindowChangeListener;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
//Synthetic comment -- @@ -47,29 +47,29 @@
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class DeviceSelector extends Composite implements WindowChangeListener, SelectionListener {
    private TreeViewer treeViewer;

    private Tree tree;

    private DeviceSelectionModel model;

    private Font boldFont;

    private Image deviceImage;

    private Image emulatorImage;

private final static int ICON_WIDTH = 16;

    private boolean doTreeViewStuff;

    private boolean doPixelPerfectStuff;

private class ContentProvider implements ITreeContentProvider, ILabelProvider, IFontProvider {
public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof IDevice && doTreeViewStuff) {
                Window[] list = model.getWindows((IDevice) parentElement);
if (list != null) {
return list;
}
//Synthetic comment -- @@ -85,8 +85,8 @@
}

public boolean hasChildren(Object element) {
            if (element instanceof IDevice && doTreeViewStuff) {
                Window[] list = model.getWindows((IDevice) element);
if (list != null) {
return list.length != 0;
}
//Synthetic comment -- @@ -96,7 +96,7 @@

public Object[] getElements(Object inputElement) {
if (inputElement instanceof DeviceSelectionModel) {
                return model.getDevices();
}
return new Object[0];
}
//Synthetic comment -- @@ -112,9 +112,9 @@
public Image getImage(Object element) {
if (element instanceof IDevice) {
if (((IDevice) element).isEmulator()) {
                    return emulatorImage;
}
                return deviceImage;
}
return null;
}
//Synthetic comment -- @@ -130,9 +130,9 @@

public Font getFont(Object element) {
if (element instanceof Window) {
                int focusedWindow = model.getFocusedWindow(((Window) element).getDevice());
if (focusedWindow == ((Window) element).getHashCode()) {
                    return boldFont;
}
}
return null;
//Synthetic comment -- @@ -154,28 +154,28 @@

public DeviceSelector(Composite parent, boolean doTreeViewStuff, boolean doPixelPerfectStuff) {
super(parent, SWT.NONE);
        this.doTreeViewStuff = doTreeViewStuff;
        this.doPixelPerfectStuff = doPixelPerfectStuff;
setLayout(new FillLayout());
        treeViewer = new TreeViewer(this, SWT.SINGLE);
        treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

        tree = treeViewer.getTree();
        tree.setLinesVisible(true);
        tree.addSelectionListener(this);

        addDisposeListener(disposeListener);

loadResources();

        model = DeviceSelectionModel.getModel();
ContentProvider contentProvider = new ContentProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(contentProvider);
        model.addWindowChangeListener(this);
        treeViewer.setInput(model);

        addControlListener(controlListener);
}

public void loadResources() {
//Synthetic comment -- @@ -189,38 +189,38 @@
.getStyle()
| SWT.BOLD);
}
        boldFont = new Font(Display.getDefault(), newFontData);

ImageLoader loader = ImageLoader.getDdmUiLibLoader();
        deviceImage =
                loader.loadImage(display, "device.png", ICON_WIDTH, ICON_WIDTH, display
.getSystemColor(SWT.COLOR_RED));

        emulatorImage =
                loader.loadImage(display, "emulator.png", ICON_WIDTH, ICON_WIDTH, display
.getSystemColor(SWT.COLOR_BLUE));
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeWindowChangeListener(DeviceSelector.this);
            boldFont.dispose();
}
};

// If the window gets too small, hide the data, otherwise SWT throws an
// ERROR.

    private ControlListener controlListener = new ControlAdapter() {
private boolean noInput = false;

@Override
public void controlResized(ControlEvent e) {
if (getBounds().height <= 38) {
                treeViewer.setInput(null);
noInput = true;
} else if (noInput) {
                treeViewer.setInput(model);
noInput = false;
}
}
//Synthetic comment -- @@ -228,20 +228,20 @@

@Override
public boolean setFocus() {
        return tree.setFocus();
}

public void setMode(boolean doTreeViewStuff, boolean doPixelPerfectStuff) {
        if (this.doTreeViewStuff != doTreeViewStuff
                || this.doPixelPerfectStuff != doPixelPerfectStuff) {
            final boolean expandAll = !this.doTreeViewStuff && doTreeViewStuff;
            this.doTreeViewStuff = doTreeViewStuff;
            this.doPixelPerfectStuff = doPixelPerfectStuff;
Display.getDefault().syncExec(new Runnable() {
public void run() {
                    treeViewer.refresh();
if (expandAll) {
                        treeViewer.expandAll();
}
}
});
//Synthetic comment -- @@ -251,8 +251,8 @@
public void deviceConnected(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                treeViewer.refresh();
                treeViewer.setExpandedState(device, true);
}
});
}
//Synthetic comment -- @@ -260,11 +260,11 @@
public void deviceChanged(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                TreeSelection selection = (TreeSelection) treeViewer.getSelection();
                treeViewer.refresh(device);
if (selection.getFirstElement() instanceof Window
&& ((Window) selection.getFirstElement()).getDevice() == device) {
                    treeViewer.setSelection(selection, true);
}
}
});
//Synthetic comment -- @@ -273,7 +273,7 @@
public void deviceDisconnected(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                treeViewer.refresh();
}
});
}
//Synthetic comment -- @@ -281,11 +281,11 @@
public void focusChanged(final IDevice device) {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                TreeSelection selection = (TreeSelection) treeViewer.getSelection();
                treeViewer.refresh(device);
if (selection.getFirstElement() instanceof Window
&& ((Window) selection.getFirstElement()).getDevice() == device) {
                    treeViewer.setSelection(selection, true);
}
}
});
//Synthetic comment -- @@ -297,9 +297,9 @@

public void widgetDefaultSelected(SelectionEvent e) {
Object selection = ((TreeItem) e.item).getData();
        if (selection instanceof IDevice && doPixelPerfectStuff) {
HierarchyViewerDirector.getDirector().loadPixelPerfectData((IDevice) selection);
        } else if (selection instanceof Window && doTreeViewStuff) {
HierarchyViewerDirector.getDirector().loadViewTreeData((Window) selection);
}
}
//Synthetic comment -- @@ -307,9 +307,9 @@
public void widgetSelected(SelectionEvent e) {
Object selection = ((TreeItem) e.item).getData();
if (selection instanceof IDevice) {
            model.setSelection((IDevice) selection, null);
} else if (selection instanceof Window) {
            model.setSelection(((Window) selection).getDevice(), (Window) selection);
}
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java
//Synthetic comment -- index 1a13e48..94c63aa 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;

//Synthetic comment -- @@ -40,66 +40,66 @@

import java.util.ArrayList;

public class LayoutViewer extends Canvas implements TreeChangeListener {

    private TreeViewModel model;

    private DrawableViewNode tree;

    private DrawableViewNode selectedNode;

    private Transform transform;

    private Transform inverse;

    private double scale;

    private boolean showExtras = false;

    private boolean onBlack = true;

public LayoutViewer(Composite parent) {
super(parent, SWT.NONE);
        model = TreeViewModel.getModel();
        model.addTreeChangeListener(this);

        addDisposeListener(disposeListener);
        addPaintListener(paintListener);
        addListener(SWT.Resize, resizeListener);
        addMouseListener(mouseListener);

        transform = new Transform(Display.getDefault());
        inverse = new Transform(Display.getDefault());

treeChanged();
}

public void setShowExtras(boolean show) {
        showExtras = show;
doRedraw();
}

public void setOnBlack(boolean value) {
        onBlack = value;
doRedraw();
}

public boolean getOnBlack() {
        return onBlack;
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeTreeChangeListener(LayoutViewer.this);
            transform.dispose();
            inverse.dispose();
            if (selectedNode != null) {
                selectedNode.viewNode.dereferenceImage();
}
}
};

    private Listener resizeListener = new Listener() {
public void handleEvent(Event e) {
synchronized (this) {
setTransform();
//Synthetic comment -- @@ -107,12 +107,12 @@
}
};

    private MouseListener mouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
            if (selectedNode != null) {
HierarchyViewerDirector.getDirector()
                        .showCapture(getShell(), selectedNode.viewNode);
}
}

//Synthetic comment -- @@ -120,21 +120,21 @@
boolean selectionChanged = false;
DrawableViewNode newSelection = null;
synchronized (LayoutViewer.this) {
                if (tree != null) {
float[] pt = {
e.x, e.y
};
                    inverse.transform(pt);
newSelection =
                            updateSelection(tree, pt[0], pt[1], 0, 0, 0, 0, tree.viewNode.width,
                                    tree.viewNode.height);
                    if (selectedNode != newSelection) {
selectionChanged = true;
}
}
}
if (selectionChanged) {
                model.setSelection(newSelection);
}
}

//Synthetic comment -- @@ -175,29 +175,29 @@
return node;
}

    private PaintListener paintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (LayoutViewer.this) {
                if (onBlack) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
} else {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
}
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                if (tree != null) {
                    e.gc.setLineWidth((int) Math.ceil(0.3 / scale));
                    e.gc.setTransform(transform);
                    if (onBlack) {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
} else {
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
}
Rectangle parentClipping = e.gc.getClipping();
                    e.gc.setClipping(0, 0, tree.viewNode.width + (int) Math.ceil(0.3 / scale),
                            tree.viewNode.height + (int) Math.ceil(0.3 / scale));
                    paintRecursive(e.gc, tree, 0, 0, true);

                    if (selectedNode != null) {
e.gc.setClipping(parentClipping);

// w00t, let's be nice and display the whole path in
//Synthetic comment -- @@ -205,8 +205,8 @@
ArrayList<Point> rightLeftDistances = new ArrayList<Point>();
int left = 0;
int top = 0;
                        DrawableViewNode currentNode = selectedNode;
                        while (currentNode != tree) {
left += currentNode.viewNode.left;
top += currentNode.viewNode.top;
currentNode = currentNode.parent;
//Synthetic comment -- @@ -215,7 +215,7 @@
rightLeftDistances.add(new Point(left, top));
}
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
                        currentNode = selectedNode.parent;
final int N = rightLeftDistances.size();
for (int i = 0; i < N; i++) {
e.gc.drawRectangle((int) (left - rightLeftDistances.get(i).x),
//Synthetic comment -- @@ -224,23 +224,23 @@
currentNode = currentNode.parent;
}

                        if (showExtras && selectedNode.viewNode.image != null) {
                            e.gc.drawImage(selectedNode.viewNode.image, left, top);
                            if (onBlack) {
e.gc.setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_WHITE));
} else {
e.gc.setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_BLACK));
}
                            paintRecursive(e.gc, selectedNode, left, top, true);

}

e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                        e.gc.setLineWidth((int) Math.ceil(2 / scale));
                        e.gc.drawRectangle(left, top, selectedNode.viewNode.width,
                                selectedNode.viewNode.height);
}
}
}
//Synthetic comment -- @@ -260,11 +260,11 @@
int x1 = Math.max(parentClipping.x, left);
int x2 =
Math.min(parentClipping.x + parentClipping.width, left + node.viewNode.width
                        + (int) Math.ceil(0.3 / scale));
int y1 = Math.max(parentClipping.y, top);
int y2 =
Math.min(parentClipping.y + parentClipping.height, top + node.viewNode.height
                        + (int) Math.ceil(0.3 / scale));

// Clipping is weird... You set it to -5 and it comes out 17 or
// something.
//Synthetic comment -- @@ -293,38 +293,38 @@
}

private void setTransform() {
        if (tree != null) {
Rectangle bounds = getBounds();
int leftRightPadding = bounds.width <= 30 ? 0 : 5;
int topBottomPadding = bounds.height <= 30 ? 0 : 5;
            scale =
                    Math.min(1.0 * (bounds.width - leftRightPadding * 2) / tree.viewNode.width, 1.0
                            * (bounds.height - topBottomPadding * 2) / tree.viewNode.height);
            int scaledWidth = (int) Math.ceil(tree.viewNode.width * scale);
            int scaledHeight = (int) Math.ceil(tree.viewNode.height * scale);

            transform.identity();
            inverse.identity();
            transform.translate((bounds.width - scaledWidth) / 2.0f,
(bounds.height - scaledHeight) / 2.0f);
            inverse.translate((bounds.width - scaledWidth) / 2.0f,
(bounds.height - scaledHeight) / 2.0f);
            transform.scale((float) scale, (float) scale);
            inverse.scale((float) scale, (float) scale);
if (bounds.width != 0 && bounds.height != 0) {
                inverse.invert();
}
}
}

public void selectionChanged() {
synchronized (this) {
            if (selectedNode != null) {
                selectedNode.viewNode.dereferenceImage();
}
            selectedNode = model.getSelection();
            if (selectedNode != null) {
                selectedNode.viewNode.referenceImage();
}
}
doRedraw();
//Synthetic comment -- @@ -335,13 +335,13 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    if (selectedNode != null) {
                        selectedNode.viewNode.dereferenceImage();
}
                    tree = model.getTree();
                    selectedNode = model.getSelection();
                    if (selectedNode != null) {
                        selectedNode.viewNode.referenceImage();
}
setTransform();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java
//Synthetic comment -- index 42bcc59..90e19c6 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
//Synthetic comment -- @@ -39,72 +39,72 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfect extends ScrolledComposite implements ImageChangeListener {
    private Canvas canvas;

    private PixelPerfectModel model;

    private Image image;

    private Color crosshairColor;

    private Color marginColor;

    private Color borderColor;

    private Color paddingColor;

    private int width;

    private int height;

    private Point crosshairLocation;

    private ViewNode selectedNode;

    private Image overlayImage;

    private double overlayTransparency;

public PixelPerfect(Composite parent) {
super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        canvas = new Canvas(this, SWT.NONE);
        setContent(canvas);
setExpandHorizontal(true);
setExpandVertical(true);
        model = PixelPerfectModel.getModel();
        model.addImageChangeListener(this);

        canvas.addPaintListener(paintListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMoveListener(mouseMoveListener);
        canvas.addKeyListener(keyListener);

        addDisposeListener(disposeListener);

        crosshairColor = new Color(Display.getDefault(), new RGB(0, 255, 255));
        borderColor = new Color(Display.getDefault(), new RGB(255, 0, 0));
        marginColor = new Color(Display.getDefault(), new RGB(0, 255, 0));
        paddingColor = new Color(Display.getDefault(), new RGB(0, 0, 255));

imageLoaded();
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeImageChangeListener(PixelPerfect.this);
            crosshairColor.dispose();
            borderColor.dispose();
            paddingColor.dispose();
}
};

@Override
public boolean setFocus() {
        return canvas.setFocus();
}

    private MouseListener mouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
// pass
//Synthetic comment -- @@ -120,7 +120,7 @@

};

    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
public void mouseMove(MouseEvent e) {
if (e.stateMask != 0) {
handleMouseEvent(e);
//Synthetic comment -- @@ -130,49 +130,49 @@

private void handleMouseEvent(MouseEvent e) {
synchronized (PixelPerfect.this) {
            if (image == null) {
return;
}
            int leftOffset = canvas.getSize().x / 2 - width / 2;
            int topOffset = canvas.getSize().y / 2 - height / 2;
e.x -= leftOffset;
e.y -= topOffset;
e.x = Math.max(e.x, 0);
            e.x = Math.min(e.x, width - 1);
e.y = Math.max(e.y, 0);
            e.y = Math.min(e.y, height - 1);
}
        model.setCrosshairLocation(e.x, e.y);
}

    private KeyListener keyListener = new KeyListener() {

public void keyPressed(KeyEvent e) {
boolean crosshairMoved = false;
synchronized (PixelPerfect.this) {
                if (image != null) {
switch (e.keyCode) {
case SWT.ARROW_UP:
                            if (crosshairLocation.y != 0) {
                                crosshairLocation.y--;
crosshairMoved = true;
}
break;
case SWT.ARROW_DOWN:
                            if (crosshairLocation.y != height - 1) {
                                crosshairLocation.y++;
crosshairMoved = true;
}
break;
case SWT.ARROW_LEFT:
                            if (crosshairLocation.x != 0) {
                                crosshairLocation.x--;
crosshairMoved = true;
}
break;
case SWT.ARROW_RIGHT:
                            if (crosshairLocation.x != width - 1) {
                                crosshairLocation.x++;
crosshairMoved = true;
}
break;
//Synthetic comment -- @@ -180,7 +180,7 @@
}
}
if (crosshairMoved) {
                model.setCrosshairLocation(crosshairLocation.x, crosshairLocation.y);
}
}

//Synthetic comment -- @@ -190,43 +190,43 @@

};

    private PaintListener paintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (PixelPerfect.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                e.gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
                if (image != null) {
// Let's be cool and put it in the center...
                    int leftOffset = canvas.getSize().x / 2 - width / 2;
                    int topOffset = canvas.getSize().y / 2 - height / 2;
                    e.gc.drawImage(image, leftOffset, topOffset);
                    if (overlayImage != null) {
                        e.gc.setAlpha((int) (overlayTransparency * 255));
int overlayTopOffset =
                                canvas.getSize().y / 2 + height / 2
                                        - overlayImage.getBounds().height;
                        e.gc.drawImage(overlayImage, leftOffset, overlayTopOffset);
e.gc.setAlpha(255);
}

                    if (selectedNode != null) {
// If the screen is in landscape mode, the
// coordinates are backwards.
int leftShift = 0;
int topShift = 0;
                        int nodeLeft = selectedNode.left;
                        int nodeTop = selectedNode.top;
                        int nodeWidth = selectedNode.width;
                        int nodeHeight = selectedNode.height;
                        int nodeMarginLeft = selectedNode.marginLeft;
                        int nodeMarginTop = selectedNode.marginTop;
                        int nodeMarginRight = selectedNode.marginRight;
                        int nodeMarginBottom = selectedNode.marginBottom;
                        int nodePadLeft = selectedNode.paddingLeft;
                        int nodePadTop = selectedNode.paddingTop;
                        int nodePadRight = selectedNode.paddingRight;
                        int nodePadBottom = selectedNode.paddingBottom;
                        ViewNode cur = selectedNode;
while (cur.parent != null) {
leftShift += cur.parent.left - cur.parent.scrollX;
topShift += cur.parent.top - cur.parent.scrollY;
//Synthetic comment -- @@ -235,44 +235,44 @@

// Everything is sideways.
if (cur.width > cur.height) {
                            e.gc.setForeground(paddingColor);
                            e.gc.drawRectangle(leftOffset + width - nodeTop - topShift - nodeHeight
+ nodePadBottom,
topOffset + leftShift + nodeLeft + nodePadLeft, nodeHeight
- nodePadBottom - nodePadTop, nodeWidth - nodePadRight
- nodePadLeft);
                            e.gc.setForeground(marginColor);
                            e.gc.drawRectangle(leftOffset + width - nodeTop - topShift - nodeHeight
- nodeMarginBottom, topOffset + leftShift + nodeLeft
- nodeMarginLeft,
nodeHeight + nodeMarginBottom + nodeMarginTop, nodeWidth
+ nodeMarginRight + nodeMarginLeft);
                            e.gc.setForeground(borderColor);
e.gc.drawRectangle(
                                    leftOffset + width - nodeTop - topShift - nodeHeight, topOffset
+ leftShift + nodeLeft, nodeHeight, nodeWidth);
} else {
                            e.gc.setForeground(paddingColor);
e.gc.drawRectangle(leftOffset + leftShift + nodeLeft + nodePadLeft,
topOffset + topShift + nodeTop + nodePadTop, nodeWidth
- nodePadRight - nodePadLeft, nodeHeight
- nodePadBottom - nodePadTop);
                            e.gc.setForeground(marginColor);
e.gc.drawRectangle(leftOffset + leftShift + nodeLeft - nodeMarginLeft,
topOffset + topShift + nodeTop - nodeMarginTop, nodeWidth
+ nodeMarginRight + nodeMarginLeft, nodeHeight
+ nodeMarginBottom + nodeMarginTop);
                            e.gc.setForeground(borderColor);
e.gc.drawRectangle(leftOffset + leftShift + nodeLeft, topOffset
+ topShift + nodeTop, nodeWidth, nodeHeight);
}
}
                    if (crosshairLocation != null) {
                        e.gc.setForeground(crosshairColor);
                        e.gc.drawLine(leftOffset, topOffset + crosshairLocation.y, leftOffset
                                + width - 1, topOffset + crosshairLocation.y);
                        e.gc.drawLine(leftOffset + crosshairLocation.x, topOffset, leftOffset
                                + crosshairLocation.x, topOffset + height - 1);
}
}
}
//Synthetic comment -- @@ -282,21 +282,21 @@
private void doRedraw() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                canvas.redraw();
}
});
}

private void loadImage() {
        image = model.getImage();
        if (image != null) {
            width = image.getBounds().width;
            height = image.getBounds().height;
} else {
            width = 0;
            height = 0;
}
        setMinSize(width, height);
}

public void imageLoaded() {
//Synthetic comment -- @@ -304,10 +304,10 @@
public void run() {
synchronized (this) {
loadImage();
                    crosshairLocation = model.getCrosshairLocation();
                    selectedNode = model.getSelected();
                    overlayImage = model.getOverlayImage();
                    overlayTransparency = model.getOverlayTransparency();
}
}
});
//Synthetic comment -- @@ -327,14 +327,14 @@

public void crosshairMoved() {
synchronized (this) {
            crosshairLocation = model.getCrosshairLocation();
}
doRedraw();
}

public void selectionChanged() {
synchronized (this) {
            selectedNode = model.getSelected();
}
doRedraw();
}
//Synthetic comment -- @@ -344,7 +344,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    selectedNode = model.getSelected();
}
}
});
//Synthetic comment -- @@ -357,15 +357,15 @@

public void overlayChanged() {
synchronized (this) {
            overlayImage = model.getOverlayImage();
            overlayTransparency = model.getOverlayTransparency();
}
doRedraw();
}

public void overlayTransparencyChanged() {
synchronized (this) {
            overlayTransparency = model.getOverlayTransparency();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectControls.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectControls.java
//Synthetic comment -- index 5a593f6..3114d34 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -34,13 +34,13 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

public class PixelPerfectControls extends Composite implements ImageChangeListener {

    private Slider overlaySlider;

    private Slider zoomSlider;

    private Slider autoRefreshSlider;

public PixelPerfectControls(Composite parent) {
super(parent, SWT.NONE);
//Synthetic comment -- @@ -114,56 +114,56 @@
zoomLeftData.left = new FormAttachment(zoom, 2);
zoomLeft.setLayoutData(zoomLeftData);

        overlaySlider = new Slider(this, SWT.HORIZONTAL);
        overlaySlider.setMinimum(0);
        overlaySlider.setMaximum(101);
        overlaySlider.setThumb(1);
        overlaySlider.setSelection((int) Math.round(PixelPerfectModel.getModel()
.getOverlayTransparency() * 100));

Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
        overlaySlider.setEnabled(overlayImage != null);
FormData overlaySliderData = new FormData();
overlaySliderData.right = new FormAttachment(overlayTransparencyRight, -4);
overlaySliderData.top = new FormAttachment(0, 2);
overlaySliderData.left = new FormAttachment(overlayTransparencyLeft, 4);
        overlaySlider.setLayoutData(overlaySliderData);

        overlaySlider.addSelectionListener(overlaySliderSelectionListener);

        autoRefreshSlider = new Slider(this, SWT.HORIZONTAL);
        autoRefreshSlider.setMinimum(1);
        autoRefreshSlider.setMaximum(41);
        autoRefreshSlider.setThumb(1);
        autoRefreshSlider.setSelection(HierarchyViewerDirector.getDirector()
.getPixelPerfectAutoRefreshInverval());
FormData refreshSliderData = new FormData();
refreshSliderData.right = new FormAttachment(overlayTransparencyRight, -4);
refreshSliderData.top = new FormAttachment(overlayTransparencyRight, 2);
        refreshSliderData.left = new FormAttachment(overlaySlider, 0, SWT.LEFT);
        autoRefreshSlider.setLayoutData(refreshSliderData);

        autoRefreshSlider.addSelectionListener(refreshSliderSelectionListener);

        zoomSlider = new Slider(this, SWT.HORIZONTAL);
        zoomSlider.setMinimum(2);
        zoomSlider.setMaximum(25);
        zoomSlider.setThumb(1);
        zoomSlider.setSelection(PixelPerfectModel.getModel().getZoom());
FormData zoomSliderData = new FormData();
zoomSliderData.right = new FormAttachment(overlayTransparencyRight, -4);
zoomSliderData.top = new FormAttachment(refreshRight, 2);
        zoomSliderData.left = new FormAttachment(overlaySlider, 0, SWT.LEFT);
        zoomSlider.setLayoutData(zoomSliderData);

        zoomSlider.addSelectionListener(zoomSliderSelectionListener);

        addDisposeListener(disposeListener);

PixelPerfectModel.getModel().addImageChangeListener(this);
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
PixelPerfectModel.getModel().removeImageChangeListener(PixelPerfectControls.this);
}
//Synthetic comment -- @@ -177,7 +177,7 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = overlaySlider.getSelection();
if (oldValue != newValue) {
PixelPerfectModel.getModel().removeImageChangeListener(PixelPerfectControls.this);
PixelPerfectModel.getModel().setOverlayTransparency(newValue / 100.0);
//Synthetic comment -- @@ -187,7 +187,7 @@
}
};

    private SelectionListener refreshSliderSelectionListener = new SelectionListener() {
private int oldValue;

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -195,14 +195,14 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = autoRefreshSlider.getSelection();
if (oldValue != newValue) {
HierarchyViewerDirector.getDirector().setPixelPerfectAutoRefreshInterval(newValue);
}
}
};

    private SelectionListener zoomSliderSelectionListener = new SelectionListener() {
private int oldValue;

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -210,7 +210,7 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = zoomSlider.getSelection();
if (oldValue != newValue) {
PixelPerfectModel.getModel().removeImageChangeListener(PixelPerfectControls.this);
PixelPerfectModel.getModel().setZoom(newValue);
//Synthetic comment -- @@ -236,10 +236,10 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
                overlaySlider.setEnabled(overlayImage != null);
if (PixelPerfectModel.getModel().getImage() == null) {
} else {
                    zoomSlider.setSelection(PixelPerfectModel.getModel().getZoom());
}
}
});
//Synthetic comment -- @@ -249,7 +249,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
Image overlayImage = PixelPerfectModel.getModel().getOverlayImage();
                overlaySlider.setEnabled(overlayImage != null);
}
});
}
//Synthetic comment -- @@ -257,7 +257,7 @@
public void overlayTransparencyChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                overlaySlider.setSelection((int) (PixelPerfectModel.getModel()
.getOverlayTransparency() * 100));
}
});
//Synthetic comment -- @@ -270,7 +270,7 @@
public void zoomChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                zoomSlider.setSelection(PixelPerfectModel.getModel().getZoom());
}
});
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java
//Synthetic comment -- index 53afc9e..129dc4d 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.hierarchyviewerlib.ui;

import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -42,72 +42,72 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectLoupe extends Canvas implements ImageChangeListener {
    private PixelPerfectModel model;

    private Image image;

    private Image grid;

    private Color crosshairColor;

    private int width;

    private int height;

    private Point crosshairLocation;

    private int zoom;

    private Transform transform;

    private int canvasWidth;

    private int canvasHeight;

    private Image overlayImage;

    private double overlayTransparency;

    private boolean showOverlay = false;

public PixelPerfectLoupe(Composite parent) {
super(parent, SWT.NONE);
        model = PixelPerfectModel.getModel();
        model.addImageChangeListener(this);

        addPaintListener(paintListener);
        addMouseListener(mouseListener);
        addMouseWheelListener(mouseWheelListener);
        addDisposeListener(disposeListener);
        addKeyListener(keyListener);

        crosshairColor = new Color(Display.getDefault(), new RGB(255, 94, 254));

        transform = new Transform(Display.getDefault());

imageLoaded();
}

public void setShowOverlay(boolean value) {
synchronized (this) {
            showOverlay = value;
}
doRedraw();
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeImageChangeListener(PixelPerfectLoupe.this);
            crosshairColor.dispose();
            transform.dispose();
            if (grid != null) {
                grid.dispose();
}
}
};

    private MouseListener mouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
// pass
//Synthetic comment -- @@ -123,20 +123,20 @@

};

    private MouseWheelListener mouseWheelListener = new MouseWheelListener() {
public void mouseScrolled(MouseEvent e) {
int newZoom = -1;
synchronized (PixelPerfectLoupe.this) {
                if (image != null && crosshairLocation != null) {
if (e.count > 0) {
                        newZoom = zoom + 1;
} else {
                        newZoom = zoom - 1;
}
}
}
if (newZoom != -1) {
                model.setZoom(newZoom);
}
}
};
//Synthetic comment -- @@ -145,51 +145,51 @@
int newX = -1;
int newY = -1;
synchronized (PixelPerfectLoupe.this) {
            if (image == null) {
return;
}
            int zoomedX = -crosshairLocation.x * zoom - zoom / 2 + getBounds().width / 2;
            int zoomedY = -crosshairLocation.y * zoom - zoom / 2 + getBounds().height / 2;
            int x = (e.x - zoomedX) / zoom;
            int y = (e.y - zoomedY) / zoom;
            if (x >= 0 && x < width && y >= 0 && y < height) {
newX = x;
newY = y;
}
}
if (newX != -1) {
            model.setCrosshairLocation(newX, newY);
}
}

    private KeyListener keyListener = new KeyListener() {

public void keyPressed(KeyEvent e) {
boolean crosshairMoved = false;
synchronized (PixelPerfectLoupe.this) {
                if (image != null) {
switch (e.keyCode) {
case SWT.ARROW_UP:
                            if (crosshairLocation.y != 0) {
                                crosshairLocation.y--;
crosshairMoved = true;
}
break;
case SWT.ARROW_DOWN:
                            if (crosshairLocation.y != height - 1) {
                                crosshairLocation.y++;
crosshairMoved = true;
}
break;
case SWT.ARROW_LEFT:
                            if (crosshairLocation.x != 0) {
                                crosshairLocation.x--;
crosshairMoved = true;
}
break;
case SWT.ARROW_RIGHT:
                            if (crosshairLocation.x != width - 1) {
                                crosshairLocation.x++;
crosshairMoved = true;
}
break;
//Synthetic comment -- @@ -197,7 +197,7 @@
}
}
if (crosshairMoved) {
                model.setCrosshairLocation(crosshairLocation.x, crosshairLocation.y);
}
}

//Synthetic comment -- @@ -207,70 +207,69 @@

};

    private PaintListener paintListener = new PaintListener() {
        @SuppressWarnings("deprecation")
public void paintControl(PaintEvent e) {
synchronized (PixelPerfectLoupe.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getSize().x, getSize().y);
                if (image != null && crosshairLocation != null) {
                    int zoomedX = -crosshairLocation.x * zoom - zoom / 2 + getBounds().width / 2;
                    int zoomedY = -crosshairLocation.y * zoom - zoom / 2 + getBounds().height / 2;
                    transform.translate(zoomedX, zoomedY);
                    transform.scale(zoom, zoom);
e.gc.setInterpolation(SWT.NONE);
                    e.gc.setTransform(transform);
                    e.gc.drawImage(image, 0, 0);
                    if (showOverlay && overlayImage != null) {
                        e.gc.setAlpha((int) (overlayTransparency * 255));
                        e.gc.drawImage(overlayImage, 0, height - overlayImage.getBounds().height);
e.gc.setAlpha(255);
}

                    transform.identity();
                    e.gc.setTransform(transform);

// If the size of the canvas has changed, we need to make
// another grid.
                    if (grid != null
                            && (canvasWidth != getBounds().width || canvasHeight != getBounds().height)) {
                        grid.dispose();
                        grid = null;
}
                    canvasWidth = getBounds().width;
                    canvasHeight = getBounds().height;
                    if (grid == null) {
// Make a transparent image;
ImageData imageData =
                                new ImageData(canvasWidth + zoom + 1, canvasHeight + zoom + 1, 1,
new PaletteData(new RGB[] {
new RGB(0, 0, 0)
}));
imageData.transparentPixel = 0;

// Draw the grid.
                        grid = new Image(Display.getDefault(), imageData);
                        GC gc = new GC(grid);
gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                        for (int x = 0; x <= canvasWidth + zoom; x += zoom) {
                            gc.drawLine(x, 0, x, canvasHeight + zoom);
}
                        for (int y = 0; y <= canvasHeight + zoom; y += zoom) {
                            gc.drawLine(0, y, canvasWidth + zoom, y);
}
gc.dispose();
}

                    e.gc.setClipping(new Rectangle(zoomedX, zoomedY, width * zoom + 1, height
                            * zoom + 1));
e.gc.setAlpha(76);
                    e.gc.drawImage(grid, (canvasWidth / 2 - zoom / 2) % zoom - zoom,
                            (canvasHeight / 2 - zoom / 2) % zoom - zoom);
e.gc.setAlpha(255);

                    e.gc.setForeground(crosshairColor);
                    e.gc.drawLine(0, canvasHeight / 2, canvasWidth - 1, canvasHeight / 2);
                    e.gc.drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight - 1);
}
}
}
//Synthetic comment -- @@ -285,13 +284,13 @@
}

private void loadImage() {
        image = model.getImage();
        if (image != null) {
            width = image.getBounds().width;
            height = image.getBounds().height;
} else {
            width = 0;
            height = 0;
}
}

//Synthetic comment -- @@ -301,10 +300,10 @@
public void run() {
synchronized (this) {
loadImage();
                    crosshairLocation = model.getCrosshairLocation();
                    zoom = model.getZoom();
                    overlayImage = model.getOverlayImage();
                    overlayTransparency = model.getOverlayTransparency();
}
}
});
//Synthetic comment -- @@ -324,7 +323,7 @@

public void crosshairMoved() {
synchronized (this) {
            crosshairLocation = model.getCrosshairLocation();
}
doRedraw();
}
//Synthetic comment -- @@ -341,14 +340,14 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    if (grid != null) {
// To notify that the zoom level has changed, we get rid
// of the
// grid.
                        grid.dispose();
                        grid = null;
}
                    zoom = model.getZoom();
}
}
});
//Synthetic comment -- @@ -357,15 +356,15 @@

public void overlayChanged() {
synchronized (this) {
            overlayImage = model.getOverlayImage();
            overlayTransparency = model.getOverlayTransparency();
}
doRedraw();
}

public void overlayTransparencyChanged() {
synchronized (this) {
            overlayTransparency = model.getOverlayTransparency();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectPixelPanel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectPixelPanel.java
//Synthetic comment -- index afe3dc8..6680523 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.hierarchyviewerlib.ui;

import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -32,14 +32,14 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectPixelPanel extends Canvas implements ImageChangeListener {
    private PixelPerfectModel model;

    private Image image;

    private Image overlayImage;

    private Point crosshairLocation;

public static final int PREFERRED_WIDTH = 180;

//Synthetic comment -- @@ -47,11 +47,11 @@

public PixelPerfectPixelPanel(Composite parent) {
super(parent, SWT.NONE);
        model = PixelPerfectModel.getModel();
        model.addImageChangeListener(this);

        addPaintListener(paintListener);
        addDisposeListener(disposeListener);

imageLoaded();
}
//Synthetic comment -- @@ -63,21 +63,21 @@
return new Point(width, height);
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeImageChangeListener(PixelPerfectPixelPanel.this);
}
};

    private PaintListener paintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (PixelPerfectPixelPanel.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                if (image != null) {
RGB pixel =
                            image.getImageData().palette.getRGB(image.getImageData().getPixel(
                                    crosshairLocation.x, crosshairLocation.y));
Color rgbColor = new Color(Display.getDefault(), pixel);
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
e.gc.setBackground(rgbColor);
//Synthetic comment -- @@ -97,19 +97,19 @@
e.gc.drawText(Integer.toString(pixel.blue), 97, 35, true);
e.gc.drawText("X:", 132, 4, true);
e.gc.drawText("Y:", 132, 20, true);
                    e.gc.drawText(Integer.toString(crosshairLocation.x) + " px", 149, 4, true);
                    e.gc.drawText(Integer.toString(crosshairLocation.y) + " px", 149, 20, true);

                    if (overlayImage != null) {
                        int xInOverlay = crosshairLocation.x;
int yInOverlay =
                                crosshairLocation.y
                                        - (image.getBounds().height - overlayImage.getBounds().height);
if (xInOverlay >= 0 && yInOverlay >= 0
                                && xInOverlay < overlayImage.getBounds().width
                                && yInOverlay < overlayImage.getBounds().height) {
pixel =
                                    overlayImage.getImageData().palette.getRGB(overlayImage
.getImageData().getPixel(xInOverlay, yInOverlay));
rgbColor = new Color(Display.getDefault(), pixel);
e.gc
//Synthetic comment -- @@ -146,30 +146,30 @@

public void crosshairMoved() {
synchronized (this) {
            crosshairLocation = model.getCrosshairLocation();
}
doRedraw();
}

public void imageChanged() {
synchronized (this) {
            image = model.getImage();
}
doRedraw();
}

public void imageLoaded() {
synchronized (this) {
            image = model.getImage();
            crosshairLocation = model.getCrosshairLocation();
            overlayImage = model.getOverlayImage();
}
doRedraw();
}

public void overlayChanged() {
synchronized (this) {
            overlayImage = model.getOverlayImage();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectTree.java
//Synthetic comment -- index d34dcf2..da7cd62 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
//Synthetic comment -- @@ -40,17 +40,17 @@

import java.util.List;

public class PixelPerfectTree extends Composite implements ImageChangeListener, SelectionListener {

    private TreeViewer treeViewer;

    private Tree tree;

    private PixelPerfectModel model;

    private Image folderImage;

    private Image fileImage;

private class ContentProvider implements ITreeContentProvider, ILabelProvider {
public Object[] getChildren(Object element) {
//Synthetic comment -- @@ -99,9 +99,9 @@
public Image getImage(Object element) {
if (element instanceof ViewNode) {
if (hasChildren(element)) {
                    return folderImage;
}
                return fileImage;
}
return null;
}
//Synthetic comment -- @@ -130,47 +130,47 @@
public PixelPerfectTree(Composite parent) {
super(parent, SWT.NONE);
setLayout(new FillLayout());
        treeViewer = new TreeViewer(this, SWT.SINGLE);
        treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

        tree = treeViewer.getTree();
        tree.addSelectionListener(this);

loadResources();

        addDisposeListener(disposeListener);

        model = PixelPerfectModel.getModel();
ContentProvider contentProvider = new ContentProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(contentProvider);
        treeViewer.setInput(model);
        model.addImageChangeListener(this);

}

private void loadResources() {
ImageLoader loader = ImageLoader.getDdmUiLibLoader();
        fileImage = loader.loadImage("file.png", Display.getDefault());
        folderImage = loader.loadImage("folder.png", Display.getDefault());
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeImageChangeListener(PixelPerfectTree.this);
}
};

@Override
public boolean setFocus() {
        return tree.setFocus();
}

public void imageLoaded() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                treeViewer.refresh();
                treeViewer.expandAll();
}
});
}
//Synthetic comment -- @@ -197,10 +197,10 @@

public void widgetSelected(SelectionEvent e) {
// To combat phantom selection...
        if (((TreeSelection) treeViewer.getSelection()).isEmpty()) {
            model.setSelected(null);
} else {
            model.setSelected((ViewNode) e.item.getData());
}
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java
//Synthetic comment -- index 14068a3..4396a1f 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.device.ViewNode.Property;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.TreeColumnResizer;

//Synthetic comment -- @@ -45,25 +45,25 @@

import java.util.ArrayList;

public class PropertyViewer extends Composite implements TreeChangeListener {
    private TreeViewModel model;

    private TreeViewer treeViewer;

    private Tree tree;

    private DrawableViewNode selectedNode;

    private Font smallFont;

private class ContentProvider implements ITreeContentProvider, ITableLabelProvider {

public Object[] getChildren(Object parentElement) {
synchronized (PropertyViewer.this) {
                if (selectedNode != null && parentElement instanceof String) {
String category = (String) parentElement;
ArrayList<Property> returnValue = new ArrayList<Property>();
                    for (Property property : selectedNode.viewNode.properties) {
if (category.equals(ViewNode.MISCELLANIOUS)) {
if (property.name.indexOf(':') == -1) {
returnValue.add(property);
//Synthetic comment -- @@ -82,8 +82,8 @@

public Object getParent(Object element) {
synchronized (PropertyViewer.this) {
                if (selectedNode != null && element instanceof Property) {
                    if (selectedNode.viewNode.categories.size() == 0) {
return null;
}
String name = ((Property) element).name;
//Synthetic comment -- @@ -99,9 +99,9 @@

public boolean hasChildren(Object element) {
synchronized (PropertyViewer.this) {
                if (selectedNode != null && element instanceof String) {
String category = (String) element;
                    for (String name : selectedNode.viewNode.namedProperties.keySet()) {
if (category.equals(ViewNode.MISCELLANIOUS)) {
if (name.indexOf(':') == -1) {
return true;
//Synthetic comment -- @@ -119,13 +119,13 @@

public Object[] getElements(Object inputElement) {
synchronized (PropertyViewer.this) {
                if (selectedNode != null && inputElement instanceof TreeViewModel) {
                    if (selectedNode.viewNode.categories.size() == 0) {
                        return selectedNode.viewNode.properties
                                .toArray(new Property[selectedNode.viewNode.properties.size()]);
} else {
                        return selectedNode.viewNode.categories
                                .toArray(new String[selectedNode.viewNode.categories.size()]);
}
}
return new Object[0];
//Synthetic comment -- @@ -146,7 +146,7 @@

public String getColumnText(Object element, int column) {
synchronized (PropertyViewer.this) {
                if (selectedNode != null) {
if (element instanceof String && column == 0) {
String category = (String) element;
return Character.toUpperCase(category.charAt(0)) + category.substring(1);
//Synthetic comment -- @@ -184,32 +184,32 @@
public PropertyViewer(Composite parent) {
super(parent, SWT.NONE);
setLayout(new FillLayout());
        treeViewer = new TreeViewer(this, SWT.NONE);

        tree = treeViewer.getTree();
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);

        TreeColumn propertyColumn = new TreeColumn(tree, SWT.NONE);
propertyColumn.setText("Property");
        TreeColumn valueColumn = new TreeColumn(tree, SWT.NONE);
valueColumn.setText("Value");

        model = TreeViewModel.getModel();
ContentProvider contentProvider = new ContentProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(contentProvider);
        treeViewer.setInput(model);
        model.addTreeChangeListener(this);

loadResources();
        addDisposeListener(disposeListener);

        tree.setFont(smallFont);

new TreeColumnResizer(this, propertyColumn, valueColumn);

        addControlListener(controlListener);

treeChanged();
}
//Synthetic comment -- @@ -222,20 +222,20 @@
for (int i = 0; i < fontData.length; i++) {
newFontData[i] = new FontData(fontData[i].getName(), 8, fontData[i].getStyle());
}
        smallFont = new Font(Display.getDefault(), newFontData);
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeTreeChangeListener(PropertyViewer.this);
            smallFont.dispose();
}
};

// If the window gets too small, hide the data, otherwise SWT throws an
// ERROR.

    private ControlListener controlListener = new ControlAdapter() {
private boolean noInput = false;

private boolean noHeader = false;
//Synthetic comment -- @@ -243,17 +243,17 @@
@Override
public void controlResized(ControlEvent e) {
if (getBounds().height <= 20) {
                tree.setHeaderVisible(false);
noHeader = true;
} else if (noHeader) {
                tree.setHeaderVisible(true);
noHeader = false;
}
if (getBounds().height <= 38) {
                treeViewer.setInput(null);
noInput = true;
} else if (noInput) {
                treeViewer.setInput(model);
noInput = false;
}
}
//Synthetic comment -- @@ -261,14 +261,14 @@

public void selectionChanged() {
synchronized (this) {
            selectedNode = model.getSelection();
}
doRefresh();
}

public void treeChanged() {
synchronized (this) {
            selectedNode = model.getSelection();
}
doRefresh();
}
//Synthetic comment -- @@ -284,7 +284,7 @@
private void doRefresh() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                treeViewer.refresh();
}
});
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index ea53ee4..3b90629 100644

//Synthetic comment -- @@ -16,12 +16,11 @@

package com.android.hierarchyviewerlib.ui;

import com.android.ddmlib.Log;
import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.device.ViewNode.ProfileRating;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Rectangle;
//Synthetic comment -- @@ -53,67 +52,67 @@

import java.text.DecimalFormat;

public class TreeView extends Canvas implements TreeChangeListener {

    private TreeViewModel model;

    private DrawableViewNode tree;

    private DrawableViewNode selectedNode;

    private Rectangle viewport;

    private Transform transform;

    private Transform inverse;

    private double zoom;

    private Point lastPoint;

    private boolean alreadySelectedOnMouseDown;

    private boolean doubleClicked;

    private boolean nodeMoved;

    private DrawableViewNode draggedNode;

public static final int LINE_PADDING = 10;

public static final float BEZIER_FRACTION = 0.35f;

    private static Image redImage;

    private static Image yellowImage;

    private static Image greenImage;

    private static Image notSelectedImage;

    private static Image selectedImage;

    private static Image filteredImage;

    private static Image filteredSelectedImage;

    private static Font systemFont;

    private Color boxColor;

    private Color textBackgroundColor;

    private Rectangle selectedRectangleLocation;

    private Point buttonCenter;

private static final int BUTTON_SIZE = 13;

    private Image scaledSelectedImage;

    private boolean buttonClicked;

    private DrawableViewNode lastDrawnSelectedViewNode;

// The profile-image box needs to be moved to,
// so add some dragging leeway.
//Synthetic comment -- @@ -149,87 +148,87 @@
public TreeView(Composite parent) {
super(parent, SWT.NONE);

        model = TreeViewModel.getModel();
        model.addTreeChangeListener(this);

        addPaintListener(paintListener);
        addMouseListener(mouseListener);
        addMouseMoveListener(mouseMoveListener);
        addMouseWheelListener(mouseWheelListener);
        addListener(SWT.Resize, resizeListener);
        addDisposeListener(disposeListener);
        addKeyListener(keyListener);

loadResources();

        transform = new Transform(Display.getDefault());
        inverse = new Transform(Display.getDefault());

loadAllData();
}

private void loadResources() {
ImageLoader loader = ImageLoader.getLoader(this.getClass());
        redImage = loader.loadImage("red.png", Display.getDefault());
        yellowImage = loader.loadImage("yellow.png", Display.getDefault());
        greenImage = loader.loadImage("green.png", Display.getDefault());
        notSelectedImage = loader.loadImage("not-selected.png", Display.getDefault());
        selectedImage = loader.loadImage("selected.png", Display.getDefault());
        filteredImage = loader.loadImage("filtered.png", Display.getDefault());
        filteredSelectedImage = loader.loadImage("selected-filtered.png", Display.getDefault());
        boxColor = new Color(Display.getDefault(), new RGB(225, 225, 225));
        textBackgroundColor = new Color(Display.getDefault(), new RGB(82, 82, 82));
        if (scaledSelectedImage != null) {
            scaledSelectedImage.dispose();
}
        systemFont = Display.getDefault().getSystemFont();
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeTreeChangeListener(TreeView.this);
            transform.dispose();
            inverse.dispose();
            boxColor.dispose();
            textBackgroundColor.dispose();
            if (tree != null) {
                model.setViewport(null);
}
}
};

    private Listener resizeListener = new Listener() {
public void handleEvent(Event e) {
synchronized (TreeView.this) {
                if (tree != null && viewport != null) {

// Keep the center in the same place.
Point viewCenter =
                            new Point(viewport.x + viewport.width / 2, viewport.y + viewport.height
/ 2);
                    viewport.width = getBounds().width / zoom;
                    viewport.height = getBounds().height / zoom;
                    viewport.x = viewCenter.x - viewport.width / 2;
                    viewport.y = viewCenter.y - viewport.height / 2;
}
}
            if (viewport != null) {
                model.setViewport(viewport);
}
}
};

    private KeyListener keyListener = new KeyListener() {

public void keyPressed(KeyEvent e) {
boolean selectionChanged = false;
DrawableViewNode clickedNode = null;
synchronized (TreeView.this) {
                if (tree != null && viewport != null && selectedNode != null) {
switch (e.keyCode) {
case SWT.ARROW_LEFT:
                            if (selectedNode.parent != null) {
                                selectedNode = selectedNode.parent;
selectionChanged = true;
}
break;
//Synthetic comment -- @@ -238,7 +237,7 @@
// On up and down, it is cool to go up and down only
// the leaf nodes.
// It goes well with the layout viewer
                            DrawableViewNode currentNode = selectedNode;
while (currentNode.parent != null && currentNode.viewNode.index == 0) {
currentNode = currentNode.parent;
}
//Synthetic comment -- @@ -254,11 +253,11 @@
}
}
if (selectionChanged) {
                                selectedNode = currentNode;
}
break;
case SWT.ARROW_DOWN:
                            currentNode = selectedNode;
while (currentNode.parent != null
&& currentNode.viewNode.index + 1 == currentNode.parent.children
.size()) {
//Synthetic comment -- @@ -274,45 +273,45 @@
}
}
if (selectionChanged) {
                                selectedNode = currentNode;
}
break;
case SWT.ARROW_RIGHT:
DrawableViewNode rightNode = null;
double mostOverlap = 0;
                            final int N = selectedNode.children.size();

// We consider all the children and pick the one
// who's tree overlaps the most.
for (int i = 0; i < N; i++) {
                                DrawableViewNode child = selectedNode.children.get(i);
DrawableViewNode topMostChild = child;
while (topMostChild.children.size() != 0) {
topMostChild = topMostChild.children.get(0);
}
double overlap =
Math.min(DrawableViewNode.NODE_HEIGHT, Math.min(
                                                selectedNode.top + DrawableViewNode.NODE_HEIGHT
- topMostChild.top, topMostChild.top
                                                        + child.treeHeight - selectedNode.top));
if (overlap > mostOverlap) {
mostOverlap = overlap;
rightNode = child;
}
}
if (rightNode != null) {
                                selectedNode = rightNode;
selectionChanged = true;
}
break;
case SWT.CR:
                            clickedNode = selectedNode;
break;
}
}
}
if (selectionChanged) {
                model.setSelection(selectedNode);
}
if (clickedNode != null) {
HierarchyViewerDirector.getDirector().showCapture(getShell(), clickedNode.viewNode);
//Synthetic comment -- @@ -323,71 +322,72 @@
}
};

    private MouseListener mouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
DrawableViewNode clickedNode = null;
synchronized (TreeView.this) {
                if (tree != null && viewport != null) {
Point pt = transformPoint(e.x, e.y);
                    clickedNode = tree.getSelected(pt.x, pt.y);
}
}
if (clickedNode != null) {
HierarchyViewerDirector.getDirector().showCapture(getShell(), clickedNode.viewNode);
                doubleClicked = true;
}
}

public void mouseDown(MouseEvent e) {
boolean selectionChanged = false;
synchronized (TreeView.this) {
                if (tree != null && viewport != null) {
Point pt = transformPoint(e.x, e.y);

// Ignore profiling rectangle, except for...
                    if (selectedRectangleLocation != null
                            && pt.x >= selectedRectangleLocation.x
                            && pt.x < selectedRectangleLocation.x + selectedRectangleLocation.width
                            && pt.y >= selectedRectangleLocation.y
                            && pt.y < selectedRectangleLocation.y
                                    + selectedRectangleLocation.height) {

// the small button!
                        if ((pt.x - buttonCenter.x) * (pt.x - buttonCenter.x)
                                + (pt.y - buttonCenter.y) * (pt.y - buttonCenter.y) <= (BUTTON_SIZE * BUTTON_SIZE) / 4) {
                            buttonClicked = true;
doRedraw();
}
return;
}
                    draggedNode = tree.getSelected(pt.x, pt.y);

// Update the selection.
                    if (draggedNode != null && draggedNode != selectedNode) {
                        selectedNode = draggedNode;
selectionChanged = true;
                        alreadySelectedOnMouseDown = false;
                    } else if (draggedNode != null) {
                        alreadySelectedOnMouseDown = true;
}

// Can't drag the root.
                    if (draggedNode == tree) {
                        draggedNode = null;
}

                    if (draggedNode != null) {
                        lastPoint = pt;
} else {
                        lastPoint = new Point(e.x, e.y);
}
                    nodeMoved = false;
                    doubleClicked = false;
}
}
if (selectionChanged) {
                model.setSelection(selectedNode);
}
}

//Synthetic comment -- @@ -397,8 +397,8 @@
boolean viewportChanged = false;
boolean selectionChanged = false;
synchronized (TreeView.this) {
                if (tree != null && viewport != null && lastPoint != null) {
                    if (draggedNode == null) {
// The viewport moves.
handleMouseDrag(new Point(e.x, e.y));
viewportChanged = true;
//Synthetic comment -- @@ -412,36 +412,36 @@
// double click event.
// During a double click, we don't want to deselect.
Point pt = transformPoint(e.x, e.y);
                    DrawableViewNode mouseUpOn = tree.getSelected(pt.x, pt.y);
                    if (mouseUpOn != null && mouseUpOn == selectedNode
                            && alreadySelectedOnMouseDown && !nodeMoved && !doubleClicked) {
                        selectedNode = null;
selectionChanged = true;
}
                    lastPoint = null;
                    draggedNode = null;
redraw = true;
}

// Just clicked the button here.
                if (buttonClicked) {
HierarchyViewerDirector.getDirector().showCapture(getShell(),
                            selectedNode.viewNode);
                    buttonClicked = false;
redrawButton = true;
}
}

// Complicated.
if (viewportChanged) {
                model.setViewport(viewport);
} else if (redraw) {
                model.removeTreeChangeListener(TreeView.this);
                model.notifyViewportChanged();
if (selectionChanged) {
                    model.setSelection(selectedNode);
}
                model.addTreeChangeListener(TreeView.this);
doRedraw();
} else if (redrawButton) {
doRedraw();
//Synthetic comment -- @@ -450,13 +450,13 @@

};

    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
public void mouseMove(MouseEvent e) {
boolean redraw = false;
boolean viewportChanged = false;
synchronized (TreeView.this) {
                if (tree != null && viewport != null && lastPoint != null) {
                    if (draggedNode == null) {
handleMouseDrag(new Point(e.x, e.y));
viewportChanged = true;
} else {
//Synthetic comment -- @@ -466,11 +466,11 @@
}
}
if (viewportChanged) {
                model.setViewport(viewport);
} else if (redraw) {
                model.removeTreeChangeListener(TreeView.this);
                model.notifyViewportChanged();
                model.addTreeChangeListener(TreeView.this);
doRedraw();
}
}
//Synthetic comment -- @@ -479,102 +479,102 @@
private void handleMouseDrag(Point pt) {

// Case 1: a node is dragged. DrawableViewNode knows how to handle this.
        if (draggedNode != null) {
            if (lastPoint.y - pt.y != 0) {
                nodeMoved = true;
}
            draggedNode.move(lastPoint.y - pt.y);
            lastPoint = pt;
return;
}

// Case 2: the viewport is dragged. We have to make sure we respect the
// bounds - don't let the user drag way out... + some leeway for the
// profiling box.
        double xDif = (lastPoint.x - pt.x) / zoom;
        double yDif = (lastPoint.y - pt.y) / zoom;

        double treeX = tree.bounds.x - DRAG_LEEWAY;
        double treeY = tree.bounds.y - DRAG_LEEWAY;
        double treeWidth = tree.bounds.width + 2 * DRAG_LEEWAY;
        double treeHeight = tree.bounds.height + 2 * DRAG_LEEWAY;

        if (viewport.width > treeWidth) {
            if (xDif < 0 && viewport.x + viewport.width > treeX + treeWidth) {
                viewport.x = Math.max(viewport.x + xDif, treeX + treeWidth - viewport.width);
            } else if (xDif > 0 && viewport.x < treeX) {
                viewport.x = Math.min(viewport.x + xDif, treeX);
}
} else {
            if (xDif < 0 && viewport.x > treeX) {
                viewport.x = Math.max(viewport.x + xDif, treeX);
            } else if (xDif > 0 && viewport.x + viewport.width < treeX + treeWidth) {
                viewport.x = Math.min(viewport.x + xDif, treeX + treeWidth - viewport.width);
}
}
        if (viewport.height > treeHeight) {
            if (yDif < 0 && viewport.y + viewport.height > treeY + treeHeight) {
                viewport.y = Math.max(viewport.y + yDif, treeY + treeHeight - viewport.height);
            } else if (yDif > 0 && viewport.y < treeY) {
                viewport.y = Math.min(viewport.y + yDif, treeY);
}
} else {
            if (yDif < 0 && viewport.y > treeY) {
                viewport.y = Math.max(viewport.y + yDif, treeY);
            } else if (yDif > 0 && viewport.y + viewport.height < treeY + treeHeight) {
                viewport.y = Math.min(viewport.y + yDif, treeY + treeHeight - viewport.height);
}
}
        lastPoint = pt;
}

private Point transformPoint(double x, double y) {
float[] pt = {
(float) x, (float) y
};
        inverse.transform(pt);
return new Point(pt[0], pt[1]);
}

    private MouseWheelListener mouseWheelListener = new MouseWheelListener() {
public void mouseScrolled(MouseEvent e) {
Point zoomPoint = null;
synchronized (TreeView.this) {
                if (tree != null && viewport != null) {
                    zoom += Math.ceil(e.count / 3.0) * 0.1;
zoomPoint = transformPoint(e.x, e.y);
}
}
if (zoomPoint != null) {
                model.zoomOnPoint(zoom, zoomPoint);
}
}
};

    private PaintListener paintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (TreeView.this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                if (tree != null && viewport != null) {

// Easy stuff!
                    e.gc.setTransform(transform);
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
Path connectionPath = new Path(Display.getDefault());
                    paintRecursive(e.gc, transform, tree, selectedNode, connectionPath);
e.gc.drawPath(connectionPath);
connectionPath.dispose();

// Draw the profiling box.
                    if (selectedNode != null) {

e.gc.setAlpha(200);

// Draw the little triangle
                        int x = selectedNode.left + DrawableViewNode.NODE_WIDTH / 2;
                        int y = (int) selectedNode.top + 4;
                        e.gc.setBackground(boxColor);
e.gc.fillPolygon(new int[] {
x, y, x - 11, y - 11, x + 11, y - 11
});
//Synthetic comment -- @@ -583,22 +583,22 @@
y -= 10 + RECT_HEIGHT;
e.gc.fillRoundRectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT, 30,
30);
                        selectedRectangleLocation =
new Rectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT);

e.gc.setAlpha(255);

// Draw the button
                        buttonCenter =
new Point(x - BUTTON_RIGHT_OFFSET + (RECT_WIDTH - BUTTON_SIZE) / 2,
y + BUTTON_TOP_OFFSET + BUTTON_SIZE / 2);

                        if (buttonClicked) {
e.gc
.setBackground(Display.getDefault().getSystemColor(
SWT.COLOR_BLACK));
} else {
                            e.gc.setBackground(textBackgroundColor);

}
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//Synthetic comment -- @@ -614,36 +614,36 @@
y += 15;

// If there is an image, draw it.
                        if (selectedNode.viewNode.image != null
                                && selectedNode.viewNode.image.getBounds().height != 1
                                && selectedNode.viewNode.image.getBounds().width != 1) {

// Scaling the image to the right size takes lots of
// time, so we want to do it only once.

// If the selection changed, get rid of the old
// image.
                            if (lastDrawnSelectedViewNode != selectedNode) {
                                if (scaledSelectedImage != null) {
                                    scaledSelectedImage.dispose();
                                    scaledSelectedImage = null;
}
                                lastDrawnSelectedViewNode = selectedNode;
}

                            if (scaledSelectedImage == null) {
double ratio =
                                        1.0 * selectedNode.viewNode.image.getBounds().width
                                                / selectedNode.viewNode.image.getBounds().height;
int newWidth, newHeight;
if (ratio > 1.0 * IMAGE_WIDTH / IMAGE_HEIGHT) {
newWidth =
                                            Math.min(IMAGE_WIDTH, selectedNode.viewNode.image
.getBounds().width);
newHeight = (int) (newWidth / ratio);
} else {
newHeight =
                                            Math.min(IMAGE_HEIGHT, selectedNode.viewNode.image
.getBounds().height);
newWidth = (int) (newHeight * ratio);
}
//Synthetic comment -- @@ -653,33 +653,34 @@
// resolution under zoom.
newWidth = Math.max(newWidth * 2, 1);
newHeight = Math.max(newHeight * 2, 1);
                                scaledSelectedImage =
new Image(Display.getDefault(), newWidth, newHeight);
                                GC gc = new GC(scaledSelectedImage);
                                gc.setBackground(textBackgroundColor);
gc.fillRectangle(0, 0, newWidth, newHeight);
                                gc.drawImage(selectedNode.viewNode.image, 0, 0,
                                        selectedNode.viewNode.image.getBounds().width,
                                        selectedNode.viewNode.image.getBounds().height, 0, 0,
newWidth, newHeight);
gc.dispose();
}

// Draw the background rectangle
                            e.gc.setBackground(textBackgroundColor);
                            e.gc.fillRoundRectangle(x - scaledSelectedImage.getBounds().width / 4
- IMAGE_OFFSET, y
                                    + (IMAGE_HEIGHT - scaledSelectedImage.getBounds().height / 2)
                                    / 2 - IMAGE_OFFSET, scaledSelectedImage.getBounds().width / 2
                                    + 2 * IMAGE_OFFSET, scaledSelectedImage.getBounds().height / 2
+ 2 * IMAGE_OFFSET, IMAGE_ROUNDING, IMAGE_ROUNDING);

// Under max zoom, we want the image to be
// untransformed. So, get back to the identity
// transform.
                            int imageX = x - scaledSelectedImage.getBounds().width / 4;
int imageY =
                                    y + (IMAGE_HEIGHT - scaledSelectedImage.getBounds().height / 2)
/ 2;

Transform untransformedTransform = new Transform(Display.getDefault());
//Synthetic comment -- @@ -687,15 +688,15 @@
float[] pt = new float[] {
imageX, imageY
};
                            transform.transform(pt);
                            e.gc.drawImage(scaledSelectedImage, 0, 0, scaledSelectedImage
                                    .getBounds().width, scaledSelectedImage.getBounds().height,
                                    (int) pt[0], (int) pt[1], (int) (scaledSelectedImage
.getBounds().width
                                            * zoom / 2),
                                    (int) (scaledSelectedImage.getBounds().height * zoom / 2));
untransformedTransform.dispose();
                            e.gc.setTransform(transform);
}

// Text stuff
//Synthetic comment -- @@ -706,24 +707,24 @@
e.gc.setFont(font);

String text =
                                selectedNode.viewNode.viewCount + " view"
                                        + (selectedNode.viewNode.viewCount != 1 ? "s" : "");
DecimalFormat formatter = new DecimalFormat("0.000");

String measureText =
"Measure: "
                                        + (selectedNode.viewNode.measureTime != -1 ? formatter
                                                .format(selectedNode.viewNode.measureTime)
+ " ms" : "n/a");
String layoutText =
"Layout: "
                                        + (selectedNode.viewNode.layoutTime != -1 ? formatter
                                                .format(selectedNode.viewNode.layoutTime)
+ " ms" : "n/a");
String drawText =
"Draw: "
                                        + (selectedNode.viewNode.drawTime != -1 ? formatter
                                                .format(selectedNode.viewNode.drawTime)
+ " ms" : "n/a");

org.eclipse.swt.graphics.Point titleExtent = e.gc.stringExtent(text);
//Synthetic comment -- @@ -740,7 +741,7 @@
+ layoutExtent.y + TEXT_SPACING + drawExtent.y + 2
* TEXT_TOP_OFFSET;

                        e.gc.setBackground(textBackgroundColor);
e.gc.fillRoundRectangle(x - boxWidth / 2, y, boxWidth, boxHeight,
TEXT_ROUNDING, TEXT_ROUNDING);

//Synthetic comment -- @@ -767,8 +768,8 @@

font.dispose();
} else {
                        selectedRectangleLocation = null;
                        buttonCenter = null;
}
}
}
//Synthetic comment -- @@ -778,13 +779,13 @@
private static void paintRecursive(GC gc, Transform transform, DrawableViewNode node,
DrawableViewNode selectedNode, Path connectionPath) {
if (selectedNode == node && node.viewNode.filtered) {
            gc.drawImage(filteredSelectedImage, node.left, (int) Math.round(node.top));
} else if (selectedNode == node) {
            gc.drawImage(selectedImage, node.left, (int) Math.round(node.top));
} else if (node.viewNode.filtered) {
            gc.drawImage(filteredImage, node.left, (int) Math.round(node.top));
} else {
            gc.drawImage(notSelectedImage, node.left, (int) Math.round(node.top));
}

int fontHeight = gc.getFontMetrics().getHeight();
//Synthetic comment -- @@ -816,44 +817,44 @@
y =
node.top + DrawableViewNode.NODE_HEIGHT
- DrawableViewNode.CONTENT_TOP_BOTTOM_PADDING
                            - redImage.getBounds().height;
x +=
                    (contentWidth - (redImage.getBounds().width * 3 + 2 * DrawableViewNode.CONTENT_INTER_PADDING)) / 2;
switch (node.viewNode.measureRating) {
case GREEN:
                    gc.drawImage(greenImage, (int) x, (int) y);
break;
case YELLOW:
                    gc.drawImage(yellowImage, (int) x, (int) y);
break;
case RED:
                    gc.drawImage(redImage, (int) x, (int) y);
break;
}

            x += redImage.getBounds().width + DrawableViewNode.CONTENT_INTER_PADDING;
switch (node.viewNode.layoutRating) {
case GREEN:
                    gc.drawImage(greenImage, (int) x, (int) y);
break;
case YELLOW:
                    gc.drawImage(yellowImage, (int) x, (int) y);
break;
case RED:
                    gc.drawImage(redImage, (int) x, (int) y);
break;
}

            x += redImage.getBounds().width + DrawableViewNode.CONTENT_INTER_PADDING;
switch (node.viewNode.drawRating) {
case GREEN:
                    gc.drawImage(greenImage, (int) x, (int) y);
break;
case YELLOW:
                    gc.drawImage(yellowImage, (int) x, (int) y);
break;
case RED:
                    gc.drawImage(redImage, (int) x, (int) y);
break;
}
}
//Synthetic comment -- @@ -953,7 +954,7 @@
}

private static Font getFont(int size, boolean bold) {
        FontData[] fontData = systemFont.getFontData();
for (int i = 0; i < fontData.length; i++) {
fontData[i].setHeight(size);
if (bold) {
//Synthetic comment -- @@ -972,17 +973,17 @@
}

public void loadAllData() {
        boolean newViewport = viewport == null;
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    tree = model.getTree();
                    selectedNode = model.getSelection();
                    viewport = model.getViewport();
                    zoom = model.getZoom();
                    if (tree != null && viewport == null) {
                        viewport =
                                new Rectangle(0, tree.top + DrawableViewNode.NODE_HEIGHT / 2
- getBounds().height / 2, getBounds().width,
getBounds().height);
} else {
//Synthetic comment -- @@ -992,7 +993,7 @@
}
});
if (newViewport) {
            model.setViewport(viewport);
}
}

//Synthetic comment -- @@ -1002,37 +1003,37 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    tree = model.getTree();
                    selectedNode = model.getSelection();
                    if (tree == null) {
                        viewport = null;
} else {
                        viewport =
                                new Rectangle(0, tree.top + DrawableViewNode.NODE_HEIGHT / 2
- getBounds().height / 2, getBounds().width,
getBounds().height);
}
}
}
});
        if (viewport != null) {
            model.setViewport(viewport);
} else {
doRedraw();
}
}

private void setTransform() {
        if (viewport != null && tree != null) {
// Set the transform.
            transform.identity();
            inverse.identity();

            transform.scale((float) zoom, (float) zoom);
            inverse.scale((float) zoom, (float) zoom);
            transform.translate((float) -viewport.x, (float) -viewport.y);
            inverse.translate((float) -viewport.x, (float) -viewport.y);
            inverse.invert();
}
}

//Synthetic comment -- @@ -1041,8 +1042,8 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    viewport = model.getViewport();
                    zoom = model.getZoom();
setTransform();
}
}
//Synthetic comment -- @@ -1056,10 +1057,10 @@

public void selectionChanged() {
synchronized (this) {
            selectedNode = model.getSelection();
            if (selectedNode != null && selectedNode.viewNode.image == null) {
HierarchyViewerDirector.getDirector()
                        .loadCaptureInBackground(selectedNode.viewNode);
}
}
doRedraw();








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewControls.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewControls.java
//Synthetic comment -- index 08117b5..5c794e4 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -35,11 +35,11 @@
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

public class TreeViewControls extends Composite implements TreeChangeListener {

    private Text filterText;

    private Slider zoomSlider;

public TreeViewControls(Composite parent) {
super(parent, SWT.NONE);
//Synthetic comment -- @@ -52,44 +52,44 @@
filterLabel.setText("Filter by class or id:");
filterLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));

        filterText = new Text(this, SWT.LEFT | SWT.SINGLE);
        filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        filterText.addModifyListener(filterTextModifyListener);
        filterText.setText(HierarchyViewerDirector.getDirector().getFilterText());

Label smallZoomLabel = new Label(this, SWT.NONE);
smallZoomLabel.setText(" 20%");
smallZoomLabel
.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));

        zoomSlider = new Slider(this, SWT.HORIZONTAL);
GridData zoomSliderGridData = new GridData(GridData.CENTER, GridData.CENTER, false, false);
zoomSliderGridData.widthHint = 190;
        zoomSlider.setLayoutData(zoomSliderGridData);
        zoomSlider.setMinimum((int) (TreeViewModel.MIN_ZOOM * 10));
        zoomSlider.setMaximum((int) (TreeViewModel.MAX_ZOOM * 10 + 1));
        zoomSlider.setThumb(1);
        zoomSlider.setSelection((int) Math.round(TreeViewModel.getModel().getZoom() * 10));

        zoomSlider.addSelectionListener(zoomSliderSelectionListener);

Label largeZoomLabel = new Label(this, SWT.NONE);
largeZoomLabel
.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
largeZoomLabel.setText("200%");

        addDisposeListener(disposeListener);

TreeViewModel.getModel().addTreeChangeListener(this);
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
TreeViewModel.getModel().removeTreeChangeListener(TreeViewControls.this);
}
};

    private SelectionListener zoomSliderSelectionListener = new SelectionListener() {
private int oldValue;

public void widgetDefaultSelected(SelectionEvent e) {
//Synthetic comment -- @@ -97,7 +97,7 @@
}

public void widgetSelected(SelectionEvent e) {
            int newValue = zoomSlider.getSelection();
if (oldValue != newValue) {
TreeViewModel.getModel().removeTreeChangeListener(TreeViewControls.this);
TreeViewModel.getModel().setZoom(newValue / 10.0);
//Synthetic comment -- @@ -107,9 +107,9 @@
}
};

    private ModifyListener filterTextModifyListener = new ModifyListener() {
public void modifyText(ModifyEvent e) {
            HierarchyViewerDirector.getDirector().filterNodes(filterText.getText());
}
};

//Synthetic comment -- @@ -121,10 +121,10 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
if (TreeViewModel.getModel().getTree() != null) {
                    zoomSlider.setSelection((int) Math
.round(TreeViewModel.getModel().getZoom() * 10));
}
                filterText.setText("");
}
});
}
//Synthetic comment -- @@ -136,7 +136,7 @@
public void zoomChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
                zoomSlider.setSelection((int) Math.round(TreeViewModel.getModel().getZoom() * 10));
}
});
};








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java
//Synthetic comment -- index fb01b86..34167dd 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.ui.util.DrawableViewNode.Rectangle;
//Synthetic comment -- @@ -41,72 +41,72 @@
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TreeViewOverview extends Canvas implements TreeChangeListener {

    private TreeViewModel model;

    private DrawableViewNode tree;

    private Rectangle viewport;

    private Transform transform;

    private Transform inverse;

    private Rectangle bounds = new Rectangle();

    private double scale;

    private boolean dragging = false;

    private DrawableViewNode selectedNode;

    private static Image notSelectedImage;

    private static Image selectedImage;

    private static Image filteredImage;

    private static Image filteredSelectedImage;

public TreeViewOverview(Composite parent) {
super(parent, SWT.NONE);

        model = TreeViewModel.getModel();
        model.addTreeChangeListener(this);

loadResources();

        addPaintListener(paintListener);
        addMouseListener(mouseListener);
        addMouseMoveListener(mouseMoveListener);
        addListener(SWT.Resize, resizeListener);
        addDisposeListener(disposeListener);

        transform = new Transform(Display.getDefault());
        inverse = new Transform(Display.getDefault());

loadAllData();
}

private void loadResources() {
ImageLoader loader = ImageLoader.getLoader(this.getClass());
        notSelectedImage = loader.loadImage("not-selected.png", Display.getDefault());
        selectedImage = loader.loadImage("selected-small.png", Display.getDefault());
        filteredImage = loader.loadImage("filtered.png", Display.getDefault());
        filteredSelectedImage =
                loader.loadImage("selected-filtered-small.png", Display.getDefault());
}

    private DisposeListener disposeListener = new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
            model.removeTreeChangeListener(TreeViewOverview.this);
            transform.dispose();
            inverse.dispose();
}
};

    private MouseListener mouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
// pass
//Synthetic comment -- @@ -115,16 +115,16 @@
public void mouseDown(MouseEvent e) {
boolean redraw = false;
synchronized (TreeViewOverview.this) {
                if (tree != null && viewport != null) {
                    dragging = true;
redraw = true;
handleMouseEvent(transformPoint(e.x, e.y));
}
}
if (redraw) {
                model.removeTreeChangeListener(TreeViewOverview.this);
                model.setViewport(viewport);
                model.addTreeChangeListener(TreeViewOverview.this);
doRedraw();
}
}
//Synthetic comment -- @@ -132,8 +132,8 @@
public void mouseUp(MouseEvent e) {
boolean redraw = false;
synchronized (TreeViewOverview.this) {
                if (tree != null && viewport != null) {
                    dragging = false;
redraw = true;
handleMouseEvent(transformPoint(e.x, e.y));

//Synthetic comment -- @@ -145,47 +145,47 @@
}
}
if (redraw) {
                model.removeTreeChangeListener(TreeViewOverview.this);
                model.setViewport(viewport);
                model.addTreeChangeListener(TreeViewOverview.this);
doRedraw();
}
}

};

    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
public void mouseMove(MouseEvent e) {
boolean moved = false;
synchronized (TreeViewOverview.this) {
                if (dragging) {
moved = true;
handleMouseEvent(transformPoint(e.x, e.y));
}
}
if (moved) {
                model.removeTreeChangeListener(TreeViewOverview.this);
                model.setViewport(viewport);
                model.addTreeChangeListener(TreeViewOverview.this);
doRedraw();
}
}
};

private void handleMouseEvent(Point pt) {
        viewport.x = pt.x - viewport.width / 2;
        viewport.y = pt.y - viewport.height / 2;
        if (viewport.x < bounds.x) {
            viewport.x = bounds.x;
}
        if (viewport.y < bounds.y) {
            viewport.y = bounds.y;
}
        if (viewport.x + viewport.width > bounds.x + bounds.width) {
            viewport.x = bounds.x + bounds.width - viewport.width;
}
        if (viewport.y + viewport.height > bounds.y + bounds.height) {
            viewport.y = bounds.y + bounds.height - viewport.height;
}
}

//Synthetic comment -- @@ -193,11 +193,11 @@
float[] pt = {
(float) x, (float) y
};
        inverse.transform(pt);
return new Point(pt[0], pt[1]);
}

    private Listener resizeListener = new Listener() {
public void handleEvent(Event arg0) {
synchronized (TreeViewOverview.this) {
setTransform();
//Synthetic comment -- @@ -206,33 +206,33 @@
}
};

    private PaintListener paintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
synchronized (TreeViewOverview.this) {
                if (tree != null) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                    e.gc.setTransform(transform);
                    e.gc.setLineWidth((int) Math.ceil(0.7 / scale));
Path connectionPath = new Path(Display.getDefault());
                    paintRecursive(e.gc, tree, connectionPath);
e.gc.drawPath(connectionPath);
connectionPath.dispose();

                    if (viewport != null) {
e.gc.setAlpha(50);
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                        e.gc.fillRectangle((int) viewport.x, (int) viewport.y, (int) Math
                                .ceil(viewport.width), (int) Math.ceil(viewport.height));

e.gc.setAlpha(255);
e.gc
.setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_DARK_GRAY));
                        e.gc.setLineWidth((int) Math.ceil(2 / scale));
                        e.gc.drawRectangle((int) viewport.x, (int) viewport.y, (int) Math
                                .ceil(viewport.width), (int) Math.ceil(viewport.height));
}
}
}
//Synthetic comment -- @@ -240,14 +240,14 @@
};

private void paintRecursive(GC gc, DrawableViewNode node, Path connectionPath) {
        if (selectedNode == node && node.viewNode.filtered) {
            gc.drawImage(filteredSelectedImage, node.left, (int) Math.round(node.top));
        } else if (selectedNode == node) {
            gc.drawImage(selectedImage, node.left, (int) Math.round(node.top));
} else if (node.viewNode.filtered) {
            gc.drawImage(filteredImage, node.left, (int) Math.round(node.top));
} else {
            gc.drawImage(notSelectedImage, node.left, (int) Math.round(node.top));
}
int N = node.children.size();
if (N == 0) {
//Synthetic comment -- @@ -284,9 +284,9 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    tree = model.getTree();
                    selectedNode = model.getSelection();
                    viewport = model.getViewport();
setBounds();
setTransform();
}
//Synthetic comment -- @@ -299,9 +299,9 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    tree = model.getTree();
                    selectedNode = model.getSelection();
                    viewport = model.getViewport();
setBounds();
setTransform();
}
//Synthetic comment -- @@ -311,48 +311,48 @@
}

private void setBounds() {
        if (viewport != null && tree != null) {
            bounds.x = Math.min(viewport.x, tree.bounds.x);
            bounds.y = Math.min(viewport.y, tree.bounds.y);
            bounds.width =
                    Math.max(viewport.x + viewport.width, tree.bounds.x + tree.bounds.width)
                            - bounds.x;
            bounds.height =
                    Math.max(viewport.y + viewport.height, tree.bounds.y + tree.bounds.height)
                            - bounds.y;
        } else if (tree != null) {
            bounds.x = tree.bounds.x;
            bounds.y = tree.bounds.y;
            bounds.width = tree.bounds.x + tree.bounds.width - bounds.x;
            bounds.height = tree.bounds.y + tree.bounds.height - bounds.y;
}
}

private void setTransform() {
        if (tree != null) {

            transform.identity();
            inverse.identity();
final Point size = new Point();
size.x = getBounds().width;
size.y = getBounds().height;
            if (bounds.width == 0 || bounds.height == 0 || size.x == 0 || size.y == 0) {
                scale = 1;
} else {
                scale = Math.min(size.x / bounds.width, size.y / bounds.height);
}
            transform.scale((float) scale, (float) scale);
            inverse.scale((float) scale, (float) scale);
            transform.translate((float) -bounds.x, (float) -bounds.y);
            inverse.translate((float) -bounds.x, (float) -bounds.y);
            if (size.x / bounds.width < size.y / bounds.height) {
                transform.translate(0, (float) (size.y / scale - bounds.height) / 2);
                inverse.translate(0, (float) (size.y / scale - bounds.height) / 2);
} else {
                transform.translate((float) (size.x / scale - bounds.width) / 2, 0);
                inverse.translate((float) (size.x / scale - bounds.width) / 2, 0);
}
            inverse.invert();
}
}

//Synthetic comment -- @@ -360,7 +360,7 @@
Display.getDefault().syncExec(new Runnable() {
public void run() {
synchronized (this) {
                    viewport = model.getViewport();
setBounds();
setTransform();
}
//Synthetic comment -- @@ -375,7 +375,7 @@

public void selectionChanged() {
synchronized (this) {
            selectedNode = model.getSelection();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java
//Synthetic comment -- index 7570676..b196aaf 100644

//Synthetic comment -- @@ -86,7 +86,7 @@

@Override
public String toString() {
            return "{" + x + ", " + y + ", " + width + ", " + height + "}";
}

}
//Synthetic comment -- @@ -104,7 +104,7 @@

@Override
public String toString() {
            return "(" + x + ", " + y + ")";
}
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/PsdFile.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/PsdFile.java
//Synthetic comment -- index 275ea36..2c1154b 100644

//Synthetic comment -- @@ -160,7 +160,7 @@

static final short MODE_LAB = 9;

        final byte[] mSignature = "8BPS".getBytes();

final short mVersion = 1;

//Synthetic comment -- @@ -214,7 +214,7 @@

int mLength = 0;

        final byte[] mSignature = "8BIM".getBytes();

final short mResourceId = RESOURCE_RESOLUTION_INFO;

//Synthetic comment -- @@ -342,9 +342,9 @@

final Channel[] mChannelInfo = new Channel[mChannelCount];

        final byte[] mBlendSignature = "8BIM".getBytes();

        final byte[] mBlendMode = "norm".getBytes();

final byte mOpacity = OPACITY_OPAQUE;

//Synthetic comment -- @@ -362,9 +362,9 @@

final byte[] mName;

        final byte[] mLayerExtraSignature = "8BIM".getBytes();

        final byte[] mLayerExtraKey = "luni".getBytes();

int mLayerExtraLength;

//Synthetic comment -- @@ -391,7 +391,7 @@
byte[] data = name.getBytes();

try {
                mLayerExtraLength = 4 + mOriginalName.getBytes("UTF-16").length;
} catch (UnsupportedEncodingException e) {
e.printStackTrace();
}
//Synthetic comment -- @@ -465,7 +465,7 @@
out.write(mLayerExtraKey);
out.writeInt(mLayerExtraLength);
out.writeInt(mOriginalName.length() + 1);
            out.write(mOriginalName.getBytes("UTF-16"));
}

void writeImageData(DataOutputStream out) throws IOException {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/TreeColumnResizer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/TreeColumnResizer.java
//Synthetic comment -- index 6b20366..e03394a 100644

//Synthetic comment -- @@ -24,24 +24,24 @@

public class TreeColumnResizer {

    private TreeColumn column1;

    private TreeColumn column2;

    private Composite control;

    private int column1Width;

    private int column2Width;

private final static int MIN_COLUMN1_WIDTH = 18;

private final static int MIN_COLUMN2_WIDTH = 3;

public TreeColumnResizer(Composite control, TreeColumn column1, TreeColumn column2) {
        this.control = control;
        this.column1 = column1;
        this.column2 = column2;
control.addListener(SWT.Resize, resizeListener);
column1.addListener(SWT.Resize, column1ResizeListener);
column2.setResizable(false);
//Synthetic comment -- @@ -49,64 +49,64 @@

private Listener resizeListener = new Listener() {
public void handleEvent(Event e) {
            if (column1Width == 0 && column2Width == 0) {
                column1Width = (control.getBounds().width - 18) / 2;
                column2Width = (control.getBounds().width - 18) / 2;
} else {
                int dif = control.getBounds().width - 18 - (column1Width + column2Width);
                int columnDif = Math.abs(column1Width - column2Width);
int mainColumnChange = Math.min(Math.abs(dif), columnDif);
int left = Math.max(0, Math.abs(dif) - columnDif);
if (dif < 0) {
                    if (column1Width > column2Width) {
                        column1Width -= mainColumnChange;
} else {
                        column2Width -= mainColumnChange;
}
                    column1Width -= left / 2;
                    column2Width -= left - left / 2;
} else {
                    if (column1Width > column2Width) {
                        column2Width += mainColumnChange;
} else {
                        column1Width += mainColumnChange;
}
                    column1Width += left / 2;
                    column2Width += left - left / 2;
}
}
            column1.removeListener(SWT.Resize, column1ResizeListener);
            column1.setWidth(column1Width);
            column2.setWidth(column2Width);
            column1.addListener(SWT.Resize, column1ResizeListener);
}
};

private Listener column1ResizeListener = new Listener() {
public void handleEvent(Event e) {
            int widthDif = column1Width - column1.getWidth();
            column1Width -= widthDif;
            column2Width += widthDif;
boolean column1Changed = false;

// Strange, but these constants make the columns look the same.

            if (column1Width < MIN_COLUMN1_WIDTH) {
                column2Width -= MIN_COLUMN1_WIDTH - column1Width;
                column1Width += MIN_COLUMN1_WIDTH - column1Width;
column1Changed = true;
}
            if (column2Width < MIN_COLUMN2_WIDTH) {
                column1Width += column2Width - MIN_COLUMN2_WIDTH;
                column2Width = MIN_COLUMN2_WIDTH;
column1Changed = true;
}
if (column1Changed) {
                column1.removeListener(SWT.Resize, this);
                column1.setWidth(column1Width);
                column1.addListener(SWT.Resize, this);
}
            column2.setWidth(column2Width);
}
};
}







