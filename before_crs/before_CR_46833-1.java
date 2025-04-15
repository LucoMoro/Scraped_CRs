/*Merge "AVD Manager: prevent NPE in AvdInfo."

AvdInfo.mProperties should never be null.
The constructor assumed it could but then the
code below uses it without guard in multiple
places. Solve this by making sure the constructor
doesn't set the map to null.

SDK Bug: 40400

(cherry picked from commit 65d2c252d1eafb22552bbfcf7fc788d4ba377c89)

Change-Id:I746dd3780cb864b7389b3ac30c58e3b7889ee5b8*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index c4b6b18..83aa2ef 100755

//Synthetic comment -- @@ -60,6 +60,7 @@
private final String mTargetHash;
private final IAndroidTarget mTarget;
private final String mAbiType;
private final Map<String, String> mProperties;
private final AvdStatus mStatus;

//Synthetic comment -- @@ -75,7 +76,7 @@
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
* @param abiType Name of the abi.
     * @param properties The property map. Cannot be null.
*/
public AvdInfo(String name,
File iniFile,
//Synthetic comment -- @@ -99,7 +100,7 @@
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
* @param abiType Name of the abi.
     * @param properties The property map. Can be null.
* @param status The {@link AvdStatus} of this AVD. Cannot be null.
*/
public AvdInfo(String name,
//Synthetic comment -- @@ -116,7 +117,8 @@
mTargetHash = targetHash;
mTarget = target;
mAbiType = abiType;
        mProperties = properties == null ? null : Collections.unmodifiableMap(properties);
mStatus = status;
}

//Synthetic comment -- @@ -261,7 +263,9 @@
}

/**
     * Returns an unmodifiable map of properties for the AVD. This can be null.
*/
public Map<String, String> getProperties() {
return mProperties;







