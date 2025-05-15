
//<Beginning of snippet n. 0>


CommandsInterface.SERVICE_CLASS_DATA +
CommandsInterface.SERVICE_CLASS_FAX;

            if (!mPhone.mIsTheCurrentActivePhone) {
                Log.e(mLogTag, "Received message " + msg + "[" + msg.what
                        + "] while being destroyed. Ignoring.");
                return;
            }

switch (msg.what) {
case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
mState = null;
index = mIccCardStatus.getGsmUmtsSubscriptionAppIndex();
}

            IccCardApplication app;
            if (index < mIccCardStatus.CARD_MAX_APPS && index >= 0) {
                app = mIccCardStatus.getApplication(index);
            } else {
                Log.e(mLogTag, "[IccCard] Invalid Subscription Application index:" + index);
                return IccCard.State.ABSENT;
            }

if (app == null) {
Log.e(mLogTag, "[IccCard] Subscription Application in not present");

//<End of snippet n. 0>








