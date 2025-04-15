/*fix display incoherence when pairing

The issue was caused by the fact that the list of
devices already discovered was systematically cleared
when a bond state changed event was received.
This fix removes the call to the clear list method.

Change-Id:I9e3644fd74353cb8b8eeda4357d998f68285dd11Author: Christophe Bransiec <christophex.bransiec@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34349*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/DeviceListPreferenceFragment.java b/src/com/android/settings/bluetooth/DeviceListPreferenceFragment.java
//Synthetic comment -- index 90f8de5..53cc349 100644

//Synthetic comment -- @@ -109,7 +109,6 @@
super.onPause();
if (mLocalManager == null) return;

        removeAllDevices();
mLocalManager.setForegroundActivity(null);
mLocalManager.getEventManager().unregisterCallback(this);
}







