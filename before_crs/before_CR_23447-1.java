/*Improve error message when ADT is older than layoutlib.

Change-Id:I1d52201c58f9fb92a87fd42585b653bf37730032*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 1bdfd4e..6beef80 100644

//Synthetic comment -- @@ -264,7 +264,8 @@
// now load the layout lib bridge
LayoutLibrary layoutBridge =  LayoutLibrary.load(
mAndroidTarget.getPath(IAndroidTarget.LAYOUT_LIB),
                    AdtPlugin.getDefault());

progress.worked(1);









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 3d1c5db..0d6e5e3 100644

//Synthetic comment -- @@ -135,7 +135,7 @@
* @param log an optional log file.
* @return a {@link LayoutLibrary} object always.
*/
    public static LayoutLibrary load(String layoutLibJarOsPath, ILogger log) {

LoadStatus status = LoadStatus.LOADING;
String message = null;
//Synthetic comment -- @@ -195,7 +195,8 @@
int api = bridge.getApiLevel();
if (api > Bridge.API_CURRENT) {
status = LoadStatus.FAILED;
                            message = "LayoutLib is too recent. Update your tool!";
}
}
}







