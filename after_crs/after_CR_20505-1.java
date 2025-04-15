/*Combine missing class errors with logger errors

When there are missing custom classes, or classes that cannot be
instantiated, a special error display is shown with hyperlinks to the
classes. However, this view does not incorporate the other logging
errors, such as resource failures.

This changeset combines the output so that you see everything --
missing and broken classes, resource warnings, and any other rendering
problems.

It also makes the hyperlinks pointing to classes in the error output
*open* the class if it already exists, and if not, continue to open
the New Class wizard as before.

Change-Id:Ic79282b8f502f03f587028040150e2cca3395fd9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 6cadd05..1783ab3 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import com.android.ide.eclipse.adt.internal.editors.ui.DecorComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -1488,20 +1489,23 @@
logger.error(null, "Unexpected error in rendering, no details given",
null /*data*/);
}
            // These errors will be included in the log warnings which are
            // displayed regardless of render success status below
        }

        // We might have detected some missing classes and swapped them by a mock view,
        // or run into fidelity warnings or missing resources, so emit all these
        // warnings
        Set<String> missingClasses = mProjectCallback.getMissingClasses();
        Set<String> brokenClasses = mProjectCallback.getUninstantiatableClasses();
        if (logger.hasProblems()) {
displayLoggerProblems(iProject, logger);
            displayFailingClasses(missingClasses, brokenClasses, true);
        } else if (missingClasses.size() > 0 || brokenClasses.size() > 0) {
            displayFailingClasses(missingClasses, brokenClasses, false);
} else {
            // Nope, no missing or broken classes. Clear success, congrats!
            hideError();
}

model.refreshUi();
//Synthetic comment -- @@ -1795,8 +1799,15 @@
* Switches the sash to display the error label to show a list of
* missing classes and give options to create them.
*/
    private void displayFailingClasses(Set<String> missingClasses, Set<String> brokenClasses,
            boolean append) {
        if (missingClasses.size() == 0 && brokenClasses.size() == 0) {
            return;
        }

        if (!append) {
            mErrorLabel.setText("");
        }
if (missingClasses.size() > 0) {
addText(mErrorLabel, "The following classes could not be found:\n");
for (String clazz : missingClasses) {
//Synthetic comment -- @@ -2095,8 +2106,10 @@
}

if (r instanceof ClassLinkStyleRange) {
                String fqcn = mErrorLabel.getText(r.start, r.start + r.length - 1);
                if (!Hyperlinks.openJavaClass(getProject(), fqcn)) {
                    createNewClass(fqcn);
                }
}

LayoutCanvas canvas = getCanvasControl();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index f593619..2fa42b1 100644

//Synthetic comment -- @@ -645,8 +645,14 @@
return null;
}

    /**
     * Opens a Java class for the given fully qualified class name
     *
     * @param project the project containing the class
     * @param fqcn the fully qualified class name of the class to be opened
     * @return true if the class was opened, false otherwise
     */
    public static boolean openJavaClass(IProject project, String fqcn) {
if (fqcn == null) {
return false;
}







