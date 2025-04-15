/*ADT tools location: traceview is a .bat

This prevents adb from starting under windows, since
adt is trying to verify the presence of traceview.exe
when it's actually traceview.bat.

Change-Id:I259d90adb17b0b91beb114319cf88392812fc264*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index a26374c..ce5a0f6 100644

//Synthetic comment -- @@ -118,7 +118,7 @@

public final static String FN_TRACEVIEW =
(SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) ?
            "traceview.exe" : "traceview"; //$NON-NLS-1$ //$NON-NLS-2$

public final static String FN_HPROF_CONV =
(SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) ?







