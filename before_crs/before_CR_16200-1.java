/*Force disconnect of all devices when adb connection is lost.

Bug 2873317

Change-Id:Idc56be6dbd8912ee463295ddfe6f3b0bbf0b5125*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index 21869af..fa24131 100644

//Synthetic comment -- @@ -219,6 +219,17 @@
// we can safely ignore that one.
}
mMainAdbConnection = null;
}
}
}







