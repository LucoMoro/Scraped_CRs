/*DISPLAY TEXT clr msg after delay, expects result code OK

Send RES_ID_TIMEOUT response if StkDialogActivity is sent to the
background due to another acitivty has replaced it as forground
activity. When a callback call interferes and closes the
StkDialogActivity, the session still needs to be ended. If the
session is not ended in a controlled way, STK will end up in a
wrong state. If STK is in wrong state it will be unable to
process subsequent callbacks.

The sendResponse call is now moved to onStop() since it showed
that the system will not call onDestroy() in some scenarios.
This happened while repeating the test for 20 or more times.

Change-Id:Ib6519ce15225b1c7be83251c6461fb14a4680200*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..59cda9a 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
// members
TextMessage mTextMsg;

Handler mTimeoutHandler = new Handler() {
@Override
public void handleMessage(Message msg) {
//Synthetic comment -- @@ -137,6 +138,14 @@
}

@Override
public void onSaveInstanceState(Bundle outState) {
super.onSaveInstanceState(outState);

//Synthetic comment -- @@ -156,6 +165,7 @@
args.putInt(StkAppService.RES_ID, resId);
args.putBoolean(StkAppService.CONFIRMATION, confirmed);
startService(new Intent(this, StkAppService.class).putExtras(args));
}

private void sendResponse(int resId) {







