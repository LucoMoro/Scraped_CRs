/*uiautomator: Misc cleanups

This CL performs a bunch of minor cleanups to allow reusing the
UiAutomatorView from within an Eclipse view/editor. In particular:
 - The model is not a singleton anymore. It can be constructed
   based on an XML file, and is stored within the view.
 - References to the view from the model have all been removed.
 - All the view code from the application window class has been
   moved into a separate UiAutomatorView class.

There is no new functionality, and from the UI perspective, there
is only one change: The are now two toolbars, a global application
level toolbar for global actions (open file, capture screenshot),
and a toolbar above the view hierarchy that allows setting a couple
of tree viewer options.

Change-Id:Ib4fe98426c2f83de233091c23b080de4f9b7449a*/
//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/OpenDialog.java b/uiautomatorviewer/src/com/android/uiautomator/OpenDialog.java
//Synthetic comment -- index a2a042d..677d2f7 100644

//Synthetic comment -- @@ -43,16 +43,16 @@
*
*/
public class OpenDialog extends Dialog {

private static final int FIXED_TEXT_FIELD_WIDTH = 300;
private static final int DEFAULT_LAYOUT_SPACING = 10;
private Text mScreenshotText;
private Text mXmlText;
    private File mScreenshotFile;
    private File mXmlDumpFile;
private boolean mFileChanged = false;
private Button mOkButton;

/**
* Create the dialog.
* @param parentShell
//Synthetic comment -- @@ -68,8 +68,6 @@
*/
@Override
protected Control createDialogArea(Composite parent) {
        loadDataFromModel();

Composite container = (Composite) super.createDialogArea(parent);
GridLayout gl_container = new GridLayout(1, false);
gl_container.verticalSpacing = DEFAULT_LAYOUT_SPACING;
//Synthetic comment -- @@ -84,8 +82,8 @@
openScreenshotGroup.setText("Screenshot");

mScreenshotText = new Text(openScreenshotGroup, SWT.BORDER | SWT.READ_ONLY);
        if (mScreenshotFile != null) {
            mScreenshotText.setText(mScreenshotFile.getAbsolutePath());
}
GridData gd_screenShotText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
gd_screenShotText.minimumWidth = FIXED_TEXT_FIELD_WIDTH;
//Synthetic comment -- @@ -108,8 +106,8 @@

mXmlText = new Text(openXmlGroup, SWT.BORDER | SWT.READ_ONLY);
mXmlText.setEditable(false);
        if (mXmlDumpFile != null) {
            mXmlText.setText(mXmlDumpFile.getAbsolutePath());
}
GridData gd_xmlText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
gd_xmlText.minimumWidth = FIXED_TEXT_FIELD_WIDTH;
//Synthetic comment -- @@ -153,18 +151,13 @@
newShell.setText("Open UI Dump Files");
}

    private void loadDataFromModel() {
        mScreenshotFile = UiAutomatorModel.getModel().getScreenshotFile();
        mXmlDumpFile = UiAutomatorModel.getModel().getXmlDumpFile();
    }

private void handleOpenScreenshotFile() {
FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
fd.setText("Open Screenshot File");
        File initialFile = mScreenshotFile;
// if file has never been selected before, try to base initial path on the mXmlDumpFile
        if (initialFile == null && mXmlDumpFile != null && mXmlDumpFile.isFile()) {
            initialFile = mXmlDumpFile.getParentFile();
}
if (initialFile != null) {
if (initialFile.isFile()) {
//Synthetic comment -- @@ -177,7 +170,7 @@
fd.setFilterExtensions(filter);
String selected = fd.open();
if (selected != null) {
            mScreenshotFile = new File(selected);
mScreenshotText.setText(selected);
mFileChanged = true;
}
//Synthetic comment -- @@ -187,10 +180,10 @@
private void handleOpenXmlDumpFile() {
FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
fd.setText("Open UI Dump XML File");
        File initialFile = mXmlDumpFile;
// if file has never been selected before, try to base initial path on the mScreenshotFile
        if (initialFile == null && mScreenshotFile != null && mScreenshotFile.isFile()) {
            initialFile = mScreenshotFile.getParentFile();
}
if (initialFile != null) {
if (initialFile.isFile()) {
//Synthetic comment -- @@ -200,14 +193,14 @@
}
}
String initialPath = mXmlText.getText();
        if (initialPath.isEmpty() && mScreenshotFile != null && mScreenshotFile.isFile()) {
            initialPath = mScreenshotFile.getParentFile().getAbsolutePath();
}
String[] filter = {"*.xml"};
fd.setFilterExtensions(filter);
String selected = fd.open();
if (selected != null) {
            mXmlDumpFile = new File(selected);
mXmlText.setText(selected);
mFileChanged = true;
}
//Synthetic comment -- @@ -215,8 +208,8 @@
}

private void updateButtonState() {
        mOkButton.setEnabled(mScreenshotFile != null && mXmlDumpFile != null
                && mScreenshotFile.isFile() && mXmlDumpFile.isFile());
}

public boolean hasFileChanged() {
//Synthetic comment -- @@ -224,10 +217,10 @@
}

public File getScreenshotFile() {
        return mScreenshotFile;
}

public File getXmlDumpFile() {
        return mXmlDumpFile;
}
}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorModel.java b/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorModel.java
//Synthetic comment -- index 0a1fab0..c724f8b 100644

//Synthetic comment -- @@ -21,121 +21,43 @@
import com.android.uiautomator.tree.UiHierarchyXmlLoader;
import com.android.uiautomator.tree.UiNode;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UiAutomatorModel {

    private static UiAutomatorModel inst = null;

    private File mScreenshotFile, mXmlDumpFile;
    private UiAutomatorViewer mView;
    private Image mScreenshot;
private BasicTreeNode mRootNode;
private BasicTreeNode mSelectedNode;
private Rectangle mCurrentDrawingRect;
private List<Rectangle> mNafNodes;
    private List<File> mTmpDirs;

// determines whether we lookup the leaf UI node on mouse move of screenshot image
private boolean mExploreMode = true;

private boolean mShowNafNodes = false;

    private UiAutomatorModel(UiAutomatorViewer view) {
        mView = view;
        mTmpDirs = new ArrayList<File>();
    }

    public static UiAutomatorModel createInstance(UiAutomatorViewer view) {
        if (inst != null) {
            throw new IllegalStateException("instance already created!");
}
        inst = new UiAutomatorModel(view);
        return inst;
    }

    public static UiAutomatorModel getModel() {
        if (inst == null) {
            throw new IllegalStateException("instance not created yet!");
}
        return inst;
    }

    public File getScreenshotFile() {
        return mScreenshotFile;
    }

    public File getXmlDumpFile() {
        return mXmlDumpFile;
    }

    public boolean loadScreenshotAndXmlDump(File screenshotFile, File xmlDumpFile) {
        if (screenshotFile != null && xmlDumpFile != null
                && screenshotFile.isFile() && xmlDumpFile.isFile()) {
            ImageData[] data = null;
            Image img = null;
            try {
                // use SWT's ImageLoader to read png from path
                data = new ImageLoader().load(screenshotFile.getAbsolutePath());
            } catch (SWTException e) {
                e.printStackTrace();
                return false;
            }
            // "data" is an array, probably used to handle images that has multiple frames
            // i.e. gifs or icons, we just care if it has at least one here
            if (data.length < 1) return false;
            UiHierarchyXmlLoader loader = new UiHierarchyXmlLoader();
            BasicTreeNode rootNode = loader.parseXml(xmlDumpFile
                    .getAbsolutePath());
            if (rootNode == null) {
                System.err.println("null rootnode after parsing.");
                return false;
            }
            mNafNodes = loader.getNafNodes();
            try {
                // Image is tied to ImageData and a Display, so we only need to create once
                // per new image
                img = new Image(mView.getShell().getDisplay(), data[0]);
            } catch (SWTException e) {
                e.printStackTrace();
                return false;
            }
            // only update screenhot and xml if both are loaded successfully
            if (mScreenshot != null) {
                mScreenshot.dispose();
            }
            mScreenshot = img;
            if (mRootNode != null) {
                mRootNode.clearAllChildren();
            }
            // TODO: we should verify here if the coordinates in the XML matches the png
            // or not: think loading a phone screenshot with a tablet XML dump
            mRootNode = rootNode;
            mScreenshotFile = screenshotFile;
            mXmlDumpFile = xmlDumpFile;
            mExploreMode = true;
            mView.loadScreenshotAndXml();
            return true;
        }
        return false;
}

public BasicTreeNode getXmlRootNode() {
return mRootNode;
}

    public Image getScreenshot() {
        return mScreenshot;
    }

public BasicTreeNode getSelectedNode() {
return mSelectedNode;
}
//Synthetic comment -- @@ -148,16 +70,12 @@
*/
public void setSelectedNode(BasicTreeNode node) {
mSelectedNode = node;
        if (mSelectedNode != null && mSelectedNode instanceof UiNode) {
UiNode uiNode = (UiNode) mSelectedNode;
mCurrentDrawingRect = new Rectangle(uiNode.x, uiNode.y, uiNode.width, uiNode.height);
} else {
mCurrentDrawingRect = null;
}
        mView.updateScreenshot();
        if (mSelectedNode != null) {
            mView.loadAttributeTable();
        }
}

public Rectangle getCurrentDrawingRect() {
//Synthetic comment -- @@ -169,15 +87,20 @@
*
* @param x
* @param y
*/
    public void updateSelectionForCoordinates(int x, int y) {
        if (mRootNode == null)
            return;
        MinAreaFindNodeListener listener = new MinAreaFindNodeListener();
        boolean found = mRootNode.findLeafMostNodesAtPoint(x, y, listener);
        if (found && listener.mNode != null && !listener.mNode.equals(mSelectedNode)) {
            mView.updateTreeSelection(listener.mNode);
}
}

public boolean isExploreMode() {
//Synthetic comment -- @@ -186,7 +109,6 @@

public void toggleExploreMode() {
mExploreMode = !mExploreMode;
        mView.updateScreenshot();
}

public void setExploreMode(boolean exploreMode) {
//Synthetic comment -- @@ -213,28 +135,9 @@

public void toggleShowNaf() {
mShowNafNodes = !mShowNafNodes;
        mView.updateScreenshot();
}

public boolean shouldShowNafNodes() {
return mShowNafNodes;
}

    /**
     * Registers a temporary directory for deletion when app exists
     *
     * @param tmpDir
     */
    public void registerTempDirectory(File tmpDir) {
        mTmpDirs.add(tmpDir);
    }

    /**
     * Performs cleanup tasks when the app is exiting
     */
    public void cleanUp() {
        for (File dir : mTmpDirs) {
            Utils.deleteRecursive(dir);
        }
    }
}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorView.java b/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorView.java
new file mode 100644
//Synthetic comment -- index 0000000..6e943c6

//Synthetic comment -- @@ -0,0 +1,365 @@








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorViewer.java b/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorViewer.java
//Synthetic comment -- index 48e01cf..d0f3b94 100644

//Synthetic comment -- @@ -16,268 +16,55 @@

package com.android.uiautomator;

import com.android.uiautomator.actions.ExpandAllAction;
import com.android.uiautomator.actions.OpenFilesAction;
import com.android.uiautomator.actions.ScreenshotAction;
import com.android.uiautomator.actions.ToggleNafAction;
import com.android.uiautomator.tree.AttributePair;
import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.BasicTreeNodeContentProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;

public class UiAutomatorViewer extends ApplicationWindow {
    private static final int IMG_BORDER = 2;

    private Canvas mScreenshotCanvas;
    private TreeViewer mTreeViewer;

    private Action mOpenFilesAction;
    private Action mExpandAllAction;
    private Action mScreenshotAction;
    private Action mToggleNafAction;
    private TableViewer mTableViewer;

    private float mScale = 1.0f;
    private int mDx, mDy;

    /**
     * Create the application window.
     */
public UiAutomatorViewer() {
super(null);
        UiAutomatorModel.createInstance(this);
        createActions();
}

    /**
     * Create contents of the application window.
     *
     * @param parent
     */
@Override
protected Control createContents(Composite parent) {
        SashForm baseSash = new SashForm(parent, SWT.HORIZONTAL | SWT.NONE);
        // draw the canvas with border, so the divider area for sash form can be highlighted
        mScreenshotCanvas = new Canvas(baseSash, SWT.BORDER);
        mScreenshotCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                UiAutomatorModel.getModel().toggleExploreMode();
            }
        });
        mScreenshotCanvas.setBackground(
                getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        mScreenshotCanvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                Image image = UiAutomatorModel.getModel().getScreenshot();
                if (image != null) {
                    updateScreenshotTransformation();
                    // shifting the image here, so that there's a border around screen shot
                    // this makes highlighting red rectangles on the screen shot edges more visible
                    Transform t = new Transform(e.gc.getDevice());
                    t.translate(mDx, mDy);
                    t.scale(mScale, mScale);
                    e.gc.setTransform(t);
                    e.gc.drawImage(image, 0, 0);
                    // this resets the transformation to identity transform, i.e. no change
                    // we don't use transformation here because it will cause the line pattern
                    // and line width of highlight rect to be scaled, causing to appear to be blurry
                    e.gc.setTransform(null);
                    if (UiAutomatorModel.getModel().shouldShowNafNodes()) {
                        // highlight the "Not Accessibility Friendly" nodes
                        e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
                        e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
                        for (Rectangle r : UiAutomatorModel.getModel().getNafNodes()) {
                            e.gc.setAlpha(50);
                            e.gc.fillRectangle(mDx + getScaledSize(r.x), mDy + getScaledSize(r.y),
                                    getScaledSize(r.width), getScaledSize(r.height));
                            e.gc.setAlpha(255);
                            e.gc.setLineStyle(SWT.LINE_SOLID);
                            e.gc.setLineWidth(2);
                            e.gc.drawRectangle(mDx + getScaledSize(r.x), mDy + getScaledSize(r.y),
                                    getScaledSize(r.width), getScaledSize(r.height));
                        }
                    }
                    // draw the mouseover rects
                    Rectangle rect = UiAutomatorModel.getModel().getCurrentDrawingRect();
                    if (rect != null) {
                        e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_RED));
                        if (UiAutomatorModel.getModel().isExploreMode()) {
                            // when we highlight nodes dynamically on mouse move,
                            // use dashed borders
                            e.gc.setLineStyle(SWT.LINE_DASH);
                            e.gc.setLineWidth(1);
                        } else {
                            // when highlighting nodes on tree node selection,
                            // use solid borders
                            e.gc.setLineStyle(SWT.LINE_SOLID);
                            e.gc.setLineWidth(2);
                        }
                        e.gc.drawRectangle(mDx + getScaledSize(rect.x), mDy + getScaledSize(rect.y),
                                getScaledSize(rect.width), getScaledSize(rect.height));
                    }
                }
            }
        });
        mScreenshotCanvas.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                if (UiAutomatorModel.getModel().isExploreMode()) {
                    UiAutomatorModel.getModel().updateSelectionForCoordinates(
                            getInverseScaledSize(e.x - mDx),
                            getInverseScaledSize(e.y - mDy));
                }
            }
        });

        // right sash is split into 2 parts: upper-right and lower-right
        // both are composites with borders, so that the horizontal divider can be highlighted by
        // the borders
        SashForm rightSash = new SashForm(baseSash, SWT.VERTICAL);

        // upper-right base contains the toolbar and the tree
        Composite upperRightBase = new Composite(rightSash, SWT.BORDER);
        upperRightBase.setLayout(new GridLayout(1, false));
ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        toolBarManager.add(mOpenFilesAction);
        toolBarManager.add(mExpandAllAction);
        toolBarManager.add(mScreenshotAction);
        toolBarManager.add(mToggleNafAction);
        toolBarManager.createControl(upperRightBase);

        mTreeViewer = new TreeViewer(upperRightBase, SWT.NONE);
        mTreeViewer.setContentProvider(new BasicTreeNodeContentProvider());
        // default LabelProvider uses toString() to generate text to display
        mTreeViewer.setLabelProvider(new LabelProvider());
        mTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    UiAutomatorModel.getModel().setSelectedNode(null);
                } else if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    Object o = selection.toArray()[0];
                    if (o instanceof BasicTreeNode) {
                        UiAutomatorModel.getModel().setSelectedNode((BasicTreeNode)o);
                    }
                }
            }
        });
        Tree tree = mTreeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        // move focus so that it's not on tool bar (looks weird)
        tree.setFocus();

        // lower-right base contains the detail group
        Composite lowerRightBase = new Composite(rightSash, SWT.BORDER);
        lowerRightBase.setLayout(new FillLayout());
        Group grpNodeDetail = new Group(lowerRightBase, SWT.NONE);
        grpNodeDetail.setLayout(new FillLayout(SWT.HORIZONTAL));
        grpNodeDetail.setText("Node Detail");

        Composite tableContainer = new Composite(grpNodeDetail, SWT.NONE);

        TableColumnLayout columnLayout = new TableColumnLayout();
        tableContainer.setLayout(columnLayout);

        mTableViewer = new TableViewer(tableContainer, SWT.NONE | SWT.FULL_SELECTION);
        Table table = mTableViewer.getTable();
        table.setLinesVisible(true);
        // use ArrayContentProvider here, it assumes the input to the TableViewer
        // is an array, where each element represents a row in the table
        mTableViewer.setContentProvider(new ArrayContentProvider());

        TableViewerColumn tableViewerColumnKey = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn tblclmnKey = tableViewerColumnKey.getColumn();
        tableViewerColumnKey.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof AttributePair) {
                    // first column, shows the attribute name
                    return ((AttributePair)element).key;
                }
                return super.getText(element);
            }
        });
        columnLayout.setColumnData(tblclmnKey,
                new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));

        TableViewerColumn tableViewerColumnValue = new TableViewerColumn(mTableViewer, SWT.NONE);
        tableViewerColumnValue.setEditingSupport(new AttributeTableEditingSupport(mTableViewer));
        TableColumn tblclmnValue = tableViewerColumnValue.getColumn();
        columnLayout.setColumnData(tblclmnValue,
                new ColumnWeightData(2, ColumnWeightData.MINIMUM_WIDTH, true));
        tableViewerColumnValue.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof AttributePair) {
                    // second column, shows the attribute value
                    return ((AttributePair)element).value;
                }
                return super.getText(element);
            }
        });
        // sets the ratio of the vertical split: left 5 vs right 3
        baseSash.setWeights(new int[]{5, 3});
        return baseSash;
}

    /**
     * Create the actions.
     */
    private void createActions() {
        mOpenFilesAction = new OpenFilesAction(this);
        mExpandAllAction = new ExpandAllAction(this);
        mScreenshotAction = new ScreenshotAction(this);
        mToggleNafAction = new ToggleNafAction();
    }

    /**
     * Launch the application.
     *
     * @param args
     */
