/*Convert EOL from dos2unix in sdkuilib *.java

Change-Id:Ia046d63aac7800326effbe61364d2efa35581633*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index fa3f688..6954c27 100755

//Synthetic comment -- @@ -1,120 +1,120 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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


import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AboutDialog extends UpdaterBaseDialog {

    public AboutDialog(Shell parentShell, UpdaterData updaterData) {
        super(parentShell, updaterData, "About" /*title*/);
        assert updaterData != null;
    }

    @Override
    protected void createContents() {
        super.createContents();
        Shell shell = getShell();
        shell.setMinimumSize(new Point(450, 150));
        shell.setSize(450, 150);

        GridLayoutBuilder.create(shell).columns(3);

        Label logo = new Label(shell, SWT.NONE);
        ImageFactory imgf = getUpdaterData() == null ? null : getUpdaterData().getImageFactory();
        Image image = imgf == null ? null : imgf.getImageByName("sdkman_logo_128.png");
        if (image != null) logo.setImage(image);

        Label label = new Label(shell, SWT.NONE);
        GridDataBuilder.create(label).hFill().hGrab().hSpan(2);;
        label.setText(String.format(
                "Android SDK Manager.\n" +
                "Revision %1$s\n" +
                "Add-on XML Schema #%2$d\n" +
                "Repository XML Schema #%3$d\n" +
                // TODO: update with new year date (search this to find other occurrences to update)
                "Copyright (C) 2009-2012 The Android Open Source Project.",
                getRevision(),
                SdkAddonConstants.NS_LATEST_VERSION,
                SdkRepoConstants.NS_LATEST_VERSION));

        Label filler = new Label(shell, SWT.NONE);
        GridDataBuilder.create(filler).fill().grab().hSpan(2);

        createCloseButton();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    // End of hiding from SWT Designer
    //$hide<<$

    private String getRevision() {
        Properties p = new Properties();
        try{
            File sourceProp = FileOp.append(getUpdaterData().getOsSdkRoot(),
                    SdkConstants.FD_TOOLS,
                    SdkConstants.FN_SOURCE_PROP);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(sourceProp);
                p.load(fis);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ignore) {
                    }
                }
            }

            String revision = p.getProperty(PkgProps.PKG_REVISION);
            if (revision != null) {
                return revision;
            }
        } catch (IOException e) {
        }

        return "?";
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index dafcc05..2c509ca 100755

//Synthetic comment -- @@ -1,160 +1,160 @@
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

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents an archive that we want to install.
 * Note that the installer deals with archives whereas the user mostly sees packages
 * but as far as we are concerned for installation there's a 1-to-1 mapping.
 * <p/>
 * A new archive is always a remote archive that needs to be downloaded and then
 * installed. It can replace an existing local one. It can also depends on another
 * (new or local) archive, which means the dependent archive needs to be successfully
 * installed first. Finally this archive can also be a dependency for another one.
 * <p/>
 * The accepted and rejected flags are used by {@link SdkUpdaterChooserDialog} to follow
 * user choices. The installer should never install something that is not accepted.
 * <p/>
 * <em>Note</em>: There is currently no logic to support more than one level of
 * dependency, either here or in the {@link SdkUpdaterChooserDialog}, since we currently
 * have no need for it.
 *
 * @see ArchiveInfo#ArchiveInfo(Archive, Archive, ArchiveInfo[])
 */
class ArchiveInfo extends ArchiveReplacement implements Comparable<ArchiveInfo> {

    private final ArchiveInfo[] mDependsOn;
    private final ArrayList<ArchiveInfo> mDependencyFor = new ArrayList<ArchiveInfo>();
    private boolean mAccepted;
    private boolean mRejected;

    /**
     * Creates a new replacement where the {@code newArchive} will replace the
     * currently installed {@code replaced} archive.
     * When {@code newArchive} is not intended to replace anything (e.g. because
     * the user is installing a new package not present on her system yet), then
     * {@code replace} shall be null.
     *
     * @param newArchive A "new archive" to be installed. This is always an archive
     *          that comes from a remote site. This <em>may</em> be null.
     * @param replaced An optional local archive that the new one will replace.
     *          Can be null if this archive does not replace anything.
     * @param dependsOn An optional new or local dependency, that is an archive that
     *          <em>this</em> archive depends upon. In other words, we can only install
     *          this archive if the dependency has been successfully installed. It also
     *          means we need to install the dependency first. Can be null or empty.
     *          However it cannot contain nulls.
     */
    public ArchiveInfo(Archive newArchive, Archive replaced, ArchiveInfo[] dependsOn) {
        super(newArchive, replaced);
        mDependsOn = dependsOn;
    }

    /**
     * Returns an optional new or local dependency, that is an archive that <em>this</em>
     * archive depends upon. In other words, we can only install this archive if the
     * dependency has been successfully installed. It also means we need to install the
     * dependency first.
     * <p/>
     * This array can be null or empty. It can't contain nulls though.
     */
    public ArchiveInfo[] getDependsOn() {
        return mDependsOn;
    }

    /**
     * Returns true if this new archive is a dependency for <em>another</em> one that we
     * want to install.
     */
    public boolean isDependencyFor() {
        return mDependencyFor.size() > 0;
    }

    /**
     * Adds an {@link ArchiveInfo} for which <em>this</em> package is a dependency.
     * This means the package added here depends on this package.
     */
    public ArchiveInfo addDependencyFor(ArchiveInfo dependencyFor) {
        if (!mDependencyFor.contains(dependencyFor)) {
            mDependencyFor.add(dependencyFor);
        }

        return this;
    }

    /**
     * Returns the list of {@link ArchiveInfo} for which <em>this</em> package is a dependency.
     * This means the packages listed here depend on this package.
     * <p/>
     * Implementation detail: this is the internal mutable list. Callers should not modify it.
     * This list can be empty but is never null.
     */
    public Collection<ArchiveInfo> getDependenciesFor() {
        return mDependencyFor;
    }

    /**
     * Sets whether this archive was accepted (either manually by the user or
     * automatically if it doesn't have a license) for installation.
     */
    public void setAccepted(boolean accepted) {
        mAccepted = accepted;
    }

    /**
     * Returns whether this archive was accepted (either manually by the user or
     * automatically if it doesn't have a license) for installation.
     */
    public boolean isAccepted() {
        return mAccepted;
    }

    /**
     * Sets whether this archive was rejected manually by the user.
     * An archive can neither accepted nor rejected.
     */
    public void setRejected(boolean rejected) {
        mRejected = rejected;
    }

    /**
     * Returns whether this archive was rejected manually by the user.
     * An archive can neither accepted nor rejected.
     */
    public boolean isRejected() {
        return mRejected;
    }

    /**
     * ArchiveInfos are compared using ther "new archive" ordering.
     *
     * @see Archive#compareTo(Archive)
     */
    @Override
    public int compareTo(ArchiveInfo rhs) {
        if (getNewArchive() != null && rhs != null) {
            return getNewArchive().compareTo(rhs.getNewArchive());
        }
        return 0;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java
//Synthetic comment -- index 6d15360..333644f 100755

//Synthetic comment -- @@ -1,108 +1,108 @@
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

import com.android.sdklib.internal.repository.DownloadCache;

import java.net.URL;
import java.util.Properties;

/**
 * Interface that a settings page must implement.
 */
public interface ISettingsPage {

    /**
     * Java system setting picked up by {@link URL} for http proxy port.
     * Type: String.
     */
    public static final String KEY_HTTP_PROXY_PORT = "http.proxyPort";           //$NON-NLS-1$

    /**
     * Java system setting picked up by {@link URL} for http proxy host.
     * Type: String.
     */
    public static final String KEY_HTTP_PROXY_HOST = "http.proxyHost";           //$NON-NLS-1$

    /**
     * Setting to force using http:// instead of https:// connections.
     * Type: Boolean.
     * Default: False.
     */
    public static final String KEY_FORCE_HTTP = "sdkman.force.http";             //$NON-NLS-1$

    /**
     * Setting to display only packages that are new or updates.
     * Type: Boolean.
     * Default: True.
     */
    public static final String KEY_SHOW_UPDATE_ONLY = "sdkman.show.update.only"; //$NON-NLS-1$

    /**
     * Setting to ask for permission before restarting ADB.
     * Type: Boolean.
     * Default: False.
     */
    public static final String KEY_ASK_ADB_RESTART = "sdkman.ask.adb.restart";   //$NON-NLS-1$

    /**
     * Setting to use the {@link DownloadCache}, for small manifest XML files.
     * Type: Boolean.
     * Default: True.
     */
    public static final String KEY_USE_DOWNLOAD_CACHE = "sdkman.use.dl.cache";   //$NON-NLS-1$

    /**
     * Setting to enabling previews in the package list
     * Type: Boolean.
     * Default: False.
     */
    public static final String KEY_ENABLE_PREVIEWS = "sdkman.enable.previews";   //$NON-NLS-1$

    /**
     * Setting to set the density of the monitor.
     * Type: Integer.
     * Default: -1
     */
    public static final String KEY_MONITOR_DENSITY = "sdkman.monitor.density"; //$NON-NLS-1$

    /** Loads settings from the given {@link Properties} container and update the page UI. */
    public abstract void loadSettings(Properties inSettings);

    /** Called by the application to retrieve settings from the UI and store them in
     * the given {@link Properties} container. */
    public abstract void retrieveSettings(Properties outSettings);

    /**
     * Called by the application to give a callback that the page should invoke when
     * settings have changed.
     */
    public abstract void setOnSettingsChanged(SettingsChangedCallback settingsChangedCallback);

    /**
     * Callback used to notify the application that settings have changed and need to be
     * applied.
     */
    public interface SettingsChangedCallback {
        /**
         * Invoked by the settings page when settings have changed and need to be
         * applied. The application will call {@link ISettingsPage#retrieveSettings(Properties)}
         * and apply the new settings.
         */
        public abstract void onSettingsChanged(ISettingsPage page);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/MenuBarWrapper.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/MenuBarWrapper.java
//Synthetic comment -- index f7bd97f..b23af12 100755

//Synthetic comment -- @@ -1,60 +1,60 @@
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


import com.android.menubar.IMenuBarCallback;
import com.android.menubar.MenuBarEnhancer;
import com.android.sdkuilib.internal.repository.sdkman2.SdkUpdaterWindowImpl2;

import org.eclipse.swt.widgets.Menu;

/**
 * A simple wrapper/delegate around the {@link MenuBarEnhancer}.
 *
 * The {@link MenuBarEnhancer} and {@link IMenuBarCallback} classes are only
 * available when the SwtMenuBar library is available too. This wrapper helps
 * {@link SdkUpdaterWindowImpl2} make the call conditional, otherwise the updater
 * window class would fail to load when the SwtMenuBar library isn't found.
 */
public abstract class MenuBarWrapper {

    public MenuBarWrapper(String appName, Menu menu) {
        MenuBarEnhancer.setupMenu(appName, menu, new IMenuBarCallback() {
            @Override
            public void onPreferencesMenuSelected() {
                MenuBarWrapper.this.onPreferencesMenuSelected();
            }

            @Override
            public void onAboutMenuSelected() {
                MenuBarWrapper.this.onAboutMenuSelected();
            }

            @Override
            public void printError(String format, Object... args) {
                MenuBarWrapper.this.printError(format, args);
            }
        });
    }

    abstract public void onPreferencesMenuSelected();

    abstract public void onAboutMenuSelected();

    abstract public void printError(String format, Object... args);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index a41a952..3b0c691 100755

//Synthetic comment -- @@ -1,762 +1,762 @@
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

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Implements an {@link SdkUpdaterChooserDialog}.
 */
final class SdkUpdaterChooserDialog extends GridDialog {

    /** Last dialog size for this session. */
    private static Point sLastSize;
    private boolean mLicenseAcceptAll;
    private boolean mInternalLicenseRadioUpdate;

    // UI fields
    private SashForm mSashForm;
    private Composite mPackageRootComposite;
    private TableViewer mTableViewPackage;
    private Table mTablePackage;
    private TableColumn mTableColum;
    private StyledText mPackageText;
    private Button mLicenseRadioAccept;
    private Button mLicenseRadioReject;
    private Button mLicenseRadioAcceptAll;
    private Group mPackageTextGroup;
    private final UpdaterData mUpdaterData;
    private Group mTableGroup;
    private Label mErrorLabel;

    /**
     * List of all archives to be installed with dependency information.
     * <p/>
     * Note: in a lot of cases, we need to find the archive info for a given archive. This
     * is currently done using a simple linear search, which is fine since we only have a very
     * limited number of archives to deal with (e.g. < 10 now). We might want to revisit
     * this later if it becomes an issue. Right now just do the simple thing.
     *<p/>
     * Typically we could add a map Archive=>ArchiveInfo later.
     */
    private final Collection<ArchiveInfo> mArchives;



    /**
     * Create the dialog.
     * @param parentShell The shell to use, typically updaterData.getWindowShell()
     * @param updaterData The updater data
     * @param archives The archives to be installed
     */
    public SdkUpdaterChooserDialog(Shell parentShell,
            UpdaterData updaterData,
            Collection<ArchiveInfo> archives) {
        super(parentShell, 3, false/*makeColumnsEqual*/);
        mUpdaterData = updaterData;
        mArchives = archives;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    /**
     * Returns the results, i.e. the list of selected new archives to install.
     * This is similar to the {@link ArchiveInfo} list instance given to the constructor
     * except only accepted archives are present.
     *
     * An empty list is returned if cancel was choosen.
     */
    public ArrayList<ArchiveInfo> getResult() {
        ArrayList<ArchiveInfo> ais = new ArrayList<ArchiveInfo>();

        if (getReturnCode() == Window.OK) {
            for (ArchiveInfo ai : mArchives) {
                if (ai.isAccepted()) {
                    ais.add(ai);
                }
            }
        }

        return ais;
    }

    /**
     * Create the main content of the dialog.
     * See also {@link #createButtonBar(Composite)} below.
     */
    @Override
    public void createDialogContent(Composite parent) {
        // Sash form
        mSashForm = new SashForm(parent, SWT.NONE);
        mSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));


        // Left part of Sash Form

        mTableGroup = new Group(mSashForm, SWT.NONE);
        mTableGroup.setText("Packages");
        mTableGroup.setLayout(new GridLayout(1, false/*makeColumnsEqual*/));

        mTableViewPackage = new TableViewer(mTableGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
        mTablePackage = mTableViewPackage.getTable();
        mTablePackage.setHeaderVisible(false);
        mTablePackage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        mTablePackage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onPackageSelected();  //$hide$
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                onPackageDoubleClick();
            }
        });

        mTableColum = new TableColumn(mTablePackage, SWT.NONE);
        mTableColum.setWidth(100);
        mTableColum.setText("Packages");


        // Right part of Sash form
        mPackageRootComposite = new Composite(mSashForm, SWT.NONE);
        mPackageRootComposite.setLayout(new GridLayout(4, false/*makeColumnsEqual*/));
        mPackageRootComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        mPackageTextGroup = new Group(mPackageRootComposite, SWT.NONE);
        mPackageTextGroup.setText("Package Description && License");
        mPackageTextGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
        mPackageTextGroup.setLayout(new GridLayout(1, false/*makeColumnsEqual*/));

        mPackageText = new StyledText(mPackageTextGroup,
                        SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        mPackageText.setBackground(
                getParentShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        mPackageText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        mLicenseRadioAccept = new Button(mPackageRootComposite, SWT.RADIO);
        mLicenseRadioAccept.setText("Accept");
        mLicenseRadioAccept.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onLicenseRadioSelected();
            }
        });

        mLicenseRadioReject = new Button(mPackageRootComposite, SWT.RADIO);
        mLicenseRadioReject.setText("Reject");
        mLicenseRadioReject.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onLicenseRadioSelected();
            }
        });

        Label placeholder = new Label(mPackageRootComposite, SWT.NONE);
        placeholder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

        mLicenseRadioAcceptAll = new Button(mPackageRootComposite, SWT.RADIO);
        mLicenseRadioAcceptAll.setText("Accept All");
        mLicenseRadioAcceptAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onLicenseRadioSelected();
            }
        });

        mSashForm.setWeights(new int[] {200, 300});
    }

    /**
     * Creates and returns the contents of this dialog's button bar.
     * <p/>
     * This reimplements most of the code from the base class with a few exceptions:
     * <ul>
     * <li>Enforces 3 columns.
     * <li>Inserts a full-width error label.
     * <li>Inserts a help label on the left of the first button.
     * <li>Renames the OK button into "Install"
     * </ul>
     */
    @Override
    protected Control createButtonBar(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 0; // this is incremented by createButton
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        composite.setLayout(layout);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        composite.setLayoutData(data);
        composite.setFont(parent.getFont());

        // Error message area
        mErrorLabel = new Label(composite, SWT.NONE);
        mErrorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

        // Label at the left of the install/cancel buttons
        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        label.setText("[*] Something depends on this package");
        label.setEnabled(false);
        layout.numColumns++;

        // Add the ok/cancel to the button bar.
        createButtonsForButtonBar(composite);

        // the ok button should be an "install" button
        Button button = getButton(IDialogConstants.OK_ID);
        button.setText("Install");

        return composite;
    }

    // -- End of UI, Start of internal logic ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    @Override
    public void create() {
        super.create();

        // set window title
        getShell().setText("Choose Packages to Install");

        setWindowImage();

        // Automatically accept those with an empty license or no license
        for (ArchiveInfo ai : mArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                String license = a.getParentPackage().getLicense();
                ai.setAccepted(license == null || license.trim().length() == 0);
            }
        }

        // Fill the list with the replacement packages
        mTableViewPackage.setLabelProvider(new NewArchivesLabelProvider());
        mTableViewPackage.setContentProvider(new NewArchivesContentProvider());
        mTableViewPackage.setInput(mArchives);

        adjustColumnsWidth();

        // select first item
        mTablePackage.select(0);
        onPackageSelected();
    }

    /**
     * Creates the icon of the window shell.
     */
    private void setWindowImage() {
        String imageName = "android_icon_16.png"; //$NON-NLS-1$
        if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png"; //$NON-NLS-1$
        }

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                getShell().setImage(imgFactory.getImageByName(imageName));
            }
        }
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
                Rectangle r = mTablePackage.getClientArea();
                mTableColum.setWidth(r.width);
            }
        };
        mTablePackage.addControlListener(resizer);
        resizer.controlResized(null);
    }

    /**
     * Captures the window size before closing this.
     * @see #getInitialSize()
     */
    @Override
    public boolean close() {
        sLastSize = getShell().getSize();
        return super.close();
    }

    /**
     * Tries to reuse the last window size during this session.
     * <p/>
     * Note: the alternative would be to implement {@link #getDialogBoundsSettings()}
     * since the default {@link #getDialogBoundsStrategy()} is to persist both location
     * and size.
     */
    @Override
    protected Point getInitialSize() {
        if (sLastSize != null) {
            return sLastSize;
        } else {
            // Arbitrary values that look good on my screen and fit on 800x600
            return new Point(740, 370);
        }
    }

    /**
     * Callback invoked when a package item is selected in the list.
     */
    private void onPackageSelected() {
        ArchiveInfo ai = getSelectedArchive();
        displayInformation(ai);
        displayMissingDependency(ai);
        updateLicenceRadios(ai);
    }

    /** Returns the currently selected {@link ArchiveInfo} or null. */
    private ArchiveInfo getSelectedArchive() {
        ISelection sel = mTableViewPackage.getSelection();
        if (sel instanceof IStructuredSelection) {
            Object elem = ((IStructuredSelection) sel).getFirstElement();
            if (elem instanceof ArchiveInfo) {
                return (ArchiveInfo) elem;
            }
        }
        return null;
    }

    /**
     * Updates the package description and license text depending on the selected package.
     * <p/>
     * Note that right now there is no logic to support more than one level of dependencies
     * (e.g. A <- B <- C and A is disabled so C should be disabled; currently C's state depends
     * solely on B's state). We currently don't need this. It would be straightforward to add
     * if we had a need for it, though. This would require changes to {@link ArchiveInfo} and
     * {@link SdkUpdaterLogic}.
     */
    private void displayInformation(ArchiveInfo ai) {
        if (ai == null) {
            mPackageText.setText("Please select a package.");
            return;
        }

        Archive aNew = ai.getNewArchive();
        if (aNew == null) {
            // Only missing archives have a null archive, so we shouldn't be here.
            return;
        }

        Package pNew = aNew.getParentPackage();

        mPackageText.setText("");                   //$NON-NLS-1$

        addSectionTitle("Package Description\n");
        addText(pNew.getLongDescription(), "\n\n"); //$NON-NLS-1$

        Archive aOld = ai.getReplaced();
        if (aOld != null) {
            Package pOld = aOld.getParentPackage();

            FullRevision rOld = pOld.getRevision();
            FullRevision rNew = pNew.getRevision();

            boolean showRev = true;

            if (pNew instanceof IAndroidVersionProvider &&
                    pOld instanceof IAndroidVersionProvider) {
                AndroidVersion vOld = ((IAndroidVersionProvider) pOld).getAndroidVersion();
                AndroidVersion vNew = ((IAndroidVersionProvider) pNew).getAndroidVersion();

                if (!vOld.equals(vNew)) {
                    // Versions are different, so indicate more than just the revision.
                    addText(String.format("This update will replace API %1$s revision %2$s with API %3$s revision %4$s.\n\n",
                            vOld.getApiString(), rOld.toShortString(),
                            vNew.getApiString(), rNew.toShortString()));
                    showRev = false;
                }
            }

            if (showRev) {
                addText(String.format("This update will replace revision %1$s with revision %2$s.\n\n",
                        rOld.toShortString(),
                        rNew.toShortString()));
            }
        }

        ArchiveInfo[] aDeps = ai.getDependsOn();
        if ((aDeps != null && aDeps.length > 0) || ai.isDependencyFor()) {
            addSectionTitle("Dependencies\n");

            if (aDeps != null && aDeps.length > 0) {
                addText("Installing this package also requires installing:");
                for (ArchiveInfo aDep : aDeps) {
                    addText(String.format("\n- %1$s",
                            aDep.getShortDescription()));
                }
                addText("\n\n");
            }

            if (ai.isDependencyFor()) {
                addText("This package is a dependency for:");
                for (ArchiveInfo ai2 : ai.getDependenciesFor()) {
                    addText(String.format("\n- %1$s",
                            ai2.getShortDescription()));
                }
                addText("\n\n");
            }
        }

        addSectionTitle("Archive Description\n");
        addText(aNew.getLongDescription(), "\n\n");                             //$NON-NLS-1$

        String license = pNew.getLicense();
        if (license != null) {
            addSectionTitle("License\n");
            addText(license.trim(), "\n\n");                                       //$NON-NLS-1$
        }

        addSectionTitle("Site\n");
        SdkSource source = pNew.getParentSource();
        if (source != null) {
            addText(source.getShortDescription());
        }
    }

    /**
     * Computes and displays missing dependencies.
     *
     * If there's a selected package, check the dependency for that one.
     * Otherwise display the first missing dependency of any other package.
     */
    private void displayMissingDependency(ArchiveInfo ai) {
        String error = null;

        try {
            if (ai != null) {
                if (ai.isAccepted()) {
                    // Case where this package is accepted but blocked by another non-accepted one
                    ArchiveInfo[] adeps = ai.getDependsOn();
                    if (adeps != null) {
                        for (ArchiveInfo adep : adeps) {
                            if (!adep.isAccepted()) {
                                error = String.format("This package depends on '%1$s'.",
                                        adep.getShortDescription());
                                return;
                            }
                        }
                    }
                } else {
                    // Case where this package blocks another one when not accepted
                    for (ArchiveInfo adep : ai.getDependenciesFor()) {
                        // It only matters if the blocked one is accepted
                        if (adep.isAccepted()) {
                            error = String.format("Package '%1$s' depends on this one.",
                                    adep.getShortDescription());
                            return;
                        }
                    }
                }
            }

            // If there is no missing dependency on the current selection,
            // just find the first missing dependency of any other package.
            for (ArchiveInfo ai2 : mArchives) {
                if (ai2 == ai) {
                    // We already processed that one above.
                    continue;
                }
                if (ai2.isAccepted()) {
                    // The user requested to install this package.
                    // Check if all its dependencies are met.
                    ArchiveInfo[] adeps = ai2.getDependsOn();
                    if (adeps != null) {
                        for (ArchiveInfo adep : adeps) {
                            if (!adep.isAccepted()) {
                                error = String.format("Package '%1$s' depends on '%2$s'",
                                        ai2.getShortDescription(),
                                        adep.getShortDescription());
                                return;
                            }
                        }
                    }
                } else {
                    // The user did not request to install this package.
                    // Check whether this package blocks another one when not accepted.
                    for (ArchiveInfo adep : ai2.getDependenciesFor()) {
                        // It only matters if the blocked one is accepted
                        // or if it's a local archive that is already installed (these
                        // are marked as implicitly accepted, so it's the same test.)
                        if (adep.isAccepted()) {
                            error = String.format("Package '%1$s' depends on '%2$s'",
                                    adep.getShortDescription(),
                                    ai2.getShortDescription());
                            return;
                        }
                    }
                }
            }
        } finally {
            mErrorLabel.setText(error == null ? "" : error);        //$NON-NLS-1$
        }
    }

    private void addText(String...string) {
        for (String s : string) {
            mPackageText.append(s);
        }
    }

    private void addSectionTitle(String string) {
        String s = mPackageText.getText();
        int start = (s == null ? 0 : s.length());
        mPackageText.append(string);

        StyleRange sr = new StyleRange();
        sr.start = start;
        sr.length = string.length();
        sr.fontStyle = SWT.BOLD;
        sr.underline = true;
        mPackageText.setStyleRange(sr);
    }

    private void updateLicenceRadios(ArchiveInfo ai) {
        if (mInternalLicenseRadioUpdate) {
            return;
        }
        mInternalLicenseRadioUpdate = true;

        boolean oneAccepted = false;

        if (mLicenseAcceptAll) {
            mLicenseRadioAcceptAll.setSelection(true);
            mLicenseRadioAccept.setEnabled(true);
            mLicenseRadioReject.setEnabled(true);
            mLicenseRadioAccept.setSelection(false);
            mLicenseRadioReject.setSelection(false);
        } else {
            mLicenseRadioAcceptAll.setSelection(false);
            oneAccepted = ai != null && ai.isAccepted();
            mLicenseRadioAccept.setEnabled(ai != null);
            mLicenseRadioReject.setEnabled(ai != null);
            mLicenseRadioAccept.setSelection(oneAccepted);
            mLicenseRadioReject.setSelection(ai != null && ai.isRejected());
        }

        // The install button is enabled if there's at least one package accepted.
        // If the current one isn't, look for another one.
        boolean missing = mErrorLabel.getText() != null && mErrorLabel.getText().length() > 0;
        if (!missing && !oneAccepted) {
            for(ArchiveInfo ai2 : mArchives) {
                if (ai2.isAccepted()) {
                    oneAccepted = true;
                    break;
                }
            }
        }

        getButton(IDialogConstants.OK_ID).setEnabled(!missing && oneAccepted);

        mInternalLicenseRadioUpdate = false;
    }

    /**
     * Callback invoked when one of the radio license buttons is selected.
     *
     * - accept/refuse: toggle, update item checkbox
     * - accept all: set accept-all, check all items
     */
    private void onLicenseRadioSelected() {
        if (mInternalLicenseRadioUpdate) {
            return;
        }
        mInternalLicenseRadioUpdate = true;

        ArchiveInfo ai = getSelectedArchive();

        if (ai == null) {
            // Should never happen.
            return;
        }

        boolean needUpdate = true;

        if (!mLicenseAcceptAll && mLicenseRadioAcceptAll.getSelection()) {
            // Accept all has been switched on. Mark all packages as accepted
            mLicenseAcceptAll = true;
            for(ArchiveInfo ai2 : mArchives) {
                ai2.setAccepted(true);
                ai2.setRejected(false);
            }

        } else if (mLicenseRadioAccept.getSelection()) {
            // Accept only this one
            mLicenseAcceptAll = false;
            ai.setAccepted(true);
            ai.setRejected(false);

        } else if (mLicenseRadioReject.getSelection()) {
            // Reject only this one
            mLicenseAcceptAll = false;
            ai.setAccepted(false);
            ai.setRejected(true);

        } else {
            needUpdate = false;
        }

        mInternalLicenseRadioUpdate = false;

        if (needUpdate) {
            if (mLicenseAcceptAll) {
                mTableViewPackage.refresh();
            } else {
               mTableViewPackage.refresh(ai);
            }
            displayMissingDependency(ai);
            updateLicenceRadios(ai);
        }
    }

    /**
     * Callback invoked when a package item is double-clicked in the list.
     */
    private void onPackageDoubleClick() {
        ArchiveInfo ai = getSelectedArchive();

        if (ai == null) {
            // Should never happen.
            return;
        }

        boolean wasAccepted = ai.isAccepted();
        ai.setAccepted(!wasAccepted);
        ai.setRejected(wasAccepted);

        // update state
        mLicenseAcceptAll = false;
        mTableViewPackage.refresh(ai);
        displayMissingDependency(ai);
        updateLicenceRadios(ai);
    }

    private class NewArchivesLabelProvider extends LabelProvider {
        @Override
        public Image getImage(Object element) {
            assert element instanceof ArchiveInfo;
            ArchiveInfo ai = (ArchiveInfo) element;

            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                if (ai.isAccepted()) {
                    return imgFactory.getImageByName("accept_icon16.png");
                } else if (ai.isRejected()) {
                    return imgFactory.getImageByName("reject_icon16.png");
                }
                return imgFactory.getImageByName("unknown_icon16.png");
            }
            return super.getImage(element);
        }

        @Override
        public String getText(Object element) {
            assert element instanceof ArchiveInfo;
            ArchiveInfo ai = (ArchiveInfo) element;

            String desc = ai.getShortDescription();

            if (ai.isDependencyFor()) {
                desc += " [*]";
            }

            return desc;
        }
    }

    private class NewArchivesContentProvider implements IStructuredContentProvider {

        @Override
        public void dispose() {
            // pass
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // Ignore. The input is always mArchives
        }

        @Override
        public Object[] getElements(Object inputElement) {
            return mArchives.toArray();
        }
    }

    // End of hiding from SWT Designer
    //$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index 62a16d1..da71115 100755

//Synthetic comment -- @@ -1,1435 +1,1435 @@
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

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinPlatformToolsDependency;
import com.android.sdklib.internal.repository.packages.IMinToolsDependency;
import com.android.sdklib.internal.repository.packages.IPlatformDependency;
import com.android.sdklib.internal.repository.packages.MinToolsPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SamplePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The logic to compute which packages to install, based on the choices
 * made by the user. This adds required packages as needed.
 * <p/>
 * When the user doesn't provide a selection, looks at local package to find
 * those that can be updated and compute dependencies too.
 */
class SdkUpdaterLogic {

    private final IUpdaterData mUpdaterData;

    public SdkUpdaterLogic(IUpdaterData updaterData) {
        mUpdaterData = updaterData;
    }

    /**
     * Retrieves an unfiltered list of all remote archives.
     * The archives are guaranteed to be compatible with the current platform.
     */
    public List<ArchiveInfo> getAllRemoteArchives(
            SdkSources sources,
            Package[] localPkgs,
            boolean includeAll) {

        List<Package> remotePkgs = new ArrayList<Package>();
        SdkSource[] remoteSources = sources.getAllSources();
        fetchRemotePackages(remotePkgs, remoteSources);

        ArrayList<Archive> archives = new ArrayList<Archive>();
        for (Package remotePkg : remotePkgs) {
            // Only look for non-obsolete updates unless requested to include them
            if (includeAll || !remotePkg.isObsolete()) {
                // Found a suitable update. Only accept the remote package
                // if it provides at least one compatible archive

                addArchives:
                for (Archive a : remotePkg.getArchives()) {
                    if (a.isCompatible()) {

                        // If we're trying to add a package for revision N,
                        // make sure we don't also have a package for revision N-1.
                        for (int i = archives.size() - 1; i >= 0; i--) {
                            Package pkgFound = archives.get(i).getParentPackage();
                            if (pkgFound.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
                                // This package can update one we selected earlier.
                                // Remove the one that can be updated by this new one.
                                archives.remove(i);
                            } else if (remotePkg.canBeUpdatedBy(pkgFound) == UpdateInfo.UPDATE) {
                                // There is a package in the list that is already better
                                // than the one we want to add, so don't add it.
                                break addArchives;
                            }
                        }

                        archives.add(a);
                        break;
                    }
                }
            }
        }

        ArrayList<ArchiveInfo> result = new ArrayList<ArchiveInfo>();

        ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

        for (Archive a : archives) {
            insertArchive(a,
                    result,
                    archives,
                    remotePkgs,
                    remoteSources,
                    localArchives,
                    false /*automated*/);
        }

        return result;
    }

    /**
     * Compute which packages to install by taking the user selection
     * and adding required packages as needed.
     *
     * When the user doesn't provide a selection, looks at local packages to find
     * those that can be updated and compute dependencies too.
     */
    public List<ArchiveInfo> computeUpdates(
            Collection<Archive> selectedArchives,
            SdkSources sources,
            Package[] localPkgs,
            boolean includeAll) {

        List<ArchiveInfo> archives = new ArrayList<ArchiveInfo>();
        List<Package>   remotePkgs = new ArrayList<Package>();
        SdkSource[] remoteSources = sources.getAllSources();

        // Create ArchiveInfos out of local (installed) packages.
        ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

        // If we do not have a specific list of archives to install (that is the user
        // selected "update all" rather than request specific packages), then we try to
        // find updates based on the *existing* packages.
        if (selectedArchives == null) {
            selectedArchives = findUpdates(
                    localArchives,
                    remotePkgs,
                    remoteSources,
                    includeAll);
        }

        // Once we have a list of packages to install, we try to solve all their
        // dependencies by automatically adding them to the list of things to install.
        // This works on the list provided either by the user directly or the list
        // computed from potential updates.
        for (Archive a : selectedArchives) {
            insertArchive(a,
                    archives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives,
                    false /*automated*/);
        }

        // Finally we need to look at *existing* packages which are not being updated
        // and check if they have any missing dependencies and suggest how to fix
        // these dependencies.
        fixMissingLocalDependencies(
                archives,
                selectedArchives,
                remotePkgs,
                remoteSources,
                localArchives);

        return archives;
    }

    private double getRevisionRank(FullRevision rev) {
        int p = rev.isPreview() ? 999 : 999 - rev.getPreview();
        return  rev.getMajor() +
                rev.getMinor() / 1000.d +
                rev.getMicro() / 1000000.d +
                p              / 1000000000.d;
    }

