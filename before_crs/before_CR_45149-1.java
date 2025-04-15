/*Stk: Timer not getting cancelled after user response

After user response, the timer should be cancelled.
Else the timer would expire and sends an untimely
Terminal response with RES_ID_TIMEOUT.

Change-Id:I81beb0aea2f4275780bbe1d5e57fb69772d23072*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..4cee6f4 100644

//Synthetic comment -- @@ -99,13 +99,14 @@

public void onClick(View v) {
String input = null;

switch (v.getId()) {
case OK_BUTTON:
sendResponse(StkAppService.RES_ID_CONFIRM, true);
finish();
break;
case CANCEL_BUTTON:
sendResponse(StkAppService.RES_ID_CONFIRM, false);
finish();
break;
//Synthetic comment -- @@ -116,6 +117,7 @@
public boolean onKeyDown(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_BACK:
sendResponse(StkAppService.RES_ID_BACKWARD);
finish();
break;








//Synthetic comment -- diff --git a/src/com/android/stk/StkInputActivity.java b/src/com/android/stk/StkInputActivity.java
//Synthetic comment -- index b6228fb..4813911 100644

//Synthetic comment -- @@ -103,6 +103,7 @@
break;
}

sendResponse(StkAppService.RES_ID_INPUT, input, false);
finish();
}
//Synthetic comment -- @@ -173,6 +174,7 @@
public boolean onKeyDown(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_BACK:
sendResponse(StkAppService.RES_ID_BACKWARD, null, false);
finish();
break;
//Synthetic comment -- @@ -219,10 +221,12 @@
public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId()) {
case StkApp.MENU_ID_END_SESSION:
sendResponse(StkAppService.RES_ID_END_SESSION);
finish();
return true;
case StkApp.MENU_ID_HELP:
sendResponse(StkAppService.RES_ID_INPUT, "", true);
finish();
return true;








//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index aac1a12..43b5dbb 100644

//Synthetic comment -- @@ -112,6 +112,7 @@
if (item == null) {
return;
}
sendResponse(StkAppService.RES_ID_MENU_SELECTION, item.id, false);
mAcceptUsersInput = false;
mProgressView.setVisibility(View.VISIBLE);







