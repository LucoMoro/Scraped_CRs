/*Enforce a given version of platform-tools.

Change-Id:I8178b4ec7435a5091fdc1dcb892285ed3ecf55b0*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/AndroidBuilder.java b/builder/src/main/java/com/android/builder/AndroidBuilder.java
//Synthetic comment -- index 04f7e3c..11629be 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.utils.ILogger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
//Synthetic comment -- @@ -79,6 +80,8 @@
*/
public class AndroidBuilder {

    private static final FullRevision MIN_PLATFORM_TOOLS_REV = new FullRevision(16, 0, 2);

private final SdkParser mSdkParser;
private final ILogger mLogger;
private final CommandLineRunner mCmdLineRunner;
//Synthetic comment -- @@ -107,6 +110,18 @@
mLogger = checkNotNull(logger);
mVerboseExec = verboseExec;
mCmdLineRunner = new CommandLineRunner(mLogger);

        FullRevision platformToolsRevision = mSdkParser.getPlatformToolsRevision();
        if (platformToolsRevision == null) {
            throw new IllegalArgumentException(
                    "The SDK Platform Tools revision could not be found. Make sure the component is installed.");
        }
        if (platformToolsRevision.compareTo(MIN_PLATFORM_TOOLS_REV) < 0) {
            throw new IllegalArgumentException(String.format(
                    "The SDK Platform Tools revision (%1$s) is too low. Minimum required is %2$s",
                    platformToolsRevision, MIN_PLATFORM_TOOLS_REV));

        }
}

@VisibleForTesting








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/DefaultSdkParser.java b/builder/src/main/java/com/android/builder/DefaultSdkParser.java
//Synthetic comment -- index f8ef418..15d00f2 100644

//Synthetic comment -- @@ -20,15 +20,30 @@
import com.android.annotations.NonNull;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.utils.ILogger;
import com.google.common.io.Closeables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.regex.Pattern;

/**
* Default implementation of {@link SdkParser} for a normal Android SDK distribution.
*/
public class DefaultSdkParser implements SdkParser {

    /**
     * Pattern to read the line containing the package revision
     */
    private final static Pattern sPkgRevisionPattern = Pattern.compile(
            "^" + PkgProps.PKG_REVISION + "=(.*)$");

private final String mSdkLocation;
private SdkManager mManager;

//Synthetic comment -- @@ -58,4 +73,39 @@
'/' + SdkConstants.FD_SUPPORT +
'/' + SdkConstants.FN_ANNOTATIONS_JAR;
}

    @Override
    public FullRevision getPlatformToolsRevision() {
        File platformTools = new File(mSdkLocation, SdkConstants.FD_PLATFORM_TOOLS);
        if (!platformTools.isDirectory()) {
            return null;
        }


        Reader reader = null;
        try {
            reader = new FileReader(new File(platformTools, SdkConstants.FN_SOURCE_PROP));
            Properties props = new Properties();
            props.load(reader);

            String value = props.getProperty(PkgProps.PKG_REVISION);

            FullRevision toolsRevision = null;
            try {
                toolsRevision = FullRevision.parseRevision(value);
            } catch (NumberFormatException ignore) {
                // we'll return null below
            }

            return toolsRevision;
        } catch (FileNotFoundException ignore) {
            // return null below.
        } catch (IOException ignore) {
            // return null below.
        } finally {
            Closeables.closeQuietly(reader);
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/SdkParser.java b/builder/src/main/java/com/android/builder/SdkParser.java
//Synthetic comment -- index 497d7a5..09c9abd 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.utils.ILogger;

/**
//Synthetic comment -- @@ -38,6 +39,15 @@
*/
IAndroidTarget resolveTarget(@NonNull String target, @NonNull ILogger logger);

    /**
     * Returns the location of the annotations jar for compilation targets that are <= 15.
     */
String getAnnotationsJar();

    /**
     * Returns the revision of the installed platform tools component.
     *
     * @return the FullRevision or null if the revision couldn't not be found
     */
    FullRevision getPlatformToolsRevision();
}
\ No newline at end of file







