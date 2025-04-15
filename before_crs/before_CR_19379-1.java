/*Display output error in the exception's toString..

Change-Id:I050a663fc760f0b0c98a7d8dbcfa141168e30030*/
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







