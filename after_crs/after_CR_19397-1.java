/*Cherry-pick 231ec810 from tools_r8

Improved error message during export.

Change-Id:Ic4b0bf8cfbf59b70a797a31036eb9204481e29d9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 002ac88..e582a91 100644

//Synthetic comment -- @@ -525,15 +525,17 @@
// get the output and return code from the process
execError = grabProcessOutput(mProject, process, results);

            if (mVerbose) {
for (String resultString : results) {
mOutStream.println(resultString);
}
}

            if (execError != 0) {
                throw new ProguardResultException(execError,
                        results.toArray(new String[results.size()]));
            }

} catch (IOException e) {
String msg = String.format(Messages.Proguard_Exec_Error, commandArray[0]);
throw new ProguardExecException(msg, e);
//Synthetic comment -- @@ -687,16 +689,15 @@
// get the output and return code from the process
execError = grabProcessOutput(mProject, process, results);

            if (mVerbose) {
for (String resultString : results) {
mOutStream.println(resultString);
}
}
            if (execError != 0) {
                throw new AaptResultException(execError,
                        results.toArray(new String[results.size()]));
            }
} catch (IOException e) {
String msg = String.format(Messages.AAPT_Exec_Error, command[0]);
throw new AaptExecException(msg, e);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index cdfe3b3..17fc1e2 100644

//Synthetic comment -- @@ -20,12 +20,18 @@
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
import com.android.ide.eclipse.adt.internal.build.NativeLibInJarException;
import com.android.ide.eclipse.adt.internal.build.ProguardExecException;
import com.android.ide.eclipse.adt.internal.build.ProguardResultException;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.xml.AndroidManifest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -213,6 +219,38 @@
// success!
} catch (CoreException e) {
throw e;
        } catch (ProguardResultException e) {
            String msg = String.format("Proguard returned with error code %d. See console",
                    e.getErrorCode());
            AdtPlugin.printErrorToConsole(project, msg);
            AdtPlugin.printErrorToConsole(project, (Object[]) e.getOutput());
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    msg, e));
        } catch (ProguardExecException e) {
            String msg = String.format("Failed to run proguard: %s", e.getMessage());
            AdtPlugin.printErrorToConsole(project, msg);
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    msg, e));
        } catch (DuplicateFileException e) {
            String msg = String.format(
                    "Found duplicate file for APK: %1$s\nOrigin 1: %2$s\nOrigin 2: %3$s",
                    e.getArchivePath(), e.getFile1(), e.getFile2());
            AdtPlugin.printErrorToConsole(project, msg);
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    e.getMessage(), e));
        } catch (NativeLibInJarException e) {
            String msg = e.getMessage();

            AdtPlugin.printErrorToConsole(project, msg);
            AdtPlugin.printErrorToConsole(project, (Object[]) e.getAdditionalInfo());
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    e.getMessage(), e));
        } catch (DexException e) {
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    e.getMessage(), e));
        } catch (ApkCreationException e) {
            throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    e.getMessage(), e));
} catch (Exception e) {
throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Failed to export application", e));







