//<Beginning of snippet n. 0>


CommandsInterface.SERVICE_CLASS_DATA +
CommandsInterface.SERVICE_CLASS_FAX;

switch (msg.what) {
case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
    mState = null;
    if (mIccCardStatus.isActivePhone() && mIccCardStatus.isRadioAvailable()) {
        index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
        if (index >= 0 && index < mIccCardStatus.getApplicationCount()) {
            IccCardApplication app = mIccCardStatus.getApplication(index);
            if (app == null) {
                Log.e(mLogTag, "[IccCard] Subscription Application in not present");
            }
        } else {
            Log.e(mLogTag, "[IccCard] Invalid subscription app index: " + index);
        }
    } else {
        Log.e(mLogTag, "[IccCard] Phone is not active or radio is not available.");
    }
    break;

//<End of snippet n. 0>