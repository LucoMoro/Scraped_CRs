/*simcard: With Kazakh Beeline SIM card, there isn't STK menu

IccFileHandler.java is modified to fix an erroneous EF_IMG file path
information. The other is CommandParamsFactory.java file, which is modified
to ignore the result of reading EF_IMG file.

This Beeline SIM card returned an error status word when the mobile device
accessed the EF_IMG file in this SIM card.
(I think this Beeline SIM card does not support EF_IMG file.)
As a result, This error result stoped processing the setup menu proactive
command, and there was no STK menu.

So, I modified the CommandParamsFactory.java file to ignore the result of
EF_IMG file read operation.
After applying this modification,  The STK menu was successfully displayed.
(This issue was resolved by these modified files.)

I think there will be no critical problem even if the EF_IMG(Icon image) file
is ignored.

Bug: 7585989

Change-Id:I16e3c077779811e3d4c46cd6e003771f3e1efde2*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 98ab17b..fd4e3cc 100644

//Synthetic comment -- @@ -88,6 +88,8 @@
static protected final int EVENT_READ_IMG_DONE = 9;
/** Finished retrieving icon data; post result. */
static protected final int EVENT_READ_ICON_DONE = 10;
    /** Finished retrieving size of record for EFimg now. */
    static protected final int EVENT_GET_RECORD_SIZE_IMG_DONE = 11;

// member variables
protected final CommandsInterface mCi;
//Synthetic comment -- @@ -162,14 +164,14 @@
*
*/
public void loadEFImgLinearFixed(int recordNum, Message onLoaded) {
        Message response = obtainMessage(EVENT_GET_RECORD_SIZE_IMG_DONE,
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

        mCi.iccIOForApp(COMMAND_GET_RESPONSE, IccConstants.EF_IMG,
                    getEFPath(IccConstants.EF_IMG), recordNum,
                    READ_RECORD_MODE_ABSOLUTE, GET_RESPONSE_EF_IMG_SIZE_BYTES,
                    null, null, mAid, response);
}

/**
//Synthetic comment -- @@ -257,8 +259,17 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        logd("IccFileHandler: loadEFImgTransparent fileid = " + fileid
                + " filePath = " + getEFPath(fileid) + " highOffset = " + highOffset
                + " lowOffset = " + lowOffset + " length = " + length);
        /*
         * Per TS 31.102, for displaying of Icon, under
         * DF Telecom and DF Graphics , EF instance(s) (4FXX,transparent files)
         * are present. The possible image file identifiers (EF instance) for
         * EF img ( 4F20, linear fixed file) are : 4F01 ... 4F05.
         */
        mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
                highOffset, lowOffset, length, null, null, mAid, response);
}

/**
//Synthetic comment -- @@ -322,6 +333,42 @@

try {
switch (msg.what) {
            case EVENT_GET_RECORD_SIZE_IMG_DONE:
                logd("IccFileHandler: get record size img done");
                ar = (AsyncResult) msg.obj;
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
                lc.recordSize = data[RESPONSE_DATA_RECORD_LENGTH] & 0xFF;

                if ((TYPE_EF != data[RESPONSE_DATA_FILE_TYPE]) ||
                    (EF_TYPE_LINEAR_FIXED != data[RESPONSE_DATA_STRUCTURE])) {
                    loge("IccFileHandler: File type mismatch: Throw Exception");
                    throw new IccFileTypeMismatch();
                }

                logd("IccFileHandler: read EF IMG");
                mCi.iccIOForApp(COMMAND_READ_RECORD, lc.efid, getEFPath(lc.efid),
                        lc.recordNum,
                        READ_RECORD_MODE_ABSOLUTE,
                        lc.recordSize, null, null, mAid,
                        obtainMessage(EVENT_READ_IMG_DONE, IccConstants.EF_IMG, 0, response));
                break;

case EVENT_READ_IMG_DONE:
ar = (AsyncResult) msg.obj;
lc = (LoadLinearFixedContext) ar.userObj;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index a554012..9de8399 100644

//Synthetic comment -- @@ -203,7 +203,7 @@
int iconIndex = 0;

if (data == null) {
            return ResultCode.OK;
}
switch(mIconLoadState) {
case LOAD_SINGLE_ICON:







