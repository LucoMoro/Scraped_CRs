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
* Opens the given file and shows the given (optional) region in the editor (or
* if no region is specified, opens the editor tab.)
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index caf617a..eb80984 100644

//Synthetic comment -- @@ -68,6 +68,8 @@
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Attr;
//Synthetic comment -- @@ -1298,4 +1300,31 @@
}
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java
//Synthetic comment -- index 91df3e9..5ef7d7e 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt;

import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.ddms.ISourceRevealer;
//Synthetic comment -- @@ -27,7 +29,11 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
//Synthetic comment -- @@ -35,6 +41,7 @@
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
//Synthetic comment -- @@ -115,6 +122,41 @@
if (filteredMatches.size() == 1) {
return revealLineMatch(filteredMatches, fileName, lineNumber, perspective);
}
}

return displayMethod((IMethod) methodMatches.get(0).getElement(), perspective);
//Synthetic comment -- @@ -302,6 +344,11 @@
}

private List<SearchMatch> searchForMethod(String fqmn) {
return searchForPattern(fqmn, IJavaSearchConstants.METHOD, MATCH_IS_METHOD_PREDICATE);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index adf59ad..7a4b5ba 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectClassLoader;
//Synthetic comment -- @@ -178,6 +179,11 @@
AdtPlugin.log(e, "%1$s failed to instantiate.", className); //$NON-NLS-1$

// Add the missing class to the list so that the renderer can print them later.
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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
//Synthetic comment -- @@ -1671,8 +1671,10 @@
if (logger.hasProblems()) {
displayLoggerProblems(iProject, logger);
displayFailingClasses(missingClasses, brokenClasses, true);
} else if (missingClasses.size() > 0 || brokenClasses.size() > 0) {
displayFailingClasses(missingClasses, brokenClasses, false);
} else {
// Nope, no missing or broken classes. Clear success, congrats!
hideError();
//Synthetic comment -- @@ -1899,6 +1901,71 @@
mSashError.setMaximizedControl(mCanvasViewer.getControl());
}

/**
* Switches the sash to display the error label to show a list of
* missing classes and give options to create them.
//Synthetic comment -- @@ -1968,7 +2035,7 @@
addText(mErrorLabel, "See the Error Log (Window > Show View) for more details.\n");

if (haveCustomClass) {
                addText(mErrorLabel, "Tip: Use View.isInEditMode() in your custom views "
+ "to skip code when shown in Eclipse");
}
}
//Synthetic comment -- @@ -2353,6 +2420,8 @@
private static final int IGNORE_FIDELITY_WARNING = 7;
/** Set an attribute on the given XML element to a given value  */
private static final int SET_ATTRIBUTE = 8;

/** Client data: the contents depend on the specific action */
private final Object[] mData;
//Synthetic comment -- @@ -2384,6 +2453,17 @@
case LINK_OPEN_CLASS:
AdtPlugin.openJavaClass(getProject(), (String) mData[0]);
break;
case LINK_SHOW_LOG:
IWorkbench workbench = PlatformUI.getWorkbench();
IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index 9b0d7a6..7b9cb55 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.eclipse.adt.AdtPlugin;

//Synthetic comment -- @@ -30,7 +32,7 @@
* A {@link LayoutLog} which records the problems it encounters and offers them as a
* single summary at the end
*/
class RenderLogger extends LayoutLog {
static final String TAG_MISSING_DIMENSION = "missing.dimension";     //$NON-NLS-1$

private final String mName;
//Synthetic comment -- @@ -39,6 +41,7 @@
private List<String> mErrors;
private boolean mHaveExceptions;
private List<String> mTags;
private static Set<String> sIgnoredFidelityWarnings;

/** Construct a logger for the given named layout */
//Synthetic comment -- @@ -57,12 +60,23 @@
}

/**
* Returns a (possibly multi-line) description of all the problems
*
* @param includeFidelityWarnings if true, include fidelity warnings in the problem
*            summary
* @return a string describing the rendering problems
*/
public String getProblems(boolean includeFidelityWarnings) {
StringBuilder sb = new StringBuilder();

//Synthetic comment -- @@ -98,6 +112,7 @@
*
* @return the fidelity warnings
*/
public List<String> getFidelityWarnings() {
return mFidelityWarnings;
}
//Synthetic comment -- @@ -135,12 +150,29 @@
return;
}

mHaveExceptions = true;
}

addError(tag, description);
}

@Override
public void warning(String tag, String message, Object data) {
String description = describe(message);
//Synthetic comment -- @@ -188,15 +220,13 @@
sIgnoredFidelityWarnings.add(message);
}

    private String describe(String message) {
        StringBuilder sb = new StringBuilder();
        if (message != null) {
            if (sb.length() > 0) {
                sb.append(": ");
            }
            sb.append(message);
}
        return sb.toString();
}

private void addWarning(String tag, String description) {







