/*Revamp of the SDK Manager UI.

This patch must not be submitted yet.

It changes the way we deal with installed versus
available packages.

Change-Id:I5ec5776da69d2668ce746c07df022bf5adc6fbf7*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 4b0581f..3e50a90 100644

//Synthetic comment -- @@ -20,23 +20,23 @@
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectCreator;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.PackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;
//Synthetic comment -- @@ -306,7 +306,7 @@
window.registerPage("Settings", SettingsPage.class);
window.registerPage("About", AboutPage.class);
if (autoUpdate) {
                window.setInitialPage(PackagesPage.class);
window.setRequestAutoUpdate(true);
}
window.open();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java
//Synthetic comment -- index bed9174..526bfcb 100755

//Synthetic comment -- @@ -247,7 +247,22 @@
return mLibs;
}

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        return String.format("%1$s by %2$s%3$s",
                getName(),
                getVendor(),
                isObsolete() ? " (Obsolete)" : "");
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
@Override
public String getShortDescription() {
return String.format("%1$s by %2$s, Android API %3$s, revision %4$s%5$s",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java
//Synthetic comment -- index 1629045..ca6f463 100755

//Synthetic comment -- @@ -99,7 +99,19 @@
return mExactApiLevel;
}

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        return mShortDescription;
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
@Override
public String getShortDescription() {
return mShortDescription;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java
//Synthetic comment -- index 8a4c19d..5171454 100755

//Synthetic comment -- @@ -117,13 +117,35 @@
mVersion.saveProperties(props);
}

    /**
     * Returns the version, for platform, add-on and doc packages.
     * Can be 0 if this is a local package of unknown api-level.
     */
