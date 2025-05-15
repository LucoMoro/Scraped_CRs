
//<Beginning of snippet n. 0>


*/
/* package */ void restartRinger() {
if (DBG) log("restartRinger()...");
        if (isRinging() || mSilentRingerRequested) return;

final Call ringingCall = mCM.getFirstActiveRingingCall();
// Don't check ringingCall.isRinging() here, since that'll be true

//<End of snippet n. 0>








