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
        mMonitor.log(format, args);
    }

    private void displayError(String format, Object...args) {
        mMonitor.logError(format, args);
}

/**
//Synthetic comment -- @@ -63,7 +67,7 @@
*/
public synchronized boolean startAdb() {
if (mAdbOsLocation == null) {
            displayError("Error: missing path to ADB."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -82,15 +86,15 @@
false /* waitForReaders */);

} catch (IOException ioe) {
            displayError("Unable to run 'adb': %1$s.", ioe.getMessage()); //$NON-NLS-1$
// we'll return false;
} catch (InterruptedException ie) {
            displayError("Unable to run 'adb': %1$s.", ie.getMessage()); //$NON-NLS-1$
// we'll return false;
}

if (status != 0) {
            displayError("'adb start-server' failed."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -105,7 +109,7 @@
*/
public synchronized boolean stopAdb() {
if (mAdbOsLocation == null) {
            displayError("Error: missing path to ADB."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -127,7 +131,7 @@
}

if (status != 0) {
            displayError("'adb kill-server' failed -- run manually if necessary."); //$NON-NLS-1$
return false;
}

//Synthetic comment -- @@ -163,7 +167,7 @@
while (true) {
String line = errReader.readLine();
if (line != null) {
                            displayError("ADB Error: %1$s", line);
errorOutput.add(line);
} else {
break;
//Synthetic comment -- @@ -185,7 +189,7 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            displayError("ADB: %1$s", line);
stdOutput.add(line);
} else {
break;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index 9b8d808..c0b7041 100755

//Synthetic comment -- @@ -142,11 +142,11 @@
reason = String.format("Unknown (%1$s)", exception[0].getClass().getName());
}

            monitor.logError("Failed to fetch URL %1$s, reason: %2$s", url, reason);
}

if (validationError[0] != null) {
            monitor.logError("%s", validationError[0]);  //$NON-NLS-1$
}

// Stop here if we failed to validate the XML. We don't want to load it.
//Synthetic comment -- @@ -409,13 +409,13 @@

return doc;
} catch (ParserConfigurationException e) {
            monitor.logError("Failed to create XML document builder");

} catch (SAXException e) {
            monitor.logError("Failed to parse XML document");

} catch (IOException e) {
            monitor.logError("Failed to read XML document");
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index 4d5d616..5807ca3 100755

//Synthetic comment -- @@ -63,7 +63,7 @@
String name = pkg.getShortDescription();

if (pkg instanceof ExtraPackage && !((ExtraPackage) pkg).isPathValid()) {
            monitor.log("Skipping %1$s: %2$s is not a valid install path.",
name,
((ExtraPackage) pkg).getPath());
return false;
//Synthetic comment -- @@ -71,14 +71,14 @@

if (archive.isLocal()) {
// This should never happen.
            monitor.log("Skipping already installed archive: %1$s for %2$s",
name,
archive.getOsDescription());
return false;
}

if (!archive.isCompatible()) {
            monitor.log("Skipping incompatible archive: %1$s for %2$s",
name,
archive.getOsDescription());
return false;
//Synthetic comment -- @@ -88,7 +88,7 @@
if (archiveFile != null) {
// Unarchive calls the pre/postInstallHook methods.
if (unarchive(archive, osSdkRoot, archiveFile, sdkManager, monitor)) {
                monitor.log("Installed %1$s", name);
// Delete the temp archive if it exists, only on success
OsHelper.deleteFileOrFolder(archiveFile);
return true;
//Synthetic comment -- @@ -110,7 +110,7 @@
String name = archive.getParentPackage().getShortDescription();
String desc = String.format("Downloading %1$s", name);
monitor.setDescription(desc);
        monitor.log(desc);

String link = archive.getUrl();
if (!link.startsWith("http://")                          //$NON-NLS-1$
//Synthetic comment -- @@ -120,7 +120,7 @@
Package pkg = archive.getParentPackage();
SdkSource src = pkg.getParentSource();
if (src == null) {
                monitor.logError("Internal error: no source for archive %1$s", name);
return null;
}

//Synthetic comment -- @@ -152,7 +152,7 @@
OsHelper.deleteFileOrFolder(tmpFolder);
}
if (!tmpFolder.mkdirs()) {
                monitor.logError("Failed to create directory %1$s", tmpFolder.getPath());
return null;
}
}
//Synthetic comment -- @@ -214,10 +214,10 @@

} catch (FileNotFoundException e) {
// The FNF message is just the URL. Make it a bit more useful.
            monitor.logError("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError(e.getMessage());

} finally {
if (is != null) {
//Synthetic comment -- @@ -327,14 +327,15 @@
}

if (monitor.isCancelRequested()) {
                    monitor.log("Download aborted by user at %1$d bytes.", total);
return false;
}

}

if (total != size) {
                monitor.logError(
                        "Download finished with wrong size. Expected %1$d bytes, got %2$d bytes.",
size, total);
return false;
}
//Synthetic comment -- @@ -343,7 +344,7 @@
String actual   = getDigestChecksum(digester);
String expected = archive.getChecksum();
if (!actual.equalsIgnoreCase(expected)) {
                monitor.logError("Download finished with wrong checksum. Expected %1$s, got %2$s.",
expected, actual);
return false;
}
//Synthetic comment -- @@ -352,10 +353,10 @@

} catch (FileNotFoundException e) {
// The FNF message is just the URL. Make it a bit more useful.
            monitor.logError("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError(e.getMessage());

} finally {
if (os != null) {
//Synthetic comment -- @@ -391,7 +392,7 @@
String pkgName = pkg.getShortDescription();
String pkgDesc = String.format("Installing %1$s", pkgName);
monitor.setDescription(pkgDesc);
        monitor.log(pkgDesc);

// Ideally we want to always unzip in a temp folder which name depends on the package
// type (e.g. addon, tools, etc.) and then move the folder to the destination folder.
//Synthetic comment -- @@ -432,12 +433,12 @@

if (destFolder == null) {
// this should not seriously happen.
                monitor.log("Failed to compute installation directory for %1$s.", pkgName);
return false;
}

if (!pkg.preInstallHook(archive, monitor, osSdkRoot, destFolder)) {
                monitor.log("Skipping archive: %1$s", pkgName);
return false;
}

//Synthetic comment -- @@ -450,14 +451,14 @@
}
if (oldDestFolder == null) {
// this should not seriously happen.
                    monitor.logError("Failed to find a temp directory in %1$s.", osSdkRoot);
return false;
}

// Try to move the current dest dir to the temp/old one. Tell the user if it failed.
while(true) {
if (!moveFolder(destFolder, oldDestFolder)) {
                        monitor.logError("Failed to rename directory %1$s to %2$s.",
destFolder.getPath(), oldDestFolder.getPath());

if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
//Synthetic comment -- @@ -489,7 +490,7 @@
// -2- Unzip new content directly in place.

if (!destFolder.mkdirs()) {
                monitor.logError("Failed to create directory %1$s", destFolder.getPath());
return false;
}

//Synthetic comment -- @@ -498,7 +499,7 @@
}

if (!generateSourceProperties(archive, destFolder)) {
                monitor.logError("Failed to generate source.properties in directory %1$s",
destFolder.getPath());
return false;
}
//Synthetic comment -- @@ -627,7 +628,7 @@
// Create directory if it doesn't exist yet. This allows us to create
// empty directories.
if (!destFile.isDirectory() && !destFile.mkdirs()) {
                        monitor.logError("Failed to create temp directory %1$s",
destFile.getPath());
return false;
}
//Synthetic comment -- @@ -638,7 +639,7 @@
File parentDir = destFile.getParentFile();
if (!parentDir.isDirectory()) {
if (!parentDir.mkdirs()) {
                            monitor.logError("Failed to create temp directory %1$s",
parentDir.getPath());
return false;
}
//Synthetic comment -- @@ -689,7 +690,7 @@
return true;

} catch (IOException e) {
            monitor.logError("Unzip failed: %1$s", e.getMessage());

} finally {
if (zipFile != null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskMonitor.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskMonitor.java
//Synthetic comment -- index e08e27c..7329d03 100755

//Synthetic comment -- @@ -27,6 +27,21 @@
* If the task runs in a non-UI worker thread, the task factory implementation
* will take care of the update the UI in the correct thread. The task itself
* must not have to deal with it.
 * <p/>
 * A monitor typically has 3 level of text displayed: <br/>
 * - A <b>title</b> <em>may</em> be present on a task dialog, typically when a task
 *   dialog is created. This is not covered by this monitor interface. <br/>
 * - A <b>description</b> display prominent information on what the task
 *   is currently doing. This is expected to vary over time, typically changing
 *   with each sub-task, and typically only the last description is visible.
 *   For example an updater would typically have descriptions such as "downloading",
 *   "installing" and finally "done". This is set using {@link #setDescription}. <br/>
 * - A <b>verbose</b> optional log that can provide more information than the summary
 *   description and is typically displayed in some kind of scrollable multi-line
 *   text field so that the user can keep track of what happened. 3 levels are
 *   provided: error, normal and verbose. An UI may hide the log till an error is
 *   logged and/or might hide the verbose text unless a flag is checked by the user.
 *   See {@link #log}, {@link #logError} and {@link #logVerbose}.
*/
public interface ITaskMonitor {

//Synthetic comment -- @@ -34,13 +49,26 @@
* Sets the description in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void setDescription(String format, Object...args);

/**
     * Logs a "normal" information line.
* This method can be invoked from a non-UI thread.
*/
    public void log(String format, Object...args);

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    public void logError(String format, Object...args);

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    public void logVerbose(String format, Object...args);

/**
* Sets the max value of the progress bar.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 6fc26e3..22b9c42 100755

//Synthetic comment -- @@ -303,7 +303,7 @@
if (usingAlternateUrl && validatedDoc != null) {
// If the second tentative succeeded, indicate it in the console
// with the URL that worked.
                            monitor.log("Repository found at %1$s", url);

// Keep the modified URL
mUrl = url;
//Synthetic comment -- @@ -388,11 +388,11 @@
reason = String.format("Unknown (%1$s)", exception[0].getClass().getName());
}

            monitor.logError("Failed to fetch URL %1$s, reason: %2$s", url, reason);
}

if (validationError[0] != null) {
            monitor.logError("%s", validationError[0]);  //$NON-NLS-1$
}

// Stop here if we failed to validate the XML. We don't want to load it.
//Synthetic comment -- @@ -748,16 +748,11 @@

if (p != null) {
packages.add(p);
                            monitor.logVerbose("Found %1$s", p.getShortDescription());
}
} catch (Exception e) {
// Ignore invalid packages
                        monitor.logError("Ignoring invalid %1$s element: %2$s", name, e.toString());
}
}
}
//Synthetic comment -- @@ -806,13 +801,13 @@

return doc;
} catch (ParserConfigurationException e) {
            monitor.logError("Failed to create XML document builder");

} catch (SAXException e) {
            monitor.logError("Failed to parse XML document");

} catch (IOException e) {
            monitor.logError("Failed to read XML document");
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index 827c721..69039ea 100755

//Synthetic comment -- @@ -263,17 +263,17 @@
status = grabProcessOutput(proc, monitor, scriptName);

} catch (Exception e) {
            monitor.logError("Exception: %s", e.toString());
}

if (status != 0) {
            monitor.logError("Failed to execute %s", scriptName);
return;
}
}

/**
     * Gets the stderr/stdout outputs of a process and returns when the process is done.
* Both <b>must</b> be read or the process will block on windows.
* @param process The process to get the ouput from.
* @param monitor The monitor where to output errors.
//Synthetic comment -- @@ -298,7 +298,7 @@
while (true) {
String line = errReader.readLine();
if (line != null) {
                            monitor.logError("[%1$s] Error: %2$s", scriptName, line);
} else {
break;
}
//Synthetic comment -- @@ -319,7 +319,7 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            monitor.log("[%1$s] %2$s", scriptName, line);
} else {
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/AddonsListFetcherTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/AddonsListFetcherTest.java
//Synthetic comment -- index 3f2bb84..3a18a12 100755

//Synthetic comment -- @@ -104,7 +104,7 @@
Site[] result = mFetcher._parseAddonsList(doc, uri, monitor);

assertEquals("", monitor.getCapturedDescriptions());
        assertEquals("", monitor.getCapturedLog());

// check the sites we found...
assertEquals(3, result.length);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java
//Synthetic comment -- index 56a7c6c..3b7e608 100755

//Synthetic comment -- @@ -24,26 +24,44 @@
*/
public class MockMonitor implements ITaskMonitor {

    String mCapturedLog = "";                                           //$NON-NLS-1$
    String mCapturedErrorLog = "";                                      //$NON-NLS-1$
    String mCapturedVerboseLog = "";                                    //$NON-NLS-1$
    String mCapturedDescriptions = "";                                  //$NON-NLS-1$

    public String getCapturedLog() {
        return mCapturedLog;
    }

    public String getCapturedErrorLog() {
        return mCapturedErrorLog;
    }

    public String getCapturedVerboseLog() {
        return mCapturedVerboseLog;
}

public String getCapturedDescriptions() {
return mCapturedDescriptions;
}

    public void log(String format, Object... args) {
        mCapturedLog += String.format(format, args) + "\n";             //$NON-NLS-1$
    }

    public void logError(String format, Object... args) {
        mCapturedErrorLog += String.format(format, args) + "\n";        //$NON-NLS-1$
    }

    public void logVerbose(String format, Object... args) {
        mCapturedVerboseLog += String.format(format, args) + "\n";      //$NON-NLS-1$
}

public void setProgressMax(int max) {
}

    public void setDescription(String format, Object... args) {
        mCapturedDescriptions += String.format(format, args) + "\n";    //$NON-NLS-1$
}

public boolean isCancelRequested() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
//Synthetic comment -- index ea6c4f6..6df440e 100755

//Synthetic comment -- @@ -176,8 +176,9 @@
"Found G USB Driver package, revision 43 (Obsolete)\n" +
"Found Android Vendor Extra API Dep package, revision 2 (Obsolete)\n" +
"Found Unkown Extra package, revision 2 (Obsolete)\n",
                monitor.getCapturedVerboseLog());
        assertEquals("", monitor.getCapturedLog());
        assertEquals("", monitor.getCapturedErrorLog());

// check the packages we found... we expected to find 11 packages with each at least
// one archive.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index aee811f..24a6fb6 100755

//Synthetic comment -- @@ -141,14 +141,15 @@

Document result = mSource._findAlternateToolsXml(xmlStream);
assertNotNull(result);
        MockMonitor monitor = new MockMonitor();
        assertTrue(mSource._parsePackages(result, SdkRepoConstants.NS_URI, monitor));

assertEquals("Found Android SDK Tools, revision 1\n" +
"Found Android SDK Tools, revision 42\n" +
"Found Android SDK Platform-tools, revision 3\n",
                monitor.getCapturedVerboseLog());
        assertEquals("", monitor.getCapturedLog());
        assertEquals("", monitor.getCapturedErrorLog());

// check the packages we found... we expected to find 2 tool packages and 1
// platform-tools package, with at least 1 archive each.
//Synthetic comment -- @@ -199,8 +200,9 @@
"Found Android SDK Tools, revision 42\n" +
"Found This add-on has no libraries by Joe Bar, Android API 4, revision 3\n" +
"Found Usb Driver package, revision 43\n",
                monitor.getCapturedVerboseLog());
        assertEquals("", monitor.getCapturedLog());
        assertEquals("", monitor.getCapturedErrorLog());

// check the packages we found... we expected to find 11 packages with each at least
// one archive.
//Synthetic comment -- @@ -277,8 +279,9 @@
"Found Usb Driver package, revision 43 (Obsolete)\n" +
"Found Extra API Dep package, revision 2 (Obsolete)\n" +
"Found Samples for SDK API 14, revision 24 (Obsolete)\n",
                monitor.getCapturedVerboseLog());
        assertEquals("", monitor.getCapturedLog());
        assertEquals("", monitor.getCapturedErrorLog());

// check the packages we found... we expected to find 13 packages with each at least
// one archive.
//Synthetic comment -- @@ -353,8 +356,9 @@
"Found A USB Driver package, revision 43 (Obsolete)\n" +
"Found Android Vendor Extra API Dep package, revision 2 (Obsolete)\n" +
"Found Samples for SDK API 14, revision 24 (Obsolete)\n",
                monitor.getCapturedVerboseLog());
        assertEquals("", monitor.getCapturedLog());
        assertEquals("", monitor.getCapturedErrorLog());

// check the packages we found... we expected to find 13 packages with each at least
// one archive.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index ddf1e2e..6d5b5d4 100755

//Synthetic comment -- @@ -174,45 +174,52 @@
/**
* Sets the description in the current task dialog.
*/
        public void setDescription(String format, Object...args) {

String last = mLastDesc;
            String line = String.format("  " + format, args);                       //$NON-NLS-1$

// If the description contains a %, it generally indicates a recurring
// progress so we want a \r at the end.
            int pos = line.indexOf('%');
            if (pos > -1) {
                String base = line.trim();
                if (mLastProgressBase != null && base.startsWith(mLastProgressBase)) {
                    line = "    " + base.substring(mLastProgressBase.length());     //$NON-NLS-1$
}
line += '\r';
} else {
                mLastProgressBase = line.trim();
line += '\n';
}

// Skip line if it's the same as the last one.
            if (last != null && last.equals(line.trim())) {
return;
}
            mLastDesc = line.trim();

// If the last line terminated with a \r but the new one doesn't, we need to
// insert a \n to avoid erasing the previous line.
if (last != null &&
                    last.endsWith("\r") &&                                          //$NON-NLS-1$
                    !line.endsWith("\r")) {                                         //$NON-NLS-1$
line = '\n' + line;
}

mSdkLog.printf("%s", line);                                             //$NON-NLS-1$
}

        public void log(String format, Object...args) {
            setDescription("  " + format, args);                                    //$NON-NLS-1$
        }

        public void logError(String format, Object...args) {
            setDescription(format, args);
        }

        public void logVerbose(String format, Object...args) {
            // The ConsoleTask does not display verbose log messages.
}

/**
//Synthetic comment -- @@ -331,12 +338,20 @@
return mRoot.isCancelRequested();
}

        public void setDescription(String format, Object... args) {
            mRoot.setDescription(format, args);
}

        public void log(String format, Object... args) {
            mRoot.log(format, args);
        }

        public void logError(String format, Object... args) {
            mRoot.logError(format, args);
        }

        public void logVerbose(String format, Object... args) {
            mRoot.logVerbose(format, args);
}

public void setProgressMax(int max) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 04cc947..1a3b5cb 100755

//Synthetic comment -- @@ -438,14 +438,14 @@
// This archive depends on a missing archive.
// We shouldn't get here.
// Skip it.
                                    monitor.log("Skipping '%1$s'; it depends on a missing package.",
archive.getParentPackage().getShortDescription());
continue nextArchive;
} else if (!installedArchives.contains(na)) {
// This archive depends on another one that was not installed.
// We shouldn't get here.
// Skip it.
                                    monitor.logError("Skipping '%1$s'; it depends on '%2$s' which was not installed.",
archive.getParentPackage().getShortDescription(),
adep.getShortDescription());
continue nextArchive;
//Synthetic comment -- @@ -500,7 +500,7 @@
baos.toString());
}

                        monitor.log(msg);
mSdkLog.error(t, msg);
} finally {

//Synthetic comment -- @@ -515,10 +515,10 @@
// Update the USB vendor ids for adb
try {
mSdkManager.updateAdb();
                        monitor.log("Updated ADB to support the USB devices declared in the SDK add-ons.");
} catch (Exception e) {
mSdkLog.error(e, "Update ADB failed");
                        monitor.logError("failed to update adb to support the USB devices declared in the SDK add-ons.");
}
}

//Synthetic comment -- @@ -941,6 +941,7 @@
}

SdkSource[] sources = mSources.getAllSources();
                monitor.setDescription("Refresh Sources");
monitor.setProgressMax(monitor.getProgress() + sources.length);
for (SdkSource source : sources) {
if (forceFetching ||








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/IProgressUiProvider.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/IProgressUiProvider.java
//Synthetic comment -- index 1d88e67..c6e1fa0 100755

//Synthetic comment -- @@ -16,9 +16,18 @@

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITaskMonitor;

import org.eclipse.swt.widgets.ProgressBar;

/**
 * Interface for a user interface that display both a task status
 * (e.g. via an {@link ITaskMonitor}) and the progress state of the
 * task (e.g. via a progress bar.)
 * <p/>
 * See {@link ITaskMonitor} for details on how a monitor expects to
 * be displayed.
 */
interface IProgressUiProvider {

public abstract boolean isCancelRequested();
//Synthetic comment -- @@ -30,10 +39,23 @@
public abstract void setDescription(String description);

/**
     * Logs a "normal" information line.
* This method can be invoked from a non-UI thread.
*/
    public abstract void log(String log);

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    public abstract void logError(String log);

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    public abstract void logVerbose(String log);

/**
* Sets the max value of the progress bar.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTask.java
//Synthetic comment -- index 26d8519..42d5558 100755

//Synthetic comment -- @@ -47,6 +47,7 @@
/**
* Creates a thread to run the task. The thread has not been started yet.
* When the task completes, requests to close the dialog.
     *
* @return A new thread that will run the task. The thread has not been started yet.
*/
private Thread createTaskThread(String title, final ITask task) {
//Synthetic comment -- @@ -67,12 +68,12 @@
}

/**
     * Sets the dialog to not auto-close since we want the user to see the error.
     * {@inheritDoc}
*/
@Override
    public void logError(String format, Object...args) {
mAutomaticallyCloseOnTaskCompletion = false;
        super.logError(format, args);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java
//Synthetic comment -- index f5cc04c..a4236fa 100755

//Synthetic comment -- @@ -283,10 +283,10 @@
}

/**
     * Adds to the log in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void log(final String info) {
if (!mDialogShell.isDisposed()) {
mDialogShell.getDisplay().syncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -295,17 +295,25 @@
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

    public void logError(String info) {
        log(info);
    }

    public void logVerbose(String info) {
        log(info);
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
    private final StringBuffer mLogText = new StringBuffer();


/**
//Synthetic comment -- @@ -150,15 +150,32 @@
mLabel.setText(description);
}
});
        mLogText.append("** ").append(description);
}

/**
     * Logs a "normal" information line.
* This method can be invoked from a non-UI thread.
*/
    public void log(String log) {
        mLogText.append("=> ").append(log);
    }

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    public void logError(String log) {
        mLogText.append("=> ").append(log);
    }

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    public void logVerbose(String log) {
        mLogText.append("=> ").append(log);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java
//Synthetic comment -- index ef408a2..c95db47 100755

//Synthetic comment -- @@ -55,18 +55,37 @@
* Sets the description in the current task dialog.
* This method can be invoked from a non-UI thread.
*/
    public void setDescription(String format, Object... args) {
        final String text = String.format(format, args);
mUi.setDescription(text);
}

/**
     * Logs a "normal" information line.
* This method can be invoked from a non-UI thread.
*/
    public void log(String format, Object... args) {
        String text = String.format(format, args);
        mUi.log(text);
    }

    /**
     * Logs an "error" information line.
     * This method can be invoked from a non-UI thread.
     */
    public void logError(String format, Object... args) {
        String text = String.format(format, args);
        mUi.logError(text);
    }

    /**
     * Logs a "verbose" information line, that is extra details which are typically
     * not that useful for the end-user and might be hidden until explicitly shown.
     * This method can be invoked from a non-UI thread.
     */
    public void logVerbose(String format, Object... args) {
        String text = String.format(format, args);
        mUi.logVerbose(text);
}

/**
//Synthetic comment -- @@ -112,7 +131,7 @@
* This method can be invoked from a non-UI thread.
*/
public int getProgress() {
        // mIncCoef is 0 if setProgressMax hasn't been used yet.
return mIncCoef > 0 ? (int)(mUi.getProgress() / mIncCoef) : 0;
}

//Synthetic comment -- @@ -181,12 +200,20 @@
return mRoot.isCancelRequested();
}

        public void setDescription(String format, Object... args) {
            mRoot.setDescription(format, args);
}

        public void log(String format, Object... args) {
            mRoot.log(format, args);
        }

        public void logError(String format, Object... args) {
            mRoot.logError(format, args);
        }

        public void logVerbose(String format, Object... args) {
            mRoot.logVerbose(format, args);
}

public void setProgressMax(int max) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index de77848..86a094f 100644

//Synthetic comment -- @@ -1073,7 +1073,8 @@
new ITask() {
public void run(ITaskMonitor monitor) {
try {
                                monitor.setDescription(
                                        "Starting emulator for AVD '%1$s'",
avdName);
int n = 10;
monitor.setProgressMax(n);
//Synthetic comment -- @@ -1095,7 +1096,7 @@
}
}
} catch (IOException e) {
                                monitor.logError("Failed to start emulator: %1$s",
e.getMessage());
}
}
//Synthetic comment -- @@ -1122,7 +1123,7 @@
while (true) {
String line = errReader.readLine();
if (line != null) {
                            monitor.logError("%1$s", line);    //$NON-NLS-1$
} else {
break;
}
//Synthetic comment -- @@ -1143,7 +1144,7 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            monitor.log("%1$s", line);    //$NON-NLS-1$
} else {
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index 4f9eec8..9470f91 100755

//Synthetic comment -- @@ -186,7 +186,7 @@
return false;
}

        public void setDescription(String format, Object... args) {
// ignore
}

//Synthetic comment -- @@ -194,7 +194,15 @@
// ignore
}

        public void log(String format, Object... args) {
            // ignore
        }

        public void logError(String format, Object... args) {
            // ignore
        }

        public void logVerbose(String format, Object... args) {
// ignore
}
}







