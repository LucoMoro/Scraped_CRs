/*Discard TLV objects with '00' padding

TLV objects should not be padded. Still, some SIM use padding
when sending "Items Next Action Indicator" tag. This causes
Stk to throw execption and not being installed. Now, such
objects are discarded and Stk will be installed.

Change-Id:I5ef23ad1241b8aaceba4f2fc99bdb2a7c714d444*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..5514c6e 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
FILE_LIST(0x12),
HELP_REQUEST(0x15),
DEFAULT_TEXT(0x17),
  NEXT_ACTION_INDICATOR(0x18),
EVENT_LIST(0x19),
ICON_ID(0x1e),
ITEM_ICON_ID_LIST(0x1f),








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/ComprehensionTlv.java b/telephony/java/com/android/internal/telephony/cat/ComprehensionTlv.java
//Synthetic comment -- index 99f662d..0a576df 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
private int mLength;
private int mValueIndex;
private byte[] mRawValue;
    private static int sPrevTag;

/**
* Constructor. Private on purpose. Use
//Synthetic comment -- @@ -88,6 +89,9 @@
int endIndex = data.length;
while (startIndex < endIndex) {
ComprehensionTlv ctlv = ComprehensionTlv.decode(data, startIndex);
            if (ctlv == null) {
                break;
            }
items.add(ctlv);
startIndex = ctlv.mValueIndex + ctlv.mLength;
}
//Synthetic comment -- @@ -115,6 +119,12 @@
int temp = data[curIndex++] & 0xff;
switch (temp) {
case 0:
                // Avoid exception if the SIM card pads NEXT_ACTION_INDICATOR command
                // with '00'. Rare (but is happening in the wild) since padding is not
                // really allowed by spec.
                if (sPrevTag == ComprehensionTlvTag.NEXT_ACTION_INDICATOR.value()) {
                    return null;
                }
case 0xff:
case 0x80:
throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
//Synthetic comment -- @@ -133,6 +143,7 @@
tag &= ~0x80;
break;
}
            sPrevTag = tag;

/* length */
int length;







