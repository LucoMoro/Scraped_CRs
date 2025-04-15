/*Force disconnect of all devices when adb connection is lost.

Bug 2873317

Change-Id:Idc56be6dbd8912ee463295ddfe6f3b0bbf0b5125*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index 21869af..a975cd4 100644

//Synthetic comment -- @@ -219,6 +219,20 @@
// we can safely ignore that one.
}
mMainAdbConnection = null;

                // remove all devices from list
                // because we are going to call mServer.deviceDisconnected which will acquire this
                // lock we lock it first, so that the AndroidDebugBridge lock is always locked
                // first.
                synchronized (AndroidDebugBridge.getLock()) {
                    synchronized (mDevices) {
                        for (int n = mDevices.size() - 1; n >= 0; n--) {
                            Device device = mDevices.get(0);
                            removeDevice(device);
                            mServer.deviceDisconnected(device);
                        }
                    }
                }
}
}
}







