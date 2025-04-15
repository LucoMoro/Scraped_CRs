/*EthernetDataTracker: indicate link up based on flags

Indicate link up state based on flags/interface up, and not on IP address.
This is for ethernet interfaces that already exists.

Change-Id:Ib342d519c483bbb2dfa08cfac2c0c1a288cee7c0Signed-off-by: Vishal Mahaveer <vishalm@ti.com>*/
//Synthetic comment -- diff --git a/core/java/android/net/EthernetDataTracker.java b/core/java/android/net/EthernetDataTracker.java
//Synthetic comment -- index 28bd289..0cc78c9 100644

//Synthetic comment -- @@ -223,7 +223,7 @@
mIface = iface;
mNMService.setInterfaceUp(iface);
InterfaceConfiguration config = mNMService.getInterfaceConfig(iface);
                    mLinkUp = config.isActive();
if (config != null && mHwAddr == null) {
mHwAddr = config.getHardwareAddress();
if (mHwAddr != null) {







