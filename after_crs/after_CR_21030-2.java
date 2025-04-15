/*Make zoom level persistent

Make the zoom level of the layout editor persistent across IDE
sessions. This is particularly useful now that we are dealing with
larger screens, such as WXGA, where you typically need to zoom out
(zoom to fit) in order to see the whole layout, and it's annoying to
have to do this every time you open the IDE.

Change-Id:Ib062a6a9f9291445978b3cfae03c120e4f2bf386*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 9636ec8..d7f2e3c 100644

//Synthetic comment -- @@ -1481,11 +1481,7 @@
QualifiedName qname = new QualifiedName(
AdtPlugin.PLUGIN_ID,
UNKNOWN_EDITOR);
                        setFileProperty(file, qname, "1"); //$NON-NLS-1$
}
}
}
//Synthetic comment -- @@ -1497,16 +1493,11 @@
QualifiedName qname = new QualifiedName(
AdtPlugin.PLUGIN_ID,
UNKNOWN_EDITOR);
                        String prop = getFileProperty(file, qname);
if (prop != null && XmlEditor.canHandleFile(file)) {
try {
// remove the property & set editor
                                setFileProperty(file, qname, null);

// the window can be null sometimes
IWorkbench wb = PlatformUI.getWorkbench();
//Synthetic comment -- @@ -1534,7 +1525,7 @@
}
}
} catch (CoreException e) {
                                // page.openEditor may have failed
}
}
}
//Synthetic comment -- @@ -1613,6 +1604,38 @@
}

/**
     * Sets the named persistent property for the given file to the given value
     *
     * @param file the file to associate the property with
     * @param qname the name of the property
     * @param value the new value, or null to clear the property
     */
    public static void setFileProperty(IFile file, QualifiedName qname, String value) {
        try {
            file.setPersistentProperty(qname, value);
        } catch (CoreException e) {
            log(e, "Cannot set property %1$s to %2$s", qname, value);
        }
    }

    /**
     * Gets the named persistent file property from the given file
     *
     * @param file the file to look up properties for
     * @param qname the name of the property to look up
     * @return the property value, or null
     */
    public static String getFileProperty(IFile file, QualifiedName qname) {
        try {
            return file.getPersistentProperty(qname);
        } catch (CoreException e) {
            log(e, "Cannot get property %1$s", qname);
        }

        return null;
    }

    /**
* Pings the usage start server.
*/
private void pingUsageServer() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 7ee9e02..0e927fa 100644

//Synthetic comment -- @@ -51,7 +51,6 @@
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.draw2d.geometry.Rectangle;
//Synthetic comment -- @@ -671,18 +670,13 @@

// get the file stored state
boolean loadedConfigData = false;
                    String data = AdtPlugin.getFileProperty(mEditedFile, NAME_CONFIG_STATE);
                    if (mInitialState != null) {
                        data = mInitialState;
                        mInitialState = null;
                    }
                    if (data != null) {
                        loadedConfigData = mState.setData(data);
}

// update the themes and locales.
//Synthetic comment -- @@ -1094,11 +1088,7 @@
* Stores the current config selection into the edited file.
*/
public void storeState() {
        AdtPlugin.setFileProperty(mEditedFile, NAME_CONFIG_STATE, mState.getData());
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java
//Synthetic comment -- index 8c2c956..f4d31d7 100644

//Synthetic comment -- @@ -437,7 +437,7 @@
try {
// Duplicate the current state into the newly created file
QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
            String state = AdtPlugin.getFileProperty(leavingFile, qname);
file.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
} catch (CoreException e) {
// pass








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index ece4c2a..6c1f7ac 100755

//Synthetic comment -- @@ -110,6 +110,8 @@
*/
@SuppressWarnings("restriction") // For WorkBench "Show In" support
public class LayoutCanvas extends Canvas {
    private final static QualifiedName NAME_ZOOM =
        new QualifiedName(AdtPlugin.PLUGIN_ID, "zoom");//$NON-NLS-1$

private static final boolean DEBUG = false;

//Synthetic comment -- @@ -226,6 +228,22 @@
mHScale = new CanvasTransform(this, getHorizontalBar());
mVScale = new CanvasTransform(this, getVerticalBar());

        IFile file = layoutEditor.getInputFile();
        if (file != null) {
            String zoom = AdtPlugin.getFileProperty(file, NAME_ZOOM);
            if (zoom != null) {
                try {
                    double initialScale = Double.parseDouble(zoom);
                    if (initialScale > 0.0) {
                        mHScale.setScale(initialScale);
                        mVScale.setScale(initialScale);
                    }
                } catch (NumberFormatException nfe) {
                    // Ignore - use zoom=100%
                }
            }
        }

mGCWrapper = new GCWrapper(mHScale, mVScale);

Display display = getDisplay();
//Synthetic comment -- @@ -544,11 +562,19 @@
}

/* package */ void setScale(double scale, boolean redraw) {
        if (scale == getScale()) {
            return;
        }

mHScale.setScale(scale);
mVScale.setScale(scale);
if (redraw) {
redraw();
}

        // Clear the zoom setting if it is almost identical to 1.0
        String zoomValue = (Math.abs(scale - 1.0) < 0.0001) ? null : Double.toString(scale);
        AdtPlugin.setFileProperty(mLayoutEditor.getInputFile(), NAME_ZOOM, zoomValue);
}

/** Scales the canvas to best fit */
//Synthetic comment -- @@ -821,7 +847,7 @@
// Set initial state of a new file
// TODO: Only set rendering target portion of the state
QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
                            String state = AdtPlugin.getFileProperty(leavingFile, qname);
xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE,
state);
} catch (CoreException e) {







