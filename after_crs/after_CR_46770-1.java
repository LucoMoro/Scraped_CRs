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
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -43,7 +49,7 @@
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
//Synthetic comment -- @@ -52,6 +58,7 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;
//Synthetic comment -- @@ -63,7 +70,10 @@

/** WizardPage for importing Android projects */
class ImportPage extends WizardPage implements SelectionListener, IStructuredContentProvider,
        ICheckStateListener, KeyListener, TraverseListener, ControlListener {
    private static final int DIR_COLUMN = 0;
    private static final int NAME_COLUMN = 1;

private final NewProjectWizardState mValues;
private List<ImportedProject> mProjectPaths;
private final IProject[] mExistingProjects;
//Synthetic comment -- @@ -120,15 +130,29 @@
projectsLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
projectsLabel.setText("Projects:");

        mTable = new Table(container, SWT.CHECK);
        mTable.setHeaderVisible(true);
        mCheckboxTableViewer = new CheckboxTableViewer(mTable);

        TableViewerColumn dirViewerColumn = new TableViewerColumn(mCheckboxTableViewer, SWT.NONE);
        TableColumn dirColumn = dirViewerColumn.getColumn();
        dirColumn.setWidth(200);
        dirColumn.setText("Project to Import");
        TableViewerColumn nameViewerColumn = new TableViewerColumn(mCheckboxTableViewer, SWT.NONE);
        TableColumn nameColumn = nameViewerColumn.getColumn();
        nameColumn.setWidth(200);
        nameColumn.setText("New Project Name");
        nameViewerColumn.setEditingSupport(new ProjectNameEditingSupport(mCheckboxTableViewer));

mTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));
        mTable.setLinesVisible(true);
        mTable.setHeaderVisible(true);
mTable.addSelectionListener(this);
        mTable.addControlListener(this);
mCheckboxTableViewer.setContentProvider(this);
mCheckboxTableViewer.setInput(this);
mCheckboxTableViewer.addCheckStateListener(this);
        mCheckboxTableViewer.setLabelProvider(new ProjectCellLabelProvider());

mSelectAllButton = new Button(container, SWT.NONE);
mSelectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//Synthetic comment -- @@ -153,6 +177,21 @@

Composite group = mWorkingSetGroup.createControl(container);
group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

        updateColumnWidths();
    }

    private void updateColumnWidths() {
        Rectangle r = mTable.getClientArea();
        int availableWidth = r.width;
        // Add all available size to the first column
        for (int i = 1; i < mTable.getColumnCount(); i++) {
            TableColumn column = mTable.getColumn(i);
            availableWidth -= column.getWidth();
        }
        if (availableWidth > 100) {
            mTable.getColumn(0).setWidth(availableWidth);
        }
}

@Override
//Synthetic comment -- @@ -196,21 +235,25 @@

private List<ImportedProject> searchForProjects(File dir) {
List<ImportedProject> projects = new ArrayList<ImportedProject>();
        addProjects(dir, projects, dir.getPath().length() + 1);
return projects;
}

/** Finds all project directories under the given directory */
    private void addProjects(File dir, List<ImportedProject> projects, int prefixLength) {
if (dir.isDirectory()) {
if (LintUtils.isProjectDir(dir)) {
                String relative = dir.getPath();
                if (relative.length() > prefixLength) {
                    relative = relative.substring(prefixLength);
                }
                projects.add(new ImportedProject(dir, relative));
}

File[] children = dir.listFiles();
if (children != null) {
for (File child : children) {
                    addProjects(child, projects, prefixLength);
}
}
}
//Synthetic comment -- @@ -234,6 +277,21 @@
String.format("Cannot import %1$s because the project name is in use",
project.getProjectName()));
break;
                } else {
                    status = ProjectNamePage.validateProjectName(project.getProjectName());
                    if (status != null && !status.isOK()) {
                        // Need to insert project name to make it clear which project name
                        // is in violation
                        if (mValues.importProjects.size() > 1) {
                            String message = String.format("%1$s: %2$s",
                                    project.getProjectName(), status.getMessage());
                            status = new Status(status.getSeverity(), AdtPlugin.PLUGIN_ID,
                                    message);
                        }
                        break;
                    } else {
                        status = null; // Don't leave non null status with isOK() == true
                    }
}
}
}
//Synthetic comment -- @@ -365,48 +423,85 @@
}
mValues.importProjects = selected;
validatePage();

        mCheckboxTableViewer.update(event.getElement(), null);
}

    // ---- Implements ControlListener ----