    /**
     * Finds new packages that the user does not have in his/her local SDK
     * and adds them to the list of archives to install.
     * <p/>
     * The default is to only find "new" platforms, that is anything more
     * recent than the highest platform currently installed.
     * A side effect is that for an empty SDK install this will list *all*
     * platforms available (since there's no "highest" installed platform.)
     *
     * @param archives The in-out list of archives to install. Typically the
     *  list is not empty at first as it should contain any archives that is
     *  already scheduled for install. This method will add to the list.
     * @param sources The list of all sources, to fetch them as necessary.
     * @param localPkgs The list of all currently installed packages.
     * @param includeAll When true, this will list all platforms.
     * (included these lower than the highest installed one) as well as
     * all obsolete packages of these platforms.
     */
    public void addNewPlatforms(
            Collection<ArchiveInfo> archives,
            SdkSources sources,
            Package[] localPkgs,
            boolean includeAll) {

        // Create ArchiveInfos out of local (installed) packages.
        ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

        // Find the highest platform installed
        double currentPlatformScore = 0;
        double currentSampleScore = 0;
        double currentAddonScore = 0;
        double currentDocScore = 0;
        HashMap<String, Double> currentExtraScore = new HashMap<String, Double>();
        if (!includeAll) {
            if (localPkgs != null) {
                for (Package p : localPkgs) {
                    double rev = getRevisionRank(p.getRevision());
                    int api = 0;
                    boolean isPreview = false;
                    if (p instanceof IAndroidVersionProvider) {
                        AndroidVersion vers = ((IAndroidVersionProvider) p).getAndroidVersion();
                        api = vers.getApiLevel();
                        isPreview = vers.isPreview();
                    }

                    // The score is 1000*api + (999 if preview) + rev
                    // This allows previews to rank above a non-preview and
                    // allows revisions to rank appropriately.
                    double score = api * 1000 + (isPreview ? 999 : 0) + rev;

                    if (p instanceof PlatformPackage) {
                        currentPlatformScore = Math.max(currentPlatformScore, score);
                    } else if (p instanceof SamplePackage) {
                        currentSampleScore = Math.max(currentSampleScore, score);
                    } else if (p instanceof AddonPackage) {
                        currentAddonScore = Math.max(currentAddonScore, score);
                    } else if (p instanceof ExtraPackage) {
                        currentExtraScore.put(((ExtraPackage) p).getPath(), score);
                    } else if (p instanceof DocPackage) {
                        currentDocScore = Math.max(currentDocScore, score);
                    }
                }
            }
        }

        SdkSource[] remoteSources = sources.getAllSources();
        ArrayList<Package> remotePkgs = new ArrayList<Package>();
        fetchRemotePackages(remotePkgs, remoteSources);

        Package suggestedDoc = null;

        for (Package p : remotePkgs) {
            // Skip obsolete packages unless requested to include them.
            if (p.isObsolete() && !includeAll) {
                continue;
            }

            double rev = getRevisionRank(p.getRevision());
            int api = 0;
            boolean isPreview = false;
            if (p instanceof IAndroidVersionProvider) {
                AndroidVersion vers = ((IAndroidVersionProvider) p).getAndroidVersion();
                api = vers.getApiLevel();
                isPreview = vers.isPreview();
            }

            double score = api * 1000 + (isPreview ? 999 : 0) + rev;

            boolean shouldAdd = false;
            if (p instanceof PlatformPackage) {
                shouldAdd = score > currentPlatformScore;
            } else if (p instanceof SamplePackage) {
                shouldAdd = score > currentSampleScore;
            } else if (p instanceof AddonPackage) {
                shouldAdd = score > currentAddonScore;
            } else if (p instanceof ExtraPackage) {
                String key = ((ExtraPackage) p).getPath();
                shouldAdd = !currentExtraScore.containsKey(key) ||
                    score > currentExtraScore.get(key).doubleValue();
            } else if (p instanceof DocPackage) {
                // We don't want all the doc, only the most recent one
                if (score > currentDocScore) {
                    suggestedDoc = p;
                    currentDocScore = score;
                }
            }

            if (shouldAdd) {
                // We should suggest this package for installation.
                for (Archive a : p.getArchives()) {
                    if (a.isCompatible()) {
                        insertArchive(a,
                                archives,
                                null /*selectedArchives*/,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }

            if (p instanceof PlatformPackage && (score >= currentPlatformScore)) {
                // We just added a new platform *or* we are visiting the highest currently
                // installed platform. In either case we want to make sure it either has
                // its own system image or that we provide one by default.
                PlatformPackage pp = (PlatformPackage) p;
                if (pp.getIncludedAbi() == null) {
                    for (Package p2 : remotePkgs) {
                        if (!(p2 instanceof SystemImagePackage) ||
                             (p2.isObsolete() && !includeAll)) {
                            continue;
                        }
                        SystemImagePackage sip = (SystemImagePackage) p2;
                        if (sip.getAndroidVersion().equals(pp.getAndroidVersion())) {
                            for (Archive a : sip.getArchives()) {
                                if (a.isCompatible()) {
                                    insertArchive(a,
                                            archives,
                                            null /*selectedArchives*/,
                                            remotePkgs,
                                            remoteSources,
                                            localArchives,
                                            true /*automated*/);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (suggestedDoc != null) {
            // We should suggest this package for installation.
            for (Archive a : suggestedDoc.getArchives()) {
                if (a.isCompatible()) {
                    insertArchive(a,
                            archives,
                            null /*selectedArchives*/,
                            remotePkgs,
                            remoteSources,
                            localArchives,
                            true /*automated*/);
                }
            }
        }
    }

    /**
     * Create a array of {@link ArchiveInfo} based on all local (already installed)
     * packages. The array is always non-null but may be empty.
     * <p/>
     * The local {@link ArchiveInfo} are guaranteed to have one non-null archive
     * that you can retrieve using {@link ArchiveInfo#getNewArchive()}.
     */
    protected ArchiveInfo[] createLocalArchives(Package[] localPkgs) {

        if (localPkgs != null) {
            ArrayList<ArchiveInfo> list = new ArrayList<ArchiveInfo>();
            for (Package p : localPkgs) {
                // Only accept packages that have one compatible archive.
                // Local package should have 1 and only 1 compatible archive anyway.
                for (Archive a : p.getArchives()) {
                    if (a != null && a.isCompatible()) {
                        // We create an "installed" archive info to wrap the local package.
                        // Note that dependencies are not computed since right now we don't
                        // deal with more than one level of dependencies and installed archives
                        // are deemed implicitly accepted anyway.
                        list.add(new LocalArchiveInfo(a));
                    }
                }
            }

            return list.toArray(new ArchiveInfo[list.size()]);
        }

        return new ArchiveInfo[0];
    }

    /**
     * Find suitable updates to all current local packages.
     * <p/>
     * Returns a list of potential updates for *existing* packages. This does NOT solve
     * dependencies for the new packages.
     * <p/>
     * Always returns a non-null collection, which can be empty.
     */
    private Collection<Archive> findUpdates(
            ArchiveInfo[] localArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            boolean includeAll) {
        ArrayList<Archive> updates = new ArrayList<Archive>();

        fetchRemotePackages(remotePkgs, remoteSources);

        for (ArchiveInfo ai : localArchives) {
            Archive na = ai.getNewArchive();
            if (na == null) {
                continue;
            }
            Package localPkg = na.getParentPackage();

            for (Package remotePkg : remotePkgs) {
                // Only look for non-obsolete updates unless requested to include them
                if ((includeAll || !remotePkg.isObsolete()) &&
                        localPkg.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
                    // Found a suitable update. Only accept the remote package
                    // if it provides at least one compatible archive

                    addArchives:
                    for (Archive a : remotePkg.getArchives()) {
                        if (a.isCompatible()) {

                            // If we're trying to add a package for revision N,
                            // make sure we don't also have a package for revision N-1.
                            for (int i = updates.size() - 1; i >= 0; i--) {
                                Package pkgFound = updates.get(i).getParentPackage();
                                if (pkgFound.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
                                    // This package can update one we selected earlier.
                                    // Remove the one that can be updated by this new one.
                                   updates.remove(i);
                                } else if (remotePkg.canBeUpdatedBy(pkgFound) ==
                                                UpdateInfo.UPDATE) {
                                    // There is a package in the list that is already better
                                    // than the one we want to add, so don't add it.
                                    break addArchives;
                                }
                            }

                            updates.add(a);
                            break;
                        }
                    }
                }
            }
        }

        return updates;
    }

    /**
     * Check all local archives which are NOT being updated and see if they
     * miss any dependency. If they do, try to fix that dependency by selecting
     * an appropriate package.
     */
    private void fixMissingLocalDependencies(
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        nextLocalArchive: for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            Package p = a == null ? null : a.getParentPackage();
            if (p == null) {
                continue;
            }

            // Is this local archive being updated?
            for (ArchiveInfo ai2 : outArchives) {
                if (ai2.getReplaced() == a) {
                    // this new archive will replace the current local one,
                    // so we don't have to care about fixing dependencies (since the
                    // new archive should already have had its dependencies resolved)
                    continue nextLocalArchive;
                }
            }

            // find dependencies for the local archive and add them as needed
            // to the outArchives collection.
            ArchiveInfo[] deps = findDependency(p,
                  outArchives,
                  selectedArchives,
                  remotePkgs,
                  remoteSources,
                  localArchives);

            if (deps != null) {
                // The already installed archive has a missing dependency, which we
                // just selected for install. Make sure we remember the dependency
                // so that we can enforce it later in the UI.
                for (ArchiveInfo aid : deps) {
                    aid.addDependencyFor(ai);
                }
            }
        }
    }

    private ArchiveInfo insertArchive(Archive archive,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives,
            boolean automated) {
        Package p = archive.getParentPackage();

        // Is this an update?
        Archive updatedArchive = null;
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package lp = a.getParentPackage();

                if (lp.canBeUpdatedBy(p) == UpdateInfo.UPDATE) {
                    updatedArchive = a;
                }
            }
        }

        // Find dependencies and adds them as needed to outArchives
        ArchiveInfo[] deps = findDependency(p,
                outArchives,
                selectedArchives,
                remotePkgs,
                remoteSources,
                localArchives);

        // Make sure it's not a dup
        ArchiveInfo ai = null;

        for (ArchiveInfo ai2 : outArchives) {
            Archive a2 = ai2.getNewArchive();
            if (a2 != null && a2.getParentPackage().sameItemAs(archive.getParentPackage())) {
                ai = ai2;
                break;
            }
        }

        if (ai == null) {
            ai = new ArchiveInfo(
                archive,        //newArchive
                updatedArchive, //replaced
                deps            //dependsOn
                );
            outArchives.add(ai);
        }

        if (deps != null) {
            for (ArchiveInfo d : deps) {
                d.addDependencyFor(ai);
            }
        }

        return ai;
    }

    /**
     * Resolves dependencies for a given package.
     *
     * Returns null if no dependencies were found.
     * Otherwise return an array of {@link ArchiveInfo}, which is guaranteed to have
     * at least size 1 and contain no null elements.
     */
    private ArchiveInfo[] findDependency(Package pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        // Current dependencies can be:
        // - addon: *always* depends on platform of same API level
        // - platform: *might* depends on tools of rev >= min-tools-rev
        // - extra: *might* depends on platform with api >= min-api-level

        Set<ArchiveInfo> aiFound = new HashSet<ArchiveInfo>();

        if (pkg instanceof IPlatformDependency) {
            ArchiveInfo ai = findPlatformDependency(
                    (IPlatformDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IMinToolsDependency) {

            ArchiveInfo ai = findToolsDependency(
                    (IMinToolsDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IMinPlatformToolsDependency) {

            ArchiveInfo ai = findPlatformToolsDependency(
                    (IMinPlatformToolsDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IMinApiLevelDependency) {

            ArchiveInfo ai = findMinApiLevelDependency(
                    (IMinApiLevelDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IExactApiLevelDependency) {

            ArchiveInfo ai = findExactApiLevelDependency(
                    (IExactApiLevelDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (aiFound.size() > 0) {
            ArchiveInfo[] result = aiFound.toArray(new ArchiveInfo[aiFound.size()]);
            Arrays.sort(result);
            return result;
        }

        return null;
    }

    /**
     * Resolves dependencies on tools.
     *
     * A platform or an extra package can both have a min-tools-rev, in which case it
     * depends on having a tools package of the requested revision.
     * Finds the tools dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    protected ArchiveInfo findToolsDependency(
            IMinToolsDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {
        // This is the requirement to match.
        FullRevision rev = pkg.getMinToolsRevision();

        if (rev.equals(MinToolsPackage.MIN_TOOLS_REV_NOT_SPECIFIED)) {
            // Well actually there's no requirement.
            return null;
        }

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install
        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
                        // The dependency is already scheduled for install, nothing else to do.
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
                        // It's not already in the list of things to install, so add it now
                        return insertArchive(a,
                                outArchives,
                                selectedArchives,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof ToolPackage) {
                if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
                    // It's not already in the list of things to install, so add the
                    // first compatible archive we can find.
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            return insertArchive(a,
                                    outArchives,
                                    selectedArchives,
                                    remotePkgs,
                                    remoteSources,
                                    localArchives,
                                    true /*automated*/);
                        }
                    }
                }
            }
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this extra depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_TOOL, rev);
    }

    /**
     * Resolves dependencies on platform-tools.
     *
     * A tool package can have a min-platform-tools-rev, in which case it depends on
     * having a platform-tool package of the requested revision.
     * Finds the platform-tool dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    protected ArchiveInfo findPlatformToolsDependency(
            IMinPlatformToolsDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {
        // This is the requirement to match.
        FullRevision rev = pkg.getMinPlatformToolsRevision();
        boolean findMax = false;
        ArchiveInfo aiMax = null;
        Archive aMax = null;

        if (rev.equals(IMinPlatformToolsDependency.MIN_PLATFORM_TOOLS_REV_INVALID)) {
            // The requirement is invalid, which is not supposed to happen since this
            // property is mandatory. However in a typical upgrade scenario we can end
            // up with the previous updater managing a new package and not dealing
            // correctly with the new unknown property.
            // So instead we parse all the existing and remote packages and try to find
            // the max available revision and we'll use it.
            findMax = true;
        }

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
                        rev = r;
                        aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install
        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
                        rev = r;
                        aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
                        // The dependency is already scheduled for install, nothing else to do.
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > 0) {
                        rev = r;
                        aiMax = null;
                        aMax = a;
                    } else if (!findMax && r.compareTo(rev) >= 0) {
                        // It's not already in the list of things to install, so add it now
                        return insertArchive(a,
                                outArchives,
                                selectedArchives,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof PlatformToolPackage) {
                FullRevision r = ((PlatformToolPackage) p).getRevision();
                if (r.compareTo(rev) >= 0) {
                    // Make sure there's at least one valid archive here
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            if (findMax && r.compareTo(rev) > 0) {
                                rev = r;
                                aiMax = null;
                                aMax = a;
                            } else if (!findMax && r.compareTo(rev) >= 0) {
                                // It's not already in the list of things to install, so add the
                                // first compatible archive we can find.
                                return insertArchive(a,
                                        outArchives,
                                        selectedArchives,
                                        remotePkgs,
                                        remoteSources,
                                        localArchives,
                                        true /*automated*/);
                            }
                        }
                    }
                }
            }
        }

        if (findMax) {
            if (aMax != null) {
                return insertArchive(aMax,
                        outArchives,
                        selectedArchives,
                        remotePkgs,
                        remoteSources,
                        localArchives,
                        true /*automated*/);
            } else if (aiMax != null) {
                return aiMax;
            }
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this package depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_PLATFORM_TOOL, rev);
    }

    /**
     * Resolves dependencies on platform for an addon.
     *
     * An addon depends on having a platform with the same API level.
     *
     * Finds the platform dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    protected ArchiveInfo findPlatformDependency(
            IPlatformDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {
        // This is the requirement to match.
        AndroidVersion v = pkg.getAndroidVersion();

        // Find a platform that would satisfy the requirement.

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install
        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                        // The dependency is already scheduled for install, nothing else to do.
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                        // It's not already in the list of things to install, so add it now
                        return insertArchive(a,
                                outArchives,
                                selectedArchives,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof PlatformPackage) {
                if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                    // It's not already in the list of things to install, so add the
                    // first compatible archive we can find.
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            return insertArchive(a,
                                    outArchives,
                                    selectedArchives,
                                    remotePkgs,
                                    remoteSources,
                                    localArchives,
                                    true /*automated*/);
                        }
                    }
                }
            }
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this addon depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingPlatformArchiveInfo(pkg.getAndroidVersion());
    }

    /**
     * Resolves platform dependencies for extras.
     * An extra depends on having a platform with a minimun API level.
     *
     * We try to return the highest API level available above the specified minimum.
     * Note that installed packages have priority so if one installed platform satisfies
     * the dependency, we'll use it even if there's a higher API platform available but
     * not installed yet.
     *
     * Finds the platform dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    protected ArchiveInfo findMinApiLevelDependency(
            IMinApiLevelDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        int api = pkg.getMinApiLevel();

        if (api == IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED) {
            return null;
        }

        // Find a platform that would satisfy the requirement.

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install
        int foundApi = 0;
        ArchiveInfo foundAi = null;

        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                        if (api > foundApi) {
                            foundApi = api;
                            foundAi = ai;
                        }
                    }
                }
            }
        }

        if (foundAi != null) {
            // The dependency is already scheduled for install, nothing else to do.
            return foundAi;
        }

        // Otherwise look in the selected archives *or* available remote packages
        // and takes the best out of the two sets.
        foundApi = 0;
        Archive foundArchive = null;
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                        if (api > foundApi) {
                            foundApi = api;
                            foundArchive = a;
                        }
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof PlatformPackage) {
                if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                    if (api > foundApi) {
                        // It's not already in the list of things to install, so add the
                        // first compatible archive we can find.
                        for (Archive a : p.getArchives()) {
                            if (a.isCompatible()) {
                                foundApi = api;
                                foundArchive = a;
                            }
                        }
                    }
                }
            }
        }

        if (foundArchive != null) {
            // It's not already in the list of things to install, so add it now
            return insertArchive(foundArchive,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives,
                    true /*automated*/);
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this extra depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingPlatformArchiveInfo(new AndroidVersion(api, null /*codename*/));
    }

    /**
     * Resolves platform dependencies for add-ons.
     * An add-ons depends on having a platform with an exact specific API level.
     *
     * Finds the platform dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    protected ArchiveInfo findExactApiLevelDependency(
            IExactApiLevelDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        int api = pkg.getExactApiLevel();

        if (api == IExactApiLevelDependency.API_LEVEL_INVALID) {
            return null;
        }

        // Find a platform that would satisfy the requirement.

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install

        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                        // It's not already in the list of things to install, so add it now
                        return insertArchive(a,
                                outArchives,
                                selectedArchives,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof PlatformPackage) {
                if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                    // It's not already in the list of things to install, so add the
                    // first compatible archive we can find.
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            return insertArchive(a,
                                    outArchives,
                                    selectedArchives,
                                    remotePkgs,
                                    remoteSources,
                                    localArchives,
                                    true /*automated*/);
                        }
                    }
                }
            }
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this extra depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingPlatformArchiveInfo(new AndroidVersion(api, null /*codename*/));
    }

    /**
     * Fetch all remote packages only if really needed.
     * <p/>
     * This method takes a list of sources. Each source is only fetched once -- that is each
     * source keeps the list of packages that we fetched from the remote XML file. If the list
     * is null, it means this source has never been fetched so we'll do it once here. Otherwise
     * we rely on the cached list of packages from this source.
     * <p/>
     * This method also takes a remote package list as input, which it will fill out.
     * If a source has already been fetched, we'll add its packages to the remote package list
     * if they are not already present. Otherwise, the source will be fetched and the packages
     * added to the list.
     *
     * @param remotePkgs An in-out list of packages available from remote sources.
     *                   This list must not be null.
     *                   It can be empty or already contain some packages.
     * @param remoteSources A list of available remote sources to fetch from.
     */
    protected void fetchRemotePackages(
            final Collection<Package> remotePkgs,
            final SdkSource[] remoteSources) {
        if (remotePkgs.size() > 0) {
            return;
        }

        // First check if there's any remote source we need to fetch.
        // This will bring the task window, so we rather not display it unless
        // necessary.
        boolean needsFetch = false;
        for (final SdkSource remoteSrc : remoteSources) {
            Package[] pkgs = remoteSrc.getPackages();
            if (pkgs == null) {
                // This source has never been fetched. We'll do it below.
                needsFetch = true;
            } else {
                // This source has already been fetched and we know its package list.
                // We still need to make sure all of its packages are present in the
                // remotePkgs list.

                nextPackage: for (Package pkg : pkgs) {
                    for (Archive a : pkg.getArchives()) {
                        // Only add a package if it contains at least one compatible archive
                        // and is not already in the remote package list.
                        if (a.isCompatible()) {
                            if (!remotePkgs.contains(pkg)) {
                                remotePkgs.add(pkg);
                                continue nextPackage;
                            }
                        }
                    }
                }
            }
        }

        if (!needsFetch) {
            return;
        }

        final boolean forceHttp = mUpdaterData.getSettingsController().getSettings().getForceHttp();

        mUpdaterData.getTaskFactory().start("Refresh Sources", new ITask() {
            @Override
            public void run(ITaskMonitor monitor) {
                for (SdkSource remoteSrc : remoteSources) {
                    Package[] pkgs = remoteSrc.getPackages();

                    if (pkgs == null) {
                        remoteSrc.load(mUpdaterData.getDownloadCache(), monitor, forceHttp);
                        pkgs = remoteSrc.getPackages();
                    }

                    if (pkgs != null) {
                        nextPackage: for (Package pkg : pkgs) {
                            for (Archive a : pkg.getArchives()) {
                                // Only add a package if it contains at least one compatible archive
                                // and is not already in the remote package list.
                                if (a.isCompatible()) {
                                    if (!remotePkgs.contains(pkg)) {
                                        remotePkgs.add(pkg);
                                        continue nextPackage;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    /**
     * A {@link LocalArchiveInfo} is an {@link ArchiveInfo} that wraps an already installed
     * "local" package/archive.
     * <p/>
     * In this case, the "new Archive" is still expected to be non null and the
     * "replaced Archive" is null. Installed archives are always accepted and never
     * rejected.
     * <p/>
     * Dependencies are not set.
     */
    private static class LocalArchiveInfo extends ArchiveInfo {

        public LocalArchiveInfo(Archive localArchive) {
            super(localArchive, null /*replaced*/, null /*dependsOn*/);
        }

        /** Installed archives are always accepted. */
        @Override
        public boolean isAccepted() {
            return true;
        }

        /** Installed archives are never rejected. */
        @Override
        public boolean isRejected() {
            return false;
        }
    }

    /**
     * A {@link MissingPlatformArchiveInfo} is an {@link ArchiveInfo} that represents a
     * package/archive that we <em>really</em> need as a dependency but that we don't have.
     * <p/>
     * This is currently used for addons and extras in case we can't find a matching base platform.
     * <p/>
     * This kind of archive has specific properties: the new archive to install is null,
     * there are no dependencies and no archive is being replaced. The info can never be
     * accepted and is always rejected.
     */
    private static class MissingPlatformArchiveInfo extends ArchiveInfo {

        private final AndroidVersion mVersion;

        /**
         * Constructs a {@link MissingPlatformArchiveInfo} that will indicate the
         * given platform version is missing.
         */
        public MissingPlatformArchiveInfo(AndroidVersion version) {
            super(null /*newArchive*/, null /*replaced*/, null /*dependsOn*/);
            mVersion = version;
        }

        /** Missing archives are never accepted. */
        @Override
        public boolean isAccepted() {
            return false;
        }

        /** Missing archives are always rejected. */
        @Override
        public boolean isRejected() {
            return true;
        }

        @Override
        public String getShortDescription() {
            return String.format("Missing SDK Platform Android%1$s, API %2$d",
                    mVersion.isPreview() ? " Preview" : "",
                    mVersion.getApiLevel());
        }
    }

    /**
     * A {@link MissingArchiveInfo} is an {@link ArchiveInfo} that represents a
     * package/archive that we <em>really</em> need as a dependency but that we don't have.
     * <p/>
     * This is currently used for extras in case we can't find a matching tool revision
     * or when a platform-tool is missing.
     * <p/>
     * This kind of archive has specific properties: the new archive to install is null,
     * there are no dependencies and no archive is being replaced. The info can never be
     * accepted and is always rejected.
     */
    private static class MissingArchiveInfo extends ArchiveInfo {

        private final FullRevision mRevision;
        private final String mTitle;

        public static final String TITLE_TOOL = "Tools";
        public static final String TITLE_PLATFORM_TOOL = "Platform-tools";

        /**
         * Constructs a {@link MissingPlatformArchiveInfo} that will indicate the
         * given platform version is missing.
         *
         * @param title Typically "Tools" or "Platform-tools".
         * @param revision The required revision.
         */
        public MissingArchiveInfo(String title, FullRevision revision) {
            super(null /*newArchive*/, null /*replaced*/, null /*dependsOn*/);
            mTitle = title;
            mRevision = revision;
        }

        /** Missing archives are never accepted. */
        @Override
        public boolean isAccepted() {
            return false;
        }

        /** Missing archives are always rejected. */
        @Override
        public boolean isRejected() {
            return true;
        }

        @Override
        public String getShortDescription() {
            return String.format("Missing Android SDK %1$s, revision %2$s",
                    mTitle,
                    mRevision.toShortString());
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 09edca8..513d159 100755

//Synthetic comment -- @@ -1,382 +1,382 @@
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

import com.android.annotations.NonNull;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.ISdkLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Controller class to get settings values. Settings are kept in-memory.
 * Users of this class must first load the settings before changing them and save
 * them when modified.
 * <p/>
 * Settings are enumerated by constants in {@link ISettingsPage}.
 */
public class SettingsController {

    private static final String SETTINGS_FILENAME = "androidtool.cfg"; //$NON-NLS-1$

    private final ISdkLog mSdkLog;
    private final Settings mSettings;

    public interface OnChangedListener {
        public void onSettingsChanged(SettingsController controller, Settings oldSettings);
    }
    private final List<OnChangedListener> mChangedListeners = new ArrayList<OnChangedListener>(1);

    /** The currently associated {@link ISettingsPage}. Can be null. */
    private ISettingsPage mSettingsPage;

    /**
     * Constructs a new default {@link SettingsController}.
     *
     * @param sdkLog A non-null logger to use.
     */
    public SettingsController(@NonNull ISdkLog sdkLog) {
        mSdkLog = sdkLog;
        mSettings = new Settings();
    }

    /**
     * Specialized constructor that wraps an existing {@link Settings} instance.
     * This is mostly used in unit-tests to override settings that are being used.
     * Normal usage should NOT need to call this constructor.
     *
     * @param sdkLog   A non-null logger to use.
     * @param settings A non-null {@link Settings} to use as-is. It is not duplicated.
     */
    protected SettingsController(@NonNull ISdkLog sdkLog, @NonNull Settings settings) {
        mSdkLog = sdkLog;
        mSettings = settings;
    }

    public Settings getSettings() {
        return mSettings;
    }

    public void registerOnChangedListener(OnChangedListener listener) {
        if (listener != null && !mChangedListeners.contains(listener)) {
            mChangedListeners.add(listener);
        }
    }

    public void unregisterOnChangedListener(OnChangedListener listener) {
        if (listener != null) {
            mChangedListeners.remove(listener);
        }
    }

    //--- Access to settings ------------


    public static class Settings {
        private final Properties mProperties;

        /** Initialize an empty set of settings. */
        public Settings() {
            mProperties = new Properties();
        }

        /** Duplicates a set of settings. */
        public Settings(Settings settings) {
            this();
            for (Entry<Object, Object> entry : settings.mProperties.entrySet()) {
                mProperties.put(entry.getKey(), entry.getValue());
            }
        }

        /**
         * Specialized constructor for unit-tests that wraps an existing
         * {@link Properties} instance. The properties instance is not duplicated,
         * it's merely used as-is and changes will be reflected directly.
         */
        protected Settings(Properties properties) {
            mProperties = properties;
        }

        /**
         * Returns the value of the {@link ISettingsPage#KEY_FORCE_HTTP} setting.
         *
         * @see ISettingsPage#KEY_FORCE_HTTP
         */
        public boolean getForceHttp() {
            return Boolean.parseBoolean(mProperties.getProperty(ISettingsPage.KEY_FORCE_HTTP));
        }

        /**
         * Returns the value of the {@link ISettingsPage#KEY_ASK_ADB_RESTART} setting.
         *
         * @see ISettingsPage#KEY_ASK_ADB_RESTART
         */
        public boolean getAskBeforeAdbRestart() {
            return Boolean.parseBoolean(mProperties.getProperty(ISettingsPage.KEY_ASK_ADB_RESTART));
        }

        /**
         * Returns the value of the {@link ISettingsPage#KEY_USE_DOWNLOAD_CACHE} setting.
         *
         * @see ISettingsPage#KEY_USE_DOWNLOAD_CACHE
         */
        public boolean getUseDownloadCache() {
            return Boolean.parseBoolean(
                    mProperties.getProperty(
                            ISettingsPage.KEY_USE_DOWNLOAD_CACHE,
                            Boolean.TRUE.toString()));
        }

        /**
         * Returns the value of the {@link ISettingsPage#KEY_SHOW_UPDATE_ONLY} setting.
         *
         * @see ISettingsPage#KEY_SHOW_UPDATE_ONLY
         */
        public boolean getShowUpdateOnly() {
            return Boolean.parseBoolean(
                    mProperties.getProperty(
                            ISettingsPage.KEY_SHOW_UPDATE_ONLY,
                            Boolean.TRUE.toString()));
        }

        /**
         * Returns the value of the {@link ISettingsPage#KEY_ENABLE_PREVIEWS} setting.
         *
         * @see ISettingsPage#KEY_ENABLE_PREVIEWS
         */
        public boolean getEnablePreviews() {
            return Boolean.parseBoolean(mProperties.getProperty(ISettingsPage.KEY_ENABLE_PREVIEWS));
        }

        /**
         * Returns the value of the {@link ISettingsPage#KEY_MONITOR_DENSITY} setting
         * @see ISettingsPage#KEY_MONITOR_DENSITY
         */
        public int getMonitorDensity() {
            String value = mProperties.getProperty(ISettingsPage.KEY_MONITOR_DENSITY, null);
            if (value == null) {
                return -1;
            }

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    /**
     * Sets the value of the {@link ISettingsPage#KEY_SHOW_UPDATE_ONLY} setting.
     *
     * @param enabled True if only compatible non-obsolete update items should be shown.
     * @see ISettingsPage#KEY_SHOW_UPDATE_ONLY
     */
    public void setShowUpdateOnly(boolean enabled) {
        setSetting(ISettingsPage.KEY_SHOW_UPDATE_ONLY, enabled);
    }

    /**
     * Sets the value of the {@link ISettingsPage#KEY_MONITOR_DENSITY} setting.
     *
     * @param density the density of the monitor
     * @see ISettingsPage#KEY_MONITOR_DENSITY
     */
    public void setMonitorDensity(int density) {
        mSettings.mProperties.setProperty(
                ISettingsPage.KEY_MONITOR_DENSITY, Integer.toString(density));
    }

    /**
     * Internal helper to set a boolean setting.
     */
    void setSetting(String key, boolean value) {
        mSettings.mProperties.setProperty(key, Boolean.toString(value));
    }

    //--- Controller methods -------------

    /**
     * Associate the given {@link ISettingsPage} with this {@link SettingsController}.
     * <p/>
     * This loads the current properties into the setting page UI.
     * It then associates the SettingsChanged callback with this controller.
     * <p/>
     * If the setting page given is null, it will be unlinked from controller.
     *
     * @param settingsPage An {@link ISettingsPage} to associate with the controller.
     */
    public void setSettingsPage(ISettingsPage settingsPage) {
        mSettingsPage = settingsPage;

        if (settingsPage != null) {
            settingsPage.loadSettings(mSettings.mProperties);

            settingsPage.setOnSettingsChanged(new ISettingsPage.SettingsChangedCallback() {
                @Override
                public void onSettingsChanged(ISettingsPage page) {
                    SettingsController.this.onSettingsChanged();
                }
            });
        }
    }

    /**
     * Load settings from the settings file.
     */
    public void loadSettings() {
        FileInputStream fis = null;
        String path = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SETTINGS_FILENAME);
            path = f.getPath();
            if (f.exists()) {
                fis = new FileInputStream(f);

                mSettings.mProperties.load(fis);

                // Properly reformat some settings to enforce their default value when missing.
                setShowUpdateOnly(mSettings.getShowUpdateOnly());
                setSetting(ISettingsPage.KEY_ASK_ADB_RESTART, mSettings.getAskBeforeAdbRestart());
                setSetting(ISettingsPage.KEY_USE_DOWNLOAD_CACHE, mSettings.getUseDownloadCache());
            }

        } catch (Exception e) {
            if (mSdkLog != null) {
                mSdkLog.error(e,
                        "Failed to load settings from .android folder. Path is '%1$s'.",
                        path);
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Saves settings to the settings file.
     */
    public void saveSettings() {

        FileOutputStream fos = null;
        String path = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SETTINGS_FILENAME);
            path = f.getPath();

            fos = new FileOutputStream(f);

            mSettings.mProperties.store(fos, "## Settings for Android Tool");  //$NON-NLS-1$

        } catch (Exception e) {
            if (mSdkLog != null) {
                // This is important enough that we want to really nag the user about it
                String reason = null;

                if (e instanceof FileNotFoundException) {
                    reason = "File not found";
                } else if (e instanceof AndroidLocationException) {
                    reason = ".android folder not found, please define ANDROID_SDK_HOME";
                } else if (e.getMessage() != null) {
                    reason = String.format("%1$s: %2$s",
                            e.getClass().getSimpleName(),
                            e.getMessage());
                } else {
                    reason = e.getClass().getName();
                }

                mSdkLog.error(e, "Failed to save settings file '%1$s': %2$s", path, reason);
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * When settings have changed: retrieve the new settings, apply them, save them
     * and notify on-settings-changed listeners.
     */
    private void onSettingsChanged() {
        if (mSettingsPage == null) {
            return;
        }

        Settings oldSettings = new Settings(mSettings);
        mSettingsPage.retrieveSettings(mSettings.mProperties);
        applySettings();
        saveSettings();

        for (OnChangedListener listener : mChangedListeners) {
            try {
                listener.onSettingsChanged(this, oldSettings);
            } catch (Throwable ignore) {}
        }
    }

    /**
     * Applies the current settings.
     */
    public void applySettings() {
        Properties props = System.getProperties();

        // Get the configured HTTP proxy settings
        String proxyHost = mSettings.mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_HOST,
                ""); //$NON-NLS-1$
        String proxyPort = mSettings.mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_PORT,
                ""); //$NON-NLS-1$

        // Set both the HTTP and HTTPS proxy system properties.
        // The system property constants can be found in the Java SE documentation at
        // http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
        final String JAVA_PROP_HTTP_PROXY_HOST =  "http.proxyHost";      //$NON-NLS-1$
        final String JAVA_PROP_HTTP_PROXY_PORT =  "http.proxyPort";      //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        // Only change the proxy if have something in the preferences.
        // Do not erase the default settings by empty values.
        if (proxyHost != null && proxyHost.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_HOST,  proxyHost);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
        }
        if (proxyPort != null && proxyPort.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_PORT,  proxyPort);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
        }
     }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index 350022e..7f44b7c 100755

//Synthetic comment -- @@ -1,275 +1,275 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.util.FormatUtils;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Properties;


public class SettingsDialog extends UpdaterBaseDialog implements ISettingsPage {


    // data members
    private final DownloadCache mDownloadCache = new DownloadCache(Strategy.SERVE_CACHE);
    private final SettingsController mSettingsController;
    private SettingsChangedCallback mSettingsChangedCallback;

    // UI widgets
    private Text mTextProxyServer;
    private Text mTextProxyPort;
    private Text mTextCacheSize;
    private Button mCheckUseCache;
    private Button mCheckForceHttp;
    private Button mCheckAskAdbRestart;
    private Button mCheckEnablePreviews;

    private SelectionAdapter mApplyOnSelected = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            applyNewSettings(); //$hide$
        }
    };

    private ModifyListener mApplyOnModified = new ModifyListener() {
        @Override
        public void modifyText(ModifyEvent e) {
            applyNewSettings(); //$hide$
        }
    };

    public SettingsDialog(Shell parentShell, UpdaterData updaterData) {
        super(parentShell, updaterData, "Settings" /*title*/);
        assert updaterData != null;
        mSettingsController = updaterData.getSettingsController();
    }

    @Override
    protected void createContents() {
        super.createContents();
        Shell shell = getShell();

        Group group = new Group(shell, SWT.NONE);
        group.setText("Proxy Settings");
        GridDataBuilder.create(group).fill().grab().hSpan(2);
        GridLayoutBuilder.create(group).columns(2);

        Label label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("HTTP Proxy Server");
        String tooltip = "The hostname or IP of the HTTP & HTTPS proxy server to use (e.g. proxy.example.com).\n" +
                         "When empty, the default Java proxy setting is used.";
        label.setToolTipText(tooltip);

        mTextProxyServer = new Text(group, SWT.BORDER);
        GridDataBuilder.create(mTextProxyServer).hFill().hGrab().vCenter();
        mTextProxyServer.addModifyListener(mApplyOnModified);
        mTextProxyServer.setToolTipText(tooltip);

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("HTTP Proxy Port");
        tooltip = "The port of the HTTP & HTTPS proxy server to use (e.g. 3128).\n" +
                  "When empty, the default Java proxy setting is used.";
        label.setToolTipText(tooltip);

        mTextProxyPort = new Text(group, SWT.BORDER);
        GridDataBuilder.create(mTextProxyPort).hFill().hGrab().vCenter();
        mTextProxyPort.addModifyListener(mApplyOnModified);
        mTextProxyPort.setToolTipText(tooltip);

        // ----
        group = new Group(shell, SWT.NONE);
        group.setText("Manifest Cache");
        GridDataBuilder.create(group).fill().grab().hSpan(2);
        GridLayoutBuilder.create(group).columns(3);

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("Directory:");

        Text text = new Text(group, SWT.NONE);
        GridDataBuilder.create(text).hFill().hGrab().vCenter().hSpan(2);
        text.setEnabled(false);
        text.setText(mDownloadCache.getCacheRoot().getAbsolutePath());

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("Current Size:");

        mTextCacheSize = new Text(group, SWT.NONE);
        GridDataBuilder.create(mTextCacheSize).hFill().hGrab().vCenter().hSpan(2);
        mTextCacheSize.setEnabled(false);
        updateDownloadCacheSize();

        mCheckUseCache = new Button(group, SWT.CHECK);
        GridDataBuilder.create(mCheckUseCache).vCenter().hSpan(1);
        mCheckUseCache.setText("Use download cache");
        mCheckUseCache.setToolTipText("When checked, small manifest files are cached locally.\n" +
                                      "Large binary files are never cached locally.");
        mCheckUseCache.addSelectionListener(mApplyOnSelected);

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hFill().hGrab().hSpan(1);

        Button button = new Button(group, SWT.PUSH);
        GridDataBuilder.create(button).vCenter().hSpan(1);
        button.setText("Clear Cache");
        button.setToolTipText("Deletes all cached files.");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                mDownloadCache.clearCache();
                updateDownloadCacheSize();
            }
        });

        // ----
        group = new Group(shell, SWT.NONE);
        group.setText("Others");
        GridDataBuilder.create(group).fill().grab().hSpan(2);
        GridLayoutBuilder.create(group).columns(2);

        mCheckForceHttp = new Button(group, SWT.CHECK);
        GridDataBuilder.create(mCheckForceHttp).hFill().hGrab().vCenter().hSpan(2);
        mCheckForceHttp.setText("Force https://... sources to be fetched using http://...");
        mCheckForceHttp.setToolTipText(
            "If you are not able to connect to the official Android repository using HTTPS,\n" +
            "enable this setting to force accessing it via HTTP.");
        mCheckForceHttp.addSelectionListener(mApplyOnSelected);

        mCheckAskAdbRestart = new Button(group, SWT.CHECK);
        GridDataBuilder.create(mCheckAskAdbRestart).hFill().hGrab().vCenter().hSpan(2);
        mCheckAskAdbRestart.setText("Ask before restarting ADB");
        mCheckAskAdbRestart.setToolTipText(
                "When checked, the user will be asked for permission to restart ADB\n" +
                "after updating an addon-on package or a tool package.");
        mCheckAskAdbRestart.addSelectionListener(mApplyOnSelected);

        mCheckEnablePreviews = new Button(group, SWT.CHECK);
        GridDataBuilder.create(mCheckEnablePreviews).hFill().hGrab().vCenter().hSpan(2);
        mCheckEnablePreviews.setText("Enable Preview Tools");
        mCheckEnablePreviews.setToolTipText(
            "When checked, the package list will also display preview versions of the tools.\n" +
            "These are optional future release candidates that the Android tools team\n" +
            "publishes from time to time for early feedback.");
        mCheckEnablePreviews.addSelectionListener(mApplyOnSelected);

        Label filler = new Label(shell, SWT.NONE);
        GridDataBuilder.create(filler).hFill().hGrab();

        createCloseButton();
    }

    @Override
    protected void postCreate() {
        super.postCreate();
        // This tells the controller to load the settings into the page UI.
        mSettingsController.setSettingsPage(this);
    }

    @Override
    protected void close() {
        // Dissociate this page from the controller
        mSettingsController.setSettingsPage(null);
        super.close();
    }


    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    /** Loads settings from the given {@link Properties} container and update the page UI. */
    @Override
    public void loadSettings(Properties inSettings) {
        mTextProxyServer.setText(inSettings.getProperty(KEY_HTTP_PROXY_HOST, ""));  //$NON-NLS-1$
        mTextProxyPort.setText(  inSettings.getProperty(KEY_HTTP_PROXY_PORT, ""));  //$NON-NLS-1$
        mCheckForceHttp.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_FORCE_HTTP)));
        mCheckAskAdbRestart.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_ASK_ADB_RESTART)));
        mCheckUseCache.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_USE_DOWNLOAD_CACHE)));
        mCheckEnablePreviews.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_ENABLE_PREVIEWS)));

    }

    /** Called by the application to retrieve settings from the UI and store them in
     * the given {@link Properties} container. */
    @Override
    public void retrieveSettings(Properties outSettings) {
        outSettings.setProperty(KEY_HTTP_PROXY_HOST, mTextProxyServer.getText());
        outSettings.setProperty(KEY_HTTP_PROXY_PORT, mTextProxyPort.getText());
        outSettings.setProperty(KEY_FORCE_HTTP,
                Boolean.toString(mCheckForceHttp.getSelection()));
        outSettings.setProperty(KEY_ASK_ADB_RESTART,
                Boolean.toString(mCheckAskAdbRestart.getSelection()));
        outSettings.setProperty(KEY_USE_DOWNLOAD_CACHE,
                Boolean.toString(mCheckUseCache.getSelection()));
        outSettings.setProperty(KEY_ENABLE_PREVIEWS,
                Boolean.toString(mCheckEnablePreviews.getSelection()));

    }

    /**
     * Called by the application to give a callback that the page should invoke when
     * settings must be applied. The page does not apply the settings itself, instead
     * it notifies the application.
     */
    @Override
    public void setOnSettingsChanged(SettingsChangedCallback settingsChangedCallback) {
        mSettingsChangedCallback = settingsChangedCallback;
    }

    /**
     * Callback invoked when user touches one of the settings.
     * There is no "Apply" button, settings are applied immediately as they are changed.
     * Notify the application that settings have changed.
     */
    private void applyNewSettings() {
        if (mSettingsChangedCallback != null) {
            mSettingsChangedCallback.onSettingsChanged(this);
        }
    }

    private void updateDownloadCacheSize() {
        long size = mDownloadCache.getCurrentSize();
        String str = FormatUtils.byteSizeToString(size);
        mTextCacheSize.setText(str);
    }


    // End of hiding from SWT Designer
    //$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java
