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
//Synthetic comment -- index 98ab17b..b980a40 100644

//Synthetic comment -- @@ -88,6 +88,8 @@
static protected final int EVENT_READ_IMG_DONE = 9;
/** Finished retrieving icon data; post result. */
static protected final int EVENT_READ_ICON_DONE = 10;

// member variables
protected final CommandsInterface mCi;
//Synthetic comment -- @@ -162,14 +164,14 @@
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
//Synthetic comment -- @@ -257,8 +259,18 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, "img", highOffset, lowOffset,
                length, null, null, mAid, response);
}

/**
//Synthetic comment -- @@ -322,6 +334,43 @@

try {
switch (msg.what) {
case EVENT_READ_IMG_DONE:
ar = (AsyncResult) msg.obj;
lc = (LoadLinearFixedContext) ar.userObj;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index a554012..9de8399 100644

//Synthetic comment -- @@ -203,7 +203,7 @@
int iconIndex = 0;

if (data == null) {
            return ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
}
switch(mIconLoadState) {
case LOAD_SINGLE_ICON:







