/*Fixed AvdManager to only have one instance per SDK location

Change-Id:I59697ef605e33a9216ed318190060e71ce0df579*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index bcca729..6b45f09 100644

//Synthetic comment -- @@ -270,7 +270,7 @@
// create the AVD Manager
AvdManager avdManager = null;
try {
                    avdManager = AvdManager.getInstance(manager, log);
} catch (AndroidLocationException e) {
log.error(e, "Error parsing the AVDs");
}








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index bbfbdcc..3d08574 100644

//Synthetic comment -- @@ -1007,7 +1007,7 @@
*/
private void displayAvdList() {
try {
            AvdManager avdManager = AvdManager.getInstance(mSdkManager, mSdkLog);
displayAvdList(avdManager);
} catch (AndroidLocationException e) {
errorAndExit(e.getMessage());
//Synthetic comment -- @@ -1031,7 +1031,7 @@

try {
boolean removePrevious = mSdkCommandLine.getFlagForce();
            AvdManager avdManager = AvdManager.getInstance(mSdkManager, mSdkLog);

String avdName = mSdkCommandLine.getParamName();

//Synthetic comment -- @@ -1157,7 +1157,7 @@
private void deleteAvd() {
try {
String avdName = mSdkCommandLine.getParamName();
            AvdManager avdManager = AvdManager.getInstance(mSdkManager, mSdkLog);
AvdInfo info = avdManager.getAvd(avdName, false /*validAvdOnly*/);

if (info == null) {
//Synthetic comment -- @@ -1177,7 +1177,7 @@
private void moveAvd() {
try {
String avdName = mSdkCommandLine.getParamName();
            AvdManager avdManager = AvdManager.getInstance(mSdkManager, mSdkLog);
AvdInfo info = avdManager.getAvd(avdName, true /*validAvdOnly*/);

if (info == null) {
//Synthetic comment -- @@ -1273,7 +1273,7 @@
private void updateAvd() {
try {
String avdName = mSdkCommandLine.getParamName();
            AvdManager avdManager = AvdManager.getInstance(mSdkManager, mSdkLog);
avdManager.updateAvd(avdName, mSdkLog);
} catch (AndroidLocationException e) {
errorAndExit(e.getMessage());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 16bbb38..3bda06f 100644

//Synthetic comment -- @@ -41,9 +41,11 @@
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -215,6 +217,12 @@
CONFLICT_EXISTING_PATH,
}

    // A map where the keys are the locations of the SDK and the values are the corresponding
    // AvdManagers. This prevents us from creating multiple AvdManagers for the same SDK and having
    // them get out of sync.
    private static final Map<String, AvdManager> mManagers =
        Collections.synchronizedMap(new WeakHashMap<String, AvdManager>());

private final ArrayList<AvdInfo> mAllAvdList = new ArrayList<AvdInfo>();
private AvdInfo[] mValidAvdList;
private AvdInfo[] mBrokenAvdList;
//Synthetic comment -- @@ -230,11 +238,23 @@
*            logging needs. Cannot be null.
* @throws AndroidLocationException
*/
    protected AvdManager(SdkManager sdkManager, ISdkLog log) throws AndroidLocationException {
mSdkManager = sdkManager;
buildAvdList(mAllAvdList, log);
}

    public static AvdManager getInstance(SdkManager sdkManager, ISdkLog log) throws AndroidLocationException {
        synchronized(mManagers) {
            AvdManager manager;
            if ((manager = mManagers.get(sdkManager.getLocation())) != null) {
                return manager;
            }
            manager = new AvdManager(sdkManager, log);
            mManagers.put(sdkManager.getLocation(), manager);
            return manager;
        }
    }

/**
* Returns the base folder where AVDs are created.
*








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 3361b77..6b3fcac 100755

//Synthetic comment -- @@ -259,7 +259,7 @@
setSdkManager(SdkManager.createManager(mOsSdkRoot, mSdkLog));
try {
mAvdManager = null;
          mAvdManager = AvdManager.getInstance(mSdkManager, mSdkLog);
} catch (AndroidLocationException e) {
mSdkLog.error(e, "Unable to read AVDs: " + e.getMessage());  //$NON-NLS-1$








