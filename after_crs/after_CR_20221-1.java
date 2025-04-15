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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -219,8 +218,6 @@
private Map<String, Map<String, ResourceValue>> mConfiguredFrameworkRes;
private Map<String, Map<String, ResourceValue>> mConfiguredProjectRes;
private ProjectCallback mProjectCallback;
private boolean mNeedsRecompute = false;

private TargetListener mTargetListener;
//Synthetic comment -- @@ -1189,8 +1186,7 @@

/**
* Renders the given model, using this editor's theme and screen settings, and returns
     * the result as a {@link RenderSession}.
*
* @param model the model to be rendered, which can be different than the editor's own
*            {@link #getModel()}.
//Synthetic comment -- @@ -1202,7 +1198,7 @@
* @param transparentBackground If true, the rendering will <b>not</b> paint the
*            normal background requested by the theme, and it will instead paint the
*            background using a fully transparent background color
     * @return the resulting rendered image wrapped in an {@link RenderSession}
*/
public RenderSession render(UiDocumentNode model, int width, int height,
Set<UiElementNode> explodeNodes, boolean transparentBackground) {
//Synthetic comment -- @@ -1216,7 +1212,7 @@

IProject iProject = mEditedFile.getProject();
return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
                transparentBackground, new LayoutLog());
}

/**
//Synthetic comment -- @@ -1436,16 +1432,23 @@
int width = rect.width;
int height = rect.height;

        RenderLogger logger = new RenderLogger(mEditedFile.getName());

RenderSession session = renderWithBridge(iProject, model, layoutLib, width, height,
                explodeNodes, false, logger);

canvas.setSession(session, explodeNodes);

// update the UiElementNode with the layout info.
if (session.getResult().isSuccess() == false) {
            // An error was generated. Print it (and any other accumulated warnings)
            String errorMessage = session.getResult().getErrorMessage();
            if (errorMessage != null && errorMessage.length() > 0) {
                logger.error(null, session.getResult().getErrorMessage());
            } else if (!logger.hasProblems()) {
                logger.error(null, "Unexpected error in rendering, no details given");
            }
            displayError(logger.getProblems());
} else {
// Success means there was no exception. But we might have detected
// some missing classes and swapped them by a mock view.
//Synthetic comment -- @@ -1453,6 +1456,8 @@
Set<String> brokenClasses = mProjectCallback.getUninstantiatableClasses();
if (missingClasses.size() > 0 || brokenClasses.size() > 0) {
displayFailingClasses(missingClasses, brokenClasses);
            } else if (logger.hasProblems()) {
                displayError(logger.getProblems());
} else {
// Nope, no missing or broken classes. Clear success, congrats!
hideError();
//Synthetic comment -- @@ -1464,7 +1469,7 @@

private RenderSession renderWithBridge(IProject iProject, UiDocumentNode model,
LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
            boolean transparentBackground, LayoutLog logger) {
ResourceManager resManager = ResourceManager.getInstance();

ProjectResources projectRes = resManager.getProjectResources(iProject);
//Synthetic comment -- @@ -1501,45 +1506,6 @@
mProjectCallback.getMissingClasses().clear();
}

// get the selected theme
String theme = mConfigComposite.getTheme();
if (theme == null) {
//Synthetic comment -- @@ -1621,7 +1587,7 @@
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,
                logger);

if (transparentBackground) {
// It doesn't matter what the background color is as long as the alpha
//Synthetic comment -- @@ -1839,12 +1805,7 @@
sr.start = start;
sr.length = link.length();
sr.fontStyle = SWT.NORMAL;
        sr.underlineStyle = SWT.UNDERLINE_LINK;
sr.underline = true;
styledText.setStyleRange(sr);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..d7c5af4

//Synthetic comment -- @@ -0,0 +1,172 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.eclipse.adt.AdtPlugin;

import org.eclipse.core.runtime.IStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link LayoutLog} which records the problems it encounters and offers them as a
 * single summary at the end
 */
class RenderLogger extends LayoutLog {
    private final String mName;
    private List<String> mFidelityWarnings;
    private List<String> mWarnings;
    private List<String> mErrors;
    private boolean mHaveExceptions;

    /** Construct a logger for the given named layout */
    RenderLogger(String name) {
        mName = name;
    }

    /**
     * Are there any logged errors or warnings during the render?
     *
     * @return true if there were problems during the render
     */
    public boolean hasProblems() {
        return mFidelityWarnings != null || mErrors != null || mWarnings != null ||
            mHaveExceptions;
    }

