/*Wrong terminal response for proactive command length errors

This fix is at least reported by type approval, can not
guarantee that it exists "in the wild" however the
specification is clear.

If the total lengths of the SIMPLE-TLV data objects are not
consistent with the length given in the BER-TLV data object,
then the whole BER-TLV data object shall be rejected. The
result field in the TERMINAL RESPONSE shall have the error
condition "Command data not understood by ME".

Change-Id:I128442ea4755679b0e5524a31c01b3c690e45ab0*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/BerTlv.java b/src/java/com/android/internal/telephony/cat/BerTlv.java
//Synthetic comment -- index 095e65b..c264c11 100644

//Synthetic comment -- @@ -28,15 +28,17 @@
class BerTlv {
private int mTag = BER_UNKNOWN_TAG;
private List<ComprehensionTlv> mCompTlvs = null;
    private boolean mLengthValid = true;

public static final int BER_UNKNOWN_TAG             = 0x00;
public static final int BER_PROACTIVE_COMMAND_TAG   = 0xd0;
public static final int BER_MENU_SELECTION_TAG      = 0xd3;
public static final int BER_EVENT_DOWNLOAD_TAG      = 0xd6;

    private BerTlv(int tag, List<ComprehensionTlv> ctlvs, boolean lengthValid) {
mTag = tag;
mCompTlvs = ctlvs;
        mLengthValid = lengthValid;
}

/**
//Synthetic comment -- @@ -58,6 +60,15 @@
}

/**
     * Gets if the length of the BER-TLV object is valid
     *
     * @return if length valid
     */
     public boolean isLengthValid() {
         return mLengthValid;
     }

    /**
* Decodes a BER-TLV object from a byte array.
*
* @param data A byte array to decode from
//Synthetic comment -- @@ -68,6 +79,7 @@
int curIndex = 0;
int endIndex = data.length;
int tag, length = 0;
        boolean isLengthValid = true;

try {
/* tag */
//Synthetic comment -- @@ -118,6 +130,32 @@
List<ComprehensionTlv> ctlvs = ComprehensionTlv.decodeMany(data,
curIndex);

        if (tag == BER_PROACTIVE_COMMAND_TAG) {
            int totalLength = 0;
            for (ComprehensionTlv item : ctlvs) {
                int itemLength = item.getLength();
                if (itemLength >= 0x80 && itemLength <= 0xFF) {
                    totalLength += itemLength + 3; //3: 'tag'(1 byte) and 'length'(2 bytes).
                } else if (itemLength >= 0 && itemLength < 0x80) {
                    totalLength += itemLength + 2; //2: 'tag'(1 byte) and 'length'(1 byte).
                } else {
                    isLengthValid = false;
                    break;
                }
            }

            // According to 3gpp11.14, chapter 6.10.6 "Length errors",

            // If the total lengths of the SIMPLE-TLV data objects are not
            // consistent with the length given in the BER-TLV data object,
            // then the whole BER-TLV data object shall be rejected. The
            // result field in the TERMINAL RESPONSE shall have the error
            // condition "Command data not understood by ME".
            if (length != totalLength) {
                isLengthValid = false;
            }
        }

        return new BerTlv(tag, ctlvs, isLengthValid);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index ed30279..6e3b19f 100644

//Synthetic comment -- @@ -124,6 +124,13 @@
return;
}

        // proactive command length is incorrect.
        if (!berTlv.isLengthValid()) {
            mCmdParams = new CommandParams(cmdDet);
            sendCmdParams(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            return;
        }

try {
switch (cmdType) {
case SET_UP_MENU:







