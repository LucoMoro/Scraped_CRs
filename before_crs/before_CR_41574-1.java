/*Phone: fix the issue cannot answer the 3rd Call

Two calls are established on the DUT: one is on hold and one
active. DUT receives a third call.
User can hear the 3rd incoming call tone, but cannot answer the
call after sliding the call button. Caller side displays
'call connected'.
After several second, 'Unable to switch call' displayed, and all
the 3 calls are ended automatically.

As per the android UI design, when the user accepts
Call C(Call A - On Hold, Call B - Active), Call B(Active call)
will be disconnected and Call C will be accepted.

As per the 3GPP 22.030 specification, above behavior
can be achieved by just calling hangupForegroundResumeBackground
on the CallManager object. No need to explicitly
issue answerCall request.

Change-Id:I0e01ea37f254f5e0ec4e4bbe0c7d79074ecaa49bAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 20106*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 616bd40..160930b 100644

//Synthetic comment -- @@ -539,13 +539,6 @@
return false;
}

        // since hangupActiveCall() also accepts the ringing call
        // check if the ringing call was already answered or not
        // only answer it when the call still is ringing
        if (ringing.isRinging()) {
            return answerCall(ringing);
        }

return true;
}








