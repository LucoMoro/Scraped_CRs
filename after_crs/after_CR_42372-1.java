/*Add Translation dialog

This CL adds a dialog to the locale menu in the configuration chooser
which makes it easy to add a new language into the set of languages
used by the project.

Also add some null annotations.

Change-Id:I70ea2f623e6c56684e7b2a51b391f472bf31529b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 1e55274..178d4be 100644

//Synthetic comment -- @@ -1089,7 +1089,8 @@
}

/**
     * Ensure that a given folder (and all its parents) are created. This implements
     * the equivalent of {@link File#mkdirs()} for {@link IContainer} folders.
*
* @param container the container to ensure exists
* @throws CoreException if an error occurs








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 1e80ce1..4324b10 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.AddTranslationDialog;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -105,6 +106,7 @@
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
//Synthetic comment -- @@ -1745,6 +1747,21 @@
});
}

                @SuppressWarnings("unused")
                MenuItem separator = new MenuItem(menu, SWT.SEPARATOR);

                MenuItem item = new MenuItem(menu, SWT.PUSH);
                item.setText("Add New Translation...");
                item.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        IProject project = mEditedFile.getProject();
                        Shell shell = ConfigurationComposite.this.getShell();
                        AddTranslationDialog dialog = new AddTranslationDialog(shell, project);
                        dialog.open();
                    }
                });

Rectangle bounds = combo.getBounds();
Point location = new Point(bounds.x, bounds.y + bounds.height);
location = combo.getParent().toDisplay(location);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java
//Synthetic comment -- index e4da6e8..0d30011 100644

//Synthetic comment -- @@ -269,6 +269,16 @@
}

/**
     * Returns all the known region codes
     *
     * @return all the known region codes
     */
    @NonNull
    public static Set<String> getRegionCodes() {
        return Collections.unmodifiableSet(sRegionNames.keySet());
    }

    /**
* Populate the various maps.
* <p>
* The language to region mapping was constructed by using the ISO 639-1 table from








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintList.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintList.java
//Synthetic comment -- index 3b01e05..ccb04bb 100644

//Synthetic comment -- @@ -166,7 +166,7 @@
mTree.addPaintListener(new PaintListener() {
@Override
public void paintControl(PaintEvent e) {
                mTreePainted = true;
mTreeViewer.getTree().removePaintListener(this);
}
});
//Synthetic comment -- @@ -212,7 +212,7 @@
});
}

    private boolean mTreePainted;

private void updateColumnWidths() {
Rectangle r = mTree.getClientArea();
//Synthetic comment -- @@ -631,7 +631,7 @@
LintColumn column = (LintColumn) treeColumn.getData(KEY_COLUMN);
// Workaround for TeeColumn.getWidth() returning 0 in some cases,
// see https://bugs.eclipse.org/341865 for details.
            int width = getColumnWidth(column, mTreePainted);
columnEntry.putInteger(getKey(treeColumn), width);
columns[positions[i]] = column;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index e6e3acb..ccd1666 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.annotations.NonNull;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.IntArrayWrapper;
import com.android.ide.common.resources.ResourceFolder;
//Synthetic comment -- @@ -81,8 +82,9 @@
* @return a map with guaranteed to contain an entry for each {@link ResourceType}
*/
@Override
    @NonNull
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
            @NonNull FolderConfiguration referenceConfig) {

Map<ResourceType, Map<String, ResourceValue>> resultMap =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);
//Synthetic comment -- @@ -232,7 +234,8 @@
}

@Override
    @NonNull
    protected ResourceItem createResourceItem(@NonNull String name) {
return new ResourceItem(name);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..b4ce446

//Synthetic comment -- @@ -0,0 +1,649 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.AndroidConstants.RES_QUALIFIER_SEP;
import static com.android.SdkConstants.FD_RES;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LocaleManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceType;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Dialog which adds a new translation to the project
 */
public class AddTranslationDialog extends Dialog implements ControlListener, SelectionListener,
        TraverseListener {
    private static final int KEY_COLUMN = 0;
    private static final int DEFAULT_TRANSLATION_COLUMN = 1;
    private static final int NEW_TRANSLATION_COLUMN = 2;
    private final FolderConfiguration mConfiguration = new FolderConfiguration();
    private final IProject mProject;
    private String mTarget;
    private boolean mIgnore;
    private Map<String, String> mTranslations;
    private Set<String> mExistingLanguages;
    private String mSelectedLanguage;
    private String mSelectedRegion;

    private Table mTable;
    private Combo mLanguageCombo;
    private Combo mRegionCombo;
    private ImageControl mFlag;
    private Label mFile;
    private Button mOkButton;
    private Composite mErrorPanel;
    private Label mErrorLabel;
    private MyTableViewer mTableViewer;

    /**
     * Creates the dialog.
     * @param parentShell the parent shell
     * @param project the project to add translations into
     */
    public AddTranslationDialog(Shell parentShell, IProject project) {
        super(parentShell);
        setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
        mProject = project;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gl_container = new GridLayout(6, false);
        gl_container.horizontalSpacing = 0;
        container.setLayout(gl_container);

        Label languageLabel = new Label(container, SWT.NONE);
        languageLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        languageLabel.setText("Language:");
        mLanguageCombo = new Combo(container, SWT.READ_ONLY);
        GridData gd_mLanguageCombo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_mLanguageCombo.widthHint = 150;
        mLanguageCombo.setLayoutData(gd_mLanguageCombo);

        Label regionLabel = new Label(container, SWT.NONE);
        GridData gd_regionLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_regionLabel.horizontalIndent = 10;
        regionLabel.setLayoutData(gd_regionLabel);
        regionLabel.setText("Region:");
        mRegionCombo = new Combo(container, SWT.READ_ONLY);
        GridData gd_mRegionCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_mRegionCombo.widthHint = 150;
        mRegionCombo.setLayoutData(gd_mRegionCombo);
        mRegionCombo.setEnabled(false);

        mFlag = new ImageControl(container, SWT.NONE, null);
        mFlag.setDisposeImage(false);
        GridData gd_mFlag = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_mFlag.exclude = true;
        gd_mFlag.widthHint = 32;
        gd_mFlag.horizontalIndent = 3;
        mFlag.setLayoutData(gd_mFlag);

        mFile = new Label(container, SWT.NONE);
        mFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        mTableViewer = new MyTableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
        mTable = mTableViewer.getTable();
        mTable.setEnabled(false);
        mTable.setLinesVisible(true);
        mTable.setHeaderVisible(true);
        mTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 2));
        mTable.addControlListener(this);
        mTable.addTraverseListener(this);
        // If you have difficulty opening up this form in WindowBuilder and it complains about
        // the next line, change the type of the mTableViewer field and the above
        // constructor call from MyTableViewer to TableViewer
        TableViewerColumn keyViewerColumn = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn keyColumn = keyViewerColumn.getColumn();
        keyColumn.setWidth(100);
        keyColumn.setText("Key");
        TableViewerColumn defaultViewerColumn = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn defaultColumn = defaultViewerColumn.getColumn();
        defaultColumn.setWidth(200);
        defaultColumn.setText("Default");
        TableViewerColumn translationViewerColumn = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn translationColumn = translationViewerColumn.getColumn();
        translationColumn.setWidth(200);
        translationColumn.setText("New Translation");

        mErrorPanel = new Composite(container, SWT.NONE);
        GridData gd_mErrorLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1);
        gd_mErrorLabel.exclude = true;
        mErrorPanel.setLayoutData(gd_mErrorLabel);

        translationViewerColumn.setEditingSupport(new TranslationEditingSupport(mTableViewer));

        fillLanguages();
        fillRegions();
        fillStrings();
        updateColumnWidths();
        validatePage();

        mLanguageCombo.addSelectionListener(this);
        mRegionCombo.addSelectionListener(this);

        return container;
    }

    /** Populates the table with keys and default strings */
    private void fillStrings() {
        ResourceManager manager = ResourceManager.getInstance();
        ProjectResources resources = manager.getProjectResources(mProject);
        mExistingLanguages = resources.getLanguages();

        Collection<ResourceItem> items = resources.getResourceItemsOfType(ResourceType.STRING);

        ResourceItem[] array = items.toArray(new ResourceItem[items.size()]);
        Arrays.sort(array);

        // TODO: Read in the actual XML files providing the default keys here
        // (they can be obtained via ResourceItem.getSourceFileList())
        // such that we can read all the attributes associated with each
        // item, and if it defines translatable=false, or the filename is
        // donottranslate.xml, we can ignore it, and in other cases just
        // duplicate all the attributes (such as "formatted=true", or other
        // local conventions such as "product=tablet", or "msgid="123123123",
        // etc.)

        mTranslations = Maps.newHashMapWithExpectedSize(items.size());
        IBaseLabelProvider labelProvider = new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                Object element = cell.getElement();
                int index = cell.getColumnIndex();
                ResourceItem item = (ResourceItem) element;
                switch (index) {
                    case KEY_COLUMN: {
                        // Key
                        cell.setText(item.getName());
                        return;
                    }
                    case DEFAULT_TRANSLATION_COLUMN: {
                        // Default translation
                        ResourceValue value = item.getResourceValue(ResourceType.STRING,
                                mConfiguration, false);

                        if (value != null) {
                            cell.setText(value.getValue());
                            return;
                        }
                        break;
                    }
                    case NEW_TRANSLATION_COLUMN: {
                        // New translation
                        String translation = mTranslations.get(item.getName());
                        if (translation != null) {
                            cell.setText(translation);
                            return;
                        }
                        break;
                    }
                    default:
                        assert false : index;
                }
                cell.setText("");
            }
        };

        mTableViewer.setLabelProvider(labelProvider);
        mTableViewer.setContentProvider(new ArrayContentProvider());
        mTableViewer.setInput(array);
    }

    /** Populate the languages dropdown */
    private void fillLanguages() {
        Set<String> languageCodes = LocaleManager.getLanguageCodes();
        List<String> labels = new ArrayList<String>();
        for (String code : languageCodes) {
            labels.add(code + ": " + LocaleManager.getLanguageName(code)); //$NON-NLS-1$
        }
        Collections.sort(labels);
        labels.add(0, "(Select)");
        mLanguageCombo.setItems(labels.toArray(new String[labels.size()]));
        mLanguageCombo.select(0);
    }

    /** Populate the regions dropdown */
    private void fillRegions() {
        // TODO: When you switch languages, offer some "default" usable options. For example,
        // when you choose English, offer the countries that use English, and so on. Unfortunately
        // we don't have good data about this, we'd just need to hardcode a few common cases.
        Set<String> regionCodes = LocaleManager.getRegionCodes();
        List<String> labels = new ArrayList<String>();
        for (String code : regionCodes) {
            labels.add(code + ": " + LocaleManager.getRegionName(code)); //$NON-NLS-1$
        }
        Collections.sort(labels);
        labels.add(0, "Any");
        mRegionCombo.setItems(labels.toArray(new String[labels.size()]));
        mRegionCombo.select(0);
    }

    /** React to resizing by distributing the space evenly between the last two columns */
    private void updateColumnWidths() {
        Rectangle r = mTable.getClientArea();
        int availableWidth = r.width;
        // Distribute all available space to the last two columns
        int columnCount = mTable.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = mTable.getColumn(i);
            availableWidth -= column.getWidth();
        }
        if (availableWidth != 0) {
            TableColumn column = mTable.getColumn(DEFAULT_TRANSLATION_COLUMN);
            column.setWidth(column.getWidth() + availableWidth / 2);
            column = mTable.getColumn(NEW_TRANSLATION_COLUMN);
            column.setWidth(column.getWidth() + availableWidth / 2 + availableWidth % 2);
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        mOkButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                // Don't make the OK button default as in most dialogs, since when you press
                // Return thinking you might edit a value it dismisses the dialog instead
                false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        mOkButton.setEnabled(false);

        validatePage();
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(800, 600);
    }

    private void updateTarget() {
        if (mSelectedLanguage == null) {
            mTarget = null;
            mFile.setText("");
        } else {
            String folder = FD_RES + '/' + FD_RES_VALUES + RES_QUALIFIER_SEP + mSelectedLanguage;
            if (mSelectedRegion != null) {
                folder = folder + RES_QUALIFIER_SEP + 'r' + mSelectedRegion;
            }
            mTarget = folder + "/strings.xml"; //$NON-NLS-1$
            mFile.setText(String.format("Creating %1$s", mTarget));
        }
    }

    private void updateFlag() {
        if (mSelectedLanguage == null) {
            // Nothing selected
            ((GridData) mFlag.getLayoutData()).exclude = true;
        } else {
            LocaleManager manager = LocaleManager.get();
            Image flag = manager.getFlag(mSelectedLanguage, mSelectedRegion);
            if (flag != null) {
                ((GridData) mFlag.getLayoutData()).exclude = false;
                mFlag.setImage(flag);
            }
        }

        mFlag.getParent().layout(true);
        mFlag.getParent().redraw();
    }

    /** Actually create the new translation file and write it to disk */
    private void createTranslation() {
        List<String> keys = new ArrayList<String>(mTranslations.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder(keys.size() * 120);
        sb.append("<resources>\n\n");          //$NON-NLS-1$
        for (String key : keys) {
            String value = mTranslations.get(key);
            if (value == null || value.trim().isEmpty()) {
                continue;
            }
            sb.append("    <string name=\"");  //$NON-NLS-1$
            sb.append(key);
            sb.append("\">");                  //$NON-NLS-1$
            sb.append(value);
            sb.append("</string>\n");          //$NON-NLS-1$
        }
        sb.append("\n</resources>");           //$NON-NLS-1$

        IFile file = mProject.getFile(mTarget);

        try {
            IContainer parent = file.getParent();
            AdtUtils.ensureExists(parent);
            InputStream source = new ByteArrayInputStream(sb.toString().getBytes(Charsets.UTF_8));
            file.create(source, true, new NullProgressMonitor());
            AdtPlugin.openFile(file, null, true /*showEditorTab*/);

            // Ensure that the project resources updates itself to notice the new language.
            // In theory, this shouldn't be necessary.
            ResourceManager manager = ResourceManager.getInstance();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IFolder folder = root.getFolder(parent.getFullPath());
            manager.getResourceFolder(folder);
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }
    }

    private void validatePage() {
        if (mOkButton == null) { // Early initialization
            return;
        }

        String message = null;

        if (mSelectedLanguage == null) {
            message = "Select a language";
        } else if (mExistingLanguages.contains(mSelectedLanguage)) {
            if (mSelectedRegion == null) {
                message = String.format("%1$s is already translated in this project",
                        LocaleManager.getLanguageName(mSelectedLanguage));
            } else {
                ResourceManager manager = ResourceManager.getInstance();
                ProjectResources resources = manager.getProjectResources(mProject);
                SortedSet<String> regions = resources.getRegions(mSelectedLanguage);
                if (regions.contains(mSelectedRegion)) {
                    message = String.format("%1$s (%2$s) is already translated in this project",
                            LocaleManager.getLanguageName(mSelectedLanguage),
                            LocaleManager.getRegionName(mSelectedRegion));
                }
            }
        } else {
            // Require all strings to be translated? No, some of these may not
            // be translatable (e.g. translatable=false, defined in donottranslate.xml, etc.)
            //int missing = mTable.getItemCount() - mTranslations.values().size();
            //if (missing > 0) {
            //    message = String.format("Missing %1$d translations", missing);
            //}
        }

        boolean valid = message == null;
        mTable.setEnabled(message == null);
        mOkButton.setEnabled(valid);
        showError(message);
    }

    private void showError(String error) {
        GridData data = (GridData) mErrorPanel.getLayoutData();

        boolean show = error != null;
        if (show == data.exclude) {
            if (show) {
                if (mErrorLabel == null) {
                    mErrorPanel.setLayout(new GridLayout(2, false));
                    IWorkbench workbench = PlatformUI.getWorkbench();
                    ISharedImages sharedImages = workbench.getSharedImages();
                    String iconName = ISharedImages.IMG_OBJS_ERROR_TSK;
                    Image image = sharedImages.getImage(iconName);
                    @SuppressWarnings("unused")
                    ImageControl icon = new ImageControl(mErrorPanel, SWT.NONE, image);

                    mErrorLabel = new Label(mErrorPanel, SWT.NONE);
                    mErrorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                            1, 1));
                }
                mErrorLabel.setText(error);
            }
            data.exclude = !show;
            mErrorPanel.getParent().layout(true);
        }
    }

    @Override
    protected void okPressed() {
        mTableViewer.applyEditorValue();

        super.okPressed();
        createTranslation();
    }

    // ---- Implements ControlListener ----

    @Override
    public void controlMoved(ControlEvent e) {
    }

    @Override
    public void controlResized(ControlEvent e) {
        if (mIgnore) {
            return;
        }

        updateColumnWidths();
    }

    // ---- Implements SelectionListener ----

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (mIgnore) {
            return;
        }

        Object source = e.getSource();
        if (source == mLanguageCombo) {
            try {
                mIgnore = true;
                mRegionCombo.select(0);
                mSelectedRegion = null;
            } finally {
                mIgnore = false;
            }

            int languageIndex = mLanguageCombo.getSelectionIndex();
            if (languageIndex == 0) {
                mSelectedLanguage = null;
                mRegionCombo.setEnabled(false);
            } else {
                // This depends on the label format
                mSelectedLanguage = mLanguageCombo.getItem(languageIndex).substring(0, 2);
                mRegionCombo.setEnabled(true);
            }

            updateTarget();
            updateFlag();
        } else if (source == mRegionCombo) {
            int regionIndex = mRegionCombo.getSelectionIndex();
            if (regionIndex == 0) {
                mSelectedRegion = null;
            } else {
                mSelectedRegion = mRegionCombo.getItem(regionIndex).substring(0, 2);
            }

            updateTarget();
            updateFlag();
        }

        try {
            mIgnore = true;
            validatePage();
        } finally {
            mIgnore = false;
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    // ---- TraverseListener ----

    @Override
    public void keyTraversed(TraverseEvent e) {
        // If you press Return and we're not cell editing, start editing the current row
        if (e.detail == SWT.TRAVERSE_RETURN && !mTableViewer.isCellEditorActive()) {
            int index = mTable.getSelectionIndex();
            if (index != -1) {
                Object next = mTable.getItem(index).getData();
                mTableViewer.editElement(next, NEW_TRANSLATION_COLUMN);
            }
        }
    }

    /** Editing support for the translation column */
    private class TranslationEditingSupport extends EditingSupport {
        /**
         * When true, setValue is being called as part of a default action
         * (e.g. Return), not due to focus loss
         */
        private boolean mDefaultAction;

        private TranslationEditingSupport(ColumnViewer viewer) {
            super(viewer);
        }

        @Override
        protected void setValue(Object element, Object value) {
            ResourceItem item = (ResourceItem) element;
            mTranslations.put(item.getName(), value.toString());
            mTableViewer.update(element, null);
            validatePage();

            // If the user is pressing Return to finish editing a value (which is
            // not the only way this method can get called - for example, if you click
            // outside the cell while editing, the focus loss will also result in
            // this method getting called), then mDefaultAction is true, and we automatically
            // start editing the next row.
            if (mDefaultAction) {
                mTable.getDisplay().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (!mTable.isDisposed() && !mTableViewer.isCellEditorActive()) {
                            int index = mTable.getSelectionIndex();
                            if (index != -1 && index < mTable.getItemCount() - 1) {
                                Object next = mTable.getItem(index + 1).getData();
                                mTableViewer.editElement(next, NEW_TRANSLATION_COLUMN);
                            }
                        }
                    }
                });
            }
        }

        @Override
        protected Object getValue(Object element) {
            ResourceItem item = (ResourceItem) element;
            String value = mTranslations.get(item.getName());
            if (value == null) {
                return "";
            }
            return value;
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return new TextCellEditor(mTable) {
                @Override
                protected void handleDefaultSelection(SelectionEvent event) {
                    try {
                        mDefaultAction = true;
                        super.handleDefaultSelection(event);
                    } finally {
                        mDefaultAction = false;
                    }
                }
            };
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }
    }

    private class MyTableViewer extends TableViewer {
        public MyTableViewer(Composite parent, int style) {
            super(parent, style);
        }

        // Make this public so we can call it to ensure values are applied before the dialog
        // is dismissed in {@link #okPressed}
        @Override
        public void applyEditorValue() {
            super.applyEditorValue();
        }
    }
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/FrameworkResources.java b/ide_common/src/com/android/ide/common/resources/FrameworkResources.java
//Synthetic comment -- index 18bbc10..2115cdc 100755

