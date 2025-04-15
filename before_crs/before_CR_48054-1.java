/*Stk: request user confirmation on OPEN CHANNEL cmd

Currently, user confirmation not requested
on OPEN CHANNEL proactive command.

With this patch, user confirmation requested.
If the user accepts, then the channel will
be opended. If the user does not accept the
channel set-up, the terminal informs the UICC
using TERMINAL RESPONSE (User did not accept
the proactive command) and the operation is
aborted.

Change-Id:Iaaff5613dd61af4c46e14cda22e929c25b3428baAuthor: Andre Amegah <andrex.amegah@intel.com>
Signed-off-by: Andre Amegah <andrex.amegah@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 52207*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 9dcd25b..58ecf0d 100644

//Synthetic comment -- @@ -442,7 +442,7 @@
launchToneDialog();
break;
case OPEN_CHANNEL:
            launchOpenChannelDialog();
break;
case CLOSE_CHANNEL:
case RECEIVE_DATA:
//Synthetic comment -- @@ -550,6 +550,11 @@
launchCallMsg();
}
break;
}
break;
case RES_ID_DONE:







