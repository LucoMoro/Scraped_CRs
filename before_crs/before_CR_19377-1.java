/*Improved error message during export.

Change-Id:Idb9981348df22e73de3f3d2a72de50a392115cc4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 7af2b9d..50c3ff1 100644

//Synthetic comment -- @@ -520,15 +520,17 @@
// get the output and return code from the process
execError = grabProcessOutput(mProject, process, results);

            if (execError != 0) {
                throw new ProguardResultException(execError,
                        results.toArray(new String[results.size()]));
            } else if (mVerbose) {
for (String resultString : results) {
mOutStream.println(resultString);
}
}

} catch (IOException e) {
String msg = String.format(Messages.Proguard_Exec_Error, commandArray[0]);
throw new ProguardExecException(msg, e);
//Synthetic comment -- @@ -682,16 +684,15 @@
// get the output and return code from the process
execError = grabProcessOutput(mProject, process, results);

            if (execError != 0) {
                throw new AaptResultException(execError,
                        results.toArray(new String[results.size()]));
            } else if (mVerbose) {
for (String resultString : results) {
mOutStream.println(resultString);
}
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
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -213,6 +219,38 @@
// success!
} catch (CoreException e) {
throw e;
} catch (Exception e) {
throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Failed to export application", e));