//Synthetic comment -- @@ -71,7 +71,8 @@
* @return a collection of items, possible empty.
*/
@Override
    @NonNull
    public List<ResourceItem> getResourceItemsOfType(@NonNull ResourceType type) {
return mPublicResourceMap.get(type);
}

//Synthetic comment -- @@ -81,12 +82,13 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
@Override
    public boolean hasResourcesOfType(@NonNull ResourceType type) {
return mPublicResourceMap.get(type).size() > 0;
}

@Override
    @NonNull
    protected ResourceItem createResourceItem(@NonNull String name) {
return new FrameworkResourceItem(name);
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 7e0338f..6fb8457 100755

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.common.resources;

import com.android.AndroidConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.configuration.Configurable;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -90,8 +92,10 @@
* @param folder The workspace folder object.
* @return the {@link ResourceFolder} object associated to this folder.
*/
    private ResourceFolder add(
            @NonNull ResourceFolderType type,
            @NonNull FolderConfiguration config,
            @NonNull IAbstractFolder folder) {
// get the list for the resource type
List<ResourceFolder> list = mFolderMap.get(type);

//Synthetic comment -- @@ -128,10 +132,14 @@
* Removes a {@link ResourceFolder} associated with the specified {@link IAbstractFolder}.
* @param type The type of the folder
* @param removedFolder the IAbstractFolder object.
     * @param context the scanning context
* @return the {@link ResourceFolder} that was removed, or null if no matches were found.
*/
    @Nullable
    public ResourceFolder removeFolder(
            @NonNull ResourceFolderType type,
            @NonNull IAbstractFolder removedFolder,
            @Nullable ScanningContext context) {
// get the list of folders for the resource type.
List<ResourceFolder> list = mFolderMap.get(type);

//Synthetic comment -- @@ -162,7 +170,7 @@
* @param url the resource URL
* @return true if the resource is known
*/
    public boolean hasResourceItem(@NonNull String url) {
assert url.startsWith("@") : url;

int typeEnd = url.indexOf('/', 1);
//Synthetic comment -- @@ -195,7 +203,7 @@
* @param name the name of the resource
* @return true if the resource is known
*/
    public boolean hasResourceItem(@NonNull ResourceType type, @NonNull String name) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map != null) {
//Synthetic comment -- @@ -217,7 +225,8 @@
* @param name the name of the resource.
* @return A resource item matching the type and name.
*/
    @NonNull
    protected ResourceItem getResourceItem(@NonNull ResourceType type, @NonNull String name) {
// looking for an existing ResourceItem with this type and name
ResourceItem item = findDeclaredResourceItem(type, name);

//Synthetic comment -- @@ -289,14 +298,16 @@
* @param name the name of the resource
* @return a new ResourceItem (or child class) instance.
*/
    @NonNull
    protected abstract ResourceItem createResourceItem(@NonNull String name);

/**
* Processes a folder and adds it to the list of existing folders.
* @param folder the folder to process
* @return the ResourceFolder created from this folder, or null if the process failed.
*/
    @Nullable
    public ResourceFolder processFolder(@NonNull IAbstractFolder folder) {
// split the name of the folder in segments.
String[] folderSegments = folder.getName().split(AndroidConstants.RES_QUALIFIER_SEP);

//Synthetic comment -- @@ -319,10 +330,12 @@
* Returns a list of {@link ResourceFolder} for a specific {@link ResourceFolderType}.
* @param type The {@link ResourceFolderType}
*/
    @Nullable
    public List<ResourceFolder> getFolders(@NonNull ResourceFolderType type) {
return mFolderMap.get(type);
}

    @NonNull
public List<ResourceType> getAvailableResourceTypes() {
List<ResourceType> list = new ArrayList<ResourceType>();

//Synthetic comment -- @@ -366,7 +379,8 @@
* @param type the type of the resource items to return
* @return a non null collection of resource items
*/
    @NonNull
    public Collection<ResourceItem> getResourceItemsOfType(@NonNull ResourceType type) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map == null) {
//Synthetic comment -- @@ -387,7 +401,7 @@
* @param type the type of resource to check.
* @return true if the repository contains resources of the given type, false otherwise.
*/
    public boolean hasResourcesOfType(@NonNull ResourceType type) {
Map<String, ResourceItem> items = mResourceMap.get(type);
return (items != null && items.size() > 0);
}
//Synthetic comment -- @@ -397,7 +411,8 @@
* @param folder The {@link IAbstractFolder} object.
* @return the {@link ResourceFolder} or null if it was not found.
*/
    @Nullable
    public ResourceFolder getResourceFolder(@NonNull IAbstractFolder folder) {
Collection<List<ResourceFolder>> values = mFolderMap.values();

if (values.isEmpty()) { // This shouldn't be necessary, but has been observed
//Synthetic comment -- @@ -427,8 +442,9 @@
* layouts, bitmap based drawable, xml, anims).
* @return the matching file or <code>null</code> if no match was found.
*/
    @Nullable
    public ResourceFile getMatchingFile(@NonNull String name, @NonNull ResourceFolderType type,
            @NonNull FolderConfiguration config) {
// get the folders for the given type
List<ResourceFolder> folders = mFolderMap.get(type);

//Synthetic comment -- @@ -467,8 +483,9 @@
*
* @return a list of files generating this resource or null if it was not found.
*/
    @Nullable
    public List<ResourceFile> getSourceFiles(@NonNull ResourceType type, @NonNull String name,
            @Nullable FolderConfiguration referenceConfig) {

Collection<ResourceItem> items = getResourceItemsOfType(type);

//Synthetic comment -- @@ -497,8 +514,9 @@
* @param referenceConfig the configuration that each value must match.
* @return a map with guaranteed to contain an entry for each {@link ResourceType}
*/
    @NonNull
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
            @NonNull FolderConfiguration referenceConfig) {
return doGetConfiguredResources(referenceConfig);
}

//Synthetic comment -- @@ -509,8 +527,9 @@
* @param referenceConfig the configuration that each value must match.
* @return a map with guaranteed to contain an entry for each {@link ResourceType}
*/
    @NonNull
protected final Map<ResourceType, Map<String, ResourceValue>> doGetConfiguredResources(
            @NonNull FolderConfiguration referenceConfig) {

Map<ResourceType, Map<String, ResourceValue>> map =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);
//Synthetic comment -- @@ -526,6 +545,7 @@
/**
* Returns the sorted list of languages used in the resources.
*/
    @NonNull
public SortedSet<String> getLanguages() {
SortedSet<String> set = new TreeSet<String>();

//Synthetic comment -- @@ -547,7 +567,8 @@
* Returns the sorted list of regions used in the resources with the given language.
* @param currentLanguage the current language the region must be associated with.
*/
    @NonNull
    public SortedSet<String> getRegions(@NonNull String currentLanguage) {
SortedSet<String> set = new TreeSet<String>();

Collection<List<ResourceFolder>> folderList = mFolderMap.values();
//Synthetic comment -- @@ -577,7 +598,7 @@
* resource folder (res/)
* @throws IOException
*/
    public void loadResources(@NonNull IAbstractFolder rootFolder)
throws IOException {
ScanningContext context = new ScanningContext(this);

//Synthetic comment -- @@ -603,13 +624,14 @@
}


    protected void removeFile(@NonNull Collection<ResourceType> types,
            @NonNull ResourceFile file) {
for (ResourceType type : types) {
removeFile(type, file);
}
}

    protected void removeFile(@NonNull ResourceType type, @NonNull ResourceFile file) {
Map<String, ResourceItem> map = mResourceMap.get(type);
if (map != null) {
Collection<ResourceItem> values = map.values();
//Synthetic comment -- @@ -626,8 +648,9 @@
* @param type the type of the resources.
* @param referenceConfig the configuration to best match.
*/
    @NonNull
    private Map<String, ResourceValue> getConfiguredResource(@NonNull ResourceType type,
            @NonNull FolderConfiguration referenceConfig) {

// get the resource item for the given type
Map<String, ResourceItem> items = mResourceMap.get(type);
//Synthetic comment -- @@ -678,7 +701,9 @@
* @param name the Resource name.
* @return the existing ResourceItem or null if no match was found.
*/
    @Nullable
    private ResourceItem findDeclaredResourceItem(@NonNull ResourceType type,
            @NonNull String name) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map != null) {







