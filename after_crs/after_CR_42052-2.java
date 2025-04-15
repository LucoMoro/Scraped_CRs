/*EF_IMG structure is linear fixed.
So we need get the record size firstly, then read record.

use MF_SIM+DF_TELECOM+DF_GRAPHICS as the root path to get/read the EFs
in the Dedicated File DF_GRAPHICS contain graphical information.

pass all TCs(related to icon support A) defined in TS 131 124.

Change-Id:I2d568bf9ae752d6601c464c1c25e2e9cbb498a8aSigned-off-by: luolj <luolj@marvell.com>*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
old mode 100644
new mode 100755
//Synthetic comment -- index 98ab17b..fc4f2c3

//Synthetic comment -- @@ -88,6 +88,8 @@
static protected final int EVENT_READ_IMG_DONE = 9;
/** Finished retrieving icon data; post result. */
static protected final int EVENT_READ_ICON_DONE = 10;
    /** Finished retrieving size of Img records for linear-fixed EF; now load. */
    static protected final int EVENT_GET_IMG_SIZE_DONE = 11;

// member variables
protected final CommandsInterface mCi;
//Synthetic comment -- @@ -162,14 +164,13 @@
*
*/
public void loadEFImgLinearFixed(int recordNum, Message onLoaded) {
        Message response = obtainMessage(EVENT_GET_IMG_SIZE_DONE,
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

        mCi.iccIOForApp(COMMAND_GET_RESPONSE, IccConstants.EF_IMG, getEFPath(EF_IMG),
                0, 0,
                GET_RESPONSE_EF_SIZE_BYTES, null, null, mAid, response);
}

/**
//Synthetic comment -- @@ -257,7 +258,7 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, getEFPath(fileid), highOffset, lowOffset,
length, null, null, mAid, response);
}

//Synthetic comment -- @@ -331,6 +332,8 @@
iccException = result.getException();
if (iccException != null) {
sendResult(response, result.payload, ar.exception);
                } else {
                    sendResult(response, result.payload, null);
}
break;
case EVENT_READ_ICON_DONE:
//Synthetic comment -- @@ -341,6 +344,8 @@
iccException = result.getException();
if (iccException != null) {
sendResult(response, result.payload, ar.exception);
                } else {
                    sendResult(response, result.payload, null);
}
break;
case EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE:
//Synthetic comment -- @@ -422,6 +427,47 @@
lc.recordSize, null, null, mAid,
obtainMessage(EVENT_READ_RECORD_DONE, lc));
break;
             case EVENT_GET_IMG_SIZE_DONE:
                 ar = (AsyncResult)msg.obj;
                 lc = (LoadLinearFixedContext) ar.userObj;
                 result = (IccIoResult) ar.result;
                 response = lc.onLoaded;

                 if (ar.exception != null) {
                     sendResult(response, null, ar.exception);
                     break;
                 }

                 iccException = result.getException();

                 if (iccException != null) {
                     sendResult(response, null, iccException);
                     break;
                 }

                 data = result.payload;
                 fileid = lc.efid;
                 recordNum = lc.recordNum;

                 if (TYPE_EF != data[RESPONSE_DATA_FILE_TYPE]) {
                     throw new IccFileTypeMismatch();
                 }

                 if (EF_TYPE_LINEAR_FIXED != data[RESPONSE_DATA_STRUCTURE]) {
                     throw new IccFileTypeMismatch();
                 }

                 lc.recordSize = data[RESPONSE_DATA_RECORD_LENGTH] & 0xFF;

                 size = ((data[RESPONSE_DATA_FILE_SIZE_1] & 0xff) << 8)
                     + (data[RESPONSE_DATA_FILE_SIZE_2] & 0xff);

                 mCi.iccIOForApp(COMMAND_READ_RECORD, lc.efid, getEFPath(lc.efid),
                         lc.recordNum,
                         READ_RECORD_MODE_ABSOLUTE,
                         lc.recordSize, null, null, mAid,
                         obtainMessage(EVENT_READ_IMG_DONE, lc));
                break;
case EVENT_GET_BINARY_SIZE_DONE:
ar = (AsyncResult)msg.obj;
response = (Message) ar.userObj;







