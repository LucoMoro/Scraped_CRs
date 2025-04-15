/*Make ddmlib recognize a device in adb 'recovery' state.

Change-Id:I3401b2efb9e0da69539ed84304b06de872ba77aa*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index c2e2c16..e408fdd 100644

//Synthetic comment -- @@ -54,7 +54,8 @@
public static enum DeviceState {
BOOTLOADER("bootloader"), //$NON-NLS-1$
OFFLINE("offline"), //$NON-NLS-1$
        ONLINE("device"), //$NON-NLS-1$
        RECOVERY("recovery"); //$NON-NLS-1$

private String mState;








