Fix the lock screen bypass issue reported inhttp://forum.xda-developers.com/showthread.php?t=436767.




diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
index 250d2d4..1731206 100644

@@ -1325,11 +1325,10 @@
// If the user is holding the menu key code, then we are
// going to boot into safe mode.
ActivityManagerNative.getDefault().enterSafeMode();
}
            // tell the keyguard
            mKeyguardMediator.onSystemReady();
            android.os.SystemProperties.set("dev.bootcomplete", "1"); 
} catch (RemoteException e) {
// Ignore
}







