/*Reorder packages in SDK Updater

SDK Bughttp://b.android.com/7920Change-Id:I94946beda47067ff93ce288b36d525f8fb1840f2*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index d03c9a9..1608887 100755

//Synthetic comment -- @@ -486,10 +486,11 @@
* - Tools.
* - Docs.
* - Platform n preview
* - Platform n
* - Platform n-1
     * - Samples packages.
     * - Add-on based on n preview
     * - Add-on based on n
* - Add-on based on n-1
* - Extra packages.
*/
//Synthetic comment -- @@ -503,26 +504,31 @@
* Computes the score for each package used by {@link #compareTo(Package)}.
*/
private int sortingScore() {
        // up to 31 bits (for signed stuff)
        int type = 0;             // max type=5 => 3 bits
        int rev = getRevision();  // 12 bits... 4095
        int offset = 0;           // 16 bits...
if (this instanceof ToolPackage) {
            type = 5;
} else if (this instanceof DocPackage) {
            type = 4;
        } else if (this instanceof PlatformPackage) {
            type = 3;
        } else if (this instanceof SamplePackage) {
type = 2;
        } else if (this instanceof AddonPackage) {
type = 1;
} else {
// extras and everything else
type = 0;
}

        if (this instanceof IPackageVersion) {
            AndroidVersion v = ((IPackageVersion) this).getVersion();
            offset = v.getApiLevel();
            offset = offset * 2 + (v.isPreview() ? 1 : 0);
        }

int n = (type << 28) + (offset << 12) + rev;
return 0 - n;
}







