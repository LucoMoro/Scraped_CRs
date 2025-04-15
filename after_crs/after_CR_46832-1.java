/*AVD Manager: prevent NPE in AvdInfo.

AvdInfo.mProperties should never be null.
The constructor assumed it could but then the
code below uses it without guard in multiple
places. Solve this by making sure the constructor
doesn't set the map to null.

SDK Bug: 40400

Change-Id:I82f4b4c3bfa05ab3b3f57463fdaf5b17273faf56*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index c4b6b18..83aa2ef 100755

//Synthetic comment -- @@ -60,6 +60,7 @@
private final String mTargetHash;
private final IAndroidTarget mTarget;
private final String mAbiType;
    /** An immutable map of properties. This must not be modified. Map can be empty. Never null. */
private final Map<String, String> mProperties;
private final AvdStatus mStatus;

//Synthetic comment -- @@ -75,7 +76,7 @@
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
* @param abiType Name of the abi.
     * @param properties The property map. If null, an empty map will be created.
*/
public AvdInfo(String name,
File iniFile,
//Synthetic comment -- @@ -99,7 +100,7 @@
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
* @param abiType Name of the abi.
     * @param properties The property map. If null, an empty map will be created.
* @param status The {@link AvdStatus} of this AVD. Cannot be null.
*/
public AvdInfo(String name,
//Synthetic comment -- @@ -116,7 +117,8 @@
mTargetHash = targetHash;
mTarget = target;
mAbiType = abiType;
        mProperties = properties == null ? Collections.<String, String>emptyMap()
                                         : Collections.unmodifiableMap(properties);
mStatus = status;
}

//Synthetic comment -- @@ -261,7 +263,9 @@
}

/**
     * Returns an unmodifiable map of properties for the AVD.
     * This can be empty but not null.
     * Callers must NOT try to modify this immutable map.
*/
public Map<String, String> getProperties() {
return mProperties;







