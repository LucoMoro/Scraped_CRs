/*Cancel inquiry before outgoing service connection.

Before connecting to a headset, if discovery is in progress,
discovery needs to be canceled, to avoid "Connection timeout"
error.

Change-Id:I5bdbb99ed5deee316cd6fd04bc06c4a82cb6eacdSigned-off-by: christian bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
//Synthetic comment -- index c0452b5..da88742 100755

//Synthetic comment -- @@ -579,6 +579,11 @@
ParcelUuid[] uuids = device.getUuids();
int type = BluetoothHandsfree.TYPE_UNKNOWN;
if (uuids != null) {

            if (mAdapter.isDiscovering()) {
                mAdapter.cancelDiscovery();
            }

if (BluetoothUuid.isUuidPresent(uuids, BluetoothUuid.Handsfree)) {
log("SDP UUID: TYPE_HANDSFREE");
type = BluetoothHandsfree.TYPE_HANDSFREE;







