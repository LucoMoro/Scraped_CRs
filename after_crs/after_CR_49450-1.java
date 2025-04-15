/*stk: fix issue in Comprehension Required bit setting

The CR (Comprehension Required) bit is set to 1 in the Android code.
It must be set to 0 according to the 3GPP spec.
See ETSI 102.223 chapter 6.8.

This patch sets the CR bit to 0.

Change-Id:I4ed380c94c040333b23d7de097e63b236c8e7d25Author: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Guillaume Lucas <guillaumex.lucas@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 2902 13417*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 4eacae3..d6c3453 100644

//Synthetic comment -- @@ -476,14 +476,14 @@
buf.write(0x00); // place holder

// device identities
        tag = ComprehensionTlvTag.DEVICE_IDENTITIES.value();
buf.write(tag);
buf.write(0x02); // length
buf.write(DEV_ID_KEYPAD); // source device id
buf.write(DEV_ID_UICC); // destination device id

// item identifier
        tag = ComprehensionTlvTag.ITEM_ID.value();
buf.write(tag);
buf.write(0x01); // length
buf.write(menuId); // menu identifier chosen
//Synthetic comment -- @@ -519,13 +519,13 @@
buf.write(0x00); // place holder, assume length < 128.

// event list
        tag = ComprehensionTlvTag.EVENT_LIST.value();
buf.write(tag);
buf.write(0x01); // length
buf.write(event); // event value

// device identities
        tag = ComprehensionTlvTag.DEVICE_IDENTITIES.value();
buf.write(tag);
buf.write(0x02); // length
buf.write(sourceId); // source device id








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/ResponseData.java b/src/java/com/android/internal/telephony/cat/ResponseData.java
//Synthetic comment -- index 814ec0d..97ee8f1 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
@Override
public void format(ByteArrayOutputStream buf) {
// Item identifier object
        int tag = ComprehensionTlvTag.ITEM_ID.value();
buf.write(tag); // tag
buf.write(1); // length
buf.write(id); // identifier of item chosen
//Synthetic comment -- @@ -174,7 +174,7 @@
}

// Text string object
        int tag = ComprehensionTlvTag.LANGUAGE.value();
buf.write(tag); // tag

byte[] data;
//Synthetic comment -- @@ -212,7 +212,7 @@
}

// DTTZ object
        int tag = CommandType.PROVIDE_LOCAL_INFORMATION.value();
buf.write(tag); // tag

byte[] data = new byte[8];