public AndroidVersion getVersion() {
return mVersion;
}

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        if (mVersion.isPreview()) {
            return String.format("Documentation for Android '%1$s' Preview SDK%2$s",
                    mVersion.getCodename(),
                    isObsolete() ? " (Obsolete)" : "");
        } else {
            return String.format("Documentation for Android SDK%2$s",
                    mVersion.getApiLevel(),
                    isObsolete() ? " (Obsolete)" : "");
        }
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
@Override
public String getShortDescription() {
if (mVersion.isPreview()) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index 3472544..bdf2805 100755

//Synthetic comment -- @@ -253,9 +253,7 @@
return ""; //$NON-NLS-1$
}

    private String getPrettyName() {
String name = mPath;

// In the past, we used to save the extras in a folder vendor-path,
//Synthetic comment -- @@ -299,8 +297,31 @@
name = name.replaceAll(" Usb ", " USB ");   //$NON-NLS-1$
name = name.replaceAll(" Api ", " API ");   //$NON-NLS-1$

        return name;
    }

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        String s = String.format("%1$s package%2$s",
                getPrettyName(),
                isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$

        return s;
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
    @Override
    public String getShortDescription() {

String s = String.format("%1$s package, revision %2$d%3$s",
                getPrettyName(),
getRevision(),
isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index 58be3c9..c597ad8 100755

//Synthetic comment -- @@ -372,6 +372,17 @@
}

/**
     * Returns a description of this package that is suitable for a list display.
     * Should not be empty. Must never be null.
     * <p/>
     * Note that this is the "base" name for the package
     * with no specific revision nor API mentionned.
     * In contrast, {@link #getShortDescription()} should be used if you want more details
     * such as the package revision number or the API, if applicable.
     */
    public abstract String getListDescription();

    /**
* Returns a short description for an {@link IDescription}.
* Can be empty but not null.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java
//Synthetic comment -- index c303e2f..622a922 100755

//Synthetic comment -- @@ -121,7 +121,31 @@
return mVersion;
}

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        String s;

        if (mVersion.isPreview()) {
            s = String.format("SDK Platform Android %1$s Preview%2$s",
                    getVersionName(),
                    isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$
        } else {
            s = String.format("SDK Platform Android %1$s%2$s",
                getVersionName(),
                isObsolete() ? " (Obsolete)" : "");      //$NON-NLS-2$
        }

        return s;
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
@Override
public String getShortDescription() {
String s;
//Synthetic comment -- @@ -130,13 +154,13 @@
s = String.format("SDK Platform Android %1$s Preview, revision %2$s%3$s",
getVersionName(),
getRevision(),
                    isObsolete() ? " (Obsolete)" : "");  //$NON-NLS-2$
} else {
s = String.format("SDK Platform Android %1$s, API %2$d, revision %3$s%4$s",
getVersionName(),
mVersion.getApiLevel(),
getRevision(),
                isObsolete() ? " (Obsolete)" : "");      //$NON-NLS-2$
}

return s;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java
//Synthetic comment -- index 860d703..9a2de62 100755

//Synthetic comment -- @@ -143,7 +143,20 @@
archiveOsPath);
}

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        return String.format("Android SDK Platform-tools%1$s",
                isObsolete() ? " (Obsolete)" : "");
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
@Override
public String getShortDescription() {
return String.format("Android SDK Platform-tools, revision %1$d%2$s",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java
//Synthetic comment -- index 035677b..b436b91 100755

//Synthetic comment -- @@ -175,7 +175,23 @@
return mVersion;
}

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        String s = String.format("Samples for SDK API %1$s%2$s%3$s",
                mVersion.getApiString(),
                mVersion.isPreview() ? " Preview" : "",
                isObsolete() ? " (Obsolete)" : "");
        return s;
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
@Override
public String getShortDescription() {
String s = String.format("Samples for SDK API %1$s%2$s, revision %3$d%4$s",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index 6e53dd7..827c721 100755

//Synthetic comment -- @@ -151,7 +151,20 @@
return mMinPlatformToolsRevision;
}

    /**
     * Returns a description of this package that is suitable for a list display.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String getListDescription() {
        return String.format("Android SDK Tools%1$s",
                isObsolete() ? " (Obsolete)" : "");
    }

    /**
     * Returns a short description for an {@link IDescription}.
     */
@Override
public String getShortDescription() {
return String.format("Android SDK Tools, revision %1$d%2$s",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IPageListener.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IPageListener.java
new file mode 100755
//Synthetic comment -- index 0000000..ba0c613

//Synthetic comment -- @@ -0,0 +1,30 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.repository;



/**
 * Interface for lifecycle events of pages.
 */
interface IPageListener {

    /**
     * The page was just selected and brought to front.
     */
    public void onPageSelected();
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
new file mode 100755
//Synthetic comment -- index 0000000..cb7287c

//Synthetic comment -- @@ -0,0 +1,823 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.IPackageVersion;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.Package.UpdateInfo;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeColumnViewerLabelProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackagesPage extends Composite
        implements ISdkChangeListener, IPageListener {

    private final List<PkgItem> mPackages = new ArrayList<PkgItem>();
    private final List<PkgCategory> mCategories = new ArrayList<PkgCategory>();
    private final UpdaterData mUpdaterData;

    private Text mTextSdkOsPath;

    private Button mCheckSortSource;
    private Button mCheckSortApi;
    private Button mCheckFilterObsolete;
    private Button mCheckFilterInstalled;
    private Button mCheckFilterNew;
    private Composite mGroupOptions;
    private Composite mGroupSdk;
    private Group mGroupPackages;
    private Composite mGroupButtons;
    private Button mButtonDelete;
    private Button mButtonInstall;
    private Tree mTree;
    private CheckboxTreeViewer mTreeViewer;
    private TreeViewerColumn mColumnName;
    private TreeViewerColumn mColumnApi;
    private TreeViewerColumn mColumnRevision;
    private TreeViewerColumn mColumnStatus;
    private Color mColorUpdate;
    private Color mColorNew;
    private Font mTreeFontItalic;

    public PackagesPage(Composite parent, UpdaterData updaterData) {
        super(parent, SWT.BORDER);

        mUpdaterData = updaterData;
        createContents(this);
        postCreate();  //$hide$
    }

    public void onPageSelected() {
        if (mPackages.isEmpty()) {
            // Initialize the package list the first time the page is shown.
            loadPackages();
        }
    }

    protected void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        mGroupSdk = new Composite(parent, SWT.NONE);
        mGroupSdk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        mGroupSdk.setLayout(new GridLayout(2, false));

        Label label1 = new Label(mGroupSdk, SWT.NONE);
        label1.setText("SDK Path:");

        mTextSdkOsPath = new Text(mGroupSdk, SWT.NONE);
        mTextSdkOsPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        mTextSdkOsPath.setEnabled(false);

        mGroupPackages = new Group(parent, SWT.NONE);
        mGroupPackages.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 1));
        mGroupPackages.setText("Packages");
        mGroupPackages.setLayout(new GridLayout(1, false));

        mTreeViewer = new CheckboxTreeViewer(mGroupPackages, SWT.BORDER);
        mTree = mTreeViewer.getTree();
        mTree.setHeaderVisible(true);
        mTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        mColumnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn1 = mColumnName.getColumn();
        treeColumn1.setImage(getImage("platform_pkg_16.png"));  //$NON-NLS-1$
        treeColumn1.setWidth(290);
        treeColumn1.setText("Name");

        mColumnApi = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn2 = mColumnApi.getColumn();
        treeColumn2.setWidth(50);
        treeColumn2.setText("API");

        mColumnRevision = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn3 = mColumnRevision.getColumn();
        treeColumn3.setWidth(50);
        treeColumn3.setText("Rev.");
        treeColumn3.setToolTipText("Revision currently installed");


        mColumnStatus = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn4 = mColumnStatus.getColumn();
        treeColumn4.setWidth(88);
        treeColumn4.setText("Status");

        mGroupOptions = new Composite(mGroupPackages, SWT.NONE);
        mGroupOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mGroupOptions.setLayout(new GridLayout(8, false));

        Label label2 = new Label(mGroupOptions, SWT.NONE);
        label2.setText("Sort by");

        mCheckSortSource = new Button(mGroupOptions, SWT.RADIO);
        mCheckSortSource.setToolTipText("Sort by Source");
        mCheckSortSource.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                on_CheckSortSource_widgetSelected(e);
            }
        });
        mCheckSortSource.setImage(getImage("source_icon16.png"));  //$NON-NLS-1$
        mCheckSortSource.setText("Source");

        mCheckSortApi = new Button(mGroupOptions, SWT.RADIO);
        mCheckSortApi.setToolTipText("Sort by API level");
        mCheckSortApi.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                on_CheckShortApi_widgetSelected(e);
            }
        });
        mCheckSortApi.setImage(getImage("platform_pkg_16.png"));  //$NON-NLS-1$
        mCheckSortApi.setText("API level");
        mCheckSortApi.setSelection(true);

                Label expandPlaceholder = new Label(mGroupOptions, SWT.NONE);
                expandPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

                Label label3 = new Label(mGroupOptions, SWT.NONE);
                label3.setText("Show:");

                        mCheckFilterNew = new Button(mGroupOptions, SWT.CHECK);
                        mCheckFilterNew.setToolTipText("Show Updates and New");
                        mCheckFilterNew.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                on_CheckFilterNew_widgetSelected(e);
                            }
                        });
                        mCheckFilterNew.setImage(getImage("reject_icon16.png"));
                        mCheckFilterNew.setSelection(true);
                        mCheckFilterNew.setText("Updates/New");

                        mCheckFilterInstalled = new Button(mGroupOptions, SWT.CHECK);
                        mCheckFilterInstalled.setToolTipText("Show Installed");
                        mCheckFilterInstalled.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                on_CheckFilterInstalled_widgetSelected(e);
                            }
                        });
                        mCheckFilterInstalled.setImage(getImage("accept_icon16.png"));  //$NON-NLS-1$
                        mCheckFilterInstalled.setSelection(true);
                        mCheckFilterInstalled.setText("Installed");

                mCheckFilterObsolete = new Button(mGroupOptions, SWT.CHECK);
                mCheckFilterObsolete.setToolTipText("Show Obsolete");
                mCheckFilterObsolete.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        on_CheckFilterObsolete_widgetSelected(e);
                    }
                });
                mCheckFilterObsolete.setImage(getImage("nopkg_icon16.png"));  //$NON-NLS-1$
                mCheckFilterObsolete.setSelection(false);
                mCheckFilterObsolete.setText("Obsolete");

        mGroupButtons = new Composite(parent, SWT.NONE);
        mGroupButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        mGroupButtons.setLayout(new GridLayout(3, false));

        mButtonDelete = new Button(mGroupButtons, SWT.NONE);
        mButtonDelete.setText("Delete");

        Label label4 = new Label(mGroupButtons, SWT.NONE);
        label4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
                1));

        mButtonInstall = new Button(mGroupButtons, SWT.NONE);
        mButtonInstall.setText("Install");
    }

    private Image getImage(String filename) {
        if (mUpdaterData != null) {
            ImageFactory imgFact = mUpdaterData.getImageFactory();
            if (imgFact != null) {
                imgFact.getImageByName(filename);
            }
        }
        return null;
    }


    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    private void postCreate() {
        if (mUpdaterData != null) {
            mTextSdkOsPath.setText(mUpdaterData.getOsSdkRoot());
        }

        mTreeViewer.setContentProvider(new PkgContentProvider());
        //--mTreeViewer.setLabelProvider(new PkgLabelProvider());


        mColumnApi.setLabelProvider(new TreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnApi)));
        mColumnName.setLabelProvider(new TreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnName)));
        mColumnStatus.setLabelProvider(new TreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnStatus)));
        mColumnRevision.setLabelProvider(new TreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnRevision)));

        FontData fontData = mTree.getFont().getFontData()[0];
        fontData.setStyle(SWT.ITALIC);
        mTreeFontItalic = new Font(mTree.getDisplay(), fontData);

        mColorUpdate = new Color(mTree.getDisplay(), 0xff, 0xff, 0xcc);
        mColorNew = new Color(mTree.getDisplay(), 0xff, 0xee, 0xcc);

        mTree.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                mTreeFontItalic.dispose();
                mColorUpdate.dispose();
                mColorNew.dispose();
                mTreeFontItalic = null;
                mColorUpdate = null;
                mColorNew = null;
            }
        });
    }

    private void loadPackages() {
        if (mUpdaterData == null) {
            return;
        }

        mPackages.clear();

        // get local packages
        for (Package pkg : mUpdaterData.getInstalledPackages()) {
            PkgItem pi = new PkgItem(pkg, PkgState.INSTALLED);
            mPackages.add(pi);
        }

        // get remote packages
        final boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();
        mUpdaterData.loadRemoteAddonsList();
        mUpdaterData.getTaskFactory().start("Loading Source", new ITask() {
            public void run(ITaskMonitor monitor) {
                for (SdkSource source : mUpdaterData.getSources().getAllSources()) {
                    Package[] pkgs = source.getPackages();
                    if (pkgs == null) {
                        source.load(monitor, forceHttp);
                        pkgs = source.getPackages();
                    }
                    if (pkgs == null) {
                        continue;
                    }

                    nextPkg: for(Package pkg : pkgs) {
                        boolean isUpdate = false;
                        for (PkgItem pi: mPackages) {
                            if (pi.isSameAs(pkg)) {
                                continue nextPkg;
                            }
                            if (pi.isUpdatedBy(pkg)) {
                                isUpdate = true;
                                break;
                            }
                        }

                        if (!isUpdate) {
                            PkgItem pi = new PkgItem(pkg, PkgState.NEW_AVAILABLE);
                            mPackages.add(pi);
                        }
                    }
                }
            }
        });

        sortPackages();
    }

    private void sortPackages() {
        if (mCheckSortApi != null && !mCheckSortApi.isDisposed() && mCheckSortApi.getSelection()) {
            sortByAPI();
        } else {
            sortBySource();
        }
    }

    private void sortByAPI() {
        mCategories.clear();

        Set<Integer> apiSet = new HashSet<Integer>();
        for (PkgItem item : mPackages) {
            if (keepItem(item)) {
                apiSet.add(item.getApi());
            }
        }

        Integer[] apis = apiSet.toArray(new Integer[apiSet.size()]);
        Arrays.sort(apis, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        for (Integer api : apis) {
            String name = api == -1 ? "Other" : "API " + api.toString();
            Object iconRef = null;

            List<PkgItem> items = new ArrayList<PkgItem>();
            for (PkgItem item : mPackages) {
                if (item.getApi() == api) {
                    items.add(item);

                    if (api != -1) {
                        Package p = item.getPackage();
                        if (p instanceof PlatformPackage) {
                            String vn = ((PlatformPackage) p).getVersionName();
                            name = String.format("%1$s (Android %2$s)", name, vn);
                            iconRef = p;
                        }
                    }
                }
            }

            PkgCategory cat = new PkgCategory(
                    name,
                    iconRef,
                    items.toArray(new PkgItem[items.size()]));
            mCategories.add(cat);
        }

        mTreeViewer.setInput(mCategories);

        // expand all items by default
        expandItem(mCategories);
    }

    private void sortBySource() {
        mCategories.clear();

        Set<SdkSource> sourceSet = new HashSet<SdkSource>();
        for (PkgItem item : mPackages) {
            if (keepItem(item)) {
                sourceSet.add(item.getSource());
            }
        }

        SdkSource[] sources = sourceSet.toArray(new SdkSource[sourceSet.size()]);
        Arrays.sort(sources, new Comparator<SdkSource>() {
            public int compare(SdkSource o1, SdkSource o2) {
                if (o1 == o2) {
                    return 0;
                } else if (o1 == null && o2 != null) {
                    return -1;
                } else if (o1 != null && o2 == null) {
                    return 1;
                }
                assert o1 != null;
                return o1.toString().compareTo(o2.toString());
            }
        });

        for (SdkSource source : sources) {
            Object key = source != null ? source : "Installed Packages";

            List<PkgItem> items = new ArrayList<PkgItem>();
            for (PkgItem item : mPackages) {
                if (item.getSource() == source) {
                    items.add(item);
                }
            }

            PkgCategory cat = new PkgCategory(
                    key,
                    key,
                    items.toArray(new PkgItem[items.size()]));
            mCategories.add(cat);
        }

        mTreeViewer.setInput(mCategories);

        // expand all items by default
        expandItem(mCategories);
    }

    private boolean keepItem(PkgItem item) {
        if (!mCheckFilterObsolete.getSelection()) {
            if (item.isObsolete()) {
                return false;
            }
        }

        if (!mCheckFilterInstalled.getSelection()) {
            if (item.getState() == PkgState.INSTALLED) {
                return false;
            }
        }

        if (!mCheckFilterNew.getSelection()) {
            if (item.getState() == PkgState.NEW_AVAILABLE ||
                    item.getState() == PkgState.UPDATE_AVAILABLE) {
                return false;
            }
        }

        return true;
    }

    private void expandItem(Object elem) {
        //if (elem instanceof SdkSource || elem instanceof SdkSourceCategory) {
            mTreeViewer.setExpandedState(elem, true);
            for (Object pkg : ((ITreeContentProvider) mTreeViewer.getContentProvider()).getChildren(elem)) {
                mTreeViewer.setChecked(pkg, true);
                expandItem(pkg);
            }
        //}
    }

    // ----------------------

    public class PkgCellLabelProvider extends ColumnLabelProvider
        implements ITableFontProvider, ITableColorProvider {

        private final TreeViewerColumn mColumn;

        public PkgCellLabelProvider(TreeViewerColumn column) {
            super();
            mColumn = column;
        }

        @Override
        public String getText(Object element) {
            if (element instanceof PkgCategory) {
                if (mColumn == mColumnName) {
                    return ((PkgCategory) element).getLabel();
                }
            } else if (element instanceof PkgItem) {
                PkgItem pkg = (PkgItem) element;

                if (mColumn == mColumnName) {
                    return pkg.getName();

                } else if (mColumn == mColumnApi) {
                    int api = pkg.getApi();
                    if (api < 1) {
                        return "";  //$NON-NLS-1$
                    } else {
                        return Integer.toString(api);
                    }
                } else if (mColumn == mColumnRevision) {
                    if (pkg.getState() == PkgState.INSTALLED || pkg.getState() == PkgState.UPDATE_AVAILABLE) {
                        return Integer.toString(pkg.getRevision());
                    }

                } else if (mColumn == mColumnStatus) {
                    switch(pkg.getState()) {
                    case INSTALLED:
                        return "Installed";
                    case UPDATE_AVAILABLE:
                        return "Rev. " + Integer.toString(pkg.getUpdateRev());
                    case NEW_AVAILABLE:
                        return "New rev. " + Integer.toString(pkg.getRevision());
                    case LOCKED_NO_INSTALL:
                        return "Locked";
                    }
                    return pkg.getState().toString();
                }
            } else if (element instanceof Package) {
                return ((Package) element).getShortDescription();
            }

            return "";  //$NON-NLS-1$
        }

        @Override
        public Image getImage(Object element) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();

            if (imgFactory != null) {
                if (mColumn == mColumnName) {
                    if (element instanceof PkgCategory) {
                        return imgFactory.getImageForObject(((PkgCategory) element).getIconRef());
                    }
                    return imgFactory.getImageForObject(element);

                } else if (mColumn == mColumnStatus && element instanceof PkgItem) {
                    switch(((PkgItem) element).getState()) {
                    case INSTALLED:
                        return imgFactory.getImageByName("accept_icon16.png");
                    case UPDATE_AVAILABLE:
                    case NEW_AVAILABLE:
                        return imgFactory.getImageByName("reject_icon16.png");
                    case LOCKED_NO_INSTALL:
                        return imgFactory.getImageByName("broken_pkg_16.png");
                    }
                }
            }
            return super.getImage(element);
        }

        // -- ITableFontProvider

        public Font getFont(Object element, int columnIndex) {
            if (element instanceof PkgItem) {
                if (((PkgItem) element).getState() != PkgState.INSTALLED) {
                    return mTreeFontItalic;
                }
            }
            return super.getFont(element);
        }

        // -- ITableColorProvider

        public Color getBackground(Object element, int columnIndex) {
            if (element instanceof PkgItem) {
                if (((PkgItem) element).getState() == PkgState.NEW_AVAILABLE) {
                    return mColorNew;
                } else if (((PkgItem) element).getState() == PkgState.UPDATE_AVAILABLE) {
                        return mColorUpdate;
                }
            }
            return null;
        }

        public Color getForeground(Object element, int columnIndex) {
            // Not used
            return null;
        }
    }

    public class PkgLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            return super.getText(element);
        }

        @Override
        public Image getImage(Object element) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();

            if (imgFactory != null) {
                return imgFactory.getImageForObject(element);
            }
            return super.getImage(element);
        }

    }

    public static class PkgContentProvider implements ITreeContentProvider {

        public Object[] getChildren(Object parentElement) {

            if (parentElement instanceof ArrayList<?>) {
                return ((ArrayList<?>) parentElement).toArray();

            } else if (parentElement instanceof PkgCategory) {
                return ((PkgCategory) parentElement).getItems();

            } else if (parentElement instanceof PkgItem) {
                List<Package> pkgs = ((PkgItem) parentElement).getUpdatePkgs();
                if (pkgs != null) {
                    return pkgs.toArray();
                }
            }

            return new Object[0];
        }

        public Object getParent(Object element) {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean hasChildren(Object parentElement) {
            if (parentElement instanceof ArrayList<?>) {
                return true;

            } else if (parentElement instanceof PkgCategory) {
                return true;

            } else if (parentElement instanceof PkgItem) {
                List<Package> pkgs = ((PkgItem) parentElement).getUpdatePkgs();
                if (pkgs != null) {
                    return !pkgs.isEmpty();
                }
            }

            return false;
        }

        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        public void dispose() {
            // TODO Auto-generated method stub

        }

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
            // TODO Auto-generated method stub

        }

    }

    public static class PkgCategory {
        private final Object mKey;
        private final PkgItem[] mItems;
        private final Object mIconRef;

        public PkgCategory(Object key, Object iconRef, PkgItem[] items) {
            mKey = key;
            mIconRef = iconRef;
            mItems = items;
        }

        public String getLabel() {
            return mKey.toString();
        }

        public Object getIconRef() {
            return mIconRef;
        }

        public PkgItem[] getItems() {
            return mItems;
        }
    }

    public enum PkgState {
        INSTALLED, UPDATE_AVAILABLE, NEW_AVAILABLE, LOCKED_NO_INSTALL
    }

    public static class PkgItem {
        private final Package mPkg;
        private PkgState mState;
        private int mUpdateRev;
        private List<Package> mUpdatePkgs;

        public PkgItem(Package pkg, PkgState state) {
            mPkg = pkg;
            mState = state;
        }

        public boolean isObsolete() {
            return mPkg.isObsolete();
        }

        public boolean isSameAs(Package pkg) {
            return mPkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE;
        }

        /**
         * Check whether the 'pkg' argument updates this package.
         * If it does, record it as a sub-package.
         * Returns true if it was recorded as an update, false otherwise.
         */
        public boolean isUpdatedBy(Package pkg) {
            if (mPkg.canBeUpdatedBy(pkg) == UpdateInfo.UPDATE) {
                if (mUpdatePkgs == null) {
                    mUpdatePkgs = new ArrayList<Package>();
                }
                mUpdatePkgs.add(pkg);
                mState = PkgState.UPDATE_AVAILABLE;
                return true;
            }

            return false;
        }

        public String getName() {
            return mPkg.getListDescription();
        }

        public int getRevision() {
            return mPkg.getRevision();
        }

        public String getDescription() {
            return mPkg.getDescription();
        }

        public Package getPackage() {
            return mPkg;
        }

        public PkgState getState() {
            return mState;
        }

        public SdkSource getSource() {
            if (mState == PkgState.NEW_AVAILABLE) {
                return mPkg.getParentSource();
            } else {
                return null;
            }
        }

        public int getApi() {
            return mPkg instanceof IPackageVersion ? ((IPackageVersion) mPkg).getVersion().getApiLevel() : -1;
        }

        public int getUpdateRev() {
            return mUpdateRev;
        }

        public List<Package> getUpdatePkgs() {
            return mUpdatePkgs;
        }
    }



    // --- Implementation of ISdkChangeListener ---

    public void onSdkLoaded() {
        onSdkReload();
    }

    public void onSdkReload() {
        loadPackages();
    }

    public void preInstallHook() {
        // nothing to be done for now.
    }

    public void postInstallHook() {
        // nothing to be done for now.
    }

    // --- End of hiding from SWT Designer ---
    //$hide<<$

    protected void on_CheckSortSource_widgetSelected(SelectionEvent e) {
        sortPackages();
    }

    protected void on_CheckShortApi_widgetSelected(SelectionEvent e) {
        sortPackages();
    }

    protected void on_CheckFilterObsolete_widgetSelected(SelectionEvent e) {
        sortPackages();
    }

    protected void on_CheckFilterInstalled_widgetSelected(SelectionEvent e) {
        sortPackages();
    }

    protected void on_CheckFilterNew_widgetSelected(SelectionEvent e) {
        sortPackages();
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index 6fbf060..40e159d 100755

//Synthetic comment -- @@ -1,495 +1,494 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.repository;


import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.util.ArrayList;


public class RemotePackagesPage extends Composite implements ISdkChangeListener {

    private final UpdaterData mUpdaterData;

    private CheckboxTreeViewer mTreeViewerSources;
    private Tree mTreeSources;
    private TreeColumn mColumnSource;
    private Button mUpdateOnlyCheckBox;
    private Group mDescriptionContainer;
    private Button mAddSiteButton;
    private Button mDeleteSiteButton;
    private Button mRefreshButton;
    private Button mInstallSelectedButton;
    private Label mDescriptionLabel;
    private Label mSdkLocLabel;



    /**
     * Create the composite.
     * @param parent The parent of the composite.
     * @param updaterData An instance of {@link UpdaterData}.
     */
    RemotePackagesPage(Composite parent, UpdaterData updaterData) {
        super(parent, SWT.BORDER);

        mUpdaterData = updaterData;

        createContents(this);
        postCreate();  //$hide$
    }

    private void createContents(Composite parent) {
        parent.setLayout(new GridLayout(5, false));

        mSdkLocLabel = new Label(parent, SWT.NONE);
        mSdkLocLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 5, 1));
        mSdkLocLabel.setText("SDK Location: " +
                (mUpdaterData.getOsSdkRoot() != null ? mUpdaterData.getOsSdkRoot()
                                                     : "<unknown>"));

        mTreeViewerSources = new CheckboxTreeViewer(parent, SWT.BORDER);
        mTreeViewerSources.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                onTreeCheckStateChanged(event); //$hide$
            }
        });
        mTreeSources = mTreeViewerSources.getTree();
        mTreeSources.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onTreeSelected(); //$hide$
            }
        });
        mTreeSources.setHeaderVisible(true);
        mTreeSources.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

        mColumnSource = new TreeColumn(mTreeSources, SWT.NONE);
        mColumnSource.setWidth(289);
        mColumnSource.setText("Packages available for download");

        mDescriptionContainer = new Group(parent, SWT.NONE);
        mDescriptionContainer.setLayout(new GridLayout(1, false));
        mDescriptionContainer.setText("Description");
        mDescriptionContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 1));

        mDescriptionLabel = new Label(mDescriptionContainer, SWT.NONE);
        mDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        mDescriptionLabel.setText("Line1\nLine2\nLine3");  //$NON-NLS-1$

        mAddSiteButton = new Button(parent, SWT.NONE);
        mAddSiteButton.setText("Add Add-on Site...");
        mAddSiteButton.setToolTipText("Allows you to enter a new add-on site. " +
                "Such site can only contribute add-ons and extra packages.");
        mAddSiteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onAddSiteSelected(); //$hide$
            }
        });

        mDeleteSiteButton = new Button(parent, SWT.NONE);
        mDeleteSiteButton.setText("Delete Add-on Site...");
        mDeleteSiteButton.setToolTipText("Allows you to remove an add-on site. " +
                "Built-in default sites cannot be removed.");
        mDeleteSiteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onRemoveSiteSelected(); //$hide$
            }
        });

        mUpdateOnlyCheckBox = new Button(parent, SWT.CHECK);
        mUpdateOnlyCheckBox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        mUpdateOnlyCheckBox.setText("Display updates only");
        mUpdateOnlyCheckBox.setToolTipText("When selected, only compatible non-obsolete update packages are shown in the list above.");
        mUpdateOnlyCheckBox.setSelection(mUpdaterData.getSettingsController().getShowUpdateOnly());
        mUpdateOnlyCheckBox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                onShowUpdateOnly(); //$hide$
            }
        });

        mRefreshButton = new Button(parent, SWT.NONE);
        mRefreshButton.setText("Refresh");
        mRefreshButton.setToolTipText("Refreshes the list of packages from open sites.");
        mRefreshButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onRefreshSelected(); //$hide$
            }
        });

        mInstallSelectedButton = new Button(parent, SWT.NONE);
        mInstallSelectedButton.setText("Install Selected");
        mInstallSelectedButton.setToolTipText("Allows you to review all selected packages " +
                "and install them.");
        mInstallSelectedButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onInstallSelectedArchives();  //$hide$
            }
        });
    }

    @Override
    public void dispose() {
        mUpdaterData.removeListener(this);
        super.dispose();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    /**
     * Called by the constructor right after {@link #createContents(Composite)}.
     */
    private void postCreate() {
        mUpdaterData.addListeners(this);
        adjustColumnsWidth();
        updateButtonsState();
    }

    /**
     * Adds a listener to adjust the columns width when the parent is resized.
     * <p/>
     * If we need something more fancy, we might want to use this:
     * http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet77.java?view=co
     */
    private void adjustColumnsWidth() {
        // Add a listener to resize the column to the full width of the table
        ControlAdapter resizer = new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Rectangle r = mTreeSources.getClientArea();
                mColumnSource.setWidth(r.width);
            }
        };

        mTreeSources.addControlListener(resizer);
        resizer.controlResized(null);
    }

    /**
     * Called when an item in the package table viewer is selected.
     * If the items is an {@link IDescription} (as it should), this will display its long
     * description in the description area. Otherwise when the item is not of the expected
     * type or there is no selection, it empties the description area.
     */
    private void onTreeSelected() {
        updateButtonsState();

        ISelection sel = mTreeViewerSources.getSelection();
        if (sel instanceof ITreeSelection) {
            Object elem = ((ITreeSelection) sel).getFirstElement();
            if (elem instanceof IDescription) {
                mDescriptionLabel.setText(((IDescription) elem).getLongDescription());
                mDescriptionContainer.layout(true);
                return;
            }
        }
        mDescriptionLabel.setText("");  //$NON-NLS1-$
    }

    /**
     * Handle checking and unchecking of the tree items.
     *
     * When unchecking, all sub-tree items checkboxes are cleared too.
     * When checking a source, all of its packages are checked too.
     * When checking a package, only its compatible archives are checked.
     */
    private void onTreeCheckStateChanged(CheckStateChangedEvent event) {
        updateButtonsState();

        boolean b = event.getChecked();
        Object elem = event.getElement(); // Will be Archive or Package or RepoSource

        assert event.getSource() == mTreeViewerSources;

        // when deselecting, we just deselect all children too
        if (b == false) {
            mTreeViewerSources.setSubtreeChecked(elem, b);
            return;
        }

        ITreeContentProvider provider =
            (ITreeContentProvider) mTreeViewerSources.getContentProvider();

        // When selecting, we want to only select compatible archives
        // and expand the super nodes.
        expandItem(elem, provider);
    }

    private void expandItem(Object elem, ITreeContentProvider provider) {
        if (elem instanceof SdkSource || elem instanceof SdkSourceCategory) {
            mTreeViewerSources.setExpandedState(elem, true);
            for (Object pkg : provider.getChildren(elem)) {
                mTreeViewerSources.setChecked(pkg, true);
                expandItem(pkg, provider);
            }
        } else if (elem instanceof Package) {
            selectCompatibleArchives(elem, provider);
        }
    }

    private void selectCompatibleArchives(Object pkg, ITreeContentProvider provider) {
        for (Object archive : provider.getChildren(pkg)) {
            if (archive instanceof Archive) {
                mTreeViewerSources.setChecked(archive, ((Archive) archive).isCompatible());
            }
        }
    }

    private void onShowUpdateOnly() {
        SettingsController controller = mUpdaterData.getSettingsController();
        controller.setShowUpdateOnly(mUpdateOnlyCheckBox.getSelection());
        controller.saveSettings();

        // Get the list of selected archives
        ArrayList<Archive> archives = new ArrayList<Archive>();
        for (Object element : mTreeViewerSources.getCheckedElements()) {
            if (element instanceof Archive) {
                archives.add((Archive) element);
            }
            // Deselect them all
            mTreeViewerSources.setChecked(element, false);
        }

        mTreeViewerSources.refresh();

        // Now reselect those that still exist in the tree but only if they
        // are compatible archives
        for (Archive a : archives) {
            if (a.isCompatible() && mTreeViewerSources.setChecked(a, true)) {
                // If we managed to select the archive, also select the parent package.
                // Technically we should only select the parent package if *all* the
                // compatible archives children are selected. In practice we'll rarely
                // have more than one compatible archive per package.
                mTreeViewerSources.setChecked(a.getParentPackage(), true);
            }
        }

        updateButtonsState();
    }

    private void onInstallSelectedArchives() {
        ArrayList<Archive> archives = new ArrayList<Archive>();
        for (Object element : mTreeViewerSources.getCheckedElements()) {
            if (element instanceof Archive) {
                archives.add((Archive) element);
            }
        }

        if (mUpdaterData != null) {
            mUpdaterData.updateOrInstallAll_WithGUI(
                    archives,
                    mUpdateOnlyCheckBox.getSelection() /* includeObsoletes */);
        }
    }

    private void onAddSiteSelected() {
        final SdkSource[] knowSources = mUpdaterData.getSources().getAllSources();
        String title = "Add Add-on Site URL";

        String msg =
        "This dialog lets you add the URL of a new add-on site.\n" +
        "\n" +
        "An add-on site can only provide new add-ons or \"user\" packages.\n" +
        "Add-on sites cannot provide standard Android platforms, docs or samples packages.\n" +
        "Inserting a URL here will not allow you to clone an official Android repository.\n" +
        "\n" +
        "Please enter the URL of the repository.xml for the new add-on site:";

        InputDialog dlg = new InputDialog(getShell(), title, msg, null, new IInputValidator() {
            public String isValid(String newText) {

                newText = newText == null ? null : newText.trim();

                if (newText == null || newText.length() == 0) {
                    return "Error: URL field is empty. Please enter a URL.";
                }

                // A URL should have one of the following prefixes
                if (!newText.startsWith("file://") &&                 //$NON-NLS-1$
                        !newText.startsWith("ftp://") &&              //$NON-NLS-1$
                        !newText.startsWith("http://") &&             //$NON-NLS-1$
                        !newText.startsWith("https://")) {            //$NON-NLS-1$
                    return "Error: The URL must start by one of file://, ftp://, http:// or https://";
                }

                // Reject URLs that are already in the source list.
                // URLs are generally case-insensitive (except for file:// where it all depends
                // on the current OS so we'll ignore this case.)
                for (SdkSource s : knowSources) {
                    if (newText.equalsIgnoreCase(s.getUrl())) {
                        return "Error : This site is already listed.";
                    }
                }

                return null;
            }
        });

        if (dlg.open() == Window.OK) {
            String url = dlg.getValue().trim();
            mUpdaterData.getSources().add(
                    SdkSourceCategory.USER_ADDONS,
                    new SdkAddonSource(url, null/*uiName*/));
            onRefreshSelected();
        }
    }

    private void onRemoveSiteSelected() {
        boolean changed = false;

        ISelection sel = mTreeViewerSources.getSelection();
        if (mUpdaterData != null && sel instanceof ITreeSelection) {
            for (Object c : ((ITreeSelection) sel).toList()) {
                if (c instanceof SdkSource) {
                    SdkSource source = (SdkSource) c;

                    if (mUpdaterData.getSources().hasSourceUrl(
                            SdkSourceCategory.USER_ADDONS, source)) {
                        String title = "Delete Add-on Site?";

                        String msg = String.format("Are you sure you want to delete the add-on site '%1$s'?",
                                source.getUrl());

                        if (MessageDialog.openQuestion(getShell(), title, msg)) {
                            mUpdaterData.getSources().remove(source);
                            changed = true;
                        }
                    }
                }
            }
        }

        if (changed) {
            onRefreshSelected();
        }
    }

    private void onRefreshSelected() {
        if (mUpdaterData != null) {
            mUpdaterData.refreshSources(false /*forceFetching*/);
        }
        mTreeViewerSources.refresh();
        updateButtonsState();
    }

    private void updateButtonsState() {
        // We install archives, so there should be at least one checked archive.
        // Having sites or packages checked does not count.
        boolean hasCheckedArchive = false;
        Object[] checked = mTreeViewerSources.getCheckedElements();
        if (checked != null) {
            for (Object c : checked) {
                if (c instanceof Archive) {
                    hasCheckedArchive = true;
                    break;
                }
            }
        }

        // Is there a selected site Source?
        boolean hasSelectedUserSource = false;
        ISelection sel = mTreeViewerSources.getSelection();
        if (sel instanceof ITreeSelection) {
            for (Object c : ((ITreeSelection) sel).toList()) {
                if (c instanceof SdkSource &&
                        mUpdaterData.getSources().hasSourceUrl(
                                SdkSourceCategory.USER_ADDONS, (SdkSource) c)) {
                    hasSelectedUserSource = true;
                    break;
                }
            }
        }

        mAddSiteButton.setEnabled(true);
        mDeleteSiteButton.setEnabled(hasSelectedUserSource);
        mRefreshButton.setEnabled(true);
        mInstallSelectedButton.setEnabled(hasCheckedArchive);

        // set value on the show only update checkbox
        mUpdateOnlyCheckBox.setSelection(mUpdaterData.getSettingsController().getShowUpdateOnly());
    }

    // --- Implementation of ISdkChangeListener ---

    public void onSdkLoaded() {
        onSdkReload();
    }

    public void onSdkReload() {
        RepoSourcesAdapter sources = mUpdaterData.getSourcesAdapter();
        mTreeViewerSources.setContentProvider(sources.getContentProvider());
        mTreeViewerSources.setLabelProvider(  sources.getLabelProvider());
        mTreeViewerSources.setInput(sources);
        onTreeSelected();
    }

    public void preInstallHook() {
        // nothing to be done for now.
    }

    public void postInstallHook() {
        // nothing to be done for now.
    }

    // End of hiding from SWT Designer
    //$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 19d3916..8e70964 100755

