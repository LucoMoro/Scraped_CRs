/*Accumulate rendering errors and ensure they are always shown

There are some scenarios where rendering failures (such as an NPE in
the layout lib) will not appear in the error display, and it will not
appear in the Error Log.

In addition, we now get fidelity warnings from the layout library
(warning about Android code using graphics operations not supported by
the layout preview), and these should all be displayed to the user
with a proper explanation.

To solve these problems, this changeset replaces the layout logger
with a new logger which:

(1) Writes all exceptions to the AdtPlug.log(Throwable) method, which
results in the full exception being captured in the Error Log

(2) Accumulates all errors, warnings and fidelity warnings

(3) Provides a summary of ALL the problems at the end, such that they
are all displayed (in decreasing order of severity) for the user
rather than the last one clobbering them all.

The logger also records whether exceptions were logged, and if so, the
last line of the error display indicates that further details can be
found in Window > Show View > Error Log.

Finally, the old logger was a member of the GraphicalEditorPart, but
this is a lightweight object so there is no need to cache it, and in
fact we don't want other types of rendering (such as preview
rendering) popping open the error display area.

Change-Id:Ia00978a725cb7e7ec2dcfbf9e868df2a5136efc0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 97fc96a..87dc966 100644

//Synthetic comment -- @@ -126,7 +126,6 @@
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -219,8 +218,6 @@
private Map<String, Map<String, ResourceValue>> mConfiguredFrameworkRes;
private Map<String, Map<String, ResourceValue>> mConfiguredProjectRes;
private ProjectCallback mProjectCallback;
    private LayoutLog mLog;

private boolean mNeedsRecompute = false;

private TargetListener mTargetListener;
//Synthetic comment -- @@ -1189,8 +1186,7 @@

/**
* Renders the given model, using this editor's theme and screen settings, and returns
     * the result as a {@link LayoutScene}. Any error messages will be written to the
     * editor's error area.
*
* @param model the model to be rendered, which can be different than the editor's own
*            {@link #getModel()}.
//Synthetic comment -- @@ -1202,7 +1198,7 @@
* @param transparentBackground If true, the rendering will <b>not</b> paint the
*            normal background requested by the theme, and it will instead paint the
*            background using a fully transparent background color
     * @return the resulting rendered image wrapped in an {@link LayoutScene}
*/
public RenderSession render(UiDocumentNode model, int width, int height,
Set<UiElementNode> explodeNodes, boolean transparentBackground) {
//Synthetic comment -- @@ -1216,7 +1212,7 @@

IProject iProject = mEditedFile.getProject();
return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
                transparentBackground);
}

/**
//Synthetic comment -- @@ -1436,16 +1432,23 @@
int width = rect.width;
int height = rect.height;

RenderSession session = renderWithBridge(iProject, model, layoutLib, width, height,
                explodeNodes, false);

canvas.setSession(session, explodeNodes);

// update the UiElementNode with the layout info.
if (session.getResult().isSuccess() == false) {
            // An error was generated. Print it.
            displayError(session.getResult().getErrorMessage());

} else {
// Success means there was no exception. But we might have detected
// some missing classes and swapped them by a mock view.
//Synthetic comment -- @@ -1453,6 +1456,8 @@
Set<String> brokenClasses = mProjectCallback.getUninstantiatableClasses();
if (missingClasses.size() > 0 || brokenClasses.size() > 0) {
displayFailingClasses(missingClasses, brokenClasses);
} else {
// Nope, no missing or broken classes. Clear success, congrats!
hideError();
//Synthetic comment -- @@ -1464,7 +1469,7 @@

private RenderSession renderWithBridge(IProject iProject, UiDocumentNode model,
LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
            boolean transparentBackground) {
ResourceManager resManager = ResourceManager.getInstance();

ProjectResources projectRes = resManager.getProjectResources(iProject);
//Synthetic comment -- @@ -1501,45 +1506,6 @@
mProjectCallback.getMissingClasses().clear();
}

        // Lazily create the logger the first time we need it
        if (mLog == null) {
            mLog = new LayoutLog() {
                @Override
                public void error(String tag, String message) {
                    AdtPlugin.printErrorToConsole(mEditedFile.getName(), message);
                }

                @Override
                public void error(String tag, Throwable throwable) {
                    String message = throwable.getMessage();
                    if (message == null) {
                        message = throwable.getClass().getName();
                    }

                    PrintStream ps = new PrintStream(AdtPlugin.getErrorStream());
                    throwable.printStackTrace(ps);
                }

                @Override
                public void error(String tag, String message, Throwable throwable) {
                    AdtPlugin.printErrorToConsole(mEditedFile.getName(), message);

                    PrintStream ps = new PrintStream(AdtPlugin.getErrorStream());
                    throwable.printStackTrace(ps);
                }

                @Override
                public void warning(String tag, String message) {
                    AdtPlugin.printToConsole(mEditedFile.getName(), message);
                }

                @Override
                public void fidelityWarning(String tag, String message, Throwable throwable) {
                    AdtPlugin.printToConsole(mEditedFile.getName(), message);
                }
            };
        }

// get the selected theme
String theme = mConfigComposite.getTheme();
if (theme == null) {
//Synthetic comment -- @@ -1621,7 +1587,7 @@
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,
                mLog);

if (transparentBackground) {
// It doesn't matter what the background color is as long as the alpha
//Synthetic comment -- @@ -1839,12 +1805,7 @@
sr.start = start;
sr.length = link.length();
sr.fontStyle = SWT.NORMAL;
        // We want to use SWT.UNDERLINE_LINK but the constant is only
        // available when using SWT from Eclipse 3.5+
        int version = SWT.getVersion();
        if (version > 3500) {
            sr.underlineStyle = 4 /*SWT.UNDERLINE_LINK*/;
        }
sr.underline = true;
styledText.setStyleRange(sr);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..d7c5af4

//Synthetic comment -- @@ -0,0 +1,172 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLoggerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLoggerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d391a04

//Synthetic comment -- @@ -0,0 +1,46 @@







