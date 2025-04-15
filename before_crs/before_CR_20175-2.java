/*Phone: Send RING,CLIP,CIEV updates only after SLC establishment.

Unsolicited RING, CLIP and CIEV notifications were being sent during
service level connection establishment. Violates BT spec. Added code
 to disable.

Change-Id:I94778b4da6560e8fe013fa9179680ea8a65e23cf*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 92caffe..2d51eef 100644

//Synthetic comment -- @@ -421,11 +421,24 @@
}

private boolean sendUpdate() {
            return isHeadsetConnected() && mHeadsetType == TYPE_HANDSFREE && mIndicatorsEnabled;
}

private boolean sendClipUpdate() {
            return isHeadsetConnected() && mHeadsetType == TYPE_HANDSFREE && mClip;
}

private void stopRing() {
//Synthetic comment -- @@ -909,7 +922,7 @@


private AtCommandResult ring() {
            if (!mIgnoreRing && !mStopRing && mCM.getFirstActiveRingingCall().isRinging()) {
AtCommandResult result = new AtCommandResult(AtCommandResult.UNSOLICITED);
result.addResponse("RING");
if (sendClipUpdate()) {







