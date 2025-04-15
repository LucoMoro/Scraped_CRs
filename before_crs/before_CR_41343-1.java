/*UsbSetting: The behavior is not right when enable/disable MTP/PTP with Accessory board connected.

Add accessory flag to track accessory status. Disable MTP/PTP button
when Usb is working in accessory mode.

Change-Id:I3edcd2a7eb281db3bb9d345429643b77a237812eAuthor: Wu, Hao <hao.wu@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27200*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/UsbSettings.java b/src/com/android/settings/deviceinfo/UsbSettings.java
//Synthetic comment -- index af279e8..b8290fc 100644

//Synthetic comment -- @@ -49,9 +49,15 @@
private UsbManager mUsbManager;
private CheckBoxPreference mMtp;
private CheckBoxPreference mPtp;

private final BroadcastReceiver mStateReceiver = new BroadcastReceiver() {
public void onReceive(Context content, Intent intent) {
updateToggles(mUsbManager.getDefaultFunction());
}
};
//Synthetic comment -- @@ -106,6 +112,18 @@
mMtp.setChecked(false);
mPtp.setChecked(false);
}
}

@Override







