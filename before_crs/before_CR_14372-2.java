/*Reorder packages in SDK Updater

SDK Bughttp://b.android.com/7920Change-Id:I94946beda47067ff93ce288b36d525f8fb1840f2*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index d03c9a9..1608887 100755

//Synthetic comment -- @@ -486,10 +486,11 @@
* - Tools.
* - Docs.
* - Platform n preview
     * - Add-on based on n preview
* - Platform n
     * - Add-on based on n
* - Platform n-1
* - Add-on based on n-1
* - Extra packages.
*/
//Synthetic comment -- @@ -503,26 +504,31 @@
* Computes the score for each package used by {@link #compareTo(Package)}.
*/
private int sortingScore() {
        int type = 0;
        int rev = getRevision();
        int offset = 0;
if (this instanceof ToolPackage) {
            type = 3;
} else if (this instanceof DocPackage) {
type = 2;
        } else if (this instanceof PlatformPackage || this instanceof AddonPackage ||
                this instanceof SamplePackage) {
type = 1;
            AndroidVersion v = ((IPackageVersion) this).getVersion();
            offset = v.getApiLevel();
            offset = offset * 2 + (v.isPreview() ? 1 : 0);
            offset = offset * 2 + ((this instanceof AddonPackage) ? 0 :
                    ((this instanceof SamplePackage) ? 1 : 2));
} else {
// extras and everything else
type = 0;
}

int n = (type << 28) + (offset << 12) + rev;
return 0 - n;
}







