/*DISPLAY TEXT clr msg after delay, expects result code OK

Send RES_ID_TIMEOUT response if StkDialogActivity is sent to the
background due to another acitivty has replaced it as forground
activity. When a callback call interferes and closes the
StkDialogActivity, the session still needs to be ended. If the
session is not ended in a controlled way, STK will end up in a
wrong state. If STK is in wrong state it will be unable to
process subsequent callbacks.

Change-Id:Ib6519ce15225b1c7be83251c6461fb14a4680200*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..67673fc 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
*/
public class StkDialogActivity extends Activity implements View.OnClickListener {
// members
    private boolean mIsResponseSent = false;

TextMessage mTextMsg;

Handler mTimeoutHandler = new Handler() {
//Synthetic comment -- @@ -137,6 +139,14 @@
}

@Override
    public void onDestroy() {
        super.onDestroy();
        if (!mIsResponseSent) {
            sendResponse(StkAppService.RES_ID_TIMEOUT);
        }
    }

    @Override
public void onSaveInstanceState(Bundle outState) {
super.onSaveInstanceState(outState);

//Synthetic comment -- @@ -156,6 +166,7 @@
args.putInt(StkAppService.RES_ID, resId);
args.putBoolean(StkAppService.CONFIRMATION, confirmed);
startService(new Intent(this, StkAppService.class).putExtras(args));
        mIsResponseSent = true;
}

private void sendResponse(int resId) {