public static void main(String args[]) {
DebugBridge.init();

//Synthetic comment -- @@ -285,7 +72,6 @@
UiAutomatorViewer window = new UiAutomatorViewer();
window.setBlockOnOpen(true);
window.open();
            UiAutomatorModel.getModel().cleanUp();
} catch (Exception e) {
e.printStackTrace();
} finally {
//Synthetic comment -- @@ -293,117 +79,27 @@
}
}

    /**
     * Configure the shell.
     *
     * @param newShell
     */
@Override
protected void configureShell(Shell newShell) {
super.configureShell(newShell);
newShell.setText("UI Automator Viewer");
}


    /**
     * Asks the Model for screenshot and xml tree data, then populates the screenshot
     * area and tree view accordingly
     */
    public void loadScreenshotAndXml() {
        mScreenshotCanvas.redraw();
        // load xml into tree
        BasicTreeNode wrapper = new BasicTreeNode();
        // putting another root node on top of existing root node
        // because Tree seems to like to hide the root node
        wrapper.addChild(UiAutomatorModel.getModel().getXmlRootNode());
        mTreeViewer.setInput(wrapper);
        mTreeViewer.getTree().setFocus();
    }

    /*
     * Causes a redraw of the canvas.
     *
     * The drawing code of canvas will handle highlighted nodes and etc based on data
     * retrieved from Model
     */
    public void updateScreenshot() {
        mScreenshotCanvas.redraw();
    }

    public void expandAll() {
        mTreeViewer.expandAll();
    }

    public void updateTreeSelection(BasicTreeNode node) {
        mTreeViewer.setSelection(new StructuredSelection(node), true);
    }

    public void loadAttributeTable() {
        // udpate the lower right corner table to show the attributes of the node
        mTableViewer.setInput(
                UiAutomatorModel.getModel().getSelectedNode().getAttributesArray());
    }

