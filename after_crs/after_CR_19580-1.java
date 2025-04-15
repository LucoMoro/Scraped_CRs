/*Bugfix in Android JUnit run configuration page in Eclipse

Bugfix for bug 12411:
code.google.com/p/android/issues/detail?id=12411
When a user leaves the Android JUnit run configuration page,
an icon is disposed. Upon returning to the page, the plugin
tries to reuse the icon, which is disposed, causing an
IllegalArgumentException. This bug is fixed by this commit.

Change-Id:I260b6a5cd75192abc7aa051d6f141956a391c0e4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigurationTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigurationTab.java
//Synthetic comment -- index cd22110..c3f319f 100644

//Synthetic comment -- @@ -478,10 +478,7 @@
@Override
public void dispose() {
super.dispose();
        mTabIcon = null;
mJavaElementLabelProvider.dispose();
}