//Synthetic comment -- @@ -69,10 +69,9 @@
private SashForm mSashForm;
private List mPageList;
private Composite mPagesRootComposite;
private AvdManagerPage mAvdManagerPage;
private StackLayout mStackLayout;
    private PackagesPage mPackagesPage;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -122,6 +121,12 @@
}
});

        mUpdaterData.setWindowShell(mAndroidSdkUpdater);
        mTaskFactory = new ProgressTaskFactory(mAndroidSdkUpdater);
        mUpdaterData.setTaskFactory(mTaskFactory);
        mUpdaterData.setImageFactory(new ImageFactory(mAndroidSdkUpdater.getDisplay()));


FillLayout fl;
mAndroidSdkUpdater.setLayout(fl = new FillLayout(SWT.HORIZONTAL));
fl.marginHeight = fl.marginWidth = 5;
//Synthetic comment -- @@ -219,15 +224,7 @@
*/
private void createPages() {
mAvdManagerPage = new AvdManagerPage(mPagesRootComposite, mUpdaterData);
        mPackagesPage = new PackagesPage(mPagesRootComposite, mUpdaterData);
}

/**
//Synthetic comment -- @@ -266,16 +263,10 @@
* Returns true if we should show the window.
*/
private boolean postCreate() {
setWindowImage(mAndroidSdkUpdater);

addPage(mAvdManagerPage,     "Virtual devices");
        addPage(mPackagesPage,      "Packages List");
addExtraPages();

int pageIndex = 0;
//Synthetic comment -- @@ -287,11 +278,11 @@
}
i++;
}

setupSources();
initializeSettings();
        displayPage(pageIndex);
        mPageList.setSelection(pageIndex);

if (mUpdaterData.checkIfInitFailed()) {
return false;
//Synthetic comment -- @@ -395,6 +386,10 @@
mPageList.setSelection(index);
mInternalPageChange = false;
}

            if (page instanceof IPageListener) {
                ((IPageListener) page).onPageSelected();
            }
}
}

//Synthetic comment -- @@ -403,7 +398,6 @@
*/
private void setupSources() {
mUpdaterData.setupDefaultSources();
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java
//Synthetic comment -- index 4a38f75..405a9b2 100755

//Synthetic comment -- @@ -122,6 +122,10 @@
}
}

        if (object instanceof String) {
            return getImageByName((String) object);
        }

return null;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index abc63d0..4f9eec8 100755

//Synthetic comment -- @@ -241,6 +241,11 @@
}

@Override
        public String getListDescription() {
            return this.getClass().getSimpleName();
        }

        @Override
public String getShortDescription() {
return this.getClass().getSimpleName();
}







