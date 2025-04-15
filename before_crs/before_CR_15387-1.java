/*Move the exceptions out of ApkBuilder.

Change-Id:I66e767cbd4f3f3521bb994b281140a433f63291f*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ApkBuilderTask.java b/anttasks/src/com/android/ant/ApkBuilderTask.java
//Synthetic comment -- index bc5f53c..d7ffd9c 100644

//Synthetic comment -- @@ -17,9 +17,9 @@
package com.android.ant;

import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkBuilder.ApkCreationException;
import com.android.sdklib.build.ApkBuilder.DuplicateFileException;
import com.android.sdklib.build.ApkBuilder.SealedApkException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java
//Synthetic comment -- index 3c2d86a..5915ebb 100644

//Synthetic comment -- @@ -28,10 +28,10 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkBuilder.ApkCreationException;
import com.android.sdklib.build.ApkBuilder.DuplicateFileException;
import com.android.sdklib.build.ApkBuilder.JarStatus;
import com.android.sdklib.build.ApkBuilder.SealedApkException;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index 9f1fc84..cd6d37c 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.android.sdklib.internal.build.DebugKeyProvider.IKeyGenOutput;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter.ZipAbortException;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -166,78 +165,6 @@
private final HashMap<String, File> mAddedFiles = new HashMap<String, File>();

/**
     * An exception thrown during packaging of an APK file.
     */
    public final static class ApkCreationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ApkCreationException(String format, Object... args) {
            super(String.format(format, args));
        }

        public ApkCreationException(Throwable cause, String format, Object... args) {
            super(String.format(format, args), cause);
        }

        public ApkCreationException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * An exception thrown during packaging of an APK file.
     */
    public final static class DuplicateFileException extends ZipAbortException {
        private static final long serialVersionUID = 1L;
        private final String mArchivePath;
        private final File mFile1;
        private final File mFile2;

        public DuplicateFileException(String archivePath, File file1, File file2) {
            super();
            mArchivePath = archivePath;
            mFile1 = file1;
            mFile2 = file2;
        }

        public String getArchivePath() {
            return mArchivePath;
        }

        public File getFile1() {
            return mFile1;
        }

        public File getFile2() {
            return mFile2;
        }

        @Override
        public String getMessage() {
            return "Duplicate files at the same path inside the APK";
        }
    }

    /**
     * An exception thrown when trying to add files to a sealed APK.
     */
    public final static class SealedApkException extends Exception {
        private static final long serialVersionUID = 1L;

        public SealedApkException(String format, Object... args) {
            super(String.format(format, args));
        }

        public SealedApkException(Throwable cause, String format, Object... args) {
            super(String.format(format, args), cause);
        }

        public SealedApkException(Throwable cause) {
            super(cause);
        }
    }

    /**
* Status for the addition of a jar file resources into the APK.
* This indicates possible issues with native library inside the jar file.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilderMain.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilderMain.java
//Synthetic comment -- index 626e285..f139eec 100644

//Synthetic comment -- @@ -16,9 +16,6 @@

package com.android.sdklib.build;

import com.android.sdklib.build.ApkBuilder.ApkCreationException;
import com.android.sdklib.build.ApkBuilder.DuplicateFileException;
import com.android.sdklib.build.ApkBuilder.SealedApkException;

import java.io.File;
import java.io.FilenameFilter;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkCreationException.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkCreationException.java
new file mode 100644
//Synthetic comment -- index 0000000..2379915

//Synthetic comment -- @@ -0,0 +1,36 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/DuplicateFileException.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/DuplicateFileException.java
new file mode 100644
//Synthetic comment -- index 0000000..ba53ba3

//Synthetic comment -- @@ -0,0 +1,55 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/SealedApkException.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/SealedApkException.java
new file mode 100644
//Synthetic comment -- index 0000000..97f03bd

//Synthetic comment -- @@ -0,0 +1,36 @@
\ No newline at end of file







