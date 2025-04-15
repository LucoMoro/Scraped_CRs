/*Add readFromParcel() method to android.graphics.Region

Adding this method allows the android.graphics.Region type to be used
for an 'out' parameter in an AIDL interface.

This patch also fixes the Javadoc comment for writeToParcel() which
suggests incorrectly that pixel data is written to the parcel along
with the region data.

Change-Id:I192cabc65e17046b3c1335846fcb8d6ceb6f7e4e*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/Region.java b/graphics/java/android/graphics/Region.java
//Synthetic comment -- index 2b080aa..d84bf3a 100644

//Synthetic comment -- @@ -305,8 +305,8 @@
}

/**
     * Write the region and its pixels to the parcel. The region can be
     * rebuilt from the parcel by calling CREATOR.createFromParcel().
* @param p    Parcel object to write the region data into
*/
public void writeToParcel(Parcel p, int flags) {
//Synthetic comment -- @@ -315,6 +315,19 @@
}
}

@Override
public boolean equals(Object obj) {
if (obj == null || !(obj instanceof Region)) {
//Synthetic comment -- @@ -371,5 +384,5 @@

private static native boolean nativeEquals(int native_r1, int native_r2);

    private final int mNativeRegion;
}







