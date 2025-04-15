/*Fix ANR when displaying the schedule bar

Multiple launch of the BluetoothOppTransferActivity can produce an ANR.
This activity must be unique then the Intent.FLAG_ACTIVITY_SINGLE_TOP
is added when BluetoothOppTransferActivity is called to have only one
instance of this activity.

Change-Id:I5fae59ed4497fa09d2b8b35e2a305d5d00f8001dAuthor: Christophe Bransiec <christophex.bransiec@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30539*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppReceiver.java b/src/com/android/bluetooth/opp/BluetoothOppReceiver.java
//Synthetic comment -- index a061fa8..3e170f3 100644

//Synthetic comment -- @@ -156,7 +156,7 @@
BluetoothOppUtility.updateVisibilityToHidden(context, uri);
} else {
Intent in = new Intent(context, BluetoothOppTransferActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
in.setData(uri);
context.startActivity(in);
}







