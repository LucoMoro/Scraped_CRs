/*This fixes crash caused by null pointer

The crash is caused by a null pointer.
This patch prevents the dereferencement of the pointer when null.

Change-Id:I7a5aa23342bf7e6ba072c8bd2533ae574e2d0150Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 42641*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/DeviceProfilesSettings.java b/src/com/android/settings/bluetooth/DeviceProfilesSettings.java
//Synthetic comment -- index 67d2258..b124166 100755

//Synthetic comment -- @@ -143,7 +143,9 @@
@Override
public void onSaveInstanceState(Bundle outState) {
super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_DEVICE, mCachedDevice.getDevice());
}

@Override







