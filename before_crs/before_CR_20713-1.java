/*Add error message to log if layoutlib init fails

Change-Id:I5f61733c02002eca9400d5abbfc33393cabd1667*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 31475ee..2a75808 100644

//Synthetic comment -- @@ -252,7 +252,7 @@
*/
public synchronized LayoutLibrary getLayoutLibrary() {
if (mLayoutBridgeInit == false && mLayoutLibrary.getStatus() == LoadStatus.LOADED) {
            mLayoutLibrary.init(
mTarget.getProperties(),
new File(mTarget.getPath(IAndroidTarget.FONTS)),
getEnumValueMap(),
//Synthetic comment -- @@ -274,6 +274,10 @@
AdtPlugin.log(IStatus.WARNING, message);
}
});
mLayoutBridgeInit = true;
}