@Override
protected Point getInitialSize() {
return new Point(800, 600);
}

    private void updateScreenshotTransformation() {
        Rectangle canvas = mScreenshotCanvas.getBounds();
        Rectangle image = UiAutomatorModel.getModel().getScreenshot().getBounds();
        float scaleX = (canvas.width - 2 * IMG_BORDER - 1) / (float)image.width;
        float scaleY = (canvas.height - 2 * IMG_BORDER - 1) / (float)image.height;
        // use the smaller scale here so that we can fit the entire screenshot
        mScale = Math.min(scaleX, scaleY);
        // calculate translation values to center the image on the canvas
        mDx = (canvas.width - getScaledSize(image.width) - IMG_BORDER * 2) / 2 + IMG_BORDER;
        mDy = (canvas.height - getScaledSize(image.height) - IMG_BORDER * 2) / 2 + IMG_BORDER;
    }

    private int getScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
} else {
            return new Double(Math.floor((size * mScale))).intValue();
}
}

    private int getInverseScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
        } else {
            return new Double(Math.floor((size / mScale))).intValue();
        }
    }

    private class AttributeTableEditingSupport extends EditingSupport {

        private TableViewer mViewer;

        public AttributeTableEditingSupport(TableViewer viewer) {
            super(viewer);
            mViewer = viewer;
        }

        @Override
        protected boolean canEdit(Object arg0) {
            return true;
        }

        @Override
        protected CellEditor getCellEditor(Object arg0) {
            return new TextCellEditor(mViewer.getTable());
        }

        @Override
        protected Object getValue(Object o) {
            return ((AttributePair)o).value;
        }

        @Override
        protected void setValue(Object arg0, Object arg1) {
        }

    }
}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/Utils.java b/uiautomatorviewer/src/com/android/uiautomator/Utils.java
deleted file mode 100644
//Synthetic comment -- index 5306fe3..0000000

