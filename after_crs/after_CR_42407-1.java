/*Improve render exception error messages

When exceptions are encountered during layout rendering, the user
experience isn't very good - the exceptions are sent to the IDE log,
and the layout editor view adds the error message from the exception
(which can sometimes be something like just "2", which is the case for
an ArrayIndexOutOfBoundsException for example).

This changeset improves this a bit:

* First, when the error message is just an exception error message,
  it's prefixed by a message stating that an exception was raised
  during layout rendering.

* Second, the first exception encountered is now shown in the layout
  editor itself. Only the frames that are part of the android view
  hierarchy is shown; all the frames from layoutlib and on down into
  the IDE are omitted. Frames that are probably part of the user's
  code (meaning they're not in the android.* or java.* namespaces) are
  hyperlinkable.

* This also includes exceptions encountered during class
  initialization. In this case, the tip message that View#isInEditMode
  can be used to do conditional code is displayed in bold.

This changeset also fixes some bugs in the SourceRevealer such that it
can handle constructors, and such that it will use the line number to
pinpoint a line within a method (as long as the line number is in the
correct range).

Change-Id:I43b635eb24b8e0e64988958c56bdb7dbc1af7221*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 71d02d0..de043aa 100644

//Synthetic comment -- @@ -1924,6 +1924,21 @@
}

/**
     * For a stack trace entry, specifying a class, method, and optionally
     * fileName and line number, open the corresponding line in the editor.
     *
     * @param fqcn the fully qualified name of the class
     * @param method the method name
     * @param fileName the file name, or null
     * @param lineNumber the line number or -1
     * @return true if the target location could be opened, false otherwise
     */
    public static boolean openStackTraceLine(@Nullable String fqcn,
            @Nullable String method, @Nullable String fileName, int lineNumber) {
        return new SourceRevealer().revealMethod(fqcn + '.' + method, fileName, lineNumber, null);
    }

    /**
* Opens the given file and shows the given (optional) region in the editor (or
* if no region is specified, opens the editor tab.)
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index caf617a..eb80984 100644

//Synthetic comment -- @@ -68,6 +68,8 @@
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Attr;
//Synthetic comment -- @@ -1298,4 +1300,31 @@
}
}
}

    /**
     * Returns the offset region of the given 0-based line number in the given
     * file
     *
     * @param file the file to look up the line number in
     * @param line the line number (0-based, meaning that the first line is line
     *            0)
     * @return the corresponding offset range, or null
     */
    @Nullable
    public static IRegion getRegionOfLine(@NonNull IFile file, int line) {
        IDocumentProvider provider = new TextFileDocumentProvider();
        try {
            provider.connect(file);
            IDocument document = provider.getDocument(file);
            if (document != null) {
                return document.getLineInformation(line);
            }
        } catch (Exception e) {
            AdtPlugin.log(e, "Can't find range information for %1$s", file.getName());
        } finally {
            provider.disconnect(file);
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java
//Synthetic comment -- index 91df3e9..5ef7d7e 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;

import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.ddms.ISourceRevealer;
//Synthetic comment -- @@ -27,7 +29,11 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
//Synthetic comment -- @@ -35,6 +41,7 @@
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
//Synthetic comment -- @@ -115,6 +122,41 @@
if (filteredMatches.size() == 1) {
return revealLineMatch(filteredMatches, fileName, lineNumber, perspective);
}
            } else if (fileName != null && lineNumber > 0) {
                // Couldn't find file match, but we have a filename and line number: attempt
                // to use this to pinpoint the location within the method
                IMethod method = (IMethod) methodMatches.get(0).getElement();
                IJavaElement element = method;
                while (element != null) {
                    if (element instanceof ICompilationUnit) {
                        ICompilationUnit unit = ((ICompilationUnit) element).getPrimary();
                        IResource resource = unit.getResource();
                        if (resource instanceof IFile) {
                            IFile file = (IFile) resource;

                            try {
                                // See if the line number looks like it's inside the given method
                                ISourceRange sourceRange = method.getSourceRange();
                                IRegion region = AdtUtils.getRegionOfLine(file, lineNumber - 1);
                                if (region != null
                                        && region.getOffset() >= sourceRange.getOffset()
                                        && region.getOffset() < sourceRange.getOffset()
                                            + sourceRange.getLength()) {
                                    // Yes: use the line number instead
                                    if (perspective != null) {
                                        SourceRevealer.switchToPerspective(perspective);
                                    }
                                    return displayFile(file, lineNumber);
                                }

                            } catch (JavaModelException e) {
                                AdtPlugin.log(e, null);
                            }
                        }
                    }
                    element = element.getParent();
                }

}

return displayMethod((IMethod) methodMatches.get(0).getElement(), perspective);
//Synthetic comment -- @@ -302,6 +344,11 @@
}

