/*EF_IMG structure is linear fixed.
So we need get the record size firstly, then read record.

use MF_SIM+DF_TELECOM+DF_GRAPHICS as the root path to get/read the EFs
in the Dedicated File DF_GRAPHICS contain graphical information.

pass all TCs(related to icon support A) defined in TS 131 124.

Change-Id:I2d568bf9ae752d6601c464c1c25e2e9cbb498a8aSigned-off-by: luolj <luolj@marvell.com>*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
old mode 100644
new mode 100755
//Synthetic comment -- index 98ab17b..9a1d18f

//Synthetic comment -- @@ -88,6 +88,8 @@
static protected final int EVENT_READ_IMG_DONE = 9;
/** Finished retrieving icon data; post result. */
static protected final int EVENT_READ_ICON_DONE = 10;

// member variables
protected final CommandsInterface mCi;
//Synthetic comment -- @@ -162,14 +164,13 @@
*
*/
public void loadEFImgLinearFixed(int recordNum, Message onLoaded) {
        Message response = obtainMessage(EVENT_READ_IMG_DONE,
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

        // TODO(): Verify when path changes are done.
        mCi.iccIOForApp(COMMAND_GET_RESPONSE, IccConstants.EF_IMG, "img",
                recordNum, READ_RECORD_MODE_ABSOLUTE,
                GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, mAid, response);
}

/**
//Synthetic comment -- @@ -257,7 +258,7 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, "img", highOffset, lowOffset,
length, null, null, mAid, response);
}

//Synthetic comment -- @@ -331,6 +332,8 @@
iccException = result.getException();
if (iccException != null) {
sendResult(response, result.payload, ar.exception);
}
break;
case EVENT_READ_ICON_DONE:
//Synthetic comment -- @@ -341,6 +344,8 @@
iccException = result.getException();
if (iccException != null) {
sendResult(response, result.payload, ar.exception);
}
break;
case EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE:
//Synthetic comment -- @@ -422,6 +427,47 @@
lc.recordSize, null, null, mAid,
obtainMessage(EVENT_READ_RECORD_DONE, lc));
break;
case EVENT_GET_BINARY_SIZE_DONE:
ar = (AsyncResult)msg.obj;
response = (Message) ar.userObj;







