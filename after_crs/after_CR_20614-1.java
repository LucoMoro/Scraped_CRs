/*Fix computeSdkVersion initialization

Change-Id:I0c8cc49eb403f207845ee8167872b6c318ee3b82*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f396a71..9dff02f 100644

//Synthetic comment -- @@ -922,6 +922,8 @@
IAndroidTarget oldTarget = getRenderingTarget();
preRenderingTargetChangeCleanUp(oldTarget);

            computeSdkVersion();

// get the project target
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
//Synthetic comment -- @@ -2396,20 +2398,19 @@
if (manifestFile != null) {
try {
Object value = AndroidManifest.getMinSdkVersion(manifestFile);
                mMinSdkVersion = 1; // Default case if missing
if (value instanceof Integer) {
mMinSdkVersion = ((Integer) value).intValue();
} else if (value instanceof String) {
                    // handle codename, only if we can resolve it.
                    if (Sdk.getCurrent() != null) {
                        IAndroidTarget target = Sdk.getCurrent().getTargetFromHashString(
                                "android-" + value); //$NON-NLS-1$
                        if (target != null) {
                            // codename future API level is current api + 1
                            mMinSdkVersion = target.getVersion().getApiLevel() + 1;
                        }
}
}

Integer i = AndroidManifest.getTargetSdkVersion(manifestFile);