private List<SearchMatch> searchForMethod(String fqmn) {
        if (fqmn.endsWith(CONSTRUCTOR_NAME)) {
            fqmn = fqmn.substring(0, fqmn.length() - CONSTRUCTOR_NAME.length() - 1); // -1: dot
            return searchForPattern(fqmn, IJavaSearchConstants.CONSTRUCTOR,
                    MATCH_IS_METHOD_PREDICATE);
        }
return searchForPattern(fqmn, IJavaSearchConstants.METHOD, MATCH_IS_METHOD_PREDICATE);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index adf59ad..7a4b5ba 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderLogger;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectClassLoader;
//Synthetic comment -- @@ -178,6 +179,11 @@
AdtPlugin.log(e, "%1$s failed to instantiate.", className); //$NON-NLS-1$

// Add the missing class to the list so that the renderer can print them later.
            if (mLogger instanceof RenderLogger) {
                RenderLogger renderLogger = (RenderLogger) mLogger;
                renderLogger.recordThrowable(e);

            }
mBrokenClasses.add(className);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 27de6e1..ded2f76 100644

//Synthetic comment -- @@ -30,7 +30,6 @@
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor.viewNeedsPackage;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_EAST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_WEST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.STATE_COLLAPSED;
//Synthetic comment -- @@ -116,6 +115,7 @@
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
//Synthetic comment -- @@ -1671,8 +1671,10 @@
if (logger.hasProblems()) {
displayLoggerProblems(iProject, logger);
displayFailingClasses(missingClasses, brokenClasses, true);
            displayUserStackTrace(logger, true);
} else if (missingClasses.size() > 0 || brokenClasses.size() > 0) {
displayFailingClasses(missingClasses, brokenClasses, false);
            displayUserStackTrace(logger, true);
} else {
// Nope, no missing or broken classes. Clear success, congrats!
hideError();
//Synthetic comment -- @@ -1899,6 +1901,71 @@
mSashError.setMaximizedControl(mCanvasViewer.getControl());
}

    /** Display the problem list encountered during a render */
    private void displayUserStackTrace(RenderLogger logger, boolean append) {
        List<Throwable> throwables = logger.getFirstTrace();
        if (throwables == null || throwables.isEmpty()) {
            return;
        }

        Throwable throwable = throwables.get(0);
        StackTraceElement[] frames = throwable.getStackTrace();
        int end = -1;
        boolean haveInterestingFrame = false;
        for (int i = 0; i < frames.length; i++) {
            StackTraceElement frame = frames[i];
            if (isInterestingFrame(frame)) {
                haveInterestingFrame = true;
            }
            String className = frame.getClassName();
            if (className.equals(
                    "com.android.layoutlib.bridge.impl.RenderSessionImpl")) { //$NON-NLS-1$
                end = i;
                break;
            }
        }

        if (end == -1 || !haveInterestingFrame) {
            // Not a recognized stack trace range: just skip it
            return;
        }

        if (!append) {
            mErrorLabel.setText("\n");    //$NON-NLS-1$
        } else {
            addText(mErrorLabel, "\n\n"); //$NON-NLS-1$
        }

        addText(mErrorLabel, throwable.toString() + '\n');
        for (int i = 0; i < end; i++) {
            StackTraceElement frame = frames[i];
            String className = frame.getClassName();
            String methodName = frame.getMethodName();
            addText(mErrorLabel, "    at " + className + '.' + methodName + '(');
            String fileName = frame.getFileName();
            if (fileName != null && !fileName.isEmpty()) {
                int lineNumber = frame.getLineNumber();
                String location = fileName + ':' + lineNumber;
                if (isInterestingFrame(frame)) {
                    addActionLink(mErrorLabel, ActionLinkStyleRange.LINK_OPEN_LINE,
                            location, className, methodName, fileName, lineNumber);
                } else {
                    addText(mErrorLabel, location);
                }
                addText(mErrorLabel, ")\n"); //$NON-NLS-1$
            }
        }
    }

    private static boolean isInterestingFrame(StackTraceElement frame) {
        String className = frame.getClassName();
        return !(className.startsWith("android.")         //$NON-NLS-1$
                || className.startsWith("com.android.")   //$NON-NLS-1$
                || className.startsWith("java.")          //$NON-NLS-1$
                || className.startsWith("javax.")         //$NON-NLS-1$
                || className.startsWith("sun."));         //$NON-NLS-1$
    }

/**
* Switches the sash to display the error label to show a list of
* missing classes and give options to create them.
//Synthetic comment -- @@ -1968,7 +2035,7 @@
addText(mErrorLabel, "See the Error Log (Window > Show View) for more details.\n");

if (haveCustomClass) {
                addBoldText(mErrorLabel, "Tip: Use View.isInEditMode() in your custom views "
+ "to skip code when shown in Eclipse");
}
}
//Synthetic comment -- @@ -2353,6 +2420,8 @@
private static final int IGNORE_FIDELITY_WARNING = 7;
/** Set an attribute on the given XML element to a given value  */
private static final int SET_ATTRIBUTE = 8;
        /** Open the given file and line number */
        private static final int LINK_OPEN_LINE = 9;

/** Client data: the contents depend on the specific action */
private final Object[] mData;
//Synthetic comment -- @@ -2384,6 +2453,17 @@
case LINK_OPEN_CLASS:
AdtPlugin.openJavaClass(getProject(), (String) mData[0]);
break;
                case LINK_OPEN_LINE:
                    boolean success = AdtPlugin.openStackTraceLine(
                            (String) mData[0],   // class
                            (String) mData[1],   // method
                            (String) mData[2],   // file
                            (Integer) mData[3]); // line
                    if (!success) {
                        MessageDialog.openError(mErrorLabel.getShell(), "Not Found",
                                String.format("Could not find %1$s.%2$s", mData[0], mData[1]));
                    }
                    break;
case LINK_SHOW_LOG:
IWorkbench workbench = PlatformUI.getWorkbench();
IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index 9b0d7a6..7b9cb55 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.eclipse.adt.AdtPlugin;

//Synthetic comment -- @@ -30,7 +32,7 @@
* A {@link LayoutLog} which records the problems it encounters and offers them as a
* single summary at the end
*/
public class RenderLogger extends LayoutLog {
static final String TAG_MISSING_DIMENSION = "missing.dimension";     //$NON-NLS-1$

private final String mName;
//Synthetic comment -- @@ -39,6 +41,7 @@
private List<String> mErrors;
private boolean mHaveExceptions;
private List<String> mTags;
    private List<Throwable> mTraces;
private static Set<String> sIgnoredFidelityWarnings;

/** Construct a logger for the given named layout */
//Synthetic comment -- @@ -57,12 +60,23 @@
}

/**
     * Returns a list of traces encountered during rendering, or null if none
     *
     * @return a list of traces encountered during rendering, or null if none
     */
    @Nullable
    public List<Throwable> getFirstTrace() {
        return mTraces;
    }

    /**
* Returns a (possibly multi-line) description of all the problems
*
* @param includeFidelityWarnings if true, include fidelity warnings in the problem
*            summary
* @return a string describing the rendering problems
*/
    @NonNull
public String getProblems(boolean includeFidelityWarnings) {
StringBuilder sb = new StringBuilder();

//Synthetic comment -- @@ -98,6 +112,7 @@
*
* @return the fidelity warnings
*/
    @Nullable
public List<String> getFidelityWarnings() {
return mFidelityWarnings;
}
//Synthetic comment -- @@ -135,12 +150,29 @@
return;
}

            if (description.equals(throwable.getLocalizedMessage()) ||
                    description.equals(throwable.getMessage())) {
                description = "Exception raised during rendering: " + description;
            }
            recordThrowable(throwable);
mHaveExceptions = true;
}

addError(tag, description);
}

    /**
     * Record that the given exception was encountered during rendering
     *
     * @param throwable the exception that was raised
     */
    public void recordThrowable(@NonNull Throwable throwable) {
        if (mTraces == null) {
            mTraces = new ArrayList<Throwable>();
        }
        mTraces.add(throwable);
    }

@Override
public void warning(String tag, String message, Object data) {
String description = describe(message);
//Synthetic comment -- @@ -188,15 +220,13 @@
sIgnoredFidelityWarnings.add(message);
}

    @NonNull
    private String describe(@Nullable String message) {
        if (message == null) {
            return "";
        } else {
            return message;
}
}

private void addWarning(String tag, String description) {







