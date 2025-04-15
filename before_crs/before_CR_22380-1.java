/*Change logging API used by ITaskMonitor.

ITaskMonitor is used by the SDK Manager to display
status information and progress bar for asynchronous
tasks (e.g. fetching sources, downloading and installing
packages).

This changes the way text is logged by the monitor.
There used to be one setResult() method which historically
was designed to report 1 final message (e.g. "Install
completed") but then this was actually used to add
ongoing logging. So in this change the monitor has
3 replacement methods: log, logError and logVerbose,
which gives us more flexibility in controlling what
gets displayed.

As a side effect, this fixes unit-tests from SdkLib
that relied on previous output that changed in a
recent CL.

Change-Id:I0fa41d59db8f5eea478b88208695ef07e246ba30*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AdbWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AdbWrapper.java
//Synthetic comment -- index 3fab9ce..ba2d501 100755

//Synthetic comment -- @@ -54,7 +54,11 @@
}

private void display(String format, Object...args) {
        mMonitor.setResult(format, args);
}

/**
//Synthetic comment -- @@ -63,7 +67,7 @@
*/
public synchronized boolean startAdb() {
if (mAdbOsLocation == null) {
            display("Error: missing path to ADB."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -82,15 +86,15 @@
false /* waitForReaders */);

} catch (IOException ioe) {
            display("Unable to run 'adb': %1$s.", ioe.getMessage()); //$NON-NLS-1$
// we'll return false;
} catch (InterruptedException ie) {
            display("Unable to run 'adb': %1$s.", ie.getMessage()); //$NON-NLS-1$
// we'll return false;
}

if (status != 0) {
            display("'adb start-server' failed."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -105,7 +109,7 @@
*/
public synchronized boolean stopAdb() {
if (mAdbOsLocation == null) {
            display("Error: missing path to ADB."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -127,7 +131,7 @@
}

if (status != 0) {
            display("'adb kill-server' failed -- run manually if necessary."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -163,7 +167,7 @@
while (true) {
String line = errReader.readLine();
if (line != null) {
                            display("ADB Error: %1$s", line);
errorOutput.add(line);
} else {
break;
//Synthetic comment -- @@ -185,7 +189,7 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            display("ADB: %1$s", line);
stdOutput.add(line);
} else {
break;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index 9b8d808..c0b7041 100755

//Synthetic comment -- @@ -142,11 +142,11 @@
reason = String.format("Unknown (%1$s)", exception[0].getClass().getName());
}

            monitor.setResult("Failed to fetch URL %1$s, reason: %2$s", url, reason);
}

if (validationError[0] != null) {
            monitor.setResult("%s", validationError[0]);  //$NON-NLS-1$
}

// Stop here if we failed to validate the XML. We don't want to load it.
//Synthetic comment -- @@ -409,13 +409,13 @@

return doc;
} catch (ParserConfigurationException e) {
            monitor.setResult("Failed to create XML document builder");

} catch (SAXException e) {
            monitor.setResult("Failed to parse XML document");

} catch (IOException e) {
            monitor.setResult("Failed to read XML document");
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index 4d5d616..5807ca3 100755

//Synthetic comment -- @@ -63,7 +63,7 @@
String name = pkg.getShortDescription();

if (pkg instanceof ExtraPackage && !((ExtraPackage) pkg).isPathValid()) {
            monitor.setResult("Skipping %1$s: %2$s is not a valid install path.",
name,
((ExtraPackage) pkg).getPath());
return false;
//Synthetic comment -- @@ -71,14 +71,14 @@

if (archive.isLocal()) {
// This should never happen.
            monitor.setResult("Skipping already installed archive: %1$s for %2$s",
name,
archive.getOsDescription());
return false;
}

if (!archive.isCompatible()) {
            monitor.setResult("Skipping incompatible archive: %1$s for %2$s",
name,
archive.getOsDescription());
return false;
//Synthetic comment -- @@ -88,7 +88,7 @@
if (archiveFile != null) {
// Unarchive calls the pre/postInstallHook methods.
if (unarchive(archive, osSdkRoot, archiveFile, sdkManager, monitor)) {
                monitor.setResult("Installed %1$s", name);
// Delete the temp archive if it exists, only on success
OsHelper.deleteFileOrFolder(archiveFile);
return true;
//Synthetic comment -- @@ -110,7 +110,7 @@
String name = archive.getParentPackage().getShortDescription();
String desc = String.format("Downloading %1$s", name);
monitor.setDescription(desc);
        monitor.setResult(desc);

String link = archive.getUrl();
if (!link.startsWith("http://")                          //$NON-NLS-1$
//Synthetic comment -- @@ -120,7 +120,7 @@
Package pkg = archive.getParentPackage();
SdkSource src = pkg.getParentSource();
if (src == null) {
                monitor.setResult("Internal error: no source for archive %1$s", name);
return null;
}

//Synthetic comment -- @@ -152,7 +152,7 @@
OsHelper.deleteFileOrFolder(tmpFolder);
}
if (!tmpFolder.mkdirs()) {
                monitor.setResult("Failed to create directory %1$s", tmpFolder.getPath());
return null;
}
}
//Synthetic comment -- @@ -214,10 +214,10 @@

} catch (FileNotFoundException e) {
// The FNF message is just the URL. Make it a bit more useful.
            monitor.setResult("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.setResult(e.getMessage());

} finally {
if (is != null) {
//Synthetic comment -- @@ -327,14 +327,15 @@
}

if (monitor.isCancelRequested()) {
                    monitor.setResult("Download aborted by user at %1$d bytes.", total);
return false;
}

}

if (total != size) {
                monitor.setResult("Download finished with wrong size. Expected %1$d bytes, got %2$d bytes.",
size, total);
return false;
}
//Synthetic comment -- @@ -343,7 +344,7 @@
String actual   = getDigestChecksum(digester);
String expected = archive.getChecksum();
if (!actual.equalsIgnoreCase(expected)) {
                monitor.setResult("Download finished with wrong checksum. Expected %1$s, got %2$s.",
expected, actual);
return false;
}
//Synthetic comment -- @@ -352,10 +353,10 @@

} catch (FileNotFoundException e) {
// The FNF message is just the URL. Make it a bit more useful.
            monitor.setResult("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.setResult(e.getMessage());

} finally {
if (os != null) {
//Synthetic comment -- @@ -391,7 +392,7 @@
String pkgName = pkg.getShortDescription();
String pkgDesc = String.format("Installing %1$s", pkgName);
monitor.setDescription(pkgDesc);
        monitor.setResult(pkgDesc);

// Ideally we want to always unzip in a temp folder which name depends on the package
// type (e.g. addon, tools, etc.) and then move the folder to the destination folder.
//Synthetic comment -- @@ -432,12 +433,12 @@

if (destFolder == null) {
// this should not seriously happen.
                monitor.setResult("Failed to compute installation directory for %1$s.", pkgName);
return false;
}

if (!pkg.preInstallHook(archive, monitor, osSdkRoot, destFolder)) {
                monitor.setResult("Skipping archive: %1$s", pkgName);
return false;
}

//Synthetic comment -- @@ -450,14 +451,14 @@
}
if (oldDestFolder == null) {
// this should not seriously happen.
                    monitor.setResult("Failed to find a temp directory in %1$s.", osSdkRoot);
return false;
}

// Try to move the current dest dir to the temp/old one. Tell the user if it failed.
while(true) {
if (!moveFolder(destFolder, oldDestFolder)) {
                        monitor.setResult("Failed to rename directory %1$s to %2$s.",
destFolder.getPath(), oldDestFolder.getPath());

if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
//Synthetic comment -- @@ -489,7 +490,7 @@
// -2- Unzip new content directly in place.

if (!destFolder.mkdirs()) {
                monitor.setResult("Failed to create directory %1$s", destFolder.getPath());
return false;
}

//Synthetic comment -- @@ -498,7 +499,7 @@
}

if (!generateSourceProperties(archive, destFolder)) {
                monitor.setResult("Failed to generate source.properties in directory %1$s",
destFolder.getPath());
return false;
}
//Synthetic comment -- @@ -627,7 +628,7 @@
// Create directory if it doesn't exist yet. This allows us to create
// empty directories.
if (!destFile.isDirectory() && !destFile.mkdirs()) {
                        monitor.setResult("Failed to create temp directory %1$s",
destFile.getPath());
return false;
}
//Synthetic comment -- @@ -638,7 +639,7 @@
File parentDir = destFile.getParentFile();
if (!parentDir.isDirectory()) {
if (!parentDir.mkdirs()) {
                            monitor.setResult("Failed to create temp directory %1$s",
parentDir.getPath());
return false;
}
//Synthetic comment -- @@ -689,7 +690,7 @@
return true;

} catch (IOException e) {
            monitor.setResult("Unzip failed: %1$s", e.getMessage());

} finally {
if (zipFile != null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskMonitor.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskMonitor.java
//Synthetic comment -- index e08e27c..7329d03 100755

//Synthetic comment -- @@ -27,6 +27,21 @@
* If the task runs in a non-UI worker thread, the task factory implementation
* will take care of the update the UI in the correct thread. The task itself
* must not have to deal with it.
*/
public interface ITaskMonitor {

//Synthetic comment -- @@ -34,13 +49,26 @@
* Sets the description in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void setDescription(String descriptionFormat, Object...args);

/**
     * Sets the result text in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void setResult(String resultFormat, Object...args);

/**
* Sets the max value of the progress bar.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 6fc26e3..22b9c42 100755

//Synthetic comment -- @@ -303,7 +303,7 @@
if (usingAlternateUrl && validatedDoc != null) {
// If the second tentative succeeded, indicate it in the console
// with the URL that worked.
                            monitor.setResult("Repository found at %1$s", url);

// Keep the modified URL
mUrl = url;
//Synthetic comment -- @@ -388,11 +388,11 @@
reason = String.format("Unknown (%1$s)", exception[0].getClass().getName());
}

            monitor.setResult("Failed to fetch URL %1$s, reason: %2$s", url, reason);
}

if (validationError[0] != null) {
            monitor.setResult("%s", validationError[0]);  //$NON-NLS-1$
}

// Stop here if we failed to validate the XML. We don't want to load it.
//Synthetic comment -- @@ -748,16 +748,11 @@

if (p != null) {
packages.add(p);
                            // TODO: change this to a monitor.print() in sdkman2. In between
                            // simply remove this which isn't very useful since it hides
                            // which source is being loaded in the progress dialog.
                            // Note that this breaks unit tests that depend on this output.
                            // monitor.setDescription("Found %1$s", p.getShortDescription());
}
} catch (Exception e) {
// Ignore invalid packages
                        monitor.setResult("Ignoring invalid %1$s element: %2$s",
                                name, e.toString());
}
}
}
//Synthetic comment -- @@ -806,13 +801,13 @@

return doc;
} catch (ParserConfigurationException e) {
            monitor.setResult("Failed to create XML document builder");

} catch (SAXException e) {
            monitor.setResult("Failed to parse XML document");

} catch (IOException e) {
            monitor.setResult("Failed to read XML document");
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index 827c721..69039ea 100755

//Synthetic comment -- @@ -263,17 +263,17 @@
status = grabProcessOutput(proc, monitor, scriptName);

} catch (Exception e) {
            monitor.setResult("Exception: %s", e.toString());
}

if (status != 0) {
            monitor.setResult("Failed to execute %s", scriptName);
return;
}
}

/**
     * Get the stderr/stdout outputs of a process and return when the process is done.
* Both <b>must</b> be read or the process will block on windows.
* @param process The process to get the ouput from.
* @param monitor The monitor where to output errors.
//Synthetic comment -- @@ -298,7 +298,7 @@
while (true) {
String line = errReader.readLine();
if (line != null) {
                            monitor.setResult("[%1$s] Error: %2$s", scriptName, line);
} else {
break;
}
//Synthetic comment -- @@ -319,7 +319,7 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            monitor.setResult("[%1$s] %2$s", scriptName, line);
} else {
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/AddonsListFetcherTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/AddonsListFetcherTest.java
//Synthetic comment -- index 3f2bb84..3a18a12 100755

//Synthetic comment -- @@ -104,7 +104,7 @@
Site[] result = mFetcher._parseAddonsList(doc, uri, monitor);

assertEquals("", monitor.getCapturedDescriptions());
        assertEquals("", monitor.getCapturedResults());

// check the sites we found...
assertEquals(3, result.length);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java
//Synthetic comment -- index 56a7c6c..3b7e608 100755

//Synthetic comment -- @@ -24,26 +24,44 @@
*/
public class MockMonitor implements ITaskMonitor {

    String mCapturedResults = "";
    String mCapturedDescriptions = "";

    public String getCapturedResults() {
        return mCapturedResults;
}

public String getCapturedDescriptions() {
return mCapturedDescriptions;
}

    public void setResult(String resultFormat, Object... args) {
        mCapturedResults += String.format(resultFormat, args) + "\n";
}

public void setProgressMax(int max) {
}

    public void setDescription(String descriptionFormat, Object... args) {
        mCapturedDescriptions += String.format(descriptionFormat, args) + "\n";
}

public boolean isCancelRequested() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
//Synthetic comment -- index ea6c4f6..6df440e 100755

//Synthetic comment -- @@ -176,8 +176,9 @@
"Found G USB Driver package, revision 43 (Obsolete)\n" +
"Found Android Vendor Extra API Dep package, revision 2 (Obsolete)\n" +
"Found Unkown Extra package, revision 2 (Obsolete)\n",
                monitor.getCapturedDescriptions());
        assertEquals("", monitor.getCapturedResults());

// check the packages we found... we expected to find 11 packages with each at least
// one archive.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index aee811f..24a6fb6 100755

//Synthetic comment -- @@ -141,14 +141,15 @@

Document result = mSource._findAlternateToolsXml(xmlStream);
assertNotNull(result);
        MockMonitor mon = new MockMonitor();
        assertTrue(mSource._parsePackages(result, SdkRepoConstants.NS_URI, mon));

assertEquals("Found Android SDK Tools, revision 1\n" +
"Found Android SDK Tools, revision 42\n" +
"Found Android SDK Platform-tools, revision 3\n",
                mon.getCapturedDescriptions());
        assertEquals("", mon.getCapturedResults());

// check the packages we found... we expected to find 2 tool packages and 1
// platform-tools package, with at least 1 archive each.
//Synthetic comment -- @@ -199,8 +200,9 @@
"Found Android SDK Tools, revision 42\n" +
"Found This add-on has no libraries by Joe Bar, Android API 4, revision 3\n" +
"Found Usb Driver package, revision 43\n",
                monitor.getCapturedDescriptions());
        assertEquals("", monitor.getCapturedResults());

// check the packages we found... we expected to find 11 packages with each at least
// one archive.
//Synthetic comment -- @@ -277,8 +279,9 @@
"Found Usb Driver package, revision 43 (Obsolete)\n" +
"Found Extra API Dep package, revision 2 (Obsolete)\n" +
"Found Samples for SDK API 14, revision 24 (Obsolete)\n",
                monitor.getCapturedDescriptions());
        assertEquals("", monitor.getCapturedResults());

// check the packages we found... we expected to find 13 packages with each at least
// one archive.
//Synthetic comment -- @@ -353,8 +356,9 @@
"Found A USB Driver package, revision 43 (Obsolete)\n" +
"Found Android Vendor Extra API Dep package, revision 2 (Obsolete)\n" +
"Found Samples for SDK API 14, revision 24 (Obsolete)\n",
                monitor.getCapturedDescriptions());
        assertEquals("", monitor.getCapturedResults());

// check the packages we found... we expected to find 13 packages with each at least
// one archive.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index ddf1e2e..6d5b5d4 100755

//Synthetic comment -- @@ -174,45 +174,52 @@
/**
* Sets the description in the current task dialog.
*/
        public void setDescription(String descriptionFormat, Object...args) {

String last = mLastDesc;
            String line = String.format("  " + descriptionFormat, args);            //$NON-NLS-1$

// If the description contains a %, it generally indicates a recurring
// progress so we want a \r at the end.
            if (line.indexOf('%') > -1) {
                if (mLastProgressBase != null && line.startsWith(mLastProgressBase)) {
                    line = "    " + line.substring(mLastProgressBase.length());     //$NON-NLS-1$
}
line += '\r';
} else {
                mLastProgressBase = line;
line += '\n';
}

// Skip line if it's the same as the last one.
            if (last != null && last.equals(line)) {
return;
}
            mLastDesc = line;

// If the last line terminated with a \r but the new one doesn't, we need to
// insert a \n to avoid erasing the previous line.
if (last != null &&
                    last.endsWith("\r") &&
                    !line.endsWith("\r")) {
line = '\n' + line;
}

mSdkLog.printf("%s", line);                                             //$NON-NLS-1$
}

        /**
         * Sets the description in the current task dialog.
         */
        public void setResult(String resultFormat, Object...args) {
            setDescription(resultFormat, args);
}

/**
//Synthetic comment -- @@ -331,12 +338,20 @@
return mRoot.isCancelRequested();
}

        public void setDescription(String descriptionFormat, Object... args) {
            mRoot.setDescription(descriptionFormat, args);
}

        public void setResult(String resultFormat, Object... args) {
            mRoot.setResult(resultFormat, args);
}

public void setProgressMax(int max) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 04cc947..1a3b5cb 100755

//Synthetic comment -- @@ -438,14 +438,14 @@
// This archive depends on a missing archive.
// We shouldn't get here.
// Skip it.
                                    monitor.setResult("Skipping '%1$s'; it depends on a missing package.",
archive.getParentPackage().getShortDescription());
continue nextArchive;
} else if (!installedArchives.contains(na)) {
// This archive depends on another one that was not installed.
// We shouldn't get here.
// Skip it.
                                    monitor.setResult("Skipping '%1$s'; it depends on '%2$s' which was not installed.",
archive.getParentPackage().getShortDescription(),
adep.getShortDescription());
continue nextArchive;
//Synthetic comment -- @@ -500,7 +500,7 @@
baos.toString());
}

                        monitor.setResult(msg);
mSdkLog.error(t, msg);
} finally {

//Synthetic comment -- @@ -515,10 +515,10 @@
// Update the USB vendor ids for adb
try {
mSdkManager.updateAdb();
                        monitor.setResult("Updated ADB to support the USB devices declared in the SDK add-ons.");
} catch (Exception e) {
mSdkLog.error(e, "Update ADB failed");
                        monitor.setResult("failed to update adb to support the USB devices declared in the SDK add-ons.");
}
}

//Synthetic comment -- @@ -941,6 +941,7 @@
}

SdkSource[] sources = mSources.getAllSources();
monitor.setProgressMax(monitor.getProgress() + sources.length);
for (SdkSource source : sources) {
if (forceFetching ||








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/IProgressUiProvider.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/IProgressUiProvider.java
//Synthetic comment -- index 1d88e67..c6e1fa0 100755

//Synthetic comment -- @@ -16,9 +16,18 @@

package com.android.sdkuilib.internal.tasks;

import org.eclipse.swt.widgets.ProgressBar;


interface IProgressUiProvider {

public abstract boolean isCancelRequested();
//Synthetic comment -- @@ -30,10 +39,23 @@
public abstract void setDescription(String description);

/**
     * Sets the description in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public abstract void setResult(String result);

/**
* Sets the max value of the progress bar.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java
//Synthetic comment -- index 26d8519..42d5558 100755

//Synthetic comment -- @@ -47,6 +47,7 @@
/**
* Creates a thread to run the task. The thread has not been started yet.
* When the task completes, requests to close the dialog.
* @return A new thread that will run the task. The thread has not been started yet.
*/
private Thread createTaskThread(String title, final ITask task) {
//Synthetic comment -- @@ -67,12 +68,12 @@
}

/**
     * Sets the result in the current task dialog.
     * Sets the dialog to not auto-close since we want the user to see the result.
*/
@Override
    public void setResult(String resultFormat, Object...args) {
mAutomaticallyCloseOnTaskCompletion = false;
        super.setResult(resultFormat, args);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java
//Synthetic comment -- index f5cc04c..a4236fa 100755

//Synthetic comment -- @@ -283,10 +283,10 @@
}

/**
     * Sets the description in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void setResult(final String result) {
if (!mDialogShell.isDisposed()) {
mDialogShell.getDisplay().syncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -295,17 +295,25 @@
String lastText = mResultText.getText();
if (lastText != null &&
lastText.length() > 0 &&
                                !lastText.endsWith("\n") &&
                                !result.startsWith("\n")) {
                            mResultText.append("\n");
}
                        mResultText.append(result);
}
}
});
}
}

/**
* Sets the max value of the progress bar.
* This method can be invoked from a non-UI thread.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index b923a17..33076e0 100755

//Synthetic comment -- @@ -53,7 +53,7 @@
private final Label mLabel;
private final Control mStopButton;
private final ProgressBar mProgressBar;
    private final StringBuffer mResultText = new StringBuffer();


/**
//Synthetic comment -- @@ -150,15 +150,32 @@
mLabel.setText(description);
}
});
        mResultText.append("** ").append(description);
}

/**
     * Sets the description in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void setResult(final String result) {
        mResultText.append("=> ").append(result);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java
//Synthetic comment -- index ef408a2..c95db47 100755

//Synthetic comment -- @@ -55,18 +55,37 @@
* Sets the description in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void setDescription(String descriptionFormat, Object... args) {
        final String text = String.format(descriptionFormat, args);
mUi.setDescription(text);
}

/**
     * Sets the result in the current task.
* This method can be invoked from a non-UI thread.
*/
    public void setResult(String resultFormat, Object... args) {
        String text = String.format(resultFormat, args);
        mUi.setResult(text);
}

/**
//Synthetic comment -- @@ -112,7 +131,7 @@
* This method can be invoked from a non-UI thread.
*/
public int getProgress() {
        assert mIncCoef > 0;
return mIncCoef > 0 ? (int)(mUi.getProgress() / mIncCoef) : 0;
}

//Synthetic comment -- @@ -181,12 +200,20 @@
return mRoot.isCancelRequested();
}

        public void setDescription(String descriptionFormat, Object... args) {
            mRoot.setDescription(descriptionFormat, args);
}

        public void setResult(String resultFormat, Object... args) {
            mRoot.setResult(resultFormat, args);
}

public void setProgressMax(int max) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index de77848..86a094f 100644

//Synthetic comment -- @@ -1073,7 +1073,8 @@
new ITask() {
public void run(ITaskMonitor monitor) {
try {
                                monitor.setDescription("Starting emulator for AVD '%1$s'",
avdName);
int n = 10;
monitor.setProgressMax(n);
//Synthetic comment -- @@ -1095,7 +1096,7 @@
}
}
} catch (IOException e) {
                                monitor.setResult("Failed to start emulator: %1$s",
e.getMessage());
}
}
//Synthetic comment -- @@ -1122,7 +1123,7 @@
while (true) {
String line = errReader.readLine();
if (line != null) {
                            monitor.setResult("%1$s", line);    //$NON-NLS-1$
} else {
break;
}
//Synthetic comment -- @@ -1143,7 +1144,7 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            monitor.setResult("%1$s", line);    //$NON-NLS-1$
} else {
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index 4f9eec8..9470f91 100755

//Synthetic comment -- @@ -186,7 +186,7 @@
return false;
}

        public void setDescription(String descriptionFormat, Object... args) {
// ignore
}

//Synthetic comment -- @@ -194,7 +194,15 @@
// ignore
}

        public void setResult(String resultFormat, Object... args) {
// ignore
}
}







