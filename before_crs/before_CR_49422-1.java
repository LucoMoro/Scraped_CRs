/*Phone: call screen not shown for 2nd call

Incoming call screen not shown for the following case
1. Receive an incoming call
2. Accept the incoming call
3. Receive another incoming call

Issue:
If the duration between Step 2 and Step 3 is
less than 500msec, then incoming call screen
for 2nd call is not shown.

Rootcause:
Ugly hack done as part of the commit
a1bfb5406fa20be8351e272a089c582946e86fc4 is
triggering this.

Change-Id:I8cb9f7481f2e48524332c363ee9cd04b35c66ed6Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 6789*/
//Synthetic comment -- diff --git a/src/com/android/phone/InCallTouchUi.java b/src/com/android/phone/InCallTouchUi.java
//Synthetic comment -- index 986765e..c01377b 100644

//Synthetic comment -- @@ -256,20 +256,6 @@
showIncomingCallControls = true;
}

            // Ugly hack to cover up slow response from the radio:
            // if we get an updateState() call immediately after answering/rejecting a call
            // (via onTrigger()), *don't* show the incoming call
            // UI even if the phone is still in the RINGING state.
            // This covers up a slow response from the radio for some actions.
            // To detect that situation, we are using "500 msec" heuristics.
            //
            // Watch out: we should *not* rely on this behavior when "instant text response" action
            // has been chosen. See also onTrigger() for why.
            long now = SystemClock.uptimeMillis();
            if (now < mLastIncomingCallActionTime + 500) {
                log("updateState: Too soon after last action; not drawing!");
                showIncomingCallControls = false;
            }

// b/6765896
// If the glowview triggers two hits of the respond-via-sms gadget in







