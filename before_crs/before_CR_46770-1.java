/*40161: Make the new names for imported projects editable

Also pick up the old Eclipse project name if importing an
Eclipse project.

Change-Id:I138195f89a1631f5b9b332973a3a2b655ae5c2de*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportPage.java
//Synthetic comment -- index f503c1e..6e63107 100644

//Synthetic comment -- @@ -24,18 +24,24 @@
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -43,7 +49,7 @@
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
//Synthetic comment -- @@ -52,6 +58,7 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;
//Synthetic comment -- @@ -63,7 +70,10 @@

/** WizardPage for importing Android projects */
class ImportPage extends WizardPage implements SelectionListener, IStructuredContentProvider,
        ICheckStateListener, ILabelProvider, IColorProvider, KeyListener, TraverseListener {
private final NewProjectWizardState mValues;
private List<ImportedProject> mProjectPaths;
private final IProject[] mExistingProjects;
//Synthetic comment -- @@ -120,15 +130,29 @@
projectsLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
projectsLabel.setText("Projects:");

        mCheckboxTableViewer = CheckboxTableViewer.newCheckList(container,
                SWT.BORDER | SWT.FULL_SELECTION);
        mTable = mCheckboxTableViewer.getTable();
mTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));
mTable.addSelectionListener(this);
        mCheckboxTableViewer.setLabelProvider(this);
mCheckboxTableViewer.setContentProvider(this);
mCheckboxTableViewer.setInput(this);
mCheckboxTableViewer.addCheckStateListener(this);

mSelectAllButton = new Button(container, SWT.NONE);
mSelectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//Synthetic comment -- @@ -153,6 +177,21 @@

Composite group = mWorkingSetGroup.createControl(container);
group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
}

@Override
//Synthetic comment -- @@ -196,21 +235,25 @@

private List<ImportedProject> searchForProjects(File dir) {
List<ImportedProject> projects = new ArrayList<ImportedProject>();
        addProjects(dir, projects);
return projects;
}

/** Finds all project directories under the given directory */
    private void addProjects(File dir, List<ImportedProject> projects) {
if (dir.isDirectory()) {
if (LintUtils.isProjectDir(dir)) {
                projects.add(new ImportedProject(dir));
}

File[] children = dir.listFiles();
if (children != null) {
for (File child : children) {
                    addProjects(child, projects);
}
}
}
//Synthetic comment -- @@ -234,6 +277,21 @@
String.format("Cannot import %1$s because the project name is in use",
project.getProjectName()));
break;
}
}
}
//Synthetic comment -- @@ -365,48 +423,85 @@
}
mValues.importProjects = selected;
validatePage();
}

    // ---- Implements ILabelProvider ----

@Override
    public void addListener(ILabelProviderListener listener) {
}

@Override
    public void removeListener(ILabelProviderListener listener) {
}

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
}

    @Override
    public Image getImage(Object element) {
        return null;
    }

    @Override
    public String getText(Object element) {
        ImportedProject file = (ImportedProject) element;
        return String.format("%1$s (%2$s)", file.getProjectName(), file.getLocation().getPath());
    }

    // ---- IColorProvider ----

    @Override
    public Color getForeground(Object element) {
        Display display = mTable.getDisplay();
        if (mCheckboxTableViewer.getGrayed(element)) {
            return display.getSystemColor(SWT.COLOR_DARK_GRAY);
}

        return display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
    }

    @Override
    public Color getBackground(Object element) {
        return mTable.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportedProject.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportedProject.java
//Synthetic comment -- index 064aa34..74af651 100644

//Synthetic comment -- @@ -15,27 +15,36 @@
*/
package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.xml.AndroidManifestParser;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.common.xml.ManifestData.Activity;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.io.FolderWrapper;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -45,16 +54,22 @@
private String mActivityName;
private ManifestData mManifest;
private String mProjectName;

    ImportedProject(File location) {
super();
mLocation = location;
}

File getLocation() {
return mLocation;
}

@Nullable
ManifestData getManifest() {
if (mManifest == null) {
//Synthetic comment -- @@ -104,6 +119,12 @@
@NonNull
public String getProjectName() {
if (mProjectName == null) {
String activityName = getActivityName();
if (activityName == null || activityName.isEmpty()) {
// I could also look at the build files, say build.xml from ant, and
//Synthetic comment -- @@ -136,6 +157,37 @@
return mProjectName;
}

public IAndroidTarget getTarget() {
// Pick a target:
// First try to find the one requested by project.properties