    /**
     * Returns a (possibly multi-line) description of all the problems
     *
     * @return a string describing the rendering problems
     */
    public String getProblems() {
        StringBuilder sb = new StringBuilder();

        if (mErrors != null) {
            for (String error : mErrors) {
                sb.append(error).append('\n');
            }
        }

        if (mWarnings != null) {
            for (String warning : mWarnings) {
                sb.append(warning).append('\n');
            }
        }

        if (mFidelityWarnings != null) {
            sb.append("The graphics preview may not be accurate:\n");
            for (String warning : mFidelityWarnings) {
                sb.append("* ");
                sb.append(warning).append('\n');
            }
        }

        if (mHaveExceptions) {
            sb.append("Exception details are logged in Window > Show View > Error Log");
        }

        return sb.toString();
    }

    // ---- extends LayoutLog ----

    @Override
    public void error(String tag, String message) {
        String description = describe(tag, message);
        AdtPlugin.log(IStatus.ERROR, "%1$s: %2$s", mName, description);

        addError(description);
    }

    @Override
    public void error(String tag, Throwable throwable) {
        AdtPlugin.log(throwable, "%1$s: %2$s", mName, tag);
        assert throwable != null;
        mHaveExceptions = true;

        String message = throwable.getMessage();
        if (message == null) {
            message = throwable.getClass().getName();
        } else if (tag == null && throwable instanceof ClassNotFoundException
                && !message.contains(ClassNotFoundException.class.getSimpleName())) {
            tag = ClassNotFoundException.class.getSimpleName();
        }
        String description = describe(tag, message);
        addError(description);
    }

    @Override
    public void error(String tag, String message, Throwable throwable) {
        String description = describe(tag, message);
        AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);
        if (throwable != null) {
            mHaveExceptions = true;
        }

        addError(description);
    }

    @Override
    public void warning(String tag, String message) {
        String description = describe(tag, message);
        AdtPlugin.log(IStatus.WARNING, "%1$s: %2$s", mName, description);
        addWarning(description);
    }

    @Override
    public void fidelityWarning(String tag, String message, Throwable throwable) {
        String description = describe(tag, message);
        AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);
        if (throwable != null) {
            mHaveExceptions = true;
        }

        addFidelityWarning(description);
    }

    private String describe(String tag, String message) {
        StringBuilder sb = new StringBuilder();
        if (tag != null) {
            sb.append(tag);
        }
        if (message != null) {
            if (sb.length() > 0) {
                sb.append(": ");
            }
            sb.append(message);
        }
        return sb.toString();
    }

    private void addWarning(String description) {
        if (mWarnings == null) {
            mWarnings = new ArrayList<String>();
        } else if (mWarnings.contains(description)) {
            // Avoid duplicates
            return;
        }
        mWarnings.add(description);
    }

    private void addError(String description) {
        if (mErrors == null) {
            mErrors = new ArrayList<String>();
        } else if (mErrors.contains(description)) {
            // Avoid duplicates
            return;
        }
        mErrors.add(description);
    }

    private void addFidelityWarning(String description) {
        if (mFidelityWarnings == null) {
            mFidelityWarnings = new ArrayList<String>();
        } else if (mFidelityWarnings.contains(description)) {
            // Avoid duplicates
            return;
        }
        mFidelityWarnings.add(description);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLoggerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLoggerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d391a04

//Synthetic comment -- @@ -0,0 +1,46 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import junit.framework.TestCase;

public class RenderLoggerTest extends TestCase {
    public void testLogger1() throws Exception {
        RenderLogger l = new RenderLogger("foo");
        assertFalse(l.hasProblems());
    }

    public void testLogger2() throws Exception {
        RenderLogger l = new RenderLogger("foo");
        assertFalse(l.hasProblems());
        l.fidelityWarning(null, "No perspective Transforms", null);
        l.fidelityWarning(null, "No GPS", null);
        assertTrue(l.hasProblems());
        assertEquals("The graphics preview may not be accurate:\n"
                + "* No perspective Transforms\n" + "* No GPS\n", l.getProblems());
    }

    public void testLogger3() throws Exception {
        RenderLogger l = new RenderLogger("foo");
        assertFalse(l.hasProblems());
        l.error("timeout", "Sample Error", new RuntimeException());
        l.warning("slow", "Sample warning");
        assertTrue(l.hasProblems());
        assertEquals("timeout: Sample Error\n" + "slow: Sample warning\n"
                + "Exception details are logged in Window > Show View > Error Log", l.getProblems());
    }
}







