/*Update export wizard for new release export.

Use the ExportHelper to fully rebuild the release
package as taking the debug package no longer works
(because it inserts debuggable=true).

Also clean up the ApkSettings thing that's not
used anymore.

Change-Id:Ia47c316df80f614d6c43fb5c625ccd859c3baa37*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 9cdc5a0..ee5b850 100644

//Synthetic comment -- @@ -139,6 +139,8 @@
null); //resourceMarker

// success!
} catch (Exception e) {
//?
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java
//Synthetic comment -- index 6322dfd..31a68e8 100644

//Synthetic comment -- @@ -18,16 +18,13 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.sdklib.internal.build.KeystoreHelper;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
//Synthetic comment -- @@ -46,7 +43,6 @@
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//Synthetic comment -- @@ -57,9 +53,6 @@
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
* Export wizard to export an apk signed with a release key/certificate.
//Synthetic comment -- @@ -77,7 +70,6 @@
static final String PROPERTY_KEYSTORE = "keystore"; //$NON-NLS-1$
static final String PROPERTY_ALIAS = "alias"; //$NON-NLS-1$
static final String PROPERTY_DESTINATION = "destination"; //$NON-NLS-1$
    static final String PROPERTY_FILENAME = "baseFilename"; //$NON-NLS-1$

static final int APK_FILE_SOURCE = 0;
static final int APK_FILE_DEST = 1;
//Synthetic comment -- @@ -171,7 +163,7 @@
private PrivateKey mPrivateKey;
private X509Certificate mCertificate;

    private File mDestinationParentFolder;

private ExportWizardPage mKeystoreSelectionPage;
private ExportWizardPage mKeyCreationPage;
//Synthetic comment -- @@ -182,8 +174,6 @@

private List<String> mExistingAliases;

    private Map<String, String[]> mApkMap;

public ExportWizard() {
setHelpAvailable(false); // TODO have help
setWindowTitle("Export Android Application");
//Synthetic comment -- @@ -206,9 +196,7 @@
ProjectHelper.saveStringProperty(mProject, PROPERTY_KEYSTORE, mKeystore);
ProjectHelper.saveStringProperty(mProject, PROPERTY_ALIAS, mKeyAlias);
ProjectHelper.saveStringProperty(mProject, PROPERTY_DESTINATION,
                mDestinationParentFolder.getAbsolutePath());
        ProjectHelper.saveStringProperty(mProject, PROPERTY_FILENAME,
                mApkMap.get(null)[APK_FILE_DEST]);

// run the export in an UI runnable.
IWorkbench workbench = PlatformUI.getWorkbench();
//Synthetic comment -- @@ -240,9 +228,6 @@

private boolean doExport(IProgressMonitor monitor) {
try {
            // first we make sure the project is built
            mProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);

// if needed, create the keystore and/or key.
if (mKeystoreCreationMode || mKeyCreationMode) {
final ArrayList<String> output = new ArrayList<String>();
//Synthetic comment -- @@ -290,68 +275,28 @@

// check the private key/certificate again since it may have been created just above.
if (mPrivateKey != null && mCertificate != null) {
                // get the output folder of the project to export.
                // this is where we'll find the built apks to resign and export.
                IFolder outputIFolder = BaseProjectHelper.getOutputFolder(mProject);
                if (outputIFolder == null) {
                    return false;
                }
                String outputOsPath =  outputIFolder.getLocation().toOSString();

                // now generate the packages.
                Set<Entry<String, String[]>> set = mApkMap.entrySet();

boolean runZipAlign = false;
String path = AdtPlugin.getOsAbsoluteZipAlign();
File zipalign = new File(path);
runZipAlign = zipalign.isFile();

                for (Entry<String, String[]> entry : set) {
                    String[] defaultApk = entry.getValue();
                    String srcFilename = defaultApk[APK_FILE_SOURCE];
                    String destFilename = defaultApk[APK_FILE_DEST];
                    File destFile;
                    if (runZipAlign) {
                        destFile = File.createTempFile("android", ".apk");
                    } else {
                        destFile = new File(mDestinationParentFolder, destFilename);
                    }


                    FileOutputStream fos = new FileOutputStream(destFile);
                    SignedJarBuilder builder = new SignedJarBuilder(fos, mPrivateKey, mCertificate);

                    // get the input file.
                    FileInputStream fis = new FileInputStream(new File(outputOsPath, srcFilename));

                    // add the content of the source file to the output file, and sign it at
                    // the same time.
                    try {
                        builder.writeZip(fis, null /* filter */);
                        // close the builder: write the final signature files,
                        // and close the archive.
                        builder.close();

                        // now zipalign the result
                        if (runZipAlign) {
                            File realDestFile = new File(mDestinationParentFolder, destFilename);
                            String message = zipAlign(path, destFile, realDestFile);
                            if (message != null) {
                                displayError(message);
                                return false;
                            }
                        }
                    } finally {
                        try {
                            fis.close();
                        } finally {
                            fos.close();
                        }
                    }
}

                // export success. In case we didn't run ZipAlign we display a warning
                if (runZipAlign == false) {
AdtPlugin.displayWarning("Export Wizard",
"The zipalign tool was not found in the SDK.\n\n" +
"Please update to the latest SDK and re-export your application\n" +
//Synthetic comment -- @@ -375,10 +320,9 @@
// a private key/certificate or the creation mode. In creation mode, unless
// all the key/keystore info is valid, the user cannot reach the last page, so there's
// no need to check them again here.
        return mApkMap != null && mApkMap.size() > 0 &&
                ((mPrivateKey != null && mCertificate != null)
|| mKeystoreCreationMode || mKeyCreationMode) &&
                mDestinationParentFolder != null;
}

/*
//Synthetic comment -- @@ -531,14 +475,12 @@
mCertificate = certificate;
}

    void setDestination(File parentFolder, Map<String, String[]> apkMap) {
        mDestinationParentFolder = parentFolder;
        mApkMap = apkMap;
}

void resetDestination() {
        mDestinationParentFolder = null;
        mApkMap = null;
}

void updatePageOnChange(int changeMask) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCheckPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCheckPage.java
//Synthetic comment -- index d247b78..5c2a3cd 100644

//Synthetic comment -- @@ -17,10 +17,7 @@
package com.android.ide.eclipse.adt.internal.wizards.export;

import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.wizards.export.ExportWizard.ExportWizardPage;
import com.android.sdklib.internal.project.ApkSettings;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -54,10 +51,6 @@
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
* Final page of the wizard that checks the key and ask for the ouput location.
//Synthetic comment -- @@ -72,8 +65,6 @@
private FormText mDetailText;
private ScrolledComposite mScrolledComposite;

    private ApkSettings mApkSettings;

private String mKeyDetails;
private String mDestinationDetails;

//Synthetic comment -- @@ -151,17 +142,11 @@
if ((mProjectDataChanged & DATA_PROJECT) != 0) {
// reset the destination from the content of the project
IProject project = mWizard.getProject();
            ProjectState state = Sdk.getProjectState(project);
            if (state != null) {
                mApkSettings = state.getApkSettings();
            }

String destination = ProjectHelper.loadStringProperty(project,
ExportWizard.PROPERTY_DESTINATION);
            String filename = ProjectHelper.loadStringProperty(project,
                    ExportWizard.PROPERTY_FILENAME);
            if (destination != null && filename != null) {
                mDestination.setText(destination + File.separator + filename);
}
}

//Synthetic comment -- @@ -322,44 +307,15 @@
return;
}

            // display the list of files that will actually be created
            Map<String, String[]> apkFileMap = getApkFileMap(file);

            // display them
            boolean fileExists = false;
            StringBuilder sb = new StringBuilder(String.format(
                    "<p>This will create the following files:</p>"));

            Set<Entry<String, String[]>> set = apkFileMap.entrySet();
            for (Entry<String, String[]> entry : set) {
                String[] apkArray = entry.getValue();
                String filename = apkArray[ExportWizard.APK_FILE_DEST];
                File f = new File(parentFolder, filename);
                if (f.isFile()) {
                    fileExists = true;
                    sb.append(String.format("<li>%1$s (WARNING: already exists)</li>", filename));
                } else if (f.isDirectory()) {
                    setErrorMessage(String.format("%1$s is a directory.", filename));
                    // reset canFinish in the wizard.
                    mWizard.resetDestination();
                    setPageComplete(false);
                    return;
                } else {
                    sb.append(String.format("<li>%1$s</li>", filename));
                }
}

            mDestinationDetails = sb.toString();

// no error, set the destination in the wizard.
            mWizard.setDestination(parentFolder, apkFileMap);
setPageComplete(true);

            // However, we should also test if the file already exists.
            if (fileExists) {
                setMessage("A destination file already exists.", WARNING);
            }

updateDetailText();
} else if (forceDetailUpdate) {
updateDetailText();
//Synthetic comment -- @@ -398,29 +354,6 @@
updateScrolling();
}

    /**
     * Creates the list of destination filenames based on the content of the destination field
     * and the list of APK configurations for the project.
     *
     * @param file File name from the destination field
     * @return A list of destination filenames based <code>file</code> and the list of APK
     *         configurations for the project.
     */
    private Map<String, String[]> getApkFileMap(File file) {
        String filename = file.getName();

        HashMap<String, String[]> map = new HashMap<String, String[]>();

        // add the default APK filename
        String[] apkArray = new String[ExportWizard.APK_COUNT];
        apkArray[ExportWizard.APK_FILE_SOURCE] = ProjectHelper.getApkFilename(
                mWizard.getProject(), null /*config*/);
        apkArray[ExportWizard.APK_FILE_DEST] = filename;
        map.put(null, apkArray);

        return map;
    }

@Override
protected void onException(Throwable t) {
super.onException(t);







