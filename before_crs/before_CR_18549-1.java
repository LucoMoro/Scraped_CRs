/*Hiding Browser Edit Text interface on incoming call

Open browser in main menu, tap address field to enter
the input field, long tap the input field, the “Edit text”
list pops up, receive an incoming call, the incoming call
interface cannot displayed, only ringtone is heard.

Added changes to hide the Edit Text Dialog when an incoming
call is received. This will keep the incoming call UI in
foreground.

Change-Id:Ic9c825c14a00b09673e97e865e0c8df27216d4f4*/
//Synthetic comment -- diff --git a/core/java/android/inputmethodservice/InputMethodService.java b/core/java/android/inputmethodservice/InputMethodService.java
//Synthetic comment -- index 1a261d3..3bfa464 100644

//Synthetic comment -- @@ -364,6 +364,11 @@
*/
public void hideSoftInput(int flags, ResultReceiver resultReceiver) {
if (DEBUG) Log.v(TAG, "hideSoftInput()");
boolean wasVis = isInputViewShown();
mShowInputFlags = 0;
mShowInputRequested = false;








//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 5a995ae..9184155 100644

//Synthetic comment -- @@ -1597,6 +1597,9 @@
mSwitchingDialog = null;
}

mDialogBuilder = null;
mItems = null;
mIms = null;







