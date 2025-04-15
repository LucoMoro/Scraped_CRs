/*Fix for issue 14189.

Now the user can set if he wants to change perspective
when he double-clicks in the LogCat view.
If he wants to change perspective, he can set which
perspective shall be opened.

Change-Id:I3ed68f3f8ad3bbfcd6d161b63a0c0f218dd778b6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/LogCatPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/LogCatPreferencePage.java
//Synthetic comment -- index 6bcec04..307dde8 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.ide.eclipse.ddms.views.LogCatView;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
//Synthetic comment -- @@ -27,8 +28,10 @@
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
* Preference Pane for LogCat.
//Synthetic comment -- @@ -36,6 +39,9 @@
public class LogCatPreferencePage extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

public LogCatPreferencePage() {
super(GRID);
setPreferenceStore(DdmsPlugin.getDefault().getPreferenceStore());
//Synthetic comment -- @@ -73,8 +79,43 @@
{ "Go to Problem (error line)", LogCatView.CHOICE_ERROR_LINE },
}, getFieldEditorParent());
addField(cfe);
}

public void init(IWorkbench workbench) {
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java
//Synthetic comment -- index 4e47365..bd8d816 100644

//Synthetic comment -- @@ -74,6 +74,12 @@
public final static String ATTR_ADBHOST_VALUE =
DdmsPlugin.PLUGIN_ID + ".adbHostValue"; //$NON-NLS-1$

/*
* (non-Javadoc)
*
//Synthetic comment -- @@ -109,6 +115,8 @@
store.setDefault(ATTR_LOGCAT_GOTO_PROBLEM, LogCatView.CHOICE_ERROR_LINE);
store.setDefault(ATTR_USE_ADBHOST, DdmPreferences.DEFAULT_USE_ADBHOST);
store.setDefault(ATTR_ADBHOST_VALUE, DdmPreferences.DEFAULT_ADBHOST_VALUE);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index 7cb9b3c..cd686e7 100644

//Synthetic comment -- @@ -52,12 +52,13 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -92,6 +93,10 @@
public static final String CHOICE_ERROR_LINE =
DdmsPlugin.PLUGIN_ID + ".logcat.ErrorLine"; //$NON-NLS-1$

private static LogCatView sThis;
private LogPanel mLogPanel;

//Synthetic comment -- @@ -435,17 +440,21 @@
}

void switchPerspective() {

        IWorkbenchWindow window = getViewSite().getWorkbenchWindow()
                .getWorkbench().getActiveWorkbenchWindow();
        String rtPerspectiveId = "org.eclipse.jdt.ui.JavaPerspective";
        IPerspectiveRegistry reg = WorkbenchPlugin.getDefault()
                .getPerspectiveRegistry();
        PerspectiveDescriptor rtPerspectiveDesc = (PerspectiveDescriptor) reg
                .findPerspectiveWithId(rtPerspectiveId);
        if (window != null) {
            IWorkbenchPage page = window.getActivePage();
            page.setPerspective(rtPerspectiveDesc);
}
}








