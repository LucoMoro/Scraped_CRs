/*Stk: Support SET UP CALL-Self explanatory icon

SET UP CALL - display of self explanatory basic
icon during call setup is not correctly managed.
UE displays text "Set up call Icon 3.4.2" and
no icon during call set up.

Icon in call setup message is not handled.

This patch adds the support for displaying
self explanatory basic icon during SAT initiated
call setup.

Change-Id:I98c72775abbadd8a9280d17eca788eedcfdbb5bfAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61206*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 9dcd25b..dff87b9 100644

//Synthetic comment -- @@ -744,15 +744,39 @@

private void launchCallMsg() {
TextMessage msg = mCurrentCmd.getCallSettings().callMsg;
        if (msg.text == null || msg.text.length() == 0) {
return;
}
        msg.title = lastSelectedItem;

        Toast toast = Toast.makeText(mContext.getApplicationContext(), msg.text,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
}

private void launchIdleText() {







