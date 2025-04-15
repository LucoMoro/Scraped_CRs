/*Fix of compatibility with multifunction extUSB adapters.
Tested with HTC YC A300.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/HeadsetObserver.java b/services/java/com/android/server/HeadsetObserver.java
//Synthetic comment -- index c9066be..1e25622 100644

//Synthetic comment -- @@ -90,7 +90,7 @@

private synchronized final void update(String newName, int newState) {
if (newName != mHeadsetName || newState != mHeadsetState) {
            boolean isUnplug = (newState == 0 && mHeadsetState == 1);
mHeadsetName = newName;
mHeadsetState = newState;
mAudioRouteNeedsUpdate = true;
//Synthetic comment -- @@ -115,7 +115,7 @@
Intent intent = new Intent(Intent.ACTION_HEADSET_PLUG);
intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);

        intent.putExtra("state", mHeadsetState);
intent.putExtra("name", mHeadsetName);

// TODO: Should we require a permission?
//Synthetic comment -- @@ -129,7 +129,7 @@

private synchronized final void updateAudioRoute() {
if (mAudioRouteNeedsUpdate) {
            mAudioManager.setWiredHeadsetOn(mHeadsetState == 1);
mAudioRouteNeedsUpdate = false;
}
}







