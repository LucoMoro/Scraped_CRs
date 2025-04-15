/*ADT tools location: traceview is a .bat

This prevents adb from starting under windows, since
adt is trying to verify the presence of traceview.exe
when it's actually traceview.bat.

Change-Id:I095c9f9d992c0a888eede6b1d585a683d0f8f196*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index 5c3ce0c..04abe34 100644

//Synthetic comment -- @@ -118,7 +118,7 @@

public final static String FN_TRACEVIEW =
(SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) ?
            "traceview.exe" : "traceview"; //$NON-NLS-1$ //$NON-NLS-2$

public final static String FN_HPROF_CONV =
(SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) ?