//Synthetic comment -- @@ -1,32 +0,0 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.uiautomator;

import java.io.File;

public class Utils {
    public static void deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (File child : children) {
                if (!child.getName().startsWith("."))
                    deleteRecursive(child);
            }
        }
        file.delete();
    }
}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/actions/ExpandAllAction.java b/uiautomatorviewer/src/com/android/uiautomator/actions/ExpandAllAction.java
//Synthetic comment -- index 3c73fdc..a37539b 100644

//Synthetic comment -- @@ -16,18 +16,18 @@

package com.android.uiautomator.actions;

import com.android.uiautomator.UiAutomatorViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class ExpandAllAction extends Action {

    UiAutomatorViewer mWindow;

    public ExpandAllAction(UiAutomatorViewer window) {
super("&Expand All");
        mWindow = window;
}

@Override
//Synthetic comment -- @@ -37,7 +37,6 @@

@Override
public void run() {
        mWindow.expandAll();
}

}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/actions/OpenFilesAction.java b/uiautomatorviewer/src/com/android/uiautomator/actions/OpenFilesAction.java
//Synthetic comment -- index 3232857..0d4e707 100644

//Synthetic comment -- @@ -18,18 +18,24 @@

import com.android.uiautomator.OpenDialog;
import com.android.uiautomator.UiAutomatorModel;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;

public class OpenFilesAction extends Action {

    ApplicationWindow mWindow;

    public OpenFilesAction(ApplicationWindow window) {
super("&Open");
        mWindow = window;
}

@Override
//Synthetic comment -- @@ -39,10 +45,37 @@

@Override
public void run() {
        OpenDialog d = new OpenDialog(mWindow.getShell());
        if (d.open() == OpenDialog.OK) {
            UiAutomatorModel.getModel().loadScreenshotAndXmlDump(
                    d.getScreenshotFile(), d.getXmlDumpFile());
}
}
}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/actions/ScreenshotAction.java b/uiautomatorviewer/src/com/android/uiautomator/actions/ScreenshotAction.java
//Synthetic comment -- index 181f655..ff09779 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
//Synthetic comment -- @@ -45,6 +46,7 @@
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

//Synthetic comment -- @@ -122,7 +124,10 @@
showError("Cannot get temp directory", e, monitor);
return;
}
                    UiAutomatorModel.getModel().registerTempDirectory(tmpDir);

String apiLevelString = device.getProperty(IDevice.PROP_BUILD_API_LEVEL);
int apiLevel;
//Synthetic comment -- @@ -180,6 +185,14 @@
return;
}

PaletteData palette = new PaletteData(
rawImage.getRedMask(),
rawImage.getGreenMask(),
//Synthetic comment -- @@ -189,19 +202,9 @@
ImageLoader loader = new ImageLoader();
loader.data = new ImageData[] { imageData };
loader.save(screenshotFile.getAbsolutePath(), SWT.IMAGE_PNG);

                    final File png = screenshotFile, xml = xmlDumpFile;
                    if(png.length() == 0) {
                        showError("Screenshot file size is 0", null, monitor);
                        return;
                    } else {
                        mViewer.getShell().getDisplay().syncExec(new Runnable() {
                            @Override
                            public void run() {
                                UiAutomatorModel.getModel().loadScreenshotAndXmlDump(png, xml);
                            }
                        });
                    }
monitor.done();
}
});








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/actions/ToggleNafAction.java b/uiautomatorviewer/src/com/android/uiautomator/actions/ToggleNafAction.java
//Synthetic comment -- index afc422d..fe4cbfa 100644

//Synthetic comment -- @@ -16,17 +16,20 @@

package com.android.uiautomator.actions;

import com.android.uiautomator.UiAutomatorModel;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;

public class ToggleNafAction extends Action {

    public ToggleNafAction() {
super("&Toggle NAF Nodes", IAction.AS_CHECK_BOX);
        setChecked(UiAutomatorModel.getModel().shouldShowNafNodes());
}

@Override
//Synthetic comment -- @@ -36,7 +39,8 @@

@Override
public void run() {
        UiAutomatorModel.getModel().toggleShowNaf();
        setChecked(UiAutomatorModel.getModel().shouldShowNafNodes());
}
}







