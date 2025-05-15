//<Beginning of snippet n. 0>
if (isPhoneConnected()) {
    index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
    
    if (index >= 0 && index < mIccCardStatus.getApplicationsLength()) {
        IccCardApplication app = mIccCardStatus.getApplication(index);

        if (app != null) {
            // Process application
        } else {
            Log.e(mLogTag, "[IccCard] Subscription Application not present");
        }
    } else {
        Log.e(mLogTag, "[IccCard] Subscription App index out of bounds");
    }
} else {
    Log.e(mLogTag, "[IccCard] No active phone connection");
}
//<End of snippet n. 0>