//<Beginning of snippet n. 0>
if (mPhone != null && mPhone.isActive()) {
    index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
    
    if (index >= 0 && index < mIccCardStatus.getApplicationCount()) {
        IccCardApplication app = mIccCardStatus.getApplication(index);
        
        if (app == null) {
            Log.e(mLogTag, "[IccCard] Subscription Application is not present. Phone state: " + mPhone.getState() + ", Index: " + index);
        }
    } else {
        Log.e(mLogTag, "[IccCard] Invalid index for Subscription Application: " + index);
    }
} else {
    Log.e(mLogTag, "[IccCard] Phone is not active or null.");
}
//<End of snippet n. 0>