//Synthetic comment -- index aaa190c..c30f2fd 100755

//Synthetic comment -- @@ -1,159 +1,159 @@
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

package com.android.sdkuilib.internal.repository.icons;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdkuilib.internal.repository.sdkman2.PkgContentProvider;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


/**
 * An utility class to serve {@link Image} correspond to the various icons
 * present in this package and dispose of them correctly at the end.
 */
public class ImageFactory {

    private final Display mDisplay;
    private final Map<String, Image> mImages = new HashMap<String, Image>();

    public ImageFactory(Display display) {
        mDisplay = display;
    }

    /**
     * Loads an image given its filename (with its extension).
     * Might return null if the image cannot be loaded.
     * The image is cached. Successive calls will return the <em>same</em> object.
     *
     * @param imageName The filename (with extension) of the image to load.
     * @return A new or existing {@link Image}. The caller must NOT dispose the image (the
     *  image will disposed by {@link #dispose()}). The returned image can be null if the
     *  expected file is missing.
     */
    public Image getImageByName(String imageName) {

        Image image = mImages.get(imageName);
        if (image != null) {
            return image;
        }

        InputStream stream = getClass().getResourceAsStream(imageName);
        if (stream != null) {
            try {
                image = new Image(mDisplay, stream);
            } catch (SWTException e) {
                // ignore
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }

        // Store the image in the hash, even if this failed. If it fails now, it will fail later.
        mImages.put(imageName, image);

        return image;
    }

    /**
     * Loads and returns the appropriate image for a given package, archive or source object.
     * The image is cached. Successive calls will return the <em>same</em> object.
     *
     * @param object A {@link SdkSource} or {@link Package} or {@link Archive}.
     * @return A new or existing {@link Image}. The caller must NOT dispose the image (the
     *  image will disposed by {@link #dispose()}). The returned image can be null if the
     *  object is of an unknown type.
     */
    public Image getImageForObject(Object object) {

        if (object == null) {
            return null;
        }

        if (object instanceof Image) {
            return (Image) object;
        }

        String clz = object.getClass().getSimpleName();
        if (clz.endsWith(Package.class.getSimpleName())) {
            String name = clz.replaceFirst(Package.class.getSimpleName(), "")   //$NON-NLS-1$
                             .replace("SystemImage", "sysimg")    //$NON-NLS-1$ //$NON-NLS-2$
                             .toLowerCase(Locale.US);
            name += "_pkg_16.png";                                              //$NON-NLS-1$
            return getImageByName(name);
        }

        if (object instanceof SdkSourceCategory) {
            return getImageByName("source_cat_icon_16.png");                     //$NON-NLS-1$

        } else if (object instanceof SdkSource) {
            return getImageByName("source_icon_16.png");                         //$NON-NLS-1$

        } else if (object instanceof PkgContentProvider.RepoSourceError) {
            return getImageByName("error_icon_16.png");                       //$NON-NLS-1$

        } else if (object instanceof PkgContentProvider.RepoSourceNotification) {
            return getImageByName("nopkg_icon_16.png");                       //$NON-NLS-1$
        }

        if (object instanceof Archive) {
            if (((Archive) object).isCompatible()) {
                return getImageByName("archive_icon16.png");                    //$NON-NLS-1$
            } else {
                return getImageByName("incompat_icon16.png");                   //$NON-NLS-1$
            }
        }

        if (object instanceof String) {
            return getImageByName((String) object);
        }


        if (object != null) {
            // For debugging
            // System.out.println("No image for object " + object.getClass().getSimpleName());
        }

        return null;
    }

    /**
     * Dispose all the images created by this factory so far.
     */
    public void dispose() {
        Iterator<Image> it = mImages.values().iterator();
        while(it.hasNext()) {
            Image img = it.next();
            if (img != null && img.isDisposed() == false) {
                img.dispose();
            }
            it.remove();
        }
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman1/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman1/AvdManagerPage.java
//Synthetic comment -- index 84a549b..63c53e0 100755

//Synthetic comment -- @@ -1,125 +1,125 @@
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

package com.android.sdkuilib.internal.repository.sdkman1;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AvdManagerPage extends UpdaterPage implements ISdkChangeListener {

    private AvdSelector mAvdSelector;

    private final UpdaterData mUpdaterData;

    /**
     * Create the composite.
     * @param parent The parent of the composite.
     * @param updaterData An instance of {@link UpdaterData}.
     */
    public AvdManagerPage(Composite parent, int swtStyle, UpdaterData updaterData) {
        super(parent, swtStyle);

        mUpdaterData = updaterData;
        mUpdaterData.addListeners(this);

        createContents(this);
        postCreate();  //$hide$
    }

    private void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        Label label = new Label(parent, SWT.NONE);
        label.setLayoutData(new GridData());

        try {
            if (mUpdaterData != null && mUpdaterData.getAvdManager() != null) {
                label.setText(String.format(
                        "List of existing Android Virtual Devices located at %s",
                        mUpdaterData.getAvdManager().getBaseAvdFolder()));
            } else {
                label.setText("Error: cannot find the AVD folder location.\r\n Please set the 'ANDROID_SDK_HOME' env variable.");
            }
        } catch (AndroidLocationException e) {
            label.setText(e.getMessage());
        }

        mAvdSelector = new AvdSelector(parent,
                mUpdaterData.getOsSdkRoot(),
                mUpdaterData.getAvdManager(),
                DisplayMode.MANAGER,
                mUpdaterData.getSdkLog());
        mAvdSelector.setSettingsController(mUpdaterData.getSettingsController());
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
        // nothing to be done for now.
    }

    // --- Implementation of ISdkChangeListener ---

    @Override
    public void onSdkLoaded() {
        onSdkReload();
    }

    @Override
    public void onSdkReload() {
        mAvdSelector.refresh(false /*reload*/);
    }

    @Override
    public void preInstallHook() {
        // nothing to be done for now.
    }

    @Override
    public void postInstallHook() {
        // nothing to be done for now.
    }

    // End of hiding from SWT Designer
    //$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AdtUpdateDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AdtUpdateDialog.java
//Synthetic comment -- index 8e66c63..95c16f1 100755

//Synthetic comment -- @@ -1,492 +1,492 @@
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

package com.android.sdkuilib.internal.repository.sdkman2;


import com.android.sdklib.AndroidVersion;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.sdkman2.PackageLoader.IAutoInstallTask;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This is a private implementation of UpdateWindow for ADT,
 * designed to install a very specific package.
 * <p/>
 * Example of usage:
 * <pre>
 * AdtUpdateDialog dialog = new AdtUpdateDialog(
 *     AdtPlugin.getDisplay().getActiveShell(),
 *     new AdtConsoleSdkLog(),
 *     sdk.getSdkLocation());
 *
 * Pair<Boolean, File> result = dialog.installExtraPackage(
 *     "android", "compatibility");  //$NON-NLS-1$ //$NON-NLS-2$
 * or
 * Pair<Boolean, File> result = dialog.installPlatformPackage(11);
 * </pre>
 */
public class AdtUpdateDialog extends SwtBaseDialog {

    public static final int USE_MAX_REMOTE_API_LEVEL = 0;

    private static final String APP_NAME = "Android SDK Manager";
    private final UpdaterData mUpdaterData;

    private Boolean mResultCode = Boolean.FALSE;
    private Map<Package, File> mResultPaths = null;
    private SettingsController mSettingsController;
    private PackageFilter mPackageFilter;
    private PackageLoader mPackageLoader;

    private ProgressBar mProgressBar;
    private Label mStatusText;

    /**
     * Creates a new {@link AdtUpdateDialog}.
     * Callers will want to call {@link #installExtraPackage} or
     * {@link #installPlatformPackage} after this.
     *
     * @param parentShell The existing parent shell. Must not be null.
     * @param sdkLog An SDK logger. Must not be null.
     * @param osSdkRoot The current SDK root OS path. Must not be null or empty.
     */
    public AdtUpdateDialog(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot) {
        super(parentShell, SWT.NONE, APP_NAME);
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
    }

    /**
     * Displays the update dialog and triggers installation of the requested {@code extra}
     * package with the specified vendor and path attributes.
     * <p/>
     * Callers must not try to reuse this dialog after this call.
     *
     * @param vendor The extra package vendor string to match.
     * @param path   The extra package path   string to match.
     * @return A boolean indicating whether the installation was successful (meaning the package
     *   was either already present, or got installed or updated properly) and a {@link File}
     *   with the path to the root folder of the package. The file is null when the boolean
     *   is false, otherwise it should point to an existing valid folder.
     * @wbp.parser.entryPoint
     */
    public Pair<Boolean, File> installExtraPackage(String vendor, String path) {
        mPackageFilter = createExtraFilter(vendor, path);
        open();

        File installPath = null;
        if (mResultPaths != null) {
            for (Entry<Package, File> entry : mResultPaths.entrySet()) {
                if (entry.getKey() instanceof ExtraPackage) {
                    installPath = entry.getValue();
                    break;
                }
            }
        }

        return Pair.of(mResultCode, installPath);
    }

    /**
     * Displays the update dialog and triggers installation of platform-tools package.
     * <p/>
     * Callers must not try to reuse this dialog after this call.
     *
     * @return A boolean indicating whether the installation was successful (meaning the package
     *   was either already present, or got installed or updated properly) and a {@link File}
     *   with the path to the root folder of the package. The file is null when the boolean
     *   is false, otherwise it should point to an existing valid folder.
     * @wbp.parser.entryPoint
     */
    public Pair<Boolean, File> installPlatformTools() {
        mPackageFilter = createPlatformToolsFilter();
        open();

        File installPath = null;
        if (mResultPaths != null) {
            for (Entry<Package, File> entry : mResultPaths.entrySet()) {
                if (entry.getKey() instanceof ExtraPackage) {
                    installPath = entry.getValue();
                    break;
                }
            }
        }

        return Pair.of(mResultCode, installPath);
    }

    /**
     * Displays the update dialog and triggers installation of the requested platform
     * package with the specified API  level.
     * <p/>
     * Callers must not try to reuse this dialog after this call.
     *
     * @param apiLevel The platform API level to match.
     *  The special value {@link #USE_MAX_REMOTE_API_LEVEL} means to use
     *  the highest API level available on the remote repository.
     * @return A boolean indicating whether the installation was successful (meaning the package
     *   was either already present, or got installed or updated properly) and a {@link File}
     *   with the path to the root folder of the package. The file is null when the boolean
     *   is false, otherwise it should point to an existing valid folder.
     */
    public Pair<Boolean, File> installPlatformPackage(int apiLevel) {
        mPackageFilter = createPlatformFilter(apiLevel);
        open();

        File installPath = null;
        if (mResultPaths != null) {
            for (Entry<Package, File> entry : mResultPaths.entrySet()) {
                if (entry.getKey() instanceof PlatformPackage) {
                    installPath = entry.getValue();
                    break;
                }
            }
        }

        return Pair.of(mResultCode, installPath);
    }

    /**
     * Displays the update dialog and triggers installation of a new SDK. This works by
     * requesting a remote platform package with the specified API levels as well as
     * the first tools or platform-tools packages available.
     * <p/>
     * Callers must not try to reuse this dialog after this call.
     *
     * @param apiLevels A set of platform API levels to match.
     *  The special value {@link #USE_MAX_REMOTE_API_LEVEL} means to use
     *  the highest API level available in the repository.
     * @return A boolean indicating whether the installation was successful (meaning the packages
     *   were either already present, or got installed or updated properly).
     */
    public boolean installNewSdk(Set<Integer> apiLevels) {
        mPackageFilter = createNewSdkFilter(apiLevels);
        open();
        return mResultCode.booleanValue();
    }

    @Override
    protected void createContents() {
        Shell shell = getShell();
        shell.setMinimumSize(new Point(450, 100));
        shell.setSize(450, 100);

        mUpdaterData.setWindowShell(shell);

        GridLayoutBuilder.create(shell).columns(1);

        Composite composite1 = new Composite(shell, SWT.NONE);
        composite1.setLayout(new GridLayout(1, false));
        GridDataBuilder.create(composite1).fill().grab();

        mProgressBar = new ProgressBar(composite1, SWT.NONE);
        GridDataBuilder.create(mProgressBar).hFill().hGrab();

        mStatusText = new Label(composite1, SWT.NONE);
        mStatusText.setText("Status Placeholder");  //$NON-NLS-1$ placeholder
        GridDataBuilder.create(mStatusText).hFill().hGrab();
    }

    @Override
    protected void postCreate() {
        ProgressViewFactory factory = new ProgressViewFactory();
        factory.setProgressView(new ProgressView(
                mStatusText,
                mProgressBar,
                null /*buttonStop*/,
                new SdkLogAdapter(mUpdaterData.getSdkLog())));
        mUpdaterData.setTaskFactory(factory);

        setupSources();
        initializeSettings();

        if (mUpdaterData.checkIfInitFailed()) {
            close();
            return;
        }

        mUpdaterData.broadcastOnSdkLoaded();

        mPackageLoader = new PackageLoader(mUpdaterData);
    }

    @Override
    protected void eventLoop() {
        mPackageLoader.loadPackagesWithInstallTask(
                mPackageFilter.installFlags(),
                new IAutoInstallTask() {
            @Override
            public Package[] filterLoadedSource(SdkSource source, Package[] packages) {
                for (Package pkg : packages) {
                    mPackageFilter.visit(pkg);
                }
                return packages;
            }

            @Override
            public boolean acceptPackage(Package pkg) {
                // Is this the package we want to install?
                return mPackageFilter.accept(pkg);
            }

            @Override
            public void setResult(boolean success, Map<Package, File> installPaths) {
                // Capture the result from the installation.
                mResultCode = Boolean.valueOf(success);
                mResultPaths = installPaths;
            }

            @Override
            public void taskCompleted() {
                // We can close that window now.
                close();
            }
        });

        super.eventLoop();
    }

    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    // --- Public API -----------


    // --- Internals & UI Callbacks -----------

    /**
     * Used to initialize the sources.
     */
    private void setupSources() {
        mUpdaterData.setupDefaultSources();
    }

    /**
     * Initializes settings.
     */
    private void initializeSettings() {
        mSettingsController = mUpdaterData.getSettingsController();
        mSettingsController.loadSettings();
        mSettingsController.applySettings();
    }

    // ----

    private static abstract class PackageFilter {
        /** Returns the installer flags for the corresponding mode. */
        abstract int installFlags();

        /** Visit a new package definition, in case we need to adjust the filter dynamically. */
        abstract void visit(Package pkg);

        /** Checks whether this is the package we've been looking for. */
        abstract boolean accept(Package pkg);
    }

    public static PackageFilter createExtraFilter(
            final String vendor,
            final String path) {
        return new PackageFilter() {
            String mVendor = vendor;
            String mPath = path;

            @Override
            boolean accept(Package pkg) {
                if (pkg instanceof ExtraPackage) {
                    ExtraPackage ep = (ExtraPackage) pkg;
                    if (ep.getVendorId().equals(mVendor)) {
                        // Check actual extra <path> field first
                        if (ep.getPath().equals(mPath)) {
                            return true;
                        }
                        // If not, check whether this is one of the <old-paths> values.
                        for (String oldPath : ep.getOldPaths()) {
                            if (oldPath.equals(mPath)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            @Override
            void visit(Package pkg) {
                // nop
            }

            @Override
            int installFlags() {
                return UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT;
            }
        };
    }

    private PackageFilter createPlatformToolsFilter() {
        return new PackageFilter() {
            @Override
            boolean accept(Package pkg) {
                return pkg instanceof PlatformToolPackage;
            }

            @Override
            void visit(Package pkg) {
                // nop
            }

            @Override
            int installFlags() {
                return UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT;
            }
        };
    }

    public static PackageFilter createPlatformFilter(final int apiLevel) {
        return new PackageFilter() {
            int mApiLevel = apiLevel;
            boolean mFindMaxApi = apiLevel == USE_MAX_REMOTE_API_LEVEL;

            @Override
            boolean accept(Package pkg) {
                if (pkg instanceof PlatformPackage) {
                    PlatformPackage pp = (PlatformPackage) pkg;
                    AndroidVersion v = pp.getAndroidVersion();
                    return !v.isPreview() && v.getApiLevel() == mApiLevel;
                }
                return false;
            }

            @Override
            void visit(Package pkg) {
                // Try to find the max API in all remote packages
                if (mFindMaxApi &&
                        pkg instanceof PlatformPackage &&
                        !pkg.isLocal()) {
                    PlatformPackage pp = (PlatformPackage) pkg;
                    AndroidVersion v = pp.getAndroidVersion();
                    if (!v.isPreview()) {
                        int api = v.getApiLevel();
                        if (api > mApiLevel) {
                            mApiLevel = api;
                        }
                    }
                }
            }

            @Override
            int installFlags() {
                return UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT;
            }
        };
    }

    public static PackageFilter createNewSdkFilter(final Set<Integer> apiLevels) {
        return new PackageFilter() {
            int mMaxApiLevel;
            boolean mFindMaxApi = apiLevels.contains(USE_MAX_REMOTE_API_LEVEL);
            boolean mNeedTools = true;
            boolean mNeedPlatformTools = true;

            @Override
            boolean accept(Package pkg) {
                if (!pkg.isLocal()) {
                    if (pkg instanceof PlatformPackage) {
                        PlatformPackage pp = (PlatformPackage) pkg;
                        AndroidVersion v = pp.getAndroidVersion();
                        if (!v.isPreview()) {
                            int level = v.getApiLevel();
                            if ((mFindMaxApi && level == mMaxApiLevel) ||
                                    (level > 0 && apiLevels.contains(level))) {
                                return true;
                            }
                        }
                    } else if (mNeedTools && pkg instanceof ToolPackage) {
                        // We want a tool package. There should be only one,
                        // but in case of error just take the first one.
                        mNeedTools = false;
                        return true;
                    } else if (mNeedPlatformTools && pkg instanceof PlatformToolPackage) {
                        // We want a platform-tool package. There should be only one,
                        // but in case of error just take the first one.
                        mNeedPlatformTools = false;
                        return true;
                    }
                }
                return false;
            }

            @Override
            void visit(Package pkg) {
                // Try to find the max API in all remote packages
                if (mFindMaxApi &&
                        pkg instanceof PlatformPackage &&
                        !pkg.isLocal()) {
                    PlatformPackage pp = (PlatformPackage) pkg;
                    AndroidVersion v = pp.getAndroidVersion();
                    if (!v.isPreview()) {
                        int api = v.getApiLevel();
                        if (api > mMaxApiLevel) {
                            mMaxApiLevel = api;
                        }
                    }
                }
            }

            @Override
            int installFlags() {
                return UpdaterData.NO_TOOLS_MSG;
            }
        };
    }



    // End of hiding from SWT Designer
    //$hide<<$

    // -----

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/LogWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/LogWindow.java
//Synthetic comment -- index 699edda..4cdd198 100755

//Synthetic comment -- @@ -1,379 +1,379 @@
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

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.tasks.ILogUiProvider;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;


/**
 * A floating log window that can be displayed or hidden by the main SDK Manager 2 window.
 * It displays a log of the sdk manager operation (listing, install, delete) including
 * any errors (e.g. network error or install/delete errors.)
 * <p/>
 * Since the SDK Manager will direct all log to this window, its purpose is to be
 * opened by the main window at startup and left open all the time. When not needed
 * the floating window is hidden but not closed. This way it can easily accumulate
 * all the log.
 */
class LogWindow implements ILogUiProvider {

    private Shell mParentShell;
    private Shell mShell;
    private Composite mRootComposite;
    private StyledText mStyledText;
    private Label mLogDescription;
    private Button mCloseButton;

    private final ISdkLog mSecondaryLog;
    private boolean mCloseRequested;
    private boolean mInitPosition = true;
    private String mLastLogMsg = null;

    private enum TextStyle {
        DEFAULT,
        TITLE,
        ERROR
    }

    /**
     * Creates the floating window. Callers should use {@link #open()} later.
     *
     * @param parentShell Parent container
     * @param secondaryLog An optional logger where messages will <em>also</em> be output.
     */
    public LogWindow(Shell parentShell, ISdkLog secondaryLog) {
        mParentShell = parentShell;
        mSecondaryLog = secondaryLog;
    }

    /**
     * For testing only. See {@link #open()} and {@link #close()} for normal usage.
     * @wbp.parser.entryPoint
     */
    void openBlocking() {
        open();
        Display display = Display.getDefault();
        while (!mShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        close();
    }

    /**
     * Opens the window.
     * This call does not block and relies on the fact that the main window is
     * already running an SWT event dispatch loop.
     * Caller should use {@link #close()} later.
     */
    public void open() {
        createShell();
        createContents();
        mShell.open();
        mShell.layout();
        mShell.setVisible(false);
    }

    /**
     * Closes and <em>destroys</em> the window.
     * This must be called just before quitting the app.
     * <p/>
     * To simply hide/show the window, use {@link #setVisible(boolean)} instead.
     */
    public void close() {
        if (mShell != null && !mShell.isDisposed()) {
            mCloseRequested = true;
            mShell.close();
            mShell = null;
        }
    }

    /**
     * Determines whether the window is currently shown or not.
     *
     * @return True if the window is shown.
     */
    public boolean isVisible() {
        return mShell != null && !mShell.isDisposed() && mShell.isVisible();
    }

    /**
     * Toggles the window visibility.
     *
     * @param visible True to make the window visible, false to hide it.
     */
    public void setVisible(boolean visible) {
        if (mShell != null && !mShell.isDisposed()) {
            mShell.setVisible(visible);
            if (visible && mInitPosition) {
                mInitPosition = false;
                positionWindow();
            }
        }
    }

    private void createShell() {
        mShell = new Shell(mParentShell, SWT.SHELL_TRIM | SWT.TOOL);
        mShell.setMinimumSize(new Point(600, 300));
        mShell.setSize(450, 300);
        mShell.setText("Android SDK Manager Log");
        GridLayoutBuilder.create(mShell);

        mShell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                if (!mCloseRequested) {
                    e.doit = false;
                    setVisible(false);
                }
            }
        });
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        mRootComposite = new Composite(mShell, SWT.NONE);
        GridLayoutBuilder.create(mRootComposite).columns(2);
        GridDataBuilder.create(mRootComposite).fill().grab();

        mStyledText = new StyledText(mRootComposite,
                SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        GridDataBuilder.create(mStyledText).hSpan(2).fill().grab();

        mLogDescription = new Label(mRootComposite, SWT.NONE);
        GridDataBuilder.create(mLogDescription).hFill().hGrab();

        mCloseButton = new Button(mRootComposite, SWT.NONE);
        mCloseButton.setText("Close");
        mCloseButton.setToolTipText("Closes the log window");
        mCloseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setVisible(false);  //$hide$
            }
        });
    }

    // --- Implementation of ILogUiProvider ---


    /**
     * Sets the description in the current task dialog.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void setDescription(final String description) {
        syncExec(mLogDescription, new Runnable() {
            @Override
            public void run() {
                mLogDescription.setText(description);

                if (acceptLog(description, true /*isDescription*/)) {
                    appendLine(TextStyle.TITLE, description);

                    if (mSecondaryLog != null) {
                        mSecondaryLog.printf("%1$s", description);  //$NON-NLS-1$
                    }
                }
            }
        });
    }

    /**
     * Logs a "normal" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void log(final String log) {
        if (acceptLog(log, false /*isDescription*/)) {
            syncExec(mLogDescription, new Runnable() {
                @Override
                public void run() {
                    appendLine(TextStyle.DEFAULT, log);
                }
            });

            if (mSecondaryLog != null) {
                mSecondaryLog.printf("  %1$s", log);                //$NON-NLS-1$
            }
        }
    }

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logError(final String log) {
        if (acceptLog(log, false /*isDescription*/)) {
            syncExec(mLogDescription, new Runnable() {
                @Override
                public void run() {
                    appendLine(TextStyle.ERROR, log);
                }
            });

            if (mSecondaryLog != null) {
                mSecondaryLog.error(null, "%1$s", log);             //$NON-NLS-1$
            }
        }
    }

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logVerbose(final String log) {
        if (acceptLog(log, false /*isDescription*/)) {
            syncExec(mLogDescription, new Runnable() {
                @Override
                public void run() {
                    appendLine(TextStyle.DEFAULT, "  " + log);      //$NON-NLS-1$
                }
            });

            if (mSecondaryLog != null) {
                mSecondaryLog.printf("    %1$s", log);              //$NON-NLS-1$
            }
        }
    }


    // ----


    /**
     * Centers the dialog in its parent shell.
     */
    private void positionWindow() {
        // Centers the dialog in its parent shell
        Shell child = mShell;
        if (child != null && mParentShell != null) {
            // get the parent client area with a location relative to the display
            Rectangle parentArea = mParentShell.getClientArea();
            Point parentLoc = mParentShell.getLocation();
            int px = parentLoc.x;
            int py = parentLoc.y;
            int pw = parentArea.width;
            int ph = parentArea.height;

            Point childSize = child.getSize();
            int cw = Math.max(childSize.x, pw);
            int ch = childSize.y;

            int x = 30 + px + (pw - cw) / 2;
            if (x < 0) x = 0;

            int y = py + (ph - ch) / 2;
            if (y < py) y = py;

            child.setLocation(x, y);
            child.setSize(cw, ch);
        }
    }

    private void appendLine(TextStyle style, String text) {
        if (!text.endsWith("\n")) {                                 //$NON-NLS-1$
            text += '\n';
        }

        int start = mStyledText.getCharCount();

        if (style == TextStyle.DEFAULT) {
            mStyledText.append(text);

        } else {
            mStyledText.append(text);

            StyleRange sr = new StyleRange();
            sr.start = start;
            sr.length = text.length();
            sr.fontStyle = SWT.BOLD;
            if (style == TextStyle.ERROR) {
                sr.foreground = mStyledText.getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
            }
            sr.underline = false;
            mStyledText.setStyleRange(sr);
        }

        // Scroll caret if it was already at the end before we added new text.
        // Ideally we would scroll if the scrollbar is at the bottom but we don't
        // have direct access to the scrollbar without overriding the SWT impl.
        if (mStyledText.getCaretOffset() >= start) {
            mStyledText.setSelection(mStyledText.getCharCount());
        }
    }


    private void syncExec(final Widget widget, final Runnable runnable) {
        if (widget != null && !widget.isDisposed()) {
            widget.getDisplay().syncExec(runnable);
        }
    }

    /**
     * Filter messages displayed in the log: <br/>
     * - Messages with a % are typical part of a progress update and shouldn't be in the log. <br/>
     * - Messages that are the same as the same output message should be output a second time.
     *
     * @param msg The potential log line to print.
     * @return True if the log line should be printed, false otherwise.
     */
    private boolean acceptLog(String msg, boolean isDescription) {
        if (msg == null) {
            return false;
        }

        msg = msg.trim();

        // Descriptions also have the download progress status (0..100%) which we want to avoid
        if (isDescription && msg.indexOf('%') != -1) {
            return false;
        }

        if (msg.equals(mLastLogMsg)) {
            return false;
        }

        mLastLogMsg = msg;
        return true;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkLogAdapter.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkLogAdapter.java
//Synthetic comment -- index 969d930..aa4bc6c 100755

//Synthetic comment -- @@ -1,112 +1,112 @@
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

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.tasks.ILogUiProvider;


/**
 * Adapter that transform log from an {@link ILogUiProvider} to an {@link ISdkLog}.
 */
public final class SdkLogAdapter implements ILogUiProvider {

    private ISdkLog mSdkLog;
    private String mLastLogMsg;

    /**
     * Creates a new adapter to output log on the given {@code sdkLog}.
     *
     * @param sdkLog The logger to output to. Must not be null.
     */
    public SdkLogAdapter(ISdkLog sdkLog) {
        mSdkLog = sdkLog;
    }

    /**
     * Sets the description in the current task dialog.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void setDescription(final String description) {
        if (acceptLog(description)) {
            mSdkLog.printf("%1$s", description);    //$NON-NLS-1$
        }
    }

    /**
     * Logs a "normal" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void log(String log) {
        if (acceptLog(log)) {
            mSdkLog.printf("  %1$s", log);          //$NON-NLS-1$
        }
    }

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logError(String log) {
        if (acceptLog(log)) {
            mSdkLog.error(null, "  %1$s", log);     //$NON-NLS-1$
        }
    }

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logVerbose(String log) {
        if (acceptLog(log)) {
            mSdkLog.printf("    %1$s", log);        //$NON-NLS-1$
        }
    }

    // ----

    /**
     * Filter messages displayed in the log: <br/>
     * - Messages with a % are typical part of a progress update and shouldn't be in the log. <br/>
     * - Messages that are the same as the same output message should be output a second time.
     *
     * @param msg The potential log line to print.
     * @return True if the log line should be printed, false otherwise.
     */
    private boolean acceptLog(String msg) {
        if (msg == null) {
            return false;
        }

        msg = msg.trim();
        if (msg.indexOf('%') != -1) {
            return false;
        }

        if (msg.equals(mLastLogMsg)) {
            return false;
        }

        mLastLogMsg = msg;
        return true;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index 4376ec8..3542c5f 100755

//Synthetic comment -- @@ -1,590 +1,590 @@
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

package com.android.sdkuilib.internal.repository.sdkman2;


import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.sources.SdkSourceProperties;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsController.Settings;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman2.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.tasks.ILogUiProvider;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.internal.widgets.ImgDisabledButton;
import com.android.sdkuilib.internal.widgets.ToggleButton;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * This is the private implementation of the UpdateWindow
 * for the second version of the SDK Manager.
 * <p/>
 * This window features only one embedded page, the combined installed+available package list.
 */
public class SdkUpdaterWindowImpl2 implements ISdkUpdaterWindow {

    public static final String APP_NAME = "Android SDK Manager";
    private static final String SIZE_POS_PREFIX = "sdkman2"; //$NON-NLS-1$

    private final Shell mParentShell;
    private final SdkInvocationContext mContext;
    /** Internal data shared between the window and its pages. */
    private final UpdaterData mUpdaterData;

    // --- UI members ---

    protected Shell mShell;
    private PackagesPage mPkgPage;
    private ProgressBar mProgressBar;
    private Label mStatusText;
    private ImgDisabledButton mButtonStop;
    private ToggleButton mButtonShowLog;
    private SettingsController mSettingsController;
    private LogWindow mLogWindow;

    /**
     * Creates a new window. Caller must call open(), which will block.
     *
     * @param parentShell Parent shell.
     * @param sdkLog Logger. Cannot be null.
     * @param osSdkRoot The OS path to the SDK root.
     * @param context The {@link SdkInvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
     */
    public SdkUpdaterWindowImpl2(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot,
            SdkInvocationContext context) {
        mParentShell = parentShell;
        mContext = context;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
    }

    /**
     * Creates a new window. Caller must call open(), which will block.
     * <p/>
     * This is to be used when the window is opened from {@link AvdManagerWindowImpl1}
     * to share the same {@link UpdaterData} structure.
     *
     * @param parentShell Parent shell.
     * @param updaterData The parent's updater data.
     * @param context The {@link SdkInvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
     */
    public SdkUpdaterWindowImpl2(
            Shell parentShell,
            UpdaterData updaterData,
            SdkInvocationContext context) {
        mParentShell = parentShell;
        mContext = context;
        mUpdaterData = updaterData;
    }

    /**
     * Opens the window.
     * @wbp.parser.entryPoint
     */
    @Override
    public void open() {
        if (mParentShell == null) {
            Display.setAppName(APP_NAME); //$hide$ (hide from SWT designer)
        }

        createShell();
        preCreateContent();
        createContents();
        createMenuBar();
        createLogWindow();
        mShell.open();
        mShell.layout();

        if (postCreateContent()) {    //$hide$ (hide from SWT designer)
            Display display = Display.getDefault();
            while (!mShell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        }

        SdkSourceProperties p = new SdkSourceProperties();
        p.save();

        dispose();  //$hide$
    }

    private void createShell() {
        // The SDK Manager must use a shell trim when standalone
        // or a dialog trim when invoked from somewhere else.
        int style = SWT.SHELL_TRIM;
        if (mContext != SdkInvocationContext.STANDALONE) {
            style |= SWT.APPLICATION_MODAL;
        }

        mShell = new Shell(mParentShell, style);
        mShell.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                ShellSizeAndPos.saveSizeAndPos(mShell, SIZE_POS_PREFIX);
                onAndroidSdkUpdaterDispose();    //$hide$ (hide from SWT designer)
            }
        });

        GridLayout glShell = new GridLayout(2, false);
        glShell.verticalSpacing = 0;
        glShell.horizontalSpacing = 0;
        glShell.marginWidth = 0;
        glShell.marginHeight = 0;
        mShell.setLayout(glShell);

        mShell.setMinimumSize(new Point(500, 300));
        mShell.setSize(700, 500);
        mShell.setText(APP_NAME);

        ShellSizeAndPos.loadSizeAndPos(mShell, SIZE_POS_PREFIX);
    }

    private void createContents() {
        mPkgPage = new PackagesPage(mShell, SWT.NONE, mUpdaterData, mContext);
        mPkgPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        Composite composite1 = new Composite(mShell, SWT.NONE);
        composite1.setLayout(new GridLayout(1, false));
        composite1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        mProgressBar = new ProgressBar(composite1, SWT.NONE);
        mProgressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        mStatusText = new Label(composite1, SWT.NONE);
        mStatusText.setText("Status Placeholder");  //$NON-NLS-1$ placeholder
        mStatusText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Composite composite2 = new Composite(mShell, SWT.NONE);
        composite2.setLayout(new GridLayout(2, false));

        mButtonStop = new ImgDisabledButton(composite2, SWT.NONE,
                getImage("stop_enabled_16.png"),    //$NON-NLS-1$
                getImage("stop_disabled_16.png"),   //$NON-NLS-1$
                "Click to abort the current task",
                "");                                //$NON-NLS-1$ nothing to abort
        mButtonStop.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                onStopSelected();
            }
        });

        mButtonShowLog = new ToggleButton(composite2, SWT.NONE,
                getImage("log_off_16.png"),         //$NON-NLS-1$
                getImage("log_on_16.png"),          //$NON-NLS-1$
                "Click to show the log window",     // tooltip for state hidden=>shown
                "Click to hide the log window");    // tooltip for state shown=>hidden
        mButtonShowLog.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                onToggleLogWindow();
            }
        });
    }

    @SuppressWarnings("unused") // MenuItem works using side effects
    private void createMenuBar() {

        Menu menuBar = new Menu(mShell, SWT.BAR);
        mShell.setMenuBar(menuBar);

        MenuItem menuBarPackages = new MenuItem(menuBar, SWT.CASCADE);
        menuBarPackages.setText("Packages");

        Menu menuPkgs = new Menu(menuBarPackages);
        menuBarPackages.setMenu(menuPkgs);

        MenuItem showUpdatesNew = new MenuItem(menuPkgs,
                MenuAction.TOGGLE_SHOW_UPDATE_NEW_PKG.getMenuStyle());
        showUpdatesNew.setText(
                MenuAction.TOGGLE_SHOW_UPDATE_NEW_PKG.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.TOGGLE_SHOW_UPDATE_NEW_PKG, showUpdatesNew);

        MenuItem showInstalled = new MenuItem(menuPkgs,
                MenuAction.TOGGLE_SHOW_INSTALLED_PKG.getMenuStyle());
        showInstalled.setText(
                MenuAction.TOGGLE_SHOW_INSTALLED_PKG.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.TOGGLE_SHOW_INSTALLED_PKG, showInstalled);

        MenuItem showObsoletePackages = new MenuItem(menuPkgs,
                MenuAction.TOGGLE_SHOW_OBSOLETE_PKG.getMenuStyle());
        showObsoletePackages.setText(
                MenuAction.TOGGLE_SHOW_OBSOLETE_PKG.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.TOGGLE_SHOW_OBSOLETE_PKG, showObsoletePackages);

        MenuItem showArchives = new MenuItem(menuPkgs,
                MenuAction.TOGGLE_SHOW_ARCHIVES.getMenuStyle());
        showArchives.setText(
                MenuAction.TOGGLE_SHOW_ARCHIVES.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.TOGGLE_SHOW_ARCHIVES, showArchives);

        new MenuItem(menuPkgs, SWT.SEPARATOR);

        MenuItem sortByApi = new MenuItem(menuPkgs,
                MenuAction.SORT_API_LEVEL.getMenuStyle());
        sortByApi.setText(
                MenuAction.SORT_API_LEVEL.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.SORT_API_LEVEL, sortByApi);

        MenuItem sortBySource = new MenuItem(menuPkgs,
                MenuAction.SORT_SOURCE.getMenuStyle());
        sortBySource.setText(
                MenuAction.SORT_SOURCE.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.SORT_SOURCE, sortBySource);

        new MenuItem(menuPkgs, SWT.SEPARATOR);

        MenuItem reload = new MenuItem(menuPkgs,
                MenuAction.RELOAD.getMenuStyle());
        reload.setText(
                MenuAction.RELOAD.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.RELOAD, reload);

        MenuItem menuBarTools = new MenuItem(menuBar, SWT.CASCADE);
        menuBarTools.setText("Tools");

        Menu menuTools = new Menu(menuBarTools);
        menuBarTools.setMenu(menuTools);

        if (mContext == SdkInvocationContext.STANDALONE) {
            MenuItem manageAvds = new MenuItem(menuTools, SWT.NONE);
            manageAvds.setText("Manage AVDs...");
            manageAvds.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    onAvdManager();
                }
            });
        }

        MenuItem manageSources = new MenuItem(menuTools,
                MenuAction.SHOW_ADDON_SITES.getMenuStyle());
        manageSources.setText(
                MenuAction.SHOW_ADDON_SITES.getMenuTitle());
        mPkgPage.registerMenuAction(
                MenuAction.SHOW_ADDON_SITES, manageSources);

        if (mContext == SdkInvocationContext.STANDALONE || mContext == SdkInvocationContext.IDE) {
            try {
                new MenuBarWrapper(APP_NAME, menuTools) {
                    @Override
                    public void onPreferencesMenuSelected() {

                        // capture a copy of the initial settings
                        Settings settings1 = new Settings(mSettingsController.getSettings());

                        // open the dialog and wait for it to close
                        SettingsDialog sd = new SettingsDialog(mShell, mUpdaterData);
                        sd.open();

                        // get the new settings
                        Settings settings2 = mSettingsController.getSettings();

                        // We need to reload the package list if the http mode or the preview
                        // modes have changed.
                        if (settings1.getForceHttp() != settings2.getForceHttp() ||
                                settings1.getEnablePreviews() != settings2.getEnablePreviews()) {
                            mPkgPage.onSdkReload();
                        }
                    }

                    @Override
                    public void onAboutMenuSelected() {
                        AboutDialog ad = new AboutDialog(mShell, mUpdaterData);
                        ad.open();
                    }

                    @Override
                    public void printError(String format, Object... args) {
                        if (mUpdaterData != null) {
                            mUpdaterData.getSdkLog().error(null, format, args);
                        }
                    }
                };
            } catch (Throwable e) {
                mUpdaterData.getSdkLog().error(e, "Failed to setup menu bar");
                e.printStackTrace();
            }
        }
    }

    private Image getImage(String filename) {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                return imgFactory.getImageByName(filename);
            }
        }
        return null;
    }

    /**
     * Creates the log window.
     * <p/>
     * If this is invoked from an IDE, we also define a secondary logger so that all
     * messages flow to the IDE log. This may or may not be what we want in the end
     * (e.g. a middle ground would be to repeat error, and ignore normal/verbose)
     */
    private void createLogWindow() {
        mLogWindow = new LogWindow(mShell,
                mContext == SdkInvocationContext.IDE ? mUpdaterData.getSdkLog() : null);
        mLogWindow.open();
    }


    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    // --- Public API -----------

    /**
     * Adds a new listener to be notified when a change is made to the content of the SDK.
     */
    @Override
    public void addListener(ISdkChangeListener listener) {
        mUpdaterData.addListeners(listener);
    }

    /**
     * Removes a new listener to be notified anymore when a change is made to the content of
     * the SDK.
     */
    @Override
    public void removeListener(ISdkChangeListener listener) {
        mUpdaterData.removeListener(listener);
    }

    // --- Internals & UI Callbacks -----------

    /**
     * Called before the UI is created.
     */
    private void preCreateContent() {
        mUpdaterData.setWindowShell(mShell);
        // We need the UI factory to create the UI
        mUpdaterData.setImageFactory(new ImageFactory(mShell.getDisplay()));
        // Note: we can't create the TaskFactory yet because we need the UI
        // to be created first, so this is done in postCreateContent().
    }

    /**
     * Once the UI has been created, initializes the content.
     * This creates the pages, selects the first one, setups sources and scans for local folders.
     *
     * Returns true if we should show the window.
     */
    private boolean postCreateContent() {
        ProgressViewFactory factory = new ProgressViewFactory();

        // This class delegates all logging to the mLogWindow window
        // and filters errors to make sure the window is visible when
        // an error is logged.
        ILogUiProvider logAdapter = new ILogUiProvider() {
            @Override
            public void setDescription(String description) {
                mLogWindow.setDescription(description);
            }

            @Override
            public void log(String log) {
                mLogWindow.log(log);
            }

            @Override
            public void logVerbose(String log) {
                mLogWindow.logVerbose(log);
            }

            @Override
            public void logError(String log) {
                mLogWindow.logError(log);

                // Run the window visibility check/toggle on the UI thread.
                // Note: at least on Windows, it seems ok to check for the window visibility
                // on a sub-thread but that doesn't seem cross-platform safe. We shouldn't
                // have a lot of error logging, so this should be acceptable. If not, we could
                // cache the visibility state.
                if (mShell != null && !mShell.isDisposed()) {
                    mShell.getDisplay().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            if (!mLogWindow.isVisible()) {
                                // Don't toggle the window visibility directly.
                                // Instead use the same action as the log-toggle button
                                // so that the button's state be kept in sync.
                                onToggleLogWindow();
                            }
                        }
                    });
                }
            }
        };

        factory.setProgressView(
                new ProgressView(mStatusText, mProgressBar, mButtonStop, logAdapter));
        mUpdaterData.setTaskFactory(factory);

        setWindowImage(mShell);

        setupSources();
        initializeSettings();

        if (mUpdaterData.checkIfInitFailed()) {
            return false;
        }

        mUpdaterData.broadcastOnSdkLoaded();

        // Tell the one page its the selected one
        mPkgPage.performFirstLoad();

        return true;
    }

    /**
     * Creates the icon of the window shell.
     *
     * @param shell The shell on which to put the icon
     */
    private void setWindowImage(Shell shell) {
        String imageName = "android_icon_16.png"; //$NON-NLS-1$
        if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png"; //$NON-NLS-1$
        }

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                shell.setImage(imgFactory.getImageByName(imageName));
            }
        }
    }

    /**
     * Called by the main loop when the window has been disposed.
     */
    private void dispose() {
        mLogWindow.close();
        mUpdaterData.getSources().saveUserAddons(mUpdaterData.getSdkLog());
    }

    /**
     * Callback called when the window shell is disposed.
     */
    private void onAndroidSdkUpdaterDispose() {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                imgFactory.dispose();
            }
        }
    }

    /**
     * Used to initialize the sources.
     */
    private void setupSources() {
        mUpdaterData.setupDefaultSources();
    }

    /**
     * Initializes settings.
     * This must be called after addExtraPages(), which created a settings page.
     * Iterate through all the pages to find the first (and supposedly unique) setting page,
     * and use it to load and apply these settings.
     */
    private void initializeSettings() {
        mSettingsController = mUpdaterData.getSettingsController();
        mSettingsController.loadSettings();
        mSettingsController.applySettings();
    }

    private void onToggleLogWindow() {
        // toggle visibility
        if (!mButtonShowLog.isDisposed()) {
            mLogWindow.setVisible(!mLogWindow.isVisible());
            mButtonShowLog.setState(mLogWindow.isVisible() ? 1 : 0);
        }
    }

    private void onStopSelected() {
        // TODO
    }

    private void onAvdManager() {
        ITaskFactory oldFactory = mUpdaterData.getTaskFactory();

        try {
            AvdManagerWindowImpl1 win = new AvdManagerWindowImpl1(
                    mShell,
                    mUpdaterData,
                    AvdInvocationContext.DIALOG);

            win.open();
        } catch (Exception e) {
            mUpdaterData.getSdkLog().error(e, "AVD Manager window error");
        } finally {
            mUpdaterData.setTaskFactory(oldFactory);
        }
    }

    // End of hiding from SWT Designer
    //$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/ShellSizeAndPos.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/ShellSizeAndPos.java
//Synthetic comment -- index e53c8ae..57161b4 100755

//Synthetic comment -- @@ -1,166 +1,166 @@
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

package com.android.sdkuilib.internal.repository.sdkman2;


import com.android.prefs.AndroidLocation;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility to save & restore the size and position on a window
 * using a common config file.
 */
public class ShellSizeAndPos {

    private static final String SETTINGS_FILENAME = "androidwin.cfg";   //$NON-NLS-1$
    private static final String PX = "_px";                             //$NON-NLS-1$
    private static final String PY = "_py";                             //$NON-NLS-1$
    private static final String SX = "_sx";                             //$NON-NLS-1$
    private static final String SY = "_sy";                             //$NON-NLS-1$

    public static void loadSizeAndPos(Shell shell, String prefix) {
        Properties props = loadProperties();

        try {
            int px = Integer.parseInt(props.getProperty(prefix + PX));
            int py = Integer.parseInt(props.getProperty(prefix + PY));
            int sx = Integer.parseInt(props.getProperty(prefix + SX));
            int sy = Integer.parseInt(props.getProperty(prefix + SY));

            Point p1 = new Point(px, py);
            Point p2 = new Point(px + sx, py + sy);
            Rectangle r = new Rectangle(px, py, sy, sy);

            Monitor bestMatch = null;
            int bestSurface = -1;
            for (Monitor monitor : shell.getDisplay().getMonitors()) {
                Rectangle area = monitor.getClientArea();
                if (area.contains(p1) && area.contains(p2)) {
                    // The shell is fully visible on this monitor. Just use that.
                    bestMatch = monitor;
                    bestSurface = Integer.MAX_VALUE;
                    break;
                } else {
                    // Find which monitor displays the largest surface of the window.
                    // We'll use this one to center the window there, to make sure we're not
                    // starting split between several monitors.
                    Rectangle i = area.intersection(r);
                    int surface = i.width * i.height;
                    if (surface > bestSurface) {
                        bestSurface = surface;
                        bestMatch = monitor;
                    }
                }
            }

            if (bestMatch != null && bestSurface != Integer.MAX_VALUE) {
                // Recenter the window on this monitor and make sure it fits
                Rectangle area = bestMatch.getClientArea();

                sx = Math.min(sx, area.width);
                sy = Math.min(sy, area.height);
                px = area.x + (area.width - sx) / 2;
                py = area.y + (area.height - sy) / 2;
            }

            shell.setLocation(px, py);
            shell.setSize(sx, sy);

        } catch ( Exception e) {
            // Ignore exception. We could typically get NPE from the getProperty
            // or NumberFormatException from parseInt calls. Either way, do
            // nothing if anything goes wrong.
        }
    }

    public static void saveSizeAndPos(Shell shell, String prefix) {
        Properties props = loadProperties();

        Point loc = shell.getLocation();
        Point size = shell.getSize();

        props.setProperty(prefix + PX, Integer.toString(loc.x));
        props.setProperty(prefix + PY, Integer.toString(loc.y));
        props.setProperty(prefix + SX, Integer.toString(size.x));
        props.setProperty(prefix + SY, Integer.toString(size.y));

        saveProperties(props);
    }

    /**
     * Load properties saved in {@link #SETTINGS_FILENAME}.
     * If the file does not exists or doesn't load properly, just return an
     * empty set of properties.
     */
    private static Properties loadProperties() {
        Properties props = new Properties();
        FileInputStream fis = null;

        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SETTINGS_FILENAME);
            if (f.exists()) {
                fis = new FileInputStream(f);

                props.load(fis);
            }
        } catch (Exception e) {
            // Ignore
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }

        return props;
    }

    private static void saveProperties(Properties props) {
        FileOutputStream fos = null;

        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SETTINGS_FILENAME);
            fos = new FileOutputStream(f);

            props.store(fos, "## Size and Pos for SDK Manager Windows");  //$NON-NLS-1$

        } catch (Exception e) {
            // ignore
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java
//Synthetic comment -- index 2563a5f..d5404ae 100755

//Synthetic comment -- @@ -1,108 +1,108 @@
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

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;

import org.eclipse.swt.widgets.Shell;


/**
 * An {@link ITaskMonitor} that displays a {@link ProgressTaskDialog}.
 */
public final class ProgressTask extends TaskMonitorImpl {

    private final String mTitle;
    private final ProgressTaskDialog mDialog;
    private volatile boolean mAutoClose = true;


    /**
     * Creates a new {@link ProgressTask} with the given title.
     * This does NOT start the task. The caller must invoke {@link #start(ITask)}.
     */
    public ProgressTask(Shell parent, String title) {
        super(new ProgressTaskDialog(parent));
        mTitle = title;
        mDialog = (ProgressTaskDialog) getUiProvider();
        mDialog.setText(mTitle);
    }

    /**
     * Execute the given task in a separate thread (not the UI thread).
     * This blocks till the thread ends.
     * <p/>
     * The {@link ProgressTask} must not be reused after this call.
     */
    public void start(ITask task) {
        assert mDialog != null;
        mDialog.open(createTaskThread(mTitle, task));
    }

    /**
     * Changes the auto-close behavior of the dialog on task completion.
     *
     * @param autoClose True if the dialog should be closed automatically when the task
     *   has completed.
     */
    public void setAutoClose(boolean autoClose) {
        if (autoClose != mAutoClose) {
            if (autoClose) {
                mDialog.setAutoCloseRequested();
            } else {
                mDialog.setManualCloseRequested();
            }
            mAutoClose = autoClose;
        }
    }

    /**
     * Creates a thread to run the task. The thread has not been started yet.
     * When the task completes, requests to close the dialog.
     *
     * @return A new thread that will run the task. The thread has not been started yet.
     */
    private Thread createTaskThread(String title, final ITask task) {
        if (task != null) {
            return new Thread(title) {
                @Override
                public void run() {
                    task.run(ProgressTask.this);
                    if (mAutoClose) {
                        mDialog.setAutoCloseRequested();
                    } else {
                        mDialog.setManualCloseRequested();
                    }
                }
            };
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Sets the dialog to not auto-close since we want the user to see the error
     * (this is equivalent to calling {@code setAutoClose(false)}).
     */
    @Override
    public void logError(String format, Object...args) {
        setAutoClose(false);
        super.logError(format, args);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java
//Synthetic comment -- index c6ba8b7..8eee146 100755

//Synthetic comment -- @@ -1,518 +1,518 @@
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

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UserCredentials;
import com.android.sdkuilib.ui.AuthenticationDialog;
import com.android.sdkuilib.ui.GridDialog;
import com.android.util.Pair;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * Implements a {@link ProgressTaskDialog}, used by the {@link ProgressTask} class.
 * This separates the dialog UI from the task logic.
 *
 * Note: this does not implement the {@link ITaskMonitor} interface to avoid confusing
 * SWT Designer.
 */
final class ProgressTaskDialog extends Dialog implements IProgressUiProvider {

    /**
     * Min Y location for dialog. Need to deal with the menu bar on mac os.
     */
    private final static int MIN_Y = SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN ?
            20 : 0;

    private static enum CancelMode {
        /** Cancel button says "Cancel" and is enabled. Waiting for user to cancel. */
        ACTIVE,
        /** Cancel button has been clicked. Waiting for thread to finish. */
        CANCEL_PENDING,
        /** Close pending. Close button clicked or thread finished but there were some
         * messages so the user needs to manually close. */
        CLOSE_MANUAL,
        /** Close button clicked or thread finished. The window will automatically close. */
        CLOSE_AUTO
    }

    /** The current mode of operation of the dialog. */
    private CancelMode mCancelMode = CancelMode.ACTIVE;

    /** Last dialog size for this session. */
    private static Point sLastSize;


    // UI fields
    private Shell mDialogShell;
    private Composite mRootComposite;
    private Label mLabel;
    private ProgressBar mProgressBar;
    private Button mCancelButton;
    private Text mResultText;


    /**
     * Create the dialog.
     * @param parent Parent container
     */
    public ProgressTaskDialog(Shell parent) {
        super(parent, SWT.APPLICATION_MODAL);
    }

    /**
     * Open the dialog and blocks till it gets closed
     * @param taskThread The thread to run the task. Cannot be null.
     */
    public void open(Thread taskThread) {
        createContents();
        positionShell();                        //$hide$ (hide from SWT designer)
        mDialogShell.open();
        mDialogShell.layout();

        startThread(taskThread);                //$hide$ (hide from SWT designer)

        Display display = getParent().getDisplay();
        while (!mDialogShell.isDisposed() && mCancelMode != CancelMode.CLOSE_AUTO) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        setCancelRequested();       //$hide$ (hide from SWT designer)

        if (!mDialogShell.isDisposed()) {
            sLastSize = mDialogShell.getSize();
            mDialogShell.close();
        }
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        mDialogShell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        mDialogShell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                onShellClosed(e);
            }
        });
        mDialogShell.setLayout(new GridLayout(1, false));
        mDialogShell.setSize(450, 300);
        mDialogShell.setText(getText());

        mRootComposite = new Composite(mDialogShell, SWT.NONE);
        mRootComposite.setLayout(new GridLayout(2, false));
        mRootComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        mLabel = new Label(mRootComposite, SWT.NONE);
        mLabel.setText("Task");
        mLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        mProgressBar = new ProgressBar(mRootComposite, SWT.NONE);
        mProgressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mCancelButton = new Button(mRootComposite, SWT.NONE);
        mCancelButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        mCancelButton.setText("Cancel");

        mCancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onCancelSelected();  //$hide$
            }
        });

        mResultText = new Text(mRootComposite,
                SWT.BORDER | SWT.READ_ONLY | SWT.WRAP |
                SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        mResultText.setEditable(true);
        mResultText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    }

    // -- End of UI, Start of internal logic ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    @Override
    public boolean isCancelRequested() {
        return mCancelMode != CancelMode.ACTIVE;
    }

    /**
     * Sets the mode to cancel pending.
     * The first time this grays the cancel button, to let the user know that the
     * cancel operation is pending.
     */
    public void setCancelRequested() {
        if (!mDialogShell.isDisposed()) {
            // The dialog is not disposed, make sure to run all this in the UI thread
            // and lock on the cancel button mode.
            mDialogShell.getDisplay().syncExec(new Runnable() {

                @Override
                public void run() {
                    synchronized (mCancelMode) {
                        if (mCancelMode == CancelMode.ACTIVE) {
                            mCancelMode = CancelMode.CANCEL_PENDING;

                            if (!mCancelButton.isDisposed()) {
                                mCancelButton.setEnabled(false);
                            }
                        }
                    }
                }
            });
        } else {
            // The dialog is disposed. Just set the boolean. We shouldn't be here.
            if (mCancelMode == CancelMode.ACTIVE) {
                mCancelMode = CancelMode.CANCEL_PENDING;
            }
        }
    }

    /**
     * Sets the mode to close manual.
     * The first time, this also ungrays the pause button and converts it to a close button.
     */
    public void setManualCloseRequested() {
        if (!mDialogShell.isDisposed()) {
            // The dialog is not disposed, make sure to run all this in the UI thread
            // and lock on the cancel button mode.
            mDialogShell.getDisplay().syncExec(new Runnable() {

                @Override
                public void run() {
                    synchronized (mCancelMode) {
                        if (mCancelMode != CancelMode.CLOSE_MANUAL &&
                                mCancelMode != CancelMode.CLOSE_AUTO) {
                            mCancelMode = CancelMode.CLOSE_MANUAL;

                            if (!mCancelButton.isDisposed()) {
                                mCancelButton.setEnabled(true);
                                mCancelButton.setText("Close");
                            }
                        }
                    }
                }
            });
        } else {
            // The dialog is disposed. Just set the booleans. We shouldn't be here.
            if (mCancelMode != CancelMode.CLOSE_MANUAL &&
                    mCancelMode != CancelMode.CLOSE_AUTO) {
                mCancelMode = CancelMode.CLOSE_MANUAL;
            }
        }
    }

    /**
     * Sets the mode to close auto.
     * The main loop will just exit and close the shell at the first opportunity.
     */
    public void setAutoCloseRequested() {
        synchronized (mCancelMode) {
            if (mCancelMode != CancelMode.CLOSE_AUTO) {
                mCancelMode = CancelMode.CLOSE_AUTO;
            }
        }
    }

    /**
     * Callback invoked when the cancel button is selected.
     * When in closing mode, this simply closes the shell. Otherwise triggers a cancel.
     */
    private void onCancelSelected() {
        if (mCancelMode == CancelMode.CLOSE_MANUAL) {
            setAutoCloseRequested();
        } else {
            setCancelRequested();
        }
    }

    /**
     * Callback invoked when the shell is closed either by clicking the close button
     * on by calling shell.close().
     * This does the same thing as clicking the cancel/close button unless the mode is
     * to auto close in which case we should do nothing to let the shell close normally.
     */
    private void onShellClosed(ShellEvent e) {
        if (mCancelMode != CancelMode.CLOSE_AUTO) {
            e.doit = false; // don't close directly
            onCancelSelected();
        }
    }

    /**
     * Sets the description in the current task dialog.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void setDescription(final String description) {
        mDialogShell.getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                if (!mLabel.isDisposed()) {
                    mLabel.setText(description);
                }
            }
        });
    }

    /**
     * Adds to the log in the current task dialog.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void log(final String info) {
        if (!mDialogShell.isDisposed()) {
            mDialogShell.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (!mResultText.isDisposed()) {
                        mResultText.setVisible(true);
                        String lastText = mResultText.getText();
                        if (lastText != null &&
                                lastText.length() > 0 &&
                                !lastText.endsWith("\n") &&     //$NON-NLS-1$
                                !info.startsWith("\n")) {       //$NON-NLS-1$
                            mResultText.append("\n");           //$NON-NLS-1$
                        }
                        mResultText.append(info);
                    }
                }
            });
        }
    }

    @Override
    public void logError(String info) {
        log(info);
    }

    @Override
    public void logVerbose(String info) {
        log(info);
    }

    /**
     * Sets the max value of the progress bar.
     * This method can be invoked from a non-UI thread.
     *
     * @see ProgressBar#setMaximum(int)
     */
    @Override
    public void setProgressMax(final int max) {
        if (!mDialogShell.isDisposed()) {
            mDialogShell.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (!mProgressBar.isDisposed()) {
                        mProgressBar.setMaximum(max);
                    }
                }
            });
        }
    }

    /**
     * Sets the current value of the progress bar.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void setProgress(final int value) {
        if (!mDialogShell.isDisposed()) {
            mDialogShell.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (!mProgressBar.isDisposed()) {
                        mProgressBar.setSelection(value);
                    }
                }
            });
        }
    }

    /**
     * Returns the current value of the progress bar,
     * between 0 and up to {@link #setProgressMax(int)} - 1.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public int getProgress() {
        final int[] result = new int[] { 0 };

        if (!mDialogShell.isDisposed()) {
            mDialogShell.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (!mProgressBar.isDisposed()) {
                        result[0] = mProgressBar.getSelection();
                    }
                }
            });
        }

        return result[0];
    }

    /**
     * Display a yes/no question dialog box.
     *
     * This implementation allow this to be called from any thread, it
     * makes sure the dialog is opened synchronously in the ui thread.
     *
     * @param title The title of the dialog box
     * @param message The error message
     * @return true if YES was clicked.
     */
    @Override
    public boolean displayPrompt(final String title, final String message) {
        Display display = mDialogShell.getDisplay();

        // we need to ask the user what he wants to do.
        final boolean[] result = new boolean[] { false };
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                result[0] = MessageDialog.openQuestion(mDialogShell, title, message);
            }
        });
        return result[0];
    }

    /**
     * This method opens a pop-up window which requests for User Login and
     * password.
     *
     * @param title The title of the window.
     * @param message The message to displayed in the login/password window.
     * @return Returns a {@link Pair} holding the entered login and password.
     *         The information must always be in the following order:
     *         Login,Password. So in order to retrieve the <b>login</b> callers
     *         should retrieve the first element, and the second value for the
     *         <b>password</b>.
     *         If operation is <b>canceled</b> by user the return value must be <b>null</b>.
     * @see ITaskMonitor#displayLoginCredentialsPrompt(String, String)
     */
    @Override
    public UserCredentials displayLoginCredentialsPrompt(
            final String title, final String message) {
        Display display = mDialogShell.getDisplay();

        // open dialog and request login and password
        GetUserCredentialsTask task = new GetUserCredentialsTask(mDialogShell, title, message);
        display.syncExec(task);

        return new UserCredentials(task.userName, task.password, task.workstation, task.domain);
    }

    private static class GetUserCredentialsTask implements Runnable {
        public String userName = null;
        public String password = null;
        public String workstation = null;
        public String domain = null;

        private Shell mShell;
        private String mTitle;
        private String mMessage;

        public GetUserCredentialsTask(Shell shell, String title, String message) {
            mShell = shell;
            mTitle = title;
            mMessage = message;
        }

        @Override
        public void run() {
            AuthenticationDialog authenticationDialog = new AuthenticationDialog(mShell,
                        mTitle, mMessage);
            int dlgResult= authenticationDialog.open();
            if(dlgResult == GridDialog.OK) {
                userName = authenticationDialog.getLogin();
                password = authenticationDialog.getPassword();
                workstation = authenticationDialog.getWorkstation();
                domain = authenticationDialog.getDomain();
            }
        }
    }

    /**
     * Starts the thread that runs the task.
     * This is deferred till the UI is created.
     */
    private void startThread(Thread taskThread) {
        if (taskThread != null) {
            taskThread.start();
        }
    }

    /**
     * Centers the dialog in its parent shell.
     */
    private void positionShell() {
        // Centers the dialog in its parent shell
        Shell child = mDialogShell;
        Shell parent = getParent();
        if (child != null && parent != null) {

            // get the parent client area with a location relative to the display
            Rectangle parentArea = parent.getClientArea();
            Point parentLoc = parent.getLocation();
            int px = parentLoc.x;
            int py = parentLoc.y;
            int pw = parentArea.width;
            int ph = parentArea.height;

            // Reuse the last size if there's one, otherwise use the default
            Point childSize = sLastSize != null ? sLastSize : child.getSize();
            int cw = childSize.x;
            int ch = childSize.y;

            int x = px + (pw - cw) / 2;
            if (x < 0) x = 0;

            int y = py + (ph - ch) / 2;
            if (y < MIN_Y) y = MIN_Y;

            child.setLocation(x, y);
            child.setSize(cw, ch);
        }
    }

    // End of hiding from SWT Designer
    //$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskFactory.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskFactory.java
//Synthetic comment -- index bd2cc14..17cba7a 100755

//Synthetic comment -- @@ -1,67 +1,67 @@
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

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;

import org.eclipse.swt.widgets.Shell;

/**
 * An {@link ITaskFactory} that creates a new {@link ProgressTask} dialog
 * for each new task.
 */
public final class ProgressTaskFactory implements ITaskFactory {

    private final Shell mShell;

    public ProgressTaskFactory(Shell shell) {
        mShell = shell;
    }

    @Override
    public void start(String title, ITask task) {
        start(title, null /*parentMonitor*/, task);
    }

    @Override
    public void start(String title, ITaskMonitor parentMonitor, ITask task) {

        if (parentMonitor == null) {
            ProgressTask p = new ProgressTask(mShell, title);
            p.start(task);
        } else {
            // Use all the reminder of the parent monitor.
            if (parentMonitor.getProgressMax() == 0) {
                parentMonitor.setProgressMax(1);
            }

            ITaskMonitor sub = parentMonitor.createSubMonitor(
                    parentMonitor.getProgressMax() - parentMonitor.getProgress());
            try {
                task.run(sub);
            } finally {
                int delta =
                    sub.getProgressMax() - sub.getProgress();
                if (delta > 0) {
                    sub.incProgress(delta);
                }
            }
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index 3448852..4691ec6 100755

//Synthetic comment -- @@ -1,375 +1,375 @@
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

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UserCredentials;
import com.android.sdkuilib.ui.AuthenticationDialog;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;


/**
 * Implements a "view" that uses an existing progress bar, status button and
 * status text to display a {@link ITaskMonitor}.
 */
public final class ProgressView implements IProgressUiProvider {

    private static enum State {
        /** View created but there's no task running. Next state can only be ACTIVE. */
        IDLE,
        /** A task is currently running. Next state is either STOP_PENDING or IDLE. */
        ACTIVE,
        /** Stop button has been clicked. Waiting for thread to finish. Next state is IDLE. */
        STOP_PENDING,
}

    /** The current mode of operation of the dialog. */
    private State mState = State.IDLE;



    // UI fields
    private final Label mLabel;
    private final Control mStopButton;
    private final ProgressBar mProgressBar;

    /** Logger object. Cannot not be null. */
    private final ILogUiProvider mLog;

    /**
     * Creates a new {@link ProgressView} object, a simple "holder" for the various
     * widgets used to display and update a progress + status bar.
     *
     * @param label The label to display titles of status updates (e.g. task titles and
     *      calls to {@link #setDescription(String)}.) Must not be null.
     * @param progressBar The progress bar to update during a task. Must not be null.
     * @param stopButton The stop button. It will be disabled when there's no task that can
     *      be interrupted. A selection listener will be attached to it. Optional. Can be null.
     * @param log A <em>mandatory</em> logger object that will be used to report all the log.
     *      Must not be null.
     */
    public ProgressView(
            Label label,
            ProgressBar progressBar,
            Control stopButton,
            ILogUiProvider log) {
        mLabel = label;
        mProgressBar = progressBar;
        mLog = log;
        mProgressBar.setEnabled(false);

        mStopButton = stopButton;
        if (mStopButton != null) {
            mStopButton.addListener(SWT.Selection, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (mState == State.ACTIVE) {
                        changeState(State.STOP_PENDING);
                    }
                }
            });
        }
    }

    /**
     * Starts the task and block till it's either finished or canceled.
     * This can be called from a non-UI thread safely.
     * <p/>
     * When a task is started from within a monitor, it reuses the thread
     * from the parent. Otherwise it starts a new thread and runs it own
     * UI loop. This means the task can perform UI operations using
     * {@link Display#asyncExec(Runnable)}.
     * <p/>
     * In either case, the method only returns when the task has finished.
     */
    public void startTask(
            final String title,
            final ITaskMonitor parentMonitor,
            final ITask task) {
        if (task != null) {
            try {
                if (parentMonitor == null && !mProgressBar.isDisposed()) {
                    mLabel.setText(title);
                    mProgressBar.setSelection(0);
                    mProgressBar.setEnabled(true);
                    changeState(ProgressView.State.ACTIVE);
                }

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (parentMonitor == null) {
                            task.run(new TaskMonitorImpl(ProgressView.this));

                        } else {
                            // Use all the reminder of the parent monitor.
                            if (parentMonitor.getProgressMax() == 0) {
                                parentMonitor.setProgressMax(1);
                            }
                            ITaskMonitor sub = parentMonitor.createSubMonitor(
                                    parentMonitor.getProgressMax() - parentMonitor.getProgress());
                            try {
                                task.run(sub);
                            } finally {
                                int delta =
                                    sub.getProgressMax() - sub.getProgress();
                                if (delta > 0) {
                                    sub.incProgress(delta);
                                }
                            }
                        }
                    }
                };

                // If for some reason the UI has been disposed, just abort the thread.
                if (mProgressBar.isDisposed()) {
                    return;
                }

                if (TaskMonitorImpl.isTaskMonitorImpl(parentMonitor)) {
                    // If there's a parent monitor and it's our own class, we know this parent
                    // is already running a thread and the base one is running an event loop.
                    // We should thus not run a second event loop and we can process the
                    // runnable right here instead of spawning a thread inside the thread.
                    r.run();

                } else {
                    // No parent monitor. This is the first one so we need a thread and
                    // we need to process UI events.

                    final Thread t = new Thread(r, title);
                    t.start();

                    // Process the app's event loop whilst we wait for the thread to finish
                    while (!mProgressBar.isDisposed() && t.isAlive()) {
                        Display display = mProgressBar.getDisplay();
                        if (!mProgressBar.isDisposed() && !display.readAndDispatch()) {
                            display.sleep();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO log

            } finally {
                if (parentMonitor == null && !mProgressBar.isDisposed()) {
                    changeState(ProgressView.State.IDLE);
                    mProgressBar.setSelection(0);
                    mProgressBar.setEnabled(false);
                }
            }
        }
    }

    private void syncExec(final Widget widget, final Runnable runnable) {
        if (widget != null && !widget.isDisposed()) {
            widget.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    // Check again whether the widget got disposed between the time where
                    // we requested the syncExec and the time it actually happened.
                    if (!widget.isDisposed()) {
                        runnable.run();
                    }
                }
            });
        }
    }

    private void changeState(State state) {
        if (mState != null ) {
            mState = state;
        }

        syncExec(mStopButton, new Runnable() {
            @Override
            public void run() {
                mStopButton.setEnabled(mState == State.ACTIVE);
            }
        });

    }

    // --- Implementation of ITaskUiProvider ---

    @Override
    public boolean isCancelRequested() {
        return mState != State.ACTIVE;
    }

    /**
     * Sets the description in the current task dialog.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void setDescription(final String description) {
        syncExec(mLabel, new Runnable() {
            @Override
            public void run() {
                mLabel.setText(description);
            }
        });

        mLog.setDescription(description);
    }

    /**
     * Logs a "normal" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void log(String log) {
        mLog.log(log);
    }

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logError(String log) {
        mLog.logError(log);
    }

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logVerbose(String log) {
        mLog.logVerbose(log);
    }

    /**
     * Sets the max value of the progress bar.
     * This method can be invoked from a non-UI thread.
     *
     * @see ProgressBar#setMaximum(int)
     */
    @Override
    public void setProgressMax(final int max) {
        syncExec(mProgressBar, new Runnable() {
            @Override
            public void run() {
                mProgressBar.setMaximum(max);
            }
        });
    }

    /**
     * Sets the current value of the progress bar.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void setProgress(final int value) {
        syncExec(mProgressBar, new Runnable() {
            @Override
            public void run() {
                mProgressBar.setSelection(value);
            }
        });
    }

    /**
     * Returns the current value of the progress bar,
     * between 0 and up to {@link #setProgressMax(int)} - 1.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public int getProgress() {
        final int[] result = new int[] { 0 };

        if (!mProgressBar.isDisposed()) {
            mProgressBar.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (!mProgressBar.isDisposed()) {
                        result[0] = mProgressBar.getSelection();
                    }
                }
            });
        }

        return result[0];
    }

    @Override
    public boolean displayPrompt(final String title, final String message) {
        final boolean[] result = new boolean[] { false };

        syncExec(mProgressBar, new Runnable() {
            @Override
            public void run() {
                Shell shell = mProgressBar.getShell();
                result[0] = MessageDialog.openQuestion(shell, title, message);
            }
        });

        return result[0];
    }

    /**
     * This method opens a pop-up window which requests for User Credentials.
     *
     * @param title The title of the window.
     * @param message The message to displayed in the login/password window.
     * @return Returns user provided credentials.
     *         If operation is <b>canceled</b> by user the return value must be <b>null</b>.
     * @see ITaskMonitor#displayLoginCredentialsPrompt(String, String)
     */
    @Override
    public UserCredentials
            displayLoginCredentialsPrompt(final String title, final String message) {
        final String[] resultArray = new String[] {"", "", "", ""};
        // open dialog and request login and password
        syncExec(mProgressBar, new Runnable() {
            @Override
            public void run() {
                Shell shell = mProgressBar.getShell();
                AuthenticationDialog authenticationDialog = new AuthenticationDialog(shell,
                        title,
                        message);
                int dlgResult = authenticationDialog.open();
                if (dlgResult == GridDialog.OK) {
                    resultArray[0] = authenticationDialog.getLogin();
                    resultArray[1] = authenticationDialog.getPassword();
                    resultArray[2] = authenticationDialog.getWorkstation();
                    resultArray[3] = authenticationDialog.getDomain();
                }
            }
        });

        return new UserCredentials(resultArray[0],
                resultArray[1],
                resultArray[2],
                resultArray[3]);
    }
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressViewFactory.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressViewFactory.java
//Synthetic comment -- index 1d39c59..2590169 100755

//Synthetic comment -- @@ -1,48 +1,48 @@
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

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;

/**
 * An {@link ITaskFactory} that creates a new {@link ProgressTask} dialog
 * for each new task.
 */
public final class ProgressViewFactory implements ITaskFactory {

    private ProgressView mProgressView;

    public ProgressViewFactory() {
    }

    public void setProgressView(ProgressView progressView) {
        mProgressView = progressView;
    }

    @Override
    public void start(String title, ITask task) {
        start(title, null /*monitor*/, task);
    }

    @Override
    public void start(String title, ITaskMonitor parentMonitor, ITask task) {
        assert mProgressView != null;
        mProgressView.startTask(title, parentMonitor, task);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java
//Synthetic comment -- index 9a796b7..9958920 100755

//Synthetic comment -- @@ -1,358 +1,358 @@
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

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UserCredentials;

/**
 * Internal class that implements the logic of an {@link ITaskMonitor}.
 * It doesn't deal with any UI directly. Instead it delegates the UI to
 * the provided {@link IProgressUiProvider}.
 */
class TaskMonitorImpl implements ITaskMonitor {

    private static final double MAX_COUNT = 10000.0;

    private interface ISubTaskMonitor extends ITaskMonitor {
        public void subIncProgress(double realDelta);
    }

    private double mIncCoef = 0;
    private double mValue = 0;
    private final IProgressUiProvider mUi;

    /**
     * Returns true if the given {@code monitor} is an instance of {@link TaskMonitorImpl}
     * or its private SubTaskMonitor.
     */
    public static boolean isTaskMonitorImpl(ITaskMonitor monitor) {
        return monitor instanceof TaskMonitorImpl || monitor instanceof SubTaskMonitor;
    }

    /**
     * Constructs a new {@link TaskMonitorImpl} that relies on the given
     * {@link IProgressUiProvider} to change the user interface.
     * @param ui The {@link IProgressUiProvider}. Cannot be null.
     */
    public TaskMonitorImpl(IProgressUiProvider ui) {
        mUi = ui;
    }

    /** Returns the {@link IProgressUiProvider} passed to the constructor. */
    public IProgressUiProvider getUiProvider() {
        return mUi;
    }

    /**
     * Sets the description in the current task dialog.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void setDescription(String format, Object... args) {
        final String text = String.format(format, args);
        mUi.setDescription(text);
    }

    /**
     * Logs a "normal" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void log(String format, Object... args) {
        String text = String.format(format, args);
        mUi.log(text);
    }

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logError(String format, Object... args) {
        String text = String.format(format, args);
        mUi.logError(text);
    }

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void logVerbose(String format, Object... args) {
        String text = String.format(format, args);
        mUi.logVerbose(text);
    }

    /**
     * Sets the max value of the progress bar.
     * This method can be invoked from a non-UI thread.
     *
     * Weird things will happen if setProgressMax is called multiple times
     * *after* {@link #incProgress(int)}: we don't try to adjust it on the
     * fly.
     */
    @Override
    public void setProgressMax(int max) {
        assert max > 0;
        // Always set the dialog's progress max to 10k since it only handles
        // integers and we want to have a better inner granularity. Instead
        // we use the max to compute a coefficient for inc deltas.
        mUi.setProgressMax((int) MAX_COUNT);
        mIncCoef = max > 0 ? MAX_COUNT / max : 0;
        assert mIncCoef > 0;
    }

    @Override
    public int getProgressMax() {
        return mIncCoef > 0 ? (int) (MAX_COUNT / mIncCoef) : 0;
    }

    /**
     * Increments the current value of the progress bar.
     *
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public void incProgress(int delta) {
        if (delta > 0 && mIncCoef > 0) {
            internalIncProgress(delta * mIncCoef);
        }
    }

    private void internalIncProgress(double realDelta) {
        mValue += realDelta;
        mUi.setProgress((int)mValue);
    }

    /**
     * Returns the current value of the progress bar,
     * between 0 and up to {@link #setProgressMax(int)} - 1.
     *
     * This method can be invoked from a non-UI thread.
     */
    @Override
    public int getProgress() {
        // mIncCoef is 0 if setProgressMax hasn't been used yet.
        return mIncCoef > 0 ? (int)(mUi.getProgress() / mIncCoef) : 0;
    }

    /**
     * Returns true if the "Cancel" button was selected.
     * It is up to the task thread to pool this and exit.
     */
    @Override
    public boolean isCancelRequested() {
        return mUi.isCancelRequested();
    }

    /**
     * Displays a yes/no question dialog box.
     *
     * This implementation allow this to be called from any thread, it
     * makes sure the dialog is opened synchronously in the ui thread.
     *
     * @param title The title of the dialog box
     * @param message The error message
     * @return true if YES was clicked.
     */
    @Override
    public boolean displayPrompt(final String title, final String message) {
        return mUi.displayPrompt(title, message);
    }

    /**
     * Displays a Login/Password dialog. This implementation allows this method to be
     * called from any thread, it makes sure the dialog is opened synchronously
     * in the ui thread.
     *
     * @param title The title of the dialog box
     * @param message Message to be displayed
     * @return Pair with entered login/password. Login is always the first
     *         element and Password is always the second. If any error occurs a
     *         pair with empty strings is returned.
     */
    @Override
    public UserCredentials displayLoginCredentialsPrompt(String title, String message) {
        return mUi.displayLoginCredentialsPrompt(title, message);
    }

    /**
     * Creates a sub-monitor that will use up to tickCount on the progress bar.
     * tickCount must be 1 or more.
     */
    @Override
    public ITaskMonitor createSubMonitor(int tickCount) {
        assert mIncCoef > 0;
        assert tickCount > 0;
        return new SubTaskMonitor(this, null, mValue, tickCount * mIncCoef);
    }

    // ----- ISdkLog interface ----

    @Override
    public void error(Throwable throwable, String errorFormat, Object... arg) {
        if (errorFormat != null) {
            logError("Error: " + errorFormat, arg);
        }

        if (throwable != null) {
            logError("%s", throwable.getMessage()); //$NON-NLS-1$
        }
    }

    @Override
    public void warning(String warningFormat, Object... arg) {
        log("Warning: " + warningFormat, arg);
    }

    @Override
    public void printf(String msgFormat, Object... arg) {
        log(msgFormat, arg);
    }

    // ----- Sub Monitor -----

    private static class SubTaskMonitor implements ISubTaskMonitor {

        private final TaskMonitorImpl mRoot;
        private final ISubTaskMonitor mParent;
        private final double mStart;
        private final double mSpan;
        private double mSubValue;
        private double mSubCoef;

        /**
         * Creates a new sub task monitor which will work for the given range [start, start+span]
         * in its parent.
         *
         * @param taskMonitor The ProgressTask root
         * @param parent The immediate parent. Can be the null or another sub task monitor.
         * @param start The start value in the root's coordinates
         * @param span The span value in the root's coordinates
         */
        public SubTaskMonitor(TaskMonitorImpl taskMonitor,
                ISubTaskMonitor parent,
                double start,
                double span) {
            mRoot = taskMonitor;
            mParent = parent;
            mStart = start;
            mSpan = span;
            mSubValue = start;
        }

        @Override
        public boolean isCancelRequested() {
            return mRoot.isCancelRequested();
        }

        @Override
        public void setDescription(String format, Object... args) {
            mRoot.setDescription(format, args);
        }

        @Override
        public void log(String format, Object... args) {
            mRoot.log(format, args);
        }

        @Override
        public void logError(String format, Object... args) {
            mRoot.logError(format, args);
        }

        @Override
        public void logVerbose(String format, Object... args) {
            mRoot.logVerbose(format, args);
        }

        @Override
        public void setProgressMax(int max) {
            assert max > 0;
            mSubCoef = max > 0 ? mSpan / max : 0;
            assert mSubCoef > 0;
        }

        @Override
        public int getProgressMax() {
            return mSubCoef > 0 ? (int) (mSpan / mSubCoef) : 0;
        }

        @Override
        public int getProgress() {
            // subCoef can be 0 if setProgressMax() and incProgress() haven't been called yet
            assert mSubValue == mStart || mSubCoef > 0;
            return mSubCoef > 0 ? (int)((mSubValue - mStart) / mSubCoef) : 0;
        }

        @Override
        public void incProgress(int delta) {
            if (delta > 0 && mSubCoef > 0) {
                subIncProgress(delta * mSubCoef);
            }
        }

        @Override
        public void subIncProgress(double realDelta) {
            mSubValue += realDelta;
            if (mParent != null) {
                mParent.subIncProgress(realDelta);
            } else {
                mRoot.internalIncProgress(realDelta);
            }
        }

        @Override
        public boolean displayPrompt(String title, String message) {
            return mRoot.displayPrompt(title, message);
        }

        @Override
        public UserCredentials displayLoginCredentialsPrompt(String title, String message) {
            return mRoot.displayLoginCredentialsPrompt(title, message);
        }

        @Override
        public ITaskMonitor createSubMonitor(int tickCount) {
            assert mSubCoef > 0;
            assert tickCount > 0;
            return new SubTaskMonitor(mRoot,
                    this,
                    mSubValue,
                    tickCount * mSubCoef);
        }

        // ----- ISdkLog interface ----

        @Override
        public void error(Throwable throwable, String errorFormat, Object... arg) {
            mRoot.error(throwable, errorFormat, arg);
        }

        @Override
        public void warning(String warningFormat, Object... arg) {
            mRoot.warning(warningFormat, arg);
        }

        @Override
        public void printf(String msgFormat, Object... arg) {
            mRoot.printf(msgFormat, arg);
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/ImgDisabledButton.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/ImgDisabledButton.java
//Synthetic comment -- index 7f1361e..62973a4 100755

//Synthetic comment -- @@ -1,60 +1,60 @@
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

package com.android.sdkuilib.internal.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * A label that can display 2 images depending on its enabled/disabled state.
 * This acts as a button by firing the {@link SWT#Selection} listener.
 */
public class ImgDisabledButton extends ToggleButton {
    public ImgDisabledButton(
            Composite parent,
            int style,
            Image imageEnabled,
            Image imageDisabled,
            String tooltipEnabled,
            String tooltipDisabled) {
        super(parent,
                style,
                imageEnabled,
                imageDisabled,
                tooltipEnabled,
                tooltipDisabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateImageAndTooltip();
        redraw();
    }

    @Override
    public void setState(int state) {
        throw new UnsupportedOperationException(); // not available for this type of button
    }

    @Override
    public int getState() {
        return (isDisposed() || !isEnabled()) ? 1 : 0;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/ToggleButton.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/ToggleButton.java
//Synthetic comment -- index 24138bc..7c66bcf 100755

//Synthetic comment -- @@ -1,134 +1,134 @@
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

package com.android.sdkuilib.internal.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * A label that can display 2 images depending on its internal state.
 * This acts as a button by firing the {@link SWT#Selection} listener.
 */
public class ToggleButton extends CLabel {
    private Image[] mImage = new Image[2];
    private String[] mTooltip = new String[2];
    private boolean mMouseIn;
    private int mState = 0;


    public ToggleButton(
            Composite parent,
            int style,
            Image image1,
            Image image2,
            String tooltip1,
            String tooltip2) {
        super(parent, style);
        mImage[0] = image1;
        mImage[1] = image2;
        mTooltip[0] = tooltip1;
        mTooltip[1] = tooltip2;
        updateImageAndTooltip();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseDown(MouseEvent e) {
                // pass
            }

            @Override
            public void mouseUp(MouseEvent e) {
                // We select on mouse-up, as it should be properly done since this is the
                // only way a user can cancel a button click by moving out of the button.
                if (mMouseIn && e.button == 1) {
                    notifyListeners(SWT.Selection, new Event());
                }
            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if (mMouseIn && e.button == 1) {
                    notifyListeners(SWT.DefaultSelection, new Event());
                }
            }
        });

        addMouseTrackListener(new MouseTrackListener() {
            @Override
            public void mouseExit(MouseEvent e) {
                if (mMouseIn) {
                    mMouseIn = false;
                    redraw();
                }
            }

            @Override
            public void mouseEnter(MouseEvent e) {
                if (!mMouseIn) {
                    mMouseIn = true;
                    redraw();
                }
            }

            @Override
            public void mouseHover(MouseEvent e) {
                // pass
            }
        });
    }

    @Override
    public int getStyle() {
        int style = super.getStyle();
        if (mMouseIn) {
            style |= SWT.SHADOW_IN;
        }
        return style;
    }

    /**
     * Sets current state.
     * @param state A value 0 or 1.
     */
    public void setState(int state) {
        assert state == 0 || state == 1;
        mState = state;
        updateImageAndTooltip();
        redraw();
    }

    /**
     * Returns the current state
     * @return Returns the current state, either 0 or 1.
     */
    public int getState() {
        return mState;
    }

    protected void updateImageAndTooltip() {
        setImage(mImage[getState()]);
        setToolTipText(mTooltip[getState()]);
    }
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java
//Synthetic comment -- index 5ad5fc2..17816b8 100755

//Synthetic comment -- @@ -1,96 +1,96 @@
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

package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.sdkman2.AvdManagerWindowImpl1;
import com.android.sdkuilib.internal.widgets.AvdSelector;

import org.eclipse.swt.widgets.Shell;

/**
 * Opens an AVD Manager Window.
 *
 * This is the public entry point for using the window.
 */
public class AvdManagerWindow {

    /** The actual window implementation to which this class delegates. */
    private AvdManagerWindowImpl1 mWindow;

    /**
     * Enum giving some indication of what is invoking this window.
     * The behavior and UI will change slightly depending on the context.
     * <p/>
     * Note: if you add Android support to your specific IDE, you might want
     * to specialize this context enum.
     */
    public enum AvdInvocationContext {
        /**
         * The AVD Manager is invoked from the stand-alone 'android' tool.
         * In this mode, we present an about box, a settings page.
         * For SdkMan2, we also have a menu bar and link to the SDK Manager 2.
         */
        STANDALONE,

        /**
         * The AVD Manager is embedded as a dialog in the SDK Manager
         * or in the {@link AvdSelector}.
         * This is similar to the {@link #STANDALONE} mode except we don't need
         * to display a menu bar at all since we don't want a menu item linking
         * back to the SDK Manager and we don't need to redisplay the options
         * and about which are already on the root window.
         */
        DIALOG,

        /**
         * The AVD Manager is invoked from an IDE.
         * In this mode, we do not modify the menu bar.
         * There is no about box and no settings.
         */
        IDE,
    }


    /**
     * Creates a new window. Caller must call open(), which will block.
     *
     * @param parentShell Parent shell.
     * @param sdkLog Logger. Cannot be null.
     * @param osSdkRoot The OS path to the SDK root.
     * @param context The {@link AvdInvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
     */
    public AvdManagerWindow(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot,
            AvdInvocationContext context) {
        mWindow = new AvdManagerWindowImpl1(
                parentShell,
                sdkLog,
                osSdkRoot,
                context);
    }

    /**
     * Opens the window.
     */
    public void open() {
        mWindow.open();
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/ISdkChangeListener.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/ISdkChangeListener.java
//Synthetic comment -- index 9cca7aa..e221f98 100755

//Synthetic comment -- @@ -1,54 +1,54 @@
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

package com.android.sdkuilib.repository;


/**
 * Interface for listeners on SDK modifications by the SDK Manager UI.
 * This notifies when the SDK manager is first loading the SDK or before/after it installed
 * a package.
 */
public interface ISdkChangeListener {
    /**
     * Invoked when the content of the SDK is being loaded by the SDK Manager UI
     * for the first time.
     * This is generally followed by a call to {@link #onSdkReload()}
     * or by a call to {@link #preInstallHook()}.
     */
    void onSdkLoaded();

    /**
     * Invoked when the SDK Manager UI is about to start installing packages.
     * This will be followed by a call to {@link #postInstallHook()}.
     */
    void preInstallHook();

    /**
     * Invoked when the SDK Manager UI is done installing packages.
     * Some new packages might have been installed or the user might have cancelled the operation.
     * This is generally followed by a call to {@link #onSdkReload()}.
     */
    void postInstallHook();

    /**
     * Invoked when the content of the SDK is being reloaded by the SDK Manager UI,
     * typically after a package was installed. The SDK content might or might not
     * have changed.
     */
    void onSdkReload();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/SdkUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/SdkUpdaterWindow.java
//Synthetic comment -- index 06fc387..d796f47 100755

//Synthetic comment -- @@ -1,112 +1,112 @@
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

package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.sdkman2.SdkUpdaterWindowImpl2;

import org.eclipse.swt.widgets.Shell;

/**
 * Opens an SDK Manager Window.
 *
 * This is the public entry point for using the window.
 */
public class SdkUpdaterWindow {

    /** The actual window implementation to which this class delegates. */
    private ISdkUpdaterWindow mWindow;

    /**
     * Enum giving some indication of what is invoking this window.
     * The behavior and UI will change slightly depending on the context.
     * <p/>
     * Note: if you add Android support to your specific IDE, you might want
     * to specialize this context enum.
     */
    public enum SdkInvocationContext {
        /**
         * The SDK Manager is invoked from the stand-alone 'android' tool.
         * In this mode, we present an about box, a settings page.
         * For SdkMan2, we also have a menu bar and link to the AVD manager.
         */
        STANDALONE,

        /**
         * The SDK Manager is invoked from the standalone AVD Manager.
         * This is similar to the standalone mode except that in this case we
         * don't display a menu item linking to the AVD Manager.
         */
        AVD_MANAGER,

        /**
         * The SDK Manager is invoked from an IDE.
         * In this mode, we do not modify the menu bar. There is no about box
         * and no settings (e.g. HTTP proxy settings are inherited from Eclipse.)
         */
        IDE,

        /**
         * The SDK Manager is invoked from the AVD Selector.
         * For SdkMan1, this means the AVD page will be displayed first.
         * For SdkMan2, we won't be using this.
         */
        AVD_SELECTOR
    }

    /**
     * Creates a new window. Caller must call open(), which will block.
     *
     * @param parentShell Parent shell.
     * @param sdkLog Logger. Cannot be null.
     * @param osSdkRoot The OS path to the SDK root.
     * @param context The {@link SdkInvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
     */
    public SdkUpdaterWindow(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot,
            SdkInvocationContext context) {

        mWindow = new SdkUpdaterWindowImpl2(parentShell, sdkLog, osSdkRoot, context);
    }

    /**
     * Adds a new listener to be notified when a change is made to the content of the SDK.
     * This should be called before {@link #open()}.
     */
    public void addListener(ISdkChangeListener listener) {
        mWindow.addListener(listener);
    }

    /**
     * Removes a new listener to be notified anymore when a change is made to the content of
     * the SDK.
     */
    public void removeListener(ISdkChangeListener listener) {
        mWindow.removeListener(listener);
    }

    /**
     * Opens the window.
     */
    public void open() {
        mWindow.open();
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index f186c84..a23b921 100755

//Synthetic comment -- @@ -1,366 +1,366 @@
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

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.packages.MockToolPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

public class UpdaterLogicTest extends TestCase {

    private static class NullUpdaterData implements IUpdaterData {

        @Override
        public AvdManager getAvdManager() {
            return null;
        }

        @Override
        public ImageFactory getImageFactory() {
            return null;
        }

        @Override
        public ISdkLog getSdkLog() {
            return null;
        }

        @Override
        public DownloadCache getDownloadCache() {
            return null;
        }

        @Override
        public SdkManager getSdkManager() {
            return null;
        }

        @Override
        public SettingsController getSettingsController() {
            return null;
        }

        @Override
        public ITaskFactory getTaskFactory() {
            return null;
        }

        @Override
        public Shell getWindowShell() {
            return null;
        }

    }

    private static class MockUpdaterLogic extends SdkUpdaterLogic {
        private final Package[] mRemotePackages;

        public MockUpdaterLogic(IUpdaterData updaterData, Package[] remotePackages) {
            super(updaterData);
            mRemotePackages = remotePackages;
        }

        @Override
        protected void fetchRemotePackages(Collection<Package> remotePkgs,
                SdkSource[] remoteSources) {
            // Ignore remoteSources and instead uses the remotePackages list given to the
            // constructor.
            if (mRemotePackages != null) {
                remotePkgs.addAll(Arrays.asList(mRemotePackages));
            }
        }
    }

    /**
     * Addon packages depend on a base platform package.
     * This test checks that UpdaterLogic.findPlatformToolsDependency(...)
     * can find the base platform for a given addon.
     */
    public void testFindAddonDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

        MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
        MockPlatformPackage p2 = new MockPlatformPackage(2, 1);

        MockAddonPackage a1 = new MockAddonPackage(p1, 1);
        MockAddonPackage a2 = new MockAddonPackage(p2, 2);

        ArrayList<ArchiveInfo> out = new ArrayList<ArchiveInfo>();
        ArrayList<Archive> selected = new ArrayList<Archive>();
        ArrayList<Package> remote = new ArrayList<Package>();

        // a2 depends on p2, which is not in the locals
        Package[] localPkgs = { p1, a1 };
        ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

        SdkSource[] sources = null;

        // a2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying platform.
        ArchiveInfo fai = mul.findPlatformDependency(a2, out, selected, remote, sources, locals);
        assertNotNull(fai);
        assertNull(fai.getNewArchive());
        assertTrue(fai.isRejected());
        assertEquals(0, out.size());

        // p2 is now selected, and should be scheduled for install in out
        Archive p2_archive = p2.getArchives()[0];
        selected.add(p2_archive);
        ArchiveInfo ai2 = mul.findPlatformDependency(a2, out, selected, remote, sources, locals);
        assertNotNull(ai2);
        assertSame(p2_archive, ai2.getNewArchive());
        assertEquals(1, out.size());
        assertSame(p2_archive, out.get(0).getNewArchive());
    }

    /**
     * Broken add-on packages require an exact platform package to be present or installed.
     * This tests checks that findExactApiLevelDependency() can find a base
     * platform package for a given broken add-on package.
     */
    public void testFindExactApiLevelDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

        MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
        MockPlatformPackage p2 = new MockPlatformPackage(2, 1);

        MockBrokenPackage a1 = new MockBrokenPackage(0, 1);
        MockBrokenPackage a2 = new MockBrokenPackage(0, 2);

        ArrayList<ArchiveInfo> out = new ArrayList<ArchiveInfo>();
        ArrayList<Archive> selected = new ArrayList<Archive>();
        ArrayList<Package> remote = new ArrayList<Package>();

        // a2 depends on p2, which is not in the locals
        Package[] localPkgs = { p1, a1 };
        ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

        SdkSource[] sources = null;

        // a1 depends on p1, which can be found in the locals. p1 is already "installed"
        // so we donn't need to suggest it as a dependency to solve any problem.
        ArchiveInfo found = mul.findExactApiLevelDependency(
                a1, out, selected, remote, sources, locals);
        assertNull(found);

        // a2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying platform.
        found = mul.findExactApiLevelDependency(a2, out, selected, remote, sources, locals);
        assertNotNull(found);
        assertNull(found.getNewArchive());
        assertTrue(found.isRejected());
        assertEquals(0, out.size());

        // p2 is now selected, and should be scheduled for install in out
        Archive p2_archive = p2.getArchives()[0];
        selected.add(p2_archive);
        found = mul.findExactApiLevelDependency(a2, out, selected, remote, sources, locals);
        assertNotNull(found);
        assertSame(p2_archive, found.getNewArchive());
        assertEquals(1, out.size());
        assertSame(p2_archive, out.get(0).getNewArchive());
    }

    /**
     * Platform packages depend on a tool package.
     * This tests checks that UpdaterLogic.findToolsDependency() can find a base
     * tool package for a given platform package.
     */
    public void testFindPlatformDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

        MockPlatformToolPackage pt1 = new MockPlatformToolPackage(1);

        MockToolPackage t1 = new MockToolPackage(1, 1);
        MockToolPackage t2 = new MockToolPackage(2, 1);

        MockPlatformPackage p2 = new MockPlatformPackage(2, 1, 2);

        ArrayList<ArchiveInfo> out = new ArrayList<ArchiveInfo>();
        ArrayList<Archive> selected = new ArrayList<Archive>();
        ArrayList<Package> remote = new ArrayList<Package>();

        // p2 depends on t2, which is not locally installed
        Package[] localPkgs = { t1, pt1 };
        ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

        SdkSource[] sources = null;

        // p2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying tool
        ArchiveInfo fai = mul.findToolsDependency(p2, out, selected, remote, sources, locals);
        assertNotNull(fai);
        assertNull(fai.getNewArchive());
        assertTrue(fai.isRejected());
        assertEquals(0, out.size());

        // t2 is now selected and can be used as a dependency
        Archive t2_archive = t2.getArchives()[0];
        selected.add(t2_archive);
        ArchiveInfo ai2 = mul.findToolsDependency(p2, out, selected, remote, sources, locals);
        assertNotNull(ai2);
        assertSame(t2_archive, ai2.getNewArchive());
        assertEquals(1, out.size());
        assertSame(t2_archive, out.get(0).getNewArchive());
    }

    /**
     * Tool packages require a platform-tool package to be present or installed.
     * This tests checks that UpdaterLogic.findPlatformToolsDependency() can find a base
     * platform-tool package for a given tool package.
     */
    public void testFindPlatformToolDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

        MockPlatformToolPackage t1 = new MockPlatformToolPackage(1);
        MockPlatformToolPackage t2 = new MockPlatformToolPackage(2);

        MockToolPackage p2 = new MockToolPackage(2, 2);

        ArrayList<ArchiveInfo> out = new ArrayList<ArchiveInfo>();
        ArrayList<Archive> selected = new ArrayList<Archive>();
        ArrayList<Package> remote = new ArrayList<Package>();

        // p2 depends on t2, which is not locally installed
        Package[] localPkgs = { t1 };
        ArchiveInfo[] locals = mul.createLocalArchives(localPkgs);

        SdkSource[] sources = null;

        // p2 now depends on a "fake" archive info with no newArchive that wraps the missing
        // underlying tool
        ArchiveInfo fai = mul.findPlatformToolsDependency(
                                    p2, out, selected, remote, sources, locals);
        assertNotNull(fai);
        assertNull(fai.getNewArchive());
        assertTrue(fai.isRejected());
        assertEquals(0, out.size());

        // t2 is now selected and can be used as a dependency
        Archive t2_archive = t2.getArchives()[0];
        selected.add(t2_archive);
        ArchiveInfo ai2 = mul.findPlatformToolsDependency(
                                    p2, out, selected, remote, sources, locals);
        assertNotNull(ai2);
        assertSame(t2_archive, ai2.getNewArchive());
        assertEquals(1, out.size());
        assertSame(t2_archive, out.get(0).getNewArchive());
    }

    public void testComputeRevisionUpdate() {
        // Scenario:
        // - user has tools rev 7 installed + plat-tools rev 1 installed
        // - server has tools rev 8, depending on plat-tools rev 2
        // - server has tools rev 9, depending on plat-tools rev 3
        // - server has platform 9 that requires min-tools-rev 9
        //
        // If we do an update all, we want to the installer to pick up:
        // - the new platform 9
        // - the tools rev 9 (required by platform 9)
        // - the plat-tools rev 3 (required by tools rev 9)

        final MockPlatformToolPackage pt1 = new MockPlatformToolPackage(1);
        final MockPlatformToolPackage pt2 = new MockPlatformToolPackage(2);
        final MockPlatformToolPackage pt3 = new MockPlatformToolPackage(3);

        final MockToolPackage t7 = new MockToolPackage(7, 1 /*min-plat-tools*/);
        final MockToolPackage t8 = new MockToolPackage(8, 2 /*min-plat-tools*/);
        final MockToolPackage t9 = new MockToolPackage(9, 3 /*min-plat-tools*/);

        final MockPlatformPackage p9 = new MockPlatformPackage(9, 1, 9 /*min-tools*/);

        // Note: the mock updater logic gets the remotes packages from the array given
        // here and bypasses the source (to avoid fetching any actual URLs)
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(),
                new Package[] { t8, pt2, t9, pt3, p9 });

        SdkSources sources = new SdkSources();
        Package[] localPkgs = { t7, pt1 };

        List<ArchiveInfo> selected = mul.computeUpdates(
                null /*selectedArchives*/,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9]",
                Arrays.toString(selected.toArray()));

        mul.addNewPlatforms(
                selected,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9, " +
                 "SDK Platform Android android-9, API 9, revision 1]",
                Arrays.toString(selected.toArray()));

        // Now try again but reverse the order of the remote package list.

        mul = new MockUpdaterLogic(new NullUpdaterData(),
                new Package[] { p9, t9, pt3, t8, pt2 });

        selected = mul.computeUpdates(
                null /*selectedArchives*/,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9]",
                Arrays.toString(selected.toArray()));

        mul.addNewPlatforms(
                selected,
                sources,
                localPkgs,
                false /*includeObsoletes*/);

        assertEquals(
                "[Android SDK Platform-tools, revision 3, " +
                 "Android SDK Tools, revision 9, " +
                 "SDK Platform Android android-9, API 9, revision 1]",
                Arrays.toString(selected.toArray()));
    }
}







