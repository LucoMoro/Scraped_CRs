/*Telephony: Fix issue in handling null alpha identifier

When alpha identifier is a provided by the SIM and
is a null data object, UE should not display
anything about the Sending but UE is showing
"Sending...".

As per 3GPP 11.14, if the alpha identifier is provided
by the SIM and is a null data object(i.e.length = '00'
and no value part), this is an indication that the ME
should not give any information to the user on the fact
that the ME is sending an Send SS/Send USSD/ Send DTMF.

Instead of returning null, retrieveAlphaId returns the
default message resulting in "Sending..." shown to the
user when it is not supposed to display anything.

This patch fixes the issue by returning null when alpha
identifier is provided and is a null data object.

Change-Id:Ia7bd690b8862a5df052a84bd6e7c94f4035baca7Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 60370*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/ValueParser.java b/src/java/com/android/internal/telephony/cat/ValueParser.java
//Synthetic comment -- index 5b1ae91..b1d9534 100644

//Synthetic comment -- @@ -285,7 +285,7 @@
throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
}
} else {
                return CatService.STK_DEFAULT;
}
} else {
return CatService.STK_DEFAULT;







