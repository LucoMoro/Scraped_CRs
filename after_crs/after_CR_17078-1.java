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
        } catch (CoreException e) {
            throw e;
} catch (Exception e) {
//?
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ExportWizard.java
//Synthetic comment -- index 6322dfd..31a68e8 100644

//Synthetic comment -- @@ -18,16 +18,13 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.ExportHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.sdklib.internal.build.KeystoreHelper;
import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
//Synthetic comment -- @@ -46,7 +43,6 @@
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//Synthetic comment -- @@ -57,9 +53,6 @@
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
* Export wizard to export an apk signed with a release key/certificate.
//Synthetic comment -- @@ -77,7 +70,6 @@
static final String PROPERTY_KEYSTORE = "keystore"; //$NON-NLS-1$
static final String PROPERTY_ALIAS = "alias"; //$NON-NLS-1$
static final String PROPERTY_DESTINATION = "destination"; //$NON-NLS-1$

static final int APK_FILE_SOURCE = 0;
static final int APK_FILE_DEST = 1;
//Synthetic comment -- @@ -171,7 +163,7 @@
private PrivateKey mPrivateKey;
private X509Certificate mCertificate;

    private File mDestinationFile;

private ExportWizardPage mKeystoreSelectionPage;
private ExportWizardPage mKeyCreationPage;
//Synthetic comment -- @@ -182,8 +174,6 @@

private List<String> mExistingAliases;

public ExportWizard() {
setHelpAvailable(false); // TODO have help
setWindowTitle("Export Android Application");
//Synthetic comment -- @@ -206,9 +196,7 @@
ProjectHelper.saveStringProperty(mProject, PROPERTY_KEYSTORE, mKeystore);
ProjectHelper.saveStringProperty(mProject, PROPERTY_ALIAS, mKeyAlias);
ProjectHelper.saveStringProperty(mProject, PROPERTY_DESTINATION,
                mDestinationFile.getAbsolutePath());

// run the export in an UI runnable.
IWorkbench workbench = PlatformUI.getWorkbench();
//Synthetic comment -- @@ -240,9 +228,6 @@

private boolean doExport(IProgressMonitor monitor) {
try {
// if needed, create the keystore and/or key.
if (mKeystoreCreationMode || mKeyCreationMode) {
final ArrayList<String> output = new ArrayList<String>();
//Synthetic comment -- @@ -290,68 +275,28 @@

// check the private key/certificate again since it may have been created just above.
if (mPrivateKey != null && mCertificate != null) {
boolean runZipAlign = false;
String path = AdtPlugin.getOsAbsoluteZipAlign();
File zipalign = new File(path);
runZipAlign = zipalign.isFile();

                File apkExportFile = mDestinationFile;
                if (runZipAlign) {
                    // create a temp file for the original export.
                    apkExportFile = File.createTempFile("androidExport_", ".apk");
}

                // export the signed apk.
                ExportHelper.export(mProject, apkExportFile, mPrivateKey, mCertificate, monitor);

                // align if we can
                if (runZipAlign) {
                    String message = zipAlign(path, apkExportFile, mDestinationFile);
                    if (message != null) {
                        displayError(message);
                        return false;
                    }
                } else {
AdtPlugin.displayWarning("Export Wizard",
"The zipalign tool was not found in the SDK.\n\n" +
"Please update to the latest SDK and re-export your application\n" +
//Synthetic comment -- @@ -375,10 +320,9 @@
// a private key/certificate or the creation mode. In creation mode, unless
// all the key/keystore info is valid, the user cannot reach the last page, so there's
// no need to check them again here.
        return ((mPrivateKey != null && mCertificate != null)
|| mKeystoreCreationMode || mKeyCreationMode) &&
                mDestinationFile != null;
}

/*
//Synthetic comment -- @@ -531,14 +475,12 @@
mCertificate = certificate;
}

    void setDestination(File destinationFile) {
        mDestinationFile = destinationFile;
}

void resetDestination() {
        mDestinationFile = null;
}

void updatePageOnChange(int changeMask) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCheckPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/KeyCheckPage.java
//Synthetic comment -- index d247b78..5c2a3cd 100644

//Synthetic comment -- @@ -17,10 +17,7 @@
package com.android.ide.eclipse.adt.internal.wizards.export;

import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.wizards.export.ExportWizard.ExportWizardPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -54,10 +51,6 @@
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;

/**
* Final page of the wizard that checks the key and ask for the ouput location.
//Synthetic comment -- @@ -72,8 +65,6 @@
private FormText mDetailText;
private ScrolledComposite mScrolledComposite;

private String mKeyDetails;
private String mDestinationDetails;

//Synthetic comment -- @@ -151,17 +142,11 @@
if ((mProjectDataChanged & DATA_PROJECT) != 0) {
// reset the destination from the content of the project
IProject project = mWizard.getProject();

String destination = ProjectHelper.loadStringProperty(project,
ExportWizard.PROPERTY_DESTINATION);
            if (destination != null) {
                mDestination.setText(destination);
}
}

//Synthetic comment -- @@ -322,44 +307,15 @@
return;
}

            if (file.isFile()) {
                mDestinationDetails = "<li>WARNING: destination file already exists</li>";
                setMessage("Destination file already exists.", WARNING);
}

// no error, set the destination in the wizard.
            mWizard.setDestination(file);
setPageComplete(true);

updateDetailText();
} else if (forceDetailUpdate) {
updateDetailText();
//Synthetic comment -- @@ -398,29 +354,6 @@
updateScrolling();
}

@Override
protected void onException(Throwable t) {
super.onException(t);







