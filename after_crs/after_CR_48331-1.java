/*Stk: Fix issue in DISPLAY TEXT-Sustained text

3GPP TS 31.124 test case 27.22.4.1.4/1 Display Text -
sustained text is failing.

As per the CAT specification, text shall
be sustained until a subsequent proactive command
is received containing display data. Existing
dialog showing the text is not replaced with
the newly received text message resulting in
conformance case failing.

waitForUserResponse not set correctly and also
Intent.FLAG_ACTIVITY_MULTIPLE_TASK is used
resulting in a starting of a new task even if there
is an existing task.

Fix the issue by setting the waitForUserResponse
to false on response not needed and on screen
not available. Also, Intent.FLAG_ACTIVITY_CLEAR_TASK
is used as one of the flags in creating the intent
for launching the dialog.

Change-Id:Ic614f96883aaa92696982ca41d85a02341bcb132Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61998*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 9dcd25b..48e3f67 100644

//Synthetic comment -- @@ -388,6 +388,7 @@
case DISPLAY_TEXT:
TextMessage msg = cmdMsg.geTextMessage();
responseNeeded = msg.responseNeeded;
            waitForUsersResponse = msg.responseNeeded;
if (lastSelectedItem != null) {
msg.title = lastSelectedItem;
} else if (mMainCmd != null){
//Synthetic comment -- @@ -644,7 +645,7 @@
private void launchTextDialog() {
Intent newIntent = new Intent(this, StkDialogActivity.class);
newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
| Intent.FLAG_ACTIVITY_NO_HISTORY
| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
| getFlagActivityNoUserAction(InitiatedByUserAction.unknown));







