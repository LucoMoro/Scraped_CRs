/*ADT GLE2: Deal with missing custom view classes.

- Detect missing view classes and replace them by a MockView.
  (This alone makes the rendering useful instead of not updating it
  on error.)
- Display the name of the missing view classes.
- Make them hot links and display the New Class Wizard to create them.

Change-Id:I20b69db5428751c4a6c1367103462b3867fa9c7d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index f5d452e..46461b0 100644

//Synthetic comment -- @@ -22,12 +22,16 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectClassLoader;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.layoutlib.api.IProjectCallback;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IProject;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
* Loader for Android Project class in order to use them in the layout editor.
//Synthetic comment -- @@ -35,6 +39,7 @@
public final class ProjectCallback implements IProjectCallback {

private final HashMap<String, Class<?>> mLoadedClasses = new HashMap<String, Class<?>>();
private final IProject mProject;
private final ClassLoader mParentClassLoader;
private final ProjectResources mProjectRes;
//Synthetic comment -- @@ -47,6 +52,10 @@
mProject = project;
}

/**
* {@inheritDoc}
*
//Synthetic comment -- @@ -74,12 +83,55 @@
mLoadedClasses.put(className, clazz);
return instantiateClass(clazz, constructorSignature, constructorParameters);
}
        } catch (Error e) {
// Log this error with the class name we're trying to load and abort.
AdtPlugin.log(e, "ProjectCallback.loadView failed to find class %1$s", className); //$NON-NLS-1$
}

        return null;
}

/**
//Synthetic comment -- @@ -138,7 +190,8 @@

/**
* Returns whether the loader has received requests to load custom views.
     * <p/>This allows to efficiently only recreate when needed upon code change in the project.
*/
public boolean isUsed() {
return mUsed;
//Synthetic comment -- @@ -153,9 +206,77 @@
* @throws Exception
*/
@SuppressWarnings("unchecked")
    private Object instantiateClass(Class<?> clazz, Class[] constructorSignature,
Object[] constructorParameters) throws Exception {
        Constructor<?> constructor = clazz.getConstructor(constructorSignature);
constructor.setAccessible(true);
return constructor.newInstance(constructorParameters);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java
//Synthetic comment -- index ce9f2c0..098cb8c 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.swt.graphics.Rectangle;

//Synthetic comment -- @@ -131,6 +132,14 @@
return null;
}

String name = gre.callGetDisplayName(canvasViewInfo.getUiViewKey());

if (name == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 0659d2d..7ecd0fa 100755

//Synthetic comment -- @@ -21,14 +21,14 @@
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomToggle;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
//Synthetic comment -- @@ -39,9 +39,9 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutBridge;
//Synthetic comment -- @@ -51,6 +51,7 @@
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -59,16 +60,29 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -89,8 +103,10 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/**
* Graphical layout editor part, version 2.
//Synthetic comment -- @@ -281,6 +297,7 @@
mErrorLabel.setEditable(false);
mErrorLabel.setBackground(d.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
mErrorLabel.setForeground(d.getSystemColor(SWT.COLOR_INFO_FOREGROUND));

mSashPalette.setWeights(new int[] { 20, 80 });
mSashError.setWeights(new int[] { 80, 20 });
//Synthetic comment -- @@ -323,22 +340,6 @@

}

    /**
     * Switches the stack to display the error label and hide the canvas.
     * @param errorFormat The new error to display if not null.
     * @param parameters String.format parameters for the error format.
     */
    private void displayError(String errorFormat, Object...parameters) {
        if (errorFormat != null) {
            mErrorLabel.setText(String.format(errorFormat, parameters));
        }
        mSashError.setMaximizedControl(null);
    }

    /** Displays the canvas and hides the error label. */
    private void hideError() {
        mSashError.setMaximizedControl(mCanvasViewer.getControl());
    }

@Override
public void dispose() {
//Synthetic comment -- @@ -944,7 +945,8 @@
UiDocumentNode model = getModel();

if (model.getUiChildren().size() == 0) {
                    displayError("No XML content. Please add a root view or layout to your document.");

// Although we display an error, we still treat an empty document as a
// successful layout result so that we can drop new elements in it.
//Synthetic comment -- @@ -977,105 +979,7 @@
LayoutBridge bridge = data.getLayoutBridge();

if (bridge.bridge != null) { // bridge can never be null.
                    ResourceManager resManager = ResourceManager.getInstance();

                    ProjectResources projectRes = resManager.getProjectResources(iProject);
                    if (projectRes == null) {
                        displayError("Missing project resources.");
                        return;
                    }

                    // get the resources of the file's project.
                    Map<String, Map<String, IResourceValue>> configuredProjectRes =
                        mConfigListener.getConfiguredProjectResources();

                    // get the framework resources
                    Map<String, Map<String, IResourceValue>> frameworkResources =
                        mConfigListener.getConfiguredFrameworkResources();

                    if (configuredProjectRes != null && frameworkResources != null) {
                        if (mProjectCallback == null) {
                            mProjectCallback = new ProjectCallback(
                                    bridge.classLoader, projectRes, iProject);
                        }

                        if (mLogger == null) {
                            mLogger = new ILayoutLog() {
                                public void error(String message) {
                                    AdtPlugin.printErrorToConsole(mEditedFile.getName(), message);
                                }

                                public void error(Throwable error) {
                                    String message = error.getMessage();
                                    if (message == null) {
                                        message = error.getClass().getName();
                                    }

                                    PrintStream ps = new PrintStream(AdtPlugin.getErrorStream());
                                    error.printStackTrace(ps);
                                }

                                public void warning(String message) {
                                    AdtPlugin.printToConsole(mEditedFile.getName(), message);
                                }
                            };
                        }

                        // get the selected theme
                        String theme = mConfigComposite.getTheme();
                        if (theme != null) {
                            // Compute the layout
                            Rectangle rect = getBounds();

                            int width = rect.width;
                            int height = rect.height;
                            if (mUseExplodeMode) {
                                // compute how many padding in x and y will bump the screen size
                                List<UiElementNode> children = getModel().getUiChildren();
                                if (children.size() == 1) {
                                    ExplodedRenderingHelper helper = new ExplodedRenderingHelper(
                                            children.get(0).getXmlNode(), iProject);

                                    // there are 2 paddings for each view
                                    // left and right, or top and bottom.
                                    int paddingValue = ExplodedRenderingHelper.PADDING_VALUE * 2;

                                    width += helper.getWidthPadding() * paddingValue;
                                    height += helper.getHeightPadding() * paddingValue;
                                }
                            }

                            int density = mConfigComposite.getDensity().getDpiValue();
                            float xdpi = mConfigComposite.getXDpi();
                            float ydpi = mConfigComposite.getYDpi();
                            boolean isProjectTheme = mConfigComposite.isProjectTheme();

                            UiElementPullParser parser = new UiElementPullParser(getModel(),
                                    mUseExplodeMode, density, xdpi, iProject);

                            ILayoutResult result = computeLayout(bridge, parser,
                                    iProject /* projectKey */,
                                    width, height, !mConfigComposite.getClipping(),
                                    density, xdpi, ydpi,
                                    theme, isProjectTheme,
                                    configuredProjectRes, frameworkResources, mProjectCallback,
                                    mLogger);

                            // post rendering clean up
                            bridge.cleanUp();

                            mCanvasViewer.getCanvas().setResult(result);

                            // update the UiElementNode with the layout info.
                            if (result.getSuccess() == ILayoutResult.SUCCESS) {
                                hideError();
                            } else {
                                displayError(result.getErrorMessage());
                            }

                            model.refreshUi();
                        }
                    }
} else {
// SDK is loaded but not the layout library!

//Synthetic comment -- @@ -1098,6 +1002,133 @@
}
}

/**
* Computes a layout by calling the correct computeLayout method of ILayoutBridge based on
* the implementation API level.
//Synthetic comment -- @@ -1264,4 +1295,281 @@
}
}
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IProjectCallback.java b/layoutlib_api/src/com/android/layoutlib/api/IProjectCallback.java
//Synthetic comment -- index 5ad5082..fbdd918 100644

//Synthetic comment -- @@ -22,36 +22,36 @@
* resource resolution, namespace information, and instantiation of custom view.
*/
public interface IProjectCallback {
    
/**
* Loads a custom view with the given constructor signature and arguments.
* @param name The fully qualified name of the class.
* @param constructorSignature The signature of the class to use
* @param constructorArgs The arguments to use on the constructor
* @return A newly instantiated android.view.View object.
     * @throws ClassNotFoundException.
     * @throws Exception 
*/
@SuppressWarnings("unchecked")
Object loadView(String name, Class[] constructorSignature, Object[] constructorArgs)
throws ClassNotFoundException, Exception;
    
/**
* Returns the namespace of the application.
* <p/>This lets the Layout Lib load custom attributes for custom views.
*/
String getNamespace();
    
/**
* Resolves the id of a resource Id.
* <p/>The resource id is the value of a <code>R.&lt;type&gt;.&lt;name&gt;</code>, and
* this method will return both the type and name of the resource.
* @param id the Id to resolve.
* @return an array of 2 strings containing the resource name and type, or null if the id
     * does not match any resource. 
*/
String[] resolveResourceValue(int id);
    
/**
* Resolves the id of a resource Id of type int[]
* <p/>The resource id is the value of a R.styleable.&lt;name&gt;, and this method will
//Synthetic comment -- @@ -60,7 +60,7 @@
* @return the name of the resource or <code>null</code> if not found.
*/
String resolveResourceValue(int[] id);
    
/**
* Returns the id of a resource.
* <p/>The provided type and name must match an existing constant defined as








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 824719a..c643d92 100644

//Synthetic comment -- @@ -329,7 +329,9 @@
"android.preference." + CLASS_NAME_PREFERENCE_SCREEN; //$NON-NLS-1$
public final static String CLASS_PREFERENCEGROUP = "android.preference.PreferenceGroup"; //$NON-NLS-1$
public final static String CLASS_PARCELABLE = "android.os.Parcelable"; //$NON-NLS-1$



/** Returns the appropriate name for the 'android' command, which is 'android.bat' for







