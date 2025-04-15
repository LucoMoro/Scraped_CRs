/*Add exception handle for startObexSession()

Throw IllegalStateException in startObexSession to handle
exception in inbound transfer, but it was sometimes throwed
(if ACTION_UUID intent arrives twice and transfer fails at
first) without being catched in outbound tranfer .

Change-Id:I4524bcc3bc668ffd55bb1eba717a20d79cedda23Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34625*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransfer.java b/src/com/android/bluetooth/opp/BluetoothOppTransfer.java
//Synthetic comment -- index ec9c7db..b69d8c7 100755

//Synthetic comment -- @@ -184,7 +184,12 @@
if (V) Log.v(TAG, "Transfer receive RFCOMM_CONNECTED msg");
mConnectThread = null;
mTransport = (ObexTransport)msg.obj;
                    startObexSession();

break;
case BluetoothOppObexSession.MSG_SHARE_COMPLETE:







