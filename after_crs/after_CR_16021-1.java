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
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IProject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
* Loader for Android Project class in order to use them in the layout editor.
//Synthetic comment -- @@ -35,6 +39,7 @@
public final class ProjectCallback implements IProjectCallback {

private final HashMap<String, Class<?>> mLoadedClasses = new HashMap<String, Class<?>>();
    private final Set<String> mMissingClasses = new TreeSet<String>();
private final IProject mProject;
private final ClassLoader mParentClassLoader;
private final ProjectResources mProjectRes;
//Synthetic comment -- @@ -47,6 +52,10 @@
mProject = project;
}

    public Set<String> getMissingClasses() {
        return mMissingClasses;
    }

/**
* {@inheritDoc}
*
//Synthetic comment -- @@ -74,12 +83,55 @@
mLoadedClasses.put(className, clazz);
return instantiateClass(clazz, constructorSignature, constructorParameters);
}
        } catch (Exception e) {
// Log this error with the class name we're trying to load and abort.
AdtPlugin.log(e, "ProjectCallback.loadView failed to find class %1$s", className); //$NON-NLS-1$

            // Add the missing class to the list so that the renderer can print them later.
            mMissingClasses.add(className);
}

        // Create a mock view instead. We don't cache it in the mLoadedClasses map.
        // If any exception is thrown, we'll return a CFN with the original class name instead.
        try {
            clazz = loader.loadClass(SdkConstants.CLASS_MOCK_VIEW);
            Object view = instantiateClass(clazz, constructorSignature, constructorParameters);

            // Set the text of the mock view to the simplified name of the custom class
            Method m = view.getClass().getMethod("setText",
                                                 new Class<?>[] { CharSequence.class });
            m.invoke(view, getShortClassName(className));
            mUsed = true;
            return view;
        } catch (Exception e) {
            // We failed to create and return a mock view.
            // Just throw back a CNF with the original class name.
            throw new ClassNotFoundException(className, e);
        }
    }

    private String getShortClassName(String fqcn) {
        // The name is typically a fully-qualified class name. Let's make it a tad shorter.

        if (fqcn.startsWith("android.")) {                                      // $NON-NLS-1$
            // For android classes, convert android.foo.Name to android...Name
            int first = fqcn.indexOf('.');
            int last = fqcn.lastIndexOf('.');
            if (last > first) {
                return fqcn.substring(0, first) + ".." + fqcn.substring(last);   // $NON-NLS-1$
            }
        } else {
            // For custom non-android classes, it's best to keep the 2 first segments of
            // the namespace, e.g. we want to get something like com.example...MyClass
            int first = fqcn.indexOf('.');
            first = fqcn.indexOf('.', first + 1);
            int last = fqcn.lastIndexOf('.');
            if (last > first) {
                return fqcn.substring(0, first) + ".." + fqcn.substring(last);   // $NON-NLS-1$
            }
        }

        return fqcn;
}

/**
//Synthetic comment -- @@ -138,7 +190,8 @@

/**
* Returns whether the loader has received requests to load custom views.
     * <p/>
     * This allows to efficiently only recreate when needed upon code change in the project.
*/
public boolean isUsed() {
return mUsed;
//Synthetic comment -- @@ -153,9 +206,77 @@
* @throws Exception
*/
@SuppressWarnings("unchecked")
    private Object instantiateClass(Class<?> clazz,
            Class[] constructorSignature,
Object[] constructorParameters) throws Exception {
        Constructor<?> constructor = null;

        try {
            constructor = clazz.getConstructor(constructorSignature);

        } catch (NoSuchMethodException e) {
            // Custom views can either implement a 3-parameter, 2-parameter or a
            // 1-parameter. Let's synthetically build and try all the alternatives.
            // That's kind of like switching to the other box.
            //
            // The 3-parameter constructor takes the following arguments:
            // ...(Context context, AttributeSet attrs, int defStyle)

            int n = constructorSignature.length;
            if (n == 0) {
                // There is no parameter-less constructor. Nobody should ask for one.
                throw e;
            }

            for (int i = 3; i >= 1; i--) {
                if (i == n) {
                    // Let's skip the one we know already fails
                    continue;
                }
                Class[] sig = new Class[i];
                Object[] params = new Object[i];

                int k = i;
                if (n < k) {
                    k = n;
                }
                System.arraycopy(constructorSignature, 0, sig, 0, k);
                System.arraycopy(constructorParameters, 0, params, 0, k);

                for (k++; k <= i; k++) {
                    if (k == 2) {
                        // Parameter 2 is the AttributeSet
                        sig[k-1] = clazz.getClassLoader().loadClass("android.util.AttributeSet");
                        params[k-1] = null;

                    } else if (k == 3) {
                        // Parameter 3 is the int defstyle
                        sig[k-1] = int.class;
                        params[k-1] = 0;
                    }
                }

                constructorSignature = sig;
                constructorParameters = params;

                try {
                    // Try again...
                    constructor = clazz.getConstructor(constructorSignature);
                    if (constructor != null) {
                        // Found a suitable constructor, now let's use it.
                        break;
                    }
                } catch (NoSuchMethodException e1) {
                    // pass
                }
            }

            // If all the alternatives failed, throw the initial exception.
            if (constructor == null) {
                throw e;
            }
        }

constructor.setAccessible(true);
return constructor.newInstance(constructorParameters);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java
//Synthetic comment -- index ce9f2c0..098cb8c 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.sdklib.SdkConstants;

import org.eclipse.swt.graphics.Rectangle;

//Synthetic comment -- @@ -131,6 +132,14 @@
return null;
}

        if (fqcn.equals(SdkConstants.CLASS_MOCK_VIEW)) {
            // The MockView class from the layout bridge is used to display views that
            // cannot be rendered properly (such as SurfaceView or missing custom views).
            // This view itself already displays the class name it represents so we don't
            // need to display anything here.
            return "";
        }

String name = gre.callGetDisplayName(canvasViewInfo.getUiViewKey());

if (name == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 0659d2d..7ecd0fa 100755

//Synthetic comment -- @@ -21,14 +21,14 @@
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomToggle;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
//Synthetic comment -- @@ -39,9 +39,9 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutBridge;
//Synthetic comment -- @@ -51,6 +51,7 @@
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -59,16 +60,29 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -89,8 +103,10 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* Graphical layout editor part, version 2.
//Synthetic comment -- @@ -281,6 +297,7 @@
mErrorLabel.setEditable(false);
mErrorLabel.setBackground(d.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
mErrorLabel.setForeground(d.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        mErrorLabel.addMouseListener(new ErrorLabelListener());

mSashPalette.setWeights(new int[] { 20, 80 });
mSashError.setWeights(new int[] { 80, 20 });
//Synthetic comment -- @@ -323,22 +340,6 @@

}


@Override
public void dispose() {
//Synthetic comment -- @@ -944,7 +945,8 @@
UiDocumentNode model = getModel();

if (model.getUiChildren().size() == 0) {
                    displayError(
                            "No XML content. Please add a root view or layout to your document.");

// Although we display an error, we still treat an empty document as a
// successful layout result so that we can drop new elements in it.
//Synthetic comment -- @@ -977,105 +979,7 @@
LayoutBridge bridge = data.getLayoutBridge();

if (bridge.bridge != null) { // bridge can never be null.
                    renderWithBridge(iProject, model, bridge);
} else {
// SDK is loaded but not the layout library!

//Synthetic comment -- @@ -1098,6 +1002,133 @@
}
}

    private void renderWithBridge(IProject iProject, UiDocumentNode model, LayoutBridge bridge) {
        ResourceManager resManager = ResourceManager.getInstance();

        ProjectResources projectRes = resManager.getProjectResources(iProject);
        if (projectRes == null) {
            displayError("Missing project resources.");
            return;
        }

        // Get the resources of the file's project.
        Map<String, Map<String, IResourceValue>> configuredProjectRes =
            mConfigListener.getConfiguredProjectResources();

        // Get the framework resources
        Map<String, Map<String, IResourceValue>> frameworkResources =
            mConfigListener.getConfiguredFrameworkResources();

        // Abort the rendering if the resources are not found.
        if (configuredProjectRes == null) {
            displayError("Missing project resources for current configuration.");
        }

        if (frameworkResources == null) {
            displayError("Missing framework resources.");
        }

        // Lazily create the project callback the first time we need it
        if (mProjectCallback == null) {
            mProjectCallback = new ProjectCallback(
                    bridge.classLoader, projectRes, iProject);
        } else {
            // Also clears the set of missing classes prior to rendering
            mProjectCallback.getMissingClasses().clear();
        }

        // Lazily create the logger the first time we need it
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
        if (theme == null) {
            displayError("Missing theme.");
        }

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
        if (result.getSuccess() != ILayoutResult.SUCCESS) {
            // An error was generated. Print it.
            displayError(result.getErrorMessage());

        } else {
            // Success means there was no exception. But we might have detected
            // some missing classes and swapped them by a mock view.
            Set<String> missingClasses = mProjectCallback.getMissingClasses();
            if (missingClasses.size() > 0) {
                displayMissingClasses(missingClasses);
            } else {
                // Nope, no missing classes. Clear success, congrats!
                hideError();
            }

        }

        model.refreshUi();
    }

/**
* Computes a layout by calling the correct computeLayout method of ILayoutBridge based on
* the implementation API level.
//Synthetic comment -- @@ -1264,4 +1295,281 @@
}
}
}

    // ---- Error handling ----

    /**
     * Switches the shash to display the error label.
     *
     * @param errorFormat The new error to display if not null.
     * @param parameters String.format parameters for the error format.
     */
    private void displayError(String errorFormat, Object...parameters) {
        if (errorFormat != null) {
            mErrorLabel.setText(String.format(errorFormat, parameters));
        } else {
            mErrorLabel.setText("");
        }
        mSashError.setMaximizedControl(null);
    }

    /** Displays the canvas and hides the error label. */
    private void hideError() {
        mErrorLabel.setText("");
        mSashError.setMaximizedControl(mCanvasViewer.getControl());
    }

    /**
     * Switches the shash to display the error label to show a list of
     * missing classes and give options to create them.
     */
    private void displayMissingClasses(Set<String> missingClasses) {
        mErrorLabel.setText("");
        addText(mErrorLabel, "The following classes could not be found:\n");
        for (String clazz : missingClasses) {
            addText(mErrorLabel, "- ");
            addLink(mErrorLabel, clazz);
            addText(mErrorLabel, "\n");
        }

        mSashError.setMaximizedControl(null);
    }

    /** Add a normal line of text to the styled text widget. */
    private void addText(StyledText styledText, String...string) {
        for (String s : string) {
            styledText.append(s);
        }
    }

    /**
     * Add a URL-looking link to the styled text widget.
     * <p/>
     * A mouse-click listener is setup and it interprets the link as being a missing class name.
     * The logic *must* be changed if this is used later for a different purpose.
     */
    private void addLink(StyledText styledText, String link) {
        String s = styledText.getText();
        int start = (s == null ? 0 : s.length());
        styledText.append(link);

        StyleRange sr = new StyleRange();
        sr.start = start;
        sr.length = link.length();
        sr.fontStyle = SWT.NORMAL;
        sr.underlineStyle = SWT.UNDERLINE_LINK;
        sr.underline = true;
        styledText.setStyleRange(sr);
    }

    /**
     * Monitor clicks on the error label.
     * If the click happens on a style range created by
     * {@link GraphicalEditorPart#addLink(StyledText, String)}, we assume it's about
     * a missing class and we then proceed to display the standard Eclipse class creator wizard.
     */
    private class ErrorLabelListener extends MouseAdapter {

        @Override
        public void mouseUp(MouseEvent event) {
            super.mouseUp(event);

            if (event.widget != mErrorLabel) {
                return;
            }

            int offset = mErrorLabel.getCaretOffset();

            StyleRange r = null;
            StyleRange[] ranges = mErrorLabel.getStyleRanges();
            if (ranges != null && ranges.length > 0) {
                for (StyleRange sr : ranges) {
                    if (sr.start <= offset && sr.start + sr.length > offset) {
                        r = sr;
                        break;
                    }
                }
            }

            if (r != null && r.underlineStyle == SWT.UNDERLINE_LINK) {
                String link = mErrorLabel.getText(r.start, r.start + r.length - 1);
                createNewClass(link);
            }
        }

        private void createNewClass(String fqcn) {

            int pos = fqcn.lastIndexOf('.');
            String packageName = pos < 0 ? "" : fqcn.substring(0, pos);  //$NON-NLS-1$
            String className = pos <= 0 || pos >= fqcn.length() ? "" : fqcn.substring(pos + 1); //$NON-NLS-1$

            // create the wizard page for the class creation, and configure it
            NewClassWizardPage page = new NewClassWizardPage();

            // set the parent class
            page.setSuperClass(SdkConstants.CLASS_VIEW, true /* canBeModified */);

            // get the source folders as java elements.
            IPackageFragmentRoot[] roots = getPackageFragmentRoots(mLayoutEditor.getProject(),
                    true /*include_containers*/);

            IPackageFragmentRoot currentRoot = null;
            IPackageFragment currentFragment = null;
            int packageMatchCount = -1;

            for (IPackageFragmentRoot root : roots) {
                // Get the java element for the package.
                // This method is said to always return a IPackageFragment even if the
                // underlying folder doesn't exist...
                IPackageFragment fragment = root.getPackageFragment(packageName);
                if (fragment != null && fragment.exists()) {
                    // we have a perfect match! we use it.
                    currentRoot = root;
                    currentFragment = fragment;
                    packageMatchCount = -1;
                    break;
                } else {
                    // we don't have a match. we look for the fragment with the best match
                    // (ie the closest parent package we can find)
                    try {
                        IJavaElement[] children;
                        children = root.getChildren();
                        for (IJavaElement child : children) {
                            if (child instanceof IPackageFragment) {
                                fragment = (IPackageFragment)child;
                                if (packageName.startsWith(fragment.getElementName())) {
                                    // its a match. get the number of segments
                                    String[] segments = fragment.getElementName().split("\\."); //$NON-NLS-1$
                                    if (segments.length > packageMatchCount) {
                                        packageMatchCount = segments.length;
                                        currentFragment = fragment;
                                        currentRoot = root;
                                    }
                                }
                            }
                        }
                    } catch (JavaModelException e) {
                        // Couldn't get the children: we just ignore this package root.
                    }
                }
            }

            ArrayList<IPackageFragment> createdFragments = null;

            if (currentRoot != null) {
                // if we have a perfect match, we set it and we're done.
                if (packageMatchCount == -1) {
                    page.setPackageFragmentRoot(currentRoot, true /* canBeModified*/);
                    page.setPackageFragment(currentFragment, true /* canBeModified */);
                } else {
                    // we have a partial match.
                    // create the package. We have to start with the first segment so that we
                    // know what to delete in case of a cancel.
                    try {
                        createdFragments = new ArrayList<IPackageFragment>();

                        int totalCount = packageName.split("\\.").length; //$NON-NLS-1$
                        int count = 0;
                        int index = -1;
                        // skip the matching packages
                        while (count < packageMatchCount) {
                            index = packageName.indexOf('.', index+1);
                            count++;
                        }

                        // create the rest of the segments, except for the last one as indexOf will
                        // return -1;
                        while (count < totalCount - 1) {
                            index = packageName.indexOf('.', index+1);
                            count++;
                            createdFragments.add(currentRoot.createPackageFragment(
                                    packageName.substring(0, index),
                                    true /* force*/, new NullProgressMonitor()));
                        }

                        // create the last package
                        createdFragments.add(currentRoot.createPackageFragment(
                                packageName, true /* force*/, new NullProgressMonitor()));

                        // set the root and fragment in the Wizard page
                        page.setPackageFragmentRoot(currentRoot, true /* canBeModified*/);
                        page.setPackageFragment(createdFragments.get(createdFragments.size()-1),
                                true /* canBeModified */);
                    } catch (JavaModelException e) {
                        // If we can't create the packages, there's a problem.
                        // We revert to the default package
                        for (IPackageFragmentRoot root : roots) {
                            // Get the java element for the package.
                            // This method is said to always return a IPackageFragment even if the
                            // underlying folder doesn't exist...
                            IPackageFragment fragment = root.getPackageFragment(packageName);
                            if (fragment != null && fragment.exists()) {
                                page.setPackageFragmentRoot(root, true /* canBeModified*/);
                                page.setPackageFragment(fragment, true /* canBeModified */);
                                break;
                            }
                        }
                    }
                }
            } else if (roots.length > 0) {
                // if we haven't found a valid fragment, we set the root to the first source folder.
                page.setPackageFragmentRoot(roots[0], true /* canBeModified*/);
            }

            // if we have a starting class name we use it
            if (className != null) {
                page.setTypeName(className, true /* canBeModified*/);
            }

            // create the action that will open it the wizard.
            OpenNewClassWizardAction action = new OpenNewClassWizardAction();
            action.setConfiguredWizardPage(page);
            action.run();
            IJavaElement element = action.getCreatedElement();

            if (element == null) {
                // lets delete the packages we created just for this.
                // we need to start with the leaf and go up
                if (createdFragments != null) {
                    try {
                        for (int i = createdFragments.size() - 1 ; i >= 0 ; i--) {
                            createdFragments.get(i).delete(true /* force*/,
                                                           new NullProgressMonitor());
                        }
                    } catch (JavaModelException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * Computes and return the {@link IPackageFragmentRoot}s corresponding to the source
         * folders of the specified project.
         *
         * @param project the project
         * @param include_containers True to include containers
         * @return an array of IPackageFragmentRoot.
         */
        private IPackageFragmentRoot[] getPackageFragmentRoots(IProject project,
                boolean include_containers) {
            ArrayList<IPackageFragmentRoot> result = new ArrayList<IPackageFragmentRoot>();
            try {
                IJavaProject javaProject = JavaCore.create(project);
                IPackageFragmentRoot[] roots = javaProject.getPackageFragmentRoots();
                for (int i = 0; i < roots.length; i++) {
                    IClasspathEntry entry = roots[i].getRawClasspathEntry();
                    if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE ||
                            (include_containers &&
                                    entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER)) {
                        result.add(roots[i]);
                    }
                }
            } catch (JavaModelException e) {
            }

            return result.toArray(new IPackageFragmentRoot[result.size()]);
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
     * @throws ClassNotFoundException
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
    /** MockView is part of the layoutlib bridge and used to display classes that have
     * no rendering in the graphical layout editor. */
    public final static String CLASS_MOCK_VIEW = "com.android.layoutlib.bridge.MockView"; //$NON-NLS-1$


/** Returns the appropriate name for the 'android' command, which is 'android.bat' for







