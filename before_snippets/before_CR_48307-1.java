
//<Beginning of snippet n. 0>


*/
/* package */ void restartRinger() {
if (DBG) log("restartRinger()...");
        if (isRinging()) return;  // Already ringing; no need to restart.

final Call ringingCall = mCM.getFirstActiveRingingCall();
// Don't check ringingCall.isRinging() here, since that'll be true

//<End of snippet n. 0>