@Override
    public void controlMoved(ControlEvent e) {
}

@Override
    public void controlResized(ControlEvent e) {
        updateColumnWidths();
}

    private final class ProjectCellLabelProvider extends CellLabelProvider {
        @Override
        public void update(ViewerCell cell) {
            Object element = cell.getElement();
            int index = cell.getColumnIndex();
            ImportedProject project = (ImportedProject) element;

            Display display = mTable.getDisplay();
            Color fg;
            if (mCheckboxTableViewer.getGrayed(element)) {
                fg = display.getSystemColor(SWT.COLOR_DARK_GRAY);
            } else {
                fg = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
            }
            cell.setForeground(fg);
            cell.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));

            switch (index) {
                case DIR_COLUMN: {
                    // Directory name
                    cell.setText(project.getRelativePath());
                    return;
                }

                case NAME_COLUMN: {
                    // New name
                    cell.setText(project.getProjectName());
                    return;
                }
                default:
                    assert false : index;
            }
            cell.setText("");
        }
}

    /** Editing support for the project name column */
    private class ProjectNameEditingSupport extends EditingSupport {
        private ProjectNameEditingSupport(ColumnViewer viewer) {
            super(viewer);
}

        @Override
        protected void setValue(Object element, Object value) {
            ImportedProject project = (ImportedProject) element;
            project.setProjectName(value.toString());
            mCheckboxTableViewer.update(element, null);
            validatePage();
        }

        @Override
        protected Object getValue(Object element) {
            ImportedProject project = (ImportedProject) element;
            return project.getProjectName();
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return new TextCellEditor(mTable);
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportedProject.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ImportedProject.java
//Synthetic comment -- index 064aa34..74af651 100644

//Synthetic comment -- @@ -15,27 +15,36 @@
*/
package com.android.ide.eclipse.adt.internal.wizards.newproject;

import static com.android.SdkConstants.ATTR_NAME;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.xml.AndroidManifestParser;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.common.xml.ManifestData.Activity;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.io.FolderWrapper;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -45,16 +54,22 @@
private String mActivityName;
private ManifestData mManifest;
private String mProjectName;
    private String mRelativePath;

    ImportedProject(File location, String relativePath) {
super();
mLocation = location;
        mRelativePath = relativePath;
}

File getLocation() {
return mLocation;
}

    String getRelativePath() {
        return mRelativePath;
    }

@Nullable
ManifestData getManifest() {
if (mManifest == null) {
//Synthetic comment -- @@ -104,6 +119,12 @@
@NonNull
public String getProjectName() {
if (mProjectName == null) {
            // Are we importing an Eclipse project? If so just use the existing project name
            mProjectName = findEclipseProjectName();
            if (mProjectName != null) {
                return mProjectName;
            }

String activityName = getActivityName();
if (activityName == null || activityName.isEmpty()) {
// I could also look at the build files, say build.xml from ant, and
//Synthetic comment -- @@ -136,6 +157,37 @@
return mProjectName;
}

    @Nullable
    private String findEclipseProjectName() {
        File projectFile = new File(mLocation, ".project"); //$NON-NLS-1$
        if (projectFile.exists()) {
            String xml;
            try {
                xml = Files.toString(projectFile, Charsets.UTF_8);
                Document doc = DomUtilities.parseDocument(xml, false);
                if (doc != null) {
                    NodeList names = doc.getElementsByTagName(ATTR_NAME);
                    if (names.getLength() >= 1) {
                        Node nameElement = names.item(0);
                        String name = nameElement.getTextContent().trim();
                        if (!name.isEmpty()) {
                            return name;
                        }
                    }
                }
            } catch (IOException e) {
                // pass: don't attempt to read project name; must be some sort of unrelated
                // file with the same name, perhaps from a different editor or IDE
            }
        }

        return null;
    }

    public void setProjectName(@NonNull String newName) {
        mProjectName = newName;
    }

public IAndroidTarget getTarget() {
// Pick a target:
// First try to find the one requested by project.properties







