//<Beginning of snippet n. 0>

if (isPhoneActive()) {
    if (mIccCardStatus != null) {
        int index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
        if (index >= 0 && index < mIccCardStatus.getApplications().size()) {
            IccCardApplication app = mIccCardStatus.getApplication(index);
            if (app == null) {
                Log.e(mLogTag, "[IccCard] Subscription Application is not present. Index: " + index);
            }
        } else {
            Log.e(mLogTag, "[IccCard] Invalid index: " + index);
        }
    } else {
        Log.e(mLogTag, "[IccCard] mIccCardStatus is null.");
    }
} // Adding the check if phone is active

//<End of snippet n. 0>