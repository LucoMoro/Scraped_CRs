/*Ethernet:delete link up update in StartMonitoring

In StartMonitoring,ethernet cable maybe not plugged in. So the link up state
shouldn't be updated according to interface config here. Only when recieving
link state change we can ensure ethernet plugged in, so then link up state
can follow up the interface config.
Issue:System boot with no ethernet cable plugged in, then plug in ethernet cable
For the first time, it will fail to process the link state change for it not
satisfy "mLinkUp != up" condition.

Change-Id:Id29fd92ed70566d1c5fb2e5a24a4f51dd3ac13b7*/
//Synthetic comment -- diff --git a/core/java/android/net/EthernetDataTracker.java b/core/java/android/net/EthernetDataTracker.java
//Synthetic comment -- index 3a06dc0..58ba493 100644

//Synthetic comment -- @@ -227,7 +227,6 @@
mIface = iface;
mNMService.setInterfaceUp(iface);
InterfaceConfiguration config = mNMService.getInterfaceConfig(iface);
                    mLinkUp = config.hasFlag("up");
if (config != null && mHwAddr == null) {
mHwAddr = config.getHardwareAddress();
if (mHwAddr != null) {







