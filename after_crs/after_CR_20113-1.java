/*Solved Android JUnit Run Configuration dialog doesn't show by error from SWT.

Change-Id:I99c5bc29e49b6049a52cc9b1a7c0a1d3b31748cc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigurationTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigurationTab.java
//Synthetic comment -- index cd22110..dda2c96 100644

//Synthetic comment -- @@ -479,7 +479,6 @@
public void dispose() {
super.dispose();
if (mTabIcon != null) {
mTabIcon = null;
}
mJavaElementLabelProvider.dispose();







