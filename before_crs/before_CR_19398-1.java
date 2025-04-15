/*Cherry-pick 0ec90f2b from tools_r8

Display output error in the exception's toString..

Change-Id:Idce557d2c172d27abaf474d14136f48d7c3396dc*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ExecResultException.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ExecResultException.java
//Synthetic comment -- index 37ab581..63a7a69 100644

//Synthetic comment -- @@ -44,4 +44,29 @@
public int getErrorCode() {
return mErrorCode;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ProguardResultException.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ProguardResultException.java
//Synthetic comment -- index bfc7e8b..54246b3 100644

//Synthetic comment -- @@ -26,4 +26,9 @@
ProguardResultException(int errorCode, String[] output) {
super(errorCode, output);
}
}







