/*Enforce a given version of platform-tools.

Change-Id:I8178b4ec7435a5091fdc1dcb892285ed3ecf55b0*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/AndroidBuilder.java b/builder/src/main/java/com/android/builder/AndroidBuilder.java
//Synthetic comment -- index 04f7e3c..11629be 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.utils.ILogger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
//Synthetic comment -- @@ -79,6 +80,8 @@
*/
public class AndroidBuilder {

private final SdkParser mSdkParser;
private final ILogger mLogger;
private final CommandLineRunner mCmdLineRunner;
//Synthetic comment -- @@ -107,6 +110,18 @@
mLogger = checkNotNull(logger);
mVerboseExec = verboseExec;
mCmdLineRunner = new CommandLineRunner(mLogger);
}

@VisibleForTesting








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/DefaultSdkParser.java b/builder/src/main/java/com/android/builder/DefaultSdkParser.java
//Synthetic comment -- index f8ef418..15d00f2 100644

//Synthetic comment -- @@ -20,15 +20,30 @@
import com.android.annotations.NonNull;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
import com.android.utils.ILogger;

import java.io.File;

/**
* Default implementation of {@link SdkParser} for a normal Android SDK distribution.
*/
public class DefaultSdkParser implements SdkParser {

private final String mSdkLocation;
private SdkManager mManager;

//Synthetic comment -- @@ -58,4 +73,39 @@
'/' + SdkConstants.FD_SUPPORT +
'/' + SdkConstants.FN_ANNOTATIONS_JAR;
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/SdkParser.java b/builder/src/main/java/com/android/builder/SdkParser.java
//Synthetic comment -- index 497d7a5..09c9abd 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.sdklib.IAndroidTarget;
import com.android.utils.ILogger;

/**
//Synthetic comment -- @@ -38,6 +39,15 @@
*/
IAndroidTarget resolveTarget(@NonNull String target, @NonNull ILogger logger);

String getAnnotationsJar();

}
\ No newline at end of file







