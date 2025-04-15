/*Added missing USB_DEVICE_ATTACHED broadcast to running Activity

If an Activity programatically adds an ACTION_USB_DEVICE_ATTACHED intent,
it will never be delivered when the device is attached.  This change adds
the missing intent broadcast to running Activities.

File changes:
	modified:   services/java/com/android/server/usb/UsbSettingsManager.java

Change-Id:I767ee4d0765a7901c36e4c6f4aaf6583da2ac6f4Signed-off-by: Robin Cutshaw <robin.cutshaw@gmail.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/usb/UsbSettingsManager.java b/services/java/com/android/server/usb/UsbSettingsManager.java
//Synthetic comment -- index 0baafbb..c83bb90 100644

//Synthetic comment -- @@ -545,6 +545,10 @@
defaultPackage = mDevicePreferenceMap.get(new DeviceFilter(device));
}

        // Send broadcast to running activity with registered intent
        mContext.sendBroadcast(intent);

        // Start activity with registered intent
resolveActivity(intent, matches, defaultPackage, device, null);
}